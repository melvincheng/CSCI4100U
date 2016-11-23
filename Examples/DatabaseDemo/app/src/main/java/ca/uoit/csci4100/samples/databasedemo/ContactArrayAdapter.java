package ca.uoit.csci4100.samples.databasedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ContactArrayAdapter extends BaseAdapter {
    private Context context = null;
    private List<Contact> contacts = null;

    public ContactArrayAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = contacts.get(position);

        if (convertView == null) {
            // no previous view:  create a new view, based on our custom list item layout
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contact_list_item, parent, false);
        } else {
            // previous view exists:  let's reuse it
        }

        // populate the UI elements with our data
        TextView lblName = (TextView)convertView.findViewById(R.id.txtName);
        lblName.setText(contact.getFirstName() + " " + contact.getLastName());

        TextView lblEmail = (TextView)convertView.findViewById(R.id.txtEmail);
        lblEmail.setText(contact.getEmail());

        TextView lblPhone = (TextView)convertView.findViewById(R.id.txtPhone);
        lblPhone.setText(contact.getPhone());

        return convertView;
    }
}
