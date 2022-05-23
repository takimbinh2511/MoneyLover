package vianh.nva.moneymanager.ui.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.data.entity.Money;
import vianh.nva.moneymanager.ui.calendar.adapter.AdapterItem;
import vianh.nva.moneymanager.ui.calendar.adapter.MoneyAdapter;
import vianh.nva.moneymanager.ui.calendar.adapter.MoneyAdapterItem;
import vianh.nva.moneymanager.ui.calendar.adapter.MoneyApdaterHeader;
import vianh.nva.moneymanager.ui.home.HomeViewModel;
import vianh.nva.moneymanager.ui.view.RecyclerItemTouchHelper;

public class CalendarFragment extends Fragment implements OnCalendarPageChangeListener, OnDayClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private final String TAG = this.getClass().getSimpleName();
    private Calendar calendar = Calendar.getInstance();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private CalendarView calendarView;
    private CalendarViewModel calendarViewModel;
    private HomeViewModel homeViewModel;
    private List<AdapterItem> listItem = new ArrayList<>();
    private MoneyAdapter moneyAdapter;
    private RecyclerView recyclerView;
    private View calendarLayout;
    private HashMap<Integer, Integer> positionMap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel =
                ViewModelProviders.of(this).get(CalendarViewModel.class);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = root.findViewById(R.id.calendarView);
        recyclerView = root.findViewById(R.id.listMoney);
        calendarLayout = root.findViewById(R.id.calendarLayout);
        calendarView.setOnPreviousPageChangeListener(this);
        calendarView.setOnForwardPageChangeListener(this);
        calendarView.setOnDayClickListener(this);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        compositeDisposable.add(calendarViewModel.getMapCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        map -> moneyAdapter.setMapCategory(map),
                        throwable -> Log.e(TAG, "err", throwable)
                ));

        moneyAdapter = new MoneyAdapter(getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(moneyAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        setupData(calendar);
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        compositeDisposable.clear();
//        Log.d(TAG, "Clear---------------------------------------");
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        Log.d(TAG, "Clear--------------------------------------- destroy");
    }

    @Override
    public void onChange() {
        Calendar c = calendarView.getCurrentPageDate();
        setupData(c);
    }

    private void setupData(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        // TODO: Refactor this
//        compositeDisposable.add(calendarViewModel.getListMoneyByMonthAndYear(month, year)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        e -> Log.d(TAG, String.valueOf(e.size())),
//                        throwable -> Log.e(TAG, "ERR", throwable)
//                ));

        compositeDisposable.add(calendarViewModel.getListMoneyByMonthAndYear(month, year)
                .subscribeOn(Schedulers.io())
                .map(monies -> {        // map list money to list event
                    List<EventDay> events = new ArrayList<>();
                    HashMap<Integer, Integer> mapPosition = new HashMap<>();
                    int tempDate = 0;
                    int index = 0;
                    listItem.clear();
                    for (Money money : monies) {
                        Calendar date = Calendar.getInstance();
                        date.set(money.getYear(), money.getMonth() -1, money.getDay());
                        // handle money for adapter
                        if (date.get(Calendar.DAY_OF_MONTH) != tempDate) {
                            events.add(new EventDay(date, R.drawable.ic_calendar));
                            MoneyApdaterHeader header = new MoneyApdaterHeader();
                            header.setCalendar(date);
                            listItem.add(header);
                            tempDate = date.get(Calendar.DAY_OF_MONTH);
                            mapPosition.put(date.get(Calendar.DAY_OF_MONTH), index);
                            index++;
                        }

                        MoneyAdapterItem moneyItem = new MoneyAdapterItem();
                        moneyItem.setMoney(money);
                        listItem.add(moneyItem);
                        index++;
                    }
                    positionMap = mapPosition;
                    return events;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        events -> {
                            Log.d(TAG, "success " + events.size() + "Month " + month);
                            calendarView.setEvents(events);
                            moneyAdapter.setListItem(listItem);
                        },
                        throwable -> Log.d("err", "err", throwable)
                )
        );
    }

    @Override
    public void onDayClick(EventDay eventDay) {
        int day = eventDay.getCalendar().get(Calendar.DAY_OF_MONTH);
        if (positionMap.get(day) != null) {
            Log.d(TAG, "clicked");
            recyclerView.smoothScrollToPosition(positionMap.get(day));
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        Money money = ((MoneyAdapterItem)listItem.get(viewHolder.getAdapterPosition())).getMoney();
//        moneyAdapter.removeItem(viewHolder.getAdapterPosition());
//        int deletePos = viewHolder.getAdapterPosition();
        if (money.getType() == Money.TYPE_SPEND) {
            money.setMoney(-1 * money.getMoney());
        }
        compositeDisposable.add(homeViewModel.deleteMoney(money)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Snackbar snackbar = Snackbar
                                    .make(calendarLayout, "Item removed from cart!", Snackbar.LENGTH_LONG);
                            snackbar.setAction("UNDO", view -> restore(money));
                            snackbar.setActionTextColor(Color.YELLOW);
                            snackbar.show();
                        }
                ));
    }

    public void restore (Money money) {
        compositeDisposable.add(homeViewModel.insertMoney(money)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Toast.makeText(getContext(), "Restored", Toast.LENGTH_SHORT).show(),
                        throwable -> {
                            Log.d(TAG, "Restore failed", throwable);
                            Toast.makeText(getContext(), "Restored failed", Toast.LENGTH_SHORT).show();
                        }
                ));
    }
}