package com.example.cynthia.myview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cynthia on 2018/3/24.
 */

public class CircleRingView extends View {
    private Paint mBackgroundPaint;
    private Paint mProcessPaint;

    private List<Paint> mBgContourPaints = new ArrayList<>();
    private List<Paint> mPcContourPaints = new ArrayList<>();
    private List<Paint> mTextPaints = new ArrayList<>();
    private List<RectF> mRectFs = new ArrayList<>();

    private int circleNum;
    private float[] radiuses = new float[4];
    private float[] current = new float[4];
    private String[] colors;
    private int backgroundColor;
    private int time;
    private String[] processes;
    private float[] pcs;
    private float[] process = new float[4];


//    层层调用使得所有的方式都放置到三参的构造函数之中处理
    public CircleRingView(Context context) {
        this(context,null);
    }

    public CircleRingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleRingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    private void initAttr(AttributeSet attr){
        TypedArray typeArray = getContext().obtainStyledAttributes(attr,R.styleable.CircleRingView);
        String color = typeArray.getString(R.styleable.CircleRingView_colors);
        String process = typeArray.getString(R.styleable.CircleRingView_processes);
        time = typeArray.getInteger(R.styleable.CircleRingView_time,2);
        circleNum = typeArray.getInt(R.styleable.CircleRingView_circleNum,1);
        typeArray.recycle();
        colors = color.split(",");
        processes = process.split(",");
        backgroundColor = getResources().getColor(R.color.white);
        init();
    }

    private void init(){

        initData();

        for (int i = 0; i < circleNum; i++) {
            Paint mBgContourPaint = new Paint();
            mBgContourPaint.setAntiAlias(true);
            mBgContourPaint.setStyle(Paint.Style.STROKE);
            mBgContourPaint.setAlpha(45);
            mBgContourPaint.setColor(Color.parseColor(colors[i]));
            mBgContourPaints.add(mBgContourPaint);

            Paint mPcContourPaint = new Paint();
            mPcContourPaint.setAntiAlias(true);
            mPcContourPaint.setStyle(Paint.Style.STROKE);
            mPcContourPaint.setStrokeCap(Paint.Cap.ROUND);
            mPcContourPaint.setAlpha(200);
            mPcContourPaint.setColor(Color.parseColor(colors[i]));
            mPcContourPaints.add(mPcContourPaint);

            Paint mTextPaint = new Paint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setStyle(Paint.Style.STROKE);
            mTextPaint.setTextSize(dp2px(12));
            mTextPaint.setColor(Color.parseColor(colors[i]));
            mTextPaints.add(mTextPaint);

        }


        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);// 抗锯齿效果
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setColor(backgroundColor);
        mBackgroundPaint.setAlpha(250);

        mProcessPaint = new Paint();
        mProcessPaint.setAntiAlias(true);
        mProcessPaint.setStyle(Paint.Style.STROKE);
        mProcessPaint.setColor(backgroundColor);
        mProcessPaint.setStrokeCap(Paint.Cap.ROUND);
        mProcessPaint.setAlpha(240);

        setAnim();

    }

    private void initData(){
        pcs = new float[circleNum];
        for (int i = 0; i < circleNum ; i++) {
            pcs[i] = Float.valueOf(processes[i]);
        }
    }


//  因为只限定了4个圆环..所以数据都测好了之后直接定值...没有做多个类型的适配emmm..
    private void addData(float radius){

        float left = getWidth()/2-radius;
        float top = getHeight()/2-radius;
        float right = getWidth()-left;
        float bottom = getHeight()-top;
        RectF rectF = new RectF(left,top,right,bottom);
        mRectFs.add(rectF);
        radiuses[0]=radius;

        left = (float) (getWidth()/2-radius*0.75);
        top = (float) (getHeight()/2-radius*0.75);
        right = getWidth()-left;
        bottom = getHeight()-top;
        RectF rectF1 = new RectF(left,top,right,bottom);
        mRectFs.add(rectF1);
        radiuses[1]=(float)(radius*0.75);

        left = (float) (getWidth()/2-radius*0.5);
        top = (float) (getHeight()/2-radius*0.5);
        right = getWidth()-left;
        bottom = getHeight()-top;
        RectF rectF2 = new RectF(left,top,right,bottom);
        mRectFs.add(rectF2);
        radiuses[2]=(float)(radius*0.5);

        left = (float) (getWidth()/2-radius*0.25);
        top = (float) (getHeight()/2-radius*0.25);
        right = getWidth()-left;
        bottom = getHeight()-top;
        RectF rectF3 = new RectF(left,top,right,bottom);
        mRectFs.add(rectF3);
        radiuses[3]=(float)(radius*0.25);
    }

    @Override
    public void onDraw(Canvas canvas){
        float x = getWidth() / 2;
        float y = getHeight() / 2;
        float radius = Math.min(x,y);
        float ringWidth = radius/6;
        radius = (float) (radius-ringWidth*0.7);

        addData(radius);

        mBackgroundPaint.setStrokeWidth(ringWidth);
        mProcessPaint.setStrokeWidth(ringWidth);

        for (int i = 0; i < circleNum; i++) {
            Paint mPcContourPaint = mPcContourPaints.get(i);
            Paint mBgContourPaint = mBgContourPaints.get(i);

            mPcContourPaint.setStrokeWidth(ringWidth+dp2px(2));
            mBgContourPaint.setStrokeWidth(ringWidth+dp2px(1));

            mPcContourPaints.set(i,mPcContourPaint);
            mBgContourPaints.set(i,mBgContourPaint);
        }


        for (int i = 0; i < circleNum; i++) {
            Paint mPcContourPaint = mPcContourPaints.get(i);
            Paint mBgContourPaint = mBgContourPaints.get(i);
            Paint mTextPaint = mTextPaints.get(i);
            RectF rectF = mRectFs.get(i);

            String mProcess = String.format("%.1f",process[i]);
            String text = mProcess + "%";
            Rect mText = new Rect();
            mTextPaint.getTextBounds(text,0,String.valueOf(text).length(),mText);

            canvas.drawCircle(x,y,radiuses[i],mBgContourPaint);
            canvas.drawCircle(x,y,radiuses[i],mBackgroundPaint);
            canvas.drawArc(rectF, -90, current[i]*360/100, false, mPcContourPaint);
            canvas.drawArc(rectF, -90, current[i]*360/100, false, mProcessPaint);
            canvas.drawText(text,getWidth()/2-ringWidth*2, (float) (ringWidth*1.1+ringWidth*1.4*i),mTextPaint);
        }
    }

    private void setAnim(){
        List<ValueAnimator> animators = new ArrayList<>();
        for (int i = 0; i < circleNum; i++) {
            ValueAnimator animator = ValueAnimator.ofFloat(0,pcs[i]);
            animators.add(animator);
        }
        for (int i = 0; i < circleNum; i++) {
            ValueAnimator animator = animators.get(i);
            animator.setDuration(time*1000);
            animator.setRepeatCount(0);
            animator.setInterpolator(new LinearInterpolator());
            final int finalI = i;
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    current[finalI] = (float)animation.getAnimatedValue();
                    process[finalI] = (float)animation.getAnimatedValue();
                    Log.d("TAG",current+" ,"+process+" ");
                    invalidate();
                }
            });
            animator.start();
        }

    }

    private int dp2px(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public void setColors(String color){
        colors = color.split(",");
    }

    public void setProcesses(String processes1){
        processes = processes1.split(",");
    }

    public void setTime(int time){
        this.time = time;
    }

    public void setCircleNum(int circleNum){
        this.circleNum = circleNum;
    }

}
