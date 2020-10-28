package com.introverted.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.introverted.PostCardModel;
import com.introverted.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class PostCardAdapter extends RecyclerView.Adapter<PostCardAdapter.ViewHolder> {

    private List<PostCardModel> items;
    private Context context;

    public PostCardAdapter(List<PostCardModel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.display_posts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostCardAdapter.ViewHolder holder, int position) {
        holder.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<PostCardModel> getItems() {
        return items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         CircleImageView userImage;
         ImageView image;
         TextView titulo, publicacion, subject, likes, dislikes;
         TextView userTime, comments;
         ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.postImage);
            titulo = itemView.findViewById(R.id.postTitle);
            publicacion = itemView.findViewById(R.id.postText);
            subject = itemView.findViewById(R.id.subjectTxt);
            likes = itemView.findViewById(R.id.likeCounter);
            dislikes = itemView.findViewById(R.id.dislikeCounter);
            userTime = itemView.findViewById(R.id.usernameTime);
            comments = itemView.findViewById(R.id.commenteCounter);
            userImage = itemView.findViewById(R.id.userPic);
        }

        void setData(PostCardModel data) {
            if(!(data.getImage() == null))
                image.setImageBitmap(data.getImage());
            if(!(data.getUserImage() == null))
                userImage.setImageBitmap(data.getUserImage());
            titulo.setText(data.getTitulo());
            publicacion.setText(data.getPublicacion());
            subject.setText(data.getSubject());
            likes.setText(String.valueOf(data.getLikes()));
            dislikes.setText(String.valueOf(data.getDislikes()));
            userTime.setText("Publicado por " + data.getUsername() + " - " + data.getTime());
            comments.setText(String.valueOf(data.getComments()));

        }
    }
}
