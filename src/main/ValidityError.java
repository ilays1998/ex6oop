package main;

/**
 * the class of the exception that
 * throws when the file contain invalid line
 */
public class ValidityError extends Exception {
    public ValidityError() {
    }

    public ValidityError(String errorMsg) {
        super(errorMsg);
    }


}
