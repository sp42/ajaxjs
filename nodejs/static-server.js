const HTTP = require('http'), PATH = require('path'), fs = require('fs'),
	connect = require('connect'), Step = require('./lib/step'),
	lessParser = require('./less_parser.js'),
	webUtils = require('./lib/webserver-utils'), MIME_TYPES = require('./lib/MIME_TYPES');

// 配置
const staticFileServer_CONFIG = {
	'host': 'apu',			// 服务器地址
	'port': 80,						// 端口
	'site_base': 'C:/project/bigfoot', 			// 根目录，虚拟目录的根目录
	'file_expiry_time': 480, 		// 缓存期限 HTTP cache expiry time, minutes
	'directory_listing': true 		// 是否打开 文件 列表
};

var app = connect()
	.use(connect.logger('dev'))
	// 样式处理器
	.use('/lessService', function (request, response) {
		lessParser.init(request, response);
	})
	.use('/bigfoot', function (request, response) {
		// 忽略 fav.ico
		if (request.url.indexOf('favicon') != -1) {
			// 'WebService不需要 favicon';
			return;
		}
		if (request.method !== 'GET') {// 静态服务服务器都是 HTTP GET 方法的
			response.writeHead(403);//	Forbidden
			response.end();
			return;
		}
		staticServer(request, response, staticFileServer_CONFIG);
	});

HTTP.createServer(app).listen(staticFileServer_CONFIG.port, null, null, function () {
	console.log('已启动服务器 Start server: http://%s:%d', staticFileServer_CONFIG.host, staticFileServer_CONFIG.port);
});

process.on('exit', function () {
	console.warn('退出了……');
});
// process.on('SIGINT', function(){
// console.log('……');
// });
// 防止异常中止进程，为最后一道防线
// process.on('uncaughtException', function(e){
// 	// responseObj4UncaughtException.writeHead(500, 'text/html');
// 	// responseObj4UncaughtException.end('未知异常！' + e);
// 	_c.error('未知异常！' + e);
// });

function staticServer(request, response, CONFIG) {
	if (PATH.extname(request.url) === '') {
		connect.directory('C:/project/bigfoot')(request, response, function () { });
		// 如果 url 只是 目录 的，则列出目录
		// console.log('如果 url 只是 目录 的，则列出目录');
		// server500(response, '如果 url 只是 目录 的，则列出目录@todo');
	} else {
		var pathname = require('url').parse(request.url).pathname;
		// 如果 url 是 目录 + 文件名 的，则返回那个文件
		var path = CONFIG.site_base + pathname;

		Step(function () {
			console.log('请求 url :' + request.url + ', path : ' + pathname);
			fs.exists(path, this);
		}, function (path_exists, err) {
			if (err)
				return webUtils.show500error(response, '查找文件失败！');

			if (path_exists) {
				fs.readFile(path, this);
			} else {
				webUtils.show404(response, '查找文件失败！');
			}
		}, function (err, data) {
			if (err)
				return webUtils.show500error(response, '读取文件失败！');

			var extName = '.' + path.split('.').pop();
			var extName = MIME_TYPES[extName] || 'text/plain';

			var httpEntity = Object.create(webUtils.HTTP_Resposne_Entity);
			httpEntity.contentType = extName;
			httpEntity.data = new Buffer(data);

			// 命中缓存，Not Modified返回 304
			if (request.headers.hasOwnProperty('if-none-match') && request.headers['if-none-match'] === httpEntity.ETag) {
				response.writeHead(304);
				response.end();
			} else {
				// 缓存过期时限
				var EXPIRY_TIME = (CONFIG.file_expiry_time * 60).toString();

				response.writeHead(200, _httpEntity.getHeaders(EXPIRY_TIME));
				response.end(httpEntity.data);
			}
		});
	}
}

// 处理目录的方法 @todo
function getDirectoryResponse(path, callback) {

	// 	var full_path = CONFIG.site_base + path;	// 完整路径
	// 	var template;
	// 	var i;

	// 	if ( CONFIG.directory_listing ){
	// 		PATH.exists( full_path, function ( path_exists ){
	// 			if ( path_exists ){
	// 				FS.readdir( full_path, function ( error, files ){
	// 					if ( error ){
	// //						Internal error
	// 						callback( new ResponseObject( {'data': error.stack, 'status': 500} ) );
	// 					}
	// 					else {
	// 						// 列出结果
	// //						Custard template
	// 						template = new CUSTARD;

	// 						template.addTagSet( 'h', require('./templates/tags/html') );
	// 						template.addTagSet( 'c', {
	// 							'title': 'Index of ' + path,
	// 							'file_list': function ( h ){
	// 								var items = [];
	// 								var stats;
	// 								for ( i = 0; i < files.length; i += 1 ){
	// 									stats = FS.statSync( full_path + files[i] );
	// 									if ( stats.isDirectory() ){
	// 										files[i] += '/';
	// 									}
	// 									items.push( h.el( 'li', [
	// 										h.el( 'a', {'href': path + files[i]}, files[i] )
	// 									] ) );
	// 								}
	// 								return items;
	// 							}
	// 						} );

	// 						template.render( template_directory, function ( error, html ){
	// 							if ( error ){
	// //								Internal error
	// 								callback( new ResponseObject( {'data': error.stack, 'status': 500} ) );
	// 							}
	// 							else {
	// 								callback( new ResponseObject( {'data': new Buffer( html ), 'type': 'text/html'} ) );
	// 							}
	// 						} );
	// 					}
	// 				} );
	// 			}
	// 			else {
	// 				// 找不到 文件，就是 404
	// //				Not found
	// 				callback( new ResponseObject( {'status': 404} ) );
	// 			}
	// 		} );
	// 	} else {
	// 		// 禁止 目录浏览，返回 403
	// //		Forbidden
	// 		callback( new ResponseObject( {'status': 403} ) );
	// 	}
}
