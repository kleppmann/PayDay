package az.ibar.payday.ms.transaction.client;

import az.ibar.payday.ms.transaction.model.StockTransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Primary
@FeignClient(value = "stock-service", fallbackFactory = StockClientFallbackFactory.class)
public interface StockClient {

    @PostMapping(value = "/transaction/result")
    void transactionResult(@RequestBody @Valid StockTransactionResponse transactionResponse);
}
