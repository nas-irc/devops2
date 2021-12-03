#!/bin/sh

/liquibase/liquibase \
  --driver="${DRIVER}" \
  --url="${URL}" \
  --changeLogFile="${CHANGELOGFILE}" \
  --username="${USERNAME}" \
  --password="${PASSWORD}" \
  --logLevel="${LOGLEVEL}" \
  "${CMD}"
