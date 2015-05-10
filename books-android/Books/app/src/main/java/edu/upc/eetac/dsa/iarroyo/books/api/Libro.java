package edu.upc.eetac.dsa.iarroyo.books.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nacho on 25/04/15.
 */
public class Libro {

    private int id;
    private String titulo;
    private String lengua;
    private String edicion;

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    private String eTag;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLengua() {
        return lengua;
    }

    public void setLengua(String lengua) {
        this.lengua = lengua;
    }

    public String getEdicion() {
        return edicion;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }

    public long getFecha_edicion() {
        return fecha_edicion;
    }

    public void setFecha_edicion(long fecha_edicion) {
        this.fecha_edicion = fecha_edicion;
    }

    public long getFecha_impresion() {
        return fecha_impresion;
    }

    public void setFecha_impresion(long fecha_impresion) {
        this.fecha_impresion = fecha_impresion;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Map<String, Link> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }

    private long fecha_edicion;
    private long fecha_impresion;

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    private long lastModified;
    private String editorial;
    private List<Review> reviews;
    private Map<String, Link> links = new HashMap<String, Link>();


}
