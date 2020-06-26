package com.alice.alicephonebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alice.alicephonebook.R;
import com.alice.alicephonebook.bean.Contact;

import java.util.List;

public class ContactListAdapter extends ArrayAdapter<Contact> {

    private int resourceId;

    public ContactListAdapter(Context context, int resource, List<Contact> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);//获取当前项的实例
        final View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        TextView tvContactName = view.findViewById(R.id.tvContactName);
        TextView tvContactPhone =view.findViewById(R.id.tvContactPhone);

        tvContactName.setText(contact.getName());
        tvContactPhone.setText(contact.getPhone());

        return view;
    }
}
