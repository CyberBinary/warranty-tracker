package com.example.warrantytracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class addDevice extends AppCompatActivity {
    EditText name, email, age;
    Button insert, view;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_device);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        age = findViewById(R.id.device);
        insert = findViewById(R.id.btnInsert);
        view = findViewById(R.id.btnView);

        DB = new DBHelper(this);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(addDevice.this, MainActivity.class));
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTXT = name.getText().toString();
                String emailTXT = name.getText().toString();
                String ageTXT = name.getText().toString();

                Boolean checkinsertdata = DB.insertuserdata(nameTXT, emailTXT, ageTXT);
                if (checkinsertdata == true){
                    Toast.makeText(addDevice.this, "New", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(addDevice.this, "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


