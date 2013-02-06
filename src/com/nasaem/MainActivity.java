package com.nasaem;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

import net.sourceforge.servestream.utils.MusicUtils;
import net.sourceforge.servestream.utils.MusicUtils.ServiceToken;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Fragment;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;

public class MainActivity extends Activity implements ActionBar.TabListener, ServiceConnection {

	RadioFragment radio = null;
	NewsFragment news = null;
	ViewPager mViewPager;
	SectionsPagerAdapter mSectionsPagerAdapter;
	private ServiceToken mToken;
	final static String TAB_RADIO = "radio";
	final static String TAB_NEWS = "news";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        actionBar.addTab(actionBar.newTab()
                        .setText(mSectionsPagerAdapter.getPageTitle(0))
                        .setTabListener(this));

        actionBar.addTab(actionBar.newTab()
                .setText(mSectionsPagerAdapter.getPageTitle(1))
                .setTabListener(this));

		if (savedInstanceState != null) {
			getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }

		mToken = MusicUtils.bindToService(this, this);
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		Log.d("Mainactivity", "updateNowPlaying");
		MusicUtils.updateNowPlaying(this);

	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		Log.d("RadioFragment", "service disconnected");
	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    protected void onDestroy() {
    	MusicUtils.unbindFromService(mToken);
    	super.onDestroy();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

        	switch (i) {
            case 0:
            	return new RadioFragment();
        	case 1:
        		return new NewsFragment();
            }
        	return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.listen).toUpperCase();
                case 1: return getString(R.string.read).toUpperCase();
            }
            return null;
        }
    }
}


