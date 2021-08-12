package me.play9.urlshortener.service;

import me.play9.urlshortener.web.webinterface.CreateRecordRequest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class ShortenRecordServiceTest {
    @Test
    public void durationMap() {
        assertThat(ShortenRecordService.DURATION_MAP.keySet())
                .containsAll(Arrays.asList(CreateRecordRequest.EXPIRE_DURATION.values()));
    }
}
