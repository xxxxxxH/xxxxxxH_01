package CustomFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import adapter.SpaceTiemAdapter;
import adapter.ViewPagerAdapter;
import bean.Common;
import bean.Lesson;
import bean.SpaceItem;
import tools.RecycleViewDivider;

/**
 * Created by wjkj__xh on 2017/3/6.
 */

public class SpaceDetailsFragment extends Fragment implements ViewPager.OnPageChangeListener{

    private ViewPager spaceitem_vp;
    private View view;
    private LinearLayout spaceitem_dot,ll_time_spacedetails;
    private TextView spacedetails_kind,spacedetails_property,spacedetails_address;
    private RecyclerView spacedetails_re;
    private String url = "http://www.one-gis.cn:911/YiDongWebService/WebService.asmx/SpaceTimeGetPK";
    private String url2 = "http://www.one-gis.cn:911/YiDongWebService/WebService.asmx/CurriculumTimeGet";
    private List<SpaceItem> list_spaceitem = new ArrayList<SpaceItem>();
    private List<SpaceItem> temp_spaceitem = new ArrayList<SpaceItem>();
    private List<SpaceItem> temp_spaceitem_after = new ArrayList<SpaceItem>();

    private List<Lesson> list_lesson = new ArrayList<Lesson>();
    private List<Lesson> temp_lesson = new ArrayList<Lesson>();
    private String[] pics = new String[]{};
    private ImageView[] dot;
    private int year = 0;
    private int month = 0;
    private int day = 0;
    private SpaceTiemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.spacedetails, container, false);
        initView();
        if (Common.business.getB_ui_type_name().equals("场地")){
            getSpaceDeatails();
        }else {
            getLessonDetails();
        }
        return view;
    }

    private void initView() {
        Common.WhichFragemntNeedViewPagerAdapter = "spacedetails";
        spaceitem_vp = (ViewPager) view.findViewById(R.id.spaceitem_vp);
        spaceitem_dot = (LinearLayout) view.findViewById(R.id.spaceitem_dot);
        ll_time_spacedetails = (LinearLayout) view.findViewById(R.id.ll_time_spacedetails);
        spacedetails_kind = (TextView) view.findViewById(R.id.spacedetails_kind);
        spacedetails_property = (TextView) view.findViewById(R.id.spacedetails_property);
        spacedetails_address = (TextView) view.findViewById(R.id.spacedetails_address);
        spacedetails_re = (RecyclerView) view.findViewById(R.id.spacedetails_re);
        spaceitem_vp.setOnPageChangeListener(this);
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = (Calendar.getInstance().get(Calendar.MONTH) + 1);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取课程预约详情
     * */
    private void getLessonDetails(){
        RequestParams params = new RequestParams();
        params.addBodyParameter("strBusinessID",Common.business.getId() + "");
        params.addBodyParameter("strCurriculumID",Common.type);
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.POST, url2, params, new RequestCallBack<String>() {

            Message msg = new Message();

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo.result.contains("nodata") || responseInfo.result.contains("failed")){
                    msg.what = -1;
                }else {
                    msg.what = 2;
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
    /**
     * 获取场地预约详情
     * */
    public void getSpaceDeatails() {
        RequestParams params = new RequestParams();
        Gson gson = new Gson();
        String json = gson.toJson(Common.spaceList);
        params.addBodyParameter("json", json);
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
                case -1:
                    Toast.makeText(getActivity(), "解析失败(原因：未发布预约或者网络连接失败)", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    try {
                        String result = msg.obj.toString().substring(msg.obj.toString().indexOf("["),msg.obj.toString().lastIndexOf("]") + 1);
                        Type type = new TypeToken<List<SpaceItem>>(){}.getType();
                        Gson gson = new Gson();
                        list_spaceitem = gson.fromJson(result, type);
                        spacedetails_kind.setText(list_spaceitem.get(0).getWar_number() + "人制" + list_spaceitem.get(0).getSale_form());
                        spacedetails_property.setText("场地属性：" + list_spaceitem.get(0).getProperty());
                        setTextColor(spacedetails_property,5);
                        spacedetails_address.setText("场地地址：" + list_spaceitem.get(0).getAddress());
                        setTextColor(spacedetails_address,5);
                        pics = list_spaceitem.get(0).getImg().split(";");
                        if (!pics[0].equals("")){
                            dot = new ImageView[pics.length];
                            for (int i = 0; i < pics.length; i++){
                                ImageView imageView = new ImageView(getActivity());
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
                                params.setMargins(10, 0, 10, 0);
                                imageView.setLayoutParams(params);
                                dot[i] = imageView;
                                if (i == 0){
                                    imageView.setBackgroundResource(R.mipmap.point_select);
                                }else {
                                    imageView.setBackgroundResource(R.mipmap.page_select);
                                }
                                spaceitem_dot.addView(dot[i]);
                            }
                        }
                        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(), pics);
                        spaceitem_vp.setAdapter(adapter);
                        spaceitem_vp.setCurrentItem(0);
                        SpaceTime(list_spaceitem);
                        int month_ = Integer.parseInt(list_spaceitem.get(0).getStarttime().split(" ")[0].split("/")[0]);
                        int day_ = Integer.parseInt(list_spaceitem.get(0).getStarttime().split(" ")[0].split("/")[1]);
                        at_date(list_spaceitem, dateFormat(month_,day_));
                    }catch (Exception e){
                        Toast.makeText(getActivity(), "界面出错(原因：解析出错或者viewpager)" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    try {
                        String result_lesson = msg.obj.toString().substring(msg.obj.toString().indexOf("["),msg.obj.toString().lastIndexOf("]") + 1);
                        Type type2 = new TypeToken<List<Lesson>>(){}.getType();
                        Gson gson = new Gson();
                        list_lesson = gson.fromJson(result_lesson, type2);

                        spacedetails_kind.setText(list_lesson.get(0).getC_name() + "（" + list_lesson.get(0).getNumber() + "人" + "）");
                        spacedetails_property.setText("预约属性：" + list_lesson.get(0).getType_name());
                        setTextColor(spacedetails_property,5);
                        spacedetails_address.setText("场馆地址：" + list_lesson.get(0).getAddress());
                        setTextColor(spacedetails_address,5);
                        pics = list_lesson.get(0).getImg().split(";");
                        if (!pics[0].equals("")){
                            dot = new ImageView[pics.length];
                            for (int i = 0; i < pics.length; i++) {
                                ImageView imageView = new ImageView(getActivity());
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
                                params.setMargins(10, 0, 10, 0);
                                imageView.setLayoutParams(params);
                                dot[i] = imageView;
                                spaceitem_dot.addView(dot[i]);
                            }
                            ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(), pics);
                            spaceitem_vp.setAdapter(adapter);
                            spaceitem_vp.setCurrentItem(0);
                        }
                        SpaceTime(list_lesson, 0);
                        int month_ = Integer.parseInt(list_lesson.get(0).getStarttime().split(" ")[0].split("/")[0]);
                        int day_ = Integer.parseInt(list_lesson.get(0).getStarttime().split(" ")[0].split("/")[1]);
                        at_dateoflesson(list_lesson, dateFormat(month_, day_));
                    }catch (Exception e){

                    }
                    break;
            }
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < pics.length; i++) {
            if (i == position % pics.length) {
                dot[i].setBackgroundResource(R.mipmap.point_select);
            } else {
                dot[i].setBackgroundResource(R.mipmap.page_select);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setTextColor(TextView textView, int start){
        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());
        ForegroundColorSpan textcolor = new ForegroundColorSpan(Color.parseColor("#ff8c00"));
        String string = textView.getText().toString();
        int len = string.length();
        if (string.equals("")) {
            string = "无";
        }
        builder.setSpan(textcolor, start, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    /**
     * 规范日期格式  设置日期
     * @param month 月
     * @param day 天
     * */
    private String dateFormat(int month, int day) {
        String string = "";
        if ((month) >= 10 && day < 10) {
            string = (month) + "/" + "0" + day + "/" + year;
        } else if ((month) >= 10 && day >= 10) {
            string = (month) + "/" + day + "/" + year;
        } else if ((month) < 10 && day < 10) {
            string = "0" + month + "/" + "0" + day + "/" + year;
        } else if ((month) < 10 && day >= 10) {
            string = "0" + (month) + "/" + day + "/" + year;
        }
        return string;
    }

    private void SpaceTime(List<SpaceItem> list){
        try{
            final Set<String> set = new LinkedHashSet<String>();
            for (int i = 0; i < list.size(); i++){
                set.add(list.get(i).getStarttime().split(" ")[0].split("/")[0] + "月" + list.get(i).getStarttime().split(" ")[0].split("/")[1] + "日");
            }
            final TextView[] time = new TextView[list.size()];
            Iterator<String> iterator = set.iterator();
            int i = 0;
            while (iterator.hasNext()){
                TextView item = new TextView(getActivity());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                params.setMargins(20, 10, 20, 10);
                item.setText(iterator.next());
                item.setPadding(10, 0, 10, 0);
                item.setTextSize(15);
                if (i == 0) {
                    item.setTextColor(Color.parseColor("#ff8c00"));
                }
                item.setLayoutParams(params);
                time[i] = item;
                i++;
                ll_time_spacedetails.addView(item);
            }
            for (int j = 0; j < set.size(); j++) {
                time[j].setTag(j);
                time[j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        int i = (Integer) arg0.getTag();
                        time[i].setTextColor(Color.parseColor("#ff8c00"));
                        int month = Integer.parseInt(time[i].getText().toString().split("月")[0]);
                        int day = Integer.parseInt(time[i].getText().toString().split("月")[1].split("日")[0]);
                        at_date(list_spaceitem, dateFormat(month, day));
                        Toast.makeText(getActivity(), "点击的是" + dateFormat(month, day), Toast.LENGTH_SHORT).show();
                        for (int j2 = 0; j2 < set.size(); j2++) {
                            if (j2 != i) {
                                time[j2].setTextColor(Color.parseColor("#666669"));
                            }
                        }
                    }
                });
            }
        }catch (Exception e){
            Toast.makeText(getActivity(), "设置日期出错" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void SpaceTime(final List<Lesson> list, int a){
        try {
            final Set<String> set = new LinkedHashSet<String>();
            for (int i = 0; i < list.size(); i++) {
                set.add(list.get(i).getStarttime().split(" ")[0].split("/")[0] + "月" + list.get(i).getStarttime().split(" ")[0].split("/")[1] + "日");
            }

            final TextView[] time = new TextView[list.size()];
            Iterator<String> iterator = set.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                TextView item = new TextView(getActivity());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                params.setMargins(20, 10, 20, 10);
                item.setText(iterator.next());
                item.setPadding(10, 0, 10, 0);
                item.setTextSize(15);
                if (i == 0) {
                    item.setTextColor(Color.parseColor("#ff0000"));
                }
                item.setLayoutParams(params);
                time[i] = item;
                i++;
                ll_time_spacedetails.addView(item);
            }
            for (int j = 0; j < set.size(); j++) {
                time[j].setTag(j);
                time[j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        int i = (Integer) arg0.getTag();
                        time[i].setTextColor(Color.parseColor("#ff8c00"));
                        int month = Integer.parseInt(time[i].getText().toString().split("月")[0]);
                        int day = Integer.parseInt(time[i].getText().toString().split("月")[1].split("日")[0]);
                        at_dateoflesson(list, dateFormat(month, day));
                        for (int j2 = 0; j2 < set.size(); j2++) {
                            if (j2 != i) {
                                time[j2].setTextColor(Color.parseColor("#666669"));
                            }
                        }
                    }
                });
            }

        }catch (Exception e){
            Toast.makeText(getActivity(), "设置日期是出错", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 根据日期筛选课程预约
     * @param list 课程全部数据
     * @param date 日期
     * */
    private void at_dateoflesson(List<Lesson> list, String date){
        temp_lesson.clear();
        if (spacedetails_re.getChildCount() > 0) {
            temp_lesson.clear();
            adapter.notifyDataSetChanged();
        }
        for (Lesson lessonitem : list) {
            if (lessonitem.getStarttime().split(" ")[0].equals(date)) {
                temp_lesson.add(lessonitem);
            }
        }
        if (temp_lesson.size() == 0) {
            adapter = new SpaceTiemAdapter(getActivity(), null, list);
            spacedetails_re.setAdapter(adapter);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            spacedetails_re.setLayoutManager(manager);
            spacedetails_re.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.HORIZONTAL, 1, Color.parseColor("#ededed")));
        }else {
            adapter = new SpaceTiemAdapter(getActivity(), null, temp_lesson);
            spacedetails_re.setAdapter(adapter);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            spacedetails_re.setLayoutManager(manager);
            spacedetails_re.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.HORIZONTAL, 1, Color.parseColor("#ededed")));
        }
    }

    /**
     * 根据日期筛选场地预约
     * @param list 场地预约全部数据
     * @param date 日期
     * */
    private void at_date(List<SpaceItem> list, String date) {
        temp_spaceitem.clear();
        if (spacedetails_re.getChildCount() > 0) {
            temp_spaceitem.clear();
            adapter.notifyDataSetChanged();
        }
        for (SpaceItem spaceItem : list) {
            if (spaceItem.getStarttime().split(" ")[0].equals(date)) {
                temp_spaceitem.add(spaceItem);
            }
        }
        if (temp_spaceitem.size() == 0) {
            Toast.makeText(getActivity(), "暂无", Toast.LENGTH_SHORT).show();
        }else {
            at_time(temp_spaceitem);
        }
    }

    /**
     * 根据时间段筛选
     * @param list at_date筛选后的数据
     * */
    private void at_time(final List<SpaceItem> list){
        temp_spaceitem_after.clear();
        List<Integer> listindex = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            if(listindex.contains(i))
                continue;
            temp_spaceitem_after.add(list.get(i));
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getStarttime().split(" ")[1].equals(list.get(i).getStarttime().split(" ")[1]) && list.get(j).getEndtime().split(" ")[1].equals(list.get(i).getEndtime().split(" ")[1]) && list.get(j).getPrice() == list.get(i).getPrice()) {
                    listindex.add(j);
                }
            }
        }
        if (temp_spaceitem_after.size() == 0) {
            adapter = new SpaceTiemAdapter(getActivity(), list, null);
            spacedetails_re.setAdapter(adapter);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            spacedetails_re.setLayoutManager(manager);
            spacedetails_re.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.HORIZONTAL, 1, Color.parseColor("#ededed")));
        }else {
            adapter = new SpaceTiemAdapter(getActivity(), temp_spaceitem_after, null);
            spacedetails_re.setAdapter(adapter);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            spacedetails_re.setLayoutManager(manager);
            spacedetails_re.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.HORIZONTAL, 1, Color.parseColor("#ededed")));

        }
    }
}
