package org.gfg.TransactionService.repository;

import jakarta.transaction.Transactional;
import org.gfg.TransactionService.model.TransactionModel;
import org.gfg.enums.TxnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel,Integer> {

    @Modifying
    @Transactional
    @Query("update transaction as t set t.txnStatus=:status,t.txnMessage=:message where t.txnId=:txnId")
    void updateTransactionDetails(String txnId, TxnStatus status, String message);

    List<TransactionModel> findBySenderIdOrReceiverId(String sender, String receiver);
}
