package az.ibar.payday.ms.transaction.service;

import az.ibar.payday.ms.transaction.model.StockTransactionRequest;

public interface TransactionService {

    void execute(StockTransactionRequest transactionRequest);
}
