package com.example.Controller;

import com.example.Model.Item;
import com.example.Model.User;
import com.example.Model.Wallet;
import com.example.Repo.UserRepository;
import com.example.Repo.WalletRepository;
import com.example.serviceImp.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class WalletController {

    @Autowired
    WalletRepository walletRepository;
    @Autowired
    WalletService walletService;
    @Autowired
    UserRepository userRepository;
    @GetMapping("/addWallet")
    public String addWallet(Model model) {
        model.addAttribute("wallet", new Wallet());
        return "AddWalletInfo";
    }

    @PostMapping("/process_wallet")
    public String processWallet(Wallet wallet, @RequestParam("userId") long userId) {
        User user = userRepository.findById(userId).orElse(null);
            wallet.setUser(user);

            walletRepository.save(wallet);
            return "register_success";

    }

    @GetMapping("/viewWallet")
    public String viewWallet(Model model, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
       Wallet wallet = walletRepository.findByUser(user);

        model.addAttribute("wallet", wallet);
        return "updateWallet";
    }

    @PostMapping("/updateWallet")
    public String updateWallet(@ModelAttribute Wallet wallet, Principal principal, RedirectAttributes redirectAttributes) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        Wallet existingWallet = walletRepository.findByUser(user);

        existingWallet.setPaypalEmail(wallet.getPaypalEmail());
        existingWallet.setCryptoCurrency(wallet.getCryptoCurrency());
        existingWallet.setCryptoAddress(wallet.getCryptoAddress());

        walletRepository.save(existingWallet);
        redirectAttributes.addFlashAttribute("message", "Your wallet has been updated.");

        return "redirect:/viewWallet";
    }

}
