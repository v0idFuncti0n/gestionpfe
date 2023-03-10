package com.gestionpfe.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestionpfe.model.responses.AppUserResponse;
import com.gestionpfe.model.responses.error.ErrorResponse;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JSONResponseUtil {

    public static HttpServletResponse writeJSONInResponse(ErrorResponse errorResponse, HttpServletResponse response, int status) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
        return response;
    }

    public static HttpServletResponse writeJSONInResponse(AppUserResponse appUserResponse, HttpServletResponse response, int status) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), appUserResponse);
        return response;
    }
}
