package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import com.example.demo.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private CategoryService categoryService;


    @GetMapping
    public String listBook(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "book/index";
    }

    @GetMapping("/add")
    public String addBook(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "book/add";
    }

    @PostMapping("/add")
    public String postBook(@Valid @ModelAttribute("book") Book book,
                           BindingResult result,
                           Model model,
                           @RequestParam("file") MultipartFile file) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "book/add";
        } else {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/images").getFile();
                String fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + fileName);
                Files.copy(file.getInputStream(), path);
                book.setCover(fileName);
                bookService.add(book);
            }
            return "redirect:/";
        }
    }

    @GetMapping("edit/{id}")
    public String editBook(@PathVariable("id") Long id, Model model) {
            model.addAttribute("book", bookService.getBookById(id));
            model.addAttribute("categories", categoryService.getAllCategories());
            return "book/edit";
    }
    @PostMapping("edit")
    public String updateBook(@Valid @ModelAttribute("book") Book book,
                             BindingResult result,
                             Model model,
                             @RequestParam("file") MultipartFile file) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "book/edit";
        } else {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/images").getFile();
                String fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + fileName);
                Files.copy(file.getInputStream(), path);
                book.setCover(fileName);
                bookService.update(book);
            } else {
                book.setCover(bookService.getBookById(book.getId()).getCover());
                bookService.update(book);
            }
            return "redirect:/";
        }
    }

    @RequestMapping("delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return "redirect:/";
    }
}
