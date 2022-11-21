package se.ifmo.ru.secondservice.service.api;

import se.ifmo.ru.secondservice.service.model.Flat;

public interface AgencyService {
    Flat findFlatWithBalcony(boolean cheapest, boolean balcony);

    long getMostExpensiveFlat(long id1, long id2, long id3);
}
