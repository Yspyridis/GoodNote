package com.example.yannis.mynote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private NotesApi notesApi;//check if its not needed

    private ListView lvResults;
    private List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NotesApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        notesApi = retrofit.create(NotesApi.class);

        lvResults = findViewById(R.id.lv_results);
        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (notes != null) {
                    Note note = notes.get(position);
                    startActivity(NoteDetailsActivity.getIntent( MainActivity.this, note.getContent(), String.valueOf(note.getDateInMillis()), note.getWebsafeKey() ));
                }
            }
        });

        findViewById(R.id.btn_send_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNote();
            }
        });

        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNotesFromServer();
            }
        });

        getNotesFromServer();

    }

    @Override
    public void onRestart() {
        super.onRestart();
        getNotesFromServer();
    }

    private void sendNote() {
        startActivity(new Intent(MainActivity.this, CreateNoteActivity.class));
    }


    private void getNotesFromServer() {
        notesApi.getAllNotes()
                .enqueue(new Callback<ListResponse>() {
                    @Override
                    public void onResponse(Call<ListResponse> call, Response<ListResponse> response) {
                        if (response.isSuccessful()) {
                            notes = response.body().getNotes();
                            lvResults.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, notes));
                        } else {
                            showToast(R.string.msg_notes_not_fetched);
                        }
                    }

                    @Override
                    public void onFailure(Call<ListResponse> call, Throwable t) {
                        showToast(R.string.msg_server_error);
                    }
                });
    }

    private void showToast(int msgString) {
        Toast.makeText(this, msgString, Toast.LENGTH_SHORT).show();
    }
}
