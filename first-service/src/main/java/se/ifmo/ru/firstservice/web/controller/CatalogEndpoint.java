package se.ifmo.ru.firstservice.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import se.ifmo.ru.firstservice.catalog.*;
import se.ifmo.ru.firstservice.exception.NotFoundException;
import se.ifmo.ru.firstservice.exception.ServiceFault;
import se.ifmo.ru.firstservice.exception.ServiceFaultException;
import se.ifmo.ru.firstservice.mapper.FlatMapper;
import se.ifmo.ru.firstservice.service.api.FlatService;
import se.ifmo.ru.firstservice.service.model.Flat;
import se.ifmo.ru.firstservice.service.model.View;
import se.ifmo.ru.firstservice.storage.model.Page;
import se.ifmo.ru.firstservice.util.ResponseUtils;
import se.ifmo.ru.firstservice.web.model.request.FlatAddOrUpdateRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Endpoint
@Slf4j
public class CatalogEndpoint {
    private static final String NAMESPACE_URI = "http://se/ifmo/ru/firstservice/catalog";
    private final FlatService flatService;
    private final FlatMapper flatMapper;
    private final ResponseUtils responseUtils;

    @Autowired
    public CatalogEndpoint(FlatService flatService, FlatMapper flatMapper, ResponseUtils responseUtils) {
        this.flatService = flatService;
        this.responseUtils = responseUtils;
        this.flatMapper = flatMapper;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getFlatsRequest")
    @ResponsePayload
    public GetFlatsResponse getFlats(@RequestPayload GetFlatsRequest request) {

        String[] sortParameters = request.getSort().toArray(String[]::new);
        String[] filterParameters = request.getFilter().toArray(String[]::new);

        String pageParam = request.getPage();
        String pageSizeParam = request.getPageSize();
        Integer page = null, pageSize = null;

        log.info("sort = {}, filter = {}, page = {}, pageSize = {}", sortParameters, filterParameters, pageParam, pageSizeParam);

        try {
            if (StringUtils.isNotEmpty(pageParam)) {
                page = Integer.parseInt(pageParam);
                if (page <= 0) {
                    log.warn("page value - format error: {}", page);
                    throw new NumberFormatException("Invalid query param value");
                }
            }

            if (StringUtils.isNotEmpty(pageSizeParam)) {
                pageSize = Integer.parseInt(pageSizeParam);
                if (pageSize <= 0) {
                    log.warn("pageSize value - format error: {}", pageSize);
                    throw new NumberFormatException("Invalid query param value");
                }
            }
        } catch (NumberFormatException e){
            throw new ServiceFaultException("Error", new ServiceFault("400", "Invalid query param value"));
        }

        List<String> sort = ArrayUtils.isEmpty(sortParameters)
                ? new ArrayList<>()
                : Stream.of(sortParameters).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        List<String> filter = ArrayUtils.isEmpty(filterParameters)
                ? new ArrayList<>()
                : Stream.of(filterParameters).filter(StringUtils::isNotEmpty).collect(Collectors.toList());

        Page<Flat> resultPage = null;

        try {
            resultPage = flatService.getFlats(sort, filter, page, pageSize);
        } catch (IllegalArgumentException e){
            throw new ServiceFaultException("Error", new ServiceFault("400", e.getMessage()));
        }

        if (resultPage == null) {
            throw new ServiceFaultException("Error", new ServiceFault("404", "Not Found"));
        }

        GetFlatsResponse response = new GetFlatsResponse();

        response.getFlatGetResponseDtos().addAll(flatMapper.toGetResponseDtoListResponse(flatMapper.toGetResponseDtoList(resultPage.getObjects())));
        response.setPage(resultPage.getPage());
        response.setPageSize(resultPage.getPageSize());
        response.setTotalPages(resultPage.getTotalPages());
        response.setTotalCount(resultPage.getTotalCount());

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getFlatRequest")
    @ResponsePayload
    public GetFlatResponse getFlat(@RequestPayload GetFlatRequest request){
        String id = request.getId();

        log.info("id = {}", id);

        Flat flat = null;
        try {
            flat = flatService.getFlat(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new ServiceFaultException("Error", new ServiceFault("400", "Invalid query param value"));
        }

        if (flat == null){
            throw new ServiceFaultException("Error", new ServiceFault("404", "Flat with id " + id + " not found"));
        }

        GetFlatResponse response = new GetFlatResponse();

        response.setFlat(flatMapper.toGetResponseDtoResponse(flatMapper.toDto(flat)));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addFlatRequest")
    @ResponsePayload
    public AddFlatResponse addFlat(@RequestPayload AddFlatRequest request) {

        FlatAddOrUpdateRequestDto flatNew = flatMapper.toFlatAddOrUpdateRequestDto(request);

        log.info(flatNew.toString());

        validateFlatAddOrUpdateRequestDto(flatNew);

        log.info("Validate - TRUE");
        log.info(flatNew.toString());

        AddFlatResponse response = new AddFlatResponse();
        response.setFlat(flatMapper.toGetResponseDto(flatService.addFlat(flatNew)));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateFlatRequest")
    @ResponsePayload
    public UpdateFlatResponse updateFlate(@RequestPayload UpdateFlatRequest request){

        FlatAddOrUpdateRequestDto flatNew = flatMapper.toFlatAddOrUpdateRequestDto(request.getFlat());
        String id = request.getId();


        log.info("UPDATE START");
        log.info("Id = {}", request.getId());
        log.info("Flat = {}", flatNew.toString());

        validateFlatAddOrUpdateRequestDto(flatNew);

        Flat flat = null;

        try {
            flat = flatService.updateFlat(Long.parseLong(id), flatNew);
        } catch (NumberFormatException e) {
            throw new ServiceFaultException("Error", new ServiceFault("400", "Invalid query param value"));
        }

        log.info("UPDATE - OK");

        if (flat == null){
            throw new ServiceFaultException("Error", new ServiceFault("404", "Flat with id " + request.getId() + " not found"));
        }

        UpdateFlatResponse response = new UpdateFlatResponse();

        response.setFlat(flatMapper.toGetResponseDto(flat));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteFlatRequest")
    @ResponsePayload
    public DeleteFlatResponse deleteFlat(@RequestPayload DeleteFlatRequest request){

        boolean deleted = false;

        try {
            deleted =  flatService.deleteFlat(Long.parseLong(request.getId()));
        } catch (NumberFormatException e) {
            throw new ServiceFaultException("Error", new ServiceFault("400", "Invalid query param value"));
        }

        log.info("DeleteById - {}", deleted);

        if (!deleted){
            throw new ServiceFaultException("Error", new ServiceFault("404", "Flat with id " + request.getId() + " not found"));
        }

        DeleteFlatResponse response = new DeleteFlatResponse();

        response.setCode("204");
        response.setMessage("The flat was successfully deleted");
        response.setTime(String.valueOf(LocalDateTime.now()));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteFlatByViewRequest")
    @ResponsePayload
    public DeleteFlatByViewResponse deleteFlatByView(@RequestPayload DeleteFlatByViewRequest request){
        boolean deleted = false;

        log.info(request.getView());
        log.info(View.fromValue(request.getView().toLowerCase()).toString());

        try {
            deleted = flatService.deleteOneFlatByView(View.fromValue(request.getView().toLowerCase()));
        } catch (NotFoundException e){
            throw new ServiceFaultException("Error", new ServiceFault("404", "Flat with view " + request.getView() + " not found"));
        }

        log.info("DeleteByView - {}, view is {}", deleted, request.getView().toLowerCase());

        if (!deleted){
            throw new ServiceFaultException("Error", new ServiceFault("404", "Flat with view " + request.getView() + " not found"));
        }

        DeleteFlatByViewResponse response = new DeleteFlatByViewResponse();

        response.setCode("204");
        response.setMessage("The flat was successfully deleted");
        response.setTime(String.valueOf(LocalDateTime.now()));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getFlatsAverageTimeMetroRequest")
    @ResponsePayload
    public GetFlatsAverageTimeMetroResponse getFlatsAverageTimeMetro(){

        GetFlatsAverageTimeMetroResponse response = new GetFlatsAverageTimeMetroResponse();

        log.info("Average time to metro: {}", flatService.getFlatsAverageTimeToMetroOnFoot());

        response.setNumber(flatService.getFlatsAverageTimeToMetroOnFoot());

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUniqueFlatViewRequest")
    @ResponsePayload
    public GetUniqueFlatViewResponse getUniqueFlatsView(){

        GetUniqueFlatViewResponse response = new GetUniqueFlatViewResponse();

        log.info("List unique view: {}", flatService.getUniqueFlatsView());

        response.getViewList().addAll(flatService.getUniqueFlatsView());

        return response;
    }

    private void validateFlatAddOrUpdateRequestDto(FlatAddOrUpdateRequestDto requestDto){
        if (StringUtils.isEmpty(requestDto.getName()) || requestDto.getName().length() > 255){
            throw new ServiceFaultException("Error", new ServiceFault("400", "Name не должен быть пустым и не больше 255 символов!"));
        }

        if (requestDto.getCoordinates() == null){
            throw new ServiceFaultException("Error", new ServiceFault("400", "Coordinates не может быть null!"));
        }

        if (requestDto.getCoordinates().getX() == null){
            throw new ServiceFaultException("Error", new ServiceFault("400", "Coordinates X не может быть null!"));
        }

        if (requestDto.getCoordinates().getY() == null){
            throw new ServiceFaultException("Error", new ServiceFault("400", "Coordinates Y не может быть null!"));
        }

        if (requestDto.getArea() <= 0 ){
            throw new ServiceFaultException("Error", new ServiceFault("400", "Area должен быть больше 0!"));
        }

        if (requestDto.getNumberOfRooms() <= 0 ){
            throw new ServiceFaultException("Error", new ServiceFault("400", "Number Of Rooms должен быть больше 0!"));
        }

        if (requestDto.getFloor() <= 0 ){
            throw new ServiceFaultException("Error", new ServiceFault("400", "Floor должен быть больше 0!"));
        }

        if (requestDto.getTimeToMetroOnFoot() <= 0 ){
            throw new ServiceFaultException("Error", new ServiceFault("400", "Time To Metro On Foot должен быть больше 0!"));
        }

        if (requestDto.getBalcony() == null ){
            throw new ServiceFaultException("Error", new ServiceFault("400", "Balcony не может быть null!"));
        }

        if (requestDto.getPrice() <= 0 ){
            throw new ServiceFaultException("Error", new ServiceFault("400", "Price должен быть больше 0!"));
        }

        if (requestDto.getHouse() != null){
            if (requestDto.getHouse().getYear() != null) {
                try {
                    if (requestDto.getHouse().getYear() <= 0) {
                        throw new ServiceFaultException("Error", new ServiceFault("400", "House Year должен быть больше 0!"));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            if (requestDto.getHouse().getNumberOfFloors() != null) {
                try {
                    if (requestDto.getHouse().getNumberOfFloors() <= 0) {
                        throw new ServiceFaultException("Error", new ServiceFault("400", "House Number Of Floors должен быть больше 0!"));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
