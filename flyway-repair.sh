#!/bin/bash

# Use this script when the flyway migrate.sh script returns checksum errors - that's usually after a database restore or something like that.

if [ "$1" == "production" ] ; then
    export FLYWAY_URL="jdbc:postgresql://ec2-52-49-124-58.eu-west-1.compute.amazonaws.com:5432/d1eedgtqeb3trf?user=u7suk4t2o6dhoa&password=papojn4so6iis2e8lgbiijven8v&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"
elif [ "$1" == "preprod" ] ; then
    export FLYWAY_URL="jdbc:postgresql://ec2-52-16-61-148.eu-west-1.compute.amazonaws.com:5432/dbdonhsc1ctr08?user=u3psok1db6nd94&password=p3dn0f2bm25qre8rav6spd99dir&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"
else
    export FLYWAY_URL="jdbc:postgresql://localhost/xxshellxx?user=xxshellxx&password=password1"
fi

mvn flyway:repair

export FLYWAY_URL="jdbc:postgresql://localhost/xxshellxx?user=xxshellxx&password=password1"
