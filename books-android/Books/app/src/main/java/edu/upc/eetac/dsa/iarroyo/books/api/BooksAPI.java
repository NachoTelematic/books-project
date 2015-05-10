package edu.upc.eetac.dsa.iarroyo.books.api;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by nacho on 25/04/15.
 */
public class BooksAPI {

    private final static String TAG = BooksAPI.class.getName();
    private static BooksAPI instance = null;
    private URL url;
    private String id= null;

    private BooksRootAPI rootAPI = null;

    private BooksAPI(Context context) throws IOException, AppException {
        super();

        AssetManager assetManager = context.getAssets();
        Properties config = new Properties();
        config.load(assetManager.open("config.properties"));
        String urlHome = config.getProperty("books.home");
        url = new URL(urlHome);

        Log.d("LINKS", url.toString());
        getRootAPI();
    }

    public final static BooksAPI getInstance(Context context) throws AppException {
        if (instance == null)
            try {
                instance = new BooksAPI(context);
            } catch (IOException e) {
                throw new AppException(
                        "Can't load configuration file");
            }
        return instance;
    }

    private void getRootAPI() throws AppException {
        Log.d(TAG, "getRootAPI()");
        rootAPI = new BooksRootAPI();
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            throw new AppException(
                    "Can't connect to Books API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, rootAPI.getLinks());
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Books API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Book Root API");
        }

    }

    public LibroCollection getBooks() throws AppException {
        Log.d(TAG, "getBooks()");
        LibroCollection books = new LibroCollection();

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
                    .get("books").getTarget()).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            throw new AppException(
                    "Can't connect to Books API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, books.getLinks());



            books.setNewestTimestamp(jsonObject.getLong("newestTimestamp"));
            books.setOldestTimestamp(jsonObject.getLong("oldestTimestamp"));
            JSONArray jsonBooks = jsonObject.getJSONArray("books");
            for (int i = 0; i < jsonBooks.length(); i++) {
                Libro book = new Libro();
                JSONObject jsonBook = jsonBooks.getJSONObject(i);
                book.setId(jsonBook.getInt("id"));
                book.setTitulo(jsonBook.getString("titulo"));
                book.setLengua(jsonBook.getString("lengua"));
                book.setEdicion(jsonBook.getString("edicion"));
                book.setFecha_edicion(jsonBook.getLong("fecha_edicion"));
                book.setFecha_impresion(jsonBook.getLong("fecha_impresion"));
                book.setEditorial(jsonBook.getString("editorial"));
                book.setLastModified(jsonBook.getLong("lastModified"));
                jsonLinks = jsonBook.getJSONArray("links");
                parseLinks(jsonLinks, book.getLinks());
                books.getBooks().add(book);
            }
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Books API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Books Root API");
        }

        return books;
    }



    public Libro getBook(String urlBook) throws AppException {
        Libro book = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlBook);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            book = booksCache.get(urlBook);
            String eTag = (book == null) ? null : book.geteTag();
            if (eTag != null)
                urlConnection.setRequestProperty("If-None-Match", eTag);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                Log.d(TAG, "CACHE");
                return booksCache.get(urlBook);
            }
            Log.d(TAG, "NOT IN CACHE");
            book = new Libro();
            eTag = urlConnection.getHeaderField("ETag");
            book.seteTag(eTag);
            booksCache.put(urlBook, book);

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject jsonBook = new JSONObject(sb.toString());
            book.setId(jsonBook.getInt("id"));
            book.setTitulo(jsonBook.getString("titulo"));
            book.setLengua(jsonBook.getString("lengua"));
            book.setEdicion(jsonBook.getString("edicion"));
            book.setFecha_edicion(jsonBook.getLong("fecha_edicion"));
            book.setFecha_impresion(jsonBook.getLong("fecha_impresion"));
            book.setEditorial(jsonBook.getString("editorial"));
            book.setLastModified(jsonBook.getLong("lastModified"));
            JSONArray jsonLinks = jsonBook.getJSONArray("links");
            parseLinks(jsonLinks, book.getLinks());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Bad book url");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Exception when getting the book");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Exception parsing response");
        }

        return book;
    }


