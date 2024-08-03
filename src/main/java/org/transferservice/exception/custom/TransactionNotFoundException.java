package org.transferservice.exception.custom;

public class TransactionNotFoundException extends Exception {
    public TransactionNotFoundException(String message) {
        super(message);
    }
}