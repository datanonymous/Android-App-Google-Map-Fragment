package com.example.alex.javagooglemapapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

import static android.R.attr.duration;
import static com.example.alex.javagooglemapapplication.R.id.listView;
import static com.example.alex.javagooglemapapplication.R.id.text;

public class NoteActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.add_note){
            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Alex Ko's Location App");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        ListView listView = (ListView) findViewById(R.id.listView);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.alex.javagooglemapapplication", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);
        if(set==null){
            notes.add("Example note");
        } else{
            notes = new ArrayList(set);
        }

        //notes.add("Example note");
        Toast.makeText(getApplicationContext(), "Use the menu to create a note and long press to delete", Toast.LENGTH_LONG).show();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                intent.putExtra("noteId",i);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l){

                final int itemToDelete = i;

                new AlertDialog.Builder(NoteActivity.this)
                       .setIcon(android.R.drawable.ic_dialog_alert)
                               .setTitle("Are you sure?")
                                .setMessage("Do you want to delete this note?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                notes.remove(itemToDelete);
                                                arrayAdapter.notifyDataSetChanged();

                                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.alex.javagooglemapapplication", Context.MODE_PRIVATE);
                                                HashSet<String> set = new HashSet(NoteActivity.notes);
                                                sharedPreferences.edit().putStringSet("notes", set).apply();
                                            }

                                        }
                                )
                                .setNegativeButton("No", null)
                                .show();
                return true;
            }

        });

    }
}
