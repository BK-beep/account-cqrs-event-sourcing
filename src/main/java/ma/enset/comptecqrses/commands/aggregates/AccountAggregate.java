package ma.enset.comptecqrses.commands.aggregates;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ma.enset.comptecqrses.common_api.enums.AccountStatus;
import ma.enset.comptecqrses.common_api.commands.CreateAccountCommand;
import ma.enset.comptecqrses.common_api.commands.CreditAccountCommand;
import ma.enset.comptecqrses.common_api.commands.DebitAccountCommand;
import ma.enset.comptecqrses.common_api.events.AccountActivatedEvent;
import ma.enset.comptecqrses.common_api.events.AccountCreatedEvent;
import ma.enset.comptecqrses.common_api.events.AccountCreditedEvent;
import ma.enset.comptecqrses.common_api.events.AccountDebitedEvent;
import ma.enset.comptecqrses.common_api.exceptions.AmountNegativeException;
import ma.enset.comptecqrses.common_api.exceptions.BalanceNotSufficientException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;

@Aggregate @NoArgsConstructor @AllArgsConstructor
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;

    private double balance;
    private String currency;
    private AccountStatus status;
    @CommandHandler //fct decision ; declencher evenement
    public AccountAggregate(CreateAccountCommand command){
        if (command.getInitialBalance()<0) throw new RuntimeException("Impossible, solde negative");
        //pour déclencher l aggregat ou l'emettre
        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getId(),
                command.getInitialBalance(),
                command.getCurrency(),
                AccountStatus.CREATED
        ));

    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
        this.accountId=event.getId();
        this.balance=event.getInitialBalance();
        this.status=AccountStatus.CREATED;
        this.currency=event.getCurrency();
        AggregateLifecycle.apply(new AccountActivatedEvent(
                event.getId(),
                AccountStatus.ACTIVATED
        ));
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event){
        this.status=event.getStatus();
    }

    @CommandHandler
    public void handle(CreditAccountCommand command){
        if (command.getAmount()<0) throw new AmountNegativeException("Amount should not be negative");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }
    @EventSourcingHandler
    public void on(AccountCreditedEvent event){
        this.balance+=event.getAmount();
    }
    @CommandHandler
    public void handle(DebitAccountCommand command){
        if (command.getAmount()<0) throw new AmountNegativeException("Amount should not be negative");
        if (command.getAmount()>this.balance) throw new BalanceNotSufficientException("Amount insufficient");
        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency(),
                new Date()
        ));
    }
    @EventSourcingHandler
    public void on(AccountDebitedEvent event){
        this.balance-=event.getAmount();
    }
}
