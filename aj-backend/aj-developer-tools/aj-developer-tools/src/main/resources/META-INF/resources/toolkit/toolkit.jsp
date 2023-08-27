<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Developer Tools</title>
        <%@ include file="../common/head.jsp" %>
        <style>
            html, body {
                height:100%;
            }
        </style>
    </head>
    <body>
        <div id="vue" style="padding:1% 5%;">
              <Tabs value="formatSQL">

                <tab-pane label="SQL 代码格式化" name="formatSQL">
                      <i-input type="textarea" v-model="formatSQL" :rows="8"></i-input>
                      <br />
                      <br />
                      <i-button type="primary" @click="doFormatSQL">格式化</i-button>
                      <br />
                      <br />
                      结果：
                      <br />
                      <br />
                      <i-input type="textarea" v-model="formattedSQL" :rows="8"></i-input>
                </tab-pane>

                 <tab-pane label="MySQL 数据备份" name="backupMySQL">

                    <div>
                        <i-input placeholder="MySQL IP" v-model="calculateRowsDir" style="width:30%;"></i-input>
                        <i-input placeholder="MySQL Port" v-model="calculateRowsDir" style="width:10%;"></i-input>
                        <i-input placeholder="MySQL User" v-model="calculateRowsDir" style="width:12%;"></i-input>
                        <i-input placeholder="MySQL Password" v-model="calculateRowsDir" style="width:15%;"></i-input>
                        <i-button type="primary" @click="calculateRows">备份远程数据库</i-button>
                    </div>

                    <br />
                    <div>
                        <i-button type="primary" @click="calculateRows">本地数据库备份</i-button>
                    </div>
                    <br />
                    <div>
                        <a href="#">下载 </a> <span>出于安全起见，20 分钟之后自动删除文件。</span>
                    </div>
                    <br />
                    <div>
                        <i-button type="primary" @click="calculateRows">加入定时任务</i-button> <span style="color:gray;">加入 Spring 定时任务进行备份</span>
                    </div>

                    <div style="border-top:1px solid lightgray;margin:40px 0;"></div>

                    <div>
                         <i-input placeholder="MySQL IP" v-model="calculateRowsDir" style="width:30%;"></i-input>
                         <i-input placeholder="MySQL Port" v-model="calculateRowsDir" style="width:10%;"></i-input>
                         <i-input placeholder="MySQL User" v-model="calculateRowsDir" style="width:12%;"></i-input>
                         <i-input placeholder="MySQL Password" v-model="calculateRowsDir" style="width:15%;"></i-input>
                         <i-button type="primary" @click="calculateRows">Ping MySQL</i-button>
                     </div>
                    <br />
                    <div>
                        <i-button type="primary" @click="calculateRows">Ping 本地 MySQL</i-button>
                    </div>
                 </tab-pane>

                <tab-pane label="实时后台日志" name="websocket-log">
                        TODO
                        https://blog.csdn.net/zhangxin09/article/details/104412493
                </tab-pane>

                 <tab-pane label="JMX Bean 浏览器" name="JmxMBeanBrowser">
                         <i-input placeholder="?name=xxx 查询参数" v-model="licenseFilePath" style="width:50%;"></i-input>
                         <i-button type="primary" @click="calculateRows">查询</i-button>
                         <br />
                         <br />
                         <iframe src="JmxMBeanBrowser.jsp"></iframe>
                   </tab-pane>

                <tab-pane label="其他" name="misc">
                    <i-input placeholder="输入一个磁盘目录" v-model="calculateRowsDir" style="width:60%;"></i-input>

                    <i-button type="primary" @click="calculateRows">代码行数统计</i-button>
                      <br />
                      <br />
                     结果：
                      <br />
                      <br />
                      <div>
                        <uil>
                            <li>
                                Java 类的数量：{{calculateRowsResult.classCount}}
                            </li>
                            <li>
                                所有行数：{{calculateRowsResult.allLines}}
                            </li>
                            <li>
                                代码行：{{calculateRowsResult.writeLines}}
                            </li>
                            <li>
                                注释行：{{calculateRowsResult.commentLines}}
                            </li>
                            <li>
                                空行：{{calculateRowsResult.normalLines}}
                            </li>
                        </ul>
                        <br />
                        <br />
                       <i-input placeholder="输入许可证文本文件之磁盘路径" v-model="licenseFilePath" style="width:60%;"></i-input>

                       <i-button type="primary" @click="calculateRows">License 复制</i-button>
                      </div>

                 </tab-pane>


              </Tabs>
        </div>
        <script>
            new Vue({
                el: '#vue',
                data: {
                    formatSQL: '',
                    formattedSQL: '',
                    calculateRowsDir: '',
                    calculateRowsResult: {},
                    licenseFilePath: ''
                },
                methods: {
                    doFormatSQL() {
                        aj.xhr.postForm('/toolkit/format_sql', {
                            sql: this.formatSQL
                        }, json => {
                            this.formattedSQL = json.data;
                        });
                    },
                    calculateRows() {
                        aj.xhr.postForm('/toolkit/calculate_rows', {
                            dir: this.calculateRowsDir
                        }, json => {
                            console.log(json);
                           // this.formattedSQL = json.data;
                           this.calculateRowsResult = json.data;
                        });
                    }
                }
            });
        </script>
    </body>
</html>