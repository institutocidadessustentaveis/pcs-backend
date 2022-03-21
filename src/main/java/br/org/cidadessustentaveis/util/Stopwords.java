package br.org.cidadessustentaveis.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Stopwords {

    private Stopwords() {

    }

    public static List<String> getStopwords() throws IOException {
        List<String> stopwords = new LinkedList<>();

        File file = new File("stopwords.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null) {
            stopwords.add(st.trim().toLowerCase());
        }

        return stopwords;
    }

}
