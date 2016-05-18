$$.upload={};
;(function(){
    /**
     * 转换为文本之后，其大小应与请求的二进制字节大小相等。否则被视为转换错误，代码中立刻抛出异常！
     * @private
     */
	function checkLen(POST_String){
        if(POST_String.length != Request.totalBytes){
            throw "网络或硬件错误 不能上传文件原因Size不匹配";	
        }else{
            return POST_String;
        }
    }
    
    function Adapter(cfg){
//    	var _propertySetter = function (){}// Object.util.propertySetter;
//    	
//        /**
//         * @property {BinStream} rawData 原始的二进制数据。
//         */
//        /**
//         * @property {BinStream} rawData_Stream 原始的二进制数据其容器。
//         */
//    	/**
//    	 * 返回原始的二进制数据。
//    	 * @param {BinStream}
//    	 * @return {BinStream}
//    	 */
//        this.setRawData		= _propertySetter('rawData', cfg.setRawData);
//        
//    	/**	
//    	 * @property {String} POST_String
//    	 */ 
//        /**
//         * 转换为字符串类型。该方法对生成的字符串进行检查。
//         * @param {String} POST_String 生成的表单字符串。
//         * @param {Number} totalBytes 表单域提交的大小。应该与字符串长度一致，否则将抛出一个异常。
//         * @return {String} POST_String
//         */
//        this.getPOST_String = _propertySetter('POST_String', cfg.getPOST_String.C(checkLen));
//    
//        /**
//         * 返回在HTTP头部的Content-type信息。
//         * @return {String}
//         */
//        this.getHttpContentType = cfg.getHttpContentType;
//        
//        /**
//         * 设置一容器收集二进制数据。
//         */
//        this.saveFile = cfg.saveFile;
    }

    
    var asp_Adapter = new Adapter({
    	 setRawData : function(){
    	 	var 
    	 		 rawData 	=  Request.binaryRead(Request.totalBytes)
           		,binHolder  = new ActiveXObject('adodb.stream');
           		
	        // 设置一容器收集二进制数据，Byte []存放在adodb.stream对象中。
			with(binHolder){
				type = 1;
				open();
				write(rawData);						
			}
			this.rawData_Stream = binHolder;
    	 	return rawData;
    	 }
        ,getPOST_String : function(){
            var POST_String;
                
            with(new ActiveXObject('adodb.stream')){
                type		= 2;
                open();
                writeText(this.setRawData());
                position	= 0;
                charset		= "us-ascii";
                position	= 2;
                POST_String = readText();
                close();
            }
            return POST_String;
        }
        ,getHttpContentType : function(){
        	var httpContentType = Request.ServerVariables("HTTP_CONTENT_TYPE")();
        	if(!httpContentType)throw "Err httpContentType!";
            return httpContentType;
        }
        
		// 将二进制流（Stream）保存到磁盘，形成文件。
        ,saveFile : function(file){
            var 
                 rawData_Stream = this.rawData_Stream
                ,fileBin
                ,filePath       = file.path;
                
            rawData_Stream.position = file.start;
            fileBin = rawData_Stream.read(file.size);
            
			return $$.fs.File.write(fileBin, filePath, true);
        }
    });
/*Public Static Member/ 
    $$.upload._loaded = true;
    

/*Private Static Method*/	
	/**
	 * 生成文件路径。如需要修改生成文件规则，可修改该函数。
	 * @private
	 * @param {fileObj}
	 * @return {fileObj}
	 */
    function getPath(fileObj){
        var fileName = Request.QueryString('uploadTo')() || '/upload';
        
        fileName += '/';
        fileName += new Date().valueOf();
        fileName += $$.fs.File.setMIME(fileObj.contentType) || "." + fileObj.fileName.split(".").pop();

        fileObj.path = Server.Mappath(fileName);
        
        return fileObj;
    }
    
	var split = /Content\-Disposition:\s+?form-data;\s+?name="(.*)";\s+?filename="(.*)"\r\nContent\-Type:\s+?(.*)\r\n\r\n/i;
	
/*Private Instance Method*/	
	/**
	 * @private
	 * @param {String} disposition 每一笔上传的档案。
	 * @param {String} POST_String
	 * @return {String}
	 */
	function fileParser(disposition, POST_String){
		var 
			// Parse的结果:Array
			 match
			// 档案的实体，开始index和实体的大小。
			,startIndex, bodySize
			,fileObj;
			
		if(split.test(disposition) && (match = disposition.match(split))){
			if (!match[2]) {
				return 'continue'; // @dep 有何作用？？？
			}			
	
			// 只要算出，与body临界的index即可
			startIndex = POST_String.indexOf(match[0]) + match[0].length;
			// body ＝ 总数 － 已匹配的head
			bodySize   = disposition.length - match.lastIndex;
			// 文件信息结构
			fileObj = {
				 start       : startIndex
				,formField	 : match[1]
				,fileName 	 : match[2]
				,contentType : match[3]
				,size        : bodySize
			};

			this.saveFile(getPath(fileObj));
			this.file.push(fileObj);
		}
	}
/*Public Instance Method*/ 
    $$.upload.prototype = {
        constructor: function(cfg){
            for(var i in asp_Adapter){
            	this[i] = asp_Adapter[i];
            }
            for(var i in cfg){
            	this[i] = cfg[i];
            }
            d();
            /**
             * @property {Array} file
             */
            this.file = [];
        }
        
        ,accpet : function(){
            this.setRawData();

            var 
                 splitToken = /.*(\-{27}[a-z0-9]+)/
                ,splitToken = this.getHttpContentType().replace(splitToken, "--$1")
                ,splitToken = new RegExp( '(\\r\\n)?' + splitToken + "(\-\-\\r\\n)?")
                ,splitToken = this.getPOST_String().split(splitToken);
                
            for(var i = 0, j = splitToken.length; i < j; i++){
            	fileParser.call(this, splitToken[i], this.getPOST_String());
            }

            this.rawData_Stream.close();
            
            delete this.rawData_Stream;
            
            return true;
        }
    };
    
    /**
	 * @class $$.fs.File
	 */
	$$.fs.File = function(){
		
		/**
		 * 根据文件的MIME（content-type）类型，返回文件的扩展名。
		 * @param  {String} contentType
		 * @return {String} null表示为不能识别该Content-Type。
		 */
		this.setMIME = function(contentType){
	        switch(contentType){
                case "application/msword" :
                    return ".doc";break;			
                case "application/pdf" :
                    return ".pdf";break;			
                case "application/vnd.ms-excel" :
                    return ".xls";break;			
                case "application/vnd.ms-powerpoint" :
                    return ".ppt";break;			
                case "application/x-javascript" :
                    return ".js";break;			
                case "application/x-shockwave-flash" :
                    return ".swf";break;			
                case "application/xhtml+xml" :
                    return ".xhtml";break;			
                case "application/zip" :
                    return ".zip";break;			
                case "application/msaccess" :
                    return ".mdb";break;			
                case "audio/midi" :
                    return ".mid";break;			
                case "audio/mpeg" :
                    return ".mp3";break;			
                case "audio/x-aiff" :
                    return ".aif";break;			
                case "audio/x-mpegurl" :
                    return ".m3u";break;			
                case "application/vnd.rn-realmedia" :
                case "audio/x-pn-realaudio" :
                    return ".rm";break;			
                case "audio/x-pn-realaudio-plugin" :
                    return ".rpm";break;			
                case "audio/x-realaudio" :
                    return ".ra";break;			
                case "audio/wav" :
                    return ".wav";break;			
                case "image/bmp" :
                    return ".bmp";break;			
                case "image/gif" :
                    return ".gif";break;		
                case "image/jpg" :
                case "image/jpeg" :
                case "image/pjpeg" :
                    return ".jpg";break;
                case "image/x-png":
                case "image/png" :
                    return ".png";break;			
                case "image/tiff" :
                    return ".tiff";break;			
                case "image/vnd.wap.wbmp" :
                    return ".wbmp";break;		
                case "text/css" :
                    return ".css";break;			
                case "text/html" :
                    return ".html";break;			
                case "text/plain" :
                    return ".txt";break;			
                case "text/richtext" :
                    return ".rtx";break;			
                case "text/rtf" :
                    return ".rtf";break;			
                case "text/xml" :
                    return ".xml";break;			
                case "video/mpeg" :
                    return ".mpeg";break;			
                case "audio/x-ms-wma" :
                    return ".wma";break;			
                case "video/quicktime" :
                    return ".mov";break;			
                case "video/vnd.mpegurl" :
                    return ".mxu";break;			
                case "video/x-msvideo" :
                    return ".avi";break;			
                case "video/x-sgi-movie" :
                    return ".movie";break;	
                case "application/octet-stream" :
                default :
                    return null; // 分不清什么类型，返回null。
            } 
        }
	}
})();