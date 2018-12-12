/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.chile.core.model.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.chile.core.model.utils.LambdaMethodsCache;
import com.haulmont.chile.core.model.utils.MethodsCache;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.sys.CubaEnhancedSetGet;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractInstance implements Instance {

    protected static final int PROPERTY_CHANGE_LISTENERS_INITIAL_CAPACITY = 4;

    protected transient Collection<WeakReference<PropertyChangeListener>> __propertyChangeListeners;

    private static transient Map<Class, MethodsCache> methodCacheMap = new ConcurrentHashMap<>();
    private static transient Map<Class, LambdaMethodsCache> lambdaMethodsCache = new ConcurrentHashMap<>();

    protected void propertyChanged(String s, Object prev, Object curr) {
        if (__propertyChangeListeners != null) {
            for (Object referenceObject : __propertyChangeListeners.toArray()) {
                @SuppressWarnings("unchecked")
                WeakReference<PropertyChangeListener> reference = (WeakReference<PropertyChangeListener>) referenceObject;

                PropertyChangeListener listener = reference.get();
                if (listener == null) {
                    __propertyChangeListeners.remove(reference);
                } else {
                    listener.propertyChanged(new PropertyChangeEvent(this, s, prev, curr));
                }
            }
        }
    }

    /**
     * @deprecated Use {@link MetadataTools#getInstanceName(com.haulmont.chile.core.model.Instance)} instead.
     */
    @Deprecated
    @Override
    public String getInstanceName() {
        return InstanceUtils.getInstanceName(this);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (__propertyChangeListeners == null) {
            __propertyChangeListeners = new ArrayList<>(PROPERTY_CHANGE_LISTENERS_INITIAL_CAPACITY);
        }
        __propertyChangeListeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (__propertyChangeListeners != null) {
            for (Iterator<WeakReference<PropertyChangeListener>> it = __propertyChangeListeners.iterator(); it.hasNext(); ) {
                PropertyChangeListener iteratorListener = it.next().get();
                if (iteratorListener == null || iteratorListener.equals(listener)) {
                    it.remove();
                }
            }
        }
    }

    @Override
    public void removeAllListeners() {
        if (__propertyChangeListeners != null) {
            __propertyChangeListeners.clear();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue(String name) {
        //if (this instanceof CubaEnhancedSetGet)
        return (T) //getMethodsCache().invokeGetter(this, name);
                ((CubaEnhancedSetGet) this).getValueNative(name);
        //else
        // throw new RuntimeException("This object ins not an instance of CubaEnhancedSetGet");
        //getLambdaMethodsCache().getValue(name, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValueOldCache(String name) {
        return (T) getMethodsCache().invokeGetter(this, name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValueLambdaCache(String name) {
        return (T) getLambdaMethodsCache().getValue(name, this);
    }

    @Override
    public void setValueOldCache(String name, Object value, boolean checkEquals) {
        Object oldValue = getValueOldCache(name);
        if ((!checkEquals) || (!InstanceUtils.propertyValueEquals(oldValue, value))) {
            getMethodsCache().invokeSetter(this, name, value);
        }
    }

    @Override
    public void setValueLambdaCache(String name, Object value, boolean checkEquals) {
        Object oldValue = getValueLambdaCache(name);
        if ((!checkEquals) || (!InstanceUtils.propertyValueEquals(oldValue, value))) {
            getLambdaMethodsCache().setValue(name, value, this);
        }
    }

    protected MethodsCache getMethodsCache() {
        Class cls = getClass();
        MethodsCache cache = methodCacheMap.get(cls);
        if (cache == null) {
            cache = new MethodsCache(cls);
            methodCacheMap.put(cls, cache);
        }
        return cache;
    }

    protected LambdaMethodsCache getLambdaMethodsCache() {
        Class cls = getClass();
        LambdaMethodsCache cache = lambdaMethodsCache.get(cls);
        if (cache == null) {
            cache = new LambdaMethodsCache(cls);
            lambdaMethodsCache.put(cls, cache);
        }
        return cache;
    }

    @Override
    public void setValue(String name, Object value) {
        setValue(name, value, true);
    }

    /**
     * Set value to property in instance
     * <p>
     * For internal use only. Use {@link #setValue(String, Object)}
     *
     * @param name        property name
     * @param value       value
     * @param checkEquals check equals for previous and new value.
     *                    If flag is true and objects equals, then setter will not be invoked
     */
    public void setValue(String name, Object value, boolean checkEquals) {
        Object oldValue = getValue(name);
        if ((!checkEquals) || (!InstanceUtils.propertyValueEquals(oldValue, value))) {
            //getLambdaMethodsCache().setValue(name, value,this);
            //getMethodsCache().invokeSetter(this, name, value);
            //if (this instanceof CubaEnhancedSetGet)
            ((CubaEnhancedSetGet) this).setValueNative(name, value);
            //else
            //throw new RuntimeException("This object ins not an instance of CubaEnhancedSetGet");
        }
    }

    @Override
    public <T> T getValueEx(String name) {
        return InstanceUtils.getValueEx(this, name);
    }

    @Nullable
    @Override
    public <T> T getValueEx(BeanPropertyPath propertyPath) {
        return InstanceUtils.getValueEx(this, propertyPath);
    }

    @Override
    public void setValueEx(String name, Object value) {
        InstanceUtils.setValueEx(this, name, value);
    }

    @Override
    public void setValueEx(BeanPropertyPath propertyPath, Object value) {
        InstanceUtils.setValueEx(this, propertyPath, value);
    }
}