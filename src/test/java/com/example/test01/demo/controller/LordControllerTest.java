package com.example.test01.demo.controller;

import com.example.test01.demo.client.lord.LordClient;
import com.example.test01.demo.client.lord.model.book.Book;
import com.example.test01.demo.client.lord.model.book.GetAllBooksResponse;
import com.example.test01.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class LordControllerTest {

    @MockBean
    private LordClient client;
    
    @Test
    void getAll() {
        final List<Book> books = Collections.singletonList(new Book("1234567890", "my personal book"));
        final GetAllBooksResponse expectedResponse = GetAllBooksResponse.builder().docs(books).build();
        when(client.getAllBooks()).thenReturn(expectedResponse);
        final GetAllBooksResponse response = client.getAllBooks();
        assertNotEquals(response.getDocs(),Collections.EMPTY_LIST);
        assertEquals(response.getDocs(),expectedResponse.getDocs());
    }

    @Test
    void getById() {
    }

    @Test
    void getChaptersOfBook() {
    }

    @Test
    void getAllChapters() {
    }
}