package org.cloudfoundry.identity.uaa.account;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }
}


