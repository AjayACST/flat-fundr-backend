package nz.co.flatfundr.api.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateClientRequest(
        @NotBlank String clientName,
        @NotEmpty @Size(min = 1, message = "You need at least one redirect URI") List<String> redirectUris
) {}