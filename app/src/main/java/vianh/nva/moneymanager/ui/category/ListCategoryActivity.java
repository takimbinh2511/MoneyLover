package vianh.nva.moneymanager.ui.category;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import java.util.Objects;
import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.RequireLoginActivity;
import vianh.nva.moneymanager.ui.category.adapter.CategoryPagerAdapter;

public class ListCategoryActivity extends RequireLoginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initView();
    }

    // Bam mui ten quay lai thi finish activity
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void initView() {
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.outcome));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.income));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Khoi tao view pager
        // Xem trong phan home
        final ViewPager viewPager = findViewById(R.id.viewPager);
        final CategoryPagerAdapter adapter = new CategoryPagerAdapter(getSupportFragmentManager(),
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
