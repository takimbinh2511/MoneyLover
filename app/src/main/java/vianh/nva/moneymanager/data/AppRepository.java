package vianh.nva.moneymanager.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import vianh.nva.moneymanager.data.entity.Category;
import vianh.nva.moneymanager.data.entity.Money;
import vianh.nva.moneymanager.data.entity.TotalMoneyDisplay;

public class AppRepository {

    private final AppDatabase db;
    private static AppRepository sInstance;

    private AppRepository(final Application application) {
        db = AppDatabase.getInstance(application);
    }

    // This must only use with application
    public static AppRepository getInstance(Application application) {
        if (sInstance == null) {
            synchronized (AppRepository.class) {
                if (sInstance == null) {
                    sInstance = new AppRepository(application);
                }
            }
        }

        return sInstance;
    }

    public LiveData<List<Category>> getListCategorySpend() {
        return db.categoryDao().getCategoriesByType(Money.TYPE_SPEND);
    }

    public LiveData<List<Category>> getListCategoryEarn() {
        return db.categoryDao().getCategoriesByType(Money.TYPE_EARN);
    }

    public Completable insertMoney(Money money) {
        Log.d("insert", "inserted");
        return db.moneyDao().insert(money);
    }

    public Flowable<List<Money>> getListMoneyByMonthAndYear(int month, int year) {
        return db.moneyDao().getMoneyByMonthAndYear(month, year);
    }

    public Flowable<List<Category>> getAllCategory() {
        return db.categoryDao().getAllCategory();
    }

    public Flowable<List<TotalMoneyDisplay>> getTotalMoneyEarnByMonthYear(int month, int year) {
        return db.moneyDao().getTotalMoneyEarnByMonthAndYear(month, year);
    }

    public Flowable<List<TotalMoneyDisplay>> getTotalMoneySpendByMonthYear(int month, int year) {
        return db.moneyDao().getTotalMoneySpendByMonthAndYear(month, year);
    }

    public Flowable<List<TotalMoneyDisplay>> getTotalMoneySpendByYear(int year) {
        return db.moneyDao().getTotalMoneySpendByYear(year);
    }

    public Flowable<List<TotalMoneyDisplay>> getTotalMoneyEarnByYear(int year) {
        return db.moneyDao().getTotalMoneyEarnByYear(year);
    }

    public Completable insertCategory(Category category) {
        return db.categoryDao().insert(category);
    }

    public Completable updateCategory(Category category) {
        return db.categoryDao().update(category);
    }

    public Completable deleteCategory(Category category) {
        return db.categoryDao().delete(category);
    }

    public void insertListCategory(List<Category> listCategory) {
        db.categoryDao().insertList(listCategory);
    }

    public Completable updateMoney(Money money) {
        return db.moneyDao().updateMoney(money);
    }

    public Completable deleteMoney(Money money) {
        return db.moneyDao().deleteMoney(money);
    }
}
