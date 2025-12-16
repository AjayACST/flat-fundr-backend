package nz.co.flatfundr.api.service.akahu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AkahuAccount {
    @NotBlank
    @JsonProperty("_id")
    private String id;

    @NotBlank
    private String name;

    @JsonProperty("connection")
    @NotNull
    private ConnectionMeta connectionMeta = new ConnectionMeta();

    @JsonProperty("formatted_account")
    @NotNull
    private String accountNumber;

    @JsonProperty("balance")
    @NotNull
    private BalanceMeta balanceMeta = new BalanceMeta();

    private static class ConnectionMeta {
        @JsonProperty("name")
        @NotNull
        private String bankName;

        @JsonProperty("logo")
        @NotNull
        private String bankLogo;
    }

    private static class BalanceMeta {
        @JsonProperty("current")
        @NotNull
        private BigDecimal currentBalance;

        @JsonProperty("available")
        @NotNull
        private BigDecimal availableBalance;
    }
}
