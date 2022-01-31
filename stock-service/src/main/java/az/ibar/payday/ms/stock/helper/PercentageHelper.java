package az.ibar.payday.ms.stock.helper;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PercentageHelper {

    public BigDecimal calculatePercentage(BigDecimal startValue, BigDecimal endValue) {
        BigDecimal diff;
        if (startValue.compareTo(endValue) < 0) {
            diff = endValue.subtract(startValue);
            return diff.multiply(BigDecimal.valueOf(100)).divide(startValue, 2, RoundingMode.HALF_UP);
        } else if (startValue.compareTo(endValue) > 0) {
            diff = startValue.subtract(endValue);
            return diff.multiply(BigDecimal.valueOf(100)).divide(startValue, 2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}
