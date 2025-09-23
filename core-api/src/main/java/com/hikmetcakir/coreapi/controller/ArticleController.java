package com.hikmetcakir.coreapi.controller;

import com.hikmetcakir.coreapi.dto.article.*;
import com.hikmetcakir.coreapi.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
@AllArgsConstructor
public class ArticleController {

    private ArticleService articleService;

    @GetMapping
    public ArticleQueryResponse query(@ModelAttribute ArticleQueryRequest request) {
        List<ArticleSummary> articleSummaryList = articleService.query(request);
        return ArticleQueryResponse.builder().articleSummaryList(articleSummaryList).build();
    }

    @PostMapping
    public ArticleSaveResponse save(@RequestBody ArticleSaveRequest request) {
        String id = articleService.save(request);
        return ArticleSaveResponse.builder().id(id).build();
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") String id, @RequestBody ArticleUpdateRequest request) {
        articleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        articleService.delete(id);
    }
}
