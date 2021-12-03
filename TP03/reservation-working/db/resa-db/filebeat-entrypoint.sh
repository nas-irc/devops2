#!/bin/sh

filebeat -c /etc/filebeat/filebeat.yml \
  --path.home /usr/share/filebeat \
  --path.config /etc/filebeat \
  --path.data /var/lib/filebeat \
  --path.logs /var/log/filebeat &

docker-entrypoint.sh "$@"
