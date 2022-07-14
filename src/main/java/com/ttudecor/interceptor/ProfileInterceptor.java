package com.ttudecor.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ProfileInterceptor implements HandlerInterceptor{
	
	@Autowired
	private HttpSession session;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if(session.getAttribute("fullname") != null) {
			
			return true;
		}else {
			String uri = request.getRequestURI();
			System.out.println("Login unsuccessful: " + uri);
			
			session.setAttribute("redirectUri", uri);
			response.sendRedirect("/login");
			return false;
		}

	}
}
