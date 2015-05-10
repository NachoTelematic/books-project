package edu.upc.eetac.dsa.iarroyo.books;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;

import edu.upc.eetac.dsa.iarroyo.books.api.AppException;
import edu.upc.eetac.dsa.iarroyo.books.api.BooksAPI;
import edu.upc.eetac.dsa.iarroyo.books.api.Libro;
import edu.upc.eetac.dsa.iarroyo.books.api.LibroCollection;
import edu.upc.eetac.dsa.iarroyo.books.api.Review;

public class BooksMainActivity extends ListActivity {




        private final static String TAG = BooksMainActivity.class.toString();
        ArrayList<Libro> booksList;
        private BookAdapter adapter;

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_books_main);

            booksList = new ArrayList<Libro>();
            adapter = new BookAdapter(this, booksList);
            setListAdapter(adapter);

            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("admin", "admin"
                            .toCharArray());
                }
            });
            (new FetchBooksTask()).execute();
        }


    private void addBooks(LibroCollection books){
        booksList.addAll(books.getBooks());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Libro book = booksList.get(position);
        Log.d(TAG, book.getLinks().get("self").getTarget());

        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra("url", book.getLinks().get("self").getTarget());
        startActivity(intent);
    }


    private class FetchBooksTask extends
            AsyncTask<Void, Void, LibroCollection> {
        private ProgressDialog pd;

        @Override
        protected LibroCollection doInBackground(Void... params) {
            LibroCollection books = null;
            try {
                books = BooksAPI.getInstance(BooksMainActivity.this)
                        .getBooks();
            } catch (AppException e) {
                e.printStackTrace();
            }
            return books;
        }

        @Override
        protected void onPostExecute(LibroCollection result) {
            addBooks(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(BooksMainActivity.this);
            pd.setTitle("Searching...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_books_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.buscardorTitulo:
                Intent intent = new Intent(this, SearchTitleActivity.class);
                startActivityForResult(intent, WRITE_ACTIVITY);

                return true;
            case R.id.autor:
                Intent intent1 = new Intent(this, AuthorActivity.class);
                startActivity(intent1);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private final static int WRITE_ACTIVITY = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case WRITE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    String jsonBook = res.getString("json-book");
                    Libro book = new Gson().fromJson(jsonBook, Libro.class);
                    booksList.add(0, book);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }


}

