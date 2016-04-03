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
import java.text.MessageFormat;

/**
 * CGLIB method interceptor for throwing exception on mutators calling
 */
public class ExceptionMethodInterceptor implements MethodInterceptor {

    private Object delegate;
    private boolean active;

    /**
     * Initializes ExceptionMethodInterceptor with delegated object, new instance is not active
     *
     * @param delegate Delegated object
     */
    public ExceptionMethodInterceptor(Object delegate) {
        this.delegate = delegate;
        this.active = false;
    }

    /**
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        // If interceptor is not yet active, it doesn't throw an exception, but just delegate method call to real object
        if (active) {
            throw new UnsupportedOperationException(MessageFormat.format("%s class is immutable: mutators execution is not allowed.", obj.getClass().getSimpleName()));
        }
        return method.invoke(delegate, args);
    }


    public boolean isActive() {
        return active;
    }

    /**
     * Activates or deactivated UnsupportedOperationExceptionMethodInterceptor:
     * once it'd been activated, it can't be deactivated
     *
     * @param active Status of UnsupportedOperationExceptionMethodInterceptor to set
     */
    public void setActive(boolean active) {
        if (!isActive()) {
            this.active = active;
        }
    }

}
