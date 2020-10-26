package com.introverted.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.introverted.CardStackAdapter;
import com.introverted.MatchModel;
import com.introverted.R;
import com.introverted.cardModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HorizontalMatchesAdapter extends RecyclerView.Adapter<HorizontalMatchesAdapter.ViewHolder>{

    private List<MatchModel> items;
    private Context context;

    public HorizontalMatchesAdapter(List<MatchModel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.display_matches_layout, parent, false);
        view.findViewById(R.id.cv).setBackgroundColor(Color.TRANSPARENT);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView matchImage;
        TextView matchName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            matchImage = itemView.findViewById(R.id.matchView);
            matchName = itemView.findViewById(R.id.matchName);
        }
        public void setData(MatchModel data){
            if(data.getImage() != null)
                matchImage.setImageBitmap(data.getImage());
            matchName.setText(data.getName());
        }
    }

}
