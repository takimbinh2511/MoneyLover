package vianh.nva.moneymanager.ui.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.RequireLoginActivity;
import vianh.nva.moneymanager.Utils.AfterTextChangedWatcher;
import vianh.nva.moneymanager.data.entity.Category;
import vianh.nva.moneymanager.ui.category.adapter.ColorAdapter;
import vianh.nva.moneymanager.ui.category.adapter.IconAdapter;
import vianh.nva.moneymanager.ui.category.adapter.ListCategoryAdapter;

public class CategoryActivity extends RequireLoginActivity {
    private final String TAG = this.getClass().getSimpleName();
    private RecyclerView colorRecyclerView;
    private EditText txtCategoryName;
    private CategoryViewModel viewModel;
    private Category category;
    private Button btnSave;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int mode;

    // khi nguoi dung bam nut mui ten quay lui thi finish activity
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lay view model
        viewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        // Set toolbar cho activity de tao label
        setSupportActionBar(toolbar);
        // Hien thi dau mui ten quay lui
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Category duoc truyen vao
        category = (Category) getIntent().getSerializableExtra("category");

        // Lay mode duoc truyen vao, update hoac insert
        mode = getIntent().getIntExtra("mode", ListCategoryAdapter.MODE_INSERT);

        Log.d(TAG, String.valueOf(mode));
        innitView();
    }

    // Clear all subscription
    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    public void innitView () {
        btnSave = findViewById(R.id.btnSave);
        txtCategoryName = findViewById(R.id.categoryName);

        // Lay het ten mau trong string array tai res/values/strings
        ArrayList<String> listColorName = new ArrayList<>();
        Collections.addAll(listColorName, getResources().getStringArray(R.array.colorNameArray));

        // Setup color recycler view
        colorRecyclerView = findViewById(R.id.recyclerViewColor);
        ColorAdapter colorAdapter = new ColorAdapter(listColorName);
        // Grid layout co so cot la 3
        colorRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        colorRecyclerView.setAdapter(colorAdapter);

        // Lay het name icon trong string array tai res/values/strings
        // Tuong tu nhu color recycler view
        ArrayList<String> listIconName = new ArrayList<>();
        Collections.addAll(listIconName, getResources().getStringArray(R.array.iconNameArray));
        colorRecyclerView = findViewById(R.id.recyclerViewIcon);
        IconAdapter iconAdapter = new IconAdapter(listIconName);
        colorRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        colorRecyclerView.setAdapter(iconAdapter);

        // Ten category != "" thi moi enable button luu
        txtCategoryName.addTextChangedListener(new AfterTextChangedWatcher() {
        @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals("")) {
                    btnSave.setEnabled(true);
                } else {
                    btnSave.setEnabled(false);
                }
            }
        });

        // Neu mode la update thi tim vi tri cua category va color
        // Roi set selected position cho 2 recycler view
        if (mode == ListCategoryAdapter.MODE_UPDATE) {
            txtCategoryName.setText(category.getDescription());
            int colorPos = 0;
            int iconPos = 0;
            for (String name : listColorName) {
                if (name.equals(category.getColorName())) {
                    break;
                }
                colorPos++;
            }

            for (String name : listIconName) {
                if (name.equals(category.getIconName())) {
                    break;
                }
                iconPos++;
            }
            Log.d(TAG, "Color pos : " + colorPos);
            colorAdapter.setSelectedPos(colorPos);
            iconAdapter.setSelectedPosition(iconPos);

        }

        // save category
        btnSave.setOnClickListener( view -> {
            // Set thong tin cho category
            String categoryName = txtCategoryName.getText().toString();
            String colorName = colorAdapter.getSelectedColorName();
            String iconName = iconAdapter.getSelectedIconName();
            category.setDescription(categoryName);
            category.setColorName(colorName);
            category.setIconName(iconName);
            // Neu mode la update thi update
            if (mode == ListCategoryAdapter.MODE_UPDATE) {
                compositeDisposable.add(
                        viewModel.updateCategory(category)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> {
                                            // Update thanh cong, hien thi toast message va ket thuc activity
                                            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                                            finish();
                                        },
                                        throwable -> {
                                            // Update failed, hien thi toast message va log lai
                                            Log.e(TAG, "Insert failed", throwable);
                                            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                                        }
                                )
                );
            } else {
                // Nguoc lai thi insert
                compositeDisposable.add(
                        viewModel.insertCategory(category)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> {
                                            // tuong tu nhu update
                                            Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();
                                            finish();
                                        },
                                        throwable -> {
                                            Log.d(TAG, "Insert failed", throwable);
                                            Toast.makeText(this, "Insert failed", Toast.LENGTH_SHORT).show();
                                        }
                                )
                );
            }
        });
    }
}
