package vianh.nva.moneymanager.ui.report;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.Utils.Utils;
import vianh.nva.moneymanager.data.entity.Money;
import vianh.nva.moneymanager.data.entity.TotalMoneyDisplay;
import vianh.nva.moneymanager.databinding.FragmentMonthReportBinding;
import vianh.nva.moneymanager.ui.report.adapter.TotalMoneyAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthReportFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private final String TAG = this.getClass().getSimpleName();
    private MonthReportViewModel viewModel;
    private FragmentMonthReportBinding binding;
    private Calendar calendar = Calendar.getInstance();
    private TotalMoneyAdapter adapter = new TotalMoneyAdapter(null);
    private Boolean isSpend = true;
    private List<TotalMoneyDisplay> listSpend;
    private List<TotalMoneyDisplay> listEarn;
    public MonthReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View root = inflater.inflate(R.layout.fragment_month_report, container, false);
        // Khuc nay setup data binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_month_report, container, false);
        viewModel = ViewModelProviders.of(this).get(MonthReportViewModel.class);
        binding.setViewmodel(viewModel);
        return binding.getRoot();
    }

    // Clear all subscription
    @Override
    public void onStop() {
        super.onStop();
        viewModel.getCompositeDisposable().clear();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Khoi tao data voi thang nam hien tai
        setupData(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        initView();
    }

    private void initView() {
        // Adapter cho spinner month
        // Khoi tao tu string array trong res/values/strings
        ArrayAdapter<CharSequence> spinnerMonthAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.monthArray, android.R.layout.simple_spinner_item);
        spinnerMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Khoi tao array nam tu nam 2000 toi 2050
        ArrayList<String> years = new ArrayList<>();
        for(int i = 2000; i <= 2050; i++) {
            years.add("Nam" + i);
        }
        // Tao adapter cho spinner years
        ArrayAdapter<String> spinnerYearAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, years);
        spinnerYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // set adapter cho spinner
        binding.spinnerMonth.setAdapter(spinnerMonthAdapter);
        binding.spinnerYear.setAdapter(spinnerYearAdapter);
        // Set click listener
        binding.spinnerYear.setOnItemSelectedListener(this);
        binding.spinnerMonth.setOnItemSelectedListener(this);
        // Set selected la thang nam hien tai
        binding.spinnerMonth.setSelection(calendar.get(Calendar.MONTH));
        binding.spinnerYear.setSelection(calendar.get(Calendar.YEAR) - 2000);
        Log.d("adapter", "set adapter");

        // Chart
        // Set dung du lieu theo phan tram
        binding.chart.setUsePercentValues(true);

        // Khong hien thi description
        binding.chart.getDescription().setEnabled(false);
        // Set chu trong vong tron o tam pie chart
        binding.chart.setCenterText("Bieu do thu chi thang");
        // Set text color
        binding.chart.setCenterTextColor(R.color.darkBlueMaterial);
        // Set ve hinh tron tai tam bieu do
        binding.chart.setDrawHoleEnabled(true);
        // Mau cua hinh tron tai tam bieu do
        binding.chart.setHoleColor(Color.WHITE);
        // Do trong suot cua hinh tron tai tam bieu do
        binding.chart.setTransparentCircleAlpha(110);
        // Khong hien thi legend
        binding.chart.getLegend().setEnabled(false);
        // Set size cua hinh tron trong tam bieu do
        // Kich thuoc mac dinh laf 50f
        binding.chart.setHoleRadius(58f);
        // Set kich thuong cua vong tron bao quanh vong tron trong tam bieu do
        binding.chart.setTransparentCircleRadius(61f);

        // Set layout manager cho recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);
        // Set click listener cho button earn, hien thi bieu do va list tien thu
        binding.btnEarn.setOnClickListener(view -> {
            if (isSpend) {
                isSpend = false;
                binding.btnEarn.setTextColor(getResources().getColor(R.color.purpleMaterial));
                binding.btnSpend.setTextColor(getResources().getColor(R.color.black));
                setupDataRecyclerView();
                setupChart(listEarn);
            }
        });

        // Set click listener cho button spend, hien thi bieu do va list tien tieu
        binding.btnSpend.setOnClickListener(v -> {
            if (!isSpend) {
                isSpend = true;
                binding.btnSpend.setTextColor(getResources().getColor(R.color.purpleMaterial));
                binding.btnEarn.setTextColor(getResources().getColor(R.color.black));
                setupDataRecyclerView();
                setupChart(listSpend);
            }
        });

        // Set text color mac dinh cho button spend
        binding.btnSpend.setTextColor(getResources().getColor(R.color.purpleMaterial));
    }

    // Setup data cho chart
    private void setupChart(List<TotalMoneyDisplay> listMoney) {
        // List pie entry
        List<PieEntry> entries = new ArrayList<>();
        // List color cua entries
        ArrayList<Integer> colors = new ArrayList<>();
        // Tong tien
        long total = 0L;
        // Neu tien nhieu hon 0 va la type spend thi lay tong tien thu
        // nguoc lai lay tong tien chi
        if (listMoney.size() > 0 && listMoney.get(0).getType() == Money.TYPE_SPEND) {
            total = viewModel.getTotalSpend();
        } else {
            total = viewModel.getTotalEarn();
        }

        // Tinh % tien tren tong so cua tung category
        for (TotalMoneyDisplay money : listMoney) {
            colors.add(getResources().getColor(Utils.getResId(money.getColorName(), R.color.class)));
            float percent = Math.round(((float)money.getTotalMoney() / total) * 10000) / 100;
            entries.add(new PieEntry(percent, money.getDescription()));
            Log.d(TAG, "percent " + percent);
            Log.d(TAG, money.getColorName());
        }

        // Tao dataset
        PieDataSet set = new PieDataSet(entries, "Money report");
        // Set space giua hai entries
        set.setSliceSpace(5f);
        // Set text color cho text hien thi %
        set.setValueTextColor(R.color.black);
        // set do cao cho phan bieu do khi duoc bam vao
        // Mac dinh la 12f
        set.setSelectionShift(6f);
        // Set color cho tung slice
        set.setColors(colors);
        // Khoi tao pie data
        PieData data = new PieData(set);
        // Set text size cho text view hien thi %
        data.setValueTextSize(15f);
        // Set values format la %
        data.setValueFormatter(new PercentFormatter(binding.chart));
        // Cai nay ko nho de lam gif
        data.setValueTextColor(R.color.black);
        // Set data cho chart
        binding.chart.setData(data);
        // Set hieu ung cho chart
        binding.chart.animateY(1400, Easing.EaseInOutQuad);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
//        String text = String.valueOf(binding.spinnerMonth.getSelectedItemPosition()) + binding.spinnerYear.getSelectedItemPosition();
//        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        if (isSpend) {
            setupData(binding.spinnerMonth.getSelectedItemPosition() + 1, binding.spinnerYear.getSelectedItemPosition() + 2000);
        } else {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // Khoi tao data cho tab tien thu va tien chi
    private void setupData(int month, int year) {
        // Lay tong tien chi theo thang, nam, group theo category
        viewModel.getCompositeDisposable().add(
                viewModel.getTotalMoneySpend(month, year)
                        .subscribeOn(Schedulers.io()) // Nhung thang tren thang nay chay tren io thread
                        .observeOn(AndroidSchedulers.mainThread()) // Nhung thang ben duoi dong nay chay tren main thread
                        .subscribe(
                                listMoney -> {
                                    Log.d(TAG, "list money retrieve" + listMoney.size());
                                    listSpend = listMoney;
                                    // update lai recycler view
                                    setupDataRecyclerView();
                                    // neu la spend thi dua vao list spend, nguoc lai thi dua vao list earn
                                    if (isSpend) {
                                        setupChart(listSpend);
                                    } else {
                                        setupChart(listEarn);
                                    }
                                    adapter.setTotalMoneyDisplays(listMoney);
                                },
                                // Failed thi log lai
                                throwable -> Log.e(TAG, "can't get list money", throwable)
                        )
        );

        // Cai nay cung giong cai tren kia
        // Lay thong tin tong tien thu
        viewModel.getCompositeDisposable().add(
                viewModel.getTotalMoneyEarn(month, year)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                listMoney -> {
                                    Log.d(TAG, "list money retrieve" + listMoney.size());
                                    listEarn = listMoney;
                                    setupDataRecyclerView();
                                    if (isSpend) {
                                        setupChart(listSpend);
                                    } else {
                                        setupChart(listEarn);
                                    }
//                                    adapter.setTotalMoneyDisplays(listMoney);

                                },
                                throwable -> Log.e(TAG, "-can't get list money", throwable)
                        )
        );
    }

    // Setup data total money cho recycler view
    private void setupDataRecyclerView() {
        if (isSpend) {
            adapter.setTotalMoneyDisplays(listSpend);
        } else {
            adapter.setTotalMoneyDisplays(listEarn);
        }
        // format values 10000 -> 10.000
        binding.earnMoneyText.setText(Utils.currencyFormat(viewModel.getTotalEarn()));
        binding.spendMoneyText.setText(Utils.currencyFormat(-1 * viewModel.getTotalSpend()));
        binding.totalMoneyText.setText(Utils.currencyFormat(viewModel.getTotalEarn() - viewModel.getTotalSpend()));
    }
}
