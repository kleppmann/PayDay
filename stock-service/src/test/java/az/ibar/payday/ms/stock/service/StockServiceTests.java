package az.ibar.payday.ms.stock.service;

import az.ibar.payday.ms.stock.client.StockDto;
import az.ibar.payday.ms.stock.client.StockListClient;
import az.ibar.payday.ms.stock.model.enums.Currency;
import az.ibar.payday.ms.stock.model.view.StockView;
import az.ibar.payday.ms.stock.service.impl.StockServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class StockServiceTests {

    @Mock
    private StockListClient stockListClient;

    @InjectMocks
    private StockServiceImpl stockService;

    @Test
    public void findAllSuccess() {

        System.out.println(stockListClient);

        List<StockDto> dubaiStocks = List
                .of(StockDto.builder().name("AMBAK").price(BigDecimal.ONE).currency(Currency.AED).build());

        List<StockDto> nyStocks = List
                .of(StockDto.builder().name("Apple").price(BigDecimal.TEN).currency(Currency.USD).build());

        List<StockDto> milanStocks = List
                .of(StockDto.builder().name("Prada").price(BigDecimal.ONE).currency(Currency.EUR).build());

        when(stockListClient.getDubaiStocks()).thenReturn(dubaiStocks);
        when(stockListClient.getNYStocks()).thenReturn(nyStocks);
        when(stockListClient.getMilanStocks()).thenReturn(milanStocks);

        List<StockView> allStocks = stockService.findAll();

        assertThat(allStocks.size()).isEqualTo(3);
    }
}
