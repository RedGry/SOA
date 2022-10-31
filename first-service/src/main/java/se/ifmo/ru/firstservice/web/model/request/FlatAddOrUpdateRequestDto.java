package se.ifmo.ru.firstservice.web.model.request;

import lombok.Data;
import lombok.ToString;
import lombok.Value;
import se.ifmo.ru.firstservice.service.model.View;
import se.ifmo.ru.firstservice.web.model.response.FlatGetResponseDto;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.*;

@Data
public class FlatAddOrUpdateRequestDto {
    @NotBlank(message = "Name не может быть пустым!")
    private String name;
    @NotNull(message = "Coordinates не может быть пустым!")
    private FlatCoordinatesAddRequestDto coordinates;
    @Min(value = 1, message = "Area должен быть больше 0!")
    private Integer area;
    @Min(value = 1, message = "Number Of Rooms должен быть больше 0!")
    private long numberOfRooms;
    @Min(value = 1, message = "Floor должен быть больше 0!")
    private int floor;
    @Min(value = 1, message = "Time To Metro On Foot должен быть больше 0!")
    private int timeToMetroOnFoot;
    @NotNull(message = "Balcony не может быть null!")
    private Boolean balcony;
    private String view;
    private FlatHouseGetRequestDto house;
    @NotNull(message = "Price должен быть null!")
    @Min(value = 1, message = "Price должен быть больше 0!")
    private Double price;

    @Data
    public static class FlatCoordinatesAddRequestDto {
        @NotNull(message = "Coordinates X не может быть null!")
        private Integer x;
        @NotNull(message = "Coordinates Y не может быть null!")
        private Float y;
    }

    @Data
    @ToString
    public static class FlatHouseGetRequestDto {
        @NotNull(message = "House name не может быть null!")
        private String name;
        @NotNull
        @Min(value = 1, message = "House year должен быть больше 0!")
        private Long year;
        @Min(value = 1, message = "House number of floors должен быть больше 0!")
        private int numberOfFloors;
    }
}
