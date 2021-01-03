const http = require('http');  //引入node http模块
const fs = require('fs');    //引入node 文件读写fs模块
const path = require('path');   //引入 node path模块
const url = require('url');     //引入  url  模块
const resMime = require('./resMime.js'); //引入自定义模块，模块主要处理响应头

const config = {
    port: 8888,
    webRoot: 'C:\\project\\aj-ts'
};
const server = http.createServer((req, res) => {    // http.createServer()创建服务器
    let pathName = url.parse(req.url).pathname;     // 获取req.url,并且转换请求的路径
    // console.log(url.parse('index.html/12323123'));  会得到 index.html
    // console.log(pathName); // 控制台打印看能不能得到请求路径

    if (pathName == '/')   // 如果请求为空或“/”，则把请求路径改为index.html
        pathName = '/index.html';

    if (pathName == '/favicon.ico')  // 如果请求为“/favicon.ico”图标,则直接结束不处理
        res.end();
    else {
        // 这里正式写处理逻辑
        // console.log('开始读取文件');
        let extName = path.extname(pathName);//拿到扩展名（比如说：'index.html' => '.html'）
        
        // 下面用到 fs 去读取客户端请求的文件，path.join拼接成绝对路径
        fs.readFile(path.join(config.webRoot || __dirname, pathName), function (err, data) {   // 绝对路径，path.jion(__dirname,pathName)
            if (err) {
                // 没有找到的话，就走ERR，转去加载404页面
                console.log(err);
                res.writeHead(404, { "Content-Type": "text/html;chartset='utf8'" })
                res.write("Not found"); // 把文件写回客户端
                res.end();   // 结束
                // fs.readFile(path.join(config.webRoot || __dirname, `404.html`), (err, data) => {
                //     // res.writeHead()向客户端返回响应头，状态码404，因为返回的是自己写的404页面，所以响应头应该为"Content-Type":"text/html;chartset='utf8'" 
                //     res.writeHead(404, { "Content-Type": "text/html;chartset='utf8'" })
                //     res.write(data); // 把文件写回客户端
                //     res.end();   // 结束
                // })
            } else {
                // 这里fs找到了请求资源
                // console.log(data) // 打印看看资源的内容
                var mime = resMime.getMime(fs, extName);  // 调用外部模块resMime,这个模块我是用来，请求的文件后缀名转换成mime标准的响应头Content-Type类型（比如说：'.css'=>'text/css','.js'=>'text/javascript'）
                res.writeHead(200, { "Content-Type": `${mime};chartset='utf8'` });
                res.write(data); // 把文件写回客户端
                res.end();   // 结束
            }
        });

    }

});

server.listen(config.port);    // 让服务器跑在8887端口
console.log('Server ready');