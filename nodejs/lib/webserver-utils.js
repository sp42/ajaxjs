const CRYPTO = require('crypto');
const responseHead_ContentType = { 'Content-Type': 'text/plain;charset=utf-8' };

module.exports.show404 = function (response, msg) {
	msg = (msg ? '404，找不到 ' + msg + '资源 ' : '404 找不到资源') + '\n';
	console.log(msg);
	response.writeHead(404, responseHead_ContentType);
	response.end(msg);

	return msg;
}

module.exports.show500error = function (response, msg) {
	msg = (msg ? '500，' + msg : '500, Server Error') + '\n';
	console.log(msg);
	response.writeHead(500, responseHead_ContentType);
	response.end(msg);

	return msg;
}

// 表示一个 HTTP Resposne
module.exports.HTTP_Resposne_Entity = {
	contentType: null,
	data: null,
	getHeaders: function (EXPIRY_TIME) {
		// 返回 HTTP Meta 的 Etag。可以了解 md5 加密方法
		var hash = CRYPTO.createHash('md5');
		hash.update(this.data);
		var etag = hash.digest('hex');

		return {
			'Content-Type': this.contentType,
			'Content-Length': this.data.length,
			'Cache-Control': 'max-age=' + EXPIRY_TIME,
			'ETag': etag
		};
	}
};

/**
 * 
 * @param  {String} ids [description]
 * @return {Object}     [description]
 */
module.exports.url2json = function (ids) {
	var _json = {};
	if (ids && ids.indexOf("?") != -1) {

		ids = ids.split('?');
		ids = ids.pop();
		ids = ids.replace(/&/g, '","').replace(/=/g, '":"');
		if (ids) {
			_json = JSON.parse('{"' + ids + '"}');
		}
		// var ids = ids.split('&');
		// var json = {};
		// for(var arr, i = 0, j = ids.length; i < j; i++){
		// 	arr = ids[i];
		// 	arr = arr.split('=');
		// 	json[arr[0]] = arr[1];
		// }
	}
	return _json;
}
