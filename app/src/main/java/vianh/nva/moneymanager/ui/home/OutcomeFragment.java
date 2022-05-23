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
import android.text.TextWatcher;
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
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.Utils.AfterTextChangedWatcher;
import vianh.nva.moneymanager.data.entity.Category;
import vianh.nva.moneymanager.data.entity.Money;
import vianh.nva.moneymanager.ui.home.adapter.CategoryAdapter;
import vianh.nva.moneymanager.ui.view.DatePickerDialogFragment;

// out come i chang income, luc nay con ngu nen chua biet xai lai thang income
public class OutcomeFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private HomeViewModel mViewModel;
    private TextView dateText;
    private Button btnInsertSpentMoney;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FragmentActivity context;
    private EditText txbMoney;
    public final String TAG = this.getClass().getSimpleName();
    private Calendar c = Calendar.getInstance();
    private EditText noteText;

    public static OutcomeFragment newInstance() {
        return new OutcomeFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = (FragmentActivity) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_outcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(view);
    }

    public void initData(View view) {
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        // get view
        RecyclerView recyclerView = view.findViewById(R.id.listCategory);
        dateText = view.findViewById(R.id.dateEditText);
        txbMoney = view.findViewById(R.id.moneyEarnEditText);
        ImageButton btn = view.findViewById(R.id.imageButtonDateDialog);
        btnInsertSpentMoney = view.findViewById(R.id.btnInsertSpentMoney);
        noteText = view.findViewById(R.id.noteEditText);

        String dateString = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
        dateText.setText(dateString);

        btnInsertSpentMoney.setEnabled(false);
        txbMoney.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (txbMoney.getText() != null && !txbMoney.getText().toString().equals("0"))
                    btnInsertSpentMoney.setEnabled(true);
                else
                    btnInsertSpentMoney.setEnabled(false);
            }
        });

        // bind adapter to recyclerview
        final CategoryAdapter adapter = new CategoryAdapter();

        mViewModel.getListCategorySpend().observe(this, categories -> {
            Category category = new Category("ic_chevron_right_black_24dp",
                    "colorPrimary", "Chinh sua gi do cho no dai ne", CategoryAdapter.TYPE_SETTING);
            categories.add(category);
            adapter.setList(categories);
            Log.d("Outcome", String.valueOf(categories.size()));
            for (Category mCategory : categories) {
                Log.d("Outcome", category.getDescription());
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        btn.setOnClickListener(view1 -> {
            DatePickerDialogFragment datePicker = new DatePickerDialogFragment(OutcomeFragment.this);
            datePicker.show(context.getSupportFragmentManager(), "Date picker dialog");
        });

        // set onclick
        // TODO: refactor this to use data binding
        btnInsertSpentMoney.setOnClickListener(
                v -> {
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH) + 1;
                    String note = noteText.getText().toString();
                    long spent = 0L;
                    if (txbMoney.getText() != null) {
                        spent = Long.valueOf(txbMoney.getText().toString());
                    }
                    int categoryId = adapter.getSelectedId();
                    Money money = new Money(note, spent, categoryId, day, month, year, Money.TYPE_SPEND);
                    compositeDisposable.add(
                            mViewModel.insertMoney(money)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            () -> {
                                                Toast.makeText(context, "Insert success", Toast.LENGTH_SHORT).show();
                                                clearTxb();
                                            },
                                            throwable -> {
                                                Log.e(TAG, throwable.getMessage(), throwable);
                                                Toast.makeText(context, "Insert failed", Toast.LENGTH_SHORT).show();
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

    public void clearTxb() {
        noteText.getText().clear();
        txbMoney.setText("0");
    }
}
