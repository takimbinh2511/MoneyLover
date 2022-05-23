package vianh.nva.moneymanager.ui.calendar.adapter;

import java.util.Calendar;
import java.util.Date;

public class MoneyApdaterHeader extends AdapterItem {
    private Calendar calendar;

    @Override
    public int getType() {
        return TYPE_DATE;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
