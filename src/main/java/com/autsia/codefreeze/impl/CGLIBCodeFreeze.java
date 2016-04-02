/*
 *    Copyright 2016 Dmytro Titov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.autsia.codefreeze.impl;

import com.autsia.codefreeze.CodeFreeze;
import com.autsia.codefreeze.impl.callbacks.DelegatingMethodInterceptor;
import com.autsia.codefreeze.impl.callbacks.EqualsMethodInterceptor;
import com.autsia.codefreeze.impl.callbacks.ExceptionMethodInterceptor;
import com.autsia.codefreeze.impl.callbacks.FreezingMethodInterceptor;
import com.autsia.codefreeze.impl.filters.ImmutabilityCallbackFilter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CGLIB-based implementation
 */
public class CGLIBCodeFreeze implements CodeFreeze {

    private ConcurrentHashMap<Class, Factory> factories = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Class, Boolean> enhanceableMap = new ConcurrentHashMap<>();

    /**
     * {@inheritDoc}
     */
    public <T> T freeze(T bean) {
        if (bean == null) {
            return null;
        }

        try {
            if (bean instanceof List) {
                return proxifyList((List) bean);
            }

            if (bean instanceof Set) {
                return proxifySet((Set) bean);
            }

            if (bean instanceof Map) {
                return proxifyMap((Map) bean);
            }

            return proxifyBean(bean);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEnhanceable(Class<?> type) {
        if (enhanceableMap.containsKey(type)) {
            return enhanceableMap.get(type);
        }
        boolean isEnhanceable = !Modifier.isFinal(type.getModifiers()) && hasParameterlessConstructor(type);
        enhanceableMap.putIfAbsent(type, isEnhanceable);
        return isEnhanceable;
    }

    private boolean hasParameterlessConstructor(Class<?> returnType) {
        return Collection.class.isAssignableFrom(returnType)
                || Map.class.isAssignableFrom(returnType)
                || Arrays.stream(returnType.getConstructors()).anyMatch(c -> c.getGenericParameterTypes().length == 0);
    }


    @SuppressWarnings("unchecked")
    private <T> T proxifyList(List list) throws InstantiationException, IllegalAccessException {
        ImmutableList.Builder<Object> builder = ImmutableList.builder();
        list.stream().forEach(bean -> builder.add(freeze(bean)));
        return (T) builder.build();
    }

    @SuppressWarnings("unchecked")
    private <T> T proxifySet(Set set) throws InstantiationException, IllegalAccessException {
        ImmutableSet.Builder<Object> builder = ImmutableSet.builder();
        set.stream().forEach(bean -> builder.add(freeze(bean)));
        return (T) builder.build();
    }

    @SuppressWarnings("unchecked")
    private <T> T proxifyMap(Map map) throws InstantiationException, IllegalAccessException {
        ImmutableMap.Builder<Object, Object> builder = ImmutableMap.builder();
        map.keySet().stream().forEach(key -> builder.put(freeze(key), freeze(map.get(key))));
        return (T) builder.build();
    }

    @SuppressWarnings("unchecked")
    private <T> T proxifyBean(T bean) throws IllegalAccessException, InstantiationException {
        if (!isEnhanceable(bean.getClass())) {
            return bean;
        }
        Factory factory = getFactory(bean.getClass());
        Callback[] callbacks = getCallbacks(bean);
        T newInstance = (T) factory.newInstance(callbacks);
        ExceptionMethodInterceptor unsupportedOperationExceptionCallback = (ExceptionMethodInterceptor) callbacks[1];
        // By default the ExceptionMethodInterceptor is not active to allow calling setters in class constructor
        // After class instance creation it should be immediately activated
        unsupportedOperationExceptionCallback.setActive(true);
        return newInstance;
    }

    private <T> Callback[] getCallbacks(T bean) {
        return new Callback[]{
                new EqualsMethodInterceptor(bean),
                new ExceptionMethodInterceptor(bean),
                new DelegatingMethodInterceptor(bean),
                new FreezingMethodInterceptor(this, bean)
        };
    }

    private Factory getFactory(Class<?> classToProxify) throws IllegalAccessException, InstantiationException {
        if (factories.containsKey(classToProxify)) {
            return factories.get(classToProxify);
        }
        Factory factory = createFactory(classToProxify);
        factories.putIfAbsent(classToProxify, factory);
        return factory;
    }

    private Factory createFactory(Class<?> classToProxify) throws IllegalAccessException, InstantiationException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(classToProxify);
        enhancer.setCallbackFilter(new ImmutabilityCallbackFilter(this, classToProxify));
        enhancer.setCallbackTypes(new Class[]{
                EqualsMethodInterceptor.class,
                ExceptionMethodInterceptor.class,
                DelegatingMethodInterceptor.class,
                FreezingMethodInterceptor.class
        });
        Class proxyClass = enhancer.createClass();
        return (Factory) proxyClass.newInstance();
    }

}
