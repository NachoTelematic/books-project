package edu.upc.eetac.dsa.iarroyo.books.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;

import edu.upc.eetac.dsa.iarroyo.books.api.BooksRootAPIResource;
import edu.upc.eetac.dsa.iarroyo.books.api.MediaType;
import edu.upc.eetac.dsa.iarroyo.books.api.BookResource;

public class BooksRootAPI {
	@InjectLinks({
			@InjectLink(resource = BooksRootAPIResource.class, style = Style.ABSOLUTE, rel = "self bookmark home", title = "Books Root API", method = "getRootAPI"),
	 		@InjectLink(value = "/books/reviews", style = Style.ABSOLUTE, rel = "reviews", title = "Show all reviews", type = MediaType.REVIEWS_API_REVIEW_COLLECTION),
			@InjectLink(resource = BookResource.class, style = Style.ABSOLUTE, rel = "books", title = "Latest books", type = MediaType.BOOKS_API_BOOK_COLLECTION),
			@InjectLink(resource = BookResource.class, style = Style.ABSOLUTE, rel = "create-book", title = "Create new book", type = MediaType.BOOKS_API_BOOK) })
	private List<Link> links;

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
}
