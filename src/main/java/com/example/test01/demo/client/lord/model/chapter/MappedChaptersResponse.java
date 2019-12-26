package com.example.test01.demo.client.lord.model.chapter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MappedChaptersResponse {
    private List<ChapterWithBook> docs;
}
