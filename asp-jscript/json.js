$$.JSON = (function() {
	function NumberAs(num) {
		return num.toString();
	}
	
//	function StringAs(string) {
//		return '"' + object.replace(/(\\|\")/g, "\\$1").replace(/\n|\r|\t/g,
//				function() {
//					var a = arguments[0];
//					return (a == '\n') ? '\\n' : (a == '\r')
//							? '\\r'
//							: (a == '\t') ? '\\t' : ""
//				}) + '"';
//	}
	
	function StringAs(string, isURLEncode){
		var reg = StringAs.reg;
		var str = '"' + string.replace(reg, "\\$1") + '"';
		return isURLEncode ? encodeURIComponent(str) : str; 
	}
	StringAs.reg = /(\\|\"|\n|\r|\t)/g

	function ObjectAs(object) {
		var 
		 value
		,results = []
		,callee  = $$.JSON.encode;

		for (var i in object) {
			value = callee(object[i]);
			if (value !== undefined) {
				results.push(callee(i) + ':' + value);
			}
		}
		return '{' + results.join(',') + '}';
	}

	function ArrayAs(array) {
		var 
		 value
		,results = []
		,callee  = $$.JSON.encode;

		for (var i = 0, j = array.length; i < j; i++) {
			value = callee(array[i]);
			if (value !== undefined) {
				results.push(value);
			};
		}
		return '[' + results.join(',') + ']';
	}

	/**
	 * "new Date(" + obj.valueOf() + ")";
	 */
	function DateAs(date){
		return "new Date(" + date.valueOf() + ")";// @todo
		
		var makeTwoDigits = DateAs.makeTwoDigits;
		
		return date.getUTCFullYear() 				+ '-'
			+ makeTwoDigits(date.getUTCMonth() + 1) + '-'
			+ makeTwoDigits(date.getUTCDate())		+ 'T'
			+ makeTwoDigits(date.getUTCHours())		+ ':'
			+ makeTwoDigits(date.getUTCMinutes()) 	+ ':'
			+ makeTwoDigits(date.getUTCSeconds())	+ 'Z';
	}
	
	/**
	 * Format integers to have at least two digits.
	 */
	DateAs.makeTwoDigits = function(n) {
		return n < 10 ? '0' + n : n;
	}
	
	/**
	 * decode日期函数，日期格式是ISO，以下就是这个转换函数，蛮有意思的是with(){} statement的用法:)
	 */
	function decodeDate(str){
		var getDate = decodeDate.reg;
		if(!getDate.test(str)){
			return null; 
		}else{
			with(getDate.exec(str)){
				return new Date([1], [2], [3], [4], [5], [6]);
			} 
  		} 
	}
	
	decodeDate.reg = /(\d{4})-(\d{1,2})-(\d{1,2})T(\d{1,2}):(\d{1,2}):(\d{1,2})/;

	return {
		/**
		 * eval() 与 new Function 谁快？
		 * 答案是差不多，见：
		 * http://jsperf.com/json-parsing/12
		 * 可以自动 decodeUrlcomponent 解码
		 * @param {String} jsonStr
		 * @return {Object}
		 */
		decode : function(jsonStr) {
			jsonStr = "(" + jsonStr + ')';
			var obj;
			try{
				obj = eval(jsonStr);
			}catch(e){
				jsonStr = decodeURIComponent(jsonStr);
				obj = eval(jsonStr);
			}
			return obj;
		},
		
		/**
		 * uneval
		 * @param {String} object
		 * @param {Boolean} isURLEncode
		 */
		encode : function(object, isURLEncode) {
			if (object === null) {
				return 'null';
			}else if(typeof object == 'undefined'){
				return;
			}
			
			if(object instanceof Boolean){
				return NumberAs(object);
			}else if(object instanceof Number){
				return NumberAs(object);
			}else if(object instanceof String){
				return StringAs(object, isURLEncode);
			}else if(object instanceof Array){
				return ArrayAs(object);
			}else if(object instanceof Date){
				return DateAs(object);
			}else if(object instanceof RegExp){
				return;
			}
			
			switch (object.constructor) {
				case Boolean :
					return NumberAs(object);
				case Number :
					return NumberAs(object);
				case String :
					return StringAs(object, isURLEncode);
				case Array :
					return ArrayAs(object);
				case Date :
					return DateAs(object);
				case RegExp :
					return RegExp;
			}
			
			switch (typeof object){
				case 'undefined' :
				case 'unknown' :
				case 'function' :
				case 'regexp' :
					return;
				case 'boolean' :
				case 'number' :
					return NumberAs(object);
				case 'date' :
					return DateAs(object);
				case 'string' :
					return StringAs(object, isURLEncode);
				case 'object' :
					return ObjectAs(object);
				case 'array' :
					return ArrayAs(object);
			}
			
			return;
		}
	};
})();