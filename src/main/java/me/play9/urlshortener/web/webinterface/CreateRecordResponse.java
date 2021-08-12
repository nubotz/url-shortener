package me.play9.urlshortener.web.webinterface;

import javax.validation.constraints.NotBlank;

public class CreateRecordResponse {
    public static CreateRecordResponse success(String slug, String url) {
        var response = new CreateRecordResponse();
        response.success = true;
        response.slug = slug;
        response.url = url;
        return response;
    }

    public static CreateRecordResponse failed(String message) {
        var response = new CreateRecordResponse();
        response.success = false;
        response.errorMessage = message;
        return response;
    }

    @NotBlank
    public Boolean success;

    public String slug;

    public String url;

    public String errorMessage;
}
