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

package com.autsia.codefreeze.impl.callbacks;

import com.autsia.codefreeze.CodeFreeze;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CGLIB method interceptor which wraps returned by the method object into immutable proxy
 */
public class FreezingMethodInterceptor implements MethodInterceptor {

    private CodeFreeze codeFreeze;
    private Object delegate;
    private ConcurrentHashMap<Method, Object> immutableBeans = new ConcurrentHashMap<>();

    /**
     * Initializes FreezingMethodInterceptor with CodeFreeze service and delegated object
     *
     * @param codeFreeze CodeFreeze service instance
     * @param delegate   Delegated object
     */
    public FreezingMethodInterceptor(CodeFreeze codeFreeze, Object delegate) {
        this.codeFreeze = codeFreeze;
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (immutableBeans.containsKey(method)) {
            return immutableBeans.get(method);
        }
        Object value = codeFreeze.freeze(method.invoke(delegate, args));
        if (value != null) {
            immutableBeans.putIfAbsent(method, value);
        }
        return value;
    }

}