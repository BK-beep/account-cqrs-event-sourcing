package ma.enset.comptecqrses.query.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.enset.comptecqrses.common_api.enums.AccountStatus;

import java.util.Collection;

@Entity
@AllArgsConstructor @NoArgsConstructor @Data
public class Account {
    @Id
    String id;
    private double balance;
    @Enumerated(EnumType.STRING)
    AccountStatus status;
    String currency;
    @OneToMany(mappedBy = "account")
    Collection<Operation> operations;
}
