package com.example.Controller;

import com.example.Model.Item;
import com.example.Model.User;
import com.example.Repo.*;
import com.example.Service.CryptoService;
import com.example.serviceImp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.example.Model.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
@Controller
public class PaymentController {

    @Autowired
    private TransactionServiceImpl transactionService;
    @Autowired
    TransactionOrderRepository transactionOrderRepository;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemServiceImp itemServiceImp;
    @Autowired
    private CryptoService cryptoService;

    @Autowired
    SellerRatingRepository sellerRatingRepository;

    @Autowired
    private PayPalOrderRepository payPalOrderRepository;

    @Autowired
    private PayPalOrderService payPalOrderService;
    @Autowired
    private BidServiceImp bidService;

    @GetMapping("/transactions")
    public String getTransactions(Model model) throws IOException {
        System.out.println("test");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user= userRepo.findByEmail(userDetails.getUsername());
        List<TransactionOrder> itemsList =  transactionService.getTransactions(user.getId());
        model.addAttribute("userId", user.getId());
        model.addAttribute("listItems", itemsList);
        return "transactions";
    }
//    @GetMapping("/PayPal")
//    public String viewCheckout(@RequestParam("item") Long itemId, @RequestParam(value = "bidPrice", required = false) Integer bidPrice, Model model) {
//        Item item = itemRepository.findByItemId(itemId);
//        System.out.println("item price :" + item.getItemPrice());
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) auth.getPrincipal();
//        User user= userRepo.findByEmail(userDetails.getUsername());
//        model.addAttribute("item", item);
//        model.addAttribute("buyerId", user.getId());
//        model.addAttribute("bid", new Bid());
//        if (bidPrice != null) {
//            item.setItemPrice(bidPrice);
//        }
//        return "Pay";
//    }
@GetMapping("/PayPal")
public String viewCheckout(@RequestParam("item") Long itemId, @RequestParam(value = "bidPrice", required = false) Integer bidPrice, Model model) {
    Item item = itemRepository.findByItemId(itemId);
    System.out.println("item price :" + item.getItemPrice());
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) auth.getPrincipal();
    User user= userRepo.findByEmail(userDetails.getUsername());
    int displayPrice = item.getItemPrice();
    if (bidPrice != null) {
        displayPrice = bidPrice;
    }
    model.addAttribute("item", item);
    model.addAttribute("buyerId", user.getId());
    model.addAttribute("bid", new Bid());
    model.addAttribute("displayPrice", displayPrice);
    return "Pay";
}

    @GetMapping("/paypalRec")
    public String viewRecPaypal(@RequestParam("item") Long itemId, @RequestParam(value = "bidPrice", required = false) Integer bidPrice, Model model){
        Item item = itemRepository.findByItemId(itemId);
        int displayPrice = item.getItemPrice();
        if (bidPrice != null) {
            displayPrice = bidPrice;
        }
        model.addAttribute("item", item);
        model.addAttribute("displayPrice", displayPrice);
        return "PPRec";
    }

    @GetMapping("/changeTransactionStatus/{id}")
    public ModelAndView changeOrderStatus(@PathVariable("id") Long id, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user= userRepo.findByEmail(userDetails.getUsername());
        transactionService.updateTransactionStatus(id,user.getId());
        return new ModelAndView("redirect:" + "/transactions");
    }

    @PostMapping("/rateSeller/{sellerId}/{rating}")
    @ResponseBody
    public ResponseEntity<String> rateSeller(@PathVariable("sellerId") long sellerId, @PathVariable("rating") int rating, Principal principal) {
        User buyer = userRepo.findByEmail(principal.getName());
        if (buyer == null) {
            throw new IllegalArgumentException("User not found");
        }

        User seller = userRepo.findById(sellerId).orElse(null);
        if (seller == null) {
            throw new IllegalArgumentException("Seller not found");
        }

        SellerRating existingRating = sellerRatingRepository.findBySellerAndBuyer(seller, buyer).orElse(null);

        if (existingRating == null) {
            SellerRating newRating = new SellerRating();
            newRating.setBuyer(buyer);
            newRating.setSeller(seller);
            newRating.setRating(rating);
            sellerRatingRepository.save(newRating);
        } else {
            existingRating.setRating(rating);
            sellerRatingRepository.save(existingRating);
        }

        return ResponseEntity.ok("Seller rating submitted");
    }

    @PostMapping("/submitBid")
    public String submitBid(@RequestParam("itemId") Long itemId, @RequestParam("amountOffered") int amountOffered, Principal principal, RedirectAttributes redirectAttributes) {
        User buyer = userRepo.findByEmail(principal.getName());
        Item item = itemServiceImp.findById(itemId);
        User seller = item.getUser();

        Bid bid = new Bid();
        bid.setItemName(item.getItemName());
        bid.setBuyer(buyer);
        bid.setSeller(seller);
        bid.setItem(item);
        bid.setAmountOffered(amountOffered);
        bid.setPhotosImagePaths(item.getPhotosImagePaths()[0]);
        bid.setStatus("received");

        itemServiceImp.saveBid(bid);
        redirectAttributes.addFlashAttribute("message", "Bid submitted. Check back here for updates.");
        return "redirect:/bidding";
    }

    @GetMapping("/bidding")
    public String viewBidding(Principal principal, Model model) {
        User user = userRepo.findByEmail(principal.getName());
        List<Bid> receivedBids = bidService.findBidsForSeller(user);
        List<Bid> placedBids = bidService.findBidsByBuyer(user);

        model.addAttribute("receivedBids", receivedBids);
        model.addAttribute("placedBids", placedBids);

        return "bidding";
    }

    @PostMapping("/acceptBid")
    public String acceptBid(@RequestParam("bidId") Long bidId) {
        bidService.acceptBid(bidId);
        return "redirect:/bidding";
    }

    @PostMapping("/declineBid")
    public String declineBid(@RequestParam("bidId") Long bidId) {
        bidService.declineBid(bidId);
        return "redirect:/bidding";
    }
}
