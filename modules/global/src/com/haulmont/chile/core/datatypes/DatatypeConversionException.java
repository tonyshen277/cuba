/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.chile.core.datatypes;

/**
 * Exception that can be thrown during value conversion in {@link Datatype}
 */
public class DatatypeConversionException extends RuntimeException {

    public DatatypeConversionException(String message) {
        super(message);
    }

    public DatatypeConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
