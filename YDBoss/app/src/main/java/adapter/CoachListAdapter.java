package adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidong.xh.ydboss.R;

import java.util.List;

import bean.CoachList;
import tools.GlideRoundTransform;

/**
 * Created by wjkj__xh on 2017/3/7.
 */

public class CoachListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private Context context;
    private List<CoachList> list;

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public CoachListAdapter(Context context, List<CoachList> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.coachitem, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            Glide.with(context).load(list.get(position).getImg()).transform(new GlideRoundTransform(context, 100)).into(((MyViewHolder)holder).coach_ima);
            ((MyViewHolder)holder).coach_name.setText("教练名称：" + list.get(position).getName().toString());
            ((MyViewHolder)holder).coach_sex.setText("性别：" + list.get(position).getSex().toString());
            ((MyViewHolder)holder).coach_workyear.setText("工作时间：" + list.get(position).getWorkyear() + "年");
            SpannableStringBuilder builder = new SpannableStringBuilder(((MyViewHolder)holder).coach_name.getText().toString());
            ForegroundColorSpan txtcolor = new ForegroundColorSpan(Color.parseColor("#00a1ff"));
            builder.setSpan(txtcolor, 5, (((MyViewHolder)holder).coach_name.getText().toString().length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((MyViewHolder)holder).coach_name.setText(builder);
            SpannableStringBuilder builder2 = new SpannableStringBuilder(((MyViewHolder)holder).coach_workyear.getText().toString());
            ForegroundColorSpan txtcolor2 = new ForegroundColorSpan(Color.parseColor("#ff8c00"));
            builder2.setSpan(txtcolor2, 5, (((MyViewHolder)holder).coach_workyear.getText().toString().length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((MyViewHolder)holder).coach_workyear.setText(builder2);
            holder.itemView.setTag(position);
        }catch (Exception e){
            Toast.makeText(context, "设置item的时候出错", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null){
            mOnItemClickListener.OnItemClick(v, (Integer) v.getTag());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView coach_ima;
        private TextView coach_name;
        private TextView coach_sex;
        private TextView coach_workyear;
        public MyViewHolder(View itemView) {
            super(itemView);
            coach_ima = (ImageView) itemView.findViewById(R.id.coach_ima);
            coach_name = (TextView) itemView.findViewById(R.id.coach_name);
            coach_sex = (TextView) itemView.findViewById(R.id.coach_sex);
            coach_workyear = (TextView) itemView.findViewById(R.id.coach_workyear);
        }
    }

    public static interface OnRecyclerViewItemClickListener{
        void OnItemClick(View view, int position);
    }
}
