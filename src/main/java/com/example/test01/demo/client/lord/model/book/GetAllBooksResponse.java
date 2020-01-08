package com.example.test01.demo.client.lord.model.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllBooksResponse {

    private List<Book> books;

    @JsonProperty("books")
    public List<Book> getBooks(){
        return this.books;
    }

    @JsonProperty("docs")
    public void setBooks(List<Book> books){
        this.books=books;
    }
}
