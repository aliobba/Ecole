package com.example.meyss.monecole.Activities.EspaceAdmin;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meyss.monecole.R;

import org.w3c.dom.Text;

class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private final String[] values;
    private final int[] images;
    View view;
    LayoutInflater layoutInflater;


    public ImageAdapter(Context mContext, String[] values, int[] images) {
        this.mContext = mContext;
        this.values = values;
        this.images = images;
    }

    public int getCount() {
        return values.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            view = new View(mContext);
            view = layoutInflater.inflate(R.layout.grid_simple_item,null);
            ImageView image = (ImageView) view.findViewById(R.id.gridImg);
            TextView texte = (TextView) view .findViewById(R.id.gridText);
            image.setImageResource(images[position]);
            texte.setText(values[position]);
        }


        return view;
    }


}
