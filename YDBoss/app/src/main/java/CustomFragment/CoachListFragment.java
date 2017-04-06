package CustomFragment;

import android.graphics.Color;
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

import adapter.CoachListAdapter;
import bean.CoachList;
import bean.Common;
import tools.RecycleViewDivider;

/**
 * Created by wjkj__xh on 2017/3/7.
 */

public class CoachListFragment extends Fragment{

    private String url = "http://www.one-gis.cn:911/YiDongWebService/WebService.asmx/CoachGetPK";
    private List<CoachList> list_coach = new ArrayList<CoachList>();
    private RecyclerView coachlist_re;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coachlist, container, false);
        coachlist_re = (RecyclerView) view.findViewById(R.id.coachlist_re);
        getCoachList();
        return view;
    }

    private void getCoachList(){
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
                hanler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                msg.what = -1;
                hanler.sendMessage(msg);
            }
        });
    }

    public Handler hanler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    Toast.makeText(getActivity(), "获取教练失败（原因：该商家暂未发布或者网络连接失败）", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    try {
                        Gson gson = new Gson();
                        String result = msg.obj.toString().substring(msg.obj.toString().indexOf("["),msg.obj.toString().lastIndexOf("]") + 1);
                        Type type = new TypeToken<List<CoachList>>() {}.getType();
                        list_coach = gson.fromJson(result, type);
                        CoachListAdapter adapter = new CoachListAdapter(getActivity(), list_coach);
                        coachlist_re.setAdapter(adapter);
                        coachlist_re.setLayoutManager(new LinearLayoutManager(getActivity()));
                        coachlist_re.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.HORIZONTAL, 1, Color.parseColor("#6c6c6c")));
                        adapter.setmOnItemClickListener(new CoachListAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void OnItemClick(View view, int position) {
                                Toast.makeText(getActivity(), "点击的是" + position, Toast.LENGTH_SHORT).show();
                                Common.CoachDetails = null;
                                Common.CoachDetails = list_coach.get(position);
                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.content, new CoachDetailsFragment());
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        });
                    }catch (Exception e){
                        Toast.makeText(getActivity(), "出错（原因：adapter或者其他）" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
}
