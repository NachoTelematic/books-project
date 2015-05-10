package edu.upc.eetac.dsa.iarroyo.books;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;

import edu.upc.eetac.dsa.iarroyo.books.api.AppException;
import edu.upc.eetac.dsa.iarroyo.books.api.BooksAPI;
import edu.upc.eetac.dsa.iarroyo.books.api.Libro;
import edu.upc.eetac.dsa.iarroyo.books.api.Review;
import edu.upc.eetac.dsa.iarroyo.books.api.ReviewCollection;

/**
 * Created by nacho on 27/04/15.
 */
public class ReviewActivity extends ListActivity {

    private final static String TAG = ReviewActivity.class.toString();
    ArrayList<Review> reviewList;
    private ReviewAdapter adapter;
    String libroid;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        libroid = (String) getIntent().getExtras().get("libroid");

        reviewList = new ArrayList<Review>();
        adapter = new ReviewAdapter(this, reviewList);
        setListAdapter(adapter);
        (new FetchReviewsTask()).execute(libroid);
    }

    private void addReviews(ReviewCollection reviews){
        reviewList.addAll(reviews.getReviews());
        adapter.notifyDataSetChanged();

    }




    private class FetchReviewsTask extends
            AsyncTask<String, Void, ReviewCollection> {
        private ProgressDialog pd;

        @Override
        protected ReviewCollection doInBackground(String... params) {
            ReviewCollection reviews = null;
            try {
                reviews = BooksAPI.getInstance(ReviewActivity.this)
                        .getReview(params[0]);


            } catch (AppException e) {
                e.printStackTrace();
            }
            return reviews;
        }

        @Override
        protected void onPostExecute(ReviewCollection result) {
            addReviews(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(ReviewActivity.this);
            pd.setTitle("Searching...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_reviews, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miWrite:
                Intent intent = new Intent(this, WriteReviewActivity.class);
                startActivityForResult(intent, WRITE_ACTIVITY);
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
                    String jsonReview = res.getString("json-review");
                    Review review = new Gson().fromJson(jsonReview, Review.class);
                    reviewList.add(0, review);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }


}
