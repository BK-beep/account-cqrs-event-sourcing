package ma.enset.comptecqrses.common_api.exceptions;

public class AmountNegativeException extends RuntimeException {
    public AmountNegativeException(String msg) {
        super(msg);
    }
}