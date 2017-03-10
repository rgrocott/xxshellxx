# Shell web app
Company name here

# Development Environment Setup
## Prerequisites
- Install git
- Install Java 8 SDK
- Install Maven
- Install an IDE (e.g. Eclipse, Intelli J, Netbeans)
- Install postgres
- Install pgadmin
- Install Heroku toolbelt

## Environment Variables 
The app requires an environment variable to be set: DATABASE_URL.  An example on the syntax is available in the start.sh.  This
file is only appropriate for the local development environment. Heroku set this environment variable automatically.

## Setup for Mac
- Install "brew" and execute the following:
  - brew install java
  - brew install maven
- Install git by going to http://git-scm.com/download/mac
- run "git config --global credential.helper osxkeychain" to reduce password prompting
- Install heroku toolbelt by going to https://devcenter.heroku.com/articles/getting-started-with-java#set-up and downloading and installing the software
  
# Development Workflow
## Edit, compile, checkin cycle
- run "heroku login" to login to heroku (only required once per day)
- Edit project files via IDE/Editor
- run "mvn package" on the command line to compile, package and execute the unti tests
- run "./start.sh" to start up the app on port 9999
- open browser to [http://localhost:9999](http://localhost:9999/)
- run "git add -A" to add changes
- run "git commit -am 'a comment about the changes'" to commit changes
- run "git push origin master" to push to github.com
- run "git push heroku master" to push and deploy to heroku (pre production)
- run "heroku logs" or "heroku logs --tail" to obtain heroku logs
- run "heroku open" to open browser to heroku hosted site

# Operations
## Logs
- logs can be downloaded by login in to heroku.com and navigating to the dashboard
- from there navigate to the xxshellxx application [https://dashboard.heroku.com/apps/xxshellxx/resources](xxshellxx)
- Click the Paper Trail addon and go to the Paper Trail dashboard
- Here you can download archived log messages (one archive file per day)
- Navigating to the Paper Trail Events tab you can see the current set of live events
- These events can be filtered and alerts configured as required

## Database
- The connection settings to the Postgres database can be obtained from the heroku management console
- These settings can be used with pgAdmin3 tool to connect to the database and administer it
- In particular, database backups can be manually triggered and migration scripts executed
- These manual steps will be required during code release
- Code migrations should be done through the migrate.sh script

## Release Procedure
- The code should be tagged "git tag -f -a v0.1 -m 'Version 0.1 - release comment'"
- execute "git push --tags origin master"
- backup the database in production (named pre-install and the tag version number e.g. v0.1-pre-install.sql)
- save backup in src/resources/db/backups
- execute upgrade scripts on production database using migrate.sh script:
  - database migration scripts live in resources/db/migrations and follow the strict flywaydb naming convension
  - migrate.sh (for development)
  - migrate.sh production (for production migration)
- backup the database in production (named post-install and the tag version number e.g. v0.1-post-install.sql)
- save backup in src/resources/db/backups
- execute "git push --tags heroku master"
- test that the code is working
- code is now live

## Rollback Procedure
- restore database to pre-install state (should have a set of backups for all previous installations)
- execute "heroku releases" to see list of code releases
- rollback by executing "heroku rollback v12" where v12 is the version you want to rollback to
- This rollback procedure can also be done via the heroku dashboard

## Spin up a parallel environment
- Heroku supports the creation of a parallel environment from the existing app.
- Execute "heroku fork -a xxshellxx new-app" where new-app is the name of the new clone
- For example "heroku fork -a xxshellxx xxshellxx-pre-prod" creates an identical preprod environment with a copy of production data
