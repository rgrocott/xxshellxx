#!/usr/bin/env bash
# PRODUCTION:
# export FLYWAY_URL="jdbc:postgresql://ec2-176-34-98-216.eu-west-1.compute.amazonaws.com:5582/d3r2du6spc77l3?user=uc5p3cgq3lkj32&password=p2oa9p4vjh47pscn3rr2vn40v4j&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"
# DEVELOPMENT:
echo '1'
export FLYWAY_URL="jdbc:postgresql://localhost/xxshellxx?user=xxshellxx&password=password1"
export DATABASE_URL=postgres://clearcompress:password1@localhost/clearcompress
export MAILGUN_API_KEY=key-ea5952510dac9891554b2fdd85f9b856
export MAILGUN_DOMAIN=app2a3def9fa2ce471c900a614d724aada7.mailgun.org
export MAILGUN_INTERNAL_SEND_TO=test.recipient@domain.com
# Change flag to Y to enable notifications
export MAILGUN_NOTIFICATIONS_ENABLED_YN=N
if [ "$1" == "debug" ] ; then
    JAVA_OPTS="$JAVA_OPTS -Dxxshellxx.debug.memory=true"
    export JAVA_OPTS
fi

java $JAVA_OPTS -jar target/dependency/webapp-runner.jar --port 9999 target/*.war


