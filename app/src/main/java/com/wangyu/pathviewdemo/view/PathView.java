package com.wangyu.pathviewdemo.view;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

/**
 * 支付成功对勾动画
 * Created by wangyu on 2017/5/4 0004.
 */

public class PathView extends View {
    private Context context;
    private Paint paint;
    private int width,height;
    private float circleAniValue,successAniValue;
    private Path circlepath;
    private Path successPath;
    private PathMeasure pathMeasure;
    private Path mPath;//截取到的真正用来画图的path

    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        initPara();
    }

    /**
     * 初始化一些参数
     */
    private void initPara() {
        //画笔
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        // 外圈圆取值
        ValueAnimator cirvleAnimator = ValueAnimator.ofFloat(0, 1);
        cirvleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleAniValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        ValueAnimator succrssAnimator = ValueAnimator.ofFloat(0, 1);
        succrssAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                successAniValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        circlepath = new Path();
        successPath = new Path();
        mPath = new Path();
        pathMeasure = new PathMeasure();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(2000);
        animatorSet.play(cirvleAnimator).before(succrssAnimator);
        animatorSet.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        }
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        }
//        else {
//            //自己测量宽度
//            int desired = height+lengthR;
//            width = desired;
//        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //构建圆形path
        circlepath.addCircle(width / 2, height / 2, width / 2 - 10, Path.Direction.CW);
        pathMeasure.setPath(circlepath,false);
        //截取片段
        pathMeasure.getSegment(0, circleAniValue * pathMeasure.getLength(), mPath, true);

        if (circleAniValue >= 1f) {
            //话对勾
            successPath.moveTo(width / 8 * 2, width / 2);
            successPath.lineTo(width / 2, width / 3 * 2);
            successPath.lineTo(width / 4 * 3, width / 5 * 2);
            pathMeasure.setPath(successPath,false);
            pathMeasure.getSegment(0, successAniValue * pathMeasure.getLength(), mPath, true);
        }


        canvas.drawPath(mPath,paint);
    }
}
