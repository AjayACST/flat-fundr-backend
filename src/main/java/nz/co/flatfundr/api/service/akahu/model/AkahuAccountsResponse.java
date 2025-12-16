package nz.co.flatfundr.api.service.akahu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class AkahuAccountsResponse extends AkahuResponseModel {
    @JsonProperty("items")
    private List<AkahuAccount> accounts = Collections.emptyList();
}
