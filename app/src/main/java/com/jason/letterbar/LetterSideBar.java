package com.jason.letterbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LetterSideBar extends View {

    private Paint mPaint;
    private Paint mCurrentPaint;

    private String[] mLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private int letterHeight;
    private String currentLetter;

    private OnSelectListener onSelectListener;

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public LetterSideBar(Context context) {
        this(context, null);
    }

    public LetterSideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LetterSideBar);
        int textSize = array.getDimensionPixelSize(R.styleable.LetterSideBar_lsb_text_size, sp2px(context, 14.f));
        int textColor = array.getColor(R.styleable.LetterSideBar_lsb_text_color, Color.BLACK);
        int selectTextColor = array.getColor(R.styleable.LetterSideBar_lsb_select_text_color,
                Color.RED);
        array.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        mPaint.setColor(textColor);

        mCurrentPaint = new Paint();
        mCurrentPaint.setAntiAlias(true);
        mCurrentPaint.setTextSize(textSize);
        mCurrentPaint.setColor(selectTextColor);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //宽度
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        int letterWidth = (int) mPaint.measureText("A");
        int width = paddingLeft + paddingRight + letterWidth;

        //高度
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        letterHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;

        for (int i = 0; i < mLetters.length; i++) {
            int x = (int) (getWidth() / 2 - mPaint.measureText(mLetters[i]) / 2);
            Rect bounds = new Rect();
            mPaint.getTextBounds(mLetters[i], 0, mLetters[i].length(), bounds);
            Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
            int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;

            int baseline = letterHeight / 2 + i * letterHeight + dy;

            if (mLetters[i].equals(currentLetter)) {
                canvas.drawText(mLetters[i], x, baseline, mCurrentPaint);
            } else {
                canvas.drawText(mLetters[i], x, baseline, mPaint);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float currentMoveY = event.getY();
                int currentPosition = (int) (currentMoveY / letterHeight);
                if (currentPosition < 0) {
                    currentPosition = 0;
                }
                if (currentPosition > mLetters.length - 1) {
                    currentPosition = mLetters.length - 1;
                }
                currentLetter = mLetters[currentPosition];
                invalidate();
                if (onSelectListener != null) {
                    onSelectListener.onSelect(currentLetter);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }

    public interface OnSelectListener {
        void onSelect(String letter);
    }

    /**
     * 将sp转换为px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将dp转换为与之相等的px
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
