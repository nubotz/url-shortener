package me.play9.urlshortener.model;

import org.hibernate.validator.constraints.Length;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(indexes = {
        @Index(columnList = "slug", unique = true),
        @Index(columnList = "expired_at")
})
public class ShortenRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @NonNull
    @Length(min = 3, max = 512)
    @Column(name = "slug")
    public String slug;

    @NonNull
    @Column(name = "target_url", columnDefinition = "text")
    public String targetUrl;

    @NonNull
    @Column(name = "expired_at")
    public ZonedDateTime expiredAt;

    @NonNull
    @Column(name = "created_at")
    public ZonedDateTime createdAt;
}
