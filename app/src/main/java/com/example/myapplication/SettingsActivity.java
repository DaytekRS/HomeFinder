package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.sql.LinksSQL;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private ArrayList<LinksSQL.Link> links;
    private TextInputEditText linkView;
    private ArrayAdapter<LinksSQL.Link> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        links = URLChecker.sql.getLinksSQL().selectLinks();
        linkView  = findViewById(R.id.linkView);
        ListView lvMain = findViewById(R.id.lvSettings);

        // создаем адаптер
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, links);

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {

                LinksSQL.Link link = (LinksSQL.Link)parent.getItemAtPosition(position);
                URLChecker.sql.getLinksSQL().deleteLink(link.getId());

                Toast.makeText(parent.getContext(),link.getName(),Toast.LENGTH_SHORT).show();
                adapter.remove(link);
                adapter.notifyDataSetChanged();

            }
        });
    }

    public void addLinkClick(View view){
        Toast.makeText(this,linkView.getText(),Toast.LENGTH_SHORT).show();

        URLChecker.sql.getLinksSQL().insertLink(linkView.getText().toString());
        links = URLChecker.sql.getLinksSQL().selectLinks();
        adapter.clear();
        adapter.addAll(links);
        adapter.notifyDataSetChanged();
        linkView.setText("");
    }

}
