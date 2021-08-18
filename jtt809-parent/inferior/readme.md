# 使用说明

1.在application.properties配置需要连接的JT/T 809上级平台(网关)相关信息。



```
netty.server.ip=127.0.0.1
netty.server.port=8000
netty.server.userid=1001
netty.server.pwd=hihihihi
netty.server.centerId=1001
```

如需要对报文进行加密，可以开启加密开关并配置加密参数(m1、ia1、ic1)，默认不开启加密。

```
#message encrypt 0/1
message.encrypt.enable=0

#encrypt parameter
superior.server.m1=10000000
superior.server.ia1=20000000
superior.server.ic1=30000000
```

mock数据时是否提供时间列，默认不提供，使用发送时的时间。如果提供时间，需要开启此开关，并在参考样例csv文件时间添加在最后一列。

```
#mock data settings
#input data carry date column whether or not 
mock.input.data.date=0
```

2、运行start.bat或start.sh脚本，即启动连接上级平台。

查看日志，打印如下内容即为登录成功

```
17:08:57.871 [nioEventLoopGroup-2-1] INFO  i.t.i.i.c.handler.CliBusiHandler - respond----------------0x1002
17:08:57.871 [nioEventLoopGroup-2-1] INFO  i.t.i.i.c.handler.CliBusiHandler - ------------------login sucessfully!
```

3、程序启动并登录成功后，会在./input/目录下面定时扫描csv文件(轨迹信息)，解析发给上级平台。

csv文件各式参考gps-sample.csv样例文件，其文件内容要求如下：

| 车牌号   | 经度     | 维度     | 速度 | 行驶记录速度 | 里程 | 方向 | 海拔高度 | 时间                |
| -------- | -------- | -------- | ---- | ------------ | ---- | ---- | -------- | ------------------- |
| 皖A66M27 | 117.2901 | 39.56363 | 57   | 67           | 5666 | 30   | 56       | 2007-02-20 02:03:13 |

注意时间列格式要求如下：

```
yyyy-MM-dd HH:mm:ss
```

4、发送以后./input/目录下的文件会删除，可以在上级平台确认轨迹信息是否正确发送。