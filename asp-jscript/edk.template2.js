
/**
 * 网页模板是把动态的数据和静态的表现组装到一起的工具，使得内容与表现方式可以分离，是 Web 开发中的重要技术手段。
 * 早期的 Web 开发很简单，没有“模板 ”的概念。
 * 只需把数据提取出来，放在 HTML 里面显示就达到程序的目的。
 * HTML 代码混合于逻辑代码之中，HTML 就是直接显示的内容，内嵌在 HMTL 中<% ... %>表示为后台(服务端)执行代码，
 * 但很多情况<% ... %>中又有 HTML 的片段，至于怎么拼凑出 HTML 片段的方式方法各式各样、与多种的后台语言掺杂一起(ASP、PHP、JSP)各显神通。
 */
$$.tplx = function(){

	/**
	 * @param  {String} tpl
	 * @param  {Object} data
	 * @return {String}
	 */
	function tpl(tpl, data){
		
	}
	
	$$.tplx = tpl;				// 覆盖类
	tpl.prototype = this;		// 为下次实例化时的关键一步
	tpl.apply(this, arguments);	// 执行构造器

	// 匹配闭合tag的正则。
	var divBlock = '<{0}\\s*[^>]*>((?:(?=([^<]+))\\2|<(?!{0}\\s*[^>]*>))*?)<\/({0})>';
	var matchValue = (function(){
		var
		 regexp    = /{([^}\n]*)}/ig
		,execJS	   = /^\[([^\]]*)\]$/
		,getParent = /^parent\.\b(\w*)\b$/i
		,getRoot   = /^root\.\b(\w*)\b$/i;
	
	/**
	 * @private
	 * @param {mixed} v
	 * @return {Boolean} 是否hash（是否对象）。
	 */
	function isObject(v){
		return !!v && isObject.toString.call(v) == isObject.token;
	}
	isObject.toString = Object.prototype.toString
	isObject.token    = '[object Object]';
	
	/**
	 * 替换置，如果未发现匹配的值，返回什么？一般原模版的格式（{xxx}），即m1。
	 * 注意那些无意义的值除外。
	 */
	function falsey(value, tpl){
		switch(value){
			case false:
			return 'false';
			case '':
			return value;
			case 0:
			return value;
		}
		
		return !!value ? value : tpl;
	}
	
 	function replace(m1, m2){
		var 
		 jsCode
		,parent  = arguments.callee.parent 
		,root	 = arguments.callee.root;
			
		if(execJS.test(m2)){
			
			jsCode = execJS.exec(m2);
			jsCode = jsCode[1];
			
			try{ // 写try之目的为容错性的设计。
				with(this){
					jsCode = eval(jsCode);
				}
				return jsCode;
			}catch(e){
				return '空值';
			}
		}
		
		if(isObject(parent) && getParent.test(m2)){ /* 父级寻址 */
			m2 = m2.match(getParent);
			m2 = m2[1];
			return falsey(parent[m2], m1);
		}else if(isObject(root) && getRoot.test(m2)){ /* 全称寻址 */
			m2 = m2.match(getRoot);
			m2 = m2[1];
			return falsey(root[m2], m1);
		}else{
			return falsey(this[m2], m1);
		}
	}
	
	 /**
	  * Lazy function
	  * @param {String} tpl
	  * @param {Object} dataObj
	  * @param {Object} parentObj
	  * @return {String}
	  */
	return function(tpl, dataObj, parentObj){
		if(!replace.root){
			replace.root = $$.tpl.root;
		}
		
		replace.parent = parentObj || 'itself'; // 无else部分，即表示全部就是trueBlock
		replace.scope  = dataObj;
		return tpl.replace(regexp, replace.delegate());// set scope object!
	}
	})();
	
	var matchIf = (function(){
		var 
		 elseReg = /<else\s*\/?>/i
		,evalReg = /<if\s*(?:eval="([\w\d=<>\.!'\|\(\)]*)")[^>]*>/i
		,tabReg  = /\t{2}/g
		,ifReg   = new RegExp(divBlock.format('if'), 'ig');
	
	/**
	 * 运算<if eval="exp">内容。
	 * @param {Object} data
	 * @param {Object} parent
	 * @param {String} ifTag
	 * @return {Boolean}
	 */
	function evalExp(data, parent, ifTag){
		var exp;
			 
		if(!evalReg.test(ifTag)){
			$$.console.error('输入的标签{0}不是一个标准的if tag'.format(ifTag));
			throw '不是一个标准的if tag';
		}
		exp = evalReg.exec(ifTag);
		exp = exp[1];
		exp = '(' + exp +')'; // make it as the Expression 表达式
//		debugger;
		// 通过with(data){}给定对象可见的范围。如果没有matchedObj，则为this。这也是有可能的。
		with(data || parent || this){
			try{
				exp = !!eval(exp);
			}catch(e){
				exp = false;
			}
		}
		return exp;
	}
	
	/**
	 * @this {OBject} 值对象
	 */
	function replace(m1, m2){
		var 
		 evalResult 		// 运算表达式后的结果
		,trueBlock			// true块
		,falseBlock = null	// false块，默认没有
			
		if(elseReg.test(m2)){// 有else部分
			var arr = m2.split(elseReg);
			if(!arr || arr.length < 2){
				$$.console.error('if-else不完整');
				throw "if-else不完整";
			};
			 
			trueBlock  = arr[0];
			falseBlock = arr[1];
		}else{
			trueBlock  = m2; // 无else部分，即表示全部就是trueBlock
		}
		
		trueBlock  = $$.trim(trueBlock);
		trueBlock  = trueBlock.replace(tabReg, '\t');// 消除多余tab符。
		// 求 if 的表达式
		evalResult = evalExp(this, arguments.callee.parent, m1);
		
		if(evalResult){
			return trueBlock;
		}else if(!evalResult && falseBlock == null){
			return '';
		}else if(!evalResult && falseBlock){
			return falseBlock;
		}else{
			// 不应该会走到这里的。若真的走到便抛出一个异常。
			$$.console.error('求if-else块时发生致命错误！');
			throw '求if-else块时发生致命错误！';							
		}
	}
	
		return function(tpl, data, parentData){
			ifReg.lastIndex = 0;// 有global时需要注意 lastIndex的问题。
			
			if(ifReg.test(tpl)){
				replace.parent = parentData;
				replace.scope  = data;
				return tpl.replace(ifReg, replace.delegate());
			}else{
				return tpl;
			}
		}
	})();

	function replace(m1, m2, m3, m4){
		var 
	     parentData = this
		,values		= parentData[m4]
		,callee		= $$.tpl.fillData
    	,str 		= ''
		,arrTpl;
	    
	    if(values && !values.pop){
	    	return callee(m2, values);			// 递归 for sub
	    }else if(values && values.pop){/* 有时候会因为大小写问题，无法匹配。请检查key即（m4）是否一致 */
	        m2 = callee(m2, values);			// 递归 for sub
        		
			for(var i = 0, j = values.length; i < j; i++){
				value  = values[i];
				m2	   = matchIf(m2, value, parentData); 
				arrTpl = callee(m2, value);
				
				str += matchValue(arrTpl, value, parentData);
			} 
			return str;
			
	    }else if(values == false /* 特定为false的情况 */ ){
	    	return str;
	    }else{
	    	$$.console.warn('No data provided');// 怎么数据源没提供数据？ 宜debug之
	    	return 'edkTpl_nothing';
	    }
	}
	
	/**
	 * @param  {String} tpl
	 * @param  {Object} data
	 * @return {String}
	 */
	this.render = function (tpl, data){
		if(!$$.tpl.root){
			$$.tpl.root = data;
		}

	    if(data && !data.pop){
	    	replace.scope = data;
	    	var _replace = replace.delegate();
	    	
	    	for(var i in data){
				tpl = tpl.replace(new RegExp(divBlock.format(i), 'i'), _replace);
	    	}
	    	
	    	tpl = matchIf(tpl, data, data); 
	    	
		    return matchValue(tpl, data);
	    }else if(data && data.pop){
	    	return tpl;
	    }else if(!data){
//	    		throw '没有输入数据的参数 data？';
	    }
	    return '';
	}
	
	/**
	 * @todo what about document.write()?
	 * 若加入更多的静态文件，请修改 head 模板
	 * 关于如何生成关键字。
	 * 生成在HTML Head专用的关键字，比较全面的生成方法，适合SEO机器阅读。
	 * 原理是“标题+标题”里除去“非中文”及中文标点外的字符的、相邻两个字符的集合。
	 * @static
	 */
	this.renderHeader = tpl.renderHeader = function (data){
		var 
		 getHeader_Reg_1 = /[\x00-\xff]+/g
		,getHeader_Reg_2 = /[\n|\s{0,2}|，|。|、|！|？|：|“|”|【|］|—|—|（|）|…]/g;
		
		var tpl = $$.cfg.commonTag('//page/head');
	    var meta_words = ' ', entityTitle;
	    
	    if(!data.entityTitle){
	    	data.entityTitle = ' ';
	    }
	    entityTitle = data.entityTitle;
	    entityTitle = entityTitle.replace(getHeader_Reg_1, '');
	    entityTitle = entityTitle.replace(getHeader_Reg_2, '');
	
	    for(var i = 0, j = entityTitle.length; i < j; i++){
	        meta_words += ',' + entityTitle.substr(i, 2);
	    }
	
		data.meta_words += meta_words;
		
		// 静态资源的所在目录
		//'static.ajaxjs.com';
		data.libsFolder  = $$.cfg.edk_isDebugging ? '127.0.0.1' : 'static.ajaxjs.com';
		data.isDebugging = $$.cfg.edk_isDebugging;
		
		return $$.tpl.fillData(tpl, data);
	}
	
	/**
	 * 渲染完整的 HTML。
	 * @param {Object} data
	 * @param {String} body
	 * @return {String}
	 */
	this.renderHTML = tpl.renderHTML = function(data, body){
		var htmlTag = 
			'<!DOCTYPE html>\n' +
			'<html>\n{0}' +
			'	\t<body>\n' +
			'			{1}' +
			'	\t</body>\n' +
			'</html>';
			
		return htmlTag.format(this.renderHeader(data), body);
	}
	
	/**
	 * 定位某一 XML 节点。如果找不到则返回一空数组。
	 * 用法：
		var doc = loadXML('edk.xml');
		var xml = getTpl.call(doc, '//head', false); 
	 * @param	{String}	xpathQuery	查询定位符。
	 * @param	{Boolean}	isSerialize	可选的，是否序列号查询结果为字符串，默认为true。
	 */
	this.getTpl = tpl.getTpl = function(xpathQuery, isSerialize){
		var nodes = [];
		var msg = 'NO Exist Node! 不存在匹配的节点，请检查输入的条件再作查询。';
		
		if(typeof isSerialize == 'undefined'){
			isSerialize = true;
		}
		
		if(typeof this.selectNodes != 'undefined'){
			nodes = this.selectSingleNode(xpathQuery);
			if(nodes == null){
				throw msg;
			}
			return isSerialize ? nodes.xml :nodes;
		}else if(document.implementation.hasFeature('XPath', '3.0')){
			// FF需要作进一步转换
			var resolver = this.createNSResolver(this.documentElement);
			var items	 = this.evaluate(xpathQuery, this, resolver, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
			
			for(var i = 0, j = items.snapshotLength; i < j; i++){
			  nodes[i] = items.snapshotItem(i);
			}
			
			if(!nodes.length){
				throw msg;
			}
			
			return isSerialize ? new XMLSerializer().serializeToString(nodes[0]) : nodes;
		}
		
		return nodes;
	}
}