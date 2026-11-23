package com.sinch.interview.prepaid.api;

import com.sinch.interview.prepaid.domain.Reservation;
import com.sinch.interview.prepaid.domain.ReservationStatus;

import java.time.Instant;
import java.util.UUID;

public record ReservationSummary(
        UUID id,
        String recipient,
        String body,
        int segments,
        long amountCents,
        ReservationStatus status,
        String carrierMessageId,
        Instant createdAt,
        Instant updatedAt
) {
    public static ReservationSummary from(Reservation reservation) {
        return new ReservationSummary(
                reservation.getId(),
                reservation.getRecipient(),
                reservation.getBody(),
                reservation.getSegments(),
                reservation.getAmountCents(),
                reservation.getStatus(),
                reservation.getCarrierMessageId(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt()
        );
    }
}
