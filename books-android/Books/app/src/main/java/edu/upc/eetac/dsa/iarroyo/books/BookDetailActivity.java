package edu.upc.eetac.dsa.iarroyo.books;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import edu.upc.eetac.dsa.iarroyo.books.api.AppException;
import edu.upc.eetac.dsa.iarroyo.books.api.BooksAPI;
import edu.upc.eetac.dsa.iarroyo.books.api.Libro;

/**
 * Created by nacho on 27/04/15.
 */
public class BookDetailActivity extends Activity {

    private final static String TAG = BookDetailActivity.class.getName();
    String urlBook  = null;
    String libroid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail_layout);
         urlBook = (String) getIntent().getExtras().get("url");
         String[] arrayUrlBook = urlBook.split("/");
         libroid = arrayUrlBook[5];
         System.out.println(libroid);

        (new FetchBookTask()).execute(urlBook);
    }


    private void loadBook(Libro book) {
        TextView tvDetailTitulo = (TextView) findViewById(R.id.tvDetailTitulo);
        TextView tvDetailLengua = (TextView) findViewById(R.id.tvDetailLengua);
        TextView tvDetailEdicion = (TextView) findViewById(R.id.tvDetailEdicion);
        TextView tvDetailFecha_edicion = (TextView) findViewById(R.id.tvDetailFecha_edicion);

        tvDetailTitulo.setText(book.getTitulo());
        tvDetailLengua.setText(book.getLengua());
        tvDetailEdicion.setText(book.getEdicion());
        tvDetailFecha_edicion.setText(SimpleDateFormat.getInstance().format(
                book.getFecha_edicion()));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_reviews:
                Intent intent = new Intent(this, ReviewActivity.class);

                intent.putExtra("libroid", libroid);

                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class FetchBookTask extends AsyncTask<String, Void, Libro> {
        private ProgressDialog pd;

        @Override
        protected Libro doInBackground(String... params) {
            Libro book = null;
            try {
                book = BooksAPI.getInstance(BookDetailActivity.this)
                        .getBook(params[0]);
            } catch (AppException e) {
                Log.d(TAG, e.getMessage(), e);
            }
            return book;
        }

        @Override
        protected void onPostExecute(Libro result) {
            loadBook(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(BookDetailActivity.this);
            pd.setTitle("Loading...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_books_main, menu);
        return true;
    }

}
