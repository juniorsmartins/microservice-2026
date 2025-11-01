package backend.finance.api_users.application_business_rules.dtos.internal;

import backend.finance.api_users.enterprise_business_rules.enums.RoleEnum;

import java.util.UUID;

public record RoleDto(UUID id, RoleEnum name) {
}
