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

package com.autsia.codefreeze.impl.filters;


import com.autsia.codefreeze.CodeFreeze;
import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * CGLIB filter for defining which Callback will be used for intercepting particular method
 */
public class ImmutabilityCallbackFilter implements CallbackFilter {

    private static final int EQUALS_CALLBACK_INDEX = 0;
    private static final int EXCEPTION_CALLBACK_INDEX = 1;
    private static final int DELEGATING_CALLBACK_INDEX = 2;
    private static final int FREEZING_CALLBACK_INDEX = 3;

    private final CodeFreeze codeFreeze;
    private final Class typeToProxify;

    /**
     * Initializes ImmutabilityCallbackFilter with with CodeFreeze service and type to proxify
     *
     * @param codeFreeze    CodeFreeze service instance
     * @param typeToProxify Type to proxify with this Filter
     */
    public ImmutabilityCallbackFilter(CodeFreeze codeFreeze, Class typeToProxify) {
        this.codeFreeze = codeFreeze;
        this.typeToProxify = typeToProxify;
    }

    @Override
    public int accept(Method method) {
        Class<?> returnType = method.getReturnType();

        if ("equals".equals(method.getName())) {
            return EQUALS_CALLBACK_INDEX;
        }

        if (Void.class.equals(returnType)
                || method.getName().startsWith("set") || method.getName().startsWith("add") || method.getName().startsWith("remove")) {
            return EXCEPTION_CALLBACK_INDEX;
        }

        if (!codeFreeze.isEnhanceable(returnType) || method.getDeclaringClass().equals(Object.class)) {
            return DELEGATING_CALLBACK_INDEX;
        }

        if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
            return FREEZING_CALLBACK_INDEX;
        }

        return DELEGATING_CALLBACK_INDEX;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImmutabilityCallbackFilter that = (ImmutabilityCallbackFilter) o;

        return typeToProxify.equals(that.typeToProxify);

    }

    @Override
    public int hashCode() {
        return typeToProxify.hashCode();
    }

}
