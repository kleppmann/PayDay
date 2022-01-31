package az.ibar.payday.ms.stock.service;

import az.ibar.payday.ms.stock.model.StockTransactionRequest;
import az.ibar.payday.ms.stock.model.StockTransactionResponse;

public interface StockTransactionService {

    void executeTransaction(StockTransactionRequest transactionRequest);

    void executeTransactionResult(StockTransactionResponse transactionResponse);
}
