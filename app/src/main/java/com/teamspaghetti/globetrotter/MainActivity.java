package com.teamspaghetti.globetrotter;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.teamspaghetti.globetrotter.fragment.FeedFragment;
import com.teamspaghetti.globetrotter.fragment.MessageFragment;
import com.teamspaghetti.globetrotter.fragment.NotificationFragment;
import com.teamspaghetti.globetrotter.fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    TabLayout mainButtonHolder;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        mainButtonHolder = (TabLayout) findViewById(R.id.tabs);

        mainButtonHolder.setupWithViewPager(viewPager);
        setupTabIcons();

    }
    private void setupTabIcons() {

        ImageView tabOne = (ImageView) LayoutInflater.from(this).inflate(R.layout.tab_layout, null);
        tabOne.setImageResource(R.drawable.mainpage_selector);
        mainButtonHolder.getTabAt(0).setCustomView(tabOne);


        ImageView tabTwo = (ImageView) LayoutInflater.from(this).inflate(R.layout.tab_layout, null);
        tabTwo.setImageResource(R.drawable.notify_selector);
        mainButtonHolder.getTabAt(1).setCustomView(tabTwo);

        ImageView tabThree  = (ImageView) LayoutInflater.from(this).inflate(R.layout.tab_layout, null);
        tabThree.setImageResource(R.drawable.messages_selector);
        mainButtonHolder.getTabAt(2).setCustomView(tabThree);
        ImageView tabFour  = (ImageView) LayoutInflater.from(this).inflate(R.layout.tab_layout, null);
        tabFour.setImageResource(R.drawable.profile_selector);
        mainButtonHolder.getTabAt(3).setCustomView(tabFour);
        mainButtonHolder.getTabAt(0).getCustomView().setSelected(true);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FeedFragment(), "feed");
        adapter.addFrag(new NotificationFragment(), "notifications");
        adapter.addFrag(new MessageFragment(), "messages");
        adapter.addFrag(new ProfileFragment(), "profile");
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
