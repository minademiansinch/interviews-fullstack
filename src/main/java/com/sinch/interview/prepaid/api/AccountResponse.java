package com.sinch.interview.prepaid.api;

import com.sinch.interview.prepaid.domain.Account;

import java.time.Instant;
import java.util.List;

public record AccountResponse(
        String id,
        String name,
        long availableBalanceCents,
        long reservedBalanceCents,
        long version,
        Instant updatedAt,
        List<ReservationSummary> recentMessages
) {
    public static AccountResponse from(Account account, List<ReservationSummary> recentMessages) {
        return new AccountResponse(
                account.getId(),
                account.getName(),
                account.getAvailableBalanceCents(),
                account.getReservedBalanceCents(),
                account.getVersion(),
                account.getUpdatedAt(),
                recentMessages
        );
    }
}
