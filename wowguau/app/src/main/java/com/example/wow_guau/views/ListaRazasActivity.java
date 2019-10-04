package com.example.wow_guau.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.wow_guau.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListaRazasActivity extends AppCompatActivity {
    ListView listView;
    EditText et_search;
    List<String> razas;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_razas);
        listView = findViewById(R.id.listView);
        et_search = findViewById(R.id.et_search);

        razas = new ArrayList<>();
        razas = Arrays.asList(getResources().getStringArray(R.array.razas_array));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, razas);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getBaseContext(), ListaRazasActivity.class);
                i.putExtra("raza", adapter.getItem(position));
                setResult(RESULT_OK, i);
                finish();
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) { }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                adapter.getFilter().filter(arg0);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
