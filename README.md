# Prepaid Messaging Console — Interview Project

### Background
Sinch customers hold a prepaid balance and send SMS/RCS messages from a Micro-Frontend (MFE) embedded in the Sinch Portal, while automated traffic bursts up to 50k–100k messages/sec. Each message:

1. Reserves funds upfront before carrier handoff (`available -> reserved`).
2. Is dispatched to an external carrier outside the database transaction.
3. Later settles via async delivery report (DLR) webhooks (`DELIVERED` or `FAILED -> REFUNDED`), including out-of-order callbacks and multi-segment pricing adjustments.

## Quick Start

```sh
chmod +x ./scripts/start.sh
./scripts/start.sh
```

> For the sake of time, please start Docker on the day of the interview and ensure no other containers are running, especially Postgres.

`mprocs.yaml` starts:
- `postgres` — PostgreSQL 16 via `docker compose up` (port `5432`)
- `app` — waits for Postgres TCP `5432` + `prepaid_db`, builds `bootJar`, then runs `java -jar` (port `8080`)
- `tests` — runs `./gradlew test` with Testcontainers on start and stays up (press `Enter` in the pane or `r` in `mprocs` to re-run)
- `psql` — waits for Postgres, then opens an interactive `psql` shell into `prepaid_db`
- `shell` — interactive terminal

### Quick Sanity Check

Verify it's running:

```sh
# List seeded accounts
curl -s http://localhost:8080/v1/accounts | jq

# Single account lookup (includes recent reservations/messages)
curl -s http://localhost:8080/v1/accounts/acct-acme-prod | jq

# Check tables in psql (or use the psql pane in mprocs)
SELECT id, name, available_balance_cents, reserved_balance_cents, version FROM accounts;
SELECT id, account_id, recipient, amount_cents, status FROM reservations;
```
---

## Pre-Seeded Accounts (`V2__seed_accounts.sql`)

| Account ID | Name | Available (cents) | Reserved (cents) | Use For |
|---|---|---|---|---|
| `acct-acme-prod` | `Acme Messaging Prod` | `5000` | `0` | Happy path MFE + `POST /messages` |
| `acct-low-balance` | `Low Balance Startup` | `15` | `0` | Insufficient funds (`409` / `422`) |
| `acct-zero-balance` | `Empty Prepaid Account` | `0` | `0` | Zero-balance rejection |
| `acct-high-volume` | `Flash Sale Enterprise` | `1000000` | `500` | Pre-seeded `SENT`/`DELIVERED`/`REFUNDED` history |

## Stub `CarrierClient` Behavior
`StubCarrierClient` simulates slow/flaky external carrier I/O (artificial latency ~200–800 ms):
- Recipients ending in `999` or bodies containing `FAIL_CARRIER` throw `CarrierTransientException` to simulate transient handoff failures for Part 2/Part 3 retry testing.
