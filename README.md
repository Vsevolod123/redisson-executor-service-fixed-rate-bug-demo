# redisson-executor-service-fixed-rate-bug-demo
minimal example to reproduce bug with redisson scheduler executor service and fixed rate schedule

## run demo
1. start redis with docker-compose
```shell
docker-compose up -d -f docker-compose/docker-compose.yml
```
2. build jar artifact
```shell
mvn clean install
```
3. start jar artifact
```shell
java -jar target/redisson-executor-service-fixed-rate-bug-demo-1.0-SNAPSHOT.jar
```

## conclusion
After some time, we can see that scheduled task stops executing.

Result of command tells that task was removed from database:
```shell
redis-cli keys '*'
```
After start exists this keys:
```
1) "{demo-executor-service:org.redisson.executor.RemoteExecutorService}:retry-interval"
2) "{demo-executor-service:org.redisson.executor.RemoteExecutorService}:tasks"
3) "{demo-executor-service:org.redisson.executor.RemoteExecutorService}:counter"
4) "{demo-executor-service:org.redisson.executor.RemoteExecutorService}:scheduler"
5) "demo-counter"
```
After some time "scheduler" and "tasks" keys were removed:  
```
1) "{demo-executor-service:org.redisson.executor.RemoteExecutorService}:retry-interval"
2) "{demo-executor-service:org.redisson.executor.RemoteExecutorService}:counter"
3) "demo-counter"
```