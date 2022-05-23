package vianh.nva.moneymanager.ui.category;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;
import vianh.nva.moneymanager.data.AppRepository;
import vianh.nva.moneymanager.data.entity.Category;

public class CategoryViewModel extends AndroidViewModel {
    // App repository, class chua cac phuong thuc de giao tiep vs cac db
    private AppRepository appRepository;

    // Khoi tao app repository
    public CategoryViewModel(@NonNull Application application) {
        super(application);
        appRepository = AppRepository.getInstance(application);
    }
    // TODO: Implement the ViewModel

    // Lay list category chi tieu
    public LiveData<List<Category>> getListCategorySpend() {
        return appRepository.getListCategorySpend();
    }

    // Lay list category thu nhap
    public LiveData<List<Category>> getListCategoryEarn() {
        return appRepository.getListCategoryEarn();
    }

    // Insert category
    public Completable insertCategory(Category category) {
        return appRepository.insertCategory(category);
    }

    // Update category
    public Completable updateCategory(Category category) {
        return appRepository.updateCategory(category);
    }
}
