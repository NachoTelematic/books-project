package edu.upc.eetac.dsa.iarroyo.books;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;

import edu.upc.eetac.dsa.iarroyo.books.api.AppException;
import edu.upc.eetac.dsa.iarroyo.books.api.BooksAPI;
import edu.upc.eetac.dsa.iarroyo.books.api.Libro;
import edu.upc.eetac.dsa.iarroyo.books.api.Review;

/**
 * Created by nacho on 5/05/15.
 */
public class SearchTitleActivity extends Activity {


    private final static String TAG = SearchTitleActivity.class.getName();

    private class FetchTitleTask extends AsyncTask<String, Void, Libro> {
        private ProgressDialog pd;

        @Override
        protected Libro doInBackground(String... params) {
            Libro libro = null;
            try {
                libro = BooksAPI.getInstance(SearchTitleActivity.this)
                        .getBookByTitle(params[0]);
            } catch (AppException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return libro;
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
            pd = new ProgressDialog(SearchTitleActivity.this);

            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_title_layout);



    }

    public void cancelar(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void buscar(View v) {
        EditText etText = (EditText) findViewById(R.id.etText);

        String titulo = etText.getText().toString();


        (new FetchTitleTask()).execute(titulo);
    }



    private void loadBook(Libro book) {
        setContentView(R.layout.book_buscado);
        TextView tvDetailTitulo = (TextView) findViewById(R.id.tvDetailTitulo);
        TextView tvDetailLengua = (TextView) findViewById(R.id.tvDetailLengua);
        TextView tvDetailEdicion = (TextView) findViewById(R.id.tvDetailEdicion);
        TextView tvDetailFecha_edicion = (TextView) findViewById(R.id.tvDetailFecha_edicion);
        System.out.println("El titulo es el siguiente: " + book.getTitulo());
        tvDetailTitulo.setText(book.getTitulo());
        tvDetailLengua.setText(book.getLengua());
        tvDetailEdicion.setText(book.getEdicion());
        tvDetailFecha_edicion.setText(SimpleDateFormat.getInstance().format(
                book.getFecha_edicion()));

    }


}
