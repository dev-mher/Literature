package com.literature.android.literature.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mher on 3/24/17.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    public static final String TAB_FRAGMENT_PAGE_TITLE = "TabFragmentTitle";

    private List<Fragment> tabPages = new ArrayList<>();

    public PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void addTabPages(Fragment tabPage) {
        tabPages.add(tabPage);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return tabPages.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return tabPages.get(position);
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        Fragment fragment = tabPages.get(position);
        if (null != fragment.getArguments()) {
            return fragment.getArguments().getString(PagerAdapter.TAB_FRAGMENT_PAGE_TITLE);
        }
        return "Page " + position;
    }
}

