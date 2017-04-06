package CustomFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yidong.xh.ydboss.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import adapter.GameListAdapter;
import bean.Common;
import bean.Game;
import tools.RecycleViewDivider;

/**
 * Created by wjkj__xh on 2017/3/3.
 */

public class GameListFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{

    private RecyclerView gamelist_recylerview;
    private String url = "http://www.one-gis.cn:911/YiDongWebService/WebService.asmx/GetGameByBid";
    private List<Game> list_game = new ArrayList<Game>();//商家赛事
    private List<Game> temp_game = new ArrayList<Game>();//赛事缓存
    private RadioGroup gamelist_rg;
    private RadioButton gamelist_rb_ing,gamelist_rb_pause,gamelist_rb_apply,gamelist_rb_close,gamelist_rb_end;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gamelist, container, false);
        gamelist_recylerview = (RecyclerView) view.findViewById(R.id.gamelist_recylerview);
        gamelist_rg = (RadioGroup) view.findViewById(R.id.gamelist_rg);
        gamelist_rb_ing = (RadioButton) view.findViewById(R.id.gamelist_rb_ing);
        gamelist_rb_pause = (RadioButton) view.findViewById(R.id.gamelist_rb_pause);
        gamelist_rb_apply = (RadioButton) view.findViewById(R.id.gamelist_rb_apply);
        gamelist_rb_close = (RadioButton) view.findViewById(R.id.gamelist_rb_close);
        gamelist_rb_end = (RadioButton) view.findViewById(R.id.gamelist_rb_end);
        gamelist_rg.setOnCheckedChangeListener(this);
        getGameLsit();
        return view;
    }

    private void getGameLsit(){
        RequestParams params = new RequestParams();
        params.addBodyParameter("bid", Common.business.getId() + "");
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            Message msg = new Message();

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (!(responseInfo.result.contains("nodata") || responseInfo.result.contains("failed"))) {
                    msg.what = 1;
                    msg.obj = responseInfo.result;
                } else {
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                msg.what = -1;
                handler.sendMessage(msg);
            }
        });
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    try {
                        Gson gson = new Gson();
                        String result = msg.obj.toString().substring(msg.obj.toString().indexOf("["),msg.obj.toString().lastIndexOf("]") + 1);
                        Type type = new TypeToken<List<Game>>() {}.getType();
                        list_game = gson.fromJson(result, type);
                        Toast.makeText(getActivity(), "解析成功", Toast.LENGTH_SHORT).show();
                        selectGamebyState("报名中");
                    }catch (Exception e){
                        Toast.makeText(getActivity(), "解析失败(原因：该商家为发布赛事或者其他原因)", Toast.LENGTH_SHORT).show();
                    }
                   
                    break;
                case -1:
                    Toast.makeText(getActivity(), "获取赛事失败（原因：该商家未发布或者其他原因）", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.gamelist_rb_ing:
                    if (selectGamebyState("进行中")){
                        if (gamelist_rb_ing.isChecked()){
                            gamelist_rb_ing.setTextColor(Color.parseColor("#ff8c00"));
                            gamelist_rb_pause.setTextColor(Color.parseColor("#666669"));
                            gamelist_rb_apply.setTextColor(Color.parseColor("#666669"));
                            gamelist_rb_close.setTextColor(Color.parseColor("#666669"));
                            gamelist_rb_end.setTextColor(Color.parseColor("#666669"));
                        }
                    }

                    break;
                case R.id.gamelist_rb_pause:
                    if (selectGamebyState("已暂停")){
                        if (gamelist_rb_pause.isChecked()){
                            gamelist_rb_ing.setTextColor(Color.parseColor("#666669"));
                            gamelist_rb_pause.setTextColor(Color.parseColor("#ff8c00"));
                            gamelist_rb_apply.setTextColor(Color.parseColor("#666669"));
                            gamelist_rb_close.setTextColor(Color.parseColor("#666669"));
                            gamelist_rb_end.setTextColor(Color.parseColor("#666669"));
                        }
                    }

                    break;
                case R.id.gamelist_rb_apply:
                    if (selectGamebyState("报名中")){
                        if (gamelist_rb_apply.isChecked()){
                            gamelist_rb_ing.setTextColor(Color.parseColor("#666669"));
                            gamelist_rb_pause.setTextColor(Color.parseColor("#666669"));
                            gamelist_rb_apply.setTextColor(Color.parseColor("#ff8c00"));
                            gamelist_rb_close.setTextColor(Color.parseColor("#666669"));
                            gamelist_rb_end.setTextColor(Color.parseColor("#666669"));
                        }
                    }

                    break;
                case R.id.gamelist_rb_close:
                    if (selectGamebyState("已关闭")){
                        if (gamelist_rb_close.isChecked()){
                            gamelist_rb_ing.setTextColor(Color.parseColor("#666669"));
                            gamelist_rb_pause.setTextColor(Color.parseColor("#666669"));
                            gamelist_rb_apply.setTextColor(Color.parseColor("#666669"));
                            gamelist_rb_close.setTextColor(Color.parseColor("#ff8c00"));
                            gamelist_rb_end.setTextColor(Color.parseColor("#666669"));
                        }
                    }

                    break;
                case R.id.gamelist_rb_end:
                    if (selectGamebyState("已结束")){
                        if (gamelist_rb_end.isChecked()){
                            gamelist_rb_ing.setTextColor(Color.parseColor("#666669"));
                            gamelist_rb_pause.setTextColor(Color.parseColor("#666669"));
                            gamelist_rb_apply.setTextColor(Color.parseColor("#666669"));
                            gamelist_rb_close.setTextColor(Color.parseColor("#666669"));
                            gamelist_rb_end.setTextColor(Color.parseColor("#ff8c00"));
                        }
                    }

                    break;
            }
    }

    private boolean selectGamebyState(String state){
        temp_game.clear();
        for (int i = 0; i < list_game.size(); i++){
            if (list_game.get(i).getState().equals(state)){
                temp_game.add(list_game.get(i));
            }
        }if (temp_game.size() > 0){
            initRecylerView(temp_game);
            return true;
        }else {
            Toast.makeText(getActivity(), "暂无此类赛事", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void initRecylerView(final List<Game> list){
        GameListAdapter adapter = new GameListAdapter(getActivity(), list);
        gamelist_recylerview.setAdapter(adapter);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(getActivity());
        gamelist_recylerview.setLayoutManager(layoutmanager);
        gamelist_recylerview.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
        adapter.setOnItemClickListener(new GameListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), "点击的是" + list.get(position).getName().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
