package com.github.ASDFGQWERY.myonote1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import static android.widget.Toast.makeText;
import static com.github.ASDFGQWERY.myonote1.FavDBhelper.TABLE_NAME;

public class MainActivity extends AppCompatActivity {
    //private ArrayList<NekoItem> nekoItems = new ArrayList<>();

    private Toolbar toolbar;
    FavDBhelper helper = null;
    String idtemp = "";
    EditText txtemp;
    EditText bodyc;
    String txt;
    private Calendar Calender;
    private View floatingActionButton2;

    EditText speachText;
    private static final int RECOGNIZER_RESULT = 1;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==RECOGNIZER_RESULT && resultCode == RESULT_OK){
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            bodyc = findViewById(R.id.bodyc);
            //bodyc.setText(matches.get(0).toString());
            bodyc.append(matches.get(0).toString()+"\n");
        }
        super.onActivityResult(requestCode, resultCode, data);



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);




        //Floating ABボタン処理(Speech)
        FloatingActionButton voic = findViewById(R.id.floatingActionButton2);
        voic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent speachIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speachIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speachIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                startActivityForResult(speachIntent, RECOGNIZER_RESULT);
            }
        });





        //bodyc.setSelection(bodyc.getText().length());

        /*
        if (txtemp!=null) {
            bodyc.setText((CharSequence) txtemp);
        }
        */





        //他人メモ帳
        if (helper == null){
            helper = new FavDBhelper(MainActivity.this);
        }
        // idがregisterのボタンを取得
        Button registerButton = (Button) findViewById(R.id.savc);
        // clickイベント追加

        registerButton.setOnClickListener(new View.OnClickListener() {

                                              @Override
                                              public void onClick(View v) {
                                                  // 入力内容を取得する
                                                  EditText body = (EditText) findViewById(R.id.bodyc);
                                                  String bodyStr = body.getText().toString();

                                                  if (bodyStr.contains("'"))
                                                      bodyStr=bodyStr.replaceAll("'","''");


                                                  // データベースに保存する
                                                  SQLiteDatabase db = helper.getWritableDatabase();


                                                  if (idtemp=="") {
                                                      // 新規作成の場合
                                                      // 新しくuuidを発行する
                                                      idtemp = UUID.randomUUID().toString();
                                                      // INSERT SQLiteの時間はUTC
                                                      db.execSQL("insert into " + TABLE_NAME + "(uuid, body, favStatus, dbtime) VALUES('" + idtemp + "', '" + bodyStr + "', '1' , strftime('%Y-%m-%d %H:%M', CURRENT_TIMESTAMP,'localtime')) ");


                                                  } else {
                                                      db.execSQL("update " + TABLE_NAME + " set body = '" + bodyStr + "' where uuid = '" + idtemp + "'");

                                                  }
                                                  db.close();

                                                  Toast.makeText(MainActivity.this, getString(R.string.success1), Toast.LENGTH_LONG).show();


                                              }


             }

        );



        /**
         * 戻るボタン処理
         */
        // idがbackのボタンを取得
        Button backButton = (Button) findViewById(R.id.clsc);
        // clickイベント追加
        backButton.setOnClickListener(new View.OnClickListener() {

                                          @Override
                                          public void onClick(View v) {
                                              // 保存せずに一覧へ戻る
                                              // 保存後に一覧へ戻る
                                              Intent intent = new Intent(MainActivity.this, ListActivity.class);
                                              startActivity(intent);
                                          }
                                      }
        );
    }





}
