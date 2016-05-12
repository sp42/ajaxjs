package com.ajaxjs.util.pagecache;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class PageCacheHttpServletResponse extends HttpServletResponseWrapper{
	private StringBuilder sb = new StringBuilder();	
	
	public PageCacheHttpServletResponse(HttpServletResponse response) {
		super(response);
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return new BufferedServletWriter(super.getWriter(), sb);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return new BufferedServletOutputStream(super.getOutputStream(), sb);
	}
	
	public String getPageContent(){
		return sb.toString();
	}
	
	private static class BufferedServletOutputStream extends ServletOutputStream {
		private ServletOutputStream out;
		private StringBuilder sb;

		public BufferedServletOutputStream(ServletOutputStream out, StringBuilder sb) {
			this.out = out;
			this.sb = sb;
		}

		@Override
		public void write(int b) throws IOException {
			this.out.write(b);
			sb.append(b);
		}
	}
    private static class BufferedServletWriter extends PrintWriter {
		private StringBuilder sb;

		public BufferedServletWriter(PrintWriter pw, StringBuilder sb) {
			super(pw);
			this.sb = sb;
		}

		@Override
		public void write(int c) {
			super.write(c);
			sb.append(c);
		}

		@Override
		public void write(char[] buf, int off, int len) {
			super.write(buf, off, len);
			sb.append(new String(buf).substring(off, len - off));
		}

		@Override
		public void write(String s, int off, int len) {
			super.write(s, off, len);
			sb.append(s.substring(off, len - off));
		}
    }
}
