package az.ibar.payday.stock.adapter.service.impl;

import az.ibar.payday.stock.adapter.model.StockDto;
import az.ibar.payday.stock.adapter.model.enums.Currency;
import az.ibar.payday.stock.adapter.service.StockListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class StockListServiceImplMock implements StockListService {

    @Override
    public List<StockDto> findAvailableDubaiStocks() {
        return List.of(StockDto.builder().name("EIBANK").currency(Currency.AED)
                        .price(BigDecimal.valueOf(new Random().nextInt(5) + 50)).build(),
                StockDto.builder().name("AMLAK").currency(Currency.AED)
                        .price(BigDecimal.valueOf(new Random().nextInt(5) + 50)).build());
    }

    @Override
    public List<StockDto> findAvailableMilanStocks() {
        return List
                .of(StockDto.builder().name("Pirelli").currency(Currency.EUR)
                                .price(BigDecimal.valueOf(new Random().nextInt(6) + 60)).build(),
                        StockDto.builder().name("Prada").currency(Currency.EUR)
                                .price(BigDecimal.valueOf(new Random().nextInt(6) + 60))
                                .build());
    }

    @Override
    public List<StockDto> findAvailableNYStocks() {
        return List.of(StockDto.builder().name("Apple").currency(Currency.USD)
                        .price(BigDecimal.valueOf(new Random().nextInt(8) + 80)).build(),
                StockDto.builder().name("Ambak").currency(Currency.USD)
                        .price(BigDecimal.valueOf(new Random().nextInt(8) + 80)).build());
    }
}
