<%@page pageEncoding="UTF-8" import="com.ajaxjs.net.upload.*"%>  
<%  
    UploadRequest ur = new UploadRequest();// 创建请求信息，所有参数都在这儿设置  
    ur.setRequest(request); //一定要传入 request  
    ur.setFileOverwrite(true);// 相同文件名是否覆盖？true=允许覆盖  
  
    Upload upload = new Upload();// 上传器  
  
    try {  
        upload.upload(ur);  
    } catch (UploadException e) {  
        response.getWriter().println(e.toString());  
    }  
  
    if (ur.isOk()) // 上传成功  
        response.getWriter().println("上传成功：" + ur.getUploaded_save_fileName());  
    else  
        response.getWriter().println("上传失败！");  
%> 