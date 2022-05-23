package vianh.nva.moneymanager.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.ui.home.adapter.PagerAdapter;

public class HomeFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initView(root);
        return root;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    private void initView(View root) {
        // Setting view pager
        TabLayout tabLayout = root.findViewById(R.id.tabLayout);

        // add tab va set title cho tab
        tabLayout.addTab(tabLayout.newTab().setText(R.string.outcome));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.income));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        final ViewPager viewPager = root.findViewById(R.id.viewPager);
        final PagerAdapter adapter = new PagerAdapter(activity.getSupportFragmentManager(),
                tabLayout.getTabCount());

        // set adapter
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Selected tab listener
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