package com.example.Controller;

import com.example.Model.Item;
import com.example.Model.TransactionOrder;
import com.example.Model.User;
import com.example.Model.Wallet;
import com.example.Repo.ItemRepository;
import com.example.Repo.TransactionOrderRepository;
import com.example.Repo.UserRepository;
import com.example.Repo.WalletRepository;
import com.example.Service.CustomUserDetails;
import com.example.serviceImp.CustomUserDetailsService;
import com.example.serviceImp.ItemServiceImp;
import com.example.serviceImp.TransactionServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ItemServiceImp itemServiceImp;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionOrderRepository transactionService;
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }

    @GetMapping("/adminProd")
    public String prod(Model model, @Param("keyword") String keyword) {
        List<Item> itemsList = (List<Item>) itemServiceImp.itemsList(keyword);
        model.addAttribute("listItems", itemsList);
        return "adminProd";
    }

    @GetMapping("/adminProd/delete/{itemId}")
    public String deleteItem(@PathVariable("itemId") Long itemId) {
        itemServiceImp.delete(itemId);
        return "redirect:/adminProd";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userDetailsService.delete(id);
        return "redirect:/users";
    }

    @GetMapping("/adminInfo")
    public String viewAccount(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        User updatedUser = userRepo.findByEmail(user.getEmail());
        user = new CustomUserDetails(updatedUser);
        model.addAttribute("user", user);
        return "adminInfo";
    }

    @PostMapping("/adminInfo/update")
    public String updateAccount(User user, RedirectAttributes redirectAttributes, Principal principal){
        System.out.println("Updating user details...");
        // user = repo.findByEmail(principal.getName());
        User updatedUser = userDetailsService.updateAccount(user);
        //System.out.println("username: " + updatedUser.getFirstName());


        if (updatedUser == null) {
            System.out.println("Error updating user details.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Details Updated!");
            return "redirect:/adminInfo";
        }

        return "redirect:/adminInfo";
    }

    @GetMapping("/viewOrders")
    public String getOrders(Model model) throws IOException {
        List<TransactionOrder> itemsList = transactionService.findAll();
        ObjectMapper objectMapper = new ObjectMapper();

        Map<Long, String> sellerWallets = new HashMap<>();
        for (TransactionOrder item : itemsList) {
            Wallet sellerWallet = walletRepository.findByUser(item.getSeller());
            try {
                sellerWallets.put(item.getTransactionId(), objectMapper.writeValueAsString(sellerWallet));
            } catch (JsonProcessingException e) {
                sellerWallets.put(item.getTransactionId(), null);
            }
        }

        model.addAttribute("listItems", itemsList);
        model.addAttribute("sellerWallets", sellerWallets);
        return "adminOrders";
    }
}
