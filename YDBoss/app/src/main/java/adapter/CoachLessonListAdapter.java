package adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yidong.xh.ydboss.R;

import java.util.List;

import bean.CoachLesson;

/**
 * Created by wjkj__xh on 2017/3/7.
 */

public class CoachLessonListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<CoachLesson> list;
    private MyOnItemClikListener myOnItemClikListener = null;

    public void setMyOnItemClikListener(MyOnItemClikListener myOnItemClikListener) {
        this.myOnItemClikListener = myOnItemClikListener;
    }

    public CoachLessonListAdapter(Context context, List<CoachLesson> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.coachlessonitem, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder)holder).coachlessonitem_name.setText("课程名称：" + list.get(position).getName());
        ((MyViewHolder)holder).coachlessonitem_price.setText("课程价格：" + list.get(position).getPrice());
        ((MyViewHolder)holder).coachlessonitem_original_price.setText("课程原价：" + list.get(position).getOriginal_price());
        SpannableStringBuilder builder = new SpannableStringBuilder(((MyViewHolder)holder).coachlessonitem_price.getText().toString());
        ForegroundColorSpan textcolor = new ForegroundColorSpan(Color.parseColor("#ff8c00"));
        builder.setSpan(textcolor, 5, ((MyViewHolder)holder).coachlessonitem_price.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((MyViewHolder)holder).coachlessonitem_price.setText(builder);
        ((MyViewHolder)holder).coachlessonitem_original_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView coachlessonitem_name;
        private TextView coachlessonitem_price;
        private TextView coachlessonitem_original_price;

        public MyViewHolder(View itemView) {
            super(itemView);
            coachlessonitem_name = (TextView) itemView.findViewById(R.id.coachlessonitem_name);
            coachlessonitem_price = (TextView) itemView.findViewById(R.id.coachlessonitem_price);
            coachlessonitem_original_price = (TextView) itemView.findViewById(R.id.coachlessonitem_original_price);
        }
    }

    public static interface MyOnItemClikListener{
        void OnItemClick(View view, int position);
    }

    @Override
    public void onClick(View v) {
        if (myOnItemClikListener != null){
            myOnItemClikListener.OnItemClick(v, (Integer) v.getTag());
        }
    }
}
