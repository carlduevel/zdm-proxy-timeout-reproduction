# zdm-proxy-timeout-reproduction



## Howto
Traffic control currently does not work with Docker for Mac so a Linux host/VM is needed. 
1. Run `docker-compose up` and wait until all containers are up. 
2. Run `curl -d'delay=2000ms' localhost:4080/zdm-proxy-timeout-reproduction-cassandra_2_3-1` to introduce latency in the target C* cluster

## Result

While the cassandra clients continues to write to the source cluster
the proxy client terminates with a timeout error::

```
zdm-proxy-timeout-reproduction-proxy_client-1      | Exception in thread "main" com.datastax.oss.driver.api.core.DriverTimeoutException: Query timed out after PT2S
zdm-proxy-timeout-reproduction-proxy_client-1      |    at com.datastax.oss.driver.api.core.DriverTimeoutException.copy(DriverTimeoutException.java:34)
zdm-proxy-timeout-reproduction-proxy_client-1      |    at com.datastax.oss.driver.internal.core.util.concurrent.CompletableFutures.getUninterruptibly(CompletableFutures.java:149)
zdm-proxy-timeout-reproduction-proxy_client-1      |    at com.datastax.oss.driver.internal.core.cql.CqlRequestSyncProcessor.process(CqlRequestSyncProcessor.java:53)
zdm-proxy-timeout-reproduction-proxy_client-1      |    at com.datastax.oss.driver.internal.core.cql.CqlRequestSyncProcessor.process(CqlRequestSyncProcessor.java:30)
zdm-proxy-timeout-reproduction-proxy_client-1      |    at com.datastax.oss.driver.internal.core.session.DefaultSession.execute(DefaultSession.java:230)
zdm-proxy-timeout-reproduction-proxy_client-1      |    at com.datastax.oss.driver.api.core.cql.SyncCqlSession.execute(SyncCqlSession.java:54)
zdm-proxy-timeout-reproduction-proxy_client-1      |    at com.datastax.oss.driver.api.core.cql.SyncCqlSession.execute(SyncCqlSession.java:78)
zdm-proxy-timeout-reproduction-proxy_client-1      |    at proxy.client.App.write(App.java:32)
zdm-proxy-timeout-reproduction-proxy_client-1      |    at proxy.client.App.main(App.java:24)

```
