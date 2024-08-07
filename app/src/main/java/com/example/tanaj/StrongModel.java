package com.example.tanaj;

import java.util.List;

public class StrongModel {
    private int book;
    private int chapter;
    private int verse;
    private String cite;
    private String bookName;

    public StrongModel(String b, String c, String v, List<String> books) {
        this.book = Integer.parseInt(b);
        this.chapter = Integer.parseInt(c);
        this.verse = Integer.parseInt(v);
        this.bookName = books.get(this.book-1);
        this.cite = bookName + " " + chapter + ":" + verse;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getVerse() {
        return verse;
    }

    public void setVerse(int verse) {
        this.verse = verse;
    }

    public String getCite() {
        return cite;
    }

    public void setCite(String cite) {
        this.cite = cite;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }


}
