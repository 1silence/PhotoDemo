package com.example.administrator.photodemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.photodemo.act.Photo2Activity;

public class MainActivity extends AppCompatActivity {

    private Button photo1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photo1 = findViewById(R.id.photo1);
        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Photo2Activity.class));
            }
        });
    }
}
