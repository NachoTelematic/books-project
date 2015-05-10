package edu.upc.eetac.dsa.iarroyo.books;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.ArrayList;

import edu.upc.eetac.dsa.iarroyo.books.api.AppException;
import edu.upc.eetac.dsa.iarroyo.books.api.Author;
import edu.upc.eetac.dsa.iarroyo.books.api.AuthorCollection;
import edu.upc.eetac.dsa.iarroyo.books.api.BooksAPI;
import edu.upc.eetac.dsa.iarroyo.books.api.Review;
import edu.upc.eetac.dsa.iarroyo.books.api.ReviewCollection;

/**
 * Created by nacho on 9/05/15.
 */
public class AuthorActivity extends ListActivity{

    private final static String TAG = AuthorActivity.class.toString();
    ArrayList<Author> authorList;
    private AuthorAdapter adapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);

        authorList = new ArrayList<Author>();
        adapter = new AuthorAdapter(this, authorList);
        setListAdapter(adapter);
        (new FetchReviewsTask()).execute();
    }

    private void addAuthors(AuthorCollection authors){
        authorList.addAll(authors.getAuthors());
        adapter.notifyDataSetChanged();

    }




    private class FetchReviewsTask extends
            AsyncTask<String, Void, AuthorCollection> {
        private ProgressDialog pd;

        @Override
        protected AuthorCollection doInBackground(String... params) {
            AuthorCollection authors = null;
            try {
                authors = BooksAPI.getInstance(AuthorActivity.this)
                        .getAuthors();


            } catch (AppException e) {
                e.printStackTrace();
            }
            return authors;
        }

        @Override
        protected void onPostExecute(AuthorCollection result) {
            addAuthors(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(AuthorActivity.this);
            pd.setTitle("Searching...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

    }



}
