package vianh.nva.moneymanager.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import vianh.nva.moneymanager.data.entity.Category;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Category category);

    @Insert
    void insertList(List<Category> categoryList);

    @Update
    Completable update(Category category);

    // Money type
    @Query("SELECT * FROM category WHERE type = :type")
    LiveData<List<Category>> getCategoriesByType(int type);

    @Query("SELECT * FROM category")
    Flowable<List<Category>> getAllCategory();

    @Query("delete from category")
    void deleteAll();

    @Delete
    Completable delete(Category category);
}
