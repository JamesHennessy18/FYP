package com.example.serviceImp;

import com.example.Model.*;
import java.lang.Integer;
import com.example.Repo.ChatRepository;
import com.example.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChatServiceImp {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    public void saveMessage(ChatModel chatModel) {
        Optional<User> sender = userRepository.findById(chatModel.getSender().longValue());
        Optional<User> reciever = userRepository.findById(chatModel.getReceiever().longValue());

        if(sender.isPresent() && reciever.isPresent() && sender.get().getId() != (reciever.get().getId())){
            ChatMessages chatMessages= new ChatMessages();
            chatMessages.setContent(chatModel.getContent());
            chatMessages.setReceiever(reciever.get());
            chatMessages.setSender(sender.get());
            chatMessages.setCreatedAt(LocalDateTime.now());
            chatRepository.save(chatMessages);
        }
    }

    public ChatMessagesResponse getMessagesBetweenUsers(Integer sender , Integer receiever){
        List<ChatMessages> list1= chatRepository.findChatMessagesBySenderAndReceiever(userRepository.findById(sender.longValue()).get(),userRepository.findById(receiever.longValue()).get());
        List<ChatMessages> list2= chatRepository.findChatMessagesBySenderAndReceiever(userRepository.findById(receiever.longValue()).get(),userRepository.findById(sender.longValue()).get());
        list1.addAll(list2);
        Collections.sort(list1,
                (o1, o2) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()));
        ChatMessagesResponse chatMessagesResponse= new ChatMessagesResponse();
        List<ChatMessagesR> chatMessagesRS = new ArrayList<>();
        for(int i=0;i<list1.size();i++) {
            ChatMessagesR chatMessagesR= new ChatMessagesR();
            chatMessagesR.setChatId(list1.get(i).getChatId());
            chatMessagesR.setContent(list1.get(i).getContent());
            chatMessagesR.setCreatedAt(list1.get(i).getCreatedAt());
            Sender sender1= new Sender();
            sender1.setEmail(list1.get(i).getSender().getEmail());
            sender1.setId(list1.get(i).getSender().getId());
            sender1.setFirstName(list1.get(i).getSender().getFirstName());
            sender1.setLastName(list1.get(i).getSender().getLastName());
            sender1.setPassword(list1.get(i).getSender().getPassword());


            Receiver receiver= new Receiver();
            receiver.setEmail(list1.get(i).getReceiever().getEmail());
            receiver.setId(list1.get(i).getReceiever().getId());
            receiver.setFirstName(list1.get(i).getReceiever().getFirstName());
            receiver.setLastName(list1.get(i).getReceiever().getLastName());
            receiver.setPassword(list1.get(i).getReceiever().getPassword());

            chatMessagesR.setSender(sender1);
            chatMessagesR.setReceiever(receiver);
            chatMessagesRS.add(chatMessagesR);
        }
        chatMessagesResponse.setChatMessagesList(chatMessagesRS);
        chatMessagesResponse.setCurrentUserId(sender.longValue());

        chatMessagesResponse.setCurrentUserEmail(userRepository.findById(receiever.longValue()).get().getEmail());
        return chatMessagesResponse;
    }

    public boolean checkUserAdded(List<UserModel> userModels,Integer id) {
        for (int i = 0; i < userModels.size(); i++) {
            if (userModels.get(i).getId().intValue() == id) {
                return true;
            }
        }
        return false;
    }
        public List<UserModel> getMessagesForUser(Integer sender){
            List<ChatMessages> list1= chatRepository.findChatMessagesBySender(userRepository.findById(sender.longValue()).get());
            List<ChatMessages> list2= chatRepository.findChatMessagesByReceiever(userRepository.findById(sender.longValue()).get());
            List<UserModel> userModels = new ArrayList<>();
            Optional<User> user= userRepository.findById(sender.longValue());


            for(int i=0;i<list1.size();i++) {
                Long baseTemp1 = list1.get(i).getReceiever().getId();

                if(!list1.get(i).getReceiever().getEmail().equals(user.get().getEmail()) && !checkUserAdded(userModels, baseTemp1.intValue())) {
                    UserModel userModel = new UserModel();
                    userModel.setId(list1.get(i).getReceiever().getId());
                    userModel.setUsername(list1.get(i).getReceiever().getFirstName());
                    userModels.add(userModel);
                }
            }

            for(int i=0;i<list2.size();i++) {
                Long baseTemp2 = list2.get(i).getSender().getId();
                if(!list2.get(i).getSender().getEmail().equals(user.get().getEmail()) && !checkUserAdded(userModels, baseTemp2.intValue())) {
                    UserModel userModel = new UserModel();
                    userModel.setId(list2.get(i).getSender().getId());
                    userModel.setUsername(list2.get(i).getSender().getFirstName());
                    userModels.add(userModel);
                }
            }

            Set<UserModel> set = new LinkedHashSet<>(userModels);
            userModels.clear();
            userModels.addAll(set);


            return userModels;
        }
    }


