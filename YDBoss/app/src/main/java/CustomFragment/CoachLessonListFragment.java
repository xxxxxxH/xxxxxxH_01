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

import adapter.CoachLessonListAdapter;
import bean.CoachLesson;
import bean.Common;
import tools.RecycleViewDivider;

/**
 * Created by wjkj__xh on 2017/3/7.
 */

public class CoachLessonListFragment extends Fragment{

    private View view;
    private RecyclerView coachlessonlist_re;
    private String url = "http://www.one-gis.cn:911/YiDongWebService/WebService.asmx/CoachCourseGetPK";
    private List<CoachLesson> list_lesson = new ArrayList<CoachLesson>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.coachlessonlist, container, false);
        coachlessonlist_re = (RecyclerView) view.findViewById(R.id.coachlessonlist_re);
        getCoachLesson();
        return view;
    }

    public void getCoachLesson() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("strBusinessID", "");
        params.addBodyParameter("strCoachID", Common.CoachDetails.getId() + "");
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
                    try{
                        Gson gson = new Gson();
                        String result = msg.obj.toString().substring(msg.obj.toString().indexOf("["),msg.obj.toString().lastIndexOf("]") + 1);
                        Type type = new TypeToken<List<CoachLesson>>() {}.getType();
                        list_lesson = gson.fromJson(result, type);
                        CoachLessonListAdapter adapter = new CoachLessonListAdapter(getActivity(), list_lesson);
                        coachlessonlist_re.setAdapter(adapter);
                        coachlessonlist_re.setLayoutManager(new LinearLayoutManager(getActivity()));
                        coachlessonlist_re.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.HORIZONTAL));
                        adapter.setMyOnItemClikListener(new CoachLessonListAdapter.MyOnItemClikListener() {
                            @Override
                            public void OnItemClick(View view, int position) {
                                Toast.makeText(getActivity(), "点击的是" + position, Toast.LENGTH_SHORT).show();
                                Common.Coachlesson = null;
                                Common.Coachlesson = list_lesson.get(position);
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.replace(R.id.content, new CoachLessonDetailsFragemnt());
                                ft.addToBackStack(null);
                                ft.commit();
                            }
                        });
                    }catch (Exception e){
                        Toast.makeText(getActivity(), "解析失败（原因：获取数据成功但是解析失败）", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case -1:
                    Toast.makeText(getActivity(), "获取课程信息失败（原因：商家暂未发布课程或者网络连接失败）", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
