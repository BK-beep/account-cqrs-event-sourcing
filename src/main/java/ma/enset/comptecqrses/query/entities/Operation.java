package ma.enset.comptecqrses.query.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import ma.enset.comptecqrses.common_api.enums.OperationType;

import java.util.Date;

@Entity
@AllArgsConstructor @NoArgsConstructor @Data
public class Operation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createdAt;
    private double amount;
    @Enumerated(EnumType.STRING)
    private OperationType type;
    @ManyToOne @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Account account;
}
