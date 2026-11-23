package com.sinch.interview.prepaid.client;

import java.util.UUID;

public interface CarrierClient {

    String send(UUID reservationId, String recipient, String body);
}
