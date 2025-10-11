package backend.finance.api_user.application.dtos.internal;

import backend.finance.api_user.domain.enums.RoleEnum;

import java.util.UUID;

public record RoleDto(UUID id, RoleEnum name) {
}
