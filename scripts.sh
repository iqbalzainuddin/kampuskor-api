#!/bin/bash

# Load .env if it exists
if [ -f .env ]; then
  # Export all variables in .env
  set -a
  source .env
  set +a
fi

case "$1" in
	migrate)
		./mvnw flyway:migrate 
	;;
	migrate:repair)
		./mvnw flyway:repair
	;;
	hash-password)
		./mvnw compile exec:java -Dexec.mainClass="com.kampuskor.restservice.utils.security.PasswordHashGenerator" -Dexec.args="$2"
	;;
esac
