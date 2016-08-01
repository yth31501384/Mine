package com.woc.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zyw on 2016/7/29.
 */
public class MainView extends View {
    private   Mine mine;
    private  boolean isFirst=true;
    private  Context context;
    private final int mineNum=10;
    private  final int ROW=15;
    private  final int COL=8;
    private   int TILE_WIDTH=50;
    private  boolean isFalse=false;
    public  MainView(Context context)
    {
        super(context);
        this.context=context;

        TILE_WIDTH=MainActivity.W/10;
        mine=new Mine((MainActivity.W-COL*TILE_WIDTH)/2,(MainActivity.H-ROW*TILE_WIDTH)/2,COL,ROW,mineNum,TILE_WIDTH);
        try {
            mine.init();
            mine.create();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void logic()
    {
        int count=0;
        for (int i=0;i<mine.mapRow;i++)
        {
            for (int j=0;j<mine.mapCol;j++)
            {
                if(!mine.tile[i][j].open)
                {
                    count++;
                }
            }
        }
        if(count==mineNum)
        {
            new AlertDialog.Builder(context)
                    .setMessage("恭喜你，你找出了所有雷")
                    .setCancelable(false)
                    .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mine.init();
                            mine.create();
                            invalidate();
                        }
                    })
                    .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .create()
                    .show();
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {
        mine.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN)
        {
            int x=(int)event.getX();
            int y=(int)event.getY();
            //判断是否点在范围内
            if(x>=mine.x&&y>=mine.y&&x<=(mine.mapWidth+mine.x)&&y<=(mine.y+mine.mapHeight))
            {
                int idxX=(x-mine.x)/mine.tileWidth;
                int idxY=(y-mine.y)/mine.tileWidth;
                mine.open(idxX,idxY,isFirst);
                isFirst=false;
                invalidate();
                if(mine.tile[idxY][idxX].value==-1)
                {
                    new AlertDialog.Builder(context)
                            .setCancelable(false)
                            .setMessage("很遗憾，你踩到雷了！")
                            .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mine.init();
                                    mine.create();
                                    invalidate();
                                    isFalse=true;
                                }
                            })
                            .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    System.exit(0);
                                }
                            })
                            .create()
                            .show();
                }
                if(isFalse)
                {
                    isFalse=false;
                    return true;
                }
                logic();
            }
        }
        return true;
    }
}
