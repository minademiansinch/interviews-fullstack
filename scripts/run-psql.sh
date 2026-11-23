#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "${BASH_SOURCE[0]}")/.."

trap 'exit 0' INT TERM HUP EXIT

echo "[psql] Waiting for Postgres (TCP 127.0.0.1:5432 + prepaid_db)..."
until docker compose exec -T postgres pg_isready -h 127.0.0.1 -p 5432 -U prepaid -d prepaid_db >/dev/null 2>&1 \
  && docker compose exec -T postgres psql -U prepaid -d prepaid_db -tAc "SELECT 1" >/dev/null 2>&1; do
  sleep 1 &
  wait $! || true
done

exec docker compose exec postgres psql -U prepaid -d prepaid_db
