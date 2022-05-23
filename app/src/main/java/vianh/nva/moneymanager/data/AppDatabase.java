package vianh.nva.moneymanager.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;

import vianh.nva.moneymanager.data.dao.CategoryDao;
import vianh.nva.moneymanager.data.dao.MoneyDao;
import vianh.nva.moneymanager.data.entity.Category;
import vianh.nva.moneymanager.data.entity.Money;

@Database(entities = {Money.class, Category.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract CategoryDao categoryDao();
    public abstract MoneyDao moneyDao();

    public static final String DATABASE_NAME = "money_manager";

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(mCallBack)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback mCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d("create db", "create db");
            new populateDbAsyncTask(INSTANCE).execute();
        }
    };

    private static class populateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private final CategoryDao categoryDao;
        public populateDbAsyncTask(AppDatabase appDatabase) {
            categoryDao = appDatabase.categoryDao();
        }


        // populate database after database is created
        @Override
        protected Void doInBackground(Void... voids) {

            categoryDao.deleteAll();
            ArrayList<Category> listCategory = new ArrayList<>();
            Category category = new Category("ic_dish",
                    "colorPrimary", "An uong", Money.TYPE_SPEND);
            Category party = new Category("ic_calendar",
                    "pinkMaterial", "Party", Money.TYPE_SPEND);

            Category shopping = new Category("ic_calendar",
                    "purpleBlueMaterial", "Shoping", Money.TYPE_SPEND);

            Category driving = new Category("ic_dish",
                    "orangeMaterial", "Driving", Money.TYPE_SPEND);
            listCategory.add(category);
            listCategory.add(party);
            listCategory.add(shopping);
            listCategory.add(driving);
            categoryDao.insertList(listCategory);

            for (Category category1 : listCategory) {
                category1.setType(Money.TYPE_EARN);
            }

            categoryDao.insertList(listCategory);

            Log.d("AppDatabase", "inserted");
            return null;
        }
    }
}
