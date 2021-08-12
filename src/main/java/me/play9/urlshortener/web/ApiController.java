package me.play9.urlshortener.web;

import lombok.extern.log4j.Log4j2;
import me.play9.urlshortener.config.AppConfig;
import me.play9.urlshortener.model.ShortenRecord;
import me.play9.urlshortener.service.ShortenRecordService;
import me.play9.urlshortener.web.webinterface.CheckSlugRequest;
import me.play9.urlshortener.web.webinterface.CheckSlugResponse;
import me.play9.urlshortener.web.webinterface.CreateRecordRequest;
import me.play9.urlshortener.web.webinterface.CreateRecordResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Log4j2
@RestController
@RequestMapping("/api/v1/")
public class ApiController {
    @Autowired
    ShortenRecordService service;
    @Autowired
    AppConfig appConfig;

    @PutMapping("/records/check-slug")
    public CheckSlugResponse checkSlug(@Valid @RequestBody CheckSlugRequest request) {
        boolean canUse = !service.exist(request.slug);
        return new CheckSlugResponse(canUse);
    }

    @PostMapping("/records")
    public CreateRecordResponse create(@Valid @RequestBody CreateRecordRequest request) {
        try {
            ShortenRecord record = service.create(request);
            String url = appConfig.baseUrl + "/" + record.slug;

            return CreateRecordResponse.success(record.slug, url);
        } catch (Exception ex) {
            log.warn("cannot create record", ex);

            return CreateRecordResponse.failed("cannot create record");
        }
    }
}
