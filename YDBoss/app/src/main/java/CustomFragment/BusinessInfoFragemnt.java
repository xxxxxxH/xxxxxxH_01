package CustomFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import adapter.PicAdapter;
import adapter.ViewPagerAdapter;
import bean.Common;
import bean.LessonSpace;
import tools.TagListView;
import tools.TagView;
import tools.UnicodeParse;

import static android.graphics.Color.*;

/**
 * Created by wjkj__xh on 2017/2/28.
 */

public class BusinessInfoFragemnt extends Fragment implements View.OnClickListener {

    private View view;
    private ImageView businessinfo_ima_main, businessinfo_ima_720, businessinfo_ima_daohang, businessinfo_ima_phone;
    private TextView title_tv,businessinfo_tv_busname, businessinfo_tv_time, businessinfo_tv_add, businessinfo_tv_notice, businessinfo_tv_intro;
    private RadioGroup business_rg;
    private RadioButton business_rb_info, business_rb_space, business_rb_lesson;
    private ViewPager businessinfo_vp;
    private TagListView view_wordwrap;
    private RecyclerView business_re;
    private int start_time, end_time;
    private RelativeLayout business_rl_main;
    private String url_place = "http://www.one-gis.cn:911/YiDongWebService/WebService.asmx/GetBusinessSpaceTypeName"; //获取球场预约
    private String url__venue = "http://www.one-gis.cn:911/YiDongWebService/WebService.asmx/CurriculumGetPK"; //获取课程预约
    private String url = "";
    private List<LessonSpace> list_lesson = new ArrayList<LessonSpace>();
    private TagView TagView;
    private int heigth = 0;
    private int width = 0;
    private LinearLayout business_ll_notcie, business_ll_intro;
    private View business_view;
    private TagView[] textViews = new TagView[]{};
    private String[] imgs = new String[]{};
    private String[] list_space = new String[]{};
    private RelativeLayout main_bottom_rl, business_bottom_rl;
    private RadioButton business_bottom_game, business_bottom_coach, business_bottom_goods, business_bottom_recurit,business_bottom_home;
    private EditText title_et;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.businessinfo, container, false);
        initView();
        getBusinessSpace();
        return view;
    }

    private void initView() {
        Common.isExit = true;
        Common.isNeedExit = "no";
        Common.WhichFragemntNeedViewPagerAdapter = "";
        Common.isBusinessInfo = "yes";
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        heigth = metrics.heightPixels;
        width = metrics.widthPixels;
        //初始化控件
        businessinfo_ima_main = (ImageView) view.findViewById(R.id.businessinfo_ima_main);
        business_rl_main = (RelativeLayout) view.findViewById(R.id.business_rl_main);
        businessinfo_ima_720 = (ImageView) view.findViewById(R.id.businessinfo_ima_720);
        businessinfo_ima_daohang = (ImageView) view.findViewById(R.id.businessinfo_ima_daohang);
        businessinfo_ima_phone = (ImageView) view.findViewById(R.id.businessinfo_ima_phone);
        businessinfo_tv_busname = (TextView) view.findViewById(R.id.businessinfo_tv_busname);
        businessinfo_tv_time = (TextView) view.findViewById(R.id.businessinfo_tv_time);
        businessinfo_tv_add = (TextView) view.findViewById(R.id.businessinfo_tv_add);
        businessinfo_tv_notice = (TextView) view.findViewById(R.id.businessinfo_tv_notice);
        businessinfo_tv_intro = (TextView) view.findViewById(R.id.businessinfo_tv_intro);
        title_et = (EditText) getActivity().findViewById(R.id.title_et);
        title_tv = (TextView) getActivity().findViewById(R.id.title_tv);
        ImageView search = (ImageView) getActivity().findViewById(R.id.search);
        title_et.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
        title_tv.setVisibility(View.VISIBLE);
        business_rg = (RadioGroup) view.findViewById(R.id.business_rg);
        business_rb_info = (RadioButton) view.findViewById(R.id.business_rb_info);
        business_rb_space = (RadioButton) view.findViewById(R.id.business_rb_space);
        business_rb_lesson = (RadioButton) view.findViewById(R.id.business_rb_lesson);
        businessinfo_vp = (ViewPager) view.findViewById(R.id.businessinfo_vp);
        view_wordwrap = (TagListView) view.findViewById(R.id.view_wordwrap);
        business_re = (RecyclerView) view.findViewById(R.id.business_re);
        business_ll_notcie = (LinearLayout) view.findViewById(R.id.business_ll_notcie);
        business_ll_intro = (LinearLayout) view.findViewById(R.id.business_ll_intro);
        business_view = view.findViewById(R.id.business_view);
        main_bottom_rl = (RelativeLayout) getActivity().findViewById(R.id.main_bottom_rl);
        business_bottom_rl = (RelativeLayout) getActivity().findViewById(R.id.business_bottom_rl);
        business_bottom_game = (RadioButton) getActivity().findViewById(R.id.business_bottom_game);
        business_bottom_coach = (RadioButton) getActivity().findViewById(R.id.business_bottom_coach);
        business_bottom_goods = (RadioButton) getActivity().findViewById(R.id.business_bottom_goods);
        business_bottom_recurit = (RadioButton) getActivity().findViewById(R.id.business_bottom_recurit);
        business_bottom_home = (RadioButton) getActivity().findViewById(R.id.business_bottom_home);
        main_bottom_rl.setVisibility(View.GONE);
        business_bottom_rl.setVisibility(View.VISIBLE);
        business_rb_info.setOnClickListener(this);
        business_rb_space.setOnClickListener(this);
        business_rb_lesson.setOnClickListener(this);
        businessinfo_ima_720.setOnClickListener(this);
        business_bottom_game.setOnClickListener(this);
        business_bottom_coach.setOnClickListener(this);
        business_bottom_goods.setOnClickListener(this);
        business_bottom_recurit.setOnClickListener(this);
        business_bottom_home.setOnClickListener(this);


        //获取数据
        String b_name = Common.business.getName();
        String time = Common.business.getHours();
        String address = Common.business.getAddress();
        String ad = Common.business.getAd();
        if (!Common.business.getCurriculum_schedule().equals("")) {
            imgs = Common.business.getCurriculum_schedule().split(";");
        }
        if (ad.equals("")) {
            ad = "商家暂无公告";
        }
        String intro = Common.business.getProfile();
        String img_main = Common.business.getImg_main();
        String[] img_list = Common.business.getImg_list().split(";");
        String mobile = Common.business.getMobile();

        //填充数据
        Glide.with(getActivity()).load(img_main).into(businessinfo_ima_main);
        businessinfo_tv_busname.setText(b_name);
        title_tv.setText(b_name);
        businessinfo_tv_add.setText(address);
        businessinfo_tv_notice.setText(ad);
        businessinfo_tv_intro.setText(intro);
        if (img_list.length > 0) {
            PicAdapter adapter = new PicAdapter(getActivity(), img_list);
            business_re.setAdapter(adapter);
            LinearLayoutManager layoutmanager = new LinearLayoutManager(getActivity());
            layoutmanager.setOrientation(LinearLayoutManager.HORIZONTAL);
            business_re.setLayoutManager(layoutmanager);
        }
        if (!time.equals("") && time != null) {
            try {
                String[] all_time = time.split(";");
                start_time = Integer.valueOf(all_time[0]) / 60;
                end_time = Integer.valueOf(all_time[1]) / 60;
                int startmiu = Integer.valueOf(all_time[0]) % 60;
                int endmiu = Integer.valueOf(all_time[1]) % 60;
                if (startmiu == 0 && endmiu == 0) {
                    businessinfo_tv_time.setText(start_time + ":00" + "-" + end_time + ":00");
                } else {
                    if (startmiu == 0 && endmiu != 0) {
                        businessinfo_tv_time.setText(start_time + ":00" + "-" + end_time + ":" + endmiu);
                    } else if (endmiu % 60 == 0 && startmiu != 0) {
                        businessinfo_tv_time.setText(start_time + ":" + startmiu + "-" + end_time + ":00");
                    } else {
                        businessinfo_tv_time.setText(start_time + ":" + startmiu + "-" + end_time + ":" + endmiu);
                    }
                }
            } catch (Exception e) {
                businessinfo_tv_time.setText("9:00-18:00");
            }
        } else {
            businessinfo_tv_time.setText("9:00-18:00");
        }

        if (imgs.length != 0) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(), imgs);
            businessinfo_vp.setAdapter(adapter);
            businessinfo_vp.setCurrentItem(0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.business_rb_info:
                business_ll_notcie.setVisibility(View.VISIBLE);
                business_ll_intro.setVisibility(View.VISIBLE);
                business_view.setVisibility(View.VISIBLE);
                view_wordwrap.setVisibility(View.GONE);
                businessinfo_vp.setVisibility(View.GONE);
                break;
            case R.id.business_rb_space:
                if (Common.business.getB_ui_type_name().equals("场馆")) {
                    if (list_lesson.size() == 0) {
                        Toast.makeText(getActivity(), "该商家暂无预约哦", Toast.LENGTH_SHORT).show();
                    } else {
                        business_ll_notcie.setVisibility(View.GONE);
                        business_ll_intro.setVisibility(View.GONE);
                        business_view.setVisibility(View.GONE);
                        businessinfo_vp.setVisibility(View.GONE);
                        view_wordwrap.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (list_space.length == 0) {
                        Toast.makeText(getActivity(), "该商家暂无预约哦", Toast.LENGTH_SHORT).show();
                    } else {
                        business_ll_notcie.setVisibility(View.GONE);
                        business_ll_intro.setVisibility(View.GONE);
                        business_view.setVisibility(View.GONE);
                        businessinfo_vp.setVisibility(View.GONE);
                        view_wordwrap.setVisibility(View.VISIBLE);
                    }
                }

                break;
            case R.id.business_rb_lesson:
                if (imgs.length == 0) {
                    Toast.makeText(getActivity(), "该商家暂无课程表", Toast.LENGTH_SHORT).show();
                } else {
                    businessinfo_vp.setVisibility(View.VISIBLE);
                    business_ll_notcie.setVisibility(View.GONE);
                    business_ll_intro.setVisibility(View.GONE);
                    business_view.setVisibility(View.GONE);
                    view_wordwrap.setVisibility(View.GONE);
                }

                break;
            case R.id.businessinfo_ima_720:
                if (!Common.business.getWeb360().toString().equals("")) {
                    Common.isBusinessInfo = "no";
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.content, new PanoramaFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(getActivity(), "全景正在紧张制作中", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.business_bottom_game:
                Common.isBusinessInfo = "no";
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.content, new GameListFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.business_bottom_coach:
                Common.isBusinessInfo = "no";
                FragmentManager manager1 = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction1 = manager1.beginTransaction();
                transaction1.replace(R.id.content, new CoachListFragment());
                transaction1.addToBackStack(null);
                transaction1.commit();
                break;
            case R.id.business_bottom_goods:
                Common.isBusinessInfo = "no";
                FragmentManager manager2 = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction2 = manager2.beginTransaction();
                transaction2.replace(R.id.content, new GoodsListFragment());
                transaction2.addToBackStack(null);
                transaction2.commit();
                break;
            case R.id.business_bottom_recurit:
                Common.isBusinessInfo = "no";
                FragmentManager manager3 = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction3 = manager3.beginTransaction();
                transaction3.replace(R.id.content, new RecruitListFragment());
                transaction3.addToBackStack(null);
                transaction3.commit();
                break;
            case R.id.business_bottom_home:
                if (Common.isBusinessInfo.equals("yes")){
                    Toast.makeText(getActivity(), "已经在主页了", Toast.LENGTH_SHORT).show();
                }else {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.content, new BusinessInfoFragemnt());
                    ft.addToBackStack(null);
                    ft.commit();
                }
                break;
        }
    }

    private void getBusinessSpace() {
        RequestParams params = new RequestParams();
        if (Common.business.getB_ui_type_name().equals("场馆")) {
            params.addBodyParameter("strBusinessID", Common.business.getId() + "");
            params.addBodyParameter("strID", "");
            params.addBodyParameter("strName", "");
            url = url__venue;
        } else {
            params.addBodyParameter("strBusinessID", Common.business.getId() + "");
            url = url_place;
        }
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            Message msg = new Message();

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo.result != null &&
                        !responseInfo.result.equals("") &&
                        !responseInfo.result.contains("nodata") &&
                        !responseInfo.result.contains("failed")) {
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

    private void CustonTagView(final List<LessonSpace> list) {
        try {
            if (list.size() != 0) {
                textViews = new TagView[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    TagView = new TagView(getActivity());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(300, 150);
                    params.setMargins(10, 10, 10, 10);
                    TagView.setText(list.get(i).getName());
                    TagView.setId(i);
                    TagView.setTextColor(Color.WHITE);
                    TagView.setTextSize(12);
                    TagView.setBackgroundResource(R.drawable.tagviewbg);
                    TagView.setSingleLine(true);
                    TagView.setMaxEms(8);
                    TagView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                    TagView.setGravity(Gravity.CENTER);
                    TagView.setLayoutParams(params);
                    textViews[i] = TagView;
                    view_wordwrap.addView(TagView);
                }
            }
            for (int j = 0; j < list.size(); j++) {
                textViews[j].setTag(j);
                textViews[j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = (Integer) v.getTag();
                        Toast.makeText(getActivity(), "点击的是" + list.get(i).getId() + "", Toast.LENGTH_SHORT).show();
                        Common.type = list.get(i).getId() + "";
                        if (Common.business.getB_ui_type_name().contains("场地")) {

                        } else {
                            Common.isBusinessInfo = "no";
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.content, new SpaceDetailsFragment());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "创建TagView时出错", Toast.LENGTH_SHORT).show();
        }
    }

    private void CustonTagView(final String[] data) {
        try {
            if (data.length != 0) {
                textViews = new TagView[data.length];
                for (int i = 0; i < data.length; i++) {
                    TagView = new TagView(getActivity());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(300, 150);
                    params.setMargins(10, 10, 10, 10);
                    TagView.setText(data[i]);
                    TagView.setId(i);
                    TagView.setTextColor(Color.WHITE);
                    TagView.setTextSize(12);
                    TagView.setBackgroundResource(R.drawable.tagviewbg);
                    TagView.setSingleLine(true);
                    TagView.setMaxEms(8);
                    TagView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                    TagView.setGravity(Gravity.CENTER);
                    TagView.setLayoutParams(params);
                    textViews[i] = TagView;
                    view_wordwrap.addView(TagView);
                }
            }
            for (int j = 0; j < data.length; j++) {
                textViews[j].setTag(j);
                textViews[j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = (Integer) v.getTag();
                        Toast.makeText(getContext(), "点击的是" + data[i].toString(), Toast.LENGTH_SHORT).show();
                        Common.type = data[i].toString();
                        if (Common.business.getB_ui_type_name().contains("场地")) {
                            Common.isBusinessInfo = "no";
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.replace(R.id.content, new SpaceGameLsitFragemnt());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else {
                        }
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "创建TagView时出错", Toast.LENGTH_SHORT).show();
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    Toast.makeText(getActivity(), "获取商家预约失败(原因：该商家无预约或者其他原因)", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    try {
                        if (Common.business.getB_ui_type_name().equals("场馆")) {
                            String result = msg.obj.toString().substring(msg.obj.toString().indexOf("["), msg.obj.toString().indexOf("]") + 1);
                            if (result != null && !result.equals("")) {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<LessonSpace>>() {
                                }.getType();
                                list_lesson = gson.fromJson(result, type);
                                Common.list_lessonn.clear();
                                Common.list_lessonn.addAll(list_lesson);
                                CustonTagView(list_lesson);
                            }
                        } else {
                            String result = msg.obj.toString().substring(msg.obj.toString().indexOf("[") + 1, msg.obj.toString().indexOf("]"));
                            if (!result.equals("")) {
                                String a = UnicodeParse.parseUnicode(result).replace("\"", "");
                                if (a.length() > 0) {
                                    list_space = a.split(",");
                                    Common.list_space = new String[]{};
                                    Common.list_space = list_space;
                                    CustonTagView(Common.list_space);
                                }
                            }
                        }

                        Toast.makeText(getActivity(), "解析完成", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "解析出错", Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };


}
