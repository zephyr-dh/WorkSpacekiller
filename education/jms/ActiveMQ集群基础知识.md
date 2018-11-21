# ActiveMQ集群基础知识



## 集群方式

- 客户端集群：让多个消费者消费同一个队列
- Broker clusters：多个Broker之间同步消息
- Master Slave：实现高可用

## 客户端配置

ActiveMQ失效转移（failover）:failover(uri1,  ...  ,uriN)?transportOptions

> transportOptions参数
>
> randomize
>
> initialReconnectDelay
>
> maxReconnectDelay
>
>

