package ca.uoit.csci4100.samples.userinterfacessample;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Randy on 2015-09-17.
 */
public class LabelAdapter extends BaseAdapter {
    private Context context;
    private String[] data;

    public LabelAdapter(Context context, String[] data) {
        this.data = data;
        this.context = context;
    }

    public int getCount() {
        return data.length;
    }

    public Object getItem(int position) {
        return data[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;

        if (convertView == null) {
            // there is no object to reuse, create one
            textView = new TextView(context);
            textView.setText(data[position]);
        } else {
            // we're reusing an old object
            textView = (TextView)convertView;
        }

        return textView;
    }
}
