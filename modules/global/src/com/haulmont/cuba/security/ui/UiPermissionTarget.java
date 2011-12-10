/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.security.ui;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.UuidProvider;

import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@com.haulmont.chile.core.annotations.MetaClass(name = "sec$UiTarget")
@SystemLevel
public class UiPermissionTarget extends AbstractInstance
        implements Entity<String> {

    @com.haulmont.chile.core.annotations.MetaProperty(mandatory = true)
    private String id;

    @com.haulmont.chile.core.annotations.MetaProperty(mandatory = true)
    private String caption;

    @com.haulmont.chile.core.annotations.MetaProperty(mandatory = true)
    private String permissionValue;

    @com.haulmont.chile.core.annotations.MetaProperty(mandatory = true)
    private UiPermissionVariant permissionVariant = UiPermissionVariant.NOTSET;

    @com.haulmont.chile.core.annotations.MetaProperty(mandatory = true)
    private String componentType;

    private UUID uuid = UuidProvider.createUuid();

    public UiPermissionTarget(String id, String caption, String permissionValue) {
        this.id = id;
        this.caption = caption;
        this.permissionValue = permissionValue;
    }

    public UiPermissionTarget(String id, String caption, String permissionValue, UiPermissionVariant permissionVariant) {
        this.id = id;
        this.caption = caption;
        this.permissionValue = permissionValue;
        this.permissionVariant = permissionVariant;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public MetaClass getMetaClass() {
        return MetadataProvider.getSession().getClass(getClass());
    }

    @Override
    public String toString() {
        return caption;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPermissionValue() {
        return permissionValue;
    }

    public void setPermissionValue(String permissionValue) {
        this.permissionValue = permissionValue;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public UiPermissionVariant getPermissionVariant() {
        return permissionVariant;
    }

    public void setPermissionVariant(UiPermissionVariant permissionVariant) {
        this.permissionVariant = permissionVariant;
    }
}