package com.example.test01.demo.client.lord;

import com.example.test01.demo.client.lord.model.book.Book;
import com.example.test01.demo.client.lord.model.book.GetAllBooksResponse;
import com.example.test01.demo.client.lord.model.chapter.Chapter;
import com.example.test01.demo.client.lord.model.chapter.GetListResponse;

import java.util.List;

public interface LordClient {
    GetAllBooksResponse getAllBooks();
    Book getOneBook(String id);
    GetListResponse getChaptersByBookId(String id);
    GetListResponse getAllChapters();
}
