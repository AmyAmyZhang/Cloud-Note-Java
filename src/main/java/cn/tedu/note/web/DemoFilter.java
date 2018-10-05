package cn.tedu.note.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class DemoFilter
 */
public class DemoFilter implements Filter {
	
	public DemoFilter() {
		//出生
	}
	public void init(FilterConfig fConfig) throws ServletException {	
		//庆贺出生
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//从请求中获取 请求的url
		HttpServletRequest req = (HttpServletRequest) request;
		String url = req.getRequestURI();
		//System.out.println("filter:" + url);
		//调用后续的WEB请求: *.html *.js *.servlet
		chain.doFilter(request, response);
	}

	public void destroy() {
		//我还会回来的！(销毁之前会执行)
	}

}
