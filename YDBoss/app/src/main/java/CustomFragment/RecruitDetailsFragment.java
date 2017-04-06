package CustomFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yidong.xh.ydboss.R;

import bean.Common;

/**
 * Created by wjkj__xh on 2017/3/8.
 */

public class RecruitDetailsFragment extends Fragment {
    private View view;
    private TextView recruitlistitemdetails_tv_name,recruitlistitemdetails_tv_kind,
            recruitlistitemdetails_tv_salary,recruitlistitemdetails_tv_bname,
            recruitlistitemdetails_tv_nature,recruitlistitemdetails_tv_sex,
            recruitlistitemdetails_tv_edu,recruitlistitemdetails_tv_num,
            recruitlistitemdetails_tv_exp,recruitlistitemdetails_tv_address,
            recruitlistitemdetails_tv_responsibilities_details,recruitlistitemdetails_tv_requirementsdetails;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recruitdetails, container, false);
        initView();
        return view;
    }

    private void initView() {
        recruitlistitemdetails_tv_name = (TextView) view.findViewById(R.id.recruitlistitemdetails_tv_name);
        recruitlistitemdetails_tv_kind = (TextView) view.findViewById(R.id.recruitlistitemdetails_tv_kind);
        recruitlistitemdetails_tv_salary = (TextView) view.findViewById(R.id.recruitlistitemdetails_tv_salary);
        recruitlistitemdetails_tv_bname = (TextView) view.findViewById(R.id.recruitlistitemdetails_tv_bname);
        recruitlistitemdetails_tv_nature = (TextView) view.findViewById(R.id.recruitlistitemdetails_tv_nature);
        recruitlistitemdetails_tv_sex = (TextView) view.findViewById(R.id.recruitlistitemdetails_tv_sex);
        recruitlistitemdetails_tv_edu = (TextView) view.findViewById(R.id.recruitlistitemdetails_tv_edu);
        recruitlistitemdetails_tv_num = (TextView) view.findViewById(R.id.recruitlistitemdetails_tv_num);
        recruitlistitemdetails_tv_exp = (TextView) view.findViewById(R.id.recruitlistitemdetails_tv_exp);
        recruitlistitemdetails_tv_address = (TextView) view.findViewById(R.id.recruitlistitemdetails_tv_address);
        recruitlistitemdetails_tv_responsibilities_details = (TextView) view.findViewById(R.id.recruitlistitemdetails_tv_responsibilities_details);
        recruitlistitemdetails_tv_requirementsdetails = (TextView) view.findViewById(R.id.recruitlistitemdetails_tv_requirementsdetails);
        String job_name = Common.Recruit.getJob_name();
        String job_kind = Common.Recruit.getJob_category();
        String b_name = Common.Recruit.getB_name();
        String natrue = Common.Recruit.getWork_nature();
        String sex = Common.Recruit.getSex();
        String edu = Common.Recruit.getEducation();
        int num  = 0 , exp = 0;
        try {
            num = Common.Recruit.getRecruit_number();
        } catch (Exception e) {
        }
        try {
            exp = Common.Recruit.getWork_year();
        } catch (Exception e) {
        }
        String address = Common.Recruit.getAddress();
        String responsibilities = Common.Recruit.getJob_responsibilities();
        String requirements = Common.Recruit.getJob_requirements();
        String salary = Common.Recruit.getSalary();
        String expp;
        recruitlistitemdetails_tv_name.setText(job_name);
        recruitlistitemdetails_tv_kind.setText("(" + job_kind + ")");
        recruitlistitemdetails_tv_bname.setText(b_name);
        recruitlistitemdetails_tv_nature.setText(natrue);
        if (sex.equals("不限")) {
            sex = "性别不限";
        }
        recruitlistitemdetails_tv_sex.setText(sex);
        if (edu.equals("不限")) {
            edu = "学历不限";
        }
        recruitlistitemdetails_tv_edu.setText(edu);
        recruitlistitemdetails_tv_num.setText(num +"人");
        if (exp == 0) {
            expp = "经验不限";
        }else {
            expp = exp + "年";
        }
        recruitlistitemdetails_tv_exp.setText(expp);
        recruitlistitemdetails_tv_address.setText(address);
        recruitlistitemdetails_tv_responsibilities_details.setText(responsibilities);
        recruitlistitemdetails_tv_requirementsdetails.setText(requirements);
        recruitlistitemdetails_tv_salary.setText("薪资:" + salary);
    }
}
