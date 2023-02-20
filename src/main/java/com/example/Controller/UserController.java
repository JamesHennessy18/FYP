package com.example.Controller;

import com.example.Model.Item;
import com.example.Model.User;
import com.example.Repo.ItemRepository;
import com.example.Repo.UserRepository;
import com.example.Service.CustomUserDetails;
import com.example.serviceImp.CustomUserDetailsService;
import com.example.serviceImp.ItemServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
	@Autowired
	private UserRepository repo;
	@Autowired
	private CustomUserDetailsService service;

	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private ItemServiceImp itemServiceImp;

	
	@GetMapping("/index")
	public String viewHomePage() {
		return "index";
	}
	
	@GetMapping("/register")
	public String showSignUp(Model model) {
		model.addAttribute("user", new User());
		return "registerHere";
	}
	
	@PostMapping("/process_register")
	public String registerResults(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		repo.save(user);

		return "register_success";
	}
	
	@GetMapping("/home_page")
	public String viewHome(Model model, @Param("keyword") String keyword) {
		List<Item> itemsList = (List<Item>) itemServiceImp.itemsList(keyword);
		System.out.println(itemsList);
		model.addAttribute("listItems", itemsList);
		model.addAttribute("currentIndex", 0);
		return "home_page";
	}

	@GetMapping("/myAccount")
	public String viewAccount(@AuthenticationPrincipal CustomUserDetails user, Model model) {

		model.addAttribute("user", user);
		return "userInfo";
	}

	@PostMapping("/myAccount/update")
	public String updateAccount(User user, RedirectAttributes redirectAttributes, Principal principal){
		System.out.println("Updating user details...");
		 user = repo.findByEmail(principal.getName());
		User updatedUser = service.updateAccount(user);

		if (updatedUser == null) {
			System.out.println("Error updating user details.");
		} else {
			redirectAttributes.addFlashAttribute("message", "Details Updated!");
			return "redirect:/myAccount";
		}

		return "redirect:/myAccount";
	}

	@GetMapping("/crypto")
	public String viewCrypto() {
		return "cryptoMarket";
	}
}
