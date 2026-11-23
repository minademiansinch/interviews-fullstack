#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "${BASH_SOURCE[0]}")/.."

if ! command -v mprocs >/dev/null 2>&1; then
  brew install mprocs
fi

exec mprocs
