package com.denobili.app.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.denobili.app.R;

public class CircularTextView extends android.support.v7.widget.AppCompatTextView{
    Paint circlePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
    int height;


    public CircularTextView(Context context) {
        super(context);
        initi(context);
    }

    public CircularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initi(context);

    }

    public CircularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initi(context);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void draw(Canvas canvas) {
        height=getHeight()/2;
        canvas.drawCircle(height,height,height-2, circlePaint);
        super.draw(canvas);
    }

    private void initi(Context context){
        circlePaint.setColor(ContextCompat.getColor(context, R.color.white));
    }

    public void setSolidColor(String color){
        circlePaint.setColor(Color.parseColor(color));
    }

    public void setSolidColor(int color){
        circlePaint.setColor(color);
        invalidate();
    }
}