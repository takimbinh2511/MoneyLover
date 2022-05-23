package vianh.nva.moneymanager.ui.report;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import vianh.nva.moneymanager.data.AppRepository;
import vianh.nva.moneymanager.data.entity.Money;
import vianh.nva.moneymanager.data.entity.TotalMoneyDisplay;

public class MonthReportViewModel extends AndroidViewModel {
    // Repository chua tat ca cac phuong thuc de giao tiep voi cac db
    private AppRepository appRepository;
    // Composite disposable de dispose nhieu subscription
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    // Tong tien thu
    private long totalEarn = 0L;
    // Tong tien chi
    private long totalSpend = 0L;

    public MonthReportViewModel(@NonNull Application application) {
        super(application);
        // Lay instance app repository
        appRepository = AppRepository.getInstance(application);
    }

    // Lay list tong tien thu
    public Flowable<List<TotalMoneyDisplay>> getTotalMoneyEarn(int month, int year) {
        return appRepository.getTotalMoneyEarnByMonthYear(month, year).map(
                totalMoneyDisplays -> {
                    // Tinh tong tien thu
                    long totalMoney = 0L;
                    for (TotalMoneyDisplay money : totalMoneyDisplays) {
                        totalMoney += money.getTotalMoney();
                    }
                    totalEarn = totalMoney;
                    return totalMoneyDisplays;
                }
        );
    }

    // Lay list tong tien chi
    public Flowable<List<TotalMoneyDisplay>> getTotalMoneySpend(int month, int year) {
        return appRepository.getTotalMoneySpendByMonthYear(month, year).map(
                totalMoneyDisplays -> {
                    // tinh tong tien chi
                    long totalMoney = 0L;
                    for (TotalMoneyDisplay money : totalMoneyDisplays) {
                        money.setTotalMoney(-1 * money.getTotalMoney());
                        totalMoney += money.getTotalMoney();
                    }
                    totalSpend = totalMoney;
                    return totalMoneyDisplays;
                }
        );
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    public long getTotalEarn() {
        return totalEarn;
    }

    public long getTotalSpend() {
        return totalSpend;
    }
}
