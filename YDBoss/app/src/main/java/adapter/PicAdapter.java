package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yidong.xh.ydboss.R;

/**
 * Created by wjkj__xh on 2017/2/28.
 */

public class PicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private String[] pics;

    public PicAdapter(Context context, String[] pics){
        this.context = context;
        this.pics = pics;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.picitem, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            Glide.with(context).load(pics[position]).into(((MyViewHolder)holder).picitem_ima);
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return pics.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView picitem_ima;

            public MyViewHolder(View view){
                super(view);
                picitem_ima = (ImageView) view.findViewById(R.id.picitem_ima);
            }
    }
}
