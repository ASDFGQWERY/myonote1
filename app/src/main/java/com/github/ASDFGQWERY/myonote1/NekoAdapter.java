package com.github.ASDFGQWERY.myonote1;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.github.ASDFGQWERY.myonote1.FavDBhelper.TABLE_NAME;
import static com.github.ASDFGQWERY.myonote1.FavDBhelper.UUID;
import static java.lang.Integer.parseInt;

public class NekoAdapter extends RecyclerView.Adapter<NekoAdapter.NekoViewHolder> {

    public static RecyclerView.Adapter adapter;
    public ArrayList<NekoItem> data1;
    private static RecyclerViewClickInterface recyclerViewClickInterface;
    private FavDBhelper helper = null;
    private SQLiteDatabase db;

    Context context;
    Button favBtn;
    Button image_delete;

    static final SparseIntArray IMG_ID = new SparseIntArray(){{
        put(1, R.mipmap.ic_l1_foreground);
        put(2, R.mipmap.ic_l2_foreground);
    }};


    public NekoAdapter(ArrayList<NekoItem> data1, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.data1 = data1;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    public NekoAdapter(ArrayList<NekoItem> data1, DialogInterface.OnClickListener onClickListener) {
        this.data1 = data1;
    }



    @NonNull
    @Override
    public NekoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        helper = new FavDBhelper(context);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row,
                parent, false);

        return new NekoViewHolder(data1, view);
    }


    @Override
    public void onBindViewHolder(@NonNull NekoViewHolder holder, int position) {
        NekoItem nekoItem = data1.get(position);
        holder.body.setText(this.data1.get(position).getBody());
        //holder.fav_status.setText(this.data1.get(position).getFavStatus());
        holder.dbtime.setText(this.data1.get(position).getDbtime());
        holder.mDeleteImage.setBackgroundResource(R.mipmap.ic_d1_foreground);

        //readCursorData(nekoItem, holder);



            int num = parseInt(this.data1.get(position).getFavStatus());
            int res_id = IMG_ID.get(num);

            if (num == 1) {
                holder.favBtn.setBackgroundResource(R.mipmap.ic_l1_foreground);
                holder.cardView.setBackgroundColor(Color.parseColor("#ffffff"));
            }else {
                holder.favBtn.setBackgroundResource(R.mipmap.ic_l2_foreground);
                holder.cardView.setBackgroundColor(Color.parseColor("#fffdd7"));
            }
            //int num = Integer.parseInt(this.data1.get(position).getFavStatus());



    }

    @Override
    public int getItemCount() {
        return data1.size();
    }



    //----------------------------------------------------------------------------------------------
    public static class NekoViewHolder extends RecyclerView.ViewHolder {



        public Button favBtn;
        public View cardView;
        //private final SQLiteOpenHelper helper = null;
        public ArrayList<NekoItem> data1;
        private final Object NekoViewHolder = null;
        private final Object Context = null;

        private FavDBhelper helper;
        private NekoViewHolder holder;

        private String k;
        View view;
        //View cardView;
        TextView body;
        //TextView fav_status;
        TextView dbtime;
        TextView uuid;
        //ImageView favBtn;
        Button mDeleteImage;
        ConstraintLayout rowlayout;
        //private CardView[] cardViewList;



        public NekoViewHolder(final ArrayList<NekoItem> data1, @NonNull View itemView) {
            super(itemView);
            this.data1 = data1;
            this.view = itemView;
            this.body = itemView.findViewById(R.id.body);
            this.dbtime = itemView.findViewById(R.id.dbtime);
            //this.fav_status = itemView.findViewById(R.id.favStatus);


            cardView = itemView.findViewById(R.id.cdv);
            favBtn = itemView.findViewById(R.id.favBtn);
            rowlayout = itemView.findViewById(R.id.rowlayout);
            mDeleteImage = itemView.findViewById(R.id.image_delete);
            //this.onNoteListener = onNoteListener;



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickInterface.onItemClick4(getAdapterPosition());
                }
            });

            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickInterface.onFavClick4(getAdapterPosition());
                }
            });

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickInterface.onDeleteClick4(getAdapterPosition());

                }
            });
        }

    }

    /*
    private void readCursorData(@NotNull NekoItem nekoItem, RecyclerView.ViewHolder holder){
        Cursor cursor = helper.read_all_data(nekoItem.getUuid());
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            boolean next = cursor.moveToNext();
            while (next) {
                String favStatus = cursor.getString(3);

                nekoItem.setFavStatus(favStatus);

                int num = parseInt(nekoItem.getFavStatus());

                if (num == 2) {
                    NekoViewHolder.favBtn.setBackgroundResource(R.mipmap.ic_l2_foreground);
                    NekoViewHolder.cardView.setBackgroundColor(Color.parseColor("#ffffe0"));
                } else {
                    NekoViewHolder.favBtn.setBackgroundResource(R.mipmap.ic_l1_foreground);
                    NekoViewHolder.cardView.setBackgroundColor(Color.parseColor("#ffffff"));
                }

                next = cursor.moveToNext();

            }
        }finally{
            cursor.close();
            db.close();
        }

    }

     */




}
