package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidong.xh.ydboss.R;

import CustomFragment.AllBusinessFragment;
import bean.Business;
import bean.Common;

import java.util.List;

/**
 * Created by wjkj__xh on 2017/2/27.
 */

public class AllBusinessAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context context;
    private List<Business> list;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public AllBusinessAdapter(Context context, List<Business> list){
        this.context = context;
        this.list = list;
    }

    public void resetAdapter(List<Business> mlist){
        list = mlist;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.allbusiness_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int heigth = metrics.heightPixels;
        int width = metrics.widthPixels;
        ((MyViewHolder)holder).allbusiness_item_rl.getLayoutParams().height = heigth / 3;
        ((MyViewHolder)holder).allbusiness_item_rl.getLayoutParams().width = width;

        ((MyViewHolder)holder).allbusiness_item_fr.getLayoutParams().height = heigth / 3;
        ((MyViewHolder)holder).allbusiness_item_fr.getLayoutParams().width = width;

        ((MyViewHolder)holder).allbusiness_item_ima.getLayoutParams().width = width;
        ((MyViewHolder)holder).allbusiness_item_ima.getLayoutParams().height = heigth / 3;

        Glide.with(context).load(list.get(position).getImg_main()).into(((MyViewHolder) holder).allbusiness_item_ima);
        ((MyViewHolder)holder).allbusiness_item_name.setText(list.get(position).getName());
        ((MyViewHolder)holder).allbusiness_item_address.setText(list.get(position).getAddress());
        if (Common.WhichBusiness.equals("all")){
            if (list.get(position).getHas_discount() != null && !list.get(position).getHas_discount().equals("")){
                ((MyViewHolder)holder).allbusiness_item_dis.setVisibility(View.VISIBLE);
            }else {
                ((MyViewHolder)holder).allbusiness_item_dis.setVisibility(View.GONE);
            }
        }else if (Common.WhichBusiness.equals("favorable")){
            ((MyViewHolder)holder).allbusiness_item_dis.setVisibility(View.VISIBLE);
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView allbusiness_item_ima;
        private TextView allbusiness_item_name;
        private TextView allbusiness_item_address;
        private ImageView allbusiness_item_dis;
        private RelativeLayout allbusiness_item_rl;
        private FrameLayout allbusiness_item_fr;

        public MyViewHolder(View view) {
            super(view);
            allbusiness_item_ima = (ImageView) view.findViewById(R.id.allbusiness_item_ima);
            allbusiness_item_name = (TextView) view.findViewById(R.id.allbusiness_item_name);
            allbusiness_item_address = (TextView) view.findViewById(R.id.allbusiness_item_address);
            allbusiness_item_dis = (ImageView) view.findViewById(R.id.allbusiness_item_dis);
            allbusiness_item_rl = (RelativeLayout) view.findViewById(R.id.allbusiness_item_rl);
            allbusiness_item_fr = (FrameLayout) view.findViewById(R.id.allbusiness_item_fr);
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }
}
