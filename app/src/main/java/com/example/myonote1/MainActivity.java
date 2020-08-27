package com.example.myonote1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    //private ArrayList<NekoItem> nekoItems = new ArrayList<>();

    private Toolbar toolbar;
    FavDBhelper helper = null;
    String idtemp = "";
    TextView bodyc;
    private String TABLE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bodyc = findViewById(R.id.bodyc);

        /*
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);

         */

        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


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
                                                      // INSERT
                                                      db.execSQL("insert into NEKO6_TABLE(uuid, body, favStatus, dbtime) VALUES('" + idtemp + "', '" + bodyStr + "', '1' , datetime(CURRENT_TIMESTAMP)) ");

                                                  } else {
                                                      db.execSQL("update NEKO6_TABLE set body = '" + bodyStr + "' where uuid = '" + idtemp + "'");

                                                  }
                                                  db.close();

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




// Nanmyohorengekyo
}
