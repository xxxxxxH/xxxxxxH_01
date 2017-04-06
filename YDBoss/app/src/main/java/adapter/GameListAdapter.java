package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yidong.xh.ydboss.R;

import java.util.List;

import bean.Game;

/**
 * Created by wjkj__xh on 2017/3/3.
 */

public class GameListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context context;
    private List<Game> list_game;
    private GameListAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public GameListAdapter(Context context, List<Game> list_game) {
        this.context = context;
        this.list_game = list_game;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gamelist_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            Glide.with(context).load(list_game.get(position).getImg().toString()).into(((MyViewHolder) holder).gamelist_item_gameimg);
            ((MyViewHolder) holder).gamelist_item_gamename.setText(list_game.get(position).getName().toString());
            ((MyViewHolder) holder).gamelist_item_addresss.setText("比赛地址:" + list_game.get(position).getAddress().toString());
            ((MyViewHolder) holder).gamelist_item_endtime.setText("报名截止:" + list_game.get(position).getApply_endtime().toString());
        }catch (Exception e){
            Toast.makeText(context, "在设置item的时候出错了" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list_game.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView gamelist_item_gameimg;
        private TextView gamelist_item_gamename;
        private TextView gamelist_item_addresss;
        private TextView gamelist_item_endtime;

        public MyViewHolder(View view) {
            super(view);
            gamelist_item_gameimg = (ImageView) view.findViewById(R.id.gamelist_item_gameimg);
            gamelist_item_gamename = (TextView) view.findViewById(R.id.gamelist_item_gamename);
            gamelist_item_addresss = (TextView) view.findViewById(R.id.gamelist_item_addresss);
            gamelist_item_endtime = (TextView) view.findViewById(R.id.gamelist_item_endtime);
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }
}
