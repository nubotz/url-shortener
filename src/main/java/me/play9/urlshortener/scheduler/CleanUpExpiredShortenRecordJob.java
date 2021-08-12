package me.play9.urlshortener.scheduler;

import me.play9.urlshortener.service.ShortenRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class CleanUpExpiredShortenRecordJob {
    @Autowired
    ShortenRecordService shortenRecordService;

    @Scheduled(fixedRate = 30 * 1000)
    public void cleanup() {
        shortenRecordService.removeExpired();
    }
}
