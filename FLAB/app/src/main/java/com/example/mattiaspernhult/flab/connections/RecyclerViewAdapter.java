package com.example.mattiaspernhult.flab.connections;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mattiaspernhult.flab.R;
import com.example.mattiaspernhult.flab.models.Economi;

import java.util.List;

/**
 * Created by mattiaspernhult on 2015-09-12.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.EconomiViewHolder> {

    private List<Economi> economis;

    public RecyclerViewAdapter(List<Economi> economis) {
        this.economis = economis;
    }

    public void updateList(List<Economi> e) {
        this.economis = e;
        notifyDataSetChanged();
    }

    @Override
    public EconomiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        EconomiViewHolder evh = new EconomiViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(EconomiViewHolder holder, int position) {
        String category = economis.get(position).getCategory();
        switch (category) {
            case "Food":
                holder.ivCategory.setImageResource(R.drawable.food_rr);
                break;
            case "Travel":
                holder.ivCategory.setImageResource(R.drawable.travel_rr);
                break;
            case "Living":
                holder.ivCategory.setImageResource(R.drawable.living_rr);
                break;
            case "Spare Time":
                holder.ivCategory.setImageResource(R.drawable.spare_time_rr);
                break;
            case "Others":
                holder.ivCategory.setImageResource(R.drawable.other_rr);
                break;
            case "Salary":
                holder.ivCategory.setImageResource(R.drawable.money_rr);
                break;
        }
        holder.tvTitle.setText(economis.get(position).getTitle());
        holder.tvDate.setText(economis.get(position).getDate());
        holder.tvPrice.setText(String.valueOf(economis.get(position).getPrice()) + " kr");
    }

    @Override
    public int getItemCount() {
        return this.economis.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class EconomiViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategory;
        TextView tvTitle;
        TextView tvDate;
        TextView tvPrice;

        public EconomiViewHolder(View itemView) {
            super(itemView);
            ivCategory = (ImageView) itemView.findViewById(R.id.ivCategory);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
        }
    }
}
