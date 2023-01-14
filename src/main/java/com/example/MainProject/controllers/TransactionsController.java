package com.example.MainProject.controllers;

import com.example.MainProject.models.Transaction;
import com.example.MainProject.services.AdminService;
import com.example.MainProject.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/transactions")
public class TransactionsController {

    private final TransactionsService transactionsService;
    private final AdminService adminService;

    @Autowired
    public TransactionsController(TransactionsService transactionsService, AdminService adminService) {
        this.transactionsService = transactionsService;
        this.adminService = adminService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("transactions", transactionsService.findAll());
        return "transactions/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("transaction", transactionsService.findOne(id));
        return "transactions/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("transaction") Transaction transaction) {
        return "transactions/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("transaction") Transaction transaction) {
        transactionsService.save(transaction);
        return "redirect:/admin/transactions";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("transaction") Transaction transaction, @PathVariable("id") int id) {
        transactionsService.update(transaction, id);
        return "redirect:/admin/transactions";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        transactionsService.delete(id);
        return "redirect:/admin/transactions";
    }
}
