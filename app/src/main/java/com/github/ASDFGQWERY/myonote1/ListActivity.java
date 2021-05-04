package com.github.ASDFGQWERY.myonote1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.github.ASDFGQWERY.myonote1.FavDBhelper.TABLE_NAME;
import static com.github.ASDFGQWERY.myonote1.NekoAdapter.NekoViewHolder;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

public class ListActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    //黒画面のRecycler Viewから-san

    private final static String TAG = "Banner";
    private FrameLayout adContainerView;
    private AdView adView;

    RecyclerView recyclerView;
    private FavDBhelper helper = null;
    private EditText body = null;
    private EditText dbtime = null;
    private String dbtim = null;
    private Button flag;
    private View floatingActionButton;
    public ArrayList<NekoItem> data1 = new ArrayList<>();
    private NekoItem k;
    private String f;
    private String idtemp;
    NekoAdapter.NekoViewHolder holder;
    Context context;

    private static boolean userPressedBackAgain;





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



        //Floating ABボタン処理
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });


        //広告バナー表示
        adContainerView =findViewById(R.id.id_FrameLayout);
        adContainerView.setVisibility(View.GONE);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.adaptive_banner_ad_unit_id));
        adView.setAdSize(adSize());
        adContainerView.addView(adView);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                //super.onAdFailedToLoad(loadAdError);
                Log.d(TAG,"Loading banner is failed");
                adContainerView.setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded() {
                //super.onAdLoaded();
                Log.d(TAG,"Banner is loaded");
                adContainerView.setVisibility(View.VISIBLE);
            }
        });


        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadBanner();
            }
        });










        /*旧バナー
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NotNull InitializationStatus initializationStatus) {
            }
        });

        AdView adView = (AdView)findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

         */








        //初期読み込みインド人
        FavDBhelper helper = new FavDBhelper(this);

        //黒画面
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String queryString = "SELECT * FROM " + TABLE_NAME + " ORDER BY dbtime DESC";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        try {
            boolean next = cursor.moveToNext();
            while (next) {
                String uuid = cursor.getString(1);
                String body = cursor.getString(2);
                String dbtim = cursor.getString(4);
                String favStatus = cursor.getString(3);

                String dbtime = dbtim.substring(0,16);
                NekoItem newNote = new NekoItem(uuid, body, dbtime, favStatus);
                data1.add(newNote);
                next = cursor.moveToNext();


                // データが何もない場合
                long recodeCount = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
                if(recodeCount!=0) {
                    TextView exampleView = (TextView) findViewById(R.id.nodata1);
                    exampleView.setVisibility(View.GONE);
                } else {
                    TextView exampleView = (TextView) findViewById(R.id.nodata1);
                    exampleView.setVisibility(View.VISIBLE);
                }

            }
        } finally{
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
        recyclerView.setAdapter(adapter);




    }

    private void loadBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private AdSize adSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
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

        recyclerView.getRecycledViewPool().clear();

        data1.get(position);
        NekoItem k = data1.get(position);
        //String k = data1.get(3);
        NekoViewHolder holder = null;
        int num = Integer.parseInt(this.data1.get(position).getFavStatus());

        switch (num){
            case 1:
                // UPDATE
                db.execSQL("update " + TABLE_NAME + " set favStatus = '2' where uuid = '" + k.getUuid() + "'");
                k.setFavStatus("2");
                //holder.favBtn.setBackgroundResource(R.mipmap.ic_l2_foreground);
                //holder.cardView.setBackgroundColor(Color.parseColor("#ffffe0"));
                db.close();
                break;

            case 2:
                // UPDATE
                db.execSQL("update " + TABLE_NAME + " set favStatus = '1' where uuid = '" + k.getUuid() + "'");
                k.setFavStatus("1");
                //holder.favBtn.setBackgroundResource(R.mipmap.ic_l1_foreground);
                //holder.cardView.setBackgroundColor(Color.parseColor("#ffffff"));
                db.close();
                break;

        }


        //位置保存
        Parcelable recyclerViewState;
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        //recyclerView.getRecycledViewPool().clear();

        final NekoAdapter adapter = new NekoAdapter(data1, this);

        //adapter.notifyItemRangeChanged(0,adapter.getItemCount());
        //adapter.notifyDataSetChanged();

        //adapter.notifyItemChanged(position);
        recyclerView.setAdapter(adapter);
        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);


    }

    //削除押下
    @Override
    public void onDeleteClick4(final int position) {

        SQLiteDatabase db = helper.getWritableDatabase();
        data1.get(position);
        final NekoItem k = data1.get(position);
        final String idtemp1 = k.getUuid();
        int p = position;


        AlertDialog.Builder a_builder = new AlertDialog.Builder(this);
        a_builder.setMessage(R.string.question)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        SQLiteDatabase db = helper.getWritableDatabase();
                        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE uuid = '"+ idtemp1 +"'");
                        db.close();

                        data1.remove(position);


                        //位置保存
                        Parcelable recyclerViewState;
                        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

                        NekoAdapter adapter = new NekoAdapter(data1, this);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);

                        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);


                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alert = a_builder.create();
        alert.show();

    }




//------------------------------------------------------------------------------------------

}
