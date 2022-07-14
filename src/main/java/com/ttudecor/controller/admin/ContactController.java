package com.ttudecor.controller.admin;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ttudecor.entity.Feedback;
import com.ttudecor.repository.FeedbackRepository;

@Controller
public class ContactController {
	
	@Autowired
	private FeedbackRepository feedbackRepository;
	
	@GetMapping("/contact")
	public String contact(Model model) {
		
		model.addAttribute("contactPage", true);
		return "shop/contact";
	}
	
	@PostMapping("/contact")
	public String contact(Model model,
			@RequestParam String fullname,
			@RequestParam String email,
			@RequestParam String phoneNumber,
			@RequestParam String message) {
		
		Feedback fb = new Feedback();
		fb.setFullname(fullname);
		fb.setEmail(email);
		fb.setPhoneNumber(phoneNumber);
		fb.setMessage(message);
		fb.setCreatedDate(new Date());
		
		feedbackRepository.save(fb);
		
		model.addAttribute("message", "Cảm ơn bạn đã để lại lời nhắn, chúng tôi sẽ liên hệ lại sau!");
		
		model.addAttribute("contact", true);
		return "shop/contact";
	}
	
	@GetMapping("/ttu-admin/feedback")
	public String feedback(Model model) {
		
		List<Feedback> list = feedbackRepository.findAll();
		
		model.addAttribute("feedbacks", list);
		model.addAttribute("feedbackPage", true);
		return "admin/list-feedbacks";
	}
	
	@GetMapping("/ttu-admin/feedback/delete/{id}")
	public String delete(Model model, @PathVariable("id") int id) {
		
		Feedback feedback = new Feedback();
		Optional<Feedback> opt = feedbackRepository.findById(id);
		if(opt != null) {
			feedback = opt.get();
			feedbackRepository.delete(feedback);
		}
		
		return "redirect:/ttu-admin/feedback";
	}
}
