package com.example.test01.demo.client.lord;

import com.example.test01.demo.client.lord.model.book.Book;
import com.example.test01.demo.client.lord.model.book.GetAllBooksResponse;
import com.example.test01.demo.client.lord.model.chapter.MappedChaptersResponse;

public interface LordClient {
    GetAllBooksResponse getAllBooks();
    Book getOneBook(String id);
    MappedChaptersResponse getChaptersByBookId(String id);
    MappedChaptersResponse getAllChapters();
}
