package com.sinch.interview.prepaid.repository;

import com.sinch.interview.prepaid.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    List<Reservation> findTop50ByAccountIdOrderByCreatedAtDesc(String accountId);

    Optional<Reservation> findByAccountIdAndIdempotencyKey(String accountId, String idempotencyKey);
}
