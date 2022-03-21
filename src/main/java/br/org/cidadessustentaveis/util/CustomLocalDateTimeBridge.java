package br.org.cidadessustentaveis.util;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateTimeBridge implements TwoWayFieldBridge {

    @Override
    public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
        LocalDateTime date = (LocalDateTime) value;

        if(date != null) {
            luceneOptions.addFieldToDocument(name, date.format(DateTimeFormatter.ofPattern("yyyyMMdd")), document);
        }
    }

    @Override
    public Object get(String name, Document document) {
        IndexableField strdate = document.getField(name);
        return LocalDateTime.parse(strdate.stringValue(), DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    @Override
    public String objectToString(Object date) {
        String str = (String) date;
        return str;
    }
}
