package com.ttudecor.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class StringFormatUtils {
	public String removeMarks(String value) {
	      try {
	            String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
	            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	            return pattern.matcher(temp).replaceAll("");
	     } catch (Exception ex) {
	            ex.printStackTrace();
	      }
	      return null;
	}
	
	public String convertToUrlFomart(String value) {
	      try {
	            String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
	            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	            return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll(" ", "-").replaceAll("đ", "d");
	      } catch (Exception ex) {
	            ex.printStackTrace();
	      }
	      return null;
	}
}
