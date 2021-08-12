package me.play9.urlshortener.web.webinterface;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class CheckSlugRequest {
    @NotBlank
    @Length(min = 3, max = 512)
    public String slug;
}
