package com.example.tanaj;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public abstract class Transliteration {

    public static Set<String> createTransliterations(){
        /*
        Positions
        1: Academic transliteration
        2: Study transliteration
        3: Spanish
        4: Basic spanish
        5: English
        6: Anglo English
        7: Custom
         */

        Set<String> setTl = new LinkedHashSet<>();

        setTl.add("1;·;·;;;;;;·");
        setTl.add("1;-;·;;;;;;-");
        setTl.add("1;’;’;;;;;;’");
        setTl.add("1;';';;;;;;'");
        setTl.add("1;’;’;;;;’;’;א");
        setTl.add("1;ḇ;v;v;v;v;v;v;ב");
        setTl.add("1;ḡ;g;g;g/gu;g;g;g;ג");
        setTl.add("1;g;g;g;g/gu;g;g;g;גּ");
        setTl.add("0;ḏ;d;d;d;d;d;d;ד");
        setTl.add("1;w;v;v;v;v;w;v;ו");
        setTl.add("1;W;V;V;V;V;W;V;ו");
        setTl.add("1;ḥ;j;j;j;h;ch;j;ח");
        setTl.add("1;Ḥ;J;J;J;H;Ch;J;ח");
        setTl.add("0;ṭ;t;t;t;t;t;t;ט");
        setTl.add("1;ḵ;kh;j;j;kh;ch;j;כ");
        setTl.add("1;‘;‘;;;;‘;‘;ע");
        setTl.add("1;p̄;f;f;f;f;ph;f;פ");
        setTl.add("1;ṣ;ts;tz;ts;tz;tz;tz;צ");
        setTl.add("1;Ṣ;Ts;Tz;Ts;Tz;Tz;Tz;צ");
        setTl.add("1;q;q;k;c/qu;k;k;k;ק");
        setTl.add("1;Q;Q;K;C/Qu;K;K;K;ק");
        setTl.add("0;ś;s;s;s;s;s;s;שׂ");
        setTl.add("0;š;sh;sh;sh;sh;sh;sh;שׁ");
        setTl.add("0;Š;Sh;Sh;Sh;Sh;Sh;Sh;שׁ");
        setTl.add("1;ṯ;t;t;t;t;th;t;ת");
        setTl.add("1;Ṯ;T;T;T;T;Th;T;ת");
        setTl.add("0;ā;a;a;a;a;a;a;Qamats");
        setTl.add("0;Ā;A;A;A;A;A;A;Qamats");
        setTl.add("0;ă;a;a;a;a;a;a;Jataf Pataj");
        setTl.add("0;ê;e;e;e;e;e;e;Tsere");
        setTl.add("0;Ê;E;E;E;E;E;E;Tsere");
        setTl.add("0;ĕ;e;e;e;e;e;e;Jataf Segol");
        setTl.add("0;Ĕ;E;E;E;E;E;E;Jataf Segol");
        setTl.add("0;ə;e;e;e;e;e;e;Shva");
        setTl.add("0;î;i;i;i;i;i;i;Jiriq Yod");
        setTl.add("0;Î;I;I;I;I;I;I;Jiriq Yod");
        setTl.add("0;ō;o;o;o;o;o;o;Jolam");
        setTl.add("0;Ō;O;O;O;O;O;O;Jolam");
        setTl.add("0;o̞;o;o;o;o;o;o;Qamats Qatan");
        setTl.add("0;ū;u;u;u;u;u;u;Shuruq");

        return setTl;
    }

    public static List<Integer>  getModifiables(List<String> equivalences){
        List<Integer> a = new ArrayList<>();
        int i;
        String s;
        for (i = 0; i < equivalences.size(); i++){
            s = equivalences.get(i);
            if(s.charAt(0) == '1'){
                a.add(i);
            }
        }
        return a;
    }

    public static String applyFormatSample (int position, String text, List<String> equivalences){
        String single = getSingleCharacters(text);
        if (position > 0){
            position += 1;
            text = applyTransformation(single, text, position, equivalences);
            // Especial spanish characters c/qu g/gu
            if (position == 4 || position == 7){
                text = specialSpReplace(text);
            }
            // Replace duplicate
            text = replaceDuplicates(text, position);
        }
        return text;
    }

    private static String replaceDuplicates(String text, int position) {
        // Replace dagguesh duplicates
        text = text.replaceAll("yy", "y");

        // Replace accents
        if(position == 4){
            text = text.replaceAll("a;","á");
            text = text.replaceAll("e;","é");
            text = text.replaceAll("i;","í");
            text = text.replaceAll("o;","ó");
            text = text.replaceAll("u;","ú");
        }else{
            text = text.replaceAll(";","");
        }

        if (position == 2 || position == 4){
            text = text.replaceAll("yi","i");
            text = text.replaceAll("Yi","I");
        }
        if (position == 4){
            text = text.replaceAll("bb", "b");
        }
        // General replacements
        text = text.replaceAll("kk", "k");
        text = text.replaceAll("gg", "g");
        text = text.replaceAll("shsh", "sh");
        text = text.replaceAll("cc", "c");
        text = text.replaceAll("ll", "l");
        text = text.replaceAll("tsts", "ts");
        text = text.replaceAll("tztz", "tz");
        text = text.replaceAll("tt", "t");
        text = text.replaceAll("mm", "m");
        text = text.replaceAll("nn", "n");
        text = text.replaceAll("ss", "s");
        text = text.replaceAll("vv", "v");
        text = text.replaceAll("ww", "w");
        text = text.replaceAll("dd", "d");
        text = text.replaceAll("zz", "z");
        text = text.replaceAll("pp", "p");
        text = text.replaceAll("cqu", "qu");

        return text;
    }

    private static String specialSpReplace(String text) {
        if (text.contains("g/gu")){
            text = text.replaceAll("g/gua", "ga");
            text = text.replaceAll("g/gue", "gue");
            text = text.replaceAll("g/gui", "gui");
            text = text.replaceAll("g/guo", "go");
            text = text.replaceAll("g/guu", "gu");
            text = text.replaceAll("g/gu", "g");
        }
        if (text.contains("c/qu")){
            text = text.replaceAll("c/qua","ca");
            text = text.replaceAll("c/que","que");
            text = text.replaceAll("c/qui","qui");
            text = text.replaceAll("c/quo","co");
            text = text.replaceAll("c/quu","cu");
            text = text.replaceAll("c/qu","c");

        }
        if (text.contains("C/Qu")){
            text = text.replaceAll("C/Qua","Ca");
            text = text.replaceAll("C/Que","Que");
            text = text.replaceAll("C/Qui","Qui");
            text = text.replaceAll("C/Quo","Co");
            text = text.replaceAll("C/Quu","Cu");
            text = text.replaceAll("C/Qu","C");

        }
        return text;
    }

    private static String applyTransformation(String single, String text, int position, List<String> equivalences) {
        String [] temp;
        int i = 0;
        for (String s : equivalences){
            temp = s.split(";");
            if(single.contains(temp[1])){
                text = text.replaceAll(temp[1], temp[position]);
            }
        }
        return text;
    }

    private static String getSingleCharacters(String text) {
        StringBuilder simple = new StringBuilder();
        String temp;
        for (int  i = 0; i < text.length(); i++){
            temp = String.valueOf(text.charAt(i));
            if (! simple.toString().contains(temp) ) {
                simple.append(temp);
            }
        }
        return simple.toString();
    }
}
