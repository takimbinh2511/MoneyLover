package vianh.nva.moneymanager.ui.category.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import vianh.nva.moneymanager.ui.category.ListCategoryFragment;
import vianh.nva.moneymanager.ui.home.IncomeFragment;
import vianh.nva.moneymanager.ui.home.OutcomeFragment;
// adapter cho view pager
public class CategoryPagerAdapter extends FragmentStatePagerAdapter {
    private int numOfTabs;

    public CategoryPagerAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numOfTabs = numOfTabs;
    }


    // khoi tao fragment roi chuyen tham so vao de phan biet cai nao la spend cai nao la earn
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment fragment = new ListCategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isSpend", true);
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                Fragment fragment1 = new ListCategoryFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("isSpend", false);
                fragment1.setArguments(bundle1);
                return fragment1;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}