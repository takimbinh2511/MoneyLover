package vianh.nva.moneymanager.ui.category.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.Utils.Utils;

// adapter cho recycler view icon
public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconViewHolder> {
    private List<String> listIconName;
    private Context context;
    private int selectedPosition = 0;
    public IconAdapter(List<String> listIcon) {
        this.listIconName = listIcon;
    }

    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.icon_item, parent, false);
        return new IconViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {
        int iconId = Utils.getResId(listIconName.get(position), R.drawable.class);
        holder.icon.setImageResource(iconId);
        holder.itemView.setSelected(position == selectedPosition);
        holder.itemView.setOnClickListener(view -> {
            notifyItemChanged(selectedPosition);
            selectedPosition = position;
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return listIconName == null ? 0 : listIconName.size();
    }

    static class IconViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        public IconViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
        }
    }

    // Lay ten icon duoc chon
    public String getSelectedIconName() {
        return listIconName.get(selectedPosition);
    }

    // set selected position cho adapter
    public void setSelectedPosition(int selectedPosition) {
        notifyItemChanged(this.selectedPosition);
        this.selectedPosition = selectedPosition;
        notifyItemChanged(selectedPosition);
    }
}
