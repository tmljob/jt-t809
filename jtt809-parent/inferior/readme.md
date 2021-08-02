# 使用说明

1.在application.properties配置需要连接的JT/T 809上级平台(网关)相关信息。



```
netty.server.ip=127.0.0.1
netty.server.port=8000
netty.server.userid=1001
netty.server.pwd=hihihihi
netty.server.centerId: 1001
```

2、运行start.bat或start.sh脚本，即启动连接上级平台。

查看日志，打印如下内容即为登录成功

```
2021/08/02-13:34:40.454 [nioEventLoopGroup-2-1] ERROR i.t.i.i.c.handler.CliBusiHandler - 应答----------------0x1002
2021/08/02-13:34:40.455 [nioEventLoopGroup-2-1] ERROR i.t.i.i.c.handler.CliBusiHandler - ------------------登录成功
```

3、程序启动并登录成功后，会在./input/目录下面定时扫描csv文件(轨迹信息)，解析发给上级平台。

csv文件各式参考gps-sample.csv样例文件，其文件内容要求如下：

| 车牌号   | 经度     | 维度     | 速度 | 方向 |
| -------- | -------- | -------- | ---- | ---- |
| 皖A66M27 | 117.2901 | 39.56363 | 57   | 30   |
| 皖A66M28 | 117.2901 | 39.56363 | 57   | 30   |
| 皖A66M29 | 117.2901 | 39.56363 | 57   | 30   |

4、发送以后./input/目录下的文件会删除，可以在上级平台确认轨迹信息是否正确发送。