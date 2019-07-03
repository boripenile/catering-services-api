package app.exceptions;

@SuppressWarnings("serial")
public class InvalidCredentialsException extends Exception {

    public InvalidCredentialsException() {
        super();
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }

}
