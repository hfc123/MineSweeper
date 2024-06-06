package com.hfc.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hfc.minesweeper.landmine.model.BlockBitmapFactory;
import com.hfc.minesweeper.landmine.model.interfaces.BitmapFactoryB;
import com.hfc.minesweeper.landmine.player.AIPlayer;
import com.hfc.minesweeper.landmine.tools.MineManager;
import com.hfc.minesweeper.landmine.view.MineAdapter;

import java.security.InvalidKeyException;

public class MainActivity extends AppCompatActivity {
    BitmapFactoryB bitmapFactoryB = BlockBitmapFactory.getInstance();
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button AIButon =findViewById(R.id.aiplay);
        RecyclerView recyclerView = findViewById(R.id.recycle);
        AIButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AIPlayer aiPlayer =new AIPlayer(MainActivity.this);
                aiPlayer.setView(recyclerView);
                aiPlayer.startPlay();
            }
        });

        try {
            MineManager.getinstance().setLevel(2);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        MineAdapter mineAdapter = new MineAdapter(MineManager.getinstance().createMinesData(),
                MineManager.getinstance().createDisplayData(),this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, MineManager.getinstance().getM()));
        recyclerView.setAdapter(mineAdapter);
       /* ImageView imageView = findViewById(R.id.image);
        ValueAnimator animator = ValueAnimator.ofInt(0,15);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);

        animator.start();

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.e( "onAnimationUpdate: ", "value"+animation.getAnimatedValue()+(imageView==null));
                count++;
                int a = count%16;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(bitmapFactoryB.getBitmap(new Block2(a)));
            }
        });*/
    }
}