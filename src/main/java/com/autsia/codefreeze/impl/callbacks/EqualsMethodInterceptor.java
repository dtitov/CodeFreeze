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

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB method interceptor for equals() method
 */
public class EqualsMethodInterceptor implements MethodInterceptor {

    private Object delegate;

    /**
     * Initializes EqualsMethodInterceptor with delegated object
     *
     * @param delegate Delegated object
     */
    public EqualsMethodInterceptor(Object delegate) {
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object objectToCompare = args[0];
        if (objectToCompare == null) {
            return delegate == null;
        }
        return objectToCompare.equals(delegate);
    }

}
