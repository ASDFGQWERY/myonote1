package com.example.myonote1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.collection.ArraySet;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.myonote1.FavDBhelper.TABLE_NAME;
import static com.example.myonote1.NekoAdapter.*;
import static com.example.myonote1.FavDBhelper.TABLE_NAME;

public class ListActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    //黒画面のRecycler Viewから-san

    RecyclerView recyclerView;
    private FavDBhelper helper = null;
    private EditText body = null;
    private EditText dbtime = null;
    private Button flag;
    private View floatingActionButton;
    public ArrayList<NekoItem> data1 = new ArrayList<>();
    private NekoItem k;
    private String f;
    private String idtemp;
    NekoAdapter.NekoViewHolder holder;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //終了ボタン処理
        Button closeapp = findViewById(R.id.closeapp);
        closeapp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {

                finishAffinity();
                System.exit(0);
            }
        });



        //Fabボタン処理
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });




        //初期読み込みインド人
        helper = new FavDBhelper(this);
        //黒画面
        String queryString = "SELECT * FROM " + TABLE_NAME + " ORDER BY dbtime DESC";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        try {
            boolean next = cursor.moveToNext();
            while (next) {
                String uuid = cursor.getString(1);
                String body = cursor.getString(2);
                String dbtime = cursor.getString(4);
                String favStatus = cursor.getString(3);

                NekoItem newNote = new NekoItem(uuid, body, dbtime, favStatus);
                data1.add(newNote);

                next = cursor.moveToNext();

            }
        }finally{
            cursor.close();
            db.close();
        }


        //リスト配置
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final CardView cardView = null;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        NekoAdapter adapter = new NekoAdapter(data1, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);



    }
    //各ボタン処理-------------------------------------------------------------------------------------------
    //Card押下
    @Override
    public void onItemClick4(int position) {
        NekoItem k = data1.get(position);
        Intent intent;
        intent = new Intent(getApplicationContext(), EditActivity.class);
        intent.putExtra("uuid", k.getUuid());
        startActivity(intent);
    }

    //フラグ押下
    @Override
    public void onFavClick4(int position) {
        SQLiteDatabase db = helper.getWritableDatabase();

        //data1.get(position);
        NekoItem k = data1.get(position);
        //String k = data1.get(3);
        //NekoViewHolder holder;
        int num = Integer.parseInt(this.data1.get(position).getFavStatus());

        switch (num){
            case 1:
                // UPDATE
                db.execSQL("update NEKO6_TABLE set favStatus = '2' where uuid = '" + k.getUuid() + "'");
                k.setFavStatus("2");
                NekoAdapter.NekoViewHolder.favBtn.setBackgroundResource(R.mipmap.ic_l2_foreground);
                NekoAdapter.NekoViewHolder.cardView.setBackgroundColor(Color.parseColor("#ffffff"));
                db.close();
                //adapter.notifyDataSetChanged();
                break;

            case 2:
                // UPDATE
                db.execSQL("update NEKO6_TABLE set favStatus = '1' where uuid = '" + k.getUuid() + "'");
                k.setFavStatus("1");
                NekoAdapter.NekoViewHolder.favBtn.setBackgroundResource(R.mipmap.ic_l1_foreground);
                NekoAdapter.NekoViewHolder.cardView.setBackgroundColor(Color.parseColor("#ffbb33"));
                db.close();
                //adapter.notifyDataSetChanged();
                break;

        }

        Parcelable recyclerViewState;
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        final NekoAdapter adapter = new NekoAdapter(data1, this);
        //adapter.notifyItemRangeChanged(0,adapter.getItemCount());
        //adapter.notifyDataSetChanged();
        adapter.notifyItemChanged(position);
        recyclerView.setAdapter(adapter);
        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);


    }

    //削除押下
    @Override
    public void onDeleteClick4(final int position) {

        SQLiteDatabase db = helper.getWritableDatabase();
        data1.get(position);
        NekoItem k = data1.get(position);
        final String idtemp1 = k.getUuid();
        int p = position;


        AlertDialog.Builder a_builder = new AlertDialog.Builder(this);
        a_builder.setMessage(R.string.question)
                .setCancelable(false)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        SQLiteDatabase db = helper.getWritableDatabase();
                        db.execSQL("DELETE FROM NEKO6_TABLE WHERE uuid = '"+ idtemp1 +"'");
                        db.close();



                        //Parcelable recyclerViewState;
                        //recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

                        //final NekoAdapter adapter = new NekoAdapter(data1, (RecyclerViewClickInterface) context);
                        data1.remove(position);
                        //adapter.notifyItemRangeChanged(0,adapter.getItemCount());
                        //adapter.notifyDataSetChanged();
                        //adapter.notifyItemRemoved(position);

                        //recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

                        //LinearLayoutManager manager = new LinearLayoutManager(onDeleteClick4(position));
                        //manager.setOrientation(LinearLayoutManager.VERTICAL);
                        // recyclerView.setLayoutManager(manager);

                        Parcelable recyclerViewState;
                        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

                        NekoAdapter adapter = new NekoAdapter(data1, this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);

                        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);


                    }
                });

        AlertDialog alert = a_builder.create();
        alert.show();

        //画面更新
        data1.clear();
        String queryString = "SELECT * FROM " + TABLE_NAME + " ORDER BY dbtime DESC";
        Cursor cursor = db.rawQuery(queryString, null);

        try {
            boolean next = cursor.moveToNext();
            while (next) {
                String uuid = cursor.getString(1);
                String body = cursor.getString(2);
                String dbtime = cursor.getString(4);
                String favStatus = cursor.getString(3);

                NekoItem newNote = new NekoItem(uuid, body, dbtime, favStatus);
                data1.add(newNote);

                next = cursor.moveToNext();

            }
        }finally{
            cursor.close();
            db.close();
        }




        /*
        LinearLayoutManager manager = new LinearLayoutManager(this);

        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        NekoAdapter adapter = new NekoAdapter(data1, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

         */




    }

//------------------------------------------------------------------------------------------
// Nanmyohorengekyo
}
