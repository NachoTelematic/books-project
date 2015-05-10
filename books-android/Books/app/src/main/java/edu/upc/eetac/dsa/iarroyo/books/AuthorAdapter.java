package edu.upc.eetac.dsa.iarroyo.books;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.upc.eetac.dsa.iarroyo.books.api.Author;
import edu.upc.eetac.dsa.iarroyo.books.api.Review;

/**
 * Created by nacho on 9/05/15.
 */
public class AuthorAdapter extends BaseAdapter{

    ArrayList<Author> data;
    LayoutInflater inflater;

    public AuthorAdapter(Context context, ArrayList<Author> data) {
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
        return ((Author) getItem(position)).getAid();
    }

    private static class ViewHolder {
        TextView tvNombre;
        TextView tvAid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row_authors, null);
            viewHolder = new ViewHolder();
            viewHolder.tvNombre = (TextView) convertView
                    .findViewById(R.id.tvNombre);
            viewHolder.tvAid = (TextView) convertView
                    .findViewById(R.id.tvAid);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String nombre = data.get(position).getNombre();
        int aid = data.get(position).getAid();

        viewHolder.tvNombre.setText(nombre);
        viewHolder.tvAid.setId(aid);

        return convertView;
    }



}
