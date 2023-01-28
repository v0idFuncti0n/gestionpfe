package com.gestionpfe.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestionpfe.model.responses.error.ErrorResponse;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JSONResponseUtil {

    public static HttpServletResponse writeJSONInResponse(ErrorResponse errorResponse, HttpServletResponse response, int status) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
        return response;
    }
}
