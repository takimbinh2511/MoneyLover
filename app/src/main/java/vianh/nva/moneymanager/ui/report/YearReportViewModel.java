package vianh.nva.moneymanager.ui.report;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import vianh.nva.moneymanager.data.AppRepository;
import vianh.nva.moneymanager.data.entity.TotalMoneyDisplay;
// Giong month report model
public class YearReportViewModel extends AndroidViewModel {
    private AppRepository appRepository;
    private long totalEarn = 0L;
    private long totalSpend = 0L;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public YearReportViewModel(@NonNull Application application) {
        super(application);
        appRepository = AppRepository.getInstance(application);
    }

    public Flowable<List<TotalMoneyDisplay>> getTotalMoneySpendByYear(int year) {
        return appRepository.getTotalMoneySpendByYear(year).map(
                totalMoneyDisplays -> {
                    for (TotalMoneyDisplay money : totalMoneyDisplays) {
                        totalSpend += money.getTotalMoney();
                    }
                    return totalMoneyDisplays;
                }
        );
    }

    public Flowable<List<TotalMoneyDisplay>> getTotalMoneyEarnByYear(int year) {
        return appRepository.getTotalMoneyEarnByYear(year).map(
                totalMoneyDisplays -> {
                    for (TotalMoneyDisplay money : totalMoneyDisplays) {
                        totalEarn += money.getTotalMoney();
                    }
                    return totalMoneyDisplays;
                }
        );
    }

    public long getTotalEarn() {
        return totalEarn;
    }

    public long getTotalSpend() {
        return totalSpend;
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }
}
