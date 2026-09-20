package io.student.rcc.model;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import io.student.rcc.data.entity.UserEntity;
import org.junit.jupiter.params.shadow.de.siegmar.fastcsv.util.Nullable;

import java.util.Base64;
import java.util.UUID;

public record UserJson(
        @JsonProperty("id") UUID id,
        @JsonProperty("username") String username,
        @JsonProperty("firstname") String firstname,
        @JsonProperty("lastname") String lastname,
        @JsonProperty("avatar") String avatar
) {
    @Nullable
    public static UserJson fromEntity(@Nullable UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return new UserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getFirstname(),
                entity.getLastname(),
                entity.getAvatar() == null ? null : Base64.getEncoder().encodeToString(entity.getAvatar())
        );
    }
}
