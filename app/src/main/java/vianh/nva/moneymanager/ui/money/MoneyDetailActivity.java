package vianh.nva.moneymanager.ui.money;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.RequireLoginActivity;
import vianh.nva.moneymanager.Utils.AfterTextChangedWatcher;
import vianh.nva.moneymanager.Utils.Utils;
import vianh.nva.moneymanager.data.entity.Category;
import vianh.nva.moneymanager.data.entity.Money;
import vianh.nva.moneymanager.ui.home.HomeViewModel;
import vianh.nva.moneymanager.ui.home.adapter.CategoryAdapter;
import vianh.nva.moneymanager.ui.view.DatePickerDialogFragment;

public class MoneyDetailActivity extends RequireLoginActivity implements DatePickerDialog.OnDateSetListener {
    private HomeViewModel mViewModel;
    private TextView dateText;
    private Button btnSave;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private EditText txbMoney;
    public final String TAG = this.getClass().getSimpleName();
    private Calendar c = Calendar.getInstance();
    private EditText noteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_detail);
        Money money = (Money) getIntent().getSerializableExtra("Money");
        assert  money != null;
        init(money);
    }

    public void init(Money money) {
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        c.set(money.getYear(), money.getMonth() - 1, money.getDay());

        // get view
        TextView textview = findViewById(R.id.textView3);
        RecyclerView recyclerView = findViewById(R.id.listCategory);
        dateText = findViewById(R.id.dateEditText);
        txbMoney = findViewById(R.id.moneyEarnEditText);
        ImageButton btn = findViewById(R.id.imageButtonDateDialog);
        btnSave = findViewById(R.id.btnSave);
        noteText = findViewById(R.id.noteEditText);

        noteText.setText(money.getNote());

        // bind adapter to recyclerview
        final CategoryAdapter adapter = new CategoryAdapter();
        if (money.getType() == Money.TYPE_SPEND) {
            textview.setText("Tien chi");
            String textMoney = Utils.currencyFormat(-1 * money.getMoney());
            txbMoney.setText(textMoney);

            mViewModel.getListCategorySpend().observe(this, categories -> {
                Category category = new Category("ic_chevron_right_black_24dp",
                        "colorPrimary", "Chinh sua gi do cho no dai ne", CategoryAdapter.TYPE_SETTING);
                categories.add(category);
                adapter.setList(categories);
                Log.d("Outcome", String.valueOf(categories.size()));
                int selectedPosition = 0;
                for (int i = 0; i < categories.size(); i++) {
                    if (categories.get(i).getId() == money.getCategoryId()) {
                        selectedPosition = i;
                    }
                }
                adapter.setSelectedPosition(selectedPosition);
            });
        } else {
            String textMoney = String.valueOf(money.getMoney());
            txbMoney.setText(textMoney);
            textview.setText("Tien thu");

            mViewModel.getListCategoryEarn().observe(this, categories -> {
                Category category = new Category("ic_chevron_right_black_24dp",
                        "colorPrimary", "Chinh sua gi do cho no dai ne", CategoryAdapter.TYPE_SETTING);
                categories.add(category);
                adapter.setList(categories);
                Log.d("Outcome", String.valueOf(categories.size()));
                int selectedPosition = 0;
                for (int i = 0; i < categories.size(); i++) {
                    if (categories.get(i).getId() == money.getCategoryId()) {
                        selectedPosition = i;
                    }
                }
                adapter.setSelectedPosition(selectedPosition);
            });
        }

        String dateString = money.getDay() + "/" + money.getMonth() + "/" + money.getYear();
        dateText.setText(dateString);

        txbMoney.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && !editable.toString().equals("0"))
                    btnSave.setEnabled(true);
                else
                    btnSave.setEnabled(false);
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        btn.setOnClickListener(view1 -> {
            DatePickerDialogFragment datePicker = new DatePickerDialogFragment(this);
            datePicker.show(getSupportFragmentManager(), "Date picker dialog");
        });

        // set onclick
        // TODO: refactor this to use data binding
        btnSave.setOnClickListener(
                v -> {
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH) + 1;
                    String note = noteText.getText().toString();
                    long value = 0L;
                    if (txbMoney.getText() != null) {
                        value = Long.valueOf(txbMoney.getText().toString().replace(".", "").trim());
                    }
                    int categoryId = adapter.getSelectedId();
                    money.setMoney(value);
                    money.setCategoryId(categoryId);
                    money.setNote(note);
                    money.setDay(day);
                    money.setMonth(month);
                    money.setYear(year);
                    compositeDisposable.add(
                            mViewModel.updateMoney(money)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            () -> {
                                                Toast.makeText(this, "Update success", Toast.LENGTH_SHORT).show();
                                                finish();
                                            },
                                            throwable -> {
                                                Log.e(TAG, throwable.getMessage(), throwable);
                                                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                                            }
                                    )

                    );
                }
        );
    }

    @Override
    public void onStop() {
        super.onStop();

        // clear all the subscriptions
        compositeDisposable.clear();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        c.set(i, i1, i2);
        String dateString = i2+ "/" + (i1 + 1)  + "/" + i;
        dateText.setText(dateString);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
