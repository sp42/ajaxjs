<!--#include virtual="/edk/ServerScript/page/Loader.inc" -->
<%
    require($$.upload);
    
    upload = new $$.upload;
    
	var 
		 resp_Script = '<scri' + 'pt>alert("上传成功！");parent.uploadSuccessful_hander({0});</script>'
		//,resp_Script = resp_Script.format(new $$.JSON().toJSON(files));
    if(upload.accpet()){
        Response.write("上传成功！");
    }
%>