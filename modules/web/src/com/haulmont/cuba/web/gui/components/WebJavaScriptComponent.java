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

import com.haulmont.cuba.web.widgets.CubaJavaScriptComponent;
import com.vaadin.ui.Dependency;
import com.vaadin.ui.JavaScriptFunction;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class WebJavaScriptComponent<T> extends WebAbstractComponent<CubaJavaScriptComponent<T>>
        implements JavaScriptComponent<T> {

    public WebJavaScriptComponent() {
        component = createComponent();
        initComponent(component);
    }

    protected CubaJavaScriptComponent<T> createComponent() {
        return new CubaJavaScriptComponent<>();
    }

    protected void initComponent(CubaJavaScriptComponent<T> component) {
    }

    @Override
    public Map<DependencyType, List<String>> getDependencies() {
        Map<Dependency.Type, List<String>> dependencies = component.getDependencies();
        if (dependencies.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<DependencyType, List<String>> dependenciesToReturn = new HashMap<>();
        for (Map.Entry<Dependency.Type, List<String>> entry : dependencies.entrySet()) {
            dependenciesToReturn.put(WebWrapperUtils.toDependencyType(entry.getKey()), entry.getValue());
        }

        return dependenciesToReturn;
    }

    @Override
    public List<String> getDependencies(DependencyType type) {
        return component.getDependencies(WebWrapperUtils.toVaadinDependencyType(type));
    }

    @Override
    public void setDependencies(Map<DependencyType, List<String>> dependencies) {
        if (dependencies != null) {

            Map<Dependency.Type, List<String>> dependenciesToSet = new HashMap<>();

            for (Map.Entry<DependencyType, List<String>> entry : dependencies.entrySet()) {
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
    public void setDependencies(DependencyType type, List<String> dependencies) {
        component.setDependencies(WebWrapperUtils.toVaadinDependencyType(type), dependencies);
    }

    @Override
    public String getInitFunctionName() {
        return component.getInitFunctionName();
    }

    @Override
    public void setInitFunctionName(String initFunctionName) {
        component.setInitFunctionName(initFunctionName);
    }

    @Override
    public T getState() {
        return component.getStateData();
    }

    @Override
    public void setState(T state) {
        component.setStateData(state);
    }

    @Override
    public void addFunction(String name, Consumer<JavaScriptCallbackEvent> function) {
        component.addFunction(name, (JavaScriptFunction) arguments -> {
            JavaScriptCallbackEvent event = new JavaScriptCallbackEvent(this, arguments);
            function.accept(event);
        });
    }

    @Override
    public void callFunction(String name, Object... arguments) {
        component.callFunction(name, arguments);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return component.isRequiredIndicatorVisible();
    }

    @Override
    public void setRequiredIndicatorVisible(boolean visible) {
        component.setRequiredIndicatorVisible(visible);
    }
}
