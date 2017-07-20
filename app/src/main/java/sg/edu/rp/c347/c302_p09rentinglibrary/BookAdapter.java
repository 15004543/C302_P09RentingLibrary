package sg.edu.rp.c347.c302_p09rentinglibrary;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 15004543 on 18/7/2017.
 */

public class BookAdapter extends ArrayAdapter<Books> {
    Context context;
    int layoutResourceId;
    ArrayList<Books> bookList = null;


    public BookAdapter(Context context, int resource, ArrayList<Books> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResourceId = resource;
        this.bookList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BookAdapter.BookHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new BookAdapter.BookHolder();
            holder.name = (TextView)row.findViewById(R.id.tvBookName);

            row.setTag(holder);
        }
        else
        {
            holder = (BookAdapter.BookHolder) row.getTag();
        }

        Books books = bookList.get(position);
        holder.name.setText(books.getTitle());
        return row;
    }

    static class BookHolder
    {
        TextView name;
    }
}
