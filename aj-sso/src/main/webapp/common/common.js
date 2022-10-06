/**
 * 原生 JS 框架
 */
jsp = {};

/**
 * 日期格式化
 * @param 	{String} format
 * @return	{String}
*/
Date.prototype.format = function(format) {
	var $1, o = {
		"M+": this.getMonth() + 1,		// 月份，从0开始算
		"d+": this.getDate(),   		// 日期
		"h+": this.getHours(),   		// 小时
		"m+": this.getMinutes(), 		// 分钟
		"s+": this.getSeconds(), 		// 秒钟
		// 季度 quarter
		"q+": Math.floor((this.getMonth() + 3) / 3),
		"S": this.getMilliseconds()	// 千秒
	};
	var key, value;

	if (/(y+)/.test(format)) {
		$1 = RegExp.$1,
			format = format.replace($1, String(this.getFullYear()).substr(4 - $1));
	}

	for (key in o) { // 如果没有指定该参数，则子字符串将延续到 stringvar 的最后。
		if (new RegExp("(" + key + ")").test(format)) {
			$1 = RegExp.$1,
				value = String(o[key]),
				value = $1.length == 1 ? value : ("00" + value).substr(value.length),
				format = format.replace($1, value);
		}
	}
	return format;
}

function tenantFilter() {
	var url = location.search;
	var tenantId = localStorage.getItem("tenantId");

	if (tenantId) {
		if (url.indexOf('tenantId') != -1) { // 已有，看能否匹配
			var nowTenantId = url.match(/tenantId=(\d+)/)[1];

			if (tenantId != nowTenantId) {
				url = url.replace(/tenantId=\d+/, 'tenantId=' + tenantId);
				location.assign(url);
			}
		} else {// 没有，加上
			url += url.indexOf('?') != -1 ? '&tenantId=' + tenantId : ('?tenantId=' + tenantId);
			location.assign(url);
		}
	} else {
		if (url.indexOf('tenantId') != -1) { // 取消过滤
			url = url.replace(/tenantId=\d+/, '');
			location.assign(url);
		}
	}
}

; (() => {
	function request(method, url, params, cb, cfg) {
		var xhr = new XMLHttpRequest();
		xhr.open(method, url);
		xhr.onreadystatechange = function(event) {
			if (this.readyState === 4 && this.status === 200) {
				var responseText = this.responseText.trim();
				var data = null;

				try {
					if (!responseText)
						throw '服务端返回空的字符串!';

					var parseContentType = cfg && cfg.parseContentType;
					switch (parseContentType) {
						case 'text':
							data = responseText;
							break;
						case 'xml':
							data = this.responseXML;
							break;
						case 'json':
						default:
							data = JSON.parse(responseText);
					}
				} catch (e) {
					alert('AJAX 错误:\n' + e + '\nThe url is:' + cb.url); // 提示用户 异常
				}

				cb && cb(data, this);
			}
		}

		if (cfg && cfg.header) {
			for (var i in cfg.header) {
				xhr.setRequestHeader(i, cfg.header[i]);
			}
		}

		xhr.send(params || null);
	}

	function json2fromParams(param) {
		let result = "";

		for (let name in param) {
			if (typeof param[name] != "function")
				result += "&" + name + "=" + encodeURIComponent(param[name]);
		}

		return result.substring(1);
	}

	function form(method, url, cb, params, cfg) {
		if (typeof params != 'string')
			params = json2fromParams(params);

		if (!cfg)
			cfg = {};

		cfg.header = { "Content-Type": "application/x-www-form-urlencoded" };
		request(method, url, params, cb, cfg);
	}

	function json(method, url, cb, params, cfg) {
		if (typeof params != 'string')
			params = JSON.stringify(params);

		if (!cfg)
			cfg = {};

		cfg.header = { "Content-Type": "application/json" };
		request(method, url, params, cb, cfg);
	}

	jsp.xhr = {
		get(url, cb, cfg) {
			request('GET', url, null, cb, cfg);
		},
		postForm(url, cb, params, cfg) {
			form("POST", url, cb, params, cfg);
		},
		postJson(url, cb, params, cfg) {
			json("POST", url, cb, params, cfg);
		},
		putForm(url, cb, params, cfg) {
			form("PUT", url, cb, params, cfg);
		},
		putJson(url, cb, params, cfg) {
			json("PUT", url, cb, params, cfg);
		},
		del(url, cb, cfg) {
			request('DELETE', url, null, cb, cfg);
		},
	};

})();