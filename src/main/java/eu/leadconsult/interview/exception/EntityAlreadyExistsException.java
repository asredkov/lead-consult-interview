package eu.leadconsult.interview.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String message) { super(message); }
}
