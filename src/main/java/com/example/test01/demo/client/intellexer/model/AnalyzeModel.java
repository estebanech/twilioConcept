package com.example.test01.demo.client.intellexer.model;

import lombok.Data;

import java.util.List;

@Data
public class AnalyzeModel {
    private TextModel sentences;
    private List<TextModel> tokens;
    private List<RelationModel> relations;
}
