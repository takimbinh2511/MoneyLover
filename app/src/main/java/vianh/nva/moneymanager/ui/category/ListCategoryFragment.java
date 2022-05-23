package vianh.nva.moneymanager.ui.category;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.ui.category.adapter.ListCategoryAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListCategoryFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private boolean isSpend = true;
    private RecyclerView listCategory;
    private CategoryViewModel viewModel;
    public ListCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        assert getArguments() != null;
        // Lay thong tin duoc truyen vao de xem cai nao la earn, cai nao la spend
        isSpend = getArguments().getBoolean("isSpend");
        // Lay view model
        viewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_list_category, container, false);
        innitView(root);
        return root;
    }

    private void innitView(View root) {
        listCategory = root.findViewById(R.id.listCategory);
        // Set adapter
        ListCategoryAdapter adapter = new ListCategoryAdapter(isSpend);
        listCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        listCategory.setAdapter(adapter);
        // Neu la spend thi dua vao list category spend
        // Nguoc lai dua vao list category earn
        if (isSpend) {
            viewModel.getListCategorySpend().observe(this, categoryList -> {
                adapter.setListCategory(categoryList);
                Log.d(TAG, String.valueOf(categoryList.size()));
            });
        } else {
            viewModel.getListCategoryEarn().observe(this, categoryList -> {
                adapter.setListCategory(categoryList);
            });
        }
    }

}
