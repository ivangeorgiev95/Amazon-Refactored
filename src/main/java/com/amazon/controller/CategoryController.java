package com.amazon.controller;

import com.amazon.domain.Category;
import com.amazon.service.CategoryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryController {

    private final @NonNull CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> getAllCategories(){return categoryService.getAllCategories();}
    @GetMapping("/departments")
    public List<Category> getDepartments(){return categoryService.getAllParentCategories();}

}
