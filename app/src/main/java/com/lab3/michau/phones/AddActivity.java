package com.lab3.michau.phones;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lab3.michau.phones.domain.Phone;
import com.lab3.michau.phones.helper.DBHelper;
import com.lab3.michau.phones.provider.PhoneContentProvider;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        setAddButton();

        Button wwwButton = findViewById(R.id.www_button);
        wwwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText www = findViewById(R.id.www);
                Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                intent.putExtra("url", www.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void setAddButton() {
        Button addButton = findViewById(R.id.add_button);
        final Phone phone = (Phone) getIntent().getSerializableExtra("phone");
        if(phone != null) {
            EditText model = findViewById(R.id.model);
            EditText producent = findViewById(R.id.producent);
            EditText androidVer = findViewById(R.id.android);
            EditText www = findViewById(R.id.www);
            model.setText(phone.getModel());
            producent.setText(phone.getManufacturer());
            androidVer.setText(phone.getAndroidVer());
            www.setText(phone.getWww());
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phone == null) {
                    addPhone();
                } else {
                    updatePhone(phone);
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addPhone() {
        ContentValues contentValues = new ContentValues();
        EditText model = findViewById(R.id.model);
        EditText producent = findViewById(R.id.producent);
        EditText androidVer = findViewById(R.id.android);
        EditText www = findViewById(R.id.www);
        if (!TextUtils.isEmpty(model.getText()) && !TextUtils.isEmpty(producent.getText())) {
            contentValues.put(DBHelper.KOLUMNA1, model.getText().toString());
            contentValues.put(DBHelper.KOLUMNA2, producent.getText().toString());
            contentValues.put(DBHelper.KOLUMNA3, androidVer.getText().toString());
            contentValues.put(DBHelper.KOLUMNA4, www.getText().toString());
            getContentResolver().insert(PhoneContentProvider.URI, contentValues);
        }
    }

    private void updatePhone(Phone phone) {
        EditText model = findViewById(R.id.model);
        EditText producent = findViewById(R.id.producent);
        EditText androidVer = findViewById(R.id.android);
        EditText www = findViewById(R.id.www);
        ContentValues contentValues = new ContentValues();
        if (!TextUtils.isEmpty(model.getText()) && !TextUtils.isEmpty(producent.getText())) {
            contentValues.put(DBHelper.KOLUMNA1, model.getText().toString());
            contentValues.put(DBHelper.KOLUMNA2, producent.getText().toString());
            contentValues.put(DBHelper.KOLUMNA3, androidVer.getText().toString());
            contentValues.put(DBHelper.KOLUMNA4, www.getText().toString());
        }

        ContentResolver contentResolver = getContentResolver();
        contentResolver.update(PhoneContentProvider.URI, contentValues, "ID =" + phone.getID() , null);
    }
}
