package edu.upc.eetac.dsa.iarroyo.books.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nacho on 25/04/15.
 */
public class LibroCollection {

    private List<Libro> books;

    public long getNewestTimestamp() {
        return newestTimestamp;
    }

    public void setNewestTimestamp(long newestTimestamp) {
        this.newestTimestamp = newestTimestamp;
    }

    public List<Libro> getBooks() {
        return books;
    }

    public void setBooks(List<Libro> books) {
        this.books = books;
    }

    public long getOldestTimestamp() {
        return oldestTimestamp;
    }

    public void setOldestTimestamp(long oldestTimestamp) {
        this.oldestTimestamp = oldestTimestamp;
    }

    public Map<String, Link> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }

    private long newestTimestamp;
    private long oldestTimestamp;
    private Map<String, Link> links = new HashMap<String, Link>();

    public LibroCollection() {
        super();
        books = new ArrayList<>();
    }

    public void addBook(Libro book) {
        books.add(book);
    }


}
