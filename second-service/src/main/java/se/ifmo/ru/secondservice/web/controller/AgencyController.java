package se.ifmo.ru.secondservice.web.controller;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import se.ifmo.ru.secondservice.mapper.FlatMapper;
import se.ifmo.ru.secondservice.service.api.AgencyService;
import se.ifmo.ru.secondservice.utils.ResponseUtils;
import se.ifmo.ru.secondservice.web.model.response.IdResponseDto;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/agency")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AgencyController {
    @Inject
    AgencyService agencyService;

    @Inject
    ResponseUtils responseUtils;

    @Inject
    FlatMapper flatMapper;

    @GET
    @Path("/find-with-balcony/{cheapest}/{balcony}")
    public Response getFlatWithBalcony(@PathParam("cheapest") boolean cheapest, @PathParam("balcony") boolean balcony){
        return Response.ok()
                .entity(flatMapper.toDto(agencyService.findFlatWithBalcony(cheapest, balcony)))
                .build();
    }

    @GET
    @Path("/get-most-expensive/{id1}/{id2}/{id3}")
    public long getFlatMostExpensive(@PathParam("id1") long id1, @PathParam("id2") long id2, @PathParam("id3") long id3){
        return agencyService.getMostExpensiveFlat(id1, id2, id3);
//        return Response.ok()
//                .entity(IdResponseDto.builder().id(agencyService.getMostExpensiveFlat(id1, id2, id3)).build())
//                .build();
    }

}
