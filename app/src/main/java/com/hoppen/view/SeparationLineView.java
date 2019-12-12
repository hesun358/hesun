package com.hoppen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.hoppen.uvcctest.R;

public class SeparationLineView extends View {

    private Paint paint;
    private Path path;
    private Paint paintText;
    private String[] textData = {"100","80","60","40","20","0","20","40","60","80","100"};

    public SeparationLineView(Context context) {
        this(context,null);
    }

    public SeparationLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SeparationLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SeparationLineView);
        int textSize = array.getInteger(R.styleable.SeparationLineView_text_size,20);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        //paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setColor(this.getResources().getColor(R.color.colorAccent));

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setTextSize(textSize);
        paintText.setColor(Color.BLACK);
        //paintText.setAntiAlias(true);
        paintText.setStyle(Paint.Style.STROKE);

        path = new Path();
        array.recycle();
    }

    private void drawSectionLine(Canvas canvas){
        int width = getWidth();
        int height = getHeight();
        canvas.drawLine(0,height/2,width,height/2,paint);
        path.moveTo(15,10);
        path.lineTo(0,height/2);
        path.lineTo(15,height-10);
        canvas.drawPath(path,paint);
        path.moveTo(width-15,10);
        path.lineTo(width,height/2);
        path.lineTo(width-15,height-10);
        canvas.drawPath(path,paint);

        int index = width/textData.length;
        int half = index/2;
        for (int i = 0; i < textData.length; i++) {
            float textWidth = paintText.measureText(textData[i])/2;
            if (i==0){
                canvas.drawLine(half,10,half,height/2,paint);
                canvas.drawText(textData[i],(index*i+half)-textWidth,height/2+20,paintText);
            }else {
                canvas.drawLine(index*i+half,10,index*i+half,height/2,paint);
                canvas.drawText(textData[i],(index*i+half)-textWidth,height/2+20,paintText);
            }
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSectionLine(canvas);
    }
}
