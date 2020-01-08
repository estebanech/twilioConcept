package com.example.test01.demo.client.lord.model.chapter;

import com.example.test01.demo.client.lord.model.book.Book;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChapterWithBook {
    private String _id;
    private Book book;
}
