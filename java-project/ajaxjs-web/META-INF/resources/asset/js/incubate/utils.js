define(function(require, exports, module) {
	// 根据生日或者日期 获取 生肖和星座的 JavaScript代码
	// 取生肖, 参数必须是四位的年    
	function getshengxiao(yyyy){
	    //by Go_Rush(阿舜) from http://ashun.cnblogs.com/
	    
	    var arr = ['猴','鸡','狗','猪','鼠','牛','虎','兔','龙','蛇','马','羊'];
	    return /^\d{4}$/.test(yyyy) ? arr [yyyy % 12] : null;
	}

	// 取星座, 参数分别是 月份和日期
	function getxingzuo(month,day){    
	    //by Go_Rush(阿舜) from http://ashun.cnblogs.com/
	        
	    var d = new Date(1999,month-1,day,0,0,0);
	    var arr = [];
	    arr.push(["魔羯座",new Date(1999, 0, 1,0,0,0)])
	    arr.push(["水瓶座",new Date(1999, 0,20,0,0,0)])
	    arr.push(["双鱼座",new Date(1999, 1,19,0,0,0)])
	    arr.push(["牡羊座",new Date(1999, 2,21,0,0,0)])
	    arr.push(["金牛座",new Date(1999, 3,21,0,0,0)])
	    arr.push(["双子座",new Date(1999, 4,21,0,0,0)])
	    arr.push(["巨蟹座",new Date(1999, 5,22,0,0,0)])    
	    arr.push(["狮子座",new Date(1999, 6,23,0,0,0)])
	    arr.push(["处女座",new Date(1999, 7,23,0,0,0)])
	    arr.push(["天秤座",new Date(1999, 8,23,0,0,0)])
	    arr.push(["天蝎座",new Date(1999, 9,23,0,0,0)])
	    arr.push(["射手座",new Date(1999,10,22,0,0,0)])
	    arr.push(["魔羯座",new Date(1999,11,22,0,0,0)])        
	    for(var i = arr.length - 1;i >= 0;i--){
	        if (d >= arr[i][1]) return arr[i][0];    
	    }
	}

	/*
	魔羯座(12/22 - 1/19)、水瓶座(1/20 - 2/18)、双鱼座(2/19 - 3/20)、牡羊座(3/21 - 4/20)、金牛座(4/21 - 5/20)、
	双子座(5/21 - 6/21)、巨蟹座(6/22 - 7/22)、狮子座(7/23 - 8/22)、处女座(8/23 - 9/22)、天秤座(9/23 - 10/22)、
	天蝎座(10/23 - 11/21)、射手座(11/22 - 12/21)    
	*/

	// 根据身份证号码取  省份，生日
	//根据身份证取 省份,生日，性别  Go_Rush(阿舜) from http://ashun.cnblogs.com/
	function getInfo(id) {
		var arr = [null,null,null,null,null,null,null,null,null,null,null,"北京","天津","河北","山西","内蒙古"
	             ,null,null,null,null,null,"辽宁","吉林","黑龙江",null,null,null,null,null,null,null,"上海"
	             ,"江苏","浙江","安微","福建","江西","山东",null,null,null,"河南","湖北","湖南","广东","广西","海南"
	             ,null,null,null,"重庆","四川","贵州","云南","西藏",null,null,null,null,null,null,"陕西","甘肃"
	             ,"青海","宁夏","新疆",null,null,null,null,null,"台湾",null,null,null,null,null,null,null,null
	             ,null,"香港","澳门",null,null,null,null,null,null,null,null,"国外"];

		var id = String(id),
			prov = arr[id.slice(0, 2)],
			sex = id.slice(14, 17) % 2 ? "男" : "女";
		var birthday = (new Date(id.slice(6, 10), id.slice(10, 12) - 1, id.slice(12, 14))).toLocaleDateString();

		return [prov, birthday, sex];
	}

	/*------------------------------------
    -----------------------------------
   			剩余字数提示
    -----------------------------------
   ------------------------------------*/
   exports.whenpresscheckCharLength = whenpresscheckCharLength;
   function whenpresscheckCharLength(formEl){
   	var whenpresscheckCharLength = formEl.$('.whenpresscheckCharLength')(true);
   	if (whenpresscheckCharLength){
	    	var _for = whenpresscheckCharLength.getAttribute('for'),
	    		showRemainCharLength = formEl.$('.' + _for)(true);
   		var charLimit = whenpresscheckCharLength.hasAttribute('charLimit') ? whenpresscheckCharLength.getAttribute('charLimit') :20; 

   		var inputed = whenpresscheckCharLength.value.length;

   		if(inputed > 0){
   			showRemainCharLength.innerHTML = String(charLimit - inputed);
   		}else showRemainCharLength.innerHTML = String(charLimit);
		
		    function handler(e){
				var pressedLength = whenpresscheckCharLength.value.length,
					left = charLimit - pressedLength;
				
				if((left + 1) <= 0){
					e.preventDefault();
					shake(whenpresscheckCharLength, 'red');
				}else showRemainCharLength.innerHTML = String(left);
				// console.log(charLimit - pressedLength);
			}

   		whenpresscheckCharLength.onkeyup = handler;
   		whenpresscheckCharLength.onpaste = handler;
   	}
   } 


	function shake(ele, cls, times) {
		var i = 0, times = times || 2;
		ele.addCls(cls);
		var t = window.setInterval(function(){
			i++;
			ele.removeCls(cls);
			if(i % 2)ele.addCls(cls);
			if (i == 2 * times) {
				window.clearInterval(t);
				ele.removeCls(cls);
			}
		}, 150);
	}
	
	/**
	 * 输入参数，还原它的primitive值。有点类似 eval() 的作用，却不使用 eval()。
	 * @param  {Mixed} v
	 * @return {Mixed}
	 */
	exports.getPrimitives = function (v){
		if(v){
			if(v == 'true' ) return true;
			if(v == 'false') return false;
			if(v.toString() == (new Date(v)).toString()) return new Date(v); // v is a date but in Stirng Type
	      	if(v == String(Number(v))) return Number(v);
		}
	  	return v;
	}

	exports.json2url = function(json){
		var str = JSON.stringify(json);
		return str.replace(/,/g, '&').replace(/:/g,'=').replace(/\{|}|"/g, '');
	}
	
	/**
	 * 
	 * @param  {String} ids [description]
	 * @return {Object}     [description]
	 */
	function url2json(ids){
		var _json = {};
		if(ids && ids.indexOf("#") != -1){
			
			ids = ids.split('#');
			ids = ids.pop();
			ids = ids.replace(/&/g, '","').replace(/=/g, '":"');
			if(ids){
				_json = JSON.parse('{"' + ids +'"}');
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

	function setUrl(key, value){
		var hash = window.location.hash;
		console.log(value)
		if(hash.indexOf(key) != -1){
			var reg = new RegExp(key + '=[\\w-]+');
			window.location.hash = hash.replace(reg, key + '=' + value);
		}else{
			// append
			if(!hash)
				window.location.hash = key + '=' + value;
			else{
				window.location.hash += '&' + key + '=' + value;
			}
		}
	}
	
	/**
    * 回到页面顶部（高度比较短的时候貌似不起作用）
    * @param acceleration 加速度
    * @param time 时间间隔 (毫秒)
    **/
   function goTop(acceleration, time) {
       acceleration = acceleration || 0.1;
       time = time || 16;

       var x1 = y1 = x2 = y2 = x3 = y3 = 0;

       if (document.documentElement) 
           x1 = document.documentElement.scrollLeft || 0, y1 = document.documentElement.scrollTop || 0;
       
       if (document.body) 
           x2 = document.body.scrollLeft || 0, y2 = document.body.scrollTop || 0;
       
       x3 = window.scrollX || 0, y3 = window.scrollY || 0;

       // 滚动条到页面顶部的水平距离
       var x = Math.max(x1, Math.max(x2, x3));
       // 滚动条到页面顶部的垂直距离
       var y = Math.max(y1, Math.max(y2, y3));

       // 滚动距离 = 目前距离 / 速度, 因为距离原来越小, 速度是大于 1 的数, 所以滚动距离会越来越小
       var speed = 1 + acceleration;
       window.scrollTo(Math.floor(x / speed), Math.floor(y / speed));

       // 如果距离不为零, 继续调用迭代本函数
       if (x > 0 || y > 0) {
           var invokeFunction = "goTop(" + acceleration + ", " + time + ")";
           window.setTimeout(invokeFunction, time);
       }
   }
	

	
	/**
	 * 微型的模板程序
	 * 支持 JS 语句执行、JSONPath、模板方法、IF 判断标签
	 */
	define(function(require, exports, module) {

	    /**
	     * 匹配每个花号
	     */
	    function replaceValue(match, key) {   
	        var value;
	        if (key in this.data) {
	            value = this.data[key]; // 此为最简单之情形
	        } else if (key.indexOf('x_root.') != -1) {
	            value = '';// 捕获 jsonPath 段并解析返回。当前是空字符串，@todo JSONPath
	        } else if (execJS.test(key) === true) {
	            value = parseJS.call(this, key);
	        } else {
	            value = '[没有该值的定义 undefined!]';
	        }
	        return value;
	    }

	    var execJS = /^\[([^\]]*)\]$/;
		/**
		 * 捕获 js 段并执行返回。
		 * @param {String} key
		 * @return {String}
		 */
	    function parseJS(key) {
	        var js = key.match(execJS), js = js[1],
	        	fnBody = 'with(value){ return {0};} ', fnBody = fnBody.format(js),
	        	fn = new Function('value', fnBody), 
	        	value;

	        try{
	        	value = fn.call(this.tpl, this.data);
	        }catch(e){
	        	value = '[tpl]捕获 js 段失败，详情：' + key;
	        	console.warn(value);
	        }
	        return value;
	    }

	    // @todo 非捕获匹配未考虑好 要琢磨下
		var getIf_Eval = /<if\s*(?:eval="([\s\w\d=<>\.!'\|\(\)&]*)")[^>]*>/i; // 2012-12-26 加入一个  /s 空格
		var regexp = /{([^}\n]*)}/ig;
		function replaceIf(match, key){
			var _eval = match.match(getIf_Eval);
			if (_eval == null){
				throw "[tpl] if tag 必须配合 eval 属性";
			}else if(!_eval[1]){
				throw '匹配失败'; // 不是第一个元素，而是第二个才是！
			}
			
			_eval = _eval[1];	// 求出了表达式，但为字符串，不能直接求值。
			
			var isReturnTrueFn = new Function('value', 'with(value){ return ' + _eval + ';}');

			var ifReturnValue;
			try{
				ifReturnValue = isReturnTrueFn.call(this.tpl, this.data);
			}catch(e){
				console.warn('[replaceIf]执行{0}错误！'.format(_eval));
			}
			
			if (ifReturnValue){
				return key.replace(regexp, replaceValue.bind(this));
			}else{
				return ''; // 条件不充分，返回空字符串。
			}
		}

	    // 匹配闭合tag的正则。
	    var divBlock = '<{0}\\s*[^>]*>((?:(?=([^<]+))\\2|<(?!{0}\\s*[^>]*>))*?)<\/({0})>',
	    	ifTag	= new RegExp(divBlock.format('if'), 'ig');
	    
	    /**
	     * 实现 alert(tpl.replace('jjj{dd}jjj', {dd:'dfsdfsdfdf'}));
	     * @param {String} tpl
	     * @param {JSON}
	     * @return {String}
	     */
		function replaceTpl(tpl, json) {
	        if (!tpl || !json)throw '[tpl] 没有模板或者数据';
	        

			// if(!this instanceof module.exports){
			// 	throw "欲执行该函数，其作用域必须为 $$.tpl 类型!";
			// }
			var sendArgsByScope = {
	        	tpl	: this,
	        	data : json	
			};

	        ifTag.lastIndex = 0;
	        var html = (ifTag.test(tpl) === true) ? 
	        			tpl.replace(ifTag, replaceIf.bind(sendArgsByScope)) : tpl;

	        return html.replace(regexp, replaceValue.bind(sendArgsByScope));
	    }
	    exports.replace = replaceTpl;

	    var tplTag = new RegExp(divBlock.format('tpl'), 'ig');

	    /**
	     * @param {String} m_tpl
	     * @param {JSON}	dataSet 支持多层的JSON
	     * @param {Function} everyFn 遍历数据每一项的执行的函数。
	     */
	    exports.renderList = {
	    	tpl : null,
	    	data : null,
	    	everyFn :null,
	    	apply : function(){
			    var self = this;
			    var data = this.data;
		        var everyFn = this.everyFn;
				// var match, tpl;
				// while ((match = tplTag.exec(m_tpl)) != null) {
				//    tpl = match[1];
				//    console.log(tpl);
				// }

		        if(typeof this.tpl == 'object')this.tpl = this.tpl.value; // for TEXTAREA HTMLElement
		        
			    
			    if(!data)
			    	throw '[tpl] 没有输入数据！';
			    else if(!this.tpl || typeof this.tpl != 'string')
			    	throw '[tpl] 没有输入模板！或者模板类型不正确，应为 String 类型。';

			    var html = this.tpl.replace(tplTag, function (a, tpl) {
			        var htmls = [];
			        for (var i = 0, j = data.length; i < j; i++) {
			            everyFn && everyFn(data[i], i, j);
			            htmls.push(replaceTpl.call(self, tpl, data[i]));
			        }
			
			        return htmls.join('');
			    }), html = replaceTpl.call(this, html, data);// 顶级替换	
			    
			    if(arguments[0]){ // 输出到 DOM
			    	if(typeof arguments[0] == 'string'){
			    		if($("#"+arguments[0])){
			    			$("#"+arguments[0]).html(html);
			    		}else{
			    			document.getElementById(arguments[0]).innerHTML = html;
			    		}
			    	}else{
			    		arguments[0].innerHTML = html;
			    	}
			    }

			    return html;
			}
	    };

		/**
		 * 为每一项设置序号和总数 index/length 字段，供显示所用。
		 * 注意序号从 1 开始
		 * @param  {Object} item [description]
		 * @param  {Number} i    [description]
		 * @param  {Number} j    [description]
		 */
		exports.showIndex = function (item, i, j){
			item.index = i + 1;
			item.length = j;
		} 
	});
	
});