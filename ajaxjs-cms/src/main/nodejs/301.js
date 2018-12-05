var httpProxy = require('http-proxy');
// 新建一个代理 Proxy Server 对象  
var proxy = httpProxy.createProxyServer({});


// 80 forward to 443
var server = require('http').createServer(function(req, res) {
    var host = req.headers.host;

    switch (host) {
        case 'ajaxjs.com':
        case 'www.ajaxjs.com':
            res.writeHead(301, { 'Location': 'https://www.ajaxjs.com/' });
            res.end();
            break;
        case 'framework.ajaxjs.com':
            res.writeHead(301, { 'Location': 'https://framework.ajaxjs.com/framework' });
            res.end();
            break;

        case '5222222.com':
        case 'wwww.5222222.com':
            proxy.web(req, res, { target: 'http://127.0.0.1:83' });
            break;
        default:
            proxy.web(req, res, { target: 'http://127.0.0.1:83' });
            // res.writeHead(302, { 'Location': 'https://www.baidu.com/' });
            // res.end();
            /*
            res.writeHead(200, {
                'Content-Type': 'text/plain'
            });
            res.end('Welcome to my server!');
            */
    }
});

var port = 80;
console.log("listening on port: " + port);
server.listen(port);