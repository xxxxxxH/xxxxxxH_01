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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidong.xh.ydboss.R;

import java.util.List;

import bean.Goods;

/**
 * Created by wjkj__xh on 2017/3/8.
 */

public class GoodsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context context;
    private List<Goods> list;
    private ItemClickListener itemClickListener = null;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public GoodsListAdapter(Context context, List<Goods> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.goodsitem, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try{
            String[] imgs = list.get(position).getImg().split(";");
            if (imgs[0].equals("")){
                Glide.with(context).load(R.mipmap.coach_bg).into(((MyViewHolder)holder).goods_ima);
            }else {
                Glide.with(context).load(imgs[0]).into(((MyViewHolder)holder).goods_ima);
            }
            ((MyViewHolder)holder).goods_tv_name.setText(list.get(position).getName().toString());
            ((MyViewHolder)holder).goods_tv_price.setText("价格：￥" + list.get(position).getPrice());
            ((MyViewHolder)holder).goods_tv_original_price.setText("原价：￥" + list.get(position).getOriginal_price());
            SpannableStringBuilder builder = new SpannableStringBuilder(((MyViewHolder)holder).goods_tv_price.getText().toString());
            ForegroundColorSpan txtcolor = new ForegroundColorSpan(Color.parseColor("#f6983e"));
            builder.setSpan(txtcolor, 3, (((MyViewHolder)holder).goods_tv_price.getText().toString().length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((MyViewHolder)holder).goods_tv_price.setText(builder);
            ((MyViewHolder)holder).goods_tv_original_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
            holder.itemView.setTag(position);
        }catch (Exception e){
            Toast.makeText(context, "设置Item的时候出错", Toast.LENGTH_SHORT).show();
        }
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

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView goods_ima;
        private TextView goods_tv_name;
        private TextView goods_tv_price;
        private TextView goods_tv_original_price;
        public MyViewHolder(View itemView) {
            super(itemView);
            goods_ima = (ImageView) itemView.findViewById(R.id.goods_ima);
            goods_tv_name = (TextView) itemView.findViewById(R.id.goods_tv_name);
            goods_tv_price = (TextView) itemView.findViewById(R.id.goods_tv_price);
            goods_tv_original_price = (TextView) itemView.findViewById(R.id.goods_tv_original_price);
        }
    }
}
