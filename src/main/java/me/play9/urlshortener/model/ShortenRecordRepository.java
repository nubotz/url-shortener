package me.play9.urlshortener.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface ShortenRecordRepository extends CrudRepository<ShortenRecord, Long> {
    long countBySlug(String slug);

    Optional<ShortenRecord> findBySlug(String slug);

    @Transactional
    long deleteByExpiredAtBefore(ZonedDateTime time);
}
