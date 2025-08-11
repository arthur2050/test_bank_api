package com.bank.api.repository;

import com.bank.api.entity.Card;
import com.bank.api.entity.CardStatus;
import com.bank.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    Page<Card> findAllByOwner(User owner, Pageable pageable);

    Page<Card> findAllByOwnerAndStatus(User owner, CardStatus status, Pageable pageable);
}
