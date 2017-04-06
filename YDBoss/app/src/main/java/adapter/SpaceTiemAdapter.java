package adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yidong.xh.ydboss.R;

import java.util.List;

import bean.Common;
import bean.Lesson;
import bean.SpaceItem;

/**
 * Created by wjkj__xh on 2017/3/6.
 */

public class SpaceTiemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<SpaceItem> list;
    private List<Lesson> list_lesson;
    private String[] time_A;
    private String[] time_B;
    private String begin;
    private String end;

    public SpaceTiemAdapter(Context context, List<SpaceItem> list, List<Lesson> list_lesson) {
        this.context = context;
        this.list = list;
        this.list_lesson = list_lesson;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.spacetimeitem, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (Common.business.getB_ui_type_name().equals("场地")) {
            try {
                time_A = list.get(position).getStarttime().split(" ")[1].split(":");
                begin = time_A[0] + ":" + time_A[1];
            } catch (Exception e) {
                begin = "待定";
            }
            try {
                time_B = list.get(position).getEndtime().split(" ")[1].split(":");
                end = time_B[0] + ":" + time_B[1];
            } catch (Exception e) {
                end = "待定";
            }

            ((MyViewHolder) holder).spaceitem_item_time.setText(begin + "\n" + end + "结束");
            SpannableString spannableString = new SpannableString(((MyViewHolder) holder).spaceitem_item_time.getText().toString());
            spannableString.setSpan(new RelativeSizeSpan(1.2f), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), 6, ((MyViewHolder) holder).spaceitem_item_time.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((MyViewHolder) holder).spaceitem_item_time.setText(spannableString);
            ((MyViewHolder) holder).spaceitem_item_money.setText("￥" + list.get(position).getPrice());
        } else {
            try {
                time_A = list_lesson.get(position).getStarttime().split(" ")[1].split(":");
                begin = time_A[0] + ":" + time_A[1];
            } catch (Exception e) {
                begin = "待定";
            }
            try {
                time_B = list_lesson.get(position).getEndtime().split(" ")[1].split(":");
                end = time_B[0] + ":" + time_B[1];
            } catch (Exception e) {
                end = "待定";
            }

            ((MyViewHolder) holder).spaceitem_item_time.setText(begin + "\n" + end + "结束");
            SpannableString spannableString = new SpannableString(((MyViewHolder) holder).spaceitem_item_time.getText().toString());
            spannableString.setSpan(new RelativeSizeSpan(1.2f), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), 6, ((MyViewHolder) holder).spaceitem_item_time.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((MyViewHolder) holder).spaceitem_item_time.setText(spannableString);
            ((MyViewHolder) holder).spaceitem_item_money.setText("￥" + list_lesson.get(position).getPrice());
        }

    }

    @Override
    public int getItemCount() {
        if (Common.business.getB_ui_type_name().equals("场地")) {
            return list.size();
        } else {
            return list_lesson.size();
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView spaceitem_item_time;
        private TextView spaceitem_item_money;

        public MyViewHolder(View itemView) {
            super(itemView);
            spaceitem_item_time = (TextView) itemView.findViewById(R.id.spaceitem_item_time);
            spaceitem_item_money = (TextView) itemView.findViewById(R.id.spaceitem_item_money);
        }
    }
}
