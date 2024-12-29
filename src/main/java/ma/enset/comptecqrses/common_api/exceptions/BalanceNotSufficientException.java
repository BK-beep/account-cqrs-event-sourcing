package ma.enset.comptecqrses.common_api.exceptions;

public class BalanceNotSufficientException extends RuntimeException {
    public BalanceNotSufficientException(String msg) {
        super(msg);
    }
}
