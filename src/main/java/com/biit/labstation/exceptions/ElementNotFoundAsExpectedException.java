package com.biit.labstation.exceptions;

import java.io.Serial;

public class ElementNotFoundAsExpectedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6568722959560394935L;

    public ElementNotFoundAsExpectedException(String message) {
        super(message);
    }

    public ElementNotFoundAsExpectedException() {
        super();
    }
}
