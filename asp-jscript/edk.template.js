/* 
 **************************************************************************
	 _____   _____   _   _   
	| ____| |  _  \ | | / /  
	| |__   | | | | | |/ /   
	|  __|  | | | | | |\ \   
	| |___  | |_| | | | \ \  
	|_____| |_____/ |_|  \_\ .template.js
 
	Module: 	edk.template.js
	Version: 	0.1.0
	Author: 	Frank Cheung / SP42
	Email: 		frank@ajaxjs.com
	Web: 		www.ajaxjs.com
	
	本代码可以免费使用、修改，希望我的程序能为您的工作带来方便，同时请保留这份请息。

	很简单的东西，其实基本上都谈不上什么版权之类的，
	但为了“做做样子”，不想漏了什么东西而因此有遗憾，还是贴上版权吧，呵呵。
	---Edk Licenses: Berkeley Software Distribution，BSD.-----

	BEGIN LICENSE BLOCK
	
	Copyright 版权所有 (c) 2011 Frank Cheung
	任何获得本软件副本及相关文档文件（下面简称为“软件”）的个人都可以免费获得不受限制处置本软件的权限，
	包括不受限制地使用、复制、修改、合并、出版、分发、重新许可或者销售本软件的副本，
	并且在满足下述条件时，允许本软件的受让人获得下述权限：

	在本软件的所有或者重要部分中包含上述版权公告信息和本权限公告信息。

	本软件不提供保证，不包含任何类型的保证（无论是明指的还是暗喻的），
	包含但不限于关于本软件的适销性、特定用途的适用性和无侵权保证。
	在任何情况下，无论是否签订了合约、存在侵权行为还是在其他情况下，
	本软件作者或版权持有人不对由本软件直接或间接产生的
	或由使用本软件或处置本软件所产生的任何索赔、损坏或者其他责任。
	
	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:
	
	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.
	
	END LICENSE BLOCK

 ***************************************************************************/
/**
 * 网页模板是把动态的数据和静态的表现组装到一起的工具，使得内容与表现方式可以分离，是Web开发中的重要技术手段。
 * 早期的Web开发很简单，没有“模板 ”的概念。
 * 只需把数据提取出来，放在HTML里面显示就达到程序的目的。
 * HTML代码混合于逻辑代码之中，HTML就是直接显示的内容，内嵌在HMTL 中<% ... %>表示为后台(服务端)执行代码，
 * 但很多情况<% ... %>中又有HTML的片段，至于怎么拼凑出HTML片段的方式方法各式各样、与多种的后台语言掺杂一起(ASP、PHP、JSP)各显神通。
 */
$$.tpl = {
	fillData : function(tpl, data){
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
				execJS.lastIndex = 0;
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
				getParent.lastIndex = 0;
				m2 = m2.match(getParent);
				m2 = m2[1];
				return falsey(parent[m2], m1);
			}else if(isObject(root) && getRoot.test(m2)){ /* 全称寻址 */
				getRoot.lastIndex = 0;
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
			
			evalReg.lastIndex = 0;
			if(!evalReg.test(ifTag)){
				$$.console.error('输入的标签{0}不是一个标准的if tag'.format(ifTag));
				throw '不是一个标准的if tag';
			}
			evalReg.lastIndex = 0;
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
			
			elseReg.lastIndex = 0;
			
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
		$$.tpl.fillData = function (tpl, data){
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
		
		return $$.tpl.fillData(tpl, data);
	}
	
	/**
	 * @todo what about document.write()?
	 * 若加入更多的静态文件，请修改 head 模板
	 * 关于如何生成关键字。
	 * 生成在HTML Head专用的关键字，比较全面的生成方法，适合SEO机器阅读。
	 * 原理是“标题+标题”里除去“非中文”及中文标点外的字符的、相邻两个字符的集合。
	 */
	,renderHeader : function (data){
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
	
	,renderHTML : function(data, body){
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
	,getTpl : function(xpathQuery, isSerialize){
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
};