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
package com.haulmont.cuba.gui.components;

import com.google.common.reflect.TypeToken;

import java.sql.Date;
import java.time.LocalTime;
import java.time.OffsetTime;

public interface TimeField<V> extends Field<V>, HasDatatype<V>, Buffered, Component.Focusable {
    String NAME = "timeField";

    TypeToken<TimeField<Date>> TYPE_DEFAULT = new TypeToken<TimeField<Date>>(){};

    TypeToken<TimeField<java.sql.Time>> TYPE_TIME = new TypeToken<TimeField<java.sql.Time>>(){};
    TypeToken<TimeField<LocalTime>> TYPE_LOCALTIME = new TypeToken<TimeField<LocalTime>>(){};
    TypeToken<TimeField<OffsetTime>> TYPE_OFFSETTIME = new TypeToken<TimeField<OffsetTime>>(){};

    enum Resolution {
        SEC,
        MIN,
        HOUR
    }

    /**
     * Return resolution of the TimeField.
     *
     * @return Resolution
     */
    Resolution getResolution();

    /**
     * Set resolution of the TimeField.
     *
     * @param resolution resolution
     */
    void setResolution(Resolution resolution);

    @Deprecated
    boolean getShowSeconds();

    /**
     * @deprecated Use either {@link #setResolution(Resolution)} or {@link #setFormat(String)}
     */
    @Deprecated
    void setShowSeconds(boolean showSeconds);

    String getFormat();

    void setFormat(String timeFormat);
}