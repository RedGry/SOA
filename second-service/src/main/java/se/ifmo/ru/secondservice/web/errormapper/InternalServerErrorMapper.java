package se.ifmo.ru.secondservice.web.errormapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import se.ifmo.ru.secondservice.utils.ResponseUtils;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

//@Provider
//@Produces(MediaType.APPLICATION_JSON)
//public class InternalServerErrorMapper implements ExceptionMapper<Throwable> {
//    @Inject
//    ResponseUtils responseUtils;
//
//    @Override
//    public Response toResponse(Throwable exception) {
//        if (StringUtils.isEmpty(exception.getMessage())) {
//            return responseUtils.buildResponseWithMessage(
//                    Response.Status.NOT_FOUND,
//                    "Something went wrong..."
//            );
//        }
//        return responseUtils.buildResponseWithMessage(Response.Status.NOT_FOUND, "Not found");
//    }
//}
