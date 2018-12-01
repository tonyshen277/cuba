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

package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.javascriptcomponent.CubaJavaScriptComponentState;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.Dependency.Type;
import com.vaadin.ui.HasDependencies;
import com.vaadin.ui.JavaScriptFunction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CubaJavaScriptComponent extends AbstractJavaScriptComponent implements HasDependencies {

    protected Map<Type, List<String>> dependencies;

    @Override
    protected CubaJavaScriptComponentState getState() {
        return (CubaJavaScriptComponentState) super.getState();
    }

    @Override
    protected CubaJavaScriptComponentState getState(boolean markAsDirty) {
        return (CubaJavaScriptComponentState) super.getState(markAsDirty);
    }

    @Nullable
    @Override
    public Map<Type, List<String>> getDependencies() {
        return dependencies;
    }

    public List<String> getDependencies(Type type) {
        if (dependencies == null) {
            return Collections.emptyList();
        }

        return nullToEmpty(dependencies.get(type));
    }

    protected List<String> nullToEmpty(List<String> dependencies) {
        return dependencies == null ? Collections.emptyList() : dependencies;
    }

    public void setDependencies(Map<Type, List<String>> dependencies) {
        this.dependencies = dependencies;
    }

    public void setDependencies(Type type, String... dependencies) {
        setDependencies(type, dependencies != null
                ? new ArrayList<>(Arrays.asList(dependencies))
                : null);
    }

    public void setDependencies(Type type, List<String> dependencies) {
        if (this.dependencies == null) {
            this.dependencies = new HashMap<>();
        }

        if (dependencies != null) {
            this.dependencies.put(type, dependencies);
        } else {
            this.dependencies.remove(type);
        }
    }

    public String getInitializingFunction() {
        return getState(false).initializingFunction;
    }

    public void setInitializationFunction(String initializingFunction) {
        if (!Objects.equals(getInitializingFunction(), initializingFunction)) {
            getState().initializingFunction = initializingFunction;
        }
    }

    public Map<String, Object> getStateObject() {
        return getState(false).data;
    }

    public void setStateObject(Map<String, Object> state) {
        getState().data = state;
    }

    @Override
    public void addFunction(String functionName, JavaScriptFunction function) {
        super.addFunction(functionName, function);
    }

    @Override
    public void callFunction(String name, Object... arguments) {
        super.callFunction(name, arguments);
    }
}
