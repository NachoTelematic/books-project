package edu.upc.eetac.dsa.iarroyo.books.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;

import edu.upc.eetac.dsa.iarroyo.books.api.BookResource;
import edu.upc.eetac.dsa.iarroyo.books.api.MediaType;

public class LibroCollection {
	@InjectLinks({
		@InjectLink(resource = BookResource.class, style = Style.ABSOLUTE, rel = "create-libros", title = "Create libro", type = MediaType.BOOKS_API_BOOK),
		@InjectLink(value = "/books/reviews", style = Style.ABSOLUTE, rel = "reviews", title = "Show all reviews", type = MediaType.BOOKS_API_BOOK),
		@InjectLink(value = "/books?before={before}", style = Style.ABSOLUTE, rel = "previous", title = "Previous llibros", type = MediaType.BOOKS_API_BOOK_COLLECTION, bindings = { @Binding(name = "before", value = "${instance.oldestTimestamp}") }),//$-->toda {}--> valor deseado
		@InjectLink(value = "/books?after={after}", style = Style.ABSOLUTE, rel = "current", title = "Newest libros", type = MediaType.BOOKS_API_BOOK_COLLECTION, bindings = { @Binding(name = "after", value = "${instance.newestTimestamp}") }) })
	private List<Link> links;
	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	private List<Libro> books;
	public long getNewestTimestamp() {
		return newestTimestamp;
	}

	public void setNewestTimestamp(long newestTimestamp) {
		this.newestTimestamp = newestTimestamp;
	}

	public long getOldestTimestamp() {
		return oldestTimestamp;
	}

	public void setOldestTimestamp(long oldestTimestamp) {
		this.oldestTimestamp = oldestTimestamp;
	}

	private long newestTimestamp;
	private long oldestTimestamp;
	
	 
	public LibroCollection() {
	super();
	books = new ArrayList<>();
	}
	 
	public List<Libro> getBooks() {
	return books;
	}
	 
	public void setBooks(List<Libro> books) {
	this.books = books;
	}
	 
	public void addBook(Libro libro) {
	books.add(libro);
	}

	public Libro getLibro(int libroid) {
		for (int i = 0; i < books.size(); i++) {
			if (books.get(i).getId() == libroid) {
				return books.get(i);
			}
		}
		return null;
	}

	public void addBooks(Libro book){
		books.add(book);
		}
}
