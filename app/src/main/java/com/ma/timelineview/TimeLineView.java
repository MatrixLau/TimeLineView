package com.ma.timelineview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TimeLineView extends View {

    String TAG = getClass().getSimpleName();

    Paint timeScalePaint, timeScaleSidePaint, scaleLinePaint, scaleTextPaint,
            timeLineSwitchLinePaint, timeLineSwitchTextPaint, timeLineBackgroundPanelPaint, timeSectionPaint,
            timeLineTrianglePaint;
//    Rect textMeasureRect;

    Path timeLineTrianglePath;

    int timeScaleColor = 0xff242847;
    int timeScaleSideColor = 0xff000000;
    int scaleLineColor = 0xffffffff;
    float scaleGap;
    float timeScaleLineLength = getDp(8);
    float timeLineLineLength = getDp(12);
    int timeScaleTextColor = 0xffffffff;
    int timeLineLineColor = 0xffffffff;
    int timeLineTriangleColor = 0xffC00000;
    /**
     * 时间段颜色数组
     * 会循环使用
     */
    int[] colorArr = {
            0xff1B2782, 0xff114773, 0xff1D6B33, 0xff8FA135,
            0xff9E5437, 0xff8A5115, 0xff801787,
    };
//    int[] colorArr = {
//            0xffDFFF00, 0xffFFBF00, 0xffFF7F50, 0xffDE3163,
//            0xff9FE2BF, 0xff40E0D0, 0xff6495ED, 0xffCCCCFF,
//    };
    /**
     * 时间线显示方式
     * 1 - 展示 此时与时间刻度有白色间隔
     * 2 - 指定 此时有红色箭头指示当前时间段在时间线的位置
     */
    int timeLineMode = 1;
    /**
     * 当前时间段
     * 当timeLineMode为2时用到
     * 从1开始，不要输入0！
     */
    int currentTimeSection = 1;

    TimeLineData timeLineData = new TimeLineData();

    public TimeLineView(Context context) {
        super(context);
        init();
    }

    public TimeLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimeLineView);

        timeScaleColor = typedArray.getColor(R.styleable.TimeLineView_TimeLineView_timeScaleColor, timeScaleColor);
        timeScaleSideColor = typedArray.getColor(R.styleable.TimeLineView_TimeLineView_timeScaleSideColor, timeScaleSideColor);
        scaleLineColor = typedArray.getColor(R.styleable.TimeLineView_TimeLineView_scaleLineColor, scaleLineColor);
        timeScaleLineLength = typedArray.getDimension(R.styleable.TimeLineView_TimeLineView_timeScaleLineLength, timeScaleLineLength);
        timeLineLineLength = typedArray.getDimension(R.styleable.TimeLineView_TimeLineView_timeLineLineLength, timeLineLineLength);
        timeScaleTextColor = typedArray.getColor(R.styleable.TimeLineView_TimeLineView_timeScaleTextColor, timeScaleTextColor);
        timeLineLineColor = typedArray.getColor(R.styleable.TimeLineView_TimeLineView_timeLineLineColor, timeLineLineColor);
        timeLineTriangleColor = typedArray.getColor(R.styleable.TimeLineView_TimeLineView_timeLineTriangleColor, timeLineTriangleColor);
        timeLineMode = typedArray.getInt(R.styleable.TimeLineView_TimeLineView_timeLineMode, timeLineMode);

        typedArray.recycle();

        init();
    }

    public TimeLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TimeLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        //时间刻度画笔
        timeScalePaint = new Paint();
        timeScalePaint.setAntiAlias(true);
        timeScalePaint.setColor(timeScaleColor);
        timeScalePaint.setStyle(Paint.Style.FILL);

        //时间刻度旁道画笔
        timeScaleSidePaint = new Paint();
        timeScaleSidePaint.setAntiAlias(true);
        timeScaleSidePaint.setColor(timeScaleSideColor);
        timeScaleSidePaint.setStyle(Paint.Style.FILL);

        //时间刻度线画笔
        scaleLinePaint = new Paint();
        scaleLinePaint.setAntiAlias(true);
        scaleLinePaint.setColor(scaleLineColor);
        scaleLinePaint.setStrokeWidth(5f);
        scaleLinePaint.setStyle(Paint.Style.FILL);

        //时间刻度文本画笔
        scaleTextPaint = new Paint();
        scaleTextPaint.setAntiAlias(true);
        scaleTextPaint.setColor(timeScaleTextColor);
        scaleTextPaint.setTextSize(22);
        scaleTextPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        //文本测量正方形
