package com.example.Payment;

import com.example.Model.Item;
import com.example.Model.User;
import com.example.Repo.ItemRepository;
import com.example.Repo.PayPalOrderRepository;
import com.example.Repo.UserRepository;
import com.example.serviceImp.PayPalOrderService;
import com.example.serviceImp.ItemServiceImp;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.paypal.api.payments.Links;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
    public class PayPalController {
        @Autowired
        PayPalService payPalService;
        @Autowired
        ItemServiceImp itemService;

        @Autowired
        ItemRepository itemRepository;

        @Autowired
        private PayPalOrderRepository orderRepository;

        @Autowired
        private PayPalOrderService orderService;

        @Autowired
        private UserRepository userRepo;

        public static final String SUCCESS_URL = "pay/success";
        public static final String CANCEL_URL = "pay/cancel";

    @PostMapping("/pay")
    public String payment(@RequestParam("itemId") Long itemId, @RequestParam("price") Double price, @RequestParam("description") String description) {

        String currency = "EUR";
        String method = "paypal";
        String intent = "sale";

        try {
            Payment payment = payPalService.createPayment(price, currency, method,
                    intent, description, "http://localhost:8080/" + CANCEL_URL,
                    "http://localhost:8080/" + SUCCESS_URL + "?itemId=" + itemId);

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/PayPal";
    }
    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, @RequestParam("itemId") Long itemId) {
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepo.findByEmail(userDetails.getUsername());

            if (payment.getState().equals("approved")) {
                Item item = itemService.findById(itemId);
                long buyerId = user.getId();
                long sellerId = item.getUser().getId();
                item.setAvailable(false);
                orderService.saveOrder(payment, itemId, buyerId, sellerId);
                return "redirect:/payPalTransactions";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/PayPal";
    }

    @GetMapping("/payPalTransactions")
    public String getTransactions(Model model) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user= userRepo.findByEmail(userDetails.getUsername());
        List<PayPalOrder> itemsList =  orderService.getPayPalOrders(user.getId());
        model.addAttribute("userId", user.getId());
        model.addAttribute("listItems", itemsList);
        return "pTransactions";
    }

    @GetMapping("/changeTransactionStatusPP/{id}")
    public ModelAndView changePaypalStatus(@PathVariable("id") Long id, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user= userRepo.findByEmail(userDetails.getUsername());
        orderService.updateOrderStatus(id,user.getId());
        return new ModelAndView("redirect:" + "/payPalTransactions");
    }

}

