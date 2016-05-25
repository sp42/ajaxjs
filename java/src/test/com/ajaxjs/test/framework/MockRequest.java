package com.ajaxjs.test.framework;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.ajaxjs.json.Json;
import com.ajaxjs.util.map.MapHelper;

public class MockRequest extends WebBaseInit {
    class StubServletOutputStream extends ServletInputStream {  
        public ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        public void write(int i) throws IOException {  
            baos.write(i);  
        }  
        public String getContent() {  
            return baos.toString();  
        }
		@Override
		public int read() throws IOException {
			// TODO Auto-generated method stub
			return 0;
		}  
    } 
    
  

    /**
     * 模拟表单请求
     * @param request
     * @param formBody
     * @param isByGetParams 是否通过 request.getParameter 返回值，而不是走表单流的方式
     * @return
     * @throws IOException
     */
    public HttpServletRequest initRequest(HttpServletRequest request, Map<String, String> formBody, boolean isByGetParams) throws IOException{
    	if(isByGetParams){
    		for(String key : formBody.keySet()){
    			when(request.getParameter(key)).thenReturn(formBody.get(key));
    		}
    	}else{
    		String form = MapHelper.join(formBody, "&");
    		
    		final ByteArrayInputStream is = new ByteArrayInputStream(form.getBytes());
    		when(request.getInputStream()).thenReturn(new ServletInputStream() {
    			@Override
    			public int read() throws IOException {
    				return is.read();
    			}
    		}); 
    	}
    	
    	return request;
    }
    
	/**
	 * 初始化请求对象
	 * @return
	 * @throws IOException 
	 */
	public HttpServletRequest initRequest(String entry){
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getPathInfo()).thenReturn("/new_test/service/" + entry);
		when(request.getRequestURI()).thenReturn("/new_test/service/" + entry);
		when(request.getContextPath()).thenReturn("/new_test");
		
//		when(request.getSession()).thenReturn("/zjtv");
		when(request.getMethod()).thenReturn("GET");
		// 设置参数
//		when(request.getParameter("a")).thenReturn("aaa");

		
		final Map<String, Object> hash = new HashMap<String, Object>();
		Answer<String> aswser = new Answer<String>() {  
		    public String answer(InvocationOnMock invocation) {  
		        Object[] args = invocation.getArguments();  
		        Object obj = hash.get(args[0].toString());
		        
		        return obj == null ? null : obj.toString();  
		    }  
		};
		
		when(request.getAttribute("isRawOutput")).thenReturn(true);  
		when(request.getAttribute("errMsg")).thenAnswer(aswser);  
		when(request.getAttribute("output")).thenAnswer(aswser);  
		when(request.getAttribute("msg")).thenAnswer(aswser);  
//		doThrow(new Exception()).when(request).setAttribute(anyString(), anyString());
		
		doAnswer(new Answer<Object>() {
	        public Object answer(InvocationOnMock invocation) {
	            Object[] args = invocation.getArguments();
	            // Object mock = invocation.getMock();  
	            // 测试终端的模拟器接收到数据
//	             System.out.println("jojo:"+args[1]);
	            hash.put(args[0].toString(), args[1]);
	            return "called with arguments: " + args;
	        }
	    }).when(request).setAttribute(anyString(), anyString());
		
		return request;
	}
	
	// 不能返回数组返回一个对象吧
	public class response_writer{
		public HttpServletResponse response;
		public StringWriter writer;
	}
	public response_writer initResponse() throws IOException{
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter writer = new StringWriter();
        
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        
        response_writer r_w = new response_writer();
        r_w.response = response;
        r_w.writer = writer;
        
        return r_w;
	}
	
	/**
	 * 进行请求
	 * 在请求之前，你可以设定请求的参数
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public String doRequest(HttpServletRequest request) throws IOException, ServletException{
		response_writer r_w = initResponse();
//		
//		EntryController news = new EntryController();
//        
//		news.init(initServletConfig());
//		
//		FilterChain filterChain = mock(FilterChain.class);
////		BaseFilter bf = new BaseFilter();
////		bf.init(initFilterConfig(news.getServletContext()));
////		bf.doFilter(request, r_w.response, filterChain);
//		
//    	RequestHelper reqHelper   = new RequestHelper(request);
//		ResponseHelper respHelper = new ResponseHelper(r_w.response);
//		
//		news.doGet(reqHelper, respHelper);
//		
		String output = r_w.writer.toString();
		System.out.println("接口返回  ：" + output);
		return output;
	}


	public Map<String, Object> shouldbe_json_return(String js_code){
		return Json.callExpect_Map(js_code);
	}
	public Map<String, Object>[] shouldbe_jsonArray_return(String js_code){
		return Json.callExpect_MapArray(js_code);
	}
	
	public boolean shouldbe_hasRecord(Map<String, Object> json){
		boolean hasRecord = false;
		
//		System.out.println(json.get("total"));
		int total = (int)json.get("total");
		System.out.println("获取记录数：" + total);
		hasRecord = total > 0;
		
		return hasRecord;
	}
}
