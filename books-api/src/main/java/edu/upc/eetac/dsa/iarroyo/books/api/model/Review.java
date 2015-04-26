package edu.upc.eetac.dsa.iarroyo.books.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;

import edu.upc.eetac.dsa.iarroyo.books.api.MediaType;

public class Review {

	@InjectLinks({
		@InjectLink(value = "/books/reviews/{libroid}", style = Style.ABSOLUTE, rel = "focus", title = "Reviews Libro", type = MediaType.REVIEWS_API_REVIEW, bindings = { @Binding(name = "libroid", value = "${instance.libroid}") }) })
	private List<Link> links;
	

	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	private int reseñaid;
	private int libroid;
	private String username;
	private String name;
	private long ultima_fecha_hora;
	private String texto;
	
	public long getUltima_fecha_hora() {
		return ultima_fecha_hora;
	}
	public void setUltima_fecha_hora(long ultima_fecha_hora) {
		this.ultima_fecha_hora = ultima_fecha_hora;
	}

	
	public int getReseñaid() {
		return reseñaid;
	}
	public void setReseñaid(int reseñaid) {
		this.reseñaid = reseñaid;
	}
	public int getLibroid() {
		return libroid;
	}
	public void setLibroid(int libroid) {
		this.libroid = libroid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	
}
