package com.example.library;

import java.util.HashMap;
import java.util.Map;

public class Book {
    static Map<String, String> genreT = new HashMap<String, String>();
    static {
        genreT.put("Народ", "Сказка");
        genreT.put("Роулинг", "Фэнтези");
        genreT.put("Достоевский", "Классика");
        genreT.put("Эвклид", "Учебники");
        genreT.put("Толстой", "Классика");
    }
    String title;
    String author;
    String genre;
    int year;
    int coverId;

    public Book(String title, String author,int year,int coverId) {
        this.title = title;
        this.author = author;
        this.year=year;
        this.coverId=coverId;
        if(genreT.containsKey(author)){
            genre=genreT.get(author);
        } else {
            genre="Неизвестно";
        }
    }

    @Override
    public String toString() {
        return  title + "/" + author + "/" + year + "/" + coverId + "/";
    }
}
