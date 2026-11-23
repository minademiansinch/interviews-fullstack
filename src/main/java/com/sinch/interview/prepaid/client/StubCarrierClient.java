package com.sinch.interview.prepaid.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class StubCarrierClient implements CarrierClient {

    private static final Logger log = LoggerFactory.getLogger(StubCarrierClient.class);

    @Override
    public String send(UUID reservationId, String recipient, String body) {
        simulateLatency();

        if ((recipient != null && recipient.endsWith("999"))
                || (body != null && body.contains("FAIL_CARRIER"))) {
            log.warn("[StubCarrierClient] Simulated transient carrier failure for reservationId={} recipient={}",
                    reservationId, recipient);
            throw new CarrierTransientException("Simulated carrier gateway timeout");
        }

        String carrierMessageId = "carrier-" + UUID.randomUUID().toString().substring(0, 8);
        log.info("[StubCarrierClient] Accepted reservationId={} recipient={} carrierMessageId={}",
                reservationId, recipient, carrierMessageId);
        return carrierMessageId;
    }

    private void simulateLatency() {
        long delayMs = ThreadLocalRandom.current().nextLong(200, 800);
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CarrierTransientException("Interrupted while contacting carrier");
        }
    }
}
