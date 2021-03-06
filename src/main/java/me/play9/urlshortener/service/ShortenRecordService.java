package me.play9.urlshortener.service;

import lombok.extern.log4j.Log4j2;
import me.play9.urlshortener.model.ShortenRecord;
import me.play9.urlshortener.model.ShortenRecordRepository;
import me.play9.urlshortener.web.webinterface.CreateRecordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Service
public class ShortenRecordService {
    private static final String[] RESERVED_SEGMENTS = {"api", "css", "js"};
    private static final char[] RESERVED_CHARS = {'/', '?', '#'};

    static Map<CreateRecordRequest.EXPIRE_DURATION, Duration> DURATION_MAP = Map.ofEntries(
            Map.entry(CreateRecordRequest.EXPIRE_DURATION.MIN_5, Duration.ofMinutes(5)),
            Map.entry(CreateRecordRequest.EXPIRE_DURATION.MIN_15, Duration.ofMinutes(15)),
            Map.entry(CreateRecordRequest.EXPIRE_DURATION.MIN_30, Duration.ofMinutes(30)),
            Map.entry(CreateRecordRequest.EXPIRE_DURATION.HOUR_1, Duration.ofHours(1)),
            Map.entry(CreateRecordRequest.EXPIRE_DURATION.HOUR_3, Duration.ofHours(3)),
            Map.entry(CreateRecordRequest.EXPIRE_DURATION.HOUR_12, Duration.ofHours(12))
    );

    @Autowired
    ShortenRecordRepository repository;

    public boolean exist(String slug) {
        return repository.existsBySlug(slug);
    }

    public ShortenRecord create(CreateRecordRequest request) {
        validate(request);

        ZonedDateTime now = ZonedDateTime.now();

        var record = new ShortenRecord();
        record.slug = request.slug;
        record.targetUrl = request.targetUrl;
        record.expiredAt = now.plus(DURATION_MAP.get(request.expireDuration));
        record.createdAt = now;
        repository.save(record);

        return record;
    }

    public Optional<ShortenRecord> get(String slug) {
        return repository.findBySlug(slug);
    }

    public void removeExpired() {
        long count = repository.deleteByExpiredAtBefore(ZonedDateTime.now());
        if (count > 0) {
            log.info("remove expired record, count={}", count);
        }
    }

    private void validate(CreateRecordRequest request) {
        for (char c : RESERVED_CHARS) {
            if (request.slug.indexOf(c) != -1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid slug");
            }
        }

        for (String reserved : RESERVED_SEGMENTS) {
            if (request.slug.equalsIgnoreCase(reserved) || request.slug.startsWith(reserved + "/")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid slug");
            }
        }
    }
}
