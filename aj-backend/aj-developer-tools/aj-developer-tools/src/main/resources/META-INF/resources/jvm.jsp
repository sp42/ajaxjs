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

    </script>
  </body>

  </html>