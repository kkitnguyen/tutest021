package com.ttudecor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ttudecor.interceptor.AdminInterceptor;
import com.ttudecor.interceptor.ProfileInterceptor;
import com.ttudecor.interceptor.RememberInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
	
	@Autowired
	private AdminInterceptor adminInterceptor;
	
	@Autowired
	private ProfileInterceptor profileInterceptor;
	
	@Autowired
	private RememberInterceptor rememberInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(rememberInterceptor)
			.excludePathPatterns("/images/**", "/shop-assets/**", "/assets/**");
		
		registry.addInterceptor(adminInterceptor).addPathPatterns("/ttu-admin/**");
		
		registry.addInterceptor(profileInterceptor).addPathPatterns("/profile/**");
	}
	
}
