const http = require('https'),
    httpProxy = require('http-proxy'),
    fs = require('fs'),
    tls = require('tls');

// 新建一个代理 Proxy Server 对象  
var proxy = httpProxy.createProxyServer({});

// 捕获异常  
proxy.on('error', function(err, req, res) {
    res.writeHead(500, {
        'Content-Type': 'text/plain'
    });
    res.end('Something went wrong. And we are reporting a custom error message. <br />ERR:' + err);
});
console.log(__dirname);
const secureContext = {
        'framework.ajaxjs.com': tls.createSecureContext({
            key: fs.readFileSync(__dirname + '/ssl/3_framework.ajaxjs.com.key', 'utf8'),
            cert: fs.readFileSync(__dirname + '/ssl/2_framework.ajaxjs.com.crt', 'utf8')
        }),
        'www.ajaxjs.com': tls.createSecureContext({
            key: fs.readFileSync(__dirname + '/ssl/3_www.ajaxjs.com.key'),
            cert: fs.readFileSync(__dirname + '/ssl/2_www.ajaxjs.com.crt', 'utf8')
                // , ca: fs.readFileSync('./path_to_certificate_authority_bundle.ca-bundle2', 'utf8'), // this ca property is optional
        })
    },
    options = {
        SNICallback: function(domain, cb) {
            if (secureContext[domain]) {
                if (cb) {
                    cb(null, secureContext[domain]);
                } else {
                    // compatibility for older versions of node
                    return secureContext[domain];
                }
            } else {
                return null;
                // throw new Error('No keys/certificates for domain requested: ' + domain);
            }
        },
        key: fs.readFileSync(__dirname + '/ssl/3_www.ajaxjs.com.key'),
        cert: fs.readFileSync(__dirname + '/ssl/2_www.ajaxjs.com.crt', 'utf8')
    };

// 另外新建一个 HTTP 80 端口的服务器，也就是常规 Node 创建 HTTP 服务器的方法。  
// 在每次请求中，调用 proxy.web(req, res config) 方法进行请求分发  
var server = require('https').createServer(options, function(req, res) {
    // 在这里可以自定义你的路由分发  
    var host = req.headers.host,
        ip = req.headers['x-forwarded-for'] || req.connection.remoteAddress;
    console.log("client ip:" + ip + ", host:" + host);

    switch (host) {
        //case 'framework.ajaxjs.com':
        case 'bbs.aaaa.com':
            res.writeHead(301, { 'Location': 'http://framework.ajaxjs.com/framework/' });
            console.log(res._header);
            res.end('hihi');
            break;
        case 'framework.ajaxjs.com':
        case 'ajaxjs.com':
        case 'www.ajaxjs.com':
            proxy.web(req, res, { target: 'http://127.0.0.1:82' });
            break;
        case 'weixintest.ajaxjs.com':
            proxy.web(req, res, { target: 'http://127.0.0.1:83' });
            break;
        default:
            // res.writeHead(302, { 'Location': 'https://www.baidu.com/' });
            // res.end();
            res.writeHead(200, {
                'Content-Type': 'text/plain'
            });
            res.end('Welcome to my server!');
    }
});

var port = 443;
console.log("listening on port: " + port);
server.listen(port);