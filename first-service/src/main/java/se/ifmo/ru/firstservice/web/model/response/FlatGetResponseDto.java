package se.ifmo.ru.firstservice.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlatGetResponseDto {
    @NotNull(message = "id - не может быть пустым!")
    @Size(min = 1, message = "id - должен быть больше 0!")
    private long id;
    @NotNull(message = "name - не может быть пустым!")
    @NotBlank(message = "name - не может быть пустым!")
    private String name;
    @NotNull(message = "coordinates - не может быть пустым!")
    private FlatCoordinatesGetResponsesDto coordinates;
    @NotNull(message = "creationDate - не может быть пустым!")
    private LocalDateTime creationDate;
    @Size(min = 1, message = "area - должен быть больше 0!")
    private Integer area;
    @Size(min = 1, message = "numberOfRooms - должен быть больше 0!")
    private long numberOfRooms;
    @Size(min = 1, message = "floor - должен быть больше 0!")
    private int floor;
    @Size(min = 1, message = "timeToMetroOnFoot - должен быть больше 0!")
    private int timeToMetroOnFoot;
    @NotNull
    private Boolean balcony;
    private String view;
    private FlatHouseGetResponseDto house;
    @NotNull
    @Size(min = 1, message = "price - должен быть больше 0!")
    private Double price;

    @Data
    public static class FlatCoordinatesGetResponsesDto {
        private Integer x;
        private Float y;
    }

    @Data
    public static class FlatHouseGetResponseDto {
        private String name;
        private Long year;
        private Integer numberOfFloors;
    }
}
