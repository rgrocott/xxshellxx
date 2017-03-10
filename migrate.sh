#!/bin/bash

# migration to production or dev

if [ "$1" == "production" ] ; then
    export FLYWAY_URL="jdbc:postgresql://dbserverid.eu-west-1.compute.amazonaws.com:5432/database-name?user=user-id&password=password-goes-here&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"
elif [ "$1" == "preprod" ] ; then
    export FLYWAY_URL="jdbc:postgresql://dbserverid.eu-west-1.compute.amazonaws.com:5432/database-name?user=user-id&password=password-goes-here&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"
else
    export FLYWAY_URL="jdbc:postgresql://localhost/xxshellxx?user=xxshellxx&password=password1"
fi

mvn flyway:migrate

export FLYWAY_URL="jdbc:postgresql://localhost/xxshellxx?user=xxshellxx&password=password1"
