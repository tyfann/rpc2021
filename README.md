# rpc2021

## 运行说明

1. rpc-server module中运行Server01和Server02，启动服务端
2. rpc-consumer-800 module中启动主程序，启动客户端
3. 打开网页链接[localhost:800/consumer/payment/zk](localhost:800/consumer/payment/zk)，会得到如下结果(轮询负载，因此交替出现)：

<img src="https://s3.ax1x.com/2021/01/12/sGtocQ.png">

<img src="https://s3.ax1x.com/2021/01/12/sGtq7q.png">

4. 完成rpc远程过程调用

