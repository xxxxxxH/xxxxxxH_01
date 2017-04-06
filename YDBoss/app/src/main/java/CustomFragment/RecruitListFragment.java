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
import android.widget.LinearLayout;
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

import adapter.RecruitListAdapter;
import bean.Common;
import bean.Recruit;
import tools.RecycleViewDivider;

/**
 * Created by wjkj__xh on 2017/3/8.
 */

public class RecruitListFragment extends Fragment{
    
    private RecyclerView recruitlist_re;
    private String url = "http://www.one-gis.cn:911/YiDongWebService/WebService.asmx/RecruitGetPK";
    private List<Recruit> list_recruit = new ArrayList<Recruit>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recruitlist, container, false);
        recruitlist_re = (RecyclerView) view.findViewById(R.id.recruitlist_re);
        getRecruitLsit();
        return view;
    }

    public void getRecruitLsit() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("strBusinessID", Common.business.getId() + "");
        params.addBodyParameter("strID", "");
        params.addBodyParameter("strName", "");
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            Message msg = new Message();

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo.result.contains("nodata") || responseInfo.result.contains("failed")){
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
                        Gson gson = new Gson();
                        String result = msg.obj.toString().substring(msg.obj.toString().indexOf("["),msg.obj.toString().lastIndexOf("]") + 1);
                        Type type = new TypeToken<List<Recruit>>(){}.getType();
                        list_recruit = gson.fromJson(result, type);
                        RecruitListAdapter adapter = new RecruitListAdapter(getActivity(), list_recruit);
                        recruitlist_re.setAdapter(adapter);
                        recruitlist_re.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recruitlist_re.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.HORIZONTAL));
                        adapter.setItemClickListener(new RecruitListAdapter.ItemClickListener() {
                            @Override
                            public void ItemClick(View view, int position) {
                                Toast.makeText(getActivity(), "点击的是" + position, Toast.LENGTH_SHORT).show();
                                Common.Recruit = null;
                                Common.Recruit = list_recruit.get(position);
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.content, new RecruitDetailsFragment());
                                ft.addToBackStack(null);
                                ft.commit();
                            }
                        });
                    }catch (Exception e){
                        Toast.makeText(getActivity(), "解析出错", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case -1:
                    Toast.makeText(getActivity(), "获取招聘列表失败（原因：该商家暂未发布招聘或者网络连接失败）", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
