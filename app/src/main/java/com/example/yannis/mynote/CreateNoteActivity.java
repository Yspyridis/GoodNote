package com.example.yannis.mynote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateNoteActivity extends AppCompatActivity {

    private NotesApi notesApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        Date curDate = new Date(System.currentTimeMillis());
        DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm z");
//        String dateFormatted = formatter.format(date);

        EditText date = findViewById(R.id.txt_date);
        date.setText(formatter.format(curDate));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NotesApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        notesApi = retrofit.create(NotesApi.class);

        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNoteToServer();
                startActivity(new Intent(CreateNoteActivity.this, MainActivity.class));
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateNoteActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendNoteToServer() {
        String content = ((TextView) findViewById(R.id.txt_note)).getText().toString();
//        System.out.println(content);
        notesApi.createNote(new Note(content))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()) {
                            showToast(R.string.msg_note_created);
                        }
                        else
                        {
                            showToast(R.string.msg_note_not_created);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        showToast(R.string.msg_server_error);
                    }
                });
    }

    private void showToast(int msgString) {
        Toast.makeText(this, msgString, Toast.LENGTH_SHORT).show();
    }
}
