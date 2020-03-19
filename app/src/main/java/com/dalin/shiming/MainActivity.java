package com.dalin.shiming;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {


    private RadioGroup mRg_main;
    private int position;
    private Fragment mContent;
    private List<BaseFragment> mBaseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initFragment();
        setListener();
}
    private void initView() {
        setContentView(R.layout.activity_main);
        mRg_main = (RadioGroup) findViewById(R.id.rg_main);
    }
    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new CustomFragment1());
        mBaseFragment.add(new CustomFragment2());
        mBaseFragment.add(new CustomFragment3());
        mBaseFragment.add(new CustomFragment4());
    }
    private void setListener() {
        mRg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置默认选中常用框架11
        mRg_main.check(R.id.rb_common_frame);
    }
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.rb_common_frame://常用框架
                    position = 0;
                    break;
                case R.id.rb_thirdparty://第三方
                    position = 1;
                    break;
                case R.id.rb_custom://自定义
                    position = 2;
                    break;
                case R.id.rb_other://其他
                    position = 3;
                    break;
                default:
                    position = 0;
                    break;
            }

            //根据位置得到对应的Fragment
            BaseFragment to = getFragment();
            //替换
            switchFrament(mContent,to);

        }
    }

    private BaseFragment getFragment() {
        BaseFragment fragment = mBaseFragment.get(position);
        return fragment;
    }

    private void switchFrament(Fragment from,Fragment to) {
        if(from != to){
            mContent = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //才切换
            //判断有没有被添加
            if(!to.isAdded()){
                //to没有被添加
                //from隐藏
                if(from != null){
                    ft.hide(from);
                }
                //添加to
                if(to != null){
                    ft.add(R.id.fl_content,to).commit();
                }
            }else{
                //to已经被添加
                // from隐藏
                if(from != null){
                    ft.hide(from);
                }
                //显示to
                if(to != null){
                    ft.show(to).commit();
                }
            }
        }

    }

}
