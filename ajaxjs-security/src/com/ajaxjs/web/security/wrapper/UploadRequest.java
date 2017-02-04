package com.ajaxjs.web.security.wrapper;

import java.io.IOException;
import java.util.ArrayList;
/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;

import com.ajaxjs.web.security.ConfigLoader;
import com.ajaxjs.web.security.ListControl;

/**
 * 文件上传安全过滤。上传文件后缀名符合白名单则允许上传。
 * @author Frank
 *
 */
public class UploadRequest extends HttpServletRequestWrapper {
	public ListControl delegate = new ListControl();
	
	public UploadRequest(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 
	 */
	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		Collection<Part> parts = super.getParts();

		if (parts == null || parts.isEmpty() || ConfigLoader.whitefilePostFixList == null
				|| ConfigLoader.whitefilePostFixList.isEmpty()) {
			return parts;
		}

		List<Part> resParts = new ArrayList<>();
		for (Part part : parts) {
			for (String extension : ConfigLoader.whitefilePostFixList) {
				if (part.getName().toUpperCase().endsWith(extension))
					resParts.add(part);

			}
		}

		return resParts;
	}

	/**
	 * 
	 */
	@Override
	public Part getPart(String name) throws IOException, ServletException {
		Part part = super.getPart(name);

		if (ConfigLoader.whitefilePostFixList.isEmpty()) {
			return part;
		}

		// String value = part.getHeader("content-disposition");
		// String filename = value.substring(value.lastIndexOf("=") + 2,
		// value.length() - 1);
		String filename = part.getName();

		for (String extension : ConfigLoader.whitefilePostFixList) {
			if (filename.toUpperCase().endsWith(extension.toUpperCase()))
				return part;
		}

		return null;
	}
}