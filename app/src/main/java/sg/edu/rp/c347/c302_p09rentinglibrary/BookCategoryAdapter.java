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

public class BookCategoryAdapter extends ArrayAdapter<BookCategory> {
    Context context;
    int layoutResourceId;
    ArrayList<BookCategory> categoryList = null;


    public BookCategoryAdapter(Context context, int resource, ArrayList<BookCategory> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResourceId = resource;
        this.categoryList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CategoryHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new CategoryHolder();
            holder.name = (TextView)row.findViewById(R.id.tvName);

            row.setTag(holder);
        }
        else
        {
            holder = (CategoryHolder) row.getTag();
        }

        BookCategory category = categoryList.get(position);
        holder.name.setText(category.getName());
        return row;
    }

    static class CategoryHolder
    {
        TextView name;
    }
}
