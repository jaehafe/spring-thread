package com.spring.board.model.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserSignUpRequestBody(
        @NotEmpty String username,
        @NotEmpty String password
) {

}
