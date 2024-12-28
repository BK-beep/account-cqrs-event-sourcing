package ma.enset.comptecqrses.common_api.events;

import lombok.Getter;

public class AccountDebitedEvent extends BaseEvent<String>{
    @Getter
    private double amount;
    @Getter
    private String currency;
    public AccountDebitedEvent(String id) {
        super(id);
        this.amount = amount;
        this.currency = currency;
    }
}
