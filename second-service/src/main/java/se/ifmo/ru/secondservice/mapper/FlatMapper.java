package se.ifmo.ru.secondservice.mapper;

import org.mapstruct.Mapper;
import se.ifmo.ru.secondservice.external.model.RestClientFlat;
import se.ifmo.ru.secondservice.service.model.Flat;
import se.ifmo.ru.secondservice.service.model.View;
import se.ifmo.ru.secondservice.web.model.response.FlatGetResponseDto;

import java.util.List;

@Mapper
public interface FlatMapper {
    FlatGetResponseDto toDto(Flat source);

    List<FlatGetResponseDto> toGetResponseDtoList(List<Flat> source);

    Flat fromRestClient(RestClientFlat restClientFlat);

    List<Flat> fromRestClient(List<RestClientFlat> restClientFlat);

    default String from(View view){
        return view.toString();
    }

    default View from (String view){
        return View.fromValue(view);
    }
}
