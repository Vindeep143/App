#!/usr/bin/env bash

cat vars.txt | grep -v '^#' | while read line; do
    key=$(echo $line | cut -d= -f1)
    value=$(echo $line | cut -d= -f2-)
    echo $key $value
    result=$(curl -s -X PUT -d "$value" http://localhost:8500/v1/kv/vars/$key)
    if [ "$result" != "true" ]; then
        echo "ERROR loading $value into $key for $prefix"
        exit 1
    fi
done
