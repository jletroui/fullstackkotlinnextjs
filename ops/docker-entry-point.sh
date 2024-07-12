#!/bin/sh

if [ -z ${ENV+x} ]; then
  echo "ENV must be set. Aborting."
  exit 1
fi

/app/bin/ejson decrypt /app/config/backend."${ENV}".ejson | /app/bin/backend
