package vianh.nva.moneymanager.ui.calendar.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.Utils.Utils;
import vianh.nva.moneymanager.data.entity.Category;
import vianh.nva.moneymanager.data.entity.Money;
import vianh.nva.moneymanager.ui.money.MoneyDetailActivity;

public class MoneyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<AdapterItem> listItem;
    private final Context context;
    private HashMap<Integer, Category> mapCategory;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public MoneyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return listItem.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == AdapterItem.TYPE_DATE) {
            View view = LayoutInflater.from(context).inflate(R.layout.money_item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.money_item, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AdapterItem item = listItem.get(position);
        if (item.getType() == AdapterItem.TYPE_DATE) {
            ((HeaderViewHolder) holder).textDate
                    .setText(simpleDateFormat.format(((MoneyApdaterHeader) item).getCalendar().getTime()));
        } else {
            ((ItemViewHolder) holder).money.setText(Utils.currencyFormat(((MoneyAdapterItem) item).getMoney().getMoney()));

            // case category load successful
            if (mapCategory != null) {
                int categoryId = ((MoneyAdapterItem) item).getMoney().getCategoryId();
                Category category = mapCategory.get(categoryId);
                assert category != null;
                int iconId = Utils.getResId(category.getIconName(), R.drawable.class);
                int colorId = Utils.getResId(category.getColorName(), R.color.class);
                ((ItemViewHolder) holder).categoryIcon.setImageResource(iconId);
                ((ItemViewHolder) holder).categoryIcon.setColorFilter(ContextCompat.getColor(context, colorId), android.graphics.PorterDuff.Mode.SRC_IN);
                ((ItemViewHolder) holder).categoryName.setText(category.getDescription());
                ((ItemViewHolder)holder).itemBg.setOnClickListener(view -> {
                    Intent intent = new Intent(context, MoneyDetailActivity.class);
                    intent.putExtra("Money", ((MoneyAdapterItem)listItem.get(position)).getMoney());
                    context.startActivity(intent);
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (listItem != null) {
            return listItem.size();
        } else
            return 0;
    }

    public void setListItem(List<AdapterItem> listItem) {
        this.listItem = new ArrayList<>();
        this.listItem.addAll(listItem);
        notifyDataSetChanged();
    }

    public void setMapCategory(HashMap<Integer, Category> mapCategory) {
        this.mapCategory = mapCategory;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        listItem.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(int position, AdapterItem item) {
        listItem.add(position, item);
        notifyItemInserted(position);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        private TextView textDate;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.textDate);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView categoryIcon;
        private TextView categoryName;
        private TextView money;
        public View itemBg, deleteBackGround;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.iconCategory);
            categoryName = itemView.findViewById(R.id.categoryName);
            money = itemView.findViewById(R.id.moneyText);
            itemBg = itemView.findViewById(R.id.itemBg);
            deleteBackGround = itemView.findViewById(R.id.bgDelete);
        }
    }
}
