package com.jetec.nordic_googleplay;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;

public class Firstpage_buttonstyle {

    private Context context;
    private double all_Width, all_Height;

    public Firstpage_buttonstyle(Context context, double all_Width, double all_Height){
        this.context = context;
        this.all_Width = all_Width;
        this.all_Height = all_Height;
    }

    public void buttonstyle(Button btn){
        int strokeWidth = 2;    //圓邊框寬度
        int roundRadius;    //圓半徑
        int strokeColor = ContextCompat.getColor(context, R.color.colormenu_btn);   //邊框顏色
        int fillColor = ContextCompat.getColor(context, R.color.colorBackground);   //內部填充顏色
        if (all_Width > all_Height) {
            roundRadius = (int) (all_Height / 2);
        }
        else {
            roundRadius = (int) (all_Width / 2);
        }
        GradientDrawable gd = new GradientDrawable();   //創建drawable
        gd.setColor(fillColor);
        Log.e("buttonstyle","roundRadius = " + roundRadius);
        gd.setCornerRadius((float) roundRadius);
        gd.setStroke(strokeWidth, strokeColor);
        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gd.setGradientCenter(0.5f,0.5f);
        //noinspection deprecation,ConstantConditions
        btn.setWidth(roundRadius);
        btn.setHeight(roundRadius);
        btn.setBackground(gd);
    }
}
