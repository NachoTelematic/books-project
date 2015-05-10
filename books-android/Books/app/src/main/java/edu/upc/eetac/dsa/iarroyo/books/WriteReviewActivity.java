package edu.upc.eetac.dsa.iarroyo.books;

/**
 * Created by nacho on 3/05/15.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import edu.upc.eetac.dsa.iarroyo.books.api.AppException;
import edu.upc.eetac.dsa.iarroyo.books.api.BooksAPI;
import edu.upc.eetac.dsa.iarroyo.books.api.Review;

public class WriteReviewActivity extends Activity {
    private final static String TAG = WriteReviewActivity.class.getName();

    private class PostReviewTask extends AsyncTask<String, Void, Review> {
        private ProgressDialog pd;

        @Override
        protected Review doInBackground(String... params) {
            Review review = null;
            try {
                review = BooksAPI.getInstance(WriteReviewActivity.this)
                        .createReview(params[0]);
            } catch (AppException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return review;
        }

        @Override
        protected void onPostExecute(Review result) {
            showReviews(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(WriteReviewActivity.this);

            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_review_layout);

    }

    public void cancelar(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void publicar(View v) {
        EditText etText = (EditText) findViewById(R.id.etText);

        String texto = etText.getText().toString();


        (new PostReviewTask()).execute(texto);
    }

    private void showReviews(Review result) {
        String json = new Gson().toJson(result);
        Bundle data = new Bundle();
        data.putString("json-review", json);
        Intent intent = new Intent();
        intent.putExtras(data);
        setResult(RESULT_OK, intent);
        finish();
    }

}