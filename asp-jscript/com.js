var $$ = $$ || {};
$$.MS_component = {
	/**
	 * 返回XHR对象
	 * @return {MSXML.ServerXMLHTTP}
	 */
	xhr : function (){
		var http;
		var activeX = ['MSXML2.ServerXMLHTTP.5.0', 'MSXML2.ServerXMLHTTP.3.0', 'MSXML2.ServerXMLHTTP', 'MSXML2.XMLHTTP.3.0', 'MSXML2.XMLHTTP', 'Microsoft.XMLHTTP'];
		for (var i = 0, j = activeX.length; i < j; ++i) {
			try {
				return new ActiveXObject(activeX[i]);
			} catch (e){}
		}
	}
	
	/**
	 * 不显示UI，以后台执行的方式运行程序。
	 * @param	{String} cmd
	 * @return	{Number}
	 */
	,quietRun : function (cmd){
	    return wshObj.run(cmd, 0/*  不显示GUI，后台运行 */, false /*不等待程序，也就是执行之后就好*/);
	}
	
	/**
	 * 目录选择器。
	 * @param 	{String}			str
	 * @return 	{Shell.Application}
	 */
	,showFolderPickuper : function (str){
	    var objFolder = new ActiveXObject("Shell.Application").BrowseForFolder(
	         0
	        ,str || "请选择一个目录。"
	        ,0
	        ,0x11
	    );
	    return objFolder ? objFolder.Self.Path : '';
	}
	
	/**
	 * 在脚本中创建浏览器窗体 适合WSC使用。
	 */
	,createBrowser : function (){
	    with(new ActiveXObject("InternetExplorer.Application")){
	        visible		=	false;
	        addressBar	=	false;
	        menuBar		=	false;
	        statusBar	=	false;
	        toolBar		=	false;
	        resizable	=	true;
	        left		=	300;
	        top			=	200;
	        width		=	300;
	        height		=	240;
	        silent		=	true;
	        navigate("http://localhost");
	        /*
	        Document.title="VBScript 创建的窗口";
	        Document.body.bgColor="#ECE9D8";
	        Document.body.style.overflow="hidden";
	        Document.body.style.borderWidth="0";
	        Document.body.innerHtml="hi  ";
	        */
	        visible		=	true;
	    }
	}
};