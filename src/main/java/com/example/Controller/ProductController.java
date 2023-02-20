package com.example.Controller;

import com.example.Model.Item;
import com.example.Model.User;
import com.example.Repo.ItemRepository;
import com.example.Repo.UserRepository;
import com.example.Service.FileUploadUtil;
import com.example.serviceImp.ItemNotFoundException;
import com.example.serviceImp.ItemServiceImp;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Controller
public class ProductController {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemServiceImp itemServiceImp;

    @GetMapping("/listItems")
    public String listing(Model model) {
        model.addAttribute("item", new Item());
        model.addAttribute("pageTitle", "Sell Product");
        return "addItem";
    }

    @GetMapping("/products")
    public String listItems(Model model, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        List<Item> listItems = itemRepository.findByUser(user);
        model.addAttribute("listItems", listItems);
        return "productList";
    }

    @PostMapping("/products/save")
    public String itemAdd(Item item,  @RequestParam("file") MultipartFile multipartFile, Principal principal, RedirectAttributes redirectAttributes) throws IOException {
        User user = userRepository.findByEmail(principal.getName());
        item.setUser(user);
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        item.setImage(fileName);
        String uploadDir = "user-photos/" + item.getItemId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        itemRepository.save(item);


        redirectAttributes.addFlashAttribute("message", "Product Listed for sale");
        return "redirect:/products";
    }

    @GetMapping("/products/update/{itemId}")
        public String updateItem(@PathVariable("itemId") Long itemId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Item item = itemServiceImp.get(itemId);
            model.addAttribute("item", item);
            model.addAttribute("pageTitle", "Update Product");
            return "addItem";
        } catch (ItemNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", "Product Updated");
            return "redirect:/products";
        }

    }

    @GetMapping("/products/delete/{itemId}")
    public String deleteItem(@PathVariable("itemId") Long itemId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Product Deleted");
        itemServiceImp.delete(itemId);
        return "redirect:/products";
    }

}