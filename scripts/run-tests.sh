#!/usr/bin/env bash
set -uo pipefail

cd "$(dirname "${BASH_SOURCE[0]}")/.."

ORBSTACK_SOCK="$HOME/.orbstack/run/docker.sock"
if [[ ! -S /var/run/docker.sock && -S "$ORBSTACK_SOCK" ]]; then
  export DOCKER_HOST="unix://$ORBSTACK_SOCK"
  export TESTCONTAINERS_DOCKER_CLIENT_STRATEGY="org.testcontainers.dockerclient.EnvironmentAndSystemPropertyClientProviderStrategy"
  export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE="/var/run/docker.sock"
fi

while true; do
  echo "[tests] Running ./gradlew test..."
  ./gradlew test --console=plain || true
  echo ""
  echo "[tests] Finished. Press Enter in this pane (or 'r' in mprocs) to re-run."
  IFS= read -r _ || break
done
