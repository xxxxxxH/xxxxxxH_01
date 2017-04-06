package CustomFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yidong.xh.ydboss.R;

import bean.Common;

/**
 * Created by wjkj__xh on 2017/3/7.
 */

public class CoachLessonDetailsFragemnt extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.coachlessondetails, container, false);
        initView();
        return view;
    }

    private void initView() {
        TextView coachlessonitemdetails_lesson_price = (TextView) view.findViewById(R.id.coachlessonitemdetails_lesson_price);
        TextView coachlessonitemdetails_lesson_original_price = (TextView) view.findViewById(R.id.coachlessonitemdetails_lesson_original_price);
        TextView coachlessonitemdetails_lesson_hours = (TextView) view.findViewById(R.id.coachlessonitemdetails_lesson_hours);
        TextView coachlessonitemdetails_lesson_c_name = (TextView) view.findViewById(R.id.coachlessonitemdetails_lesson_c_name);
        TextView coachlessonitemdetails_lesson_explain = (TextView) view.findViewById(R.id.coachlessonitemdetails_lesson_explain);
        TextView coachlessonitemdetails_lesson_details = (TextView) view.findViewById(R.id.coachlessonitemdetails_lesson_details);
        coachlessonitemdetails_lesson_price.setText("课程价格：" + Common.Coachlesson.getPrice());
        SpannableStringBuilder builder = new SpannableStringBuilder(coachlessonitemdetails_lesson_price.getText().toString());
        ForegroundColorSpan textcolor = new ForegroundColorSpan(Color.parseColor("#ff8c00"));
        builder.setSpan(textcolor, 5, coachlessonitemdetails_lesson_price.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        coachlessonitemdetails_lesson_price.setText(builder);
        coachlessonitemdetails_lesson_original_price.setText("课程原价：" + Common.Coachlesson.getOriginal_price());
        int at_least_buy = Common.Coachlesson.getAt_least_buy();
        int limit_buy = Common.Coachlesson.getLimit_buy();
        if (at_least_buy != 0 && limit_buy != 0) {
            coachlessonitemdetails_lesson_explain.setText("购买说明: " + at_least_buy + "节起购" + "限购" + limit_buy + "节");
        }else if (at_least_buy == 0 && limit_buy != 0) {
            coachlessonitemdetails_lesson_explain.setText("购买说明: " + "限购" + limit_buy + "节");
        }else if (at_least_buy != 0 && limit_buy == 0) {
            coachlessonitemdetails_lesson_explain.setText("购买说明: " + at_least_buy + "节起购");
        }else {
            coachlessonitemdetails_lesson_explain.setText("购买说明: 无购买限制");
        }
        String lesson_hours = Common.Coachlesson.getHours();
        if (lesson_hours != null && !lesson_hours.equals("")) {
            try {
                String[] hString = lesson_hours.split(";");
                int startmiu = Integer.valueOf(hString[0]) % 60;
                int endmiu = Integer.valueOf(hString[1]) % 60;
                int starthours = Integer.valueOf(hString[0]) / 60;
                int endhours = Integer.valueOf(hString[1]) / 60;
                if (startmiu == 0 && endmiu == 0) {
                    coachlessonitemdetails_lesson_hours.setText("使用时间: " + starthours+ ":00"+"-"+endhours+ ":00");
                }else {
                    if (startmiu == 0 && endmiu != 0) {
                        coachlessonitemdetails_lesson_hours.setText("使用时间: " + starthours+ ":00"+"-"+endhours+ ":"+endmiu);
                    }else if (endmiu % 60 == 0 && startmiu != 0) {
                        coachlessonitemdetails_lesson_hours.setText("使用时间: " + starthours+ ":"+startmiu+"-"+endhours+ ":00");
                    }else {
                        coachlessonitemdetails_lesson_hours.setText("使用时间: " + starthours+ ":"+startmiu+"-"+endhours+ ":"+endmiu);
                    }
                }
            } catch (Exception e) {
                coachlessonitemdetails_lesson_hours.setText("使用时间: 9:00-18:00");
            }
        }else {
            coachlessonitemdetails_lesson_hours.setText("使用时间: 9:00-18:00");
        }
        coachlessonitemdetails_lesson_c_name.setText("教练名称：" + Common.Coachlesson.getC_name());
        coachlessonitemdetails_lesson_details.setText(Common.Coachlesson.getDetails().toString());
    }
}
