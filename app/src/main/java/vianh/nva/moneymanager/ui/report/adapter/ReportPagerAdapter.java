package vianh.nva.moneymanager.ui.report.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import vianh.nva.moneymanager.ui.report.MonthReportFragment;
import vianh.nva.moneymanager.ui.report.YearReportFragment;

// Xem trong phan home
public class ReportPagerAdapter extends FragmentStatePagerAdapter {
    private int numOfTabs;

    public ReportPagerAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numOfTabs = numOfTabs;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MonthReportFragment();
            case 1:
                return new YearReportFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
