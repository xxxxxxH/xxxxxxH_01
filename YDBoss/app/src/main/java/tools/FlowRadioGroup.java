package tools;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

public class FlowRadioGroup extends RadioGroup {
	private List<List<View>> mAllViews;//���������е�����View
	private List<Integer> mLineHeight;//����ÿһ�е��и�
	
	public FlowRadioGroup(Context context) {  
        super(context);  
    }  
  
    public FlowRadioGroup(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
    
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
    	return (LayoutParams) new MarginLayoutParams(getContext(), attrs);
    }
  

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0, height = 0;
        int lineWidth = 0, lineHeight = 0;
        int childWidth = 0, childHeight = 0;

        mAllViews = new ArrayList<List<View>>();
        mLineHeight = new ArrayList<Integer>();

        List<View> lineViews = new ArrayList<View>();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();

            childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);

                lineWidth = childWidth;
                lineHeight = childHeight;
                lineViews = new ArrayList<View>();
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(childHeight, lineHeight);
            }
            lineViews.add(child);

            if (i == (count - 1)) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);
        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(modeWidth == MeasureSpec.AT_MOST ? width : sizeWidth, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = getPaddingTop();//��ʼ������view�� top����
        int left = getPaddingLeft();//��ʼ������view�� left���릻

        int lineNum = mAllViews.size();//����
        List<View> lineView;
        int lineHeight;
        for (int i = 0; i < lineNum; i++) {
            lineView = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            int lineNumber;
            if (lineView.size() < 2) {
                lineNumber = lineView.size();
            } else {
                lineNumber = 2;
            }
            for (int j = 0; j < lineNumber; j++) {
                View child = lineView.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();
                int ld = left + params.leftMargin;
                int td = top + params.topMargin;
                int rd = ld + child.getMeasuredWidth();//����Ҫ���� params.rightMargin,
                int bd = td + child.getMeasuredHeight();//����Ҫ���� params.bottomMargin, ��Ϊ�� onMeasure , ���Ѿ������� lineHeight ��
                child.layout(ld, td, rd, bd);

                left += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;//��Ϊ�� ���������;
            }

            left = getPaddingLeft();
            top += lineHeight;
        }
    }
	
}
