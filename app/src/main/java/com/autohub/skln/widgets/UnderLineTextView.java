package com.autohub.skln.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.autohub.skln.R;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-07-31.
 */
public class UnderLineTextView extends AppCompatTextView {
    private Paint mPaint;
    private float mLineStroke;

    public UnderLineTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public UnderLineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public UnderLineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        int currentTextColor = getCurrentTextColor();
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UnderLineTextView, defStyleAttr, 0);
            currentTextColor = a.getColor(R.styleable.UnderLineTextView_line_color, currentTextColor);
            a.recycle();
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(currentTextColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLineStroke = getResources().getDimension(R.dimen._1dp);
        mPaint.setStrokeWidth(mLineStroke);
        //setPaintFlags(getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mPaint.setColor(getCurrentTextColor());
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getMeasuredWidth();
        int y = (int) (getMeasuredHeight() - mLineStroke);
        canvas.drawLine(0, y, x, y, mPaint);
    }
}
