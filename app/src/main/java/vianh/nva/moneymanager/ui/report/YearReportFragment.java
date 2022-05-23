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
import vianh.nva.moneymanager.databinding.FragmentYearReportBinding;
import vianh.nva.moneymanager.ui.report.adapter.TotalMoneyAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
// Giong fragment month, khac cai la no khong co spinner thang thoi
public class YearReportFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private FragmentYearReportBinding binding;
    private Calendar calendar = Calendar.getInstance();

    @Override
    public void onStop() {
        super.onStop();
        viewModel.getCompositeDisposable().clear();
    }

    private YearReportViewModel viewModel;
    private boolean isSpend = true;
    private TotalMoneyAdapter adapter = new TotalMoneyAdapter(null);
    private List<TotalMoneyDisplay> listSpend;
    private List<TotalMoneyDisplay> listEarn;

    public YearReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this).get(YearReportViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_year_report, container, false);
        binding.setViewModel(viewModel);
//        return inflater.inflate(R.layout.fragment_year_report, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupData(calendar.get(Calendar.YEAR));
        innitView();

    }

    private void innitView() {
        ArrayList<String> years = new ArrayList<>();
        for(int i = 2000; i <= 2050; i++) {
            years.add("Nam" + i);
        }
        ArrayAdapter<String> spinnerYearAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, years);
        spinnerYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerYear.setAdapter(spinnerYearAdapter);
        binding.spinnerYear.setSelection(calendar.get(Calendar.YEAR) - 2000);

        binding.chart.setUsePercentValues(true);
        binding.chart.getDescription().setEnabled(false);
        binding.chart.setCenterText("Bieu do thu chi nam");
        binding.chart.setCenterTextColor(R.color.darkBlueMaterial);
        binding.chart.setDrawHoleEnabled(true);
        binding.chart.setHoleColor(Color.WHITE);
        binding.chart.setTransparentCircleAlpha(110);
        binding.chart.getLegend().setEnabled(false);
        binding.chart.setHoleRadius(58f);
        binding.chart.setTransparentCircleRadius(61f);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);

        binding.btnEarn.setOnClickListener(view -> {
            if (isSpend) {
                isSpend = false;
                binding.btnEarn.setTextColor(getResources().getColor(R.color.purpleMaterial));
                binding.btnSpend.setTextColor(getResources().getColor(R.color.black));
                setupDataRecyclerView();
                setupChart(listEarn);
            }
        });

        binding.btnSpend.setOnClickListener(v -> {
            if (!isSpend) {
                isSpend = true;
                binding.btnSpend.setTextColor(getResources().getColor(R.color.purpleMaterial));
                binding.btnEarn.setTextColor(getResources().getColor(R.color.black));
                setupDataRecyclerView();
                setupChart(listSpend);
            }
        });

        binding.btnSpend.setTextColor(getResources().getColor(R.color.purpleMaterial));
    }

    private void setupChart(List<TotalMoneyDisplay> listMoney) {
        List<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        long total = 0L;
        if (listMoney.size() > 0 && listMoney.get(0).getType() == Money.TYPE_SPEND) {
            total = viewModel.getTotalSpend();
        } else {
            total = viewModel.getTotalEarn();
        }

        for (TotalMoneyDisplay money : listMoney) {
            colors.add(getResources().getColor(Utils.getResId(money.getColorName(), R.color.class)));
            float percent = Math.round((float)((float)money.getTotalMoney() / total) * 10000) / 100;
            entries.add(new PieEntry(percent, money.getDescription()));
            Log.d(TAG, "percent " + percent);
            Log.d(TAG, money.getColorName());
        }

        PieDataSet set = new PieDataSet(entries, "Money report");
        set.setSliceSpace(5f);
        set.setValueTextColor(R.color.black);
        set.setSelectionShift(6f);
        set.setColors(colors);
        PieData data = new PieData(set);
        data.setValueTextSize(15f);
        data.setValueFormatter(new PercentFormatter(binding.chart));
        data.setValueTextColor(R.color.black);
        binding.chart.setData(data);
        binding.chart.animateY(1400, Easing.EaseInOutQuad);
    }

    private void setupData(int year) {
        viewModel.getCompositeDisposable().add(
                viewModel.getTotalMoneySpendByYear(year)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                listMoney -> {
                                    Log.d(TAG, "list money retrieve" + listMoney.size());
                                    listSpend = listMoney;
                                    setupDataRecyclerView();
                                    if (isSpend) {
                                        setupChart(listSpend);
                                    } else {
                                        setupChart(listEarn);
                                    }
                                    adapter.setTotalMoneyDisplays(listMoney);
                                },
                                throwable -> Log.e(TAG, "can't get list money", throwable)
                        )
        );

        viewModel.getCompositeDisposable().add(
                viewModel.getTotalMoneyEarnByYear(year)
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

    private void setupDataRecyclerView() {
        if (isSpend) {
            adapter.setTotalMoneyDisplays(listSpend);
        } else {
            adapter.setTotalMoneyDisplays(listEarn);
        }
        binding.txtEarn.setText(String.valueOf(viewModel.getTotalEarn()));
        binding.txtSpend.setText(String.valueOf(viewModel.getTotalSpend()));
        binding.txtTotal.setText(String.valueOf(viewModel.getTotalEarn() + viewModel.getTotalSpend()));
    }
}
