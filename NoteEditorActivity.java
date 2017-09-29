package com.example.alex.javagooglemapapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

import static com.example.alex.javagooglemapapplication.NoteActivity.arrayAdapter;
import static com.example.alex.javagooglemapapplication.NoteActivity.notes;
import static com.example.alex.javagooglemapapplication.R.id.listView;
import static com.example.alex.javagooglemapapplication.R.styleable.View;

public class NoteEditorActivity extends AppCompatActivity {

    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Alex Ko's Location App");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText editText = (EditText) findViewById(R.id.multiLineEditText);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        if(noteId != -1){
            editText.setText(NoteActivity.notes.get(noteId));
        } else{
            NoteActivity.notes.add("");
            noteId = NoteActivity.notes.size() -1;
            NoteActivity.arrayAdapter.notifyDataSetChanged();
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                NoteActivity.notes.set(noteId, String.valueOf(charSequence));
                NoteActivity.arrayAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.alex.javagooglemapapplication", Context.MODE_PRIVATE);
                HashSet<String> set = new HashSet(NoteActivity.notes);
                sharedPreferences.edit().putStringSet("notes", set).apply();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
}
