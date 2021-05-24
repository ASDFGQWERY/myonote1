package com.github.ASDFGQWERY.myonote1;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import static android.widget.Toast.LENGTH_SHORT;
import static com.github.ASDFGQWERY.myonote1.FavDBhelper.TABLE_NAME;

public class EditActivity extends AppCompatActivity {

    FavDBhelper helper = null;

    String idtemp;
    EditText body;

    private View floatingActionButton3;

    EditText speachText;
    private static final int RECOGNIZER_RESULT = 1;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==RECOGNIZER_RESULT && resultCode == RESULT_OK){
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            body = findViewById(R.id.body);
            //bodyc.setText(matches.get(0).toString());
            body.append(matches.get(0).toString()+"\n");
        }
        super.onActivityResult(requestCode, resultCode, data);


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);



        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        //Floating ABボタン処理(Speech)
        FloatingActionButton voic = findViewById(R.id.floatingActionButton3);
        voic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent speachIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speachIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speachIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                startActivityForResult(speachIntent, RECOGNIZER_RESULT);
            }
        });




        //他人メモ帳
        if (helper == null) {
            helper = new FavDBhelper(EditActivity.this);
        }

        Intent intent = this.getIntent();
        idtemp = intent.getStringExtra("uuid");


        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            Cursor c = db.rawQuery("select body from "+ TABLE_NAME + " where uuid ='" + idtemp + "'", null);
            boolean next = c.moveToFirst();
            while (next) {
                String dispBody = c.getString(0);
                EditText body = (EditText) findViewById(R.id.body);
                body.setText(dispBody, TextView.BufferType.NORMAL);
                next = c.moveToNext();
            }
        } finally {
            db.close();
        }


        body = findViewById(R.id.body);
        body.setSelection(body.getText().length());



        /**
         * 登録ボタン処理
         */
        // idがregisterのボタンを取得
        Button registerButton = findViewById(R.id.sav);
        // clickイベント追加
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 入力内容を取得する
                EditText body = (EditText) findViewById(R.id.body);
                String bodyStr = body.getText().toString();
                if (bodyStr.contains("'"))
                    bodyStr=bodyStr.replaceAll("'","''");


                // データベースに保存する
                SQLiteDatabase db = helper.getWritableDatabase();
                // UPDATE
                db.execSQL("update " + TABLE_NAME + " set body = '" + bodyStr + "' where uuid = '" + idtemp + "'");
                db.execSQL("update " + TABLE_NAME + " set dbtime = strftime('%Y-%m-%d %H:%M:%S', CURRENT_TIMESTAMP,'localtime') where uuid = '" + idtemp + "'");



                db.close();

                Toast.makeText(getApplicationContext(), getString(R.string.success1), LENGTH_SHORT).show();
                //View v1 = t1.getView();
                //v1.getBackground().setColorFilter(Color.rgb(152,251,152), PorterDuff.Mode.SRC_IN);
                //t1.show();
            }

        });


        /**
         * 戻るボタン処理
         */
        // idがbackのボタンを取得
        Button closeButton = (Button) findViewById(R.id.cls);
        // clickイベント追加
        closeButton.setOnClickListener(new View.OnClickListener() {

                                          @Override
                                          public void onClick(View v) {

                                              EditText body3 = (EditText) findViewById(R.id.body);
                                              String bodyStr3 = body3.getText().toString();


                                              SQLiteDatabase db = helper.getReadableDatabase();
                                              try {
                                                  Cursor c = db.rawQuery("select body from " + TABLE_NAME + " where uuid ='" + idtemp + "'", null);
                                                  boolean next = c.moveToFirst();
                                                  while (next) {
                                                      String dispBody3 = c.getString(0);
                                                      next = c.moveToNext();

                                                      //内容が同じ場合
                                                      if (bodyStr3.equals(dispBody3)) {
                                                          Intent intent3 = new Intent(EditActivity.this, ListActivity.class);
                                                          startActivity(intent3);

                                                      } else {
                                                          //内容が違う場合
                                                          // 保存しなくていいか聞く
                                                          AlertDialog.Builder a_builder = new AlertDialog.Builder(EditActivity.this);
                                                          a_builder.setMessage(R.string.move1)
                                                                  .setCancelable(false)
                                                                  .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                                      @Override
                                                                      public void onClick(DialogInterface dialogInterface, int i) {

                                                                          // 一覧へ
                                                                          Intent intent4 = new Intent(EditActivity.this, ListActivity.class);
                                                                          startActivity(intent4);

                                                                      }
                                                                  })
                                                                  .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                                                      @Override
                                                                      public void onClick(DialogInterface dialogInterface, int i) {
                                                                          // キャンセルしてそのまま
                                                                          dialogInterface.cancel();
                                                                      }
                                                                  });

                                                          AlertDialog alert = a_builder.create();
                                                          alert.show();

                                                      }
                                                  }

                                              }finally {
                                                  db.close();
                                              }
                                          }




    });

}}
