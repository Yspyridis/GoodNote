package com.example.yannis.mynote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
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


public class NoteDetailsActivity extends AppCompatActivity {

    private NotesApi notesApi;
    private static final String EXTRA_CONTENT = "content";
    private static final String EXTRA_DATEINMILLIS = "date_in_millis";
    private static final String EXTRA_WEBSAFEKEY = "websafeKey";


    public static Intent getIntent(Context context, String content, String dateInMillis, String websafeKey) {
        Intent intent = new Intent(context, NoteDetailsActivity.class);
        intent.putExtra(EXTRA_CONTENT, content);
        intent.putExtra(EXTRA_DATEINMILLIS, dateInMillis);
        intent.putExtra(EXTRA_WEBSAFEKEY, websafeKey);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NotesApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        notesApi = retrofit.create(NotesApi.class);

        final Intent intent = getIntent();
        String content = intent.getStringExtra(EXTRA_CONTENT);
        String dateInMillis = intent.getStringExtra(EXTRA_DATEINMILLIS);

        final String websafeKey = intent.getStringExtra(EXTRA_WEBSAFEKEY);
        System.out.println("KEY :" +websafeKey);



        Date date = new Date(Long.parseLong(dateInMillis));
        DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm z");
        String dateFormatted = formatter.format(date);

        TextView tvContent = (TextView) findViewById(R.id.tv_content);
        TextView tvDate = (TextView) findViewById(R.id.tv_date);

        tvContent.setText(content);
        tvDate.setText(dateFormatted);

        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNoteFromServer(retrofit, intent);
                startActivity(new Intent(NoteDetailsActivity.this, MainActivity.class));
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoteDetailsActivity.this, MainActivity.class));
            }
        });
    }

    private void deleteNoteFromServer(Retrofit retrofit, Intent intent) {

        notesApi = retrofit.create(NotesApi.class);

        String websafeKey = intent.getStringExtra(EXTRA_WEBSAFEKEY);
        System.out.println("KEY: "+websafeKey);

        notesApi.deleteNote(websafeKey)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            showToast(R.string.msg_note_deleted);
                        } else {
                            showToast(R.string.msg_note_not_deleted);
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
