package com.example.pramesh.demo;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editName,editSurname,editMarks;
    Button buttonInsert,buttonView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        editName = (EditText) findViewById(R.id.editText);
        editSurname = (EditText) findViewById(R.id.editText2);
        editMarks = (EditText) findViewById(R.id.editText3);
        buttonInsert = (Button) findViewById(R.id.button_add);
        buttonView = (Button) findViewById(R.id.button_view);
        AddData();
        viewData();
    }
    public void AddData(){
        buttonInsert.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       boolean isInserted = myDb.insertData(editName.getText().toString(),editSurname.getText().toString(),editMarks.getText().toString());
                        if (isInserted == true){
                            Toast.makeText(MainActivity.this,"Data Inserted",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
    public void viewData(){
        buttonView.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cursor res = myDb.getAllData();
                    if(res.getCount() == 0){
                        Toast.makeText(MainActivity.this,"Data Not found",Toast.LENGTH_LONG).show();
                        showMessage("Add Something!","Add Some Task. You currently have to task added.");
                        return;
                    }else{
                        StringBuffer buffer = new StringBuffer();
                        while(res.moveToNext()){
                            buffer.append("Task 1 :"+res.getString(1)+"\n");
                            buffer.append("Task 2 :"+res.getString(2)+"\n");
                            buffer.append("Task 3 :"+res.getString(3)+"\n\n");
                        }
                        // Show All data ...
                        showMessage("Data ",buffer.toString());
                    }
                }
            }
        );
    }

    private void showMessage(String title,String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();}
}
