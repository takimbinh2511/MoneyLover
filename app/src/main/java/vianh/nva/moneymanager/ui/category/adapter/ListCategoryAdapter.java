package vianh.nva.moneymanager.ui.category.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.Utils.Utils;
import vianh.nva.moneymanager.data.entity.Category;
import vianh.nva.moneymanager.data.entity.Money;
import vianh.nva.moneymanager.ui.category.CategoryActivity;

// Adapter cho activity list category
public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.CategoryViewHolder> {
    private List<Category> listCategory;
    private Context context;
    private boolean isSpend = true;

    // Mode de truyen vao khi chon them moi category
    public static final int MODE_INSERT = 5;
    public static final int MODE_UPDATE = 6;

    // Khoi tao adapter va truyen vao mode
    public ListCategoryAdapter(boolean isSpend) {
        this.isSpend = isSpend;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.money_item, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        // Lay icon id vs color id theo ten
        int iconId = Utils.getResId(listCategory.get(position).getIconName(), R.drawable.class);
        int colorId = Utils.getResId(listCategory.get(position).getColorName(), R.color.class);
        String desc = listCategory.get(position).getDescription();

        // Set thong tin cho item
        holder.categoryIcon.setColorFilter(ContextCompat.getColor(context, colorId), android.graphics.PorterDuff.Mode.SRC_IN);
        holder.categoryIcon.setImageResource(iconId);
        holder.categoryDescription.setText(desc);

        // Set onclick listener cho item
        // Click vao item thi hien thi trang thong tin category hoac them moi
        holder.fontView.setOnClickListener(view -> {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Intent intent = new Intent(activity, CategoryActivity.class);
            intent.putExtra("category", listCategory.get(position));
            if (position == 0) {
                intent.putExtra("mode", MODE_INSERT);
            } else {
                intent.putExtra("mode", MODE_UPDATE);
            }
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listCategory == null ? 0 : listCategory.size();
    }

    // Set list category cho adapter vs them item them moi vao dau list
    public void setListCategory(List<Category> listCategory) {
        List<Category> temp = new ArrayList<>();
        int type = isSpend ? Money.TYPE_SPEND : Money.TYPE_EARN;
        temp.add(new Category("ic_calendar", "black", "Them moi category", type));
        temp.addAll(listCategory);
        this.listCategory = temp;
        notifyDataSetChanged();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryIcon;
        private View fontView;
        private TextView categoryDescription;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.iconCategory);
            categoryDescription = itemView.findViewById(R.id.categoryName);
            fontView = itemView.findViewById(R.id.itemBg);
            // xoa money text view
            TextView moneyTextView = itemView.findViewById(R.id.moneyText);
            moneyTextView.setVisibility(View.INVISIBLE);
        }
    }
}
