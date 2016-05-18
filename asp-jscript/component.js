$$.xml = {
	doc : function(){
		var doc;
		
		/**
		 * ActiveX Objects for importing XML (IE only)
		 * @type Array
		 */
		var MSXML = [
			"Msxml2.DOMDocument.6.0",
			"Msxml2.DOMDocument.5.0", 
			"Msxml2.DOMDocument.4.0", 
			"Msxml2.DOMDocument.3.0", 
			"MSXML2.DOMDocument", 
			"Microsoft.XMLDOM"   
		];
		
		for(var i = 0, j = MSXML.length; i < j; i++){
			try{
				doc = new ActiveXObject(MSXML[i]);
				break;
			}catch(e){}
		}
		
		return doc;	
	}

	/**
	 * 保存 XML 对象为 XML 文本文件。
	 * 注意：Server Side Only
	 * @param	{XMLDocument}	xmlDoc		XML 文档对象本身。
	 * @param	{String}		xmlFilePath	可选的。XML 文档的真实磁盘路径。
	 * @return	{Boolean}					是否操作成功。
	 */
	,save : function(xmlDoc, xmlFilePath){
	 	xmlFilePath = xmlFilePath || xmlDoc.url.replace(/file:\/\/\//,'');
	 	// make a clone
		var	saver = this.doc();
			saver.loadXML(xmlDoc.xml);
			
		if(	saver.readyState == 4 && saver.parsed){
			saver.save(xmlFilePath);
		}
		
		return true;
	}
		
	/**
	 * @param {String} node
	 * @param {String} str
	 */
	,saveCDATA : function (node, str){
		var parentNode = this.selectSingleNode(node);
		var CDataNode  = this.createCDATASection(str);
		parentNode.replaceChild(CDataNode, parentNode.childNodes(0));
		
		return true;// 写入CData数据成功！
	}
};

$$.file = {
	/** 
	 * 打开文件，读取其内容，返回文本的格式。
	 * @param	{String}	path		文件路径
	 * @param	{String}	sCharset	指定字符集
	 * @return	{String} 				文本内容
	 */
	read_File_with_Chartset : function (filename, sCharset){
		var stream = new ActiveXObject('adodb.stream');
		var fileContent;
	
		with(stream){
			type = 2;// 1－二进制，2－文本
			mode = 3;// 1－读，2－写，3－读写
			open();
		
			if (!sCharset) {
				try{
					charset = "437"; // why try, cause some bug now
				}catch(e){}
				loadFromFile(filename);
				
				// get the BOM(byte order mark) or escape(ReadText(2)) is fine?
				switch (escape(readText(2).replace(/\s/g, ''))) {
					case "%3Ca" :
					case "%3Cd" :
					case "%3C%3F" :
					case "%u2229%u2557" :	// 0xEF,0xBB => UTF-8
						sCharset = "UTF-8";
						break;
					case "%A0%u25A0" :		// 0xFF,0xFE => Unicode
					case "%u25A0%A0" :		// 0xFE,0xFF => Unicode big endian
						sCharset = "Unicode";
						break;
					default :
						// 判断不出来就使用GBK，这样可以在大多数情况下正确处理中文
						sCharset = "GBK";	
				}
				close();
				open();
			}
			charset = sCharset;
			loadFromFile(filename);
			fileContent = new String(readText());
			fileContent.charset = sCharset;
			close();
		}
		return fileContent;
	}
	
	/**
	 * 将数据保存到磁盘上。可支持文本数据和二进制数据。
	 * @param 	{String} 	path 		文件路径。
	 * @param 	{String} 	data 		要写入的数据，可以是二进制对象。
	 * @param 	{Boolean} 	isBinary	是否为二进制数据。
	 * @param 	{Boolean} 	isMapPath	是否送入相对路径。True 的话把虚拟路径还原为真实的磁盘路径。
	 * @return 	{Boolean} 	True		表示操作成功。
	 */
	,write : function(path, data, isBinary, chartset){
		path = path.replace(/\\/g, '\\\\');
		
		l('deleting file:' + path);
		
		var 
		 fso	= new ActiveXObject("Scripting.FileSystemObject")
		,file		= fso.getFile(path);
		// 删除文件。
		file.Delete();
		
		fso = file = null;
	    with(new ActiveXObject("Adodb.Stream")){
	        type = isBinary ? 1 : 2;
	        if (!chartset && !isBinary){
				charset = "utf-8";
	        }
	        if (chartset){
				charset = "GB2312";
	        }
	        try {
				open();
				if(!isBinary){
					writeText(data);
				}else{
					write(data);
				} 
				saveToFile(path, 2);
				
				return true;
	        }catch(e){
				throw e;
	        } finally {
				close();
	        }
	    }
	    
	    return true;
	}
	
	/**
	 * 送入一个文件磁盘地址，使用ADODB.Stream组件下载。
	 * 文件大小有限制？ Need Huge Server RAM！！
	 * 隐藏下载地址及防盗代码。 防盗链 
	 * @param {String} filePath
	 * @return {Boolean} 是否传送成功。
	 */
	,downFile : function(data){
		var fileObj, fileSize;
			
		with(new ActiveXObject("Scripting.FileSystemObject")){
			fileObj = getFile(filePath);
			if(!fileObj){
				throw '目标文件不存在！';
			}
			fileSize = fileObj.size;
		}
		
	    with(new ActiveXObject("ADODB.Stream")){
	        open();
	        type = 1;
	        loadFromFile(filePath);
		    Response.addHeader("Content-DIsposItIon", "attachment; FIlename=" + F.Name);
		    Response.addHeader("Content-Length", IntFilelength);
	        Response.Buffer      = True
		    Response.CharSet     = "UTF-8";
		    Response.ContentType = "application/x-download";
    		Response.clear();
		    Response.binaryWrite(Read());
		    Response.flush();
		    Close();
	    }
	    return true;
	}
	
	,base64EncodeText: function (TextStr) {
		var xml_dom = new ActiveXObject("MSXML2.DOMDocument");
		var ado_stream = new ActiveXObject("ADODB.Stream");
		var tmpNode = xml_dom.createElement("tmpNode");
		
		tmpNode.dataType = "bin.base64";
		ado_stream.Charset = "gb2312";
		ado_stream.Type = 2;// 1=adTypeBinary 2=adTypeText
		if (ado_stream.state == 0) {// 0=adStateClosed 1=adStateOpen
			ado_stream.Open();
		}
		ado_stream.WriteText(TextStr);
		ado_stream.Position = 0;
		ado_stream.Type = 1;// 1=adTypeBinary 2=adTypeText
		tmpNode.nodeTypedValue = ado_stream.Read(-1);// -1=adReadAll
		ado_stream.Close();
		return tmpNode.text;
	}
};

$$.file.toString = $$.file.read_File_with_Chartset.delegate(null, 'UTF-8');

$$.db = {
	connect : function(cfg){
		var connectObj = new ActiveXObject("ADODB.CONNECTION"); // 数据库对象
		var 
		 dbType			// 链接数据库的类型，是 Access 呢？MySQL 呢？还是 SQLServer？
		,dbPath 		// 数据库文件的路径，如果通过磁盘访问的话。
		,connectStr;	// 链接数据库的字符串。
		
		if(cfg){
			dbType = cfg.dbType;
		}else if(!cfg && $$.cfg){
			dbType = $$.cfg.edk_dbType.toLowerCase();
		}else{
			dbType = 'access';	// 默认为MS Access数据库
		}
		
		switch(dbType){
			case 'access' :
				if(cfg && cfg.dbPath){
					dbPath = cfg.dbPath;
				}else if(!cfg && $$.cfg){
				    dbPath = $$.cfg.edk_isDebugging ? $$.cfg.edk_dbFilePath_Test : Server.mappath($$.cfg.edk_dbFilePath);
				}
		        connectStr = "DBQ={0};DefaultDir=;DRIVER={Microsoft Access Driver (*.mdb)};".format(dbPath);
			break;
			case 'sqlservver' :
				throw '尚未实现';
			case 'mysql' :
				// 使用driver模式不用有空格在连接字符串中
				connectStr = "DRIVER={mysql odbc 5.1 driver};SERVER={0};PORT={1};UID={2};PASSWORD={3};DATABASE={4};OPTION=3";
				connectStr = connectStr.format('localhost', 3306, 'root' ,'123', 'test');            		
			break;
			case 'sqlite' :
				dbPath = 'd:\\test.db';
				connectStr = "DRIVER={SQLite3 ODBC Driver};Database=" + dbPath;
			break;
			default:
				throw '非法数据库连接类型！';
		};
		connectObj.open(connectStr);
		
		return connectObj;
	}
};


function sql(extraParam, baseParam, baseUrl, tableName){
	var id = '';
	if(typeof extraParam == 'number'){ // 约定，如果第一个参数是 int，则表示此为 entry 的 id
		id = '/' + extraParam;
	}
    var url = '';
    url += baseUrl;
    url += tableName;
    url += id;
    url += '?';

    if(baseParam)
        for(var i in  baseParam)
            url += i + '=' + simpleJSON(baseParam[i]) + '&';

    if(extraParam && typeof extraParam != 'number')
        for(var i in  extraParam)
            url += i + '=' + simpleJSON(extraParam[i]) + '&';

    url = url.substr(0, url.length - 1);  
    // where 参数其值为 JSON，当中的字符串必须带引号，但 url 下的引号须要转义
    url = url.replace(/'|"/g, "%22"); 
    return url;
}

function simpleJSON(obj, isStrictJSON){
	if(typeof obj == 'string' || typeof obj == 'number' || typeof obj == 'boolean'){
		return obj;
	}

	var str = '{';
	var key, value, j = 0;
	for(var i in obj){
		key = isStrictJSON ? '"' + i + '"' : i;
		value = typeof obj[i] == 'string' ? '"' + obj[i] + '"' : obj[i];
		str += (j == 0 ? '' : ',') + key + ' : ' + value;
		j++;
	}

	return str + '}';
}