//        textMeasureRect = new Rect();

        //时间线开关指示文字画笔
        timeLineSwitchTextPaint = new Paint();
        timeLineSwitchTextPaint.setAntiAlias(true);
        timeLineSwitchTextPaint.setColor(timeLineLineColor);
        timeLineSwitchTextPaint.setTextSize(30);
        timeLineSwitchTextPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        //时间线开关指示线画笔
        timeLineSwitchLinePaint = new Paint();
        timeLineSwitchLinePaint.setAntiAlias(true);
        timeLineSwitchLinePaint.setColor(timeLineLineColor);
        timeLineSwitchLinePaint.setStrokeWidth(10f);
        timeLineSwitchLinePaint.setStyle(Paint.Style.FILL);

        //时间线面板画笔
        timeLineBackgroundPanelPaint = new Paint();
        timeLineBackgroundPanelPaint.setAntiAlias(true);
        timeLineBackgroundPanelPaint.setColor(timeLineLineColor);
        timeLineBackgroundPanelPaint.setStyle(Paint.Style.FILL);

        //时间段画笔
        timeSectionPaint = new Paint();
        timeSectionPaint.setAntiAlias(true);
        timeSectionPaint.setColor(timeLineLineColor);
        timeSectionPaint.setStyle(Paint.Style.FILL);

        //当前时间段三角指示标画笔
        timeLineTrianglePaint = new Paint();
        timeLineTrianglePaint.setAntiAlias(true);
        timeLineTrianglePaint.setColor(timeLineTriangleColor);
        timeLineTrianglePaint.setStrokeWidth(5f);
        timeLineTrianglePaint.setStyle(Paint.Style.FILL);

        //当前时间段三角指示标轨迹
        timeLineTrianglePath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画左边时间刻度
        canvas.drawRect(0f, 0f, 3f * getWidth() / 4f, getHeight(), timeScalePaint);
        //画左边时间刻度旁黑色道
        canvas.drawRect(1f * getWidth() / 2f, 0f, 3f * getWidth() / 4f, getHeight(), timeScaleSidePaint);

        scaleGap = getHeight() / 24f;
        for (int i = 0; i <= 24; i++) {
            //画时间刻度线
            canvas.drawLine(1f * getWidth() / 2f - timeScaleLineLength, i * scaleGap, 1f * getWidth() / 2f, i * scaleGap, scaleLinePaint);
            //画时间刻度数字
//            textMeasureRect = new Rect();
//            String temp = String.valueOf(i); //被获取的文字
//            scaleTextPaint.getTextBounds(temp, 0, temp.length(), textMeasureRect);
//            float textHeight = textMeasureRect.height() * 0.5f;
            float textHeight = scaleTextPaint.getFontSpacing() * 0.3f;
            float textWidth = scaleTextPaint.measureText(String.valueOf(i)) * 0.5f;
            canvas.drawText(String.valueOf(i), 1f * getWidth() / 4f, i * scaleGap + scaleGap / 2f + textHeight, scaleTextPaint);
        }