/*
    public ReviewCollection getReviews() throws AppException {
        Log.d(TAG, "getReviews()");
        ReviewCollection reviews = new ReviewCollection();

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
                    .get("reviews").getTarget()).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            throw new AppException(
                    "Can't connect to Books API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, reviews.getLinks());
            JSONArray jsonReviews = jsonObject.getJSONArray("reviews");


            for (int i = 0; i < jsonReviews.length(); i++) {
                Review review = new Review();

                JSONObject jsonReview = jsonReviews.getJSONObject(i);
                review.setLibroid(jsonReview.getInt("libroid"));
                review.setReseñaid(jsonReview.getInt("reseñaid"));
                review.setUsername(jsonReview.getString("username"));
                review.setName(jsonReview.getString("name"));
                review.setUltima_fecha_hora(jsonReview.getLong("ultima_fecha_hora"));
                review.setTexto(jsonReview.getString("texto"));
                jsonLinks = jsonReview.getJSONArray("links");
                parseLinks(jsonLinks, review.getLinks());
                reviews.getReviews().add(review);
            }
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Books API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Books Root API");
        }

        return reviews;
    }

    */
    public ReviewCollection getReview(String libroid) throws AppException {
        ReviewCollection reviews = new ReviewCollection();


        HttpURLConnection urlConnection = null;
        try {

            String preURL = rootAPI.getLinks().get("books").getTarget();
            String URL = preURL + "/reviews/" + libroid;
            id = libroid;
            System.out.println(URL);
            urlConnection = (HttpURLConnection) new URL(URL).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            throw new AppException(
                    "Can't connect to Books API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, reviews.getLinks());
            JSONArray jsonReviews = jsonObject.getJSONArray("reviews");


            for (int i = 0; i < jsonReviews.length(); i++) {
                Review review = new Review();

                JSONObject jsonReview = jsonReviews.getJSONObject(i);
                review.setLibroid(jsonReview.getInt("libroid"));
                review.setReseñaid(jsonReview.getInt("reseñaid"));
                review.setUsername(jsonReview.getString("username"));
                review.setName(jsonReview.getString("name"));
                review.setUltima_fecha_hora(jsonReview.getLong("ultima_fecha_hora"));
                review.setTexto(jsonReview.getString("texto"));
                jsonLinks = jsonReview.getJSONArray("links");
                parseLinks(jsonLinks, review.getLinks());
                reviews.getReviews().add(review);
            }
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Books API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Books Root API");
        }

        return reviews;
    }

    private void parseLinks(JSONArray jsonLinks, Map<String, Link> map)
            throws AppException, JSONException {
        for (int i = 0; i < jsonLinks.length(); i++) {
            Link link = null;
            try {
                link = SimpleLinkHeaderParser
                        .parseLink(jsonLinks.getString(i));
            } catch (Exception e) {
                throw new AppException(e.getMessage());
            }
            String rel = link.getParameters().get("rel");
            String rels[] = rel.split("\\s");
            for (String s : rels)
                map.put(s, link);
        }
    }



    public Review createReview(String texto) throws AppException {
        Review review = new Review();

        review.setLibroid(Integer.parseInt(id));
        review.setUsername("test");
        review.setName("Test");
        review.setTexto(texto);


        HttpURLConnection urlConnection = null;
        try {

            JSONObject jsonReview = createJsonReview(review);
            String preURL = rootAPI.getLinks().get("books").getTarget();
            String URL = preURL + "/reviews/" + id;
            URL urlPostReviews = new URL(URL);

            urlConnection = (HttpURLConnection) urlPostReviews.openConnection();
            urlConnection.setRequestProperty("Accept",
                    MediaType.REVIEWS_API_REVIEW);
            urlConnection.setRequestProperty("Content-Type",
                    MediaType.REVIEWS_API_REVIEW);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            PrintWriter writer = new PrintWriter(
                    urlConnection.getOutputStream());
            writer.println(jsonReview.toString());
            writer.flush();
            //writer.close();
            int rc = urlConnection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            jsonReview = new JSONObject(sb.toString());

            review.setLibroid(jsonReview.getInt("libroid"));
            review.setReseñaid(jsonReview.getInt("reseñaid"));
            review.setUsername(jsonReview.getString("username"));
            review.setName(jsonReview.getString("name"));
            review.setUltima_fecha_hora(jsonReview.getLong("ultima_fecha_hora"));
            review.setTexto(jsonReview.getString("texto"));
            JSONArray jsonLinks = jsonReview.getJSONArray("links");
            parseLinks(jsonLinks, review.getLinks());

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Error parsing response");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Error getting response");
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return review;
    }

    private JSONObject createJsonReview(Review review) throws JSONException {
        JSONObject jsonReview = new JSONObject();
        jsonReview.put("libroid",review.getLibroid());
        jsonReview.put("username",review.getUsername());
        jsonReview.put("name",review.getName());
        jsonReview.put("texto", review.getTexto());


        return jsonReview;
    }


    private Map<String, Libro> booksCache = new HashMap<String, Libro>();

    public Libro getBookByTitle(String titulo) throws AppException {
        Libro book = null;
        LibroCollection books = new LibroCollection();


        HttpURLConnection urlConnection = null;
        try {

            String preURL = rootAPI.getLinks().get("books").getTarget();

                String URL = preURL + "/search?titulo=" + titulo;



            URL url = new URL(URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            book = booksCache.get(URL);
            String eTag = (book == null) ? null : book.geteTag();
            if (eTag != null)
                urlConnection.setRequestProperty("If-None-Match", eTag);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                Log.d(TAG, "CACHE");
                return booksCache.get(URL);
            }
            Log.d(TAG, "NOT IN CACHE");
            book = new Libro();
            eTag = urlConnection.getHeaderField("ETag");
            book.seteTag(eTag);
            booksCache.put(URL, book);

            int rc = urlConnection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, books.getLinks());



            books.setNewestTimestamp(jsonObject.getLong("newestTimestamp"));
            books.setOldestTimestamp(jsonObject.getLong("oldestTimestamp"));
            JSONArray jsonBooks = jsonObject.getJSONArray("books");
            for (int i = 0; i < jsonBooks.length(); i++) {

                JSONObject jsonBook = jsonBooks.getJSONObject(i);
                book.setId(jsonBook.getInt("id"));
                book.setTitulo(jsonBook.getString("titulo"));
                book.setLengua(jsonBook.getString("lengua"));
                book.setEdicion(jsonBook.getString("edicion"));
                book.setFecha_edicion(jsonBook.getLong("fecha_edicion"));
                book.setFecha_impresion(jsonBook.getLong("fecha_impresion"));
                book.setEditorial(jsonBook.getString("editorial"));
                book.setLastModified(jsonBook.getLong("lastModified"));
                jsonLinks = jsonBook.getJSONArray("links");
                parseLinks(jsonLinks, book.getLinks());
                books.getBooks().add(book);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Bad book url");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Exception when getting the book");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Exception parsing response");
        }

        return book;



    }


    public AuthorCollection getAuthors() throws AppException {
        Log.d(TAG, "getAuthors()");
        AuthorCollection authors = new AuthorCollection();

        HttpURLConnection urlConnection = null;
        try {
            String preurlAuthors = rootAPI.getLinks()
                    .get("self").getTarget();
            String urlAuthors = preurlAuthors + "authors";
            URL urlAuthorstotal = new URL(urlAuthors);
            urlConnection = (HttpURLConnection) (urlAuthorstotal.openConnection());
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            throw new AppException(
                    "Can't connect to Books API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());

            JSONArray jsonAuthors = jsonObject.getJSONArray("authors");
            for (int i = 0; i < jsonAuthors.length(); i++) {
                Author author = new Author();
                JSONObject jsonAuthor = jsonAuthors.getJSONObject(i);
                author.setAid(jsonAuthor.getInt("aid"));
                author.setNombre(jsonAuthor.getString("nombre"));
                authors.getAuthors().add(author);


            }
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Books API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Books Root API");
        }

        return authors;
    }



}
