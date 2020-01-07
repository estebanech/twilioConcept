package com.example.test01.demo.controller;

import com.example.test01.demo.client.lord.LordClient;
import com.example.test01.demo.client.lord.exception.LordClientException;
import com.example.test01.demo.client.lord.model.book.Book;
import com.example.test01.demo.client.lord.model.book.GetAllBooksResponse;
import com.example.test01.demo.client.lord.model.chapter.ChapterWithBook;
import com.example.test01.demo.client.lord.model.chapter.MappedChaptersResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
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
        final GetAllBooksResponse expectedResponse = GetAllBooksResponse.builder().books(books).build();
        when(client.getAllBooks()).thenReturn(expectedResponse);
        final ResponseEntity<GetAllBooksResponse> controllerResponse = controller.getAll();
        assertNotEquals(Objects.requireNonNull(controllerResponse.getBody()).getBooks(),Collections.EMPTY_LIST);
        assertEquals(controllerResponse.getBody().getBooks(),expectedResponse.getBooks());

    }

    @Test
    void checkIfControllerIsGettingABookFromClient() {
        final String wrongId = "1234567890";
        final String rightId = "0987654321";
        final Book book = Book.builder()
                ._id(rightId)
                .name("my personal book")
                .build();
        when(client.getOneBook(wrongId)).thenThrow(new LordClientException(
                ResponseEntity.badRequest().build()));
        when(client.getOneBook(rightId)).thenReturn(book);
        final LordClientException exception = assertThrows(LordClientException.class,() ->
                controller.getById(wrongId));
        assertEquals(exception.getResponse().getStatusCode(), HttpStatus.BAD_REQUEST);
        final ResponseEntity<Book> bookFromController =  controller.getById(rightId);
        assertEquals(bookFromController.getBody(),book);
    }

    @Test
    void CheckIfControllerIsGettingALisOfChaptersOfABookFromClient() {
        final String wrongBookId = "1234567890";
        final String rightBookId = "0987654321";
        final String chapterId = "13579";
        final Book book = Book.builder()
                ._id(rightBookId)
                .name("my personal book")
                .build();
        final List<ChapterWithBook> chapters =  Collections.singletonList(ChapterWithBook.builder()
                ._id(chapterId)
                .book(book)
                .build());
        final MappedChaptersResponse expectedResponse = MappedChaptersResponse.builder().chapters(chapters).build();
        when(client.getChaptersByBookId(wrongBookId)).thenThrow(new LordClientException(
                ResponseEntity.badRequest().build()));
        when(client.getChaptersByBookId(rightBookId)).thenReturn(expectedResponse);
        final LordClientException exception = assertThrows(LordClientException.class,() ->
                controller.getChaptersOfBook(wrongBookId));
        assertEquals(exception.getResponse().getStatusCode(), HttpStatus.BAD_REQUEST);
        final ResponseEntity<MappedChaptersResponse> controllerResponse = controller.getChaptersOfBook(rightBookId);
        assertEquals(controllerResponse.getBody(),expectedResponse);

    }

    @Test
    void CheckIfControllerIsGettingALisOfChaptersFromClient() {
        final String bookId = "0987654321";
        final String chapterId = "13579";
        final Book book = Book.builder()
                ._id(bookId)
                .name("my personal book")
                .build();
        final List<ChapterWithBook> chapters =  Collections.singletonList(ChapterWithBook.builder()
                ._id(chapterId)
                .book(book)
                .build());
        final MappedChaptersResponse expectedResponse = MappedChaptersResponse.builder().chapters(chapters).build();
        when(client.getAllChapters()).thenReturn(expectedResponse);
        final ResponseEntity<MappedChaptersResponse> controllerResponse = controller.getAllChapters();
        assertNotEquals(Objects.requireNonNull(controllerResponse.getBody()).getChapters(),Collections.EMPTY_LIST);
        assertEquals(controllerResponse.getBody().getChapters(),expectedResponse.getChapters());
    }
}