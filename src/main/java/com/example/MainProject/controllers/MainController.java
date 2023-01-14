package com.example.MainProject.controllers;

import com.example.MainProject.models.Person;
import com.example.MainProject.models.Transaction;
import com.example.MainProject.services.BooksService;
import com.example.MainProject.services.PeopleService;
import com.example.MainProject.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("/home")
public class MainController {

    private final BooksService booksService;
    private final PeopleService peopleService;
    private final TransactionsService transactionsService;

    @Autowired
    public MainController(BooksService booksService, PeopleService peopleService, TransactionsService transactionsService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
        this.transactionsService = transactionsService;
    }

    @RequestMapping()
    public String mainPage() {
        return "main/home";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, Principal principal) {
        model.addAttribute("person", peopleService.findByUsername(principal.getName()));
        return "main/profile";
    }

    @GetMapping("/mybook")
    public String myBookPage(Model model, Principal principal) {
        model.addAttribute("transactions", peopleService.findByUsername(principal.getName()).getTransactionList());
        return "main/my_book";
    }

    @GetMapping("/mybook/{id}")
    public String showMyBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findOne(id));

        return "main/show_mybook";
    }


    @PostMapping("/mybook/{id}")
    public String downloadBook(@PathVariable int id) {
        Path path = Paths.get(booksService.findOne(id).getFile());
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

        return "redirect:/home/mybook/{id}";
    }

    @GetMapping()
    public String indexBook(Model model) {
        model.addAttribute("books", booksService.findAll());
        return "main/home";
    }

    @GetMapping("/book/{id}")
    public String showBook(@PathVariable("id") int id, Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("bookTakeTest", booksService.findOne(id)
                    .getTransactionList()
                    .stream()
                    .anyMatch(transaction -> Objects.equals(
                            transaction.getPerson().getUsername(),
                            principal.getName())
                    ));
        }
        model.addAttribute("book", booksService.findOne(id));
        return "main/show_book";
    }

    @PostMapping("/book/{id}")
    public String addTransactionFromPerson(@ModelAttribute("transaction")Transaction transaction, @PathVariable("id") int id, @AuthenticationPrincipal(expression = "username") String username) {
        transactionsService.saveFromPerson(transaction, id, username);
        return "redirect:/home";
    }

    @RequestMapping("/profile/setting")
    public String settingPerson() {
        return "main/person_setting";
    }

    @GetMapping("/profile/setting/resetdate")
    public String updateDate(Model model, Principal principal) {
        model.addAttribute("person", peopleService.findByUsername(principal.getName()));
        return "people/reset_date_of_birth";
    }

    @PatchMapping("/profile/setting/resetdate")
    public String updateDate(@ModelAttribute("person") Person person, Principal principal) {
        Person person1 = peopleService.findByUsername(principal.getName());
        person1.setDateOfBirth(person.getDateOfBirth());
        peopleService.update(person1, person1.getPersonId());
        return "redirect:/home/profile";
    }

    @GetMapping("/profile/setting/resetname")
    public String updateName(Model model, Principal principal) {
        model.addAttribute("person", peopleService.findByUsername(principal.getName()));
        return "people/reset_date_of_birth";
    }

    @PatchMapping("/profile/setting/resetname")
    public String updateName(@ModelAttribute("person") Person person, Principal principal) {
        Person person1 = peopleService.findByUsername(principal.getName());
        person1.setName(person.getName());
        person1.setSurname(person.getSurname());
        peopleService.update(person1, person1.getPersonId());
        return "redirect:/home/profile";
    }

    @GetMapping("/profile/setting/resetpassword")
    public String updatePassword(Model model, Principal principal) {
        model.addAttribute("person", peopleService.findByUsername(principal.getName()));
        return "people/reset_date_of_birth";
    }

    @PatchMapping("/profile/setting/resetpassword")
    public String updatePassword(@ModelAttribute("person") Person person, Principal principal) {
        Person person1 = peopleService.findByUsername(principal.getName());
        person1.setPassword(person.getPassword());
        peopleService.update(person1, person1.getPersonId());
        return "redirect:/home/profile";
    }
}
