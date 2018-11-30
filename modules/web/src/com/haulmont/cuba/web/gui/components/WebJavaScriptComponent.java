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

package com.haulmont.cuba.web.gui.components;

import com.google.gson.Gson;
import com.haulmont.cuba.gui.components.JavaScriptComponent;
import com.haulmont.cuba.web.widgets.CubaJavaScriptComponent;
import com.vaadin.ui.Dependency;
import com.vaadin.ui.JavaScriptFunction;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WebJavaScriptComponent extends WebAbstractComponent<CubaJavaScriptComponent>
        implements JavaScriptComponent {

    protected Gson gson;

    public WebJavaScriptComponent() {
        component = createComponent();
        initComponent(component);
    }

    protected CubaJavaScriptComponent createComponent() {
        return new CubaJavaScriptComponent();
    }

    protected void initComponent(CubaJavaScriptComponent component) {
    }

    @Nullable
    @Override
    public Map<DependencyType, Set<String>> getDependencies() {
        Map<Dependency.Type, Set<String>> dependencies = component.getDependencies();
        if (dependencies == null) {
            return null;
        }

        Map<DependencyType, Set<String>> dependenciesToReturn = new HashMap<>();

        for (Map.Entry<Dependency.Type, Set<String>> entry : dependencies.entrySet()) {
            dependenciesToReturn.put(WebWrapperUtils.toDependencyType(entry.getKey()), entry.getValue());
        }

        return dependenciesToReturn;
    }

    @Override
    public Set<String> getDependencies(DependencyType type) {
        return component.getDependencies(WebWrapperUtils.toVaadinDependencyType(type));
    }

    @Override
    public void setDependencies(Map<DependencyType, Set<String>> dependencies) {
        if (dependencies != null) {

            Map<Dependency.Type, Set<String>> dependenciesToSet = new HashMap<>();

            for (Map.Entry<DependencyType, Set<String>> entry : dependencies.entrySet()) {
                dependenciesToSet.put(WebWrapperUtils.toVaadinDependencyType(entry.getKey()), entry.getValue());
            }
            component.setDependencies(dependenciesToSet);
        } else {
            component.setDependencies(null);
        }
    }

    @Override
    public void setDependencies(DependencyType type, String... dependencies) {
        component.setDependencies(WebWrapperUtils.toVaadinDependencyType(type), dependencies);
    }

    @Override
    public void setDependencies(DependencyType type, Set<String> dependencies) {
        component.setDependencies(WebWrapperUtils.toVaadinDependencyType(type), dependencies);
    }

    @Override
    public void setInitializationFunction(String initializingFunction) {
        component.setInitializationFunction(initializingFunction);
    }

    @Override
    public Map<String, Object> getState() {
        return component.getStateObject();
    }

    @Override
    public void setState(Map<String, Object> state) {
        component.setStateObject(state);
    }

    @Override
    public void addFunction(String name, JavaScriptCallbackFunction function) {
        component.addFunction(name, (JavaScriptFunction) arguments -> {
            List list = fromJson(arguments.toJson(), List.class);
            JavaScriptCallbackEvent event = new JavaScriptCallbackEvent(this, list);
            function.onCall(event);
        });
    }

    protected <T> T fromJson(String json, Class<T> type) {
        if (gson == null) {
            gson = new Gson();
        }

        return gson.fromJson(json, type);
    }

    @Override
    public void callFunction(String name, Object... arguments) {
        component.callFunction(name, arguments);
    }
}
