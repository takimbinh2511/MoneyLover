package vianh.nva.moneymanager.ui.report.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.Utils.Utils;
import vianh.nva.moneymanager.data.entity.TotalMoneyDisplay;

// Adapter cho recycler view hien thi tong tien
public class TotalMoneyAdapter extends RecyclerView.Adapter<TotalMoneyAdapter.MyViewHolder> {
    private List<TotalMoneyDisplay> totalMoneyDisplays;
    private Context context;

    // Constructor
    public TotalMoneyAdapter(List<TotalMoneyDisplay> totalMoneyDisplays) {
        this.totalMoneyDisplays = totalMoneyDisplays;
    }

    // Truyen vao list total money xong roi refresh lai recycler view
    // Dung de update list money
    public void setTotalMoneyDisplays(List<TotalMoneyDisplay> totalMoneyDisplays) {
        this.totalMoneyDisplays = totalMoneyDisplays;
        notifyDataSetChanged();
    }

    // Nay de khoi tao view holder thoi
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.total_money_item, parent, false);
        return new MyViewHolder(itemView);
    }

    // Set cac thong tin cho view holder(i.e item trong recycler view)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Lay money trong list
        TotalMoneyDisplay money = totalMoneyDisplays.get(position);

        // Lay id money vs id color bang ten
        int color = Utils.getResId(money.getColorName(), R.color.class);
        int icon = Utils.getResId(money.getIconName(), R.drawable.class);

        // Set cac thong tin icon, money, description(i.e ten icon) cho item
        holder.categoryIcon.setImageResource(icon);
        holder.categoryIcon.setColorFilter(ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN);
        holder.totalMoney.setText(Utils.currencyFormat(money.getTotalMoney()));
        holder.description.setText(money.getDescription());
    }

    @Override
    public int getItemCount() {
        if (totalMoneyDisplays == null) {
            return 0;
        } else
            return totalMoneyDisplays.size();
    }

    // Class item
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryIcon;
        private TextView description;
        private TextView totalMoney;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.iconCategory);
            description = itemView.findViewById(R.id.categoryName);
            totalMoney = itemView.findViewById(R.id.moneyText);
        }
    }
}
