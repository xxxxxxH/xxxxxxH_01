package CustomFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import adapter.SpaceListAdapter;
import adapter.SpaceListAdapter.OnRecyclerViewItemClickListener;
import bean.Common;
import bean.SpaceList;

/**
 * Created by wjkj__xh on 2017/3/6.
 */

public class SpaceGameLsitFragemnt extends Fragment {

    private RecyclerView spacegamelist_re;
    private String url = "http://www.one-gis.cn:911/YiDongWebService/WebService.asmx/GetSpaceTimeType";
    private List<SpaceList> list_space = new ArrayList<SpaceList>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.spacegamelist, container, false);
        getSpaceList();
        spacegamelist_re = (RecyclerView) view.findViewById(R.id.spacegamelist_re);
        return view;
    }

    private void getSpaceList(){
        RequestParams params = new RequestParams();
        params.addBodyParameter("strBusinessID", Common.business.getId() + "");
        params.addBodyParameter("strType", Common.type);
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            Message msg = new Message();

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo.result.contains("failed") || responseInfo.result.contains("nodata")){
                    msg.what = -1;
                }else {
                    msg.what = 1;
                    msg.obj = responseInfo.result;
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
                        String result = msg.obj.toString().substring(msg.obj.toString().indexOf("["),msg.obj.toString().lastIndexOf("]") + 1);
                        Type type = new TypeToken<List<SpaceList>>(){}.getType();
                        Gson gson = new Gson();
                        list_space = gson.fromJson(result, type);
                        SpaceListAdapter adapter = new SpaceListAdapter(getActivity(), list_space);
                        spacegamelist_re.setAdapter(adapter);
                        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                        spacegamelist_re.setLayoutManager(manager);
                        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Common.spaceList = list_space.get(position);
                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.content, new SpaceDetailsFragment());
                                transaction.addToBackStack(null);
                                transaction.commit();
                                Toast.makeText(getActivity(), "点击的是" + position, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }catch (Exception e){
                        Toast.makeText(getActivity(), "解析出错", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case -1:
                    Toast.makeText(getActivity(), "获取预约失败(原因：网络连接失败或者该商家暂未发布预约)", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
