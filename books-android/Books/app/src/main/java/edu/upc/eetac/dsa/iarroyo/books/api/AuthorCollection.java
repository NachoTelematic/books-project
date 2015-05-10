package edu.upc.eetac.dsa.iarroyo.books.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nacho on 9/05/15.
 */
public class AuthorCollection {
    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    private List<Author> authors;


    public AuthorCollection() {
        super();
        authors = new ArrayList<>();
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }


}
