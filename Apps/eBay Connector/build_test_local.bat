call mvn compile package -Dmaven.test.skip=true
call sls invoke local -f handle -p event-sqs.json