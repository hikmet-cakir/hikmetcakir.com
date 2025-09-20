package com.hikmetcakir.coreapi.controller;

import com.hikmetcakir.coreapi.dto.article.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @GetMapping
    public ArticleQueryResponse query(@ModelAttribute ArticleQueryRequest request) {
        var articleSummary = ArticleSummary.builder()
                .id(UUID.randomUUID().toString())
                .title("DummyTitle")
                .content("DummyContent")
                .build();
        return ArticleQueryResponse.builder()
                .articleSummaryList(List.of(articleSummary))
                .build();
    }

    @PostMapping
    public ArticleSaveResponse save(@RequestBody ArticleSaveRequest request) {
        return ArticleSaveResponse.builder()
                .id(UUID.randomUUID().toString())
                .build();
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") Long id, @RequestBody ArticleUpdateRequest request) {
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
    }
}
