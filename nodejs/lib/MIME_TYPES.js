// 当前支持的 文件类型，你可以不断扩充。
const MIME_TYPES = {
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

module.exports = MIME_TYPES;