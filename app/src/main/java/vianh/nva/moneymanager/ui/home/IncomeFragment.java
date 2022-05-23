package vianh.nva.moneymanager.ui.home;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.Utils.AfterTextChangedWatcher;
import vianh.nva.moneymanager.data.entity.Category;
import vianh.nva.moneymanager.data.entity.Money;
import vianh.nva.moneymanager.ui.home.adapter.CategoryAdapter;
import vianh.nva.moneymanager.ui.view.DatePickerDialogFragment;

public class IncomeFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    // View model
    private HomeViewModel mViewModel;
    private TextView dateText;
    private Button btnInsertEarnMoney;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FragmentActivity context;
    private EditText txbMoney;
    public static final String TAG = IncomeFragment.class.getSimpleName();
    private Calendar c = Calendar.getInstance();
    private EditText noteText;

    public static IncomeFragment newInstance() {
        return new IncomeFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        // Lay context
        this.context = (FragmentActivity) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(view);
    }

    @Override
    public void onStop() {
        super.onStop();
        // clear all subscriptions
        compositeDisposable.clear();
    }

    private void initData(View view) {
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        // get view
        RecyclerView recyclerView = view.findViewById(R.id.listCategory);
        dateText = view.findViewById(R.id.dateEditText);
        txbMoney = view.findViewById(R.id.moneyEarnEditText);
        ImageButton btn = view.findViewById(R.id.imageButtonDateDialog);
        btnInsertEarnMoney = view.findViewById(R.id.btnInsertEarnMoney);
        noteText = view.findViewById(R.id.noteEditText);

        // Set text cho text view ngay thang la ngay thang nam hien tai
        String dateString = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
        dateText.setText(dateString);

        // Disable button nhap khoan thu chi
        btnInsertEarnMoney.setEnabled(false);

        // Khi nao text khac null va khac 0 thi enable cho button nhap
        txbMoney.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (txbMoney.getText() != null && !txbMoney.getText().toString().equals("0"))
                    btnInsertEarnMoney.setEnabled(true);
                else
                    btnInsertEarnMoney.setEnabled(false);
            }
        });

        // bind adapter to recyclerview
        final CategoryAdapter adapter = new CategoryAdapter();

        // Lay list category
        // Neu category duoi db bi thay doi thi update lai list category trong adapter
        mViewModel.getListCategoryEarn().observe(this, categories -> {

            // Category de chuyen den man hinh chinh sua category
            Category category = new Category("ic_chevron_right_black_24dp",
                    "colorPrimary", "Chinh sua gi do cho no dai ne", CategoryAdapter.TYPE_SETTING);
            categories.add(category);
            adapter.setList(categories);
            Log.d("Income", "Changed");
        });

        // Setup grid layout cho recycler view
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // set onclick cho button pick date
        btn.setOnClickListener(view1 -> {
            DatePickerDialogFragment datePicker = new DatePickerDialogFragment(IncomeFragment.this);
            datePicker.show(context.getSupportFragmentManager(), "Date picker dialog");
        });

        // set onclik cho button nhap khoan thu chi
        btnInsertEarnMoney.setOnClickListener(
                v -> {
                    // lay cac thong tin
                    int month = c.get(Calendar.MONTH) + 1;
                    int year = c.get(Calendar.YEAR);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    String note = noteText.getText().toString();
                    long spent = 0L;
                    if (txbMoney.getText() != null) {
                        spent = Long.valueOf(txbMoney.getText().toString());
                    }
                    int categoryId = adapter.getSelectedId();

                    // tao money de luu vao db
                    Money money = new Money(note, spent, categoryId, day, month, year, Money.TYPE_EARN);

                    // insert money
                    compositeDisposable.add(
                            mViewModel.insertMoney(money)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            () -> {
                                                // insert thanh cong, tao toast message xong roi clear text box
                                                Toast.makeText(context, "Insert success", Toast.LENGTH_SHORT).show();
                                                clearTxb();
                                            },
                                            throwable -> {
                                                // tao toast message
                                                Log.e(TAG, throwable.getMessage(), throwable);
                                                Toast.makeText(context, "Insert failed", Toast.LENGTH_SHORT).show();
                                            }
                                    )

                    );
                }
        );
    }

    // Implement onDateSet interface, set text cho textbox ngay la ngay duoc chon trong calendar dialog
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        c.set(i, i1, i2);
        String dateString = i2 + "/" + (i1 + 1) + "/" + i;
        dateText.setText(dateString);
    }

    // clear all text
    private void clearTxb() {
        noteText.getText().clear();
        txbMoney.setText("0");
    }
}
