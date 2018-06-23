package com.lab3.michau.phones;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lab3.michau.phones.adapter.PhoneAdapter;
import com.lab3.michau.phones.domain.Phone;
import com.lab3.michau.phones.helper.DBHelper;
import com.lab3.michau.phones.provider.PhoneContentProvider;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView list_view;
    private List<Phone> list;
    private ArrayAdapter adapter;
    private ArrayList<Phone> list_items = new ArrayList<>();
    private int countAnInt = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_view = findViewById(R.id.list_view);
        list = cursorToPhone();
        adapter = new PhoneAdapter(this, list);
        list_view.setAdapter(adapter);
        list_view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list_view.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int position, long l, boolean b) {
                if(!list_items.contains(list.get(position))) {
                    countAnInt +=1;
                    actionMode.setTitle(countAnInt + " items selected");
                    list_items.add(list.get(position));
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater menuInflater = actionMode.getMenuInflater();
                menuInflater.inflate(R.menu.menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete_id:
                        for (Phone list_item : list_items) {
                            adapter.remove(list_item);
                            getContentResolver().delete(PhoneContentProvider.URI, "ID =" + list_item.getID(),null);
                        }
                        Toast toast = Toast.makeText(getBaseContext(), countAnInt + " items removed", Toast.LENGTH_SHORT);
                        toast.show();
                        countAnInt = 0;
                        actionMode.finish();
                        return true;
                    case R.id.modify_id:
                        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                        intent.putExtra("phone",list_items.get(0));
                        startActivity(intent);
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                list_items.clear();
                countAnInt = 0;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<Phone> cursorToPhone() {
        ArrayList<Phone> phones = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        String[] projection = {DBHelper.ID, DBHelper.KOLUMNA1, DBHelper.KOLUMNA2, DBHelper.KOLUMNA3, DBHelper.KOLUMNA4};
        Cursor cursor = contentResolver.query(PhoneContentProvider.URI, projection, null, null, null);
        while(cursor != null && cursor.moveToNext()) {
            phones.add(new Phone(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
        }

        return phones;
    }
}
