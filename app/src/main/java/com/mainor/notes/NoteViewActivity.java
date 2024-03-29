package com.mainor.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mainor.notes.entities.Note;
import com.mainor.notes.persistence.AppDatabase;

import java.text.DateFormat;
import java.util.Date;

public class NoteViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);
        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();


        final TextView titleText = findViewById(R.id.noteViewActivity_tv_title);
        final TextView contentText = findViewById(R.id.noteViewActivity_tv_content);
        final TextView dateText = findViewById(R.id.noteViewActivity_tv_creationDate);
        final TextView editDateText = findViewById(R.id.noteViewActivity_tv_updateDate);
        final int id;

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        long editId = extras.getLong("ID");
        if (editId != 0){
             id = (int) editId;
        } else {
            id = extras.getInt("ID");
        }



        Note note = database.noteDao().selectNote(id);
        Long strDate1 = note.getCreationDate();
        Long strDate2 = note.getUpdateDate();
        Date date = new Date(strDate1);
        Date updateDate = new Date(strDate2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateText1 = dateFormat.format(date);
        String dateText2 = dateFormat.format(updateDate);

        titleText.setText(note.getTitle());
        contentText.setText(note.getContent());
        dateText.setText(dateText1);
        if (strDate2==0){
            editDateText.setVisibility(View.INVISIBLE);
        } else {
            editDateText.setText(dateText2);
        }


        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),MainActivity.class));
            }
        });
        findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle edit = new Bundle();
                boolean editNote = true;
               edit.putBoolean("editNote", editNote);
               edit.putInt("ID", id);

                startActivity(new Intent(v.getContext(),NoteEditActivity.class).putExtras(edit));
            }
        });
    }
}
