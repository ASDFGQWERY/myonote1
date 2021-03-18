package com.github.ASDFGQWERY.myonote1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Dummy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 何もしないで進む
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }}
