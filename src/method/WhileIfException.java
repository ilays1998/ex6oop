package method;

public class WhileIfException extends Exception {
    public WhileIfException(String empty_condition) {
        super(empty_condition);
    }

    public WhileIfException() {
        super();
    }
}
