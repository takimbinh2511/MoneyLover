package vianh.nva.moneymanager.ui.calendar;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.data.AppRepository;
import vianh.nva.moneymanager.data.entity.Category;
import vianh.nva.moneymanager.data.entity.Money;

public class CalendarViewModel extends AndroidViewModel {
    AppRepository repository;

    public CalendarViewModel(Application application) {
        super(application);
        repository = AppRepository.getInstance(application);
    }

    public Flowable<List<Money>> getListMoneyByMonthAndYear(int month, int year) {
        return repository.getListMoneyByMonthAndYear(month, year);
    }

    public Flowable<HashMap<Integer, Category>> getMapCategory() {
        return repository.getAllCategory()
                .map(categories -> {
                   HashMap<Integer, Category> map = new HashMap<>();
                   for (Category category : categories) {
                       map.put(category.getId(), category);
                   }

                   return map;
                });
    }
}