package com.introverted.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.introverted.Dashboard;
import com.introverted.PostCardModel;
import com.introverted.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        holder.itemView.findViewById(R.id.likePost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.liked) {
                    if(holder.disliked){
                        holder.disliked = false;
                        ((ImageButton)holder.itemView.findViewById(R.id.dislikePost)).setImageResource(R.drawable.ic_dislike_post);
                        ((ImageButton) holder.itemView.findViewById(R.id.likePost)).setImageResource(R.drawable.ic_liked_post);
                        holder.liked = true;
                        int me_gustas = Integer.parseInt(holder.likes.getText().toString())+1;
                        int no_me_gustas = Integer.parseInt(holder.dislikes.getText().toString())-1;
                        if(no_me_gustas < 0)
                            no_me_gustas = 0;
                        holder.likes.setText(""+me_gustas);
                        holder.dislikes.setText(""+no_me_gustas);
                        updateLikesDislikes(String.valueOf(me_gustas), String.valueOf(no_me_gustas),
                                String.valueOf(items.get(position).getPostId()));
                    }
                    else{
                        ((ImageButton) holder.itemView.findViewById(R.id.likePost)).setImageResource(R.drawable.ic_liked_post);
                        holder.liked = true;
                        int me_gustas = Integer.parseInt(holder.likes.getText().toString())+1;
                        holder.likes.setText(""+me_gustas);
                        updateLikesDislikes(String.valueOf(me_gustas), holder.dislikes.getText().toString(),
                                String.valueOf(items.get(position).getPostId()));
                    }
                }
                else{
                    ((ImageButton)holder.itemView.findViewById(R.id.likePost)).setImageResource(R.drawable.ic_like_post);
                    holder.liked = false;
                    int me_gustas = Integer.parseInt(holder.likes.getText().toString())-1;
                    if(me_gustas < 0)
                        me_gustas = 0;
                    holder.likes.setText(""+me_gustas);
                    updateLikesDislikes(String.valueOf(me_gustas), holder.dislikes.getText().toString(),
                            String.valueOf(items.get(position).getPostId()));
                }
            }
        });
        holder.itemView.findViewById(R.id.dislikePost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.disliked) {
                    if(holder.liked){
                        holder.liked = false;
                        ((ImageButton)holder.itemView.findViewById(R.id.likePost)).setImageResource(R.drawable.ic_like_post);
                        ((ImageButton) holder.itemView.findViewById(R.id.dislikePost)).setImageResource(R.drawable.ic_disliked_post);
                        holder.disliked = true;
                        int me_gustas = Integer.parseInt(holder.likes.getText().toString())-1;
                        if(me_gustas < 0)
                            me_gustas = 0;
                        int no_me_gustas = Integer.parseInt(holder.dislikes.getText().toString())+1;
                        holder.likes.setText(""+me_gustas);
                        holder.dislikes.setText(""+no_me_gustas);
                        updateLikesDislikes(String.valueOf(me_gustas), String.valueOf(no_me_gustas),
                                String.valueOf(items.get(position).getPostId()));
                    }
                    else{
                        ((ImageButton) holder.itemView.findViewById(R.id.dislikePost)).setImageResource(R.drawable.ic_disliked_post);
                        holder.disliked = true;
                        int no_me_gustas = Integer.parseInt(holder.dislikes.getText().toString())+1;
                        holder.dislikes.setText(""+no_me_gustas);
                        updateLikesDislikes(holder.likes.getText().toString(), String.valueOf(no_me_gustas),
                                String.valueOf(items.get(position).getPostId()));
                    }
                }
                else{
                    ((ImageButton)holder.itemView.findViewById(R.id.dislikePost)).setImageResource(R.drawable.ic_dislike_post);
                    holder.liked = false;
                    int no_me_gustas = Integer.parseInt(holder.dislikes.getText().toString())-1;
                    if(no_me_gustas < 0)
                        no_me_gustas = 0;
                    holder.dislikes.setText(""+no_me_gustas);
                    updateLikesDislikes(holder.likes.getText().toString(), String.valueOf(no_me_gustas),
                            String.valueOf(items.get(position).getPostId()));
                }
            }
        });
    }


    public void updateLikesDislikes(final String likes, final String dislikes, final String postId){
        String url = "http://"+context.getString(R.string.IP)+":80/WebService-Introverted/updateLikesDislikes.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("1")) {
                            Log.i("UPDATE_LIKEDISLIKES: ", "La operación fue un éxito");
                        }
                        else{
                            Log.i("UPDATE_LIKEDISLIKES: ", "No se pudo concretar la operación");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("post_id", postId);
                params.put("likes", likes);
                params.put("dislikes", dislikes);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
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
         boolean liked = false;
         boolean disliked = false;
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
