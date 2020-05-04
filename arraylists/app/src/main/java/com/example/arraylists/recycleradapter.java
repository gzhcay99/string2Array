package com.example.arraylists;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arraylists.util.NumberFormat;

import java.util.ArrayList;

public class recycleradapter extends RecyclerView.Adapter<recycleradapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Model> models;
    private ArrayList<Float> diff;

    public recycleradapter(Context mContext, ArrayList<Model> models, ArrayList<Float> diff) {
        this.mContext = mContext;
        this.models = models;
        this.diff = diff;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Model model = models.get(position);
        holder.description.setText(model.getDescription());
        holder.currency.setText(model.getCurrency());
        holder.amount.setText(NumberFormat.string(model.getAmount()));
        holder.change.setText(NumberFormat.changefloatNo(diff.get(position)));
        if (diff.get(position) >= 0) {
            holder.change.setTextColor(Color.BLUE);
        }

    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView description, currency, amount, change;
        LinearLayout parentlayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.desc_text);
            currency = itemView.findViewById(R.id.ccy_text);
            amount = itemView.findViewById(R.id.amount_text);
            change = itemView.findViewById(R.id.amountdiff_text);
            parentlayout = itemView.findViewById(R.id.itemview_layout);


        }


    }
}

