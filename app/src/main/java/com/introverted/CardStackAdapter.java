package com.introverted;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.introverted.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private List<cardModel> items;
    private Context context;

    public CardStackAdapter(List<cardModel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.elemento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(items.get(position));
        holder.link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in;
                in = new Intent(context, ProfileLink.class);
                in.putExtra("ID", items.get(position).getId());
                in.putExtra("NOMBRE", items.get(position).getName());
                in.putExtra("CITY", items.get(position).getCity());
                in.putExtra("DISTANCE", holder.distance.getText().toString());
                String[] splt = holder.name.getText().toString().split(", ");
                in.putExtra("AGE", splt[1]);
                BMHelper.getInstance_PL().setPpBM(((BitmapDrawable)holder.image.getDrawable()).getBitmap());
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<cardModel> getItems() {
        return items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        int pos;
        ImageView image;
        TextView name, age, city, distance, id;
        ImageButton link;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.pos = -1;
            image = itemView.findViewById(R.id.item_image);
            name = itemView.findViewById(R.id.item_name);
            distance = itemView.findViewById(R.id.item_distance);
            city = itemView.findViewById(R.id.item_city);
            link = itemView.findViewById(R.id.profile_link);
            id = itemView.findViewById(R.id.idUser);
        }


        void setData(cardModel data) {
            if(data.getImage() != null)
                image.setImageBitmap(data.getImage());
            name.setText(data.getName() + ", " + data.getAge());
            city.setText(data.getCity());
            distance.setText(data.getDistance());
            id.setText(String.valueOf(data.getId()));
        }
    }
}
