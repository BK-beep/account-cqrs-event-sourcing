package ma.enset.comptecqrses.query.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.comptecqrses.common_api.enums.AccountStatus;
import ma.enset.comptecqrses.common_api.enums.OperationType;
import ma.enset.comptecqrses.common_api.events.AccountActivatedEvent;
import ma.enset.comptecqrses.common_api.events.AccountCreatedEvent;
import ma.enset.comptecqrses.common_api.events.AccountCreditedEvent;
import ma.enset.comptecqrses.common_api.events.AccountDebitedEvent;
import ma.enset.comptecqrses.common_api.queries.GetAccountById;
import ma.enset.comptecqrses.common_api.queries.GetAllAccountsQuery;
import ma.enset.comptecqrses.query.entities.Account;
import ma.enset.comptecqrses.query.entities.Operation;
import ma.enset.comptecqrses.query.repositories.AccountRepository;
import ma.enset.comptecqrses.query.repositories.OperationRepository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class AccountServiceHandler {
    private final AccountRepository accountRepo;
    private final OperationRepository operationRepo;
    private final QueryGateway queryGateway;

    @EventHandler
    private void  on(AccountCreatedEvent event){
        log.info("*****************");
        log.info("AccountCreatedEvent received !");
        accountRepo.save(new Account(
                event.getId(),
                event.getInitialBalance(),
                event.getStatus(),
                event.getCurrency(),
                null
        ));
        log.info("account saved!");

    }
    @EventHandler
    private void  on(AccountActivatedEvent event){
        log.info("*****************");
        log.info("AccountActivatedEvent received !");
        Account account=accountRepo.findById(event.getId()).orElse(null);
        account.setStatus(AccountStatus.ACTIVATED);
        accountRepo.save(account);
        log.info("account activated!");

    }
    @EventHandler
    private void  on(AccountDebitedEvent event){
        log.info("*****************");
        log.info("AccountDebitedEvent received ! with amount : "+event.getAmount()+" "+event.getCurrency());
        Account account=accountRepo.findById(event.getId()).orElse(null);
        Operation operation=new Operation();
        operation.setAmount(event.getAmount());
        operation.setCreatedAt(new Date());
        operation.setType(OperationType.DEBIT);
        operation.setAccount(account);
        operationRepo.save(operation);
        account.setBalance(account.getBalance()-event.getAmount());
        accountRepo.save(account);
        log.info("account debited!");
    }
    @EventHandler
    private void  on(AccountCreditedEvent event){
        log.info("*****************");
        log.info("AccountCreditedEvent  received ! with amount : "+event.getAmount()+" "+event.getCurrency());
        Account account=accountRepo.findById(event.getId()).orElse(null);
        Operation operation=new Operation();
        operation.setAmount(event.getAmount());
        operation.setCreatedAt(new Date());
        operation.setType(OperationType.CREDIT);
        operation.setAccount(account);
        operationRepo.save(operation);
        account.setBalance(account.getBalance()+event.getAmount());
        accountRepo.save(account);
        log.info("account credited!");
    }

    @QueryHandler
    private List<Account> on(GetAllAccountsQuery query){
        return accountRepo.findAll();
    }
    @QueryHandler
    private Account on(GetAccountById query){
        return accountRepo.findById(query.getId()).orElse(null);
    }

}
