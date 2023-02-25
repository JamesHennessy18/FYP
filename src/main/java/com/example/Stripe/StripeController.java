package com.example.Stripe;

import com.example.Model.Item;
import com.example.Model.User;
import com.example.Repo.ItemRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class StripeController {

  @Autowired
    private ItemRepository itemRepository;

    //@Value("${stripe.publicKey}")
    private String stripePublicKey;
    @GetMapping("/Card")
    public String viewHomePage() {
        return "PayByStripe";
    }
   /* @PostMapping("/Stripe")
    public String stripeCharge(@RequestParam("itemId") User user,
                               HttpServletRequest request,
                               Model model) throws StripeException {

        Stripe.apiKey = "sk_test_51MNF6oGOhNZwZyuGMbdnmVzG9lwgqnReZ5peBGyeQ6SvJCV3EY7652mlcwMtjTt9Sdb5Yoy29SJ884ScoNWLSNlH003zeMcX8l";

        // Retrieve the item from the database
        Item item = (Item) itemRepository.findByUser(user);

        // Create a checkout session
        SessionCreateParams.Builder builder = new SessionCreateParams.Builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/paymentSuccessView")
                .setCancelUrl("http://localhost:8080/paymentCancelView")
                .addLineItem(new SessionCreateParams.LineItem.Builder()
                        .setName(item.getItemName())
                        .setAmount((long) (item.getItemPrice() * 100)) // Stripe uses cents
                        .setCurrency("usd")
                        .setQuantity(1L)
                        .build());

        SessionCreateParams createParams = builder.build();
        Session session = Session.create(createParams);

        // Add the session ID to the model
        model.addAttribute("sessionId", session.getId());
        model.addAttribute("stripePublicKey", stripePublicKey);

        // Return the name of the view that should be displayed
        return "checkoutView";
    }*/
}


