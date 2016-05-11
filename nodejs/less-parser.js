const HTTP = require('http'), less = require('less'), fs = require('fs'), Step = require('./lib/step'), webUtils = require('./lib/webserver-utils');

const port = 80, server = HTTP.createServer((req, res) => {
	init(req, res);
});

server.listen(port, null, () => {
	var host = '';
	console.log(`Image Server running at http://${host}:${port}/`);
});

init = function (request, response) {
	console.log('Current Less.js vresion:' + less.version.join('.'));
	var _req = webUtils.url2json(request.url),
		lessFile = _req.lessFile,
		ns = _req.ns; // 读取 url 的命名空间

	if (!lessFile) 
		return webUtils.show500error(response, '未发行对应的样式文件！');

	// 指定包空间
	var ns_arr = [
		'c:/project/bigfoot/less'
    ];
	ns && ns_arr.push(ns);

	// 背景图的路径按 CSS 为基准。但因为 CSS 处于 Node 服务器上，所以要指定到 Tomcat 上才行。
	var assetFilePath = '@assetFilePath: "http://localhost:8080/sport/asset/images";\n';
	var assetFilePath = '@assetFilePath: "' + _req.picPath + '";\n';

	Step(function () {
		console.log('请求 LESS 命名空间：' + lessFile.split('/').pop());
		fs.readFile(lessFile, "utf8", this);
	}, function (err, lessFile) {
		if (err) 
			return webUtils.show500error(response, '未发行对应的样式文件！原因：' + err.toString());
		var isDebug = _req.isdebug; // 是否压缩样式 

		try {
			less.render(assetFilePath + lessFile, {
				paths: ns_arr,
				compress: (!isDebug || isDebug == 'false')
			}, this);
		} catch (e) {
			return webUtils.show500error(response, 'LESS 解析 less 失败，请检查 LESS 文件是否写错！原因:' + e);
		}
	}, function (err, tree) {
		if (err) 
			return webUtils.show500error(response, 'LESS 解析 less 失败！原因:' + e);
		//console.log('parse:' + lessFile);
		response.writeHead(200, { 'Content-Type': 'text/css; charset=UTF-8' });

		var isDebug = _req.isdebug; // 是否压缩样式

		response.end(tree.css);
		if (!isDebug || isDebug == 'false') { }
		else if (isDebug == 'true') { }

		// 总是 localhost/app 后面有一个 app 项目名
		//			deployUsed = deployUsed.replace(/http:\/\/localhost:8080\/\w+/i, _req.MainDomain);
		//		console.log('--------' + tree);
		console.log('--------' + _req.picPath);
		var deployUsed = tree.css.replace(new RegExp(_req.picPath, 'g'), '../images');

		// 保存文件
		// 父级 css 目录下，save to file
		// toCSS(lessFile.replace(/(.*?\/)(\w+)\.less/, '$1../css/$2.css'), deployUsed);
	});
}

// fs 覆盖文件
function toCSS(newlessFile, css) {
	var _fd;
	Step(function () {
		fs.open(newlessFile, 'w', this);
	}, function (err, fd) {
		if (err) throw err;

		_fd = fd;
		var wBuff = new Buffer(css),
			buffPos = 0,
			buffLen = wBuff.length,
			filePos = 0;

		fs.write(fd, wBuff, buffPos, buffLen, filePos, this);
	}, function (err, wbytes, data) {
		if (err) throw err;
		console.log('保存 CSS 压缩文件成功！wrote ' + wbytes + ' bytes，保存路径：' + newlessFile);
		fs.close(_fd);
	});
}

function show500(res, msg) {
	if (res) {
		msg = msg || '读取 LESS 文件异常，原因未知！';
		res.writeHead(500, { 'Content-Type': 'text/html;charset=UTF-8' });
		res.end(msg);
		console.log('------------------------------------------');
		console.log('[Statics]' + new Date + msg);
	} else {
		console.log('Request 对象不存在！');
	}
}

