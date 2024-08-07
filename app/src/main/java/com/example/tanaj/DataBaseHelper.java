package com.example.tanaj;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DataBaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 1;
    private static SQLiteDatabase DB;


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DB = getReadableDatabase();
    }

    public List<String> getBooks(int pos){
        String bookName;

        switch (pos){
            case 0:
                bookName = "BOOK_NAME_SP";
                break;
            case 1:
                bookName = "BOOK_NAME_TL";
                break;
            case 2:
                bookName = "BOOK_NAME_HE";
                break;
            default:
                bookName = "BOOK_NAME";
                break;
        }


        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {bookName};
        String sqlTables = "BOOK_NAME";

        qb.setTables(sqlTables);

        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        List<String> books = new ArrayList<>();

        c.moveToFirst();
        while (!c.isAfterLast()){
            books.add(c.getString(0));
            c.moveToNext();
        }

        c.close();

        return books;
    }

    public List<String> getChapters(int book){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String bookN = Integer.toString(book);

        String [] sqlSelect = {"CHAPTER_NUMBER"};
        String sqlTables = "VERSE";

        Cursor c = db.rawQuery("SELECT DISTINCT CHAPTER_NUMBER FROM VERSE WHERE BOOK_NUMBER = " + bookN + ";", null);

        List<String> chapters = new ArrayList<>();

        c.moveToFirst();
        while (!c.isAfterLast()){
            chapters.add(Integer.toString(c.getInt(0)));
            c.moveToNext();
        }

        c.close();

        return chapters;
    }

    public List<String> getVerses(int book, int chapter){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String bookN = Integer.toString(book);
        String chapN = Integer.toString(chapter);

        String [] sqlSelect = {"VERSE_NUMBER"};
        String sqlTables = "VERSE";

        Cursor c = db.rawQuery("SELECT VERSE_NUMBER FROM VERSE WHERE BOOK_NUMBER = " + bookN + " AND CHAPTER_NUMBER = " + chapN +  ";", null);

        List<String> chapters = new ArrayList<>();

        c.moveToFirst();
        while (!c.isAfterLast()){
            chapters.add(Integer.toString(c.getInt(0)));
            c.moveToNext();
        }

        c.close();

        return chapters;
    }

    public List<VerseModel> getVerse(int book, int chapter, int verse){
        String bn = Integer.toString(book);
        String cn = Integer.toString(chapter);
        String vn = Integer.toString(verse);
        String grammar;
        Cursor c1;

        // Get ID From verse
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT ID FROM VERSE WHERE BOOK_NUMBER =  " + bn + " AND CHAPTER_NUMBER = " + cn + " AND VERSE_NUMBER = " + vn, null);

        c.moveToFirst();
        int _id = c.getInt(0);
        c.close();

        // Get all terms from verse
        c = db.rawQuery("SELECT STRONGS_WORD_ID, CONTEXTUAL_FORM, SP, TRANSLITERATED_CONTEXTUAL_FORM, MORPHOLOGY_ID_SP FROM INTERLINEAR_WORD WHERE VERSE_ID = " + _id, null);

        // Retrieve data
        c.moveToFirst();

        List<VerseModel> words = new ArrayList<>();

        while (!c.isAfterLast()){
            /// Get category
            grammar = c.getString(4);
            c1 = db.rawQuery("SELECT LABEL FROM MORPHOLOGY WHERE ID = '" + grammar + "';", null);
            c1.moveToFirst();
            words.add(new VerseModel(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c1.getString(0)));
            c1.close();
            c.moveToNext();
        }

        c.close();

        // Configure Strong Fields per word
        words = getStrongs(words, db);

        return words;
    }

    private List<VerseModel> getStrongs(List<VerseModel> words, SQLiteDatabase db) {
        String href;
        Cursor c, c1;

        for(VerseModel w : words){
            // Convert strong reference to SQL format
            href = "'" + w.getStrong() + "'";

            // Query fields
            c = db.rawQuery("SELECT LEXICAL_FORM, TRANSLITERATED_LEXICAL_FORM, DEFINITION_SP, PART_OF_SPEECH_SP, ORIGIN FROM STRONGS_WORD WHERE HREF = " + href , null);

            if(c.getCount()>0){
                c.moveToFirst();
                w.setsHebrew(c.getString(0));
                w.setsTransl(c.getString(1));
                w.setsDefint(c.getString(2));
                w.setsLemma(c.getString(3));
                w.setsOrigin(c.getString(4));
            }else{
                w.setsHebrew(w.getHebrew());
                w.setsTransl(w.getTransliteration());
                w.setsDefint(w.getTranslation());
                w.setsLemma("-");
                w.setsOrigin("-");
            }

            c.close();

        }

        return words;
    }

    public List<String> queryStrong(String id_){

        // Convert to SQL Format
        String href =  "'" + id_ + "'";

        // Get information
        List<String> l = new ArrayList<>();

        // Create cursor
        Cursor c;

        c = DB.rawQuery("SELECT LEXICAL_FORM, TRANSLITERATED_LEXICAL_FORM, DEFINITION_SP, PART_OF_SPEECH_SP, ORIGIN FROM STRONGS_WORD WHERE ID = " + href + ";", null);

        if(c.getCount() > 0){
            c.moveToFirst();
            l.add(c.getString(0));
            l.add(c.getString(1));
            l.add(c.getString(2));
            l.add(c.getString(3));
            l.add(c.getString(4));
        }

        c.close();

        return l;

    }

    public int freqStrong(String id_){
        // Convert to SQL format
        String href =  "'h" + id_ + "'";

        // Cursor
        Cursor c;

        c = DB.rawQuery("SELECT count(*) FROM INTERLINEAR_WORD WHERE STRONGS_WORD_ID = " + href + ";", null);
        c.moveToFirst();

        int freq = c.getInt(0);
        c.close();

        return freq;
    }

    public List<List<String>> verseStrong(String id_){
        // Define list
        List<List<String>> l = new ArrayList<List<String>>();

        // Convert to SQL format
        String href =  "'h" + id_ + "'";

        // Cursor
        Cursor c;

        c = DB.rawQuery("SELECT BOOK_NUMBER, CHAPTER_NUMBER, VERSE_NUMBER\n" +
                "FROM VERSE WHERE ID IN\n" +
                "(SELECT DISTINCT VERSE_ID FROM INTERLINEAR_WORD WHERE STRONGS_WORD_ID =" + href + ");", null);

        int n = c.getCount();
        String length = Integer.toString(n);

        l.add(Collections.singletonList(length));

        if (n > 0){
            c.moveToFirst();
            while (!c.isAfterLast()){
                l.add(Arrays.asList(c.getString(0), c.getString(1), c.getString(2)));
                c.moveToNext();
            }
        }

        c.close();

        return l;

    }
}