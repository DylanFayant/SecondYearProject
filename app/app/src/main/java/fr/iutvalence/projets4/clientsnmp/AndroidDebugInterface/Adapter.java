package fr.iutvalence.projets4.clientsnmp.AndroidDebugInterface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import fr.iutvalence.projets4.clientsnmp.R;

/**
 * This class initialize a ListView with 2 table of string.
 * The first table of string contain title of data
 * and The second table of string contain data
 * Created by Thundermist on 10/12/16.
 */

public class Adapter extends BaseAdapter {

    Context context;
    String[] data;
    String[] title;
    private static LayoutInflater inflater = null;

    public Adapter(Context context,String[] title, String[] data) {
        this.context = context;
        this.data = data;
        this.title = title;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);
        //With position given in paramter, we get data and title of data in
        //the table of string "this.data" and "this.title" and set
        //this in the list view
        TextView title = (TextView) vi.findViewById(R.id.title);
        title.setText(this.title[position]);
        TextView data = (TextView) vi.findViewById(R.id.data);
        data.setText(this.data[position]);
        return vi;
    }

    public void update(String[] data)
    {
        this.data = data;
    }
}