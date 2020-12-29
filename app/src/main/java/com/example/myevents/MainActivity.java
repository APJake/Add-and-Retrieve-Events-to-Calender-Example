package com.example.myevents;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnShowEvents, btnAddEvents;
    Cursor cursor;

    private static final String TAG = "MyEvents";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();

        btnAddEvents=findViewById(R.id.btnAddEvents);
        btnShowEvents=findViewById(R.id.btnShowEvents);

        btnShowEvents.setOnClickListener(this);
        btnAddEvents.setOnClickListener(this);
    }

    private void requestPermission(){
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.O){
            if(ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED){
                String[] permissionString = {
                        Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR
                };
                ActivityCompat.requestPermissions(this, permissionString, 1);
            }
        }else{

            if(ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                String[] permissionString = {
                        Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR
                };
                ActivityCompat.requestPermissions(this, permissionString, 2);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id==R.id.btnShowEvents){
            cursor=getContentResolver().query(CalendarContract.Events.CONTENT_URI,null,null,null);
            if(cursor!=null && cursor.getCount()>0){
                while(cursor.moveToNext()){
                    int id1 = cursor.getColumnIndex(CalendarContract.Events._ID);
                    int id2 = cursor.getColumnIndex(CalendarContract.Events.TITLE);
                    int id3 = cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION);
                    int id4 = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);

                    String idValue = cursor.getString(id1);
                    String titleValue = cursor.getString(id2);
                    String descValue = cursor.getString(id3);
                    String locValue = cursor.getString(id4);

                    Toast.makeText(this, idValue+", "+titleValue+", "+descValue+", "+locValue, Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "No Events to show", Toast.LENGTH_SHORT).show();
            }

        }else if(id==R.id.btnAddEvents){
            ContentResolver resolver= getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CalendarContract.Events.TITLE,"UCSY");
            contentValues.put(CalendarContract.Events.DESCRIPTION,"This is from android class");
            contentValues.put(CalendarContract.Events.EVENT_LOCATION,"Yangon, Myanmar");
            contentValues.put(CalendarContract.Events.DTSTART, Calendar.getInstance().getTimeInMillis());
            contentValues.put(CalendarContract.Events.DTEND, Calendar.getInstance().getTimeInMillis()+60*1000);
            contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
            contentValues.put(CalendarContract.Events.CALENDAR_ID,1);
            resolver.insert(CalendarContract.Events.CONTENT_URI, contentValues);
            Toast.makeText(this, CalendarContract.Events.CONTENT_URI.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}