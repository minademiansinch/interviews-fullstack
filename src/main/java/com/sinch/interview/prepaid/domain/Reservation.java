package com.sinch.interview.prepaid.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    private UUID id;

    @Column(name = "account_id", length = 64, nullable = false)
    private String accountId;

    @Column(length = 64, nullable = false)
    private String recipient;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @Column(nullable = false)
    private int segments;

    @Column(name = "idempotency_key", length = 128)
    private String idempotencyKey;

    @Column(name = "payload_hash", length = 128)
    private String payloadHash;

    @Column(name = "amount_cents", nullable = false)
    private long amountCents;

    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    private ReservationStatus status;

    @Column(name = "carrier_message_id", length = 128)
    private String carrierMessageId;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Reservation() {
    }

    public Reservation(
            String accountId,
            String recipient,
            String body,
            int segments,
            String idempotencyKey,
            String payloadHash,
            long amountCents,
            ReservationStatus status,
            Instant expiresAt
    ) {
        this.id = UUID.randomUUID();
        this.accountId = accountId;
        this.recipient = recipient;
        this.body = body;
        this.segments = segments;
        this.idempotencyKey = idempotencyKey;
        this.payloadHash = payloadHash;
        this.amountCents = amountCents;
        this.status = status;
        this.expiresAt = expiresAt;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getBody() {
        return body;
    }

    public int getSegments() {
        return segments;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getPayloadHash() {
        return payloadHash;
    }

    public long getAmountCents() {
        return amountCents;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getCarrierMessageId() {
        return carrierMessageId;
    }

    public void setCarrierMessageId(String carrierMessageId) {
        this.carrierMessageId = carrierMessageId;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
