package vianh.nva.moneymanager.ui.calendar.adapter;

public abstract class AdapterItem {
    public static final int TYPE_DATE = 0;
    public static final int TYPE_ITEM = 1;

    abstract public int getType();
}
