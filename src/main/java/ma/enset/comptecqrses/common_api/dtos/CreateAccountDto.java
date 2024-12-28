package ma.enset.comptecqrses.common_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data @NoArgsConstructor @AllArgsConstructor
public class CreateAccountDto {
    private double initialBalance;
    private String currency;

}
