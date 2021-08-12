package me.play9.urlshortener.web;

import me.play9.urlshortener.config.AppConfig;
import me.play9.urlshortener.model.ShortenRecord;
import me.play9.urlshortener.model.ShortenRecordRepository;
import me.play9.urlshortener.service.ShortenRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;

@Controller
public class WebController {
    @Autowired
    ShortenRecordService service;
    @Autowired
    AppConfig appConfig;
    @Autowired
    ShortenRecordRepository repository;

    private ModelAndView indexModelAndView;

    @PostConstruct
    private void setup() {
        indexModelAndView = new ModelAndView();
        indexModelAndView.setViewName("index.html");
        indexModelAndView.addObject("baseUrl", appConfig.baseUrl + "/");
    }

    @GetMapping("/")
    public ModelAndView index() {
        return indexModelAndView;
    }

    @GetMapping("/create")
    public String create() {
        ZonedDateTime now = ZonedDateTime.now();

        var record = new ShortenRecord();
        record.slug = "okok";
        record.targetUrl = "https://google.com";
        record.expiredAt = now.plus(Duration.ofMinutes(1));
        record.createdAt = now;
        repository.save(record);

        return "index";
    }

    @GetMapping("/{slug}")
    public RedirectView redirect(@PathVariable("slug") String slug) {
        Optional<ShortenRecord> recordOptional = service.get(slug);
        if (recordOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        }
        return new RedirectView(recordOptional.get().targetUrl);
    }
}
