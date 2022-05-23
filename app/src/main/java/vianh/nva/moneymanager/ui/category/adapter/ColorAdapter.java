package vianh.nva.moneymanager.ui.category.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.Utils.Utils;

// Adapter cho recycler view color
public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {
    private List<String> listColorName;
    private Context context;
    private int selectedPos = 0;


    public ColorAdapter(List<String> listColorName) {
        this.listColorName = listColorName;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.color_item, parent, false);
        Log.d("Color adapter", String.valueOf(listColorName.size()));
        return new ColorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        // Lay color id theo ten
        int colorId = Utils.getResId(listColorName.get(position), R.color.class);

        // Lay thong tin drawable
        // Set drawable background cho color item
        StateListDrawable drawable = (StateListDrawable) holder.itemView.getBackground();
        DrawableContainer.DrawableContainerState drawableContainerState = (DrawableContainer.DrawableContainerState) drawable.getConstantState();
        assert drawableContainerState != null;
        Drawable[] children = drawableContainerState.getChildren();
        GradientDrawable selectedItem = (GradientDrawable) children[0];
        GradientDrawable unSelectedItem = (GradientDrawable) children[1];
//        GradientDrawable selectedDrawable = (GradientDrawable) selectedItem.getDrawable(0);
//        GradientDrawable unselectedDrawable = (GradientDrawable) unSelectedItem.getDrawable(0);
        selectedItem.setColor(context.getResources().getColor(colorId));
        unSelectedItem.setColor(context.getResources().getColor(colorId));

        // Set selected cho item dc chon
        holder.itemView.setSelected(position == selectedPos);
        // Neu click vao item khac thi set selected cho item do
        holder.itemView.setOnClickListener(
                v -> {
                    notifyItemChanged(selectedPos);
                    selectedPos = position;
                    notifyItemChanged(selectedPos);
                }
        );
    }

    @Override
    public int getItemCount() {
        return listColorName == null ? 0 : listColorName.size();
    }

    static class ColorViewHolder extends RecyclerView.ViewHolder {
        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public String getSelectedColorName() {
        return listColorName.get(selectedPos);
    }

    public void setSelectedPos (int selectedPos) {
        notifyItemChanged(this.selectedPos);
        this.selectedPos = selectedPos;
        notifyItemChanged(selectedPos);
    }
}
