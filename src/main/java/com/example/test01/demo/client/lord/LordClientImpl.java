package com.example.test01.demo.client.lord;

import com.example.test01.demo.client.lord.exception.LordClientException;
import com.example.test01.demo.client.lord.model.book.Book;
import com.example.test01.demo.client.lord.model.book.GetAllBooksRequest;
import com.example.test01.demo.client.lord.model.book.GetAllBooksResponse;
import com.example.test01.demo.client.lord.model.book.GetOneBookRequest;
import com.example.test01.demo.client.lord.model.chapter.Chapter;
import com.example.test01.demo.client.lord.model.chapter.ChapterWithBook;
import com.example.test01.demo.client.lord.model.chapter.GetListRequest;
import com.example.test01.demo.client.lord.model.chapter.GetListResponse;
import com.example.test01.demo.client.lord.model.chapter.MappedChaptersResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@AllArgsConstructor
public class LordClientImpl implements LordClient {

    private final LordProperties properties;

    @Qualifier("lordRestTemplate") private final RestTemplate restTemplate;

    private HttpHeaders getHeaders(){
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
    private HttpHeaders getAuthHeaders(){
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getKey());
        return headers;
    }

    private <T, R> R makeRequest(final LordRequest<T, R> lordRequest) {
        log.info("Request URL: {}", lordRequest.getPath());
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(properties.getUrl())
                .path(lordRequest.getPath());
        final HttpHeaders headers;
        if(lordRequest.isRequireAuth()){
            headers = getAuthHeaders();
        }else {
            headers = getHeaders();
        }
        final HttpEntity<T> entity = new HttpEntity<>(lordRequest.getRequest(), headers);
        log.info("Request: {}", entity.toString());

        final ResponseEntity<R> response;
        response = restTemplate.exchange(
                builder.build().encode().toUri(),
                lordRequest.getMethod(),
                entity,
                lordRequest.getClazz());
        log.info("Response: {}", response.toString());
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Request error {}", response);
            throw new LordClientException(response);
        }
        return response.getBody();
    }

    @Override
    public GetAllBooksResponse getAllBooks(){
        final LordRequest<GetAllBooksRequest,GetAllBooksResponse> request = LordRequest.
                <GetAllBooksRequest,GetAllBooksResponse>builder()
                .path("/book")
                .clazz(GetAllBooksResponse.class)
                .build();
        return makeRequest(request);
    }

    @Override
    public Book getOneBook(final String id){
        final LordRequest<GetOneBookRequest, Book> request = LordRequest.
                <GetOneBookRequest,Book>builder()
                .path(String.format("/book/%s",id))
                .clazz(Book.class)
                .build();
        return makeRequest(request);
    }

    @Override
    public MappedChaptersResponse getChaptersByBookId(final String id){
        final Book book = getOneBook(id);
        final LordRequest<GetListRequest, GetListResponse> request = LordRequest.
                <GetListRequest,GetListResponse>builder()
                .path(String.format("/book/%s/chapter",id))
                .clazz(GetListResponse.class)
                .build();
        final GetListResponse chapters = makeRequest(request);
        return transformBookIdByBook(chapters,book);
    }

    @Override
    public MappedChaptersResponse getAllChapters(){
        final LordRequest<GetListRequest, GetListResponse> request = LordRequest.
                <GetListRequest,GetListResponse>builder()
                .path("/chapter")
                .clazz(GetListResponse.class)
                .requireAuth(true)
                .build();
        final GetListResponse chapters = makeRequest(request);
        final GetAllBooksResponse books = getAllBooks();
        return transformBookIdByListOfBooks(chapters,books.getDocs());
    }

    private MappedChaptersResponse transformBookIdByBook(final GetListResponse listResponse, final Book book){
        return MappedChaptersResponse.builder()
                .docs(listResponse.getDocs().stream().map(chapter -> ChapterWithBook.builder()
                        ._id(chapter.get_id())
                        .book(book)
                        .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private MappedChaptersResponse transformBookIdByListOfBooks(final GetListResponse listResponse, final List<Book> books){
        return MappedChaptersResponse.builder()
                .docs(listResponse.getDocs().stream()
                        .map((chapter)->Chapter.transformBookIdToName(chapter,books))
                        .collect(Collectors.toList()))
                .build();
    }
}
