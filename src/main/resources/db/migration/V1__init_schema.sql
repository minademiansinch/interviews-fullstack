CREATE TABLE accounts (
    id                       VARCHAR(64)  PRIMARY KEY,
    name                     VARCHAR(128) NOT NULL,
    available_balance_cents  BIGINT       NOT NULL CHECK (available_balance_cents >= 0),
    reserved_balance_cents   BIGINT       NOT NULL DEFAULT 0 CHECK (reserved_balance_cents >= 0),
    version                  BIGINT       NOT NULL DEFAULT 0,
    created_at               TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at               TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE reservations (
    id                  UUID         PRIMARY KEY,
    account_id          VARCHAR(64)  NOT NULL REFERENCES accounts(id),
    recipient           VARCHAR(64)  NOT NULL,
    body                TEXT         NOT NULL,
    segments            INT          NOT NULL DEFAULT 1,
    idempotency_key     VARCHAR(128),
    payload_hash        VARCHAR(128),
    amount_cents        BIGINT       NOT NULL CHECK (amount_cents > 0),
    status              VARCHAR(32)  NOT NULL,
    carrier_message_id  VARCHAR(128),
    expires_at          TIMESTAMPTZ  NOT NULL,
    created_at          TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX uq_reservations_account_idempotency_key
    ON reservations (account_id, idempotency_key)
    WHERE idempotency_key IS NOT NULL;

CREATE INDEX idx_reservations_account_created
    ON reservations (account_id, created_at DESC);

CREATE INDEX idx_reservations_status_expires
    ON reservations (status, expires_at);

CREATE TABLE outbox_events (
    id               UUID         PRIMARY KEY,
    aggregate_id     VARCHAR(64)  NOT NULL,
    event_type       VARCHAR(64)  NOT NULL,
    payload          TEXT         NOT NULL,
    status           VARCHAR(32)  NOT NULL DEFAULT 'PENDING',
    attempts         INT          NOT NULL DEFAULT 0,
    next_attempt_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    last_error       VARCHAR(512),
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_outbox_events_dispatch ON outbox_events (status, next_attempt_at);
