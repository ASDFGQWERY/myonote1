package com.github.ASDFGQWERY.myonote1;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import static android.widget.Toast.LENGTH_SHORT;
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
    Integer num1=0;

    EditText speachText;
    private static final int RECOGNIZER_RESULT = 1;
    private static boolean userPressedBackAgain;


    //音声処理1
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

                                                  Toast t1 = Toast.makeText(getApplicationContext(), getString(R.string.success1), LENGTH_SHORT);
                                                  View v1 = t1.getView();
                                                  v1.getBackground().setColorFilter(Color.rgb(152,251,152), PorterDuff.Mode.SRC_IN);
                                                  t1.show();




                                              }


             }

        );



        /**
         * 閉じるボタン処理
         */
        // idがcloseのボタンを取得
        Button closeButton = (Button) findViewById(R.id.clsc);
        // clickイベント追加
        closeButton.setOnClickListener(new View.OnClickListener() {



                                           @Override
                                          public void onClick(View v) {

                                               //空っぽの場合
                                               EditText body2 = (EditText) findViewById(R.id.bodyc);
                                               String bodyStr2 = body2.getText().toString();
                                               //String crt=getString(R.string.create);

                                                //IDがない場合（変更なし）
                                               if (idtemp.equals("") && TextUtils.isEmpty(bodyStr2)) {
                                                   Intent intent2 = new Intent(MainActivity.this, ListActivity.class);
                                                   startActivity(intent2);
                                               }


                                               //IDがない場合（変更あり）
                                               if (idtemp.equals("") && !TextUtils.isEmpty(bodyStr2)) {

                                                   // 保存しなくていいか聞く
                                                   AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);
                                                   a_builder.setMessage(R.string.move1)
                                                           .setCancelable(false)
                                                           .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                               @Override
                                                               public void onClick(DialogInterface dialogInterface, int i) {

                                                                   // 一覧へ
                                                                   Intent intent = new Intent(MainActivity.this, ListActivity.class);
                                                                   startActivity(intent);


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

                                               //IDがある場合
                                               if (!idtemp.equals("")) {

                                                   SQLiteDatabase db = helper.getReadableDatabase();
                                                   try {
                                                       Cursor c = db.rawQuery("select body from " + TABLE_NAME + " where uuid ='" + idtemp + "'", null);
                                                       boolean next = c.moveToFirst();
                                                       while (next) {
                                                           String dispBody2 = c.getString(0);
                                                           next = c.moveToNext();

                                                           //内容が同じ場合
                                                           if (bodyStr2.equals(dispBody2)) {
                                                               Intent intent2 = new Intent(MainActivity.this, ListActivity.class);
                                                               startActivity(intent2);

                                                           } else {
                                                               //内容が違う場合
                                                               // 保存しなくていいか聞く
                                                               AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);
                                                               a_builder.setMessage(R.string.move1)
                                                                       .setCancelable(false)
                                                                       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                                           @Override
                                                                           public void onClick(DialogInterface dialogInterface, int i) {

                                                                               // 一覧へ
                                                                               Intent intent = new Intent(MainActivity.this, ListActivity.class);
                                                                               startActivity(intent);

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


                                                           } finally {
                                                            db.close();
                                                           }


                                                   }
                                           }

        }
        );


    }

    //2度戻るボタンで終了
    @Override
    public void onBackPressed(){
        if (!userPressedBackAgain){
            //Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            userPressedBackAgain = true;
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finishAffinity();
            System.exit(0);
        }
        new CountDownTimer(2000,100){
            @Override
            public void onTick(long millsUntilFinished) {

            }

            @Override
            public void onFinish(){
                userPressedBackAgain = false;
            }
        }.start();
    }




}
