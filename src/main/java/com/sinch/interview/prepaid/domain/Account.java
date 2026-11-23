package com.sinch.interview.prepaid.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(length = 64, nullable = false)
    private String id;

    @Column(length = 128, nullable = false)
    private String name;

    @Column(name = "available_balance_cents", nullable = false)
    private long availableBalanceCents;

    @Column(name = "reserved_balance_cents", nullable = false)
    private long reservedBalanceCents;

    @Version
    @Column(nullable = false)
    private long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Account() {
    }

    public Account(String id, String name, long availableBalanceCents, long reservedBalanceCents) {
        this.id = id;
        this.name = name;
        this.availableBalanceCents = availableBalanceCents;
        this.reservedBalanceCents = reservedBalanceCents;
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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getAvailableBalanceCents() {
        return availableBalanceCents;
    }

    public void setAvailableBalanceCents(long availableBalanceCents) {
        this.availableBalanceCents = availableBalanceCents;
    }

    public long getReservedBalanceCents() {
        return reservedBalanceCents;
    }

    public void setReservedBalanceCents(long reservedBalanceCents) {
        this.reservedBalanceCents = reservedBalanceCents;
    }

    public long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
