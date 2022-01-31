package az.ibar.payday.ms.stock.mapper;

import az.ibar.payday.ms.stock.entity.StockChangeEntity;
import az.ibar.payday.ms.stock.entity.StockEntity;
import az.ibar.payday.ms.stock.model.view.StockChangeView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class StockChangeMapper {

    public static final StockChangeMapper INSTANCE = getMapper(StockChangeMapper.class);

    @Mapping(source = "stock.name", target = "name")
    public abstract StockChangeView stockChangeViewFromEntity(StockChangeEntity entity, StockEntity stock);
}
