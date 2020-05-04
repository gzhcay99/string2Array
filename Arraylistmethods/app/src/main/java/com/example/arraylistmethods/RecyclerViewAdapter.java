package com.example.arraylistmethods;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arraylistmethods.util.*;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<Model> models;
    //private ArrayList<String> mImageNames;

    public RecyclerViewAdapter(Context mContext, ArrayList<Model> models) {
        this.models = models;
        this.mContext = mContext;
    }

    private Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemview_card, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.amount.setText(NumberFormat.string(models.get(position).getAmount()));
        holder.desc.setText(models.get(position).getDescription());
        holder.ccy.setText(models.get(position).getCurrency());

        /*Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.imageView);
        holder.imageName.setText(mImageNames.get(position));*/
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + models.get(position));
                //Toast.makeText(mContext, mAssets.get(position), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, GalleryActivity.class);
                intent.putExtra("category", models.get(position).getCategory());
                intent.putExtra("subcategory", models.get(position).getSubcategory());
                intent.putExtra("description", models.get(position).getDescription());
                intent.putExtra("country", models.get(position).getCountry());
                intent.putExtra("currency", models.get(position).getCurrency());
                intent.putExtra("amount", models.get(position).getAmount());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView desc;
        TextView ccy;
        TextView amount;
        ImageView image;
        LinearLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            desc = itemView.findViewById(R.id.textView);
            ccy = itemView.findViewById(R.id.textView2);
            amount = itemView.findViewById(R.id.textView3);
            parentLayout = itemView.findViewById(R.id.card_layout);
        }
    }
}
