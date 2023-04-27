package com.example.Controller;

import com.example.Model.*;
import com.example.Repo.ItemRepository;
import com.example.Repo.UserRepository;
import com.example.serviceImp.ChatServiceImp;
import com.example.serviceImp.ItemServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import java.lang.Integer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static java.lang.String.format;


@Controller
public class ChatController {

    @Autowired
    private ChatServiceImp chatService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemServiceImp itemServiceImp;

    @MessageMapping("/hello")
    @SendTo("/topic/sellerbuyer")
    public ChatModel sendMessage(@Payload ChatModel webSocketChatMessage) {
        try{
            chatService.saveMessage(webSocketChatMessage);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        return webSocketChatMessage;
    }

    @GetMapping("/chat/messages/{sender}")
    public ResponseEntity<?> chatMessages(@PathVariable("sender") Integer sender, HttpServletRequest request) {
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User user= userRepo.findByEmail(userDetails.getUsername());
            request.setAttribute("username",user.getEmail());
            System.out.println(sender);
            ChatMessagesResponse chatMessagesResponse= chatService.getMessagesBetweenUsers((int) user.getId(),sender);
            return  ResponseEntity.ok(chatMessagesResponse);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok(e.getMessage());
        }

    }

    @GetMapping("/chat")
    public String chat(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user= userRepo.findByEmail(userDetails.getUsername());
        List<UserModel> users= chatService.getMessagesForUser((int) user.getId());
        model.addAttribute("userList", users);
        System.out.println(users.size());
        System.out.println();
        return "chat";
    }
}
