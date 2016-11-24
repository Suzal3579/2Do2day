package com.example.pramesh.demo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import java.util.Calendar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editName, editSurname, editMarks, editTextId;
    Button buttonInsert, buttonView, buttonUpdate, buttonDelete;
    public static final String titleBegMsg = "What's This?";
    public static final String messageBodyAlertBox = "App That Takes In Your Task And Displays It.\n\n\n\n\n\n By - Pramesh Bajracharya";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        editName = (EditText) findViewById(R.id.editText);
        editSurname = (EditText) findViewById(R.id.editText2);
        editMarks = (EditText) findViewById(R.id.editText3);
        editTextId = (EditText) findViewById(R.id.editTextId);
        buttonInsert = (Button) findViewById(R.id.button_add);
        buttonView = (Button) findViewById(R.id.button_view);
        buttonUpdate = (Button) findViewById(R.id.update);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        if (isFirstTime()) {
            showBeginning(titleBegMsg, messageBodyAlertBox);
        }
        AddData();
        viewData();
        updateData();
        deleteData();

//      Create Notification Alert Every 24 hours ...

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 20);
        calendar.set(Calendar.SECOND, 0);
        Intent intent1 = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 10120, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private boolean isFirstTime() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }

    public void AddData() {
        buttonInsert.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editName.getText().toString().length() == 0) {
                            editName.setError("Task 1 is required!");
                            if (editSurname.getText().toString().length() == 0) {
                                editSurname.setError("Task 2 is required!");
                                if (editMarks.getText().toString().length() == 0) {
                                    editMarks.setError("Task 3 is required!");
                                }
                            }
                        } else {
                            boolean isInserted = myDb.insertData(editName.getText().toString(), editSurname.getText().toString(), editMarks.getText().toString());
                            if (isInserted == true) {
                                Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                                editMarks.setText("");
                                editSurname.setText("");
                                editName.setText("");
                                editTextId.setText("");
                            } else {
                                Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                                editMarks.setText("");
                                editSurname.setText("");
                                editName.setText("");
                                editTextId.setText("");
                            }
                        }
                    }
                }
        );
    }

    public void viewData() {
        buttonView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Cursor res = myDb.getAllData();
                        if (res.getCount() == 0) {
                            Toast.makeText(MainActivity.this, "Data Not found", Toast.LENGTH_LONG).show();
                            showMessage("Add Something!", "Add Some Task. You currently have no task to be displayed.");
                            return;
                        } else {
                            StringBuffer buffer = new StringBuffer();
                            while (res.moveToNext()) {
                                buffer.append("Number :" + res.getString(0) + "\n");
                                buffer.append("Task 1 :" + res.getString(1) + "\n");
                                buffer.append("Task 2 :" + res.getString(2) + "\n");
                                buffer.append("Task 3 :" + res.getString(3) + "\n\n");
                            }
                            // Show All data ...
                            showMessage("List Of Task ", buffer.toString());
                        }
                    }
                }
        );
    }

    public void updateData() {
        buttonUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editTextId.getText().toString().length() == 0) {
                            editTextId.setError("Please enter Number here");
                        } else {
                            boolean isUpdated = myDb.updateData(
                                    editTextId.getText().toString(),
                                    editName.getText().toString(),
                                    editSurname.getText().toString(),
                                    editMarks.getText().toString()
                            );
                            if (isUpdated == true) {
                                Toast.makeText(MainActivity.this, "Data Updated", Toast.LENGTH_LONG).show();
                                editMarks.setText("");
                                editSurname.setText("");
                                editName.setText("");
                                editTextId.setText("");
                            } else {
                                editMarks.setText("");
                                editSurname.setText("");
                                editName.setText("");
                                editTextId.setText("");
                                Toast.makeText(MainActivity.this, "Data Not Updated", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        );
    }

    public void deleteData() {
        buttonDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editTextId.getText().toString().length() == 0) {
                            editTextId.setError("Please enter Number here");
                        } else {
                            Integer deletedRows = myDb.deletedata(editTextId.getText().toString());
                            if (deletedRows > 0) {
                                Toast.makeText(MainActivity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                                editTextId.setText("");
                            } else {
                                Toast.makeText(MainActivity.this, "Data Not Deleted. Make sure you have enter the number right.", Toast.LENGTH_LONG).show();
                                editTextId.setText("");
                            }
                        }
                    }
                }
        );
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void showBeginning(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
