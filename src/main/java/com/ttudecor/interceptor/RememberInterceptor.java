package com.ttudecor.interceptor;

import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ttudecor.entity.User;
import com.ttudecor.service.UserService;
import com.ttudecor.utils.EncryptionUtils;

@Component
public class RememberInterceptor implements HandlerInterceptor{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpSession session;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if(session.getAttribute("fullname") != null) 
			return true;
		else {
			Cookie[] cookies = request.getCookies();
			
			for(Cookie c : cookies) {
				if(c.getName().equals("uid")) {
					String uid = EncryptionUtils.dencrypt("LoginID", c.getValue());
					
					try {
						int userId = Integer.parseInt(uid);
						
						Optional<User> opt = userService.findById(userId);
						User user = new User();
						if(opt != null) user = opt.get();
						
						session.setAttribute("fullname", user.getFullname());
						session.setAttribute("userId", user.getId());
						if(user.isIsadmin()) session.setAttribute("admin", true);
						
						return true;
					} catch (Exception e) {
					}
				}
			}
		}
		
		return true;

	}

}
