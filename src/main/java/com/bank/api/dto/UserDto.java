package com.bank.api.dto;

import com.bank.api.entity.Role;
import lombok.Data;

/**
 * DTO для передачи информации о пользователе.
 */
@Data
public class UserDto {
    private Long id;
    private String username;
    private Role role;
    private boolean enabled;

    /**
     * Преобразует сущность User в DTO для передачи данных клиенту.
     *
     * @param user сущность пользователя
     * @return UserDto с данными пользователя (id, username, role, enabled)
     *
     * Примечание:
     * Этот метод позволяет отделить внутреннюю структуру сущности от того, что возвращается клиенту.
     * Например, пароль не включается в DTO.
     */
    public static UserDto fromEntity(com.bank.api.entity.User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setEnabled(user.isEnabled());
        return dto;
    }
}
