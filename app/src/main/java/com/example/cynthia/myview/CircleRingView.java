package com.example.cynthia.myview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Cynthia on 2018/3/24.
 */

public class CircleRingView extends View {
    private Paint mBackgroundPaint;
    private Paint mProcessPaint;
    private Paint mTextPaint;
    private RectF rectF = new RectF();
    private Rect mText = new Rect();

    private int circleNum;
    private float[] radiuses = new float[4];
    private float[] current = new float[4];
    private CharSequence[] crs;
    private String[] colors;
    private int backgroundColor;
    private int time;
    private CharSequence[] processes;
    private float[] pcs;
    private float[] process = new float[4];
    private float[] left = new float[4];
    private float[] right = new float[4];
    private float[] top = new float[4];
    private float[] bottom = new float[4];


    //    层层调用使得所有的方式都放置到三参的构造函数之中处理
    public CircleRingView(Context context) {
        this(context, null);
    }

    public CircleRingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleRingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    private void initAttr(AttributeSet attr) {
        TypedArray typeArray = getContext().obtainStyledAttributes(attr, R.styleable.CircleRingView);
        crs = typeArray.getTextArray(R.styleable.CircleRingView_colors);
        processes = typeArray.getTextArray(R.styleable.CircleRingView_processes);
        time = typeArray.getInteger(R.styleable.CircleRingView_time, 2);
        circleNum = typeArray.getInt(R.styleable.CircleRingView_circleNum, 1);
        typeArray.recycle();
        backgroundColor = getResources().getColor(R.color.white);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//      获取在xml文件里面设置的模式和测量得到的宽高？
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == heightMode && widthMode == MeasureSpec.AT_MOST) {
            int length = Math.min(heightSize, widthSize);
            length = Math.min(length, dp2px(200));
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(length, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(length, MeasureSpec.EXACTLY);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        } else {
            int length = Math.min(heightSize, widthSize);
            length = Math.min(length, dp2px(200));
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(length, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(length, MeasureSpec.EXACTLY);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }


    private void init() {

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextSize(dp2px(12));

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);


        mProcessPaint = new Paint();
        mProcessPaint.setAntiAlias(true);
        mProcessPaint.setStyle(Paint.Style.STROKE);
        mProcessPaint.setStrokeCap(Paint.Cap.ROUND);

        initData();

        setAnim();

    }

    private void initData() {
        colors = new String[circleNum];
        pcs = new float[circleNum];
        for (int i = 0; i < circleNum; i++) {
            colors[i] = String.valueOf(crs[i]);
            pcs[i] = Float.parseFloat(String.valueOf(processes[i]));
        }
    }

    /*
     * 直接进行了固定测量以及设定
     * emmm...之后想好了算法之后来做圆环数>4的情况
     */
    private void addData(float radius, float length) {

        left[0] = length / 2 - radius;
        top[0] = length / 2 - radius;
        right[0] = length - left[0];
        bottom[0] = length - top[0];
        radiuses[0] = radius;

        left[1] = (float) (length / 2 - radius * 0.75);
        top[1] = (float) (length / 2 - radius * 0.75);
        right[1] = length - left[1];
        bottom[1] = length - top[1];
        radiuses[1] = (float) (radius * 0.75);

        left[2] = (float) (length / 2 - radius * 0.5);
        top[2] = (float) (length / 2 - radius * 0.5);
        right[2] = length - left[2];
        bottom[2] = length - top[2];
        radiuses[2] = (float) (radius * 0.5);

        left[3] = (float) (length / 2 - radius * 0.25);
        top[3] = (float) (length / 2 - radius * 0.25);
        right[3] = length - left[3];
        bottom[3] = length - top[3];
        radiuses[3] = (float) (radius * 0.25);
    }

    /*
     *  做一个假的半透明效果
     *  背景边缘透明度 50(0-255) 中心24
     *  主要内容边缘透明度 200 中心240
     *  先设置颜色再设置透明度
     */
    @Override
    public void onDraw(Canvas canvas) {
        float x = getWidth() / 2;
        float y = getHeight() / 2;
        float radius = Math.min(x, y);
        float ringWidth = radius / 6;
        radius = (float) (radius - ringWidth * 0.7);

        addData(radius,getWidth());

        for (int i = 0; i < circleNum; i++) {

            rectF.set(left[i],top[i],right[i],bottom[i]);

            String mProcess = String.format("%.1f", process[i]);
            String text = mProcess + "%";
            mTextPaint.getTextBounds(text, 0, String.valueOf(text).length(), mText);
            mTextPaint.setColor(Color.parseColor(colors[i]));

            mBackgroundPaint.setStrokeWidth(ringWidth + dp2px(1));
            mBackgroundPaint.setColor(Color.parseColor(colors[i]));
            mBackgroundPaint.setAlpha(60);
            canvas.drawCircle(x, y, radiuses[i], mBackgroundPaint);
            mBackgroundPaint.setStrokeWidth(ringWidth);
            mBackgroundPaint.setColor(backgroundColor);
            mBackgroundPaint.setAlpha(240);
            canvas.drawCircle(x, y, radiuses[i], mBackgroundPaint);


            mProcessPaint.setStrokeWidth(ringWidth + dp2px(2));
            mProcessPaint.setColor(Color.parseColor(colors[i]));
            mProcessPaint.setAlpha(150);
            canvas.drawArc(rectF, -90, current[i] * 360 / 100, false, mProcessPaint);
            mProcessPaint.setStrokeWidth(ringWidth);
            mProcessPaint.setColor(backgroundColor);
            mProcessPaint.setAlpha(240);
            canvas.drawArc(rectF, -90, current[i] * 360 / 100, false, mProcessPaint);
            mTextPaint.setColor(Color.parseColor(colors[i]));
            canvas.drawText(text, (float) (radius + ringWidth*0.5), (float) (radius - ringWidth * (4.3 - i*1.3)), mTextPaint);
        }
    }

    private void setAnim() {
        ValueAnimator animator;
        for (int i = 0; i < circleNum; i++) {
            animator = ValueAnimator.ofFloat(0f, pcs[i]);
            animator.setDuration(time * 1000);
            animator.setRepeatCount(0);
            animator.setInterpolator(new LinearInterpolator());
            final int finalI = i;
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    current[finalI] = (float) animation.getAnimatedValue();
                    process[finalI] = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animator.start();
        }

    }

    private int dp2px(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public void setColors(int arrayColors) {
        crs = getResources().getStringArray(arrayColors);
    }

    public void setProcesses(int arrayProcesses) {
        processes = getResources().getStringArray(arrayProcesses);
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setCircleNum(int circleNum) {
        this.circleNum = circleNum;
    }

}