//
//        //测试定义的数据
        int startHour = Integer.parseInt(timeLineData.getData().get(0).getHour());
        int startMin = Integer.parseInt(timeLineData.getData().get(0).getMin());
        int endHour = Integer.parseInt(timeLineData.getData().get(timeLineData.getData().size() - 1).getHour());
        int endMin = Integer.parseInt(timeLineData.getData().get(timeLineData.getData().size() - 1).getMin());

        Log.i(TAG, "onDraw: timeLineData" + timeLineData.getData());

//        //测试定义的数据
//        int startHour = 9;
//        int startMin = 25;
//        int endHour = 18;
//        int endMin = 0;


        float startPos = 0f;
        float endPos = 0f;

        if (timeLineMode == 1) {

//            if (endHour > startHour) {
//                //画时间线背景面板
//                canvas.drawRect(1f * getWidth() / 2f, startHour * scaleGap + scaleGap * (startMin / 60f), getWidth(), endHour * scaleGap + scaleGap * (endMin / 60f), timeLineBackgroundPanelPaint);
//
//                //画时间段
//                if (timeLineData.getData().size() > 1) {
//                    for (int i = 0; i < timeLineData.getData().size() - 1; i++) {
//                        int colorIndex = i;
//                        while (colorIndex >= colorArr.length) colorIndex -= colorArr.length;
//
//                        timeSectionPaint.setColor(colorArr[colorIndex]);
//
//                        int sectionHour = Integer.parseInt(timeLineData.getData().get(i).getHour());
//                        int sectionMin = Integer.parseInt(timeLineData.getData().get(i).getMin());
//                        int nextSectionHour = Integer.parseInt(timeLineData.getData().get(i + 1).getHour());
//                        int nextSectionMin = Integer.parseInt(timeLineData.getData().get(i + 1).getMin());
//                        //画出时间段方块
//                        canvas.drawRect(3f * getWidth() / 4f, sectionHour * scaleGap + scaleGap * (sectionMin / 60f), getWidth(), nextSectionHour * scaleGap + scaleGap * (nextSectionMin / 60f), timeSectionPaint);
//                        //画出时间段数字标记
//                        float textHeight = scaleTextPaint.getFontSpacing() * 0.3f;
//                        float textWidth = scaleTextPaint.measureText(String.valueOf(i)) * 0.5f;
//                        float sectionDiffHeight = ((nextSectionHour * scaleGap + scaleGap * (nextSectionMin / 60f)) - (sectionHour * scaleGap + scaleGap * (sectionMin / 60f))) * 0.5f;
//                        canvas.drawText(String.valueOf(i), 7f * getWidth() / 8f - textWidth, (sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionDiffHeight + textHeight, scaleTextPaint);
//                    }
//                }
//            }

            //画时间段
            if (timeLineData.getData().size() > 1) {
                for (int i = 0; i < timeLineData.getData().size() - 1; i++) {
                    int colorIndex = i;
                    while (colorIndex >= colorArr.length) colorIndex -= colorArr.length;

                    timeSectionPaint.setColor(colorArr[colorIndex]);

                    int sectionHour = Integer.parseInt(timeLineData.getData().get(i).getHour());
                    int sectionMin = Integer.parseInt(timeLineData.getData().get(i).getMin());
                    int nextSectionHour = Integer.parseInt(timeLineData.getData().get(i + 1).getHour());
                    int nextSectionMin = Integer.parseInt(timeLineData.getData().get(i + 1).getMin());

                    if (nextSectionHour >= sectionHour) {   //当天
                        //画时间线背景面板
                        canvas.drawRect(1f * getWidth() / 2f, sectionHour * scaleGap + scaleGap * (sectionMin / 60f), getWidth(), nextSectionHour * scaleGap + scaleGap * (nextSectionMin / 60f), timeLineBackgroundPanelPaint);
                        //画出时间段方块
                        canvas.drawRect(3f * getWidth() / 4f, sectionHour * scaleGap + scaleGap * (sectionMin / 60f), getWidth(), nextSectionHour * scaleGap + scaleGap * (nextSectionMin / 60f), timeSectionPaint);
                        //画出各时间段开始线
                        canvas.drawLine(1f * getWidth() / 2f, sectionHour * scaleGap + scaleGap * (sectionMin / 60f), getWidth(), sectionHour * scaleGap + scaleGap * (sectionMin / 60f), timeLineSwitchLinePaint);
                        //画出时间段数字标记
                        float textHeight = scaleTextPaint.getFontSpacing() * 0.3f;
                        float textWidth = scaleTextPaint.measureText(String.valueOf(i + 1)) * 0.5f;
                        float sectionDiffHeight = ((nextSectionHour * scaleGap + scaleGap * (nextSectionMin / 60f)) - (sectionHour * scaleGap + scaleGap * (sectionMin / 60f))) * 0.5f;
                        canvas.drawText(String.valueOf(i + 1), 7f * getWidth() / 8f - textWidth, (sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionDiffHeight + textHeight, scaleTextPaint);

                    } else {   //隔天
                        //画时间线背景面板
                        canvas.drawRect(1f * getWidth() / 2f, sectionHour * scaleGap + scaleGap * (sectionMin / 60f), getWidth(), 24 * scaleGap, timeLineBackgroundPanelPaint);
                        //先画当天的时间段
                        canvas.drawRect(3f * getWidth() / 4f, sectionHour * scaleGap + scaleGap * (sectionMin / 60f), getWidth(), 24 * scaleGap, timeSectionPaint);

                        //画时间线背景面板
                        canvas.drawRect(1f * getWidth() / 2f, 0, getWidth(), nextSectionHour * scaleGap + scaleGap * (nextSectionMin / 60f), timeLineBackgroundPanelPaint);
                        //再画出跨天的时间段
                        canvas.drawRect(3f * getWidth() / 4f, 0, getWidth(), nextSectionHour * scaleGap + scaleGap * (nextSectionMin / 60f), timeSectionPaint);
                        //画出各时间段开始线   与当天一致
                        canvas.drawLine(1f * getWidth() / 2f, sectionHour * scaleGap + scaleGap * (sectionMin / 60f), getWidth(), sectionHour * scaleGap + scaleGap * (sectionMin / 60f), timeLineSwitchLinePaint);
                        float textHeight = scaleTextPaint.getFontSpacing() * 0.3f;
                        float textWidth = scaleTextPaint.measureText(String.valueOf(i + 1)) * 0.5f;
                        float sectionInTodayHeight = (24 * scaleGap) - (sectionHour * scaleGap + scaleGap * (sectionMin / 60f));
                        float sectionInNextDayHeight = nextSectionHour * scaleGap + scaleGap * (nextSectionMin / 60f);
                        float sectionHalfHeight = (sectionInTodayHeight + sectionInNextDayHeight) * 0.5f;
                        //画出时间段数字标记
                        if (((sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionHalfHeight + textHeight) < getHeight()) {
                            //若时间段中间在当天且够文本高度
                            canvas.drawText(String.valueOf(i + 1), 7f * getWidth() / 8f - textWidth, (sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionHalfHeight + textHeight, scaleTextPaint);

                        } else if ((sectionHalfHeight - sectionInTodayHeight) > textHeight) {
                            //若时间段中间在隔天且够文本高度
                            canvas.drawText(String.valueOf(i + 1), 7f * getWidth() / 8f - textWidth, (sectionHalfHeight - sectionInTodayHeight) + textHeight, scaleTextPaint);

                        } else {
                            //若都不够位置 两边都显示
                            canvas.drawText(String.valueOf(i + 1), 7f * getWidth() / 8f - textWidth, (sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionInTodayHeight * 0.5f + textHeight, scaleTextPaint);
                            canvas.drawText(String.valueOf(i + 1), 7f * getWidth() / 8f - textWidth, sectionInNextDayHeight * 0.5f + textHeight, scaleTextPaint);

                        }
                    }

                }
            }

        } else if (timeLineMode == 2) {
            //画时间段
            if (timeLineData.getData().size() > 1) {
                for (int i = 0; i < timeLineData.getData().size() - 1; i++) {
                    int colorIndex = i;
                    while (colorIndex >= colorArr.length) colorIndex -= colorArr.length;

                    timeSectionPaint.setColor(colorArr[colorIndex]);

                    int sectionHour = Integer.parseInt(timeLineData.getData().get(i).getHour());
                    int sectionMin = Integer.parseInt(timeLineData.getData().get(i).getMin());
                    int nextSectionHour = Integer.parseInt(timeLineData.getData().get(i + 1).getHour());
                    int nextSectionMin = Integer.parseInt(timeLineData.getData().get(i + 1).getMin());

                    if (nextSectionHour >= sectionHour) {   //当天
                        //画出时间段方块
                        canvas.drawRect(1f * getWidth() / 2f, sectionHour * scaleGap + scaleGap * (sectionMin / 60f), 3f * getWidth() / 4f, nextSectionHour * scaleGap + scaleGap * (nextSectionMin / 60f), timeSectionPaint);
                        //画出各时间段开始线
                        canvas.drawLine(1f * getWidth() / 2f - timeLineLineLength, sectionHour * scaleGap + scaleGap * (sectionMin / 60f), 3f * getWidth() / 4f, sectionHour * scaleGap + scaleGap * (sectionMin / 60f), timeLineSwitchLinePaint);
                        //画出时间段数字标记
                        float textHeight = scaleTextPaint.getFontSpacing() * 0.3f;
                        float textWidth = scaleTextPaint.measureText(String.valueOf(i + 1)) * 0.5f;
                        float sectionDiffHeight = ((nextSectionHour * scaleGap + scaleGap * (nextSectionMin / 60f)) - (sectionHour * scaleGap + scaleGap * (sectionMin / 60f))) * 0.5f;
                        canvas.drawText(String.valueOf(i + 1), 5f * getWidth() / 8f - textWidth, (sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionDiffHeight + textHeight, scaleTextPaint);

                        //画出当前时间段指示标
                        if (currentTimeSection == i + 1) {
                            //先清除之前的Path
                            timeLineTrianglePath.reset();
                            timeLineTrianglePath.moveTo(3f * getWidth() / 4f + 5f, (sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionDiffHeight);
                            timeLineTrianglePath.lineTo(3f * getWidth() / 4f + 25f, (sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionDiffHeight + 20f);
                            timeLineTrianglePath.lineTo(3f * getWidth() / 4f + 25f, (sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionDiffHeight - 20f);
                            timeLineTrianglePath.close();
                            canvas.drawPath(timeLineTrianglePath, timeLineTrianglePaint);
                        }
                    } else {   //隔天
                        //先画当天的时间段
                        canvas.drawRect(1f * getWidth() / 2f, sectionHour * scaleGap + scaleGap * (sectionMin / 60f), 3f * getWidth() / 4f, 24 * scaleGap, timeSectionPaint);
                        //再画出跨天的时间段
                        canvas.drawRect(1f * getWidth() / 2f, 0, 3f * getWidth() / 4f, nextSectionHour * scaleGap + scaleGap * (nextSectionMin / 60f), timeSectionPaint);
                        //画出各时间段开始线   与当天一致
                        canvas.drawLine(1f * getWidth() / 2f - timeLineLineLength, sectionHour * scaleGap + scaleGap * (sectionMin / 60f), 3f * getWidth() / 4f, sectionHour * scaleGap + scaleGap * (sectionMin / 60f), timeLineSwitchLinePaint);
                        float textHeight = scaleTextPaint.getFontSpacing() * 0.3f;
                        float textWidth = scaleTextPaint.measureText(String.valueOf(i + 1)) * 0.5f;
                        float sectionInTodayHeight = (24 * scaleGap) - (sectionHour * scaleGap + scaleGap * (sectionMin / 60f));
                        float sectionInNextDayHeight = nextSectionHour * scaleGap + scaleGap * (nextSectionMin / 60f);
                        float sectionHalfHeight = (sectionInTodayHeight + sectionInNextDayHeight) * 0.5f;
                        //画出时间段数字标记
                        if (((sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionHalfHeight + textHeight) < getHeight()) {
                            //若时间段中间在当天且够文本高度
                            canvas.drawText(String.valueOf(i + 1), 5f * getWidth() / 8f - textWidth, (sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionHalfHeight + textHeight, scaleTextPaint);
                            //画出当前时间段指示标
                            if (currentTimeSection == i + 1) {
                                //先清除之前的Path
                                timeLineTrianglePath.reset();
                                timeLineTrianglePath.moveTo(3f * getWidth() / 4f + 5f, (sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionHalfHeight);
                                timeLineTrianglePath.lineTo(3f * getWidth() / 4f + 25f, (sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionHalfHeight + 20f);
                                timeLineTrianglePath.lineTo(3f * getWidth() / 4f + 25f, (sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionHalfHeight - 20f);
                                timeLineTrianglePath.close();
                                canvas.drawPath(timeLineTrianglePath, timeLineTrianglePaint);
                            }
                        } else if ((sectionHalfHeight - sectionInTodayHeight) > textHeight) {
                            //若时间段中间在隔天且够文本高度
                            canvas.drawText(String.valueOf(i + 1), 5f * getWidth() / 8f - textWidth, (sectionHalfHeight - sectionInTodayHeight) + textHeight, scaleTextPaint);
                            //画出当前时间段指示标
                            if (currentTimeSection == i + 1) {
                                //先清除之前的Path
                                timeLineTrianglePath.reset();
                                timeLineTrianglePath.moveTo(3f * getWidth() / 4f + 5f, sectionHalfHeight - sectionInTodayHeight);
                                timeLineTrianglePath.lineTo(3f * getWidth() / 4f + 25f, sectionHalfHeight - sectionInTodayHeight + 20f);
                                timeLineTrianglePath.lineTo(3f * getWidth() / 4f + 25f, sectionHalfHeight - sectionInTodayHeight - 20f);
                                timeLineTrianglePath.close();
                                canvas.drawPath(timeLineTrianglePath, timeLineTrianglePaint);
                            }
                        } else {
                            //若都不够位置 两边都显示
                            canvas.drawText(String.valueOf(i + 1), 5f * getWidth() / 8f - textWidth, (sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionInTodayHeight * 0.5f + textHeight, scaleTextPaint);
                            canvas.drawText(String.valueOf(i + 1), 5f * getWidth() / 8f - textWidth, sectionInNextDayHeight * 0.5f + textHeight, scaleTextPaint);
                            if (currentTimeSection == i + 1) {
                                //先清除之前的Path
                                timeLineTrianglePath.reset();

                                timeLineTrianglePath.moveTo(3f * getWidth() / 4f + 5f, (sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionInTodayHeight * 0.5f);
                                timeLineTrianglePath.lineTo(3f * getWidth() / 4f + 25f, (sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionInTodayHeight * 0.5f + 20f);
                                timeLineTrianglePath.lineTo(3f * getWidth() / 4f + 25f, (sectionHour * scaleGap + scaleGap * (sectionMin / 60f)) + sectionInTodayHeight * 0.5f - 20f);
                                timeLineTrianglePath.close();
                                canvas.drawPath(timeLineTrianglePath, timeLineTrianglePaint);

                                timeLineTrianglePath.moveTo(3f * getWidth() / 4f + 5f, sectionInNextDayHeight * 0.5f);
                                timeLineTrianglePath.lineTo(3f * getWidth() / 4f + 25f, sectionInNextDayHeight * 0.5f + 20f);
                                timeLineTrianglePath.lineTo(3f * getWidth() / 4f + 25f, sectionInNextDayHeight * 0.5f - 20f);
                                timeLineTrianglePath.close();
                                canvas.drawPath(timeLineTrianglePath, timeLineTrianglePaint);
                            }
                        }
                    }

                }
            }
        }

        float signTextHeight = timeLineSwitchTextPaint.getFontSpacing() * 0.3f;

        //画时间线开指示
        canvas.drawLine(1f * getWidth() / 2f - timeLineLineLength, startHour * scaleGap + scaleGap * (startMin / 60f), timeLineMode == 1 ? getWidth() : 3f * getWidth() / 4f, startHour * scaleGap + scaleGap * (startMin / 60f), timeLineSwitchLinePaint);
        canvas.drawText("ON", 0 + 18f, startHour * scaleGap + scaleGap * (startMin / 60f) + signTextHeight, timeLineSwitchTextPaint);

        //画时间线关指示
        canvas.drawLine(1f * getWidth() / 2f - timeLineLineLength, endHour * scaleGap + scaleGap * (endMin / 60f), timeLineMode == 1 ? getWidth() : 3f * getWidth() / 4f, endHour * scaleGap + scaleGap * (endMin / 60f), timeLineSwitchLinePaint);
        canvas.drawText("OFF", 0 + 18f, endHour * scaleGap + scaleGap * (endMin / 60f) + signTextHeight, timeLineSwitchTextPaint);

    }

    public int getCurrentTimeSection() {
        return currentTimeSection;
    }

    public void setCurrentTimeSection(int currentTimeSection) {
        this.currentTimeSection = currentTimeSection;
        this.invalidate();
    }

    public TimeLineData getTimeLineData() {
        return timeLineData;
    }

    public int[] getColorArr() {
        return colorArr;
    }

    public void setColorArr(int[] colorArr) {
        this.colorArr = colorArr;
        invalidate();
    }

    public void setTimeLineData(TimeLineData timeLineData) {
        this.timeLineData = timeLineData;
    }

    public int getTimeLineMode() {
        return timeLineMode;
    }

    public void setTimeLineMode(int timeLineMode) {
        this.timeLineMode = timeLineMode;
        invalidate();
    }

    /**
     * 获取统一化像素大小
     *
     * @param dp 传入的值
     * @return 转化成统一标准的值
     */
    public float getDp(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}

class TimeLineData {
    public ArrayList<TimeLineSection> data = new ArrayList<TimeLineSection>() {
        {
            add(new TimeLineSection("9", "10"));
            add(new TimeLineSection("10", "30"));
            add(new TimeLineSection("12", "50"));
            add(new TimeLineSection("14", "20"));
            add(new TimeLineSection("17", "30"));
            add(new TimeLineSection("18", "00"));
            add(new TimeLineSection("19", "00"));
            add(new TimeLineSection("20", "00"));
//            add(new TimeLineSection("21", "00"));
//            add(new TimeLineSection("22", "00"));
            add(new TimeLineSection("22", "30"));
            add(new TimeLineSection("1", "00"));
            add(new TimeLineSection("1", "30"));
            add(new TimeLineSection("2", "00"));
//            add(new TimeLineSection("4", "30"));

        }
    };

    public ArrayList<TimeLineSection> getData() {
        return data;
    }

    public void setData(ArrayList<TimeLineSection> data) {
        this.data.clear();
        this.data.addAll(data);
    }

    @Override
    public String toString() {
        return "timeLine{" +
                "data=" + data +
                '}';
    }
}

class TimeLineSection {
    private String hour;
    private String min;

    public TimeLineSection(String hour, String min) {
        this.hour = hour;
        this.min = min;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "timeLineSection{" +
                "hour='" + hour + '\'' +
                ", min='" + min + '\'' +
                '}';
    }
}
