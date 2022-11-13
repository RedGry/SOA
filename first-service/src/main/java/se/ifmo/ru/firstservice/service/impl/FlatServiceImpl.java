package se.ifmo.ru.firstservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.StringUtils;
import se.ifmo.ru.firstservice.mapper.FlatMapper;
import se.ifmo.ru.firstservice.service.api.FlatService;
import se.ifmo.ru.firstservice.service.model.Flat;
import se.ifmo.ru.firstservice.service.model.View;
import se.ifmo.ru.firstservice.storage.model.*;
import se.ifmo.ru.firstservice.storage.repostitory.impl.FlatRepositoryImpl;
import se.ifmo.ru.firstservice.web.model.request.FlatAddOrUpdateRequestDto;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class FlatServiceImpl implements FlatService {

    private final FlatRepositoryImpl flatDto;
    private final FlatMapper flatMapper;

    @Autowired
    public FlatServiceImpl(FlatRepositoryImpl flatDto, FlatMapper flatMapper){
        this.flatDto = flatDto;
        this.flatMapper = flatMapper;
    }

    @Override
    public Page<Flat> getFlats(List<String> sortsList, List<String> filtersList, Integer page, Integer pageSize) {
        if (page != null || pageSize != null){
            if (page == null){
                page = 0;
            }
            if (pageSize == null){
                pageSize = 20;
            }
        }

        Pattern nestedFieldNamePattern = Pattern.compile("(.*)\\.(.*)");
        Pattern lhsPattern = Pattern.compile("(.*)\\[(.*)\\]=(.*)");

        List<Sort> sorts = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(sortsList)){
            boolean containsOppositeSorts = sortsList.stream().allMatch(e1 ->
                    sortsList.stream().allMatch(e2 -> Objects.equals(e1, "-" + e2))
            );

            if (containsOppositeSorts){
                throw new IllegalArgumentException("Request contains opposite sort parameters");
            }

            for (String sort: sortsList){
                boolean desc = sort.startsWith("-");
                String sortFieldName = desc ? sort.split("-")[1] : sort;

                Matcher matcher = nestedFieldNamePattern.matcher(sortFieldName);
                if (matcher.find()){
                    String nestedField = matcher.group(2).substring(0, 1).toUpperCase() + matcher.group(2).substring(1);
                    sortFieldName = matcher.group(1) + nestedField;
                }

                sorts.add(Sort.builder()
                        .desc(desc)
                        .fieldName(sortFieldName)
                        .build()
                );
            }
        }

        List<Filter> filters = new ArrayList<>();

        for (String filter : filtersList){
            Matcher matcher = lhsPattern.matcher(filter);
            String fieldName = null, fieldValue = null;
            FilteringOperation filteringOperation = null;

            if (matcher.find()){
                fieldName = matcher.group(1);

                Matcher nestedFieldMatcher = nestedFieldNamePattern.matcher(fieldName);
                if (nestedFieldMatcher.find()){
                    String nestedField = nestedFieldMatcher.group(2).substring(0, 1).toUpperCase() + nestedFieldMatcher.group(2).substring(1);
                    fieldName = nestedFieldMatcher.group(1) + nestedField;
                }

                filteringOperation = FilteringOperation.fromValue(matcher.group(2));

                if (Objects.equals(fieldName, "balcony")){
                    if (!Objects.equals(filteringOperation, FilteringOperation.EQ) && !Objects.equals(filteringOperation, FilteringOperation.NEQ)) {
                        throw new IllegalArgumentException("Only [eq] and [neq] operations are allowed for \"balcony\" field");
                    }
                }

                if (Objects.equals(fieldName, "view")){
                    fieldValue = matcher.group(3).toLowerCase();
                } else fieldValue = matcher.group(3);
            }

            if (StringUtils.isEmpty(fieldName)){
                throw new IllegalArgumentException("Filter field name is empty");
            }

            if (StringUtils.isEmpty(fieldValue)){
                throw new IllegalArgumentException("Filter field value is empty");
            }

            if (Objects.equals(filteringOperation, FilteringOperation.UNDEFINED)){
                throw new IllegalArgumentException("No or unknown filtering operation. Possible values are: eq,neq,gt,lt,gte,lte.");
            }

            filters.add(Filter.builder()
                    .fieldName(fieldName)
                    .fieldValue(fieldValue)
                    .filteringOperation(filteringOperation)
                    .build()
            );
        }

        Page<FlatEntity> entityPage;

        try {
            entityPage = flatDto.getSortedAndFilteredPage(sorts, filters, page, pageSize);
        } catch (NullPointerException e){
            throw new IllegalArgumentException("Error while getting page. Check query params format. " + e.getMessage(), e);
        }

        Page<Flat> ret = new Page<>();
        ret.setObjects(flatMapper.fromEntityList(entityPage.getObjects()));
        ret.setPage(entityPage.getPage());
        ret.setPageSize(entityPage.getPageSize());
        ret.setTotalPages(entityPage.getTotalPages());
        ret.setTotalCount(entityPage.getTotalCount());

        return ret;
    }

    @Override
    public Flat getFlat(long id) {
        return flatMapper.fromEntity(flatDto.findById(id));
    }

    @Override
    public Flat updateFlat(long id, FlatAddOrUpdateRequestDto requestDto) {
        FlatEntity flatEntity = flatDto.findById(id);

        if (flatEntity == null){
            return null;
        }

        flatEntity.setName(requestDto.getName());
        flatEntity.setArea(requestDto.getArea());
        flatEntity.setPrice(requestDto.getPrice());
        flatEntity.setBalcony(requestDto.getBalcony());
        flatEntity.setNumberOfRooms(requestDto.getNumberOfRooms());
        flatEntity.setFloor(requestDto.getFloor());
        flatEntity.setTimeToMetroOnFoot(requestDto.getTimeToMetroOnFoot());
        flatEntity.setView(View.fromValue(requestDto.getView().toLowerCase()));

        flatEntity.setCoordinatesX(requestDto.getCoordinates().getX());
        flatEntity.setCoordinatesY(requestDto.getCoordinates().getY());


        if (requestDto.getHouse() != null) {
            flatEntity.setHouseName(requestDto.getHouse().getName());
            flatEntity.setHouseYear(requestDto.getHouse().getYear());
            flatEntity.setHouseNumberOfFloors(requestDto.getHouse().getNumberOfFloors());
        }

        flatEntity = flatDto.save(flatEntity);

        return flatMapper.fromEntity(flatEntity);
    }

    @Override
    public Flat addFlat(FlatAddOrUpdateRequestDto requestDto) {
//        FlatEntity flatEntity = FlatEntity.builder()
//                .name(requestDto.getName())
//                .area(requestDto.getArea())
//                .price(requestDto.getPrice())
//                .balcony(requestDto.getBalcony())
//                .numberOfRooms(requestDto.getNumberOfRooms())
//                .floor(requestDto.getFloor())
//                .timeToMetroOnFoot(requestDto.getTimeToMetroOnFoot())
//                .view(View.fromValue(requestDto.getView()))
//                .coordinatesX(requestDto.getCoordinates().getX())
//                .coordinatesY(requestDto.getCoordinates().getY())
//                .houseName(requestDto.getHouse().getName())
//                .houseYear(requestDto.getHouse().getYear())
//                .houseNumberOfFloors(requestDto.getHouse().getNumberOfFloors())
//                .build();

        FlatEntity flatEntity = flatMapper.fromFlatRequestDto(requestDto);

        return flatMapper.fromEntity(flatDto.save(flatEntity));
    }

    @Override
    public boolean deleteFlat(long id) {
        return flatDto.deleteById(id);
    }

    @Override
    public boolean deleteOneFlatByView(View view) {
        return flatDto.deleteByView(view);
    }

    @Override
    public double getFlatsAverageTimeToMetroOnFoot() {
        return flatDto.averageTimeToMetro();
    }

    @Override
    public List<String> getUniqueFlatsView() {
        return flatDto.getUniqueView();
    }
}
