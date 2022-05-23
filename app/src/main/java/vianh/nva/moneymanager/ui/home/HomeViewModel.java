package vianh.nva.moneymanager.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;
import vianh.nva.moneymanager.data.AppRepository;
import vianh.nva.moneymanager.data.entity.Category;
import vianh.nva.moneymanager.data.entity.Money;

public class HomeViewModel extends AndroidViewModel {

    // Repository cho app, de quan ly cac db local hoac remote, tat ca cac giao tiep voi db deu dung thang nay
    private AppRepository appRepository;

    // Live data chua list category, duoc update moi khi category trong db thay doi
    private LiveData<List<Category>> listCategorySpend;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        appRepository = AppRepository.getInstance(application);
        this.listCategorySpend = appRepository.getListCategorySpend();
    }

    // Lay tat ca cac category chi tieu
    public LiveData<List<Category>> getListCategorySpend() {
//        Log.d("Home viewmodel", String.valueOf(listCategorySpend.getValue().size()));
        return listCategorySpend;
    }

    // Lay tat ca cac category thu nhap
    public LiveData<List<Category>> getListCategoryEarn() {
        return appRepository.getListCategoryEarn();
    }

    // Insert money
    public Completable insertMoney(Money money) {
        // check xem co am hay ko
        if (!isValid(money)) {
            return Completable.error(new Throwable("Invalid argument"));
        }

        // TYPE_SPEND thi nhan -1
        // vi tinh lam chuc nang tien du dau thang ma luoi nen bo?
        if (money.getType() == Money.TYPE_SPEND) {
            long value = money.getMoney() * -1;
            money.setMoney(value);
        }

        // insert money
        return appRepository.insertMoney(money);
    }

    // Update money
    public Completable updateMoney(Money money) {
        if (!isValid(money)) {
            return Completable.error(new Throwable("Invalid argument"));
        }
        if (money.getType() == Money.TYPE_SPEND) {
            long value = money.getMoney() * -1;
            money.setMoney(value);
        }
        return appRepository.updateMoney(money);
    }

    // Delete money
    public Completable deleteMoney(Money money) {
        return appRepository.deleteMoney(money);
    }

    private boolean isValid(Money money) {
        return money.getMoney() > 0;
    }
}