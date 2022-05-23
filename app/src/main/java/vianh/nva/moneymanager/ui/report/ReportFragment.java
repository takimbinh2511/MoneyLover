package vianh.nva.moneymanager.ui.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.ui.report.adapter.ReportPagerAdapter;
// report fragment de chua view pager
public class ReportFragment extends Fragment {

    private ReportViewModel reportViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportViewModel =
                ViewModelProviders.of(this).get(ReportViewModel.class);
        View root = inflater.inflate(R.layout.fragment_report, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        // setup tab layout, xem trong phan home
        TabLayout tabLayout = root.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.month)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.year)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        final ViewPager viewPager = root.findViewById(R.id.viewPager);
        assert activity != null;
        final ReportPagerAdapter adapter = new ReportPagerAdapter(activity.getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}