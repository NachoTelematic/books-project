package edu.upc.eetac.dsa.iarroyo.books;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.upc.eetac.dsa.iarroyo.books.api.Libro;

/**
 * Created by nacho on 26/04/15.
 */
public class BookAdapter extends BaseAdapter{

    ArrayList<Libro> data;
    LayoutInflater inflater;

    public BookAdapter(Context context, ArrayList<Libro> data) {
        super();
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((Libro) getItem(position)).getId();
    }

    private static class ViewHolder {
        TextView tvTitulo;
        TextView tvLengua;
        TextView tvEdicion;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row_books, null);
            viewHolder = new ViewHolder();
            viewHolder.tvTitulo = (TextView) convertView
                    .findViewById(R.id.tvTitulo);
            viewHolder.tvLengua = (TextView) convertView
                    .findViewById(R.id.tvLengua);
            viewHolder.tvEdicion = (TextView) convertView
                    .findViewById(R.id.tvEdicion);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String titulo = data.get(position).getTitulo();
        String lengua = data.get(position).getLengua();
        String edicion = data.get(position).getEdicion();
        viewHolder.tvTitulo.setText(titulo);
        viewHolder.tvLengua.setText(lengua);
        viewHolder.tvEdicion.setText(edicion);
        return convertView;
    }



}
