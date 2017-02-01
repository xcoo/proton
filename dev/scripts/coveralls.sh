#!/bin/bash

set -e

echo "coverage for $1"

./lein-stable with-profile dev,$1 cloverage --coveralls
curl -F 'json_file=@target/coverage/coveralls.json' 'https://coveralls.io/api/v1/jobs'
