package com.example.tanaj;

public class Word {

    private int strong;
    private String hebrew;

    public Word(int book, int chapter, int verse, int strong, String hebrew, String translation, String transliteration, String morphology) {
        this.strong = strong;
        this.hebrew = hebrew;
        this.translation = translation;
        this.transliteration = transliteration;
        this.morphology = morphology;
        this.book = book;
        this.chapter = chapter;
        this.verse = verse;
    }

    private String translation;
    private String transliteration;
    private String morphology;
    private int book;
    private int chapter;
    private int verse;

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

    public int getStrong() {
        return strong;
    }

    public void setStrong(int strong) {
        this.strong = strong;
    }

    public String getHebrew() {
        return hebrew;
    }

    public void setHebrew(String hebrew) {
        this.hebrew = hebrew;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public void setTransliteration(String transliteration) {
        this.transliteration = transliteration;
    }

    public String getMorphology() {
        return morphology;
    }

    public void setMorphology(String morphology) {
        this.morphology = morphology;
    }

    public int getVerse(){
        return verse;
    }

    public void setVerse(int verse){
        this.verse = verse;
    }

}
