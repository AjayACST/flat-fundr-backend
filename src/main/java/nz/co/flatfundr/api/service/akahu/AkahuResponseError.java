package nz.co.flatfundr.api.service.akahu;

import lombok.Getter;

@Getter
public class AkahuResponseError extends Exception {
    private final int statusCode;

    public AkahuResponseError(String message, int statusCode) {
        super(message + ", with status code: " + statusCode);
        this.statusCode = statusCode;
    }
}
