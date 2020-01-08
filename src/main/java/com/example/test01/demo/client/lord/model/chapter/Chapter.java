package com.example.test01.demo.client.lord.model.chapter;

import com.example.test01.demo.client.lord.model.book.Book;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@Builder
public class Chapter {
    private String _id;
    private String book;

    public static ChapterWithBook transformBookIdToName(final Chapter chapter, final List<Book> books){
        final Optional<Book> book = books.stream()
                .filter(item -> item.get_id().equals(chapter.getBook())).findFirst();
        if(book.isPresent()){
            return ChapterWithBook.builder()
                    ._id(chapter.get_id())
                    .book(book.get())
                    .build();
        }
        return ChapterWithBook.builder()
                ._id(chapter.get_id())
                .book(Book.builder().name("Unknown").build())
                .build();
    }
}
