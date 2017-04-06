package adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidong.xh.ydboss.R;

import java.util.List;

import bean.SpaceList;

/**
 * Created by wjkj__xh on 2017/3/6.
 */

public class SpaceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnClickListener{

    private Context context;
    private List<SpaceList> list;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public SpaceListAdapter(Context context, List<SpaceList> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.spaceitem, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!list.get(position).getImg().toString().equals("")){
            Glide.with(context).load(list.get(position).getImg().toString()).into(((MyViewHolder)holder).spaceitem_ima);
        }
        ((MyViewHolder)holder).spaceitem_tv.setText(list.get(position).getWar_number() + "人制" + "（" + list.get(position).getProperty().toString() +"）");
        ((MyViewHolder)holder).spaceitem_tv_kind.setText("报名方式：" + list.get(position).getSale_form().toString());
        SpannableStringBuilder builder = new SpannableStringBuilder(((MyViewHolder)holder).spaceitem_tv_kind.getText().toString());
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#ff8c00"));
        builder.setSpan(span, 5, ((MyViewHolder)holder).spaceitem_tv_kind.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((MyViewHolder)holder).spaceitem_tv_kind.setText(builder);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView spaceitem_ima;
        private TextView spaceitem_tv;
        private TextView spaceitem_tv_kind;

        public MyViewHolder(View itemView) {
            super(itemView);
            spaceitem_ima = (ImageView) itemView.findViewById(R.id.spaceitem_ima);
            spaceitem_tv = (TextView) itemView.findViewById(R.id.spaceitem_tv);
            spaceitem_tv_kind = (TextView) itemView.findViewById(R.id.spaceitem_tv_kind);
        }
    }

    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view, int position);
    }
}
