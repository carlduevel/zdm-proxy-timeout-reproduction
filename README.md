# zdm-proxy-timeout-reproduction

Reproduces [issue 90 of
zdm-proxy](https://github.com/datastax/zdm-proxy/issues/90).

## Howto

1. Run `docker-compose up` and wait until all containers are up. 
2. Stop one of the cassandra nodes in the source cluster. E. g. `docker stop invalid_peer_reproduction-cassandra_1_3-1`

## Result

While the cassandra clients continues to create connections an read from
the source cluster the proxy client will contact the stopped node and throw this
error:

```
Exception in thread "main" com.datastax.oss.driver.api.core.AllNodesFailedException: Could not reach any contact point, make sure you've provided valid addresses (showing first 3 nodes, use getAllErrors() for more): Node(endPoint=proxy_1/172.21.0.6:9042, hostId=null, hashCode=728a646): [com.datastax.oss.driver.api.core.DriverTimeoutException: [s22|control|id: 0x63f66325, L:/172.21.0.9:47842 - R:proxy_1/172.21.0.6:9042] Protocol initialization request, step 1 (OPTIONS): timed out after 5000 ms], Node(endPoint=proxy_2/172.21.0.7:9042, hostId=null, hashCode=9ef590b): [com.datastax.oss.driver.api.core.DriverTimeoutException: [s22|control|id: 0x078588f5, L:/172.21.0.9:35198 - R:proxy_2/172.21.0.7:9042] Protocol initialization request, step 1 (OPTIONS): timed out after 5000 ms], Node(endPoint=proxy_3/172.21.0.8:9042, hostId=null, hashCode=317327b): [com.datastax.oss.driver.api.core.DriverTimeoutException: [s22|control|id: 0x7e1e65ab, L:/172.21.0.9:57086 - R:proxy_3/172.21.0.8:9042] Protocol initialization request, step 1 (OPTIONS): timed out after 5000 ms]
	at com.datastax.oss.driver.api.core.AllNodesFailedException.copy(AllNodesFailedException.java:141)
	at com.datastax.oss.driver.internal.core.util.concurrent.CompletableFutures.getUninterruptibly(CompletableFutures.java:149)
	at com.datastax.oss.driver.api.core.session.SessionBuilder.build(SessionBuilder.java:835)
	at proxy.client.App.session(App.java:36)
	at proxy.client.App.main(App.java:17)
	Suppressed: com.datastax.oss.driver.api.core.DriverTimeoutException: [s22|control|id: 0x63f66325, L:/172.21.0.9:47842 - R:proxy_1/172.21.0.6:9042] Protocol initialization request, step 1 (OPTIONS): timed out after 5000 ms
		at com.datastax.oss.driver.internal.core.channel.ChannelHandlerRequest.onTimeout(ChannelHandlerRequest.java:108)
		at io.netty.util.concurrent.PromiseTask.runTask(PromiseTask.java:98)
		at io.netty.util.concurrent.ScheduledFutureTask.run(ScheduledFutureTask.java:170)
		at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:164)
		at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:469)
		at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:503)
		at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:986)
		at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
		at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
		at java.base/java.lang.Thread.run(Thread.java:833)
	Suppressed: com.datastax.oss.driver.api.core.DriverTimeoutException: [s22|control|id: 0x078588f5, L:/172.21.0.9:35198 - R:proxy_2/172.21.0.7:9042] Protocol initialization request, step 1 (OPTIONS): timed out after 5000 ms
		at com.datastax.oss.driver.internal.core.channel.ChannelHandlerRequest.onTimeout(ChannelHandlerRequest.java:108)
		at io.netty.util.concurrent.PromiseTask.runTask(PromiseTask.java:98)
		at io.netty.util.concurrent.ScheduledFutureTask.run(ScheduledFutureTask.java:170)
		at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:164)
		at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:469)
		at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:503)
		at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:986)
		at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
		at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
		at java.base/java.lang.Thread.run(Thread.java:833)
	Suppressed: com.datastax.oss.driver.api.core.DriverTimeoutException: [s22|control|id: 0x7e1e65ab, L:/172.21.0.9:57086 - R:proxy_3/172.21.0.8:9042] Protocol initialization request, step 1 (OPTIONS): timed out after 5000 ms
		at com.datastax.oss.driver.internal.core.channel.ChannelHandlerRequest.onTimeout(ChannelHandlerRequest.java:108)
		at io.netty.util.concurrent.PromiseTask.runTask(PromiseTask.java:98)
		at io.netty.util.concurrent.ScheduledFutureTask.run(ScheduledFutureTask.java:170)
		at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:164)
		at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:469)
		at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:503)
		at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:986)
		at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
		at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
		at java.base/java.lang.Thread.run(Thread.java:833)
```
