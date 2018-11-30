/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.gui.components;

import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface JavaScriptComponent extends Component {

    String NAME = "javaScriptComponent";

    Map<DependencyType, Set<String>> getDependencies();

    Set<String> getDependencies(DependencyType type);

    void setDependencies(Map<DependencyType, Set<String>> dependencies);

    void setDependencies(DependencyType type, String... dependencies);

    void setDependencies(DependencyType type, Set<String> dependencies);

    void setInitializationFunction(String initializingFunction);

    Map<String, Object> getState();

    void setState(Map<String, Object> state);

    void addFunction(String name, JavaScriptCallbackFunction function);

    void callFunction(String name, Object... arguments);

    class JavaScriptCallbackEvent extends EventObject {

        protected List<?> arguments;

        /**
         * Constructs a prototypical Event.
         *
         * @param source The object on which the Event initially occurred.
         * @throws IllegalArgumentException if source is null.
         */
        public JavaScriptCallbackEvent(JavaScriptComponent source, List<?> arguments) {
            super(source);
            this.arguments = arguments;
        }

        @Override
        public JavaScriptComponent getSource() {
            return (JavaScriptComponent) super.getSource();
        }

        public List<?> getArguments() {
            return arguments;
        }
    }

    interface JavaScriptCallbackFunction {
        void onCall(JavaScriptCallbackEvent event);
    }

    enum DependencyType {
        STYLESHEET,
        JAVASCRIPT,
        HTML_IMPORT
    }

}
