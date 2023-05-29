package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping
    public String listCategory(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "category/index";
    }
    @GetMapping("/add")
    public String newCategory(Model model){
        model.addAttribute("category", new Category());
        return "category/add";
    }
    @PostMapping("/add")
    public String postCategory(@Valid @ModelAttribute("category") Category category,
                               BindingResult result,
                               Model model){
        if (result.hasErrors()) {
            model.addAttribute("category", category);
            return "category/add";
        }else categoryService.add(category);
        return "redirect:/category";
    }
    @GetMapping("edit/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model){
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "category/edit";
    }
    @PostMapping("/edit")
    public String updateCategory(@Valid @ModelAttribute("category") Category category,
                               BindingResult result,
                               Model model){
        if (result.hasErrors()) {
            model.addAttribute("category", category);
            return "category/edit";
        }else categoryService.add(category);
        return "redirect:/category";
    }
    @RequestMapping("delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id){
        categoryService.delete(id);
        return "redirect:/category";
    }
}
