package nz.co.flatfundr.api.service.akahu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class AkahuAccountResponse extends AkahuResponseModel {
    @JsonProperty("item")
    private AkahuAccount account;
}
