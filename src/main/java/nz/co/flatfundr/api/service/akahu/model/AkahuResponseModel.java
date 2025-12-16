package nz.co.flatfundr.api.service.akahu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed abstract class AkahuResponseModel permits AkahuAccountResponse, AkahuAccountsResponse, ErrorResponse {
    private Boolean success;
}
