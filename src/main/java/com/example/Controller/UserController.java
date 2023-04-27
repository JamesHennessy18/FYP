package com.example.Controller;

import com.example.Model.*;
import com.example.Repo.*;
import com.example.Service.CustomUserDetails;
import com.example.serviceImp.CustomUserDetailsService;
import com.example.serviceImp.ItemServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

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

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private SearchHistoryRepository searchHistoryRepository;

	@Autowired
	private TransactionOrderRepository transactionOrderRepository;

	@Autowired
	private SellerRatingRepository sellerRatingRepository;

	@Autowired
	private PayPalOrderRepository payPalOrderRepository;
	
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
	public String registerResults(User user, Model model, HttpSession session) {

		User existingUser = repo.findByEmail(user.getEmail());
		if (existingUser != null) {
			model.addAttribute("emailExistsError", "Email already exists. Please use a different email.");
			return "registerHere";
		}

		Role customerRole = roleRepo.findByName("CUSTOMER");
		user.getRoles().add(customerRole);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		User savedUser = repo.save(user);

		session.setAttribute("userId", savedUser.getId());

		return "redirect:/addWallet";
	}
	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/home_page")
	public String viewHome(Model model,
						   @RequestParam(name = "sort", defaultValue = "test") String sort,
						   @RequestParam(name = "category", required = false) String category,
						   @Param("keyword") String keyword) {

		List<Item> itemsList = (List<Item>) itemServiceImp.itemsList(keyword);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		User user= repo.findByEmail(userDetails.getUsername());
		model.addAttribute("userId", user.getId());
		System.out.println(itemsList);

		if (keyword != null && !keyword.trim().isEmpty()) {
			SearchHistory searchHistory = new SearchHistory();
			searchHistory.setUser(user);
			searchHistory.setKeyword(keyword);
			searchHistory.setTimestamp(new Timestamp(System.currentTimeMillis()));
			searchHistoryRepository.save(searchHistory);
		}
		List<SearchHistory> searchHistoryList = searchHistoryRepository.findByUserIdOrderByTimestampDesc(user.getId());
		List<Item> recommendedItems = new ArrayList<>();
		for (SearchHistory searchHistory : searchHistoryList) {
			List<Item> itemsByKeyword = (List<Item>) itemServiceImp.itemsList(searchHistory.getKeyword());
			recommendedItems.addAll(itemsByKeyword);
		}
		recommendedItems = recommendedItems.stream().distinct().collect(Collectors.toList());

		if (recommendedItems.isEmpty()) {
			recommendedItems = itemsList;
		} else {

			LinkedHashSet<Item> combinedItems = new LinkedHashSet<>(recommendedItems);
			combinedItems.addAll(itemsList);
			recommendedItems = new ArrayList<>(combinedItems);
		}
		recommendedItems = recommendedItems.stream()
				.distinct()
				.filter(item -> item.isAvailable())
				.collect(Collectors.toList());

		if (sort.equals("lowest")) {
			recommendedItems.sort(Comparator.comparing(Item::getItemPrice));
		} else if (sort.equals("highest")) {
			recommendedItems.sort(Comparator.comparing(Item::getItemPrice).reversed());
		}
		if (category != null) {
			recommendedItems = recommendedItems.stream()
					.filter(item -> item.getCategory().equalsIgnoreCase(category))
					.collect(Collectors.toList());
		}

		List<Double> sellerRatings = new ArrayList<>();

		for (Item item : recommendedItems) {
			User seller = item.getUser();
			List<SellerRating> sellerRatingList = sellerRatingRepository.findBySeller(seller);
			double avgerageRating = 0;
			if (!sellerRatingList.isEmpty()) {
				double sumOfRatings = 0;
				for (SellerRating sellerRating : sellerRatingList) {
					sumOfRatings += sellerRating.getRating();
				}
				avgerageRating = sumOfRatings / sellerRatingList.size();
			}
			sellerRatings.add(avgerageRating);
		}
		//model.addAttribute("listItems", itemsList);
		model.addAttribute("sellerRatings", sellerRatings);
		model.addAttribute("recommendedItems", recommendedItems);
		model.addAttribute("currentIndex", 0);
		return "home_page";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin_home")
	public String adminHome(Model model) {
		List<Object[]> categorySales = transactionOrderRepository.findCategorySales();
		model.addAttribute("categorySales", categorySales);

		List<Object[]> statusCounts = transactionOrderRepository.findStatusCounts();
		model.addAttribute("statusCounts", statusCounts);

		return "admin_home";
	}

	@GetMapping("/myAccount")
	public String viewAccount(@AuthenticationPrincipal CustomUserDetails user, Model model) {
		User updatedUser = repo.findByEmail(user.getEmail());
		user = new CustomUserDetails(updatedUser);
		model.addAttribute("user", user);
		return "userInfo";
	}

	@PostMapping("/myAccount/update")
	public String updateAccount(User user, RedirectAttributes redirectAttributes, Principal principal){
		System.out.println("Updating user details...");
		// user = repo.findByEmail(principal.getName());
		User updatedUser = service.updateAccount(user);
		//System.out.println("username: " + updatedUser.getFirstName());


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

	@GetMapping("/trending")
	public String showTrending(Model model) {

		Long transactionTotalSales = transactionOrderRepository.findTotalSales();
		Long payPalTotalSales = payPalOrderRepository.findTotalSales();
		Long combinedTotalSales = transactionTotalSales + payPalTotalSales;
		List<Object[]> statusCounts = transactionOrderRepository.findStatusCounts();
		List<Object[]> categorySales = transactionOrderRepository.findCategorySales();
		List<Object[]> payPalCategorySales = payPalOrderRepository.findCategorySalesByPayPalOrder();
		List<Object[]> payPalStatusCounts = payPalOrderRepository.findStatusCounts();

		model.addAttribute("payPalStatusCounts", payPalStatusCounts);
		model.addAttribute("payPalCategorySales", payPalCategorySales);
		model.addAttribute("transactionTotalSales", transactionTotalSales);
		model.addAttribute("payPalTotalSales", payPalTotalSales);
		model.addAttribute("combinedTotalSales", combinedTotalSales);
		model.addAttribute("categorySales", categorySales);
		model.addAttribute("statusCounts", statusCounts);

		return "trending";
	}

	@GetMapping("/saleTrends")
	public String showSaleTrends(
			@RequestParam(value = "view", required = false) String view, Model model) {

		List<Item> lastFiveSoldItems;
		if (view == null || view.trim().isEmpty()) {
			lastFiveSoldItems = itemRepository.findLastFiveSoldItems();
		} else {
			lastFiveSoldItems = itemRepository.findLastFiveSoldItemsByItemName(view);
		}

		model.addAttribute("lastFiveSoldItems", lastFiveSoldItems);
		return "saleTrends";
	}
}
