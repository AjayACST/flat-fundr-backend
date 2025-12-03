package nz.co.flatfundr.api.dto.flat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateNewFlatRequest(
    @NotBlank String flatName,
    List<String> flatmateEmailList
) {}
