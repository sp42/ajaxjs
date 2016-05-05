const
	HTTP = require('http'), PATH = require('path'), fs = require('fs'), CRYPTO = require('crypto'),
    url = require("url"), querystring = require("querystring"),
	Step = require('./step'), images = require("images");

// 配置对象。
var staticFileServer_CONFIG = {
	'host': '127.0.0.1',			// 服务器地址
	'port': 3000,						// 端口
	'site_base': 'C:/project/bigfoot', 			// 根目录，虚拟目录的根目录
	'file_expiry_time': 480, 		// 缓存期限 HTTP cache expiry time, minutes
	'directory_listing': true 		// 是否打开 文件 列表
};

const server = HTTP.createServer((req, res) => {
	init(req, res, staticFileServer_CONFIG);
});

server.listen(staticFileServer_CONFIG.port, staticFileServer_CONFIG.host, () => {
	console.log(`Image Server running at http://${staticFileServer_CONFIG.host}:${staticFileServer_CONFIG.port}/`);
});

// 当前支持的 文件类型，你可以不断扩充。
var MIME_TYPES = {
	'.txt': 'text/plain',
	'.md': 'text/plain',
	'': 'text/plain',
	'.html': 'text/html',
	'.css': 'text/css',
	'.js': 'application/javascript',
	'.json': 'application/json',
	'.jpg': 'image/jpeg',
	'.png': 'image/png',
	'.gif': 'image/gif'
};

MIME_TYPES['.htm'] = MIME_TYPES['.html'];

var httpEntity = {
	contentType: null,
	data: null,
	getHeaders: function (EXPIRY_TIME) {
		// 返回 HTTP Meta 的 Etag。可以了解 md5 加密方法
		var hash = CRYPTO.createHash('md5');
		//hash.update(this.data);
		//var etag = hash.digest('hex');

		return {
			'Content-Type': this.contentType,
			'Content-Length': this.data.length,
			//'Cache-Control': 'max-age=' + EXPIRY_TIME,
			//'ETag': etag
		};
	}
};

function init(request, response) {
	var args = url.parse(request.url).query,         //arg => name=a&id=5  
		params = querystring.parse(args);

	if (params.url) {
		getRemoteImg(request, response, params);
	} else {
		load_local_img(request, response, params);
	}
}

// 加载远程图片
function getRemoteImg(request, response, params) {
	var imgData = ""; // 字符串
	var size = 0;
    var chunks = [];

	Step(function () {
			HTTP.get(params.url, this);
		},
		function (res) {
			var maxLength = 10; // 10mb
			var imgData = "";
			if (res.headers['content-length'] > maxLength * 1024 * 1024) {
				server500(response, new Error('Image too large.'));
			} else if (!~[200, 304].indexOf(res.statusCode)) {
				server500(response, new Error('Received an invalid status code.'));
			} else if (!res.headers['content-type'].match(/image/)) {
				server500(response, new Error('Not an image.'));
			} else {
				// res.setEncoding("binary"); //一定要设置response的编码为binary否则会下载下来的图片打不开
				res.on("data", function (chunk) {
					// imgData+=chunk;
					size += chunk.length;
					chunks.push(chunk);
				});

				res.on("end", this);
			}
			
		},
		function () { 
			imgData = Buffer.concat(chunks, size);
		
			var _httpEntity = Object.create(httpEntity);
			_httpEntity.contentType = MIME_TYPES['.png'];
			_httpEntity.data = imgData;
			// console.log('imgData.length:::' + imgData.length)
			// 缓存过期时限
			var EXPIRY_TIME = (staticFileServer_CONFIG.file_expiry_time * 60).toString();
			response.writeHead(200);
			response.end(_httpEntity.data);
		}
	);
	
}


// 获取本地图片
function load_local_img(request, response, params) {
	if (PATH.extname(request.url) === '') {
		// connect.directory('C:/project/bigfoot')(request, response, function(){});
		// 如果 url 只是 目录 的，则列出目录
		console.log('如果 url 只是 目录 的，则列出目录');
		server500(response, '如果 url 只是 目录 的，则列出目录@todo');
	} else {
		var pathname = require('url').parse(request.url).pathname;
		// 如果 url 是 目录 + 文件名 的，则返回那个文件
		var path = staticFileServer_CONFIG.site_base + pathname;

		Step(function () {
			console.log('请求 url :' + request.url + ', path : ' + pathname);
			fs.exists(path, this);
		}, function (path_exists, err) {
			if (err) {
				server500(response, '查找文件失败！');
				return;
			}
			if (path_exists) {
				fs.readFile(path, this);
			} else {
				response.writeHead(404, { 'Content-Type': 'text/plain;charset=utf-8' });
				response.end('找不到 ' + path + '\n');
			}
		}, function (err, data) {
			if (err) {
				server500(response, '读取文件失败！');
				return;
			}
			var extName = '.' + path.split('.').pop();
			var extName = MIME_TYPES[extName] || 'text/plain';

			var _httpEntity = Object.create(httpEntity);
			_httpEntity.contentType = extName;
			var buData = new Buffer(data);
			//images(buData).height(100);

			var newImage;

			if (params.w && params.h) {
				newImage = images(buData).resize(Number(params.w), Number(params.h)).encode("jpg", { operation: 50 });
			} else if (params.w) {
				newImage = images(buData).resize(Number(params.w)).encode("jpg", { operation: 50 });
			} else if (params.h) {
				newImage = images(buData).resize(null, Number(params.h)).encode("jpg", { operation: 50 });
			} else {
				newImage = buData; // 原图
			}

			_httpEntity.data = newImage;

			// 命中缓存，Not Modified返回 304
			if (request.headers.hasOwnProperty('if-none-match') && request.headers['if-none-match'] === _httpEntity.ETag) {
				response.writeHead(304);
				response.end();
			} else {
				// 缓存过期时限
				var EXPIRY_TIME = (staticFileServer_CONFIG.file_expiry_time * 60).toString();

				response.writeHead(200, _httpEntity.getHeaders(EXPIRY_TIME));
				response.end(_httpEntity.data);
			}
		});
	}
}
function server500(response, msg) {
	console.log(msg);
	response.writeHead(404, { 'Content-Type': 'text/plain;charset=utf-8' });
	response.end(msg + '\n');
}