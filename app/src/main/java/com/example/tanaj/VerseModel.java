package com.example.tanaj;

public class VerseModel {

    // Fields to show in activity
    private String hebrew;
    private String morphology;
    private String strong;
    private String translation;
    private String transliteration;
    private String grammar;

    // Fields to show in Strongs concordance
    private String sHebrew;
    private String sTransl;
    private String sDefint;
    private String sLemma;
    private String sOrigin;
    private String sCount;

    public VerseModel(String strong, String hebrew, String translation, String transliteration, String morphology, String grammar) {
        this.hebrew = hebrew;
        this.morphology = morphology;
        this.strong = strong;
        this.translation = translation;
        this.transliteration = transliteration;
        this.grammar = grammar;
    }

    public String getHebrew() {
        return hebrew;
    }

    public void setHebrew(String hebrew) {
        this.hebrew = hebrew;
    }

    public String getMorphology() {
        return morphology;
    }

    public void setMorphology(String morphology) {
        this.morphology = morphology;
    }

    public String getStrong() {
        return strong;
    }

    public void setStrong(String strong) {
        this.strong = strong;
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

    public String getsHebrew(){
        return this.sHebrew;
    }

    public void setsHebrew(String sHebrew){
        this.sHebrew = sHebrew;
    }

    public String getsTransl(){
        return this.sTransl;
    }

    public void setsTransl(String sTransl){
        this.sTransl = sTransl;
    }

    public String getsDefint() {
        return sDefint;
    }

    public void setsDefint(String sDefint) {
        this.sDefint = sDefint;
    }

    public String getsLemma() {
        return sLemma;
    }

    public void setsLemma(String sLemma) {
        this.sLemma = sLemma;
    }

    public String getsOrigin() {
        return sOrigin;
    }

    public void setsOrigin(String sOrigin) {
        this.sOrigin = sOrigin;
    }

    public String getsCount() {
        return sCount;
    }

    public void setsCount(String sCount) {
        this.sCount = sCount;
    }

    public String getGrammar() {
        return grammar;
    }

    public void setsGrammar(String newGrammar) {
        this.grammar = newGrammar;
    }
}
