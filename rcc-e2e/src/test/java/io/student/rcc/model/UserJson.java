package io.student.rcc.model;

import java.util.UUID;

public record UserJson(
    UUID id,
    String username,
    String password,
    String firstname,
    String lastName,
    String avatar
    ) {
}
