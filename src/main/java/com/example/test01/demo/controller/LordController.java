package com.example.test01.demo.controller;

import com.example.test01.demo.client.lord.LordClient;
import com.example.test01.demo.client.lord.model.book.Book;
import com.example.test01.demo.client.lord.model.book.GetAllBooksResponse;
import com.example.test01.demo.client.lord.model.chapter.MappedChaptersResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lord")
@AllArgsConstructor
public class LordController {
    private final LordClient lordClient;

    @GetMapping("/book")
    public ResponseEntity<GetAllBooksResponse> getAll(){
        return ResponseEntity.ok(lordClient.getAllBooks());
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Book> getById(final @PathVariable(value = "id") String id){
        return ResponseEntity.ok(lordClient.getOneBook(id));
    }

    @GetMapping("/book/{id}/chapter")
    public  ResponseEntity<MappedChaptersResponse> getChaptersOfBook(final @PathVariable(value = "id") String id){
        return ResponseEntity.ok(lordClient.getChaptersByBookId(id));
    }

    @GetMapping("/chapter")
    public  ResponseEntity<MappedChaptersResponse> getAllChapters(){
        return ResponseEntity.ok(lordClient.getAllChapters());
    }

}
