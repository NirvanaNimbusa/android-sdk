#!/usr/bin/env bash

# exit with failure when an error occurs
set -e


# Allow script to be run from parent directory, and also from the base directory of the project
# Ref: https://stackoverflow.com/a/246128
SCRIPT_PARENT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
BASE_DIR=$(realpath "${SCRIPT_PARENT_DIR}/..")

cd "${BASE_DIR}/server"
python -m SimpleHTTPServer

