package vianh.nva.moneymanager.ui.calendar.adapter;

import vianh.nva.moneymanager.data.entity.Money;

public class MoneyAdapterItem extends AdapterItem {
    private Money money;

    @Override
    public int getType() {
        return TYPE_ITEM;
    }

    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }
}
