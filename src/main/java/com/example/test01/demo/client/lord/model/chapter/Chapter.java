package com.example.test01.demo.client.lord.model.chapter;

import com.example.test01.demo.client.lord.model.book.Book;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
public class Chapter {
    private String _id;
    private String book;

    public static Chapter transformBookIdToName(final Chapter chapter, final List<Book> books){
        final Optional<Book> book = books.stream()
                .filter(item -> item.get_id().equals(chapter.getBook())).findFirst();
        if(book.isPresent()){
            return Chapter.builder()
                    ._id(chapter.get_id())
                    .book(book.get().getName())
                    .build();
        }
        return Chapter.builder()
                ._id(chapter.get_id())
                .book("Unknown")
                .build();
    }
}
