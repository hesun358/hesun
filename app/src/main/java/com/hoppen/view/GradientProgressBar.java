package com.hoppen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.hoppen.uvcctest.R;

public class GradientProgressBar extends View {

    private Paint mPaint;
    private Paint paintText;
    private RectF rectF;
    private int[] gradualChangeColor = {0xff111111,0x00111111}; //渐变颜色
    private int fillet; //圆角
    private int progress; //进度值
    private int MaxiProgress = 100; //最大进度
    //private String numericalValue = "85";

    public GradientProgressBar(Context context) {
        this(context,null);
    }

    public GradientProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GradientProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        if (progress>MaxiProgress){
            progress = MaxiProgress;
        }
        this.progress = progress;
        postInvalidate();
    }

    private void init(Context context,AttributeSet attrs){
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GradientProgressBar);
        fillet = array.getInteger(R.styleable.GradientProgressBar_fillet,10);
        progress = array.getInteger(R.styleable.GradientProgressBar_progress,100);
        if (progress>MaxiProgress) progress = MaxiProgress;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);

        paintText = new Paint();
        paintText.setTextSize(10);
        paintText.setColor(Color.BLACK);
        paintText.setAntiAlias(true);
        paintText.setStyle(Paint.Style.STROKE);

        rectF = new RectF();
        array.recycle();
    }

    private void drawingProgressBar(Canvas canvas){
        //float measureText = paintText.measureText(numericalValue);
        rectF.left = 0;
        //rectF.right = progress*(getWidth()-measureText)/MaxiProgress;
        rectF.right = progress*getWidth()/MaxiProgress;
        rectF.bottom = getHeight();
        rectF.top = 0;

        LinearGradient linearGradient = new LinearGradient(rectF.right,rectF.bottom,0,0,Color.RED,Color.GREEN, Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);
        canvas.drawRoundRect(rectF,fillet,fillet,mPaint);
        //canvas.drawText(numericalValue,rectF.right,getHeight()/2,paintText);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawingProgressBar(canvas);
    }
}
