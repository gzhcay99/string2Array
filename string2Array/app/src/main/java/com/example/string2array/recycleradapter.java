package com.example.string2array;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recycleradapter extends RecyclerView.Adapter<recycleradapter.ViewHolder>  {
    private Context mContext;
    private ArrayList<Model> models;
    private ArrayList<Float> diff;
    private OnModelListener mOnModelListener;

    public recycleradapter(Context mContext, ArrayList<Model> models, ArrayList<Float> diff,OnModelListener onModelListener) {
        this.mContext = mContext;
        this.models = models;
        this.diff =diff;
        this.mOnModelListener = onModelListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnModelListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Model model = models.get(position);
        holder.description.setText(model.getDescription());
        holder.currency.setText(model.getCurrency());
        holder.amount.setText(NumberFormat.string(model.getAmount()));
        holder.change.setText(NumberFormat.changefloatNo(diff.get(position)));
        if(diff.get(position)>=0){holder.change.setTextColor(Color.BLUE);}

        }

    @Override
    public int getItemCount() {
        return models.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView description, currency, amount, change;
        LinearLayout parentlayout;
        OnModelListener onModelListener;

        public ViewHolder(@NonNull View itemView, OnModelListener onModelListener) {
            super(itemView);
            description = itemView.findViewById(R.id.desc_text);
            currency = itemView.findViewById(R.id.ccy_text);
            amount = itemView.findViewById(R.id.amount_text);
            change =itemView.findViewById(R.id.amountdiff_text);
            parentlayout = itemView.findViewById(R.id.itemview_layout);
            this.onModelListener=onModelListener;
            itemView.setOnClickListener(this);
             }

        @Override
        public void onClick(View v) {
            onModelListener.onModelClick(getAdapterPosition());
        }
    }

    public interface OnModelListener{
        void onModelClick(int position);

    }
}

