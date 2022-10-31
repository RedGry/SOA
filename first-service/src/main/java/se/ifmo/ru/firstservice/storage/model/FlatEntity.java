package se.ifmo.ru.firstservice.storage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.ifmo.ru.firstservice.service.model.View;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flat")
public class FlatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "coordinates_x")
    private Integer coordinatesX;

    @Column(name = "coordinates_y")
    private Float coordinatesY;

    @Column(name = "area")
    private Integer area;

    @Column(name = "number_of_rooms")
    private long numberOfRooms;

    @Column(name = "floor")
    private int floor;

    @Column(name = "time_to_metro_on_foot")
    private int timeToMetroOnFoot;

    @Column(name = "balcony")
    private Boolean balcony;

    @Column(name = "view")
    @Enumerated(EnumType.STRING)
    private View view;

    @Column(name = "house_name")
    private String houseName;

    @Column(name = "house_year")
    private Long houseYear;

    @Column(name = "house_number_of_floors")
    private int houseNumberOfFloors;

    @Column(name = "price")
    private Double price;
}
