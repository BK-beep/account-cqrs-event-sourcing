package ma.enset.comptecqrses.common_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class DebitAccountDto {
    private String accountId;
    private double amount;
    private String currency;
}
