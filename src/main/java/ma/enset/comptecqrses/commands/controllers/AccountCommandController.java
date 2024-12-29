package ma.enset.comptecqrses.commands.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import ma.enset.comptecqrses.commands.aggregates.AccountAggregate;
import ma.enset.comptecqrses.common_api.commands.CreateAccountCommand;
import ma.enset.comptecqrses.common_api.commands.CreditAccountCommand;
import ma.enset.comptecqrses.common_api.commands.DebitAccountCommand;
import ma.enset.comptecqrses.common_api.dtos.CreateAccountDto;
import ma.enset.comptecqrses.common_api.dtos.CreditAccountDto;
import ma.enset.comptecqrses.common_api.dtos.DebitAccountDto;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/commands/account")
public class AccountCommandController {
    private final AccountAggregate accountAggregate;
    private CommandGateway commandGateway;
    private EventStore eventStore;
    @PostMapping("/create")
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountDto request){
        CompletableFuture<String > response = commandGateway
                                            .send(new CreateAccountCommand(
                                                    UUID.randomUUID().toString(),
                                                    request.getInitialBalance(),
                                                    request.getCurrency()
                                            ));
        return response;
    }
    @PutMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountDto request){
        CompletableFuture<String > response = commandGateway
                .send(new CreditAccountCommand(
                        request.getAccountId(),
                        request.getAmount(),
                        request.getCurrency()
                ));
        return response;
    }
    @PutMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountDto request){
        CompletableFuture<String > response = commandGateway
                .send(new DebitAccountCommand(
                        request.getAccountId(),
                        request.getAmount(),
                        request.getCurrency()
                ));
        return response;
    }
    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception exception){
        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/eventStore/{accountId}")
    public Stream eventStore(@PathVariable String accountId){
        return eventStore.readEvents(accountId).asStream();
    }

}
