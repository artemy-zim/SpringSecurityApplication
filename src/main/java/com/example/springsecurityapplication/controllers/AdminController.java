package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.enumm.Status;
import com.example.springsecurityapplication.models.*;
import com.example.springsecurityapplication.repositories.CategoryRepository;
import com.example.springsecurityapplication.repositories.OrderRecordRepository;
import com.example.springsecurityapplication.repositories.OrderRepository;
import com.example.springsecurityapplication.repositories.PersonRepository;
import com.example.springsecurityapplication.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class AdminController {

    List<Status> status =new ArrayList<>(EnumSet.allOf(Status.class));
    private final ProductService productService;
    private final PersonService personService;
    private final OrderRecordService orderRecordService;

    @Value("${upload.path}")
    private String uploadPath;

    private final CategoryRepository categoryRepository;
    private final PersonRepository personRepository;
    private final OrderRepository orderRepository;

    private final OrderRecordRepository orderRecordRepository;

    public AdminController(ProductService productService,
                           CategoryRepository categoryRepository,
                           PersonService personService,
                           PersonRepository personRepository,
                           OrderRepository orderRepository,
                           OrderRecordService orderRecordService,
                           OrderRecordRepository orderRecordRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.personService = personService;
        this.personRepository = personRepository;
        this.orderRepository = orderRepository;
        this.orderRecordService = orderRecordService;
        this.orderRecordRepository = orderRecordRepository;
    }

    @GetMapping("admin/product/add")
    public String addProduct(Model model){
        model.addAttribute("product", new Product());
        model.addAttribute("category", categoryRepository.findAll());
        return "product/addProduct";
    }

    @PostMapping("/admin/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("file_one")MultipartFile file_one, @RequestParam("file_two")MultipartFile file_two, @RequestParam("file_three")MultipartFile file_three, @RequestParam("file_four")MultipartFile file_four, @RequestParam("file_five")MultipartFile file_five, @RequestParam("category") int category, Model model) throws IOException {
        Category category_db = (Category) categoryRepository.findById(category).orElseThrow();
        System.out.println(category_db.getName());
        if(bindingResult.hasErrors()){
            model.addAttribute("category", categoryRepository.findAll());
            return "product/addProduct";
        }

        if(file_one != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);

        }

        if(file_two != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_three != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_four != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();
            file_four.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if(file_five != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_five .getOriginalFilename();
            file_five .transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        productService.saveProduct(product, category_db);
        return "redirect:/admin";
    }


    @GetMapping("/admin")
    public String admin(Model model)
    {
        model.addAttribute("persons", personRepository.findAll());
        model.addAttribute("products", productService.getAllProduct());
        return "/admin";
    }

    @GetMapping("/admin/view_order/{id}")
    public String viewOrder(@PathVariable("id") int id, Model model) {
        model.addAttribute("persons", personRepository.findAll());
        model.addAttribute("products", productService.getAllProduct());
        model.addAttribute("orders", orderRecordRepository.findOrderByPersonId(id));
        model.addAttribute("status", status);
        return "/admin";
    }

    @PostMapping("/admin/view_order/{id}")
    public String editOrder(@ModelAttribute("orderperson") @Valid OrderRecord orderRecord, @PathVariable("id") int id, @RequestParam("status") String status, @RequestParam("order_number") String orderNumber, @RequestParam("order_id") String idOrder, Model model) {
        OrderRecord person = orderRecordRepository.findOrderById(Integer.parseInt(idOrder));
        person.setStatus(Status.valueOf(status));
        orderRecordRepository.save(person);
        return "redirect:/admin/view_order/{id}";
    }

    @PostMapping("/admin/view_order/search_order")
    public String searchOrder(Model model, @RequestParam("search_order") String searchOrder) {
        model.addAttribute("users", personRepository.findAll());
        model.addAttribute("products", productService.getAllProduct());
        model.addAttribute("find_orders", orderRecordService.findByNumberEndingWithIgnoreCase(searchOrder));
        return "redirect:/admin";
    }
    @GetMapping("/admin/change_role/{id}")
    public String changeRole(@PathVariable("id") int id, Model model) {
        Person person = personService.findById(id);
        String person_role = person.getRole();
        if (!person.getLogin().equals("admin")) {
            if (!person_role.equals("ROLE_USER")) {
                person_role = "ROLE_USER";
            } else {
                person_role = "ROLE_ADMIN";
            }
        }
        person.setRole(person_role);
        personRepository.save(person);
        model.addAttribute("person");
        return "redirect:/admin";
    }

    @GetMapping("admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id){
        productService.deleteProduct(id);
        return "redirect:/admin";
    }

    @GetMapping("admin/product/edit/{id}")
    public String editProduct(Model model, @PathVariable("id") int id){
        model.addAttribute("product", productService.getProductId(id));
        model.addAttribute("category", categoryRepository.findAll());
        return "product/editProduct";
    }

    @PostMapping("admin/product/edit/{id}")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @PathVariable("id") int id, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("category", categoryRepository.findAll());
            return "product/editProduct";
        }
        productService.updateProduct(id, product);
        return "redirect:/admin";
    }
}
