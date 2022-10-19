package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.sql.LinksSQL;
import com.example.myapplication.sql.SqlHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity
{
    private ArrayList<LinksSQL.Link> links;
    private TextInputEditText linkView;
    private TextInputEditText nameView;
    private ArrayAdapter<LinksSQL.Link> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        nameView = findViewById(R.id.nameView);
        linkView = findViewById(R.id.linkView);
        ListView lvMain = findViewById(R.id.lvSettings);

        final LinksSQL linksSQL = SqlHelper.getInstance().getLinksSQL();
        links = linksSQL.selectLinks();

        // создаем адаптер
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, links);

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id)
            {
                LinksSQL.Link link = (LinksSQL.Link) parent.getItemAtPosition(position);
                linksSQL.deleteLink(link.getId());

                Toast.makeText(parent.getContext(), link.getLink(), Toast.LENGTH_SHORT).show();
                adapter.remove(link);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void addLinkClick(View view)
    {
        Toast.makeText(this, linkView.getText(), Toast.LENGTH_SHORT).show();

        LinksSQL linksSQL = SqlHelper.getInstance().getLinksSQL();
        linksSQL.insertLink(nameView.getText().toString(), linkView.getText().toString());
        links = linksSQL.selectLinks();

        adapter.clear();
        adapter.addAll(links);
        adapter.notifyDataSetChanged();

        linkView.setText("");
    }
}
