package vianh.nva.moneymanager.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import vianh.nva.moneymanager.data.entity.Money;
import vianh.nva.moneymanager.data.entity.TotalMoneyDisplay;

@Dao
public interface MoneyDao {
    @Insert
    Completable insert(Money money);

    @Query("SELECT * FROM money where year = :year and month = :month and year = :year and day = :day")
    LiveData<List<Money>> getMoneyByDate(int day, int month, int year);

    @Query("SELECT * FROM money WHERE month = :month AND year = :year ORDER BY day ASC")
    Flowable<List<Money>> getMoneyByMonthAndYear(int month, int year);

    @Query("Delete from money")
    Completable deleteAll();

    @Query("SELECT total(money) AS totalMoney, money.type, day, month, year, description, iconName, colorName " +
            "FROM money INNER JOIN category ON money.categoryId = category.id " +
            "WHERE month = :month AND year = :year AND money.type = 1 GROUP BY money.categoryId")
    Flowable<List<TotalMoneyDisplay>> getTotalMoneyEarnByMonthAndYear(int month, int year);

    @Query("SELECT total(money) AS totalMoney, money.type, day, month, year, description, iconName, colorName " +
            "FROM money INNER JOIN category ON money.categoryId = category.id " +
            "WHERE month = :month AND year = :year AND money.type = 0 GROUP BY money.categoryId")
    Flowable<List<TotalMoneyDisplay>> getTotalMoneySpendByMonthAndYear(int month, int year);

    @Query("SELECT total(money) AS totalMoney, money.type, day, month, year, description, iconName, colorName " +
            "FROM money INNER JOIN category ON money.categoryId = category.id " +
            "WHERE year = :year AND money.type = 0 GROUP BY money.categoryId")
    Flowable<List<TotalMoneyDisplay>> getTotalMoneySpendByYear(int year);

    @Query("SELECT total(money) AS totalMoney, money.type, day, month, year, description, iconName, colorName " +
            "FROM money INNER JOIN category ON money.categoryId = category.id " +
            "WHERE year = :year AND money.type = 1 GROUP BY money.categoryId")
    Flowable<List<TotalMoneyDisplay>> getTotalMoneyEarnByYear(int year);

    @Update
    Completable updateMoney(Money money);

    @Delete
    Completable deleteMoney(Money money);
}
