package com.hfc.minesweeper.landmine.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hfc.minesweeper.R;
import com.hfc.minesweeper.landmine.model.BlockBitmapFactory;
import com.hfc.minesweeper.landmine.model.data.Block2;
import com.hfc.minesweeper.landmine.model.interfaces.BitmapFactoryB;
import com.hfc.minesweeper.landmine.tools.MineManager;

public class  MineAdapter extends RecyclerView.Adapter<MineAdapter.MineViewHolder> {
    int[][] desplay;
    int[][] mines;
    Context context;
    public  static boolean isAiPlay =false;
    int counts;
    int n = 0 ;
    int m = 0 ;
    BitmapFactoryB bitmapFactoryB = BlockBitmapFactory.getInstance();
    public MineAdapter(int[][] mines) {
        this.mines = mines;
    }
    public MineAdapter(int[][] mines,int[][] desplay, Context context) {
        this.desplay = desplay;
        this.mines = mines;
        this.context = context;
         n = desplay.length;
         m = desplay[0].length;
        counts = n * m ;

    }

    @NonNull
    @Override
    public MineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        MineViewHolder holder =   new MineViewHolder(itemView);
        holder.setIsRecyclable(false);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MineViewHolder holder, int position) {

        int n1 = position/m;
        int m1 = position%m;
//        Log.e("onBindViewHolder: ",n1+","+m1+","+mines[n1][m1] );
        holder.imageView.setImageBitmap(bitmapFactoryB.getBitmap(new Block2(15-desplay[n1][m1])));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desplay[n1][m1]!=0){
                    return;
                }
                MineManager.getinstance().ondisplayClick(n1, m1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifyDataSetChanged();
                    }
                });
            }
        });
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (desplay[n1][m1]==0){
                    desplay[n1][m1] = 12;
                }else if ( desplay[n1][m1] ==12){
                    desplay[n1][m1] = 13;
                }else if( desplay[n1][m1] == 13){
                    desplay[n1][m1] = 0;
                }
                if (isAiPlay){
                    desplay[n1][m1] = 12;
                }
                notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {

        return counts;
    }

    class MineViewHolder extends  RecyclerView.ViewHolder{
        ImageView imageView ;
        public MineViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.iamge);
        }
    }

    public void setDesplay(int[][] desplay) {
        this.desplay = desplay;
        n = desplay.length;
        m = desplay[0].length;
    }


}
