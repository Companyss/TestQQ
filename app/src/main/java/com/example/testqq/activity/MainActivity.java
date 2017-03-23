package com.example.testqq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.testqq.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.asd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Splik(MainActivity.this, new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }
}
