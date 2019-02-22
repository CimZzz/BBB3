package com.gogoh5.apps.quanmaomao.library.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.gogoh5.apps.quanmaomao.library.R;

import java.util.ArrayList;
import java.util.List;

public class AutoWrapLayout extends ViewGroup {
    private List<LineWrapper> wrapperList;

    private int horGap = 0;
    private int verGap = 0;
    private int maxLineCount;

    public AutoWrapLayout(Context context) {
        super(context);
        init();
    }

    public AutoWrapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoWrapLayout);
        horGap = (int) typedArray.getDimension(R.styleable.AutoWrapLayout_horGap,horGap);
        verGap = (int) typedArray.getDimension(R.styleable.AutoWrapLayout_verGap,verGap);
        maxLineCount = typedArray.getInteger(R.styleable.AutoWrapLayout_maxLineCount, -1);
        typedArray.recycle();

        init();
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        if(childCount == 0) {
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
            return;
        }
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        LineWrapper lineWrapper = new LineWrapper();

        wrapperList.clear();

        int nextLineCount = 1;
        for(int i = 0 ,restWidth = width; i < childCount ; i ++) {
            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if(lineWrapper.count != 0)
                childWidth += horGap;

            if(childWidth > restWidth) {
                if(childWidth > width) {
                    if(lineWrapper.count == 0) {
                        lineWrapper.count = 1;
                        lineWrapper.height = childHeight;
                        wrapperList.add(lineWrapper);
                        nextLineCount ++;
                        if(nextLineCount > maxLineCount && maxLineCount > 0)
                            break;
                        lineWrapper = new LineWrapper();
                        restWidth = width;
                        continue;
                    } else {
                        wrapperList.add(lineWrapper);
                        nextLineCount ++;
                        if(nextLineCount > maxLineCount && maxLineCount > 0)
                            break;
                        lineWrapper = new LineWrapper();
                        lineWrapper.count = 1;
                        lineWrapper.height = childHeight;
                        wrapperList.add(lineWrapper);
                        nextLineCount ++;
                        if(nextLineCount > maxLineCount && maxLineCount > 0)
                            break;
                        lineWrapper = new LineWrapper();
                        restWidth = width;
                        continue;
                    }
                } else {
                    wrapperList.add(lineWrapper);
                    nextLineCount ++;
                    if(nextLineCount > maxLineCount && maxLineCount > 0)
                        break;
                    lineWrapper = new LineWrapper();
                    restWidth = width;
                }
            }

            if(lineWrapper.height < childHeight)
                lineWrapper.height = childHeight;

            lineWrapper.count ++;

            restWidth = restWidth - childWidth;
        }

        if(lineWrapper.count != 0 && (nextLineCount <= maxLineCount || maxLineCount <= 0))
            wrapperList.add(lineWrapper);

        int height = getPaddingTop() + getPaddingBottom() + (wrapperList.size() - 1) * verGap;

        for(LineWrapper wrapper : wrapperList)
            height += wrapper.height;

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        Integer span = (Integer) child.getTag(R.id.span);
        if(span != null) {
            int width = MeasureSpec.getSize(parentWidthMeasureSpec) - getPaddingLeft() - getPaddingRight() - (span - 1) * horGap;
            parentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width / span, MeasureSpec.getMode(parentWidthMeasureSpec));
        }
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if(childCount == 0)
            return;

        LineWrapper wrapper = wrapperList.get(0);
        int index = 0;
        int startBottom = wrapper.height + getPaddingTop();
        int startLeft = getPaddingLeft();

        int curBottom = startBottom;
        int curLeft = startLeft;

        for(int i = 0,wrapCount = wrapper.count ; i < childCount ; i ++) {
            View child = getChildAt(i);
            int childL = curLeft;
            int childT = curBottom - child.getMeasuredHeight();
            int childR = curLeft + child.getMeasuredWidth();
            int childB = curBottom;
            child.layout(childL,childT,childR,childB);

            wrapCount --;
            if(wrapCount == 0) {
                index++;
                if(index == wrapperList.size())
                    break;
                wrapper = wrapperList.get(index);
                wrapCount = wrapper.count;
                curLeft = startLeft;
                curBottom += verGap + wrapper.height;
            } else {
                curLeft += horGap + child.getMeasuredWidth();
            }
        }
    }


    /*内部处理方法*/
    private void init() {
        wrapperList = new ArrayList<>();
    }



    /*内部类*/
    private static class LineWrapper {
        int height = 0;
        int count = 0;
    }
}
