package edu.upc.eetac.dsa.iarroyo.books;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.upc.eetac.dsa.iarroyo.books.api.Review;


import android.widget.BaseAdapter;


;

/**
 * Created by nacho on 2/05/15.
 */
public class ReviewAdapter extends BaseAdapter{


    /**
     * Created by nacho on 26/04/15.
     */


        ArrayList<Review> data;
        LayoutInflater inflater;

        public ReviewAdapter(Context context, ArrayList<Review> data) {
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
            return ((Review) getItem(position)).getReseñaid();
        }

        private static class ViewHolder {
            TextView tvResenaid;
            TextView tvTexto;
            TextView tvUltimaFechaHora;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_row_reviews, null);
                viewHolder = new ViewHolder();
                viewHolder.tvResenaid = (TextView) convertView
                        .findViewById(R.id.tvResenaid);
                viewHolder.tvTexto = (TextView) convertView
                        .findViewById(R.id.tvTexto);
                viewHolder.tvUltimaFechaHora = (TextView) convertView
                        .findViewById(R.id.tvUltimaFechaHora);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            int resenaid = data.get(position).getReseñaid();
            String texto = data.get(position).getTexto();
            String ultima_fecha_hora = SimpleDateFormat.getInstance().format(
                    data.get(position).getUltima_fecha_hora());
            viewHolder.tvResenaid.setId(resenaid);
            viewHolder.tvTexto.setText(texto);
            viewHolder.tvUltimaFechaHora.setText(ultima_fecha_hora);
            return convertView;
        }



 }


