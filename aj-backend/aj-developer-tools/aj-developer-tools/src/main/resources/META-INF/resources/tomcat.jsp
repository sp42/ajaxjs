<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*" %>
  <!DOCTYPE html>
  <html>

  <head>
    <%@ include file="common/head.jsp" %>
      <style>
        body {
          padding: 2%;
        }
      </style>
  </head>

  <body>
    <h2>JVM 信息</h2>

    <div id="jvm">
      <table width="100%">
        <tr>
          <td width="50%" valign="top">
            <fieldset class="fieldset-box">
              <legend>连接本地 JVM</legend>
              <ul>
                <li v-for="item in localJVMs" v-if="!!item.name">
                  <a href="#" style="float: right;" @click="connectLocal(item.pid)">连接</a>
                  {{item.name}}:{{item.pid}}
                </li>
              </ul>
            </fieldset>
          </td>
          <td width="50%" valign="top">
            <fieldset class="fieldset-box">
              <legend>连接远程 JVM</legend>
              <i-input v-model="addJvm.ip" placeholder="远程服务器 IP 或 hostname" style="width: 50%"></i-input> :
              <i-input v-model="addJvm.port" placeholder="端口" style="width: 15%"></i-input>
              <i-button @click="connectRemote">连 接</i-button>
            </fieldset>
          </td>
        </tr>
      </table>


      <br />
      <br />

      <Tabs value="system">
        <tab-pane label="系统信息" name="system">
          <table class="key-value">
            <tr>
              <td class="name">系统名称</td>
              <td class="value">{{overview.systemInfo.name}}</td>
              <td class="name">系统架构</td>
              <td class="value">{{overview.systemInfo.arch}}</td>
            </tr>
            <tr>
              <td class="name">可用处理器个数</td>
              <td class="value">{{overview.systemInfo.availableProcessors}}</td>
              <td class="name">系统版本</td>
              <td class="value">{{overview.systemInfo.version}}</td>
            </tr>
            <tr>
              <td class="name">系统 CPU 使用</td>
              <td class="value">{{(overview.systemInfo.systemCpuLoad * 100).toFixed(2)}}%</td>
              <td class="name">当前 JVM CPU 使用率</td>
              <td class="value">{{(overview.systemInfo.processCpuLoad * 100).toFixed(2)}}%</td>
            </tr>
            <tr>
              <td class="name">物理内存</td>
              <td class="value"> {{(overview.systemInfo.totalPhysicalMemorySize / 1024 / 1024 / 1024).toFixed(2)}}GB
              </td>
              <td class="name">最后一分钟 CPU 平均负载</td>
              <td class="value">{{overview.systemInfo.systemLoadAverage}}</td>
            </tr>
            <tr>
              <td class="name">已提交内存</td>
              <td class="value"> {{(overview.systemInfo.committedVirtualMemorySize / 1024 / 1024 ).toFixed(2)}}MB</td>
              <td class="name">空闲内存</td>
              <td class="value">{{(overview.systemInfo.freePhysicalMemorySize / 1024 / 1024 ).toFixed(2)}}MB</td>
            </tr>
            <tr>
              <td class="name">交换内存空间</td>
              <td class="value"> {{(overview.systemInfo.totalSwapSpaceSize / 1024 / 1024 ).toFixed(2)}}MB</td>
              <td class="name">空闲交换空间</td>
              <td class="value">{{(overview.systemInfo.freeSwapSpaceSize / 1024 / 1024 ).toFixed(2)}}MB</td>
            </tr>
          </table>
        </tab-pane>
        <tab-pane label="JVM 信息" name="jvm">
          <table class="key-value">
            <tr>
              <td class="name">JDK 厂商</td>
              <td class="value">{{overview.jvmInfo.specVendor}}</td>
              <td class="name">JDK 版本</td>
              <td class="value">{{overview.jvmInfo.specVersion}} {{overview.jvmInfo.vmVersion}}</td>
            </tr>
            <tr>
              <td class="name">输入参数</td>
              <td class="value">{{overview.jvmInfo.inputArguments}}</td>
              <td class="name">启动时间</td>
              <td class="value">{{overview.jvmInfo.startTime}}</td>
            </tr>
            <tr v-for="(item, index) in overview.jvmInfo.systemProperties" :key="index">
              <td class="name">{{ item[0].name }}</td>
              <td class="value">{{ item[0].value }}</td>
              <td class="name">{{ item[1].name }}</td>
              <td class="value">{{ item[1].value }}</td>
            </tr>
          </table>
        </tab-pane>
      </Tabs>
      <br />
    </div>

    <script src="http://www.ajaxjs.com/public/jvm.js">

      LABEL_THREAD_POOL = {
        "currentThreadsBusy": "当前正在执行任务的线程数",
        "sslImplementationName": "SSL 实现的名称",
        "paused": "是否暂停",
        "selectorTimeout": "Selector 的超时时间",
        "modelerType": "模型类型",
        "connectionCount": "当前连接数",
        "acceptCount": "监听器接受的连接数",
        "threadPriority": "线程优先级",
        "executorTerminationTimeoutMillis": "Executor 终止的超时时间",
        "running": "是否正在运行",
        "currentThreadCount": "当前线程数",
        "sSLEnabled": "是否启用 SSL",
        "sniParseLimit": "SNI 解析限制",
        "maxThreads": "最大线程数",
        "sslImplementation": "SSL 实现对象",
        "connectionTimeout": "连接超时时间",
        "tcpNoDelay": "是否启用 TCP 不延迟",
        "maxConnections": "最大连接数",
        "connectionLinger": "连接延迟时间",
        "keepAliveCount": "Keep-Alive 计数",
        "keepAliveTimeout": "Keep-Alive 超时时间",
        "maxKeepAliveRequests": "最大 Keep-Alive 请求次数",
        "localPort": "本地端口",
        "deferAccept": "是否延迟接受连接",
        "useSendfile": "是否使用 sendfile",
        "acceptorThreadCount": "接收线程数",
        "pollerThreadCount": "轮询线程数",
        "daemon": "是否是守护进程",
        "minSpareThreads": "最小空闲线程数",
        "useInheritedChannel": "是否使用继承通道",
        "alpnSupported": "是否支持 ALPN",
        "acceptorThreadPriority": "接收线程优先级",
        "bindOnInit": "是否在初始化时绑定",
        "pollerThreadPriority": "轮询线程优先级",
        "port": "监听端口",
        "domain": "域名",
        "name": "名称",
        "defaultSSLHostConfigName": "默认 SSL 主机配置名称"
      };


      LABEL_SESSION = {
        "sessionAttributeValueClassNameFilter": "会话属性值类名过滤器",
        "modelerType": "模型类型",
        "warnOnSessionAttributeFilterFailure": "会话属性过滤器失败时是否发出警告",
        "className": "类名",
        "secureRandomAlgorithm": "安全随机算法",
        "secureRandomClass": "安全随机类",
        "sessionAverageAliveTime": "会话平均存活时间",
        "rejectedSessions": "被拒绝的会话数",
        "processExpiresFrequency": "处理过期会话的频率",
        "stateName": "状态名称",
        "persistAuthentication": "是否持久化身份验证",
        "duplicates": "重复会话数",
        "maxActiveSessions": "最大活动会话数",
        "sessionMaxAliveTime": "会话最大存活时间",
        "processingTime": "处理时间",
        "pathname": "路径名",
        "sessionExpireRate": "会话过期速率",
        "sessionAttributeNameFilter": "会话属性名称过滤器",
        "activeSessions": "活动会话数",
        "sessionCreateRate": "会话创建速率",
        "name": "名称",
        "secureRandomProvider": "安全随机提供者",
        "jvmRoute": "JVM 路由",
        "expiredSessions": "已过期的会话数",
        "maxActive": "最大活动数",
        "sessionCounter": "会话计数器"
      };

      LABEL_SYSTEM= {
        "ObjectPendingFinalizationCount": "待终结对象计数",
        "Verbose": "是否启用详细输出",
        "Name": "名称",
        "ClassPath": "类路径",
        "BootClassPath": "引导类路径",
        "LibraryPath": "库路径",
        "Uptime": "运行时间",
        "VmName": "虚拟机名称",
        "VmVendor": "虚拟机供应商",
        "VmVersion": "虚拟机版本",
        "BootClassPathSupported": "是否支持引导类路径",
        "InputArguments": "输入参数",
        "ManagementSpecVersion": "管理规范版本",
        "SpecName": "规范名称",
        "SpecVendor": "规范供应商",
        "SpecVersion": "规范版本",
        "StartTime": "启动时间"

      };
    </script>
  </body>

  </html>