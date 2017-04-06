package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yidong.xh.ydboss.R;

import java.util.List;

import bean.Recruit;

/**
 * Created by wjkj__xh on 2017/3/8.
 */

public class RecruitListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context context;
    private List<Recruit> list;
    private ItemClickListener itemClickListener = null;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public RecruitListAdapter(Context context, List<Recruit> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recruititem, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            ((MyViewHolder)holder).recuritlistitem_tv_name.setText("职位名称：" + list.get(position).getJob_name());
            ((MyViewHolder)holder).recuritlistitem_bname.setText(list.get(position).getB_name().toString());
            ((MyViewHolder)holder).recuritlistitem_tv_money.setText(list.get(position).getSalary().toString());
        }catch (Exception e){
            Toast.makeText(context, "设置Item的时候出错", Toast.LENGTH_SHORT).show();
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null){
            itemClickListener.ItemClick(v, (Integer) v.getTag());
        }
    }

    public static interface ItemClickListener{
        void ItemClick(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView recuritlistitem_tv_name;
        private TextView recuritlistitem_bname;
        private TextView recuritlistitem_tv_money;

        public MyViewHolder(View itemView) {
            super(itemView);
            recuritlistitem_tv_name = (TextView) itemView.findViewById(R.id.recuritlistitem_tv_name);
            recuritlistitem_bname = (TextView) itemView.findViewById(R.id.recuritlistitem_bname);
            recuritlistitem_tv_money = (TextView) itemView.findViewById(R.id.recuritlistitem_tv_money);
        }
    }
}
