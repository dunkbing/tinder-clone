package com.dangbinh.moneymanagement.ui.category_selection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dangbinh.moneymanagement.R;
import com.dangbinh.moneymanagement.models.Transaction;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class CategorySelectionMainActivity extends AppCompatActivity implements IFragmentToActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection_main);
        Toolbar toolbar = findViewById(R.id.trans_view_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CategorySelectionView1(), "INCOME");
        adapter.addFragment(new CategorySelectionView2(), "EXPENSE");
        adapter.addFragment(new CategorySelectionView3(), "DEBT & LOANS");
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void selectItem(String cate, Transaction.Type type) {
        Log.d("got item", cate);
        //Toast.makeText(getApplicationContext(),"InActivity got data"+msg,Toast.LENGTH_LONG).show();
        Intent return_intent = new Intent();
        if (cate != null) {
            return_intent.putExtra(CateSelectionResult.CATEGORY.toString(), cate);
            return_intent.putExtra(CateSelectionResult.TYPE.toString(), type.toString());
            setResult(Activity.RESULT_OK, return_intent);
        } else {
            setResult(Activity.RESULT_CANCELED, return_intent);
        }
        finish();
    }

    public enum CateSelectionResult {
        CATEGORY,
        TYPE
    }
}