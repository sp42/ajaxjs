/**
 * @class
 * @singleton
 * 兼容浏览器与服务端的XML加载器。
 */
$$.xml = $$.XML = new (function(){
	
	/**
	 * 把 XML 格式转换为 JSON 输出。MS ONLY 暂时。
	 * @param	{IXMLDOMNode}	n
	 * @return	{Object}		JSON
	 */
	this.xml2json = function (node){
		var obj = {};
		var element = node.firstChild;
		while (element) {
			if (element.nodeType === 1) {
				var name = element.nodeName;
				var sub;
				
				sub = arguments.callee(element)
				sub.nodeValue = "";
				sub.xml = element.xml;
				sub.toString = function() {
					return this.nodeValue;
				};
				sub.toXMLString = function() {
					return this.xml;
				}
				// get attributes
				if (element.attributes) {
					for (var i = 0; i < element.attributes.length; i++) {
						var attribute = element.attributes[i];
						sub[attribute.nodeName] = attribute.nodeValue;
					}
				}
				// get nodeValue
				if (element.firstChild) {
					var nodeType = element.firstChild.nodeType;
					if (nodeType === 3 || nodeType === 4) {
						sub.nodeValue = element.firstChild.nodeValue;
					}
				}
				// node already exists?
				if (obj[name]) {
					// need to create array?
					if (!obj[name].length) {
						var temp = obj[name];
						obj[name] = [];
						obj[name].push(temp);
					}
					// append object to array
					obj[name].push(sub);
				} else {
					// create object
					obj[name] = sub;
				}
			}
			element = element.nextSibling;
		}
		return obj;
	}
	
	// @todo 需要吗？
	var Xml = {
		text: function(text) {
			return text.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;")
		},
	
		attr: function(name, value) {
			return (name && value != null) ? (" " + name + "=\"" + this.text(value).replace(/"/g, /* "-->\x22 ? */ "&quot;") + "\"") : "";
		},
	
		cdata: function(text) {
			return (text) ? "<![CDATA[" + text.toString().replace("]>>", "]&gt;&gt;") + "]>>" : "";
		}
	};
	
	/**
	 * json2xml
	 */
	this.json2xml = function(obj) {
		var str = '';
		var tpl = '<{0} type="{1}">{2}</{0}>\n';
		
		function serialize(obj) {
			var fn	= arguments.callee;
			var xml	= [];
			var typ	= typeof obj;
			
			switch (typ) {
				case "object" : {
					if (obj === null) {
						xml.push("<" + i + " />\n");	// 空的闭合标签
					} else if (typeof obj.getTime === "function") {
						xml.push(tpl.format(i, 'date', obj.toUTCString()));
					} else if (typeof obj.join === "function") {
						for (var j = 0; j < obj.length; j++) {
							xml.push(fn(obj[j]));
						}
					} else {
						xml.push(tpl.format(i, typ, fn(obj)));
					}
					break;
				}
				case "string" :
					obj = obj.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
				case "date" :
				case "boolean" :
				case "number" : 
					xml.push(tpl.format(i ,typ, obj));
			}
			return xml.join("");
		}
		for (var i in obj) {
			str += serialize(obj[i]);
		}
		return str;
	}
		
	/**
	 * 兼容浏览器与服务端的XML加载器。注意，当前模式下关闭异步的通讯方式。
	 * create a document object
	 * @param	{String}	xml		XML文档路径或者XML文档片段。
	 * @param	{Boolean}	isNode	true表示为送入的为XML文档片段。
	 * @return {Object} the document
	 */
	this.loadXML = function(xml, isNode){
		var doc;
		
		if(typeof ActiveXObject != 'undefined'){
			doc = $$.xml.doc();
		}else if(typeof document != 'undefined' && !isNode){
			if(document.implementation && document.implementation.createDocument){
				doc = document.implementation.createDocument("", "", null);
			}  
		}else if(typeof DOMParser != 'undefined' && isNode){
			doc = new DOMParser().parseFromString(xml, "text/xml");		// 加载XML片段（Moliza Firefox）
			return doc;
		}
		
		if(!doc){
			throw '创建XML文档对象失败！';
		}
		
		doc.async = false;  // 关闭异步特性
		

		if		(xml && !isNode && (doc.load(xml) 	 == false)){		// 加载一份完整的XML文档(Moliza Firefox 与 IE均如此)
			throw '加载XML文档资源失败！';
		}else if(xml &&  isNode && (doc.loadXML(xml) == false)){		// 加载XML片段（IE）
			throw '加载XML片段失败！';									
		}
		
		return doc;
	}
	
	/**
	 * 定位某一XML节点。如果找不到则返回一空数组。
	 * 用法：
		var doc = loadXML('edk.xml');
		var xml = getNode.call(doc, '//head', false); 
	 * @param	{String}	xpathQuery	查询定位符。
	 * @param	{Boolean}	isSerialize	可选的，是否序列号查询结果为字符串，默认为true。
	 */
	this.getNode = function(xpathQuery, isSerialize){
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
	
	/**
	 * perform an XLS transformation on an XML document and store the results in an element
	 * @param {Element} target the element to populate with the results of the transformation
	 */
	this.transform = function (xsltDoc, target){
		if(XSLTProcessor){
			var processor = new XSLTProcessor();
			processor.importStylesheet(xsltDoc);
			var doc = processor.transformToFragment(this, document);
			target.appendChild(doc);
		}else{
			target.innerHTML = this.transformNode(xsltDoc);
		}		
	}
			
	/**
	 * 依据数据值是什么（在 data 里查找匹配的），决定选中哪一个 Option 控件。
	 * @param	{IXMLDOMNode}	n		XML节点对象。
	 * @param	{Object}		data	数据对象。
	 * @return	{IXMLDOMNode}			XML节点对象。
	 */
	this.setSelectByNode = function(n, data){
        var i, k;
        var selectEl;
        
        for(i in data){
            k = ".//select[@name='{0}']".format(i);
            selectEl = n.selectSingleNode(k);
            if(selectEl){
                // 选中value一样的节点
                for(var z = 0; z < 2; z++){
                    if( selectEl.childNodes(z).attributes(0).value == data[i]){
                        selectEl.childNodes(z).setAttribute('selected', 'true'); // 设置为选中！
                    } 
                }
            }
        }
        return n;
	}
		
	/**
	 * 依据数据值是什么（在 data 里查找匹配的），决定选中哪一个Radio控件。
	 * @param	{IXMLDOMNode}	n		XML节点对象。
	 * @param	{Object}		data	数据对象。
	 * @return	{IXMLDOMNode}			XML节点对象。
	 */
	this.setRadioByNode = function(n, data){
        var 
         k
        ,selectEl;
        
        for(var i in data){
            k = ".//input[@name='{0}']".format(i);
            
            selectEl = n.selectNodes(k);
            if(selectEl && selectEl.length > 0){
                // 选中value一样的节点
                for(var z = 0; z < selectEl.length; z++){
                	// 默认attributes(3)是name属性
                    if( selectEl(z).attributes(1).value == data[i]){
                        selectEl(z).setAttribute('checked', 'true'); // 设置为选中！
                    } 
                }
            }
        }
        return n;
	}
	
	/**
	 * 从XML文档中选择指定的模板文件，将其数据绑定 data 输出。
	 * 有取消 CData 作转意之功能。
	 * @param	{String}	xmlFile	XML 片段或者是 XML 文件，需要完全的文件路径地址，一般需要 Server.Mappath() 获取真实地址后才输入到这里。
	 * @param	{String}	xPath 	XPath路径。
	 * @param	{Object} 	data	（可选的）数据实体，通常是 JSON 实体或者是配置文件 $$.cfg。
	 * @return	{String}			携带数据的HTML。
	 */
	this.from_XML_Tpl = function(xmlFile, xPath, data){
		var  
		 xml	= new ActiveXObject('Msxml2.DOMDocument.6.0')
		,node
		,tpl
		,html;

        // 自动判别是否可使用服务端的方法
		if(typeof Server != 'undefiend'){
			if(xml.load(Server.Mappath(xmlFile)) != true){
				throw '加载' + xmlFile + '模板文件失败';
			};
		}else{
			if(xml.loadXML(xmlFile) != true){
				throw '加载' + xmlFile + '片段失败';
			};			
		}
		
		node = xml.selectSingleNode(xPath);
		
		if(!node){
			throw '没有模板节点';
		}
		
		// Option的Selected属性等的居然没用完整XML陈述形式！！
		if(data){
			$$.XML.setSelectByNode(node, data);
			$$.XML.setRadioByNode(node,  data);
		}
   		
   		 tpl  = node.xml
   		,tpl  = tpl.replace('&amp;', '&') // 规避XML标准的转义
   		,xml  = null
   		,html = data ? new Edk.Template(tpl).applyTemplate(data) : tpl;
   		
		// 由于有些地步不合XML WellForm标准，故用CData豁免之，先还原。
   		if(html.indexOf('<![CDATA[')){ 
   			html = html.replace('<![CDATA[', '').replace(']]>', '');
   		}
   		return html;
	}

	this.saveCDATA = function (){
		var post     = $$.form.post();
		var filePath = Server.Mappath($$.cfg.edk_root + '/app/form/staticPage.xml');
	    
	    var xml = new ActiveXObject('Msxml2.DOMDocument.6.0');
	    if(!xml.load(filePath)){
	        throw "打开模板文件错误";
	    }

		var parentNode = xml.selectSingleNode('//' + page.split('.').pop());
		var CDataNode  = xml.createCDATASection(post['Content']);
		parentNode.replaceChild(CDataNode, parentNode.childNodes(0));
		
		$$.XML.saveXML(xml, filePath);
		
		Response.Write('写入CData数据成功！');
		return true;
	}
});