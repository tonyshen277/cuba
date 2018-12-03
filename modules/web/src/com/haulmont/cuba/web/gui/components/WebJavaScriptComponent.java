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

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.haulmont.cuba.gui.components.JavaScriptComponent;
import com.haulmont.cuba.web.gui.components.serialization.DateJsonSerializer;
import com.haulmont.cuba.web.widgets.CubaJavaScriptComponent;
import com.vaadin.ui.Dependency;
import com.vaadin.ui.JavaScriptFunction;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class WebJavaScriptComponent extends WebAbstractComponent<CubaJavaScriptComponent>
        implements JavaScriptComponent {

    protected final static Gson sharedGson;

    static {
        // GSON is thread safe so we can use shared GSON instance
        sharedGson = createSharedGsonBuilder().create();
    }

    protected static GsonBuilder createSharedGsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                Expose expose = f.getAnnotation(Expose.class);
                return expose != null && !expose.serialize();
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });

        setDefaultProperties(builder);
        return builder;
    }

    protected static void setDefaultProperties(GsonBuilder builder) {
        builder.registerTypeHierarchyAdapter(Date.class, new DateJsonSerializer());
    }

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
    public String getInitializationFunctionName() {
        return component.getInitializingFunctionName();
    }

    @Override
    public void setInitializationFunctionName(String initializingFunctionName) {
        component.setInitializationFunctionName(initializingFunctionName);
    }

    @Override
    public Object getState() {
        return getState(Object.class);
    }

    @Override
    public <T> T getState(Class<T> type) {
        return fromJson(component.getStateData(), type);
    }

    @Override
    public void setState(Object state) {
        String json = getStateSerializer().toJson(state);
        component.setStateData(json);
    }

    @Override
    public void addFunction(String name, Consumer<JavaScriptCallbackEvent> function) {
        component.addFunction(name, (JavaScriptFunction) arguments -> {
            List list = fromJson(arguments.toJson(), List.class);
            JavaScriptCallbackEvent event = new JavaScriptCallbackEvent(this, list);
            function.accept(event);
        });
    }

    @Override
    public void callFunction(String name, Object... arguments) {
        component.callFunction(name, arguments);
    }

    @Override
    public Gson getStateSerializer() {
        return gson != null ? gson : sharedGson;
    }

    @Override
    public void setStateSerializer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return component.isRequiredIndicatorVisible();
    }

    @Override
    public void setRequiredIndicatorVisible(boolean visible) {
        component.setRequiredIndicatorVisible(visible);
    }

    protected <T> T fromJson(String json, Class<T> type) {
        return getStateSerializer().fromJson(json, type);
    }
}
