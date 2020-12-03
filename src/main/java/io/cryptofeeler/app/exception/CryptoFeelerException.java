package io.cryptofeeler.app.exception;

public class CryptoFeelerException extends RuntimeException {

    public CryptoFeelerException(String message) {
        super(message);
    }

    public CryptoFeelerException(String message, Throwable cause) {
        super(message, cause);
    }
}
