package me.play9.urlshortener.web.webinterface;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class CreateRecordRequest {
    @NotBlank
    @Length(min = 3, max = 512)
    public String slug;

    @NotBlank
    public String targetUrl;

    public EXPIRE_DURATION expireDuration = EXPIRE_DURATION.MIN_15;

    public enum EXPIRE_DURATION {
        MIN_5,
        MIN_15,
        MIN_30,
        HOUR_1,
        HOUR_3,
        HOUR_12
    }
}
