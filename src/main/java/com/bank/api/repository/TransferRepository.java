package com.bank.api.repository;

import com.bank.api.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью Transfer.
 * <p>
 * Поддерживает CRUD операции с переводами между картами.
 */
public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
