package com.biit.labstation.exceptions;

import java.io.Serial;

public class ChromeDriverException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6568722959560394935L;

    public ChromeDriverException(String message) {
        super(message);
    }

    public ChromeDriverException() {
        super();
    }
}
