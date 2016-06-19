/*
 * Copyright 2009 Pavel Jbanov
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ajaxjs.mvc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;


public abstract class LiteMvcFilter implements Filter {
    private static Map<Pattern, ActionAndView> bindingsMap = new HashMap<>();
    
    private static Map<String, Action> globalResults = new HashMap<>();

    private static Map<Class<? extends Throwable>, String> exceptionsMap =  new LinkedHashMap<>();
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,  FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        RequestHelper.setHttpServletRequest(request);
        RequestHelper.setHttpServletResponse(response);

        ActionAndView binding = null;
        Matcher matcher = null;
        String servletPath = request.getServletPath();
        
        for (Pattern p : bindingsMap.keySet()) {
        	matcher = p.matcher(servletPath);
        	if (matcher.matches()) {
        		binding = bindingsMap.get(p);
        		break;
        	}
        }
        
        try {
            if (binding != null) {
                Object controller = createObject(binding.getHandlerClass());
                
                if (tryToExecuteMethod(request, response, matcher, binding, controller)) 
                    return;
            }
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            RequestHelper.clean();
        }
        
        chain.doFilter(req, resp);
    }
    

	@Override
	public void destroy() {}

    private boolean tryToExecuteMethod(HttpServletRequest request, HttpServletResponse response, Matcher matcher, ActionAndView binding, Object controller)  throws Exception {
        Method[] methods = binding.getHandlerClass().getMethods();
        
        for (Method method : methods) {
            if (isMappedMethod(request, method, binding)) {
                Class<?>[] parmTypes = method.getParameterTypes();
                
                int matchCount = 1;
                ArrayList<Object> args = new ArrayList<>();
                
                for (Class<?> clazz : parmTypes) {
                    if (clazz.equals(HttpServletRequest.class)) {
                        args.add(request);
                    }
                    if (clazz.equals(HttpServletResponse.class)) {
                        args.add(response);
                    }
                    if (clazz.equals(String.class)) {
                        args.add(matcher.group(matchCount));
                        matchCount++;
                    }
                }
                
                for (Object oParmName : request.getParameterMap().keySet()) {
                    String parmName = (String) oParmName;
                    if (PropertyUtils.isWriteable(controller, parmName)) {
                        BeanUtils.setProperty(controller, parmName, request.getParameter(parmName));
                    }
                }
                
                boolean isError = false;
                String result = null; 
                
                try {
                    result = (String) method.invoke(controller, args.toArray());
                } catch (Exception e) {
                    Throwable cause = e;
                    
                    if (e instanceof InvocationTargetException) {
                        InvocationTargetException ite = (InvocationTargetException) e;
                        cause = ite.getCause();
                    }
                    
                    if (exceptionsMap.containsKey(cause.getClass())) {
                        result = exceptionsMap.get(cause.getClass());
                        isError = true;
                    } else {
                        throw e;
                    }
                }
                
                if (result == null) 
                    return true;
                
                Action action = null; 
                
                if (!isError)  // we only check global results in case of an error.
                    action = binding.getAction(result);
                
                if (action == null) 
                    action = globalResults.get(result);
                
                if (action == null) 
                    throw new RuntimeException("unmapped" + (isError ? " global" : "") + " result: " + result);
                
                if (action instanceof DispatcherAction) {
                    request.setAttribute("handler", controller);
                    request.getRequestDispatcher(((DispatcherAction) action).getLocation()).forward(request, response);
                    
                    return true;
                }
                
                if (!customActionProcessor(binding, request, response, action)) 
                    throw new RuntimeException("unkown action type: " + action.getClass().getName());
                
                return true;
            }
        }
        
        return false;
    }

    private boolean isMappedMethod(HttpServletRequest request, Method method, ActionAndView binding) {
        if (binding.getMethodName() == null) {
            return method.getName().equals(request.getMethod().toLowerCase());
        } else {
            return method.getName().equals(binding.getMethodName());
        }
    }
    
    private Object createObject(Class<?> clazz) throws Exception {
        return clazz.newInstance();
    }
    
    /**
     * Map a regex to a handler class. Will execute method on the handler object
     * that matches the HTTP reqeust method. Ex.
     *  if HTTP GET:   handlerObj.get(...)
     *  if HTTP POST:  handlerObj.post(...)
     *  etc.
     * 
     * @param regex
     * @param handler
     * @return the binding object builder
     */
    protected final ActionAndView map(String regex, Class<?> handler) {
        ActionAndView binding = new ActionAndView(regex, handler);
        bindingsMap.put(binding.getPattern(), binding);
       
        return binding;
    }
    
    /**
     * Map a regex to a handler class and methodName. Will execute methodName on 
     * the handler object. Ex.
     *  if HTTP *:   handlerObj.methodName(...)
     * 
     * @param regex
     * @param handler
     * @return the binding object builder
     */
    protected final ActionAndView map(String regex, Class<?> handler, String methodName) {
        ActionAndView binding = new ActionAndView(regex, handler, methodName);
        bindingsMap.put(binding.getPattern(), binding);
        
        return binding;
    }
    
    protected void mapException(Class<? extends Throwable> ex, String globalResult) {
        exceptionsMap.put(ex, globalResult);
    }
 
  
    protected void globalDispatchResult(String result, String location) {
        globalResults.put(result, new DispatcherAction(location));
    }
    
    protected void globalResult(String result, Action action) {
        globalResults.put(result, action);
    }
    
    public boolean customActionProcessor(ActionAndView binding, HttpServletRequest request, HttpServletResponse response, Action action) {
        return false;
    }
}
