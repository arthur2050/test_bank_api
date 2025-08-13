package com.bank.api.repository;

import com.bank.api.entity.Card;
import com.bank.api.entity.CardStatus;
import com.bank.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью Card.
 * <p>
 * Поддерживает пагинацию и фильтрацию карт по пользователю и статусу.
 */
public interface CardRepository extends JpaRepository<Card, Long> {

    /**
     * Возвращает все карты пользователя с пагинацией.
     *
     * @param owner    пользователь
     * @param pageable объект пагинации
     * @return страница карт
     */
    Page<Card> findAllByOwner(User owner, Pageable pageable);

    /**
     * Возвращает все карты пользователя с фильтром по статусу и пагинацией.
     *
     * @param owner    пользователь
     * @param status   статус карты
     * @param pageable объект пагинации
     * @return страница карт
     */
    Page<Card> findAllByOwnerAndStatus(User owner, CardStatus status, Pageable pageable);
}
