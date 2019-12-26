package com.example.test01.demo.client.lord.model.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Book {
    private String _id;
    private String name;
}
