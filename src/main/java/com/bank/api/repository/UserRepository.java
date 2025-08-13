package com.bank.api.repository;


import com.bank.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью User.
 * <p>
 * Поддерживает поиск пользователя по логину и проверку существования пользователя.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Ищет пользователя по логину.
     *
     * @param username логин пользователя
     * @return Optional с пользователем или пустой, если не найден
     */
    Optional<User> findByUsername(String username);

    /**
     * Проверяет существование пользователя по логину.
     *
     * @param username логин пользователя
     * @return true, если пользователь существует
     */
    boolean existsByUsername(String username);
}