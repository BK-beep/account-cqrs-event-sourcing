package ma.enset.comptecqrses.common_api.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;



public class CreateAccountCommand extends BaseCommand<String>{
    @Getter
    private double initialBalance;
    @Getter
    private String currency;

    public CreateAccountCommand(String id, double initialBalance, String currency) {
        super(id);
        this.initialBalance = initialBalance;
        this.currency = currency;
    }
}
