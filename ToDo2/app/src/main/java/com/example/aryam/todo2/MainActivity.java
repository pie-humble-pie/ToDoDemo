 package com.example.aryam.todo2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.VolumeShaper;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;

 public class MainActivity extends AppCompatActivity {

     private AlarmManager alarmManager;
     private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ArrayList<Note> noteArrayList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.contentRV);
        final NoteAdapter noteAdapter = new NoteAdapter(noteArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(noteAdapter);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_main,null,false);
        final EditText editText1 = dialogView.findViewById(R.id.edit1);
        final EditText editText2 = dialogView.findViewById(R.id.edit2);
        final Button button = dialogView.findViewById(R.id.button);
        final Integer[] interval = {0};

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                interval[0] = (hourOfDay * 60) + minute;
                            }
                        }, 0, 0, false);
                timePickerDialog.show();

            }
        });

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Enter the title")
                .setView(dialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Note note = new Note(editText1.getText().toString(),editText2.getText().toString());
                        noteArrayList.add(note);
                        noteAdapter.notifyItemInserted(noteArrayList.size());
                        alarmManager = (AlarmManager)getBaseContext().getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(MainActivity.this,AlarmReceiver.class);
                        alarmIntent = PendingIntent.getBroadcast(MainActivity.this,12345,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + interval[0] * 60 * 1000, alarmIntent);
                        editText1.setText("");
                        editText2.setText("");
                    }
                })
                .setNegativeButton("Don't Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
