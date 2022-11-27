package se.ifmo.ru.secondservice.external.client;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.Value;
import se.ifmo.ru.secondservice.external.model.FlatListGetResponseDto;
import se.ifmo.ru.secondservice.external.model.RestClientFlat;
import se.ifmo.ru.secondservice.web.model.response.FlatGetResponseDto;

import javax.ejb.Stateless;
import javax.enterprise.context.Initialized;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Stateless
public class CatalogRestClient {
    private Client client;
    private final String serviceUrl = "https://localhost:45382/api/v1";

    public RestClientFlat getFlatById(long id){
        String url = serviceUrl + "/catalog/flats/" + id;
        try {
            client = ClientBuilder.newClient();

            Response response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE).get();

            RestClientFlat flat = response.readEntity(RestClientFlat.class);

            client.close();

            return flat;

        } catch (ProcessingException e) {
            return null;
        }

    }

    public List<RestClientFlat> getAllFlats(){
        String url = serviceUrl + "/catalog/flats";
        client = ClientBuilder.newClient();

        Response response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE).get();

        FlatListGetResponseDto flats = response.readEntity(FlatListGetResponseDto.class);

        client.close();

        return flats.getFlatGetResponseDtos();
    }
}
