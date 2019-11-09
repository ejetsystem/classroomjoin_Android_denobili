package com.classroomjoin.app.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.classroomjoin.app.R;

public class CircularCheckableTextView extends android.support.v7.widget.AppCompatTextView implements Checkable {
    private final Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int height;


    public CircularCheckableTextView(Context context) {
        super(context);
        initi(context);
    }

    public CircularCheckableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initi(context);

    }

    public CircularCheckableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initi(context);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void draw(Canvas canvas) {
        height = getHeight() / 2;
        canvas.drawCircle(height, height, height - 2, circlePaint);
        super.draw(canvas);
    }

    private void initi(Context context) {
        circlePaint.setColor(ContextCompat.getColor(context, R.color.green));
    }

    private void setSolidColor(String color) {
        circlePaint.setColor(Color.parseColor(color));
    }

// --Commented out by Inspection START (13/2/18 3:03 PM):
//    public void setSolidColor(int color){
//        circlePaint.setColor(color);
//        invalidate();
//    }
// --Commented out by Inspection STOP (13/2/18 3:03 PM)

    private boolean mChecked = false;

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    @Override
    public int[] onCreateDrawableState(final int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            if (checked) setSolidColor("#00c853");
            else setSolidColor("#d50000");
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public void setOnClickListener(final OnClickListener l) {
        View.OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
                l.onClick(v);
            }
        };
        super.setOnClickListener(onClickListener);
    }
}