package nz.co.flatfundr.api.service.akahu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ErrorResponse extends AkahuResponseModel {
    private Integer statusCode;
    private String errorMessage;
}
