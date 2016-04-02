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

package com.autsia.codefreeze;

/**
 * Service for obtaining immutable versions of beans
 */
public interface CodeFreeze {

    /**
     * Converts input bean to immutable one
     *
     * @param bean Bean to make immutable
     * @param <T>  Bean type
     * @return Immutable version of bean
     */
    <T> T freeze(T bean);

    /**
     * Checks if type can by proxified by CGLIB
     *
     * @param type Type to check
     * @return True if input type can be proxified, false otherwise
     */
    boolean isEnhanceable(Class<?> type);

}
