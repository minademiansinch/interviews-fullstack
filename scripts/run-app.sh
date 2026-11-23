#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "${BASH_SOURCE[0]}")/.."

cleanup() {
  trap - INT TERM HUP
  pkill -TERM -P $$ 2>/dev/null || true
  wait 2>/dev/null || true
  exit 0
}
trap cleanup INT TERM HUP

echo "[app] Waiting for Postgres (TCP 127.0.0.1:5432 + prepaid_db)..."
until docker compose exec -T postgres pg_isready -h 127.0.0.1 -p 5432 -U prepaid -d prepaid_db >/dev/null 2>&1 \
  && docker compose exec -T postgres psql -U prepaid -d prepaid_db -tAc "SELECT 1" >/dev/null 2>&1; do
  sleep 1 &
  wait $! || true
done

echo "[app] Building bootJar..."
./gradlew --no-daemon bootJar --console=plain

echo "[app] Starting Spring Boot on :8080..."
exec java -jar build/libs/prepaid-messaging-0.0.1-SNAPSHOT.jar
