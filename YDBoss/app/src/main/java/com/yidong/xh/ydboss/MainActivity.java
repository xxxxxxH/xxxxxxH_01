package com.yidong.xh.ydboss;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import CustomFragment.AllBusinessFragment;
import CustomFragment.FavorBusinessFragment;
import bean.Business;
import bean.Common;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText title_et;
//    private RadioGroup rg;
    private String url = "http://www.one-gis.cn:911/YiDongWebService/WebService.asmx/BusinessGetPKDebug";
    private String url_alluser = "http://www.one-gis.cn:911/YiDongWebService/WebService.asmx/GetUserCount";
    private List<Business> list_business = new ArrayList<Business>();
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private FrameLayout content;
    private RadioButton rb_all,rb_favorable,rb_user;
    private RelativeLayout main_bottom_rl,business_bottom_rl;
    private long secondtime;
    private ImageView search;
    private TextView title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getAllBusiness();
    }

    private void initView() {
        title_et = (EditText) findViewById(R.id.title_et);
        title_tv = (TextView) findViewById(R.id.title_tv);
        search = (ImageView) findViewById(R.id.search);
        title_et.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
        title_tv.setVisibility(View.GONE);

        rb_all = (RadioButton) findViewById(R.id.rb_all);
        rb_favorable = (RadioButton) findViewById(R.id.rb_favorable);
        rb_user = (RadioButton) findViewById(R.id.rb_user);
        content = (FrameLayout) findViewById(R.id.content);
        main_bottom_rl = (RelativeLayout) findViewById(R.id.main_bottom_rl);
        business_bottom_rl = (RelativeLayout) findViewById(R.id.business_bottom_rl);
        rb_all.setOnClickListener(this);
        rb_favorable.setOnClickListener(this);
        rb_user.setOnClickListener(this);
        search.setOnClickListener(this);

    }

    public void getAllBusiness() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("strID","");
        params.addBodyParameter("strName","");
        params.addBodyParameter("strType","");
        params.addBodyParameter("strProvince","四川");
        params.addBodyParameter("strCity","成都");
        params.addBodyParameter("strDistrict","");
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
                Log.i("xxxxxxH",responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                msg.what = -1;
            }
        });
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    Toast.makeText(MainActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    try {
                        String result = msg.obj.toString().substring(msg.obj.toString().indexOf("[") , msg.obj.toString().indexOf("]") + 1);
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Business>>() {}.getType();
                        list_business = gson.fromJson(result,type);
                        Toast.makeText(MainActivity.this, "解析成功", Toast.LENGTH_SHORT).show();
                        Common.WhichBusiness = "all";
                        Common.list_business.clear();
                        Common.list_business.addAll(list_business);
                        manager = getSupportFragmentManager();
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.content, new AllBusinessFragment());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "解析出错", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case -2:
                    Toast.makeText(MainActivity.this, "连接出错", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    String result = msg.obj.toString().substring(msg.obj.toString().indexOf(">") + 1, msg.obj.toString().lastIndexOf("<"));
                    String a = result.substring(result.indexOf(">") + 1, result.length());
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("目前已经有" + a + "个基佬注册了");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    break;
            }
        }
    };


    private void ScreenBusiness(){
        Common.favorable_business.clear();
        for (int i = 0; i < Common.list_business.size(); i++){
            if (Common.list_business.get(i).getHas_discount() != null && !Common.list_business.get(i).getHas_discount().equals("")){
                Common.favorable_business.add(Common.list_business.get(i));
            }
        }
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.rb_all:
                    title_et.setText("");
                    title_et.setHint("在所有商家中搜索");
                    manager = getSupportFragmentManager();
                    transaction = manager.beginTransaction();
                    Common.WhichBusiness = "all";
                    transaction.replace(R.id.content, new AllBusinessFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
                case R.id.rb_favorable:
                    title_et.setText("");
                    title_et.setHint("在优惠商家中搜索");
                    manager = getSupportFragmentManager();
                    transaction = manager.beginTransaction();
                    ScreenBusiness();
                    Common.WhichBusiness = "favorable";
                    transaction.replace(R.id.content, new FavorBusinessFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
                case R.id.rb_user:
                    getRegistered();
                    break;
                case R.id.search:

                    break;
        }
    }

    @Override
    public void onBackPressed() {
        if (Common.isNeedExit.equals("yes")){
            if (Common.isExit ){
                ExitDialog();
            }else {
                Common.isExit = true;
            }
        }else {
            if (Common.isBusinessInfo.equals("yes")){
                Common.WhichBusiness = "all";
                title_et.setText("");
                title_et.setHint("在所有商家中搜索");
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.content, new AllBusinessFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        }
    }

    private void getRegistered(){
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.POST, url_alluser, null, new RequestCallBack<String>() {

            Message msg = new Message();

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo.result.contains("nodata") || responseInfo.result.contains("failed")){
                    msg.what = -2;
                }else {
                    msg.what =2;
                    msg.obj = responseInfo.result.toString();
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                msg.what = -2;
                handler.sendMessage(msg);
            }
        });
    }

    private void ExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否退出？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MainActivity.this.finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
