package backend.finance.api_users.application.dtos.internal;

import backend.finance.api_users.domain.enums.RoleEnum;

import java.util.UUID;

public record RoleDto(UUID id, RoleEnum name) {
}
