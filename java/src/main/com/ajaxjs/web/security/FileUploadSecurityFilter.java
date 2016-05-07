package com.ajaxjs.web.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.ajaxjs.util.Util;



/**
 * 文件上传后缀白名单验证,需要配置whitefilePostFixList参数；
 * @author weijian.zhongwj
 * 
 */
public class FileUploadSecurityFilter {

	public void doFilterInvoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request = new UploadFileExtensionFilterHttpServletRequest(request);
	}

	public class UploadFileExtensionFilterHttpServletRequest extends HttpServletRequestWrapper {
		public UploadFileExtensionFilterHttpServletRequest(HttpServletRequest request) {
			super(request);
		}

		@Override
		public Collection<Part> getParts() throws IOException, ServletException {
			Collection<Part> parts = super.getParts();
			if (!Util.isNotNull(parts)) return parts;
			
			List<Part> resParts = new ArrayList<>();
			for (Part part : parts) {
				for (String extension : SecurityFilter.whitefilePostFixList) {
					if (part.getName().toUpperCase().endsWith(extension))
						resParts.add(part);
				}
			}
			return resParts;
		}
		
		/**
		 * 如果后缀名符合白名单则允许上传
		 */
		@Override
		public Part getPart(String name) throws IOException, ServletException {
			Part part = super.getPart(name);
			
			for (String extension : SecurityFilter.whitefilePostFixList) {
				if (part.getName().toUpperCase().endsWith(extension)) {
					return part;
				}
			}
			return null;
		}
	}

}
