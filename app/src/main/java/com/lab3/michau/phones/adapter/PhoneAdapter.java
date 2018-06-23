package com.lab3.michau.phones.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lab3.michau.phones.AddActivity;
import com.lab3.michau.phones.R;
import com.lab3.michau.phones.domain.Phone;

import java.util.List;

public class PhoneAdapter extends ArrayAdapter<Phone> {
    private List<Phone> phones;
    private Activity activity;

    public PhoneAdapter(@NonNull Activity activity, List<Phone> phones) {
        super(activity, R.layout.phones_row, phones);
        this.phones = phones;
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.phones_row, null);

        LinearLayout layout = convertView.findViewById(R.id.row);
//        layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(activity.getApplicationContext(), AddActivity.class);
//                intent.putExtra("phone", phones.get(position));
//                activity.startActivity(intent);
//            }
//        });

        TextView model = convertView.findViewById(R.id.model);
        TextView producent = convertView.findViewById(R.id.producent);
        model.setText(phones.get(position).getModel());
        producent.setText(phones.get(position).getManufacturer());

        return convertView;
    }
}
