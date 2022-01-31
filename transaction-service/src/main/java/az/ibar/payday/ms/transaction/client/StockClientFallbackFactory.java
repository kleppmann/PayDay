package az.ibar.payday.ms.transaction.client;

import az.ibar.payday.ms.transaction.logger.SafeLogger;
import az.ibar.payday.ms.transaction.model.StockTransactionResponse;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
public class StockClientFallbackFactory implements FallbackFactory<StockClient> {

    private static final SafeLogger logger = SafeLogger.getLogger(StockClientFallbackFactory.class);

    @Override
    public StockClient create(Throwable cause) {
        return new StockClient() {
            @Override
            public void transactionResult(@Valid StockTransactionResponse transactionResponse) {
                logger.error("transactionResult.error", cause);
            }
        };
    }
}
