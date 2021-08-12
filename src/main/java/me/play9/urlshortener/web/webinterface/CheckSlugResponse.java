package me.play9.urlshortener.web.webinterface;

import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class CheckSlugResponse {
    @NotNull
    public Boolean success;
}
