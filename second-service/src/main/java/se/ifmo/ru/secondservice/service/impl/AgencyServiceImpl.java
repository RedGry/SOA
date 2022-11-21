package se.ifmo.ru.secondservice.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import se.ifmo.ru.secondservice.external.client.CatalogRestClient;
import se.ifmo.ru.secondservice.mapper.FlatMapper;
import se.ifmo.ru.secondservice.service.api.AgencyService;
import se.ifmo.ru.secondservice.service.model.Flat;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.List;

@ApplicationScoped
public class AgencyServiceImpl implements AgencyService {
    @Inject
    private CatalogRestClient catalogRestClient;
    @Inject
    private FlatMapper flatMapper;

    @Override
    public Flat findFlatWithBalcony(boolean cheapest, boolean balcony) {

        List<Flat> flats = flatMapper.fromRestClient(catalogRestClient.getAllFlats());

        Flat bestFlat = new Flat();

        if (CollectionUtils.isNotEmpty(flats)){
            Double price = 0.0;
            int i = 0;
            for (Flat flat : flats){
                if (flat.getBalcony() == balcony && i == 0) {
                    price = flat.getPrice();
                    i++;
                }
                if (cheapest){
                    if (flat.getBalcony() == balcony && price <= flat.getPrice()) {
                        bestFlat = flat;
                    }
                } else {
                    if (flat.getBalcony() == balcony && price >= flat.getPrice()) {
                        bestFlat = flat;
                    }
                }
            }
        } else {
            throw new BadRequestException("Нет списка квартир :(");
        }

        if (bestFlat.getId() == null){
            throw new BadRequestException("Не найдено подходящей квартиры :(");
        }

        return bestFlat;
    }

    @Override
    public long getMostExpensiveFlat(long id1, long id2, long id3) {
        Flat flat1 = flatMapper.fromRestClient(catalogRestClient.getFlatById(id1));
        if (flat1 == null) {
            throw new BadRequestException("Flat with id " + id1 + " not found");
        }

        Flat flat2 = flatMapper.fromRestClient(catalogRestClient.getFlatById(id2));
        if (flat2 == null) {
            throw new BadRequestException("Flat with id " + id2 + " not found");
        }

        Flat flat3 = flatMapper.fromRestClient(catalogRestClient.getFlatById(id3));
        if (flat3 == null) {
            throw new BadRequestException("Flat with id " + id3 + " not found");
        }

        return flat1.getPrice() >= flat2.getPrice() ?
                flat1.getPrice() >= flat3.getPrice() ?
                        flat1.getId() : flat3.getId() :
                flat2.getPrice() >= flat3.getPrice() ?
                        flat2.getId() : flat3.getId();
    }
}
