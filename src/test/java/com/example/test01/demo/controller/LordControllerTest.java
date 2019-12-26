package com.example.test01.demo.controller;

import com.example.test01.demo.client.lord.LordClient;
import com.example.test01.demo.client.lord.exception.LordClientException;
import com.example.test01.demo.client.lord.model.book.Book;
import com.example.test01.demo.client.lord.model.book.GetAllBooksResponse;
import com.example.test01.demo.client.lord.model.chapter.Chapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class LordControllerTest {

    @MockBean
    private LordClient client;

    @Autowired
    private LordController controller;

    @Test
    void checkIfTheControllerIsGettingTheListOfBooks() {
        final List<Book> books = Collections.singletonList(Book.builder()
                ._id("0987654321")
                .name("my personal book")
                .build());
        final GetAllBooksResponse expectedResponse = GetAllBooksResponse.builder().docs(books).build();
        when(client.getAllBooks()).thenReturn(expectedResponse);
        final ResponseEntity<GetAllBooksResponse> controllerResponse = controller.getAll();
        assertNotEquals(controllerResponse.getBody().getDocs(),Collections.EMPTY_LIST);
        assertEquals(controllerResponse.getBody().getDocs(),expectedResponse.getDocs());

    }

    @Test
    void getById() {
        final String wrongId = "1234567890";
        final String rightId = "0987654321";
        final Book book = Book.builder()
                ._id(rightId)
                .name("my personal book")
                .build();
        when(client.getOneBook(wrongId)).thenThrow(new LordClientException(ResponseEntity.badRequest().build()));
        when(client.getOneBook(rightId)).thenReturn(book);
        final LordClientException exception = assertThrows(LordClientException.class,()->controller.getById(wrongId));
        assertEquals(exception.getResponse().getStatusCode(), HttpStatus.BAD_REQUEST);
        final Book bookFromController =  client.getOneBook(rightId);
        assertEquals(book,bookFromController);
    }

    @Test
    void getChaptersOfBook() {
    }

    @Test
    void getAllChapters() {
    }
}