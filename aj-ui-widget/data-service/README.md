
需要一个 http server 跑这静态页面，原因是 less.js 导入外部 less 必须网络下，file:// 下不行：（

推荐 node 的 http-server，安装：

    npm install --global http-server

启动：

    http-server
    OR 
    http-server -c-1 