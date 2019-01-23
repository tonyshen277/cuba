/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.chile.core.datatypes;

/**
 * Exception that can be thrown during value conversion in {@link Datatype}.
 */
public class ValueConversionException extends RuntimeException {

    public ValueConversionException(String message) {
        super(message);
    }

    public ValueConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
