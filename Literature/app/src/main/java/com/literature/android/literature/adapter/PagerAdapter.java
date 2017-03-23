package com.literature.android.literature.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.literature.android.literature.tab.Poem;
import com.literature.android.literature.tab.Story;

/**
 * Created by mher on 3/24/17.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 1;

    public PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return Poem.newInstance(0, "Page # 1");
            case 1: // Fragment # 0 - This will show FirstFragment
                return Story.newInstance(0, "Page # 2");
            default:
                return Poem.newInstance(0, "Page # 1");
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}

