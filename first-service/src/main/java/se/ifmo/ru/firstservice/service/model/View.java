package se.ifmo.ru.firstservice.service.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
public enum View implements Serializable {
    STREET("street"),
    PARK("park"),
    BAD("bad"),
    GOOD("good"),
    TERRIBLE("terrible"),
    UNDEFINED("undefined");

    @Getter
    private final String value;

    @Override
    public String toString(){
        return value;
    }

    public static View fromValue(String value){
        return Arrays.stream(View.values())
                .filter(e -> Objects.equals(e.getValue(), value))
                .findFirst()
                .orElse(UNDEFINED);
    }
}
