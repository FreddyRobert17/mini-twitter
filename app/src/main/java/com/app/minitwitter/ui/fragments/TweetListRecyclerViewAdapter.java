package com.app.minitwitter.ui.fragments;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.minitwitter.R;
import com.app.minitwitter.common.Constants;
import com.app.minitwitter.common.SharedPreferencesManager;
import com.app.minitwitter.databinding.FragmentTweetListBinding;
import com.app.minitwitter.retrofit.response.Like;
import com.app.minitwitter.retrofit.response.Tweet;
import com.app.minitwitter.viewmodel.TweetViewModel;
import com.bumptech.glide.Glide;

import java.util.List;


public class TweetListRecyclerViewAdapter extends RecyclerView.Adapter<TweetListRecyclerViewAdapter.ViewHolder> {
    private List<Tweet> tweetList;
    private Context context;

    private TweetViewModel tweetViewModel;

    public TweetListRecyclerViewAdapter(Context context, List<Tweet> tweetList) {
        this.context = context;
        this.tweetList = tweetList;
        tweetViewModel = new ViewModelProvider((FragmentActivity)context,
                ViewModelProvider.Factory.from(TweetViewModel.initializer))
                .get(TweetViewModel.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentTweetListBinding view = FragmentTweetListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int currentPos) {
        int position = holder.getBindingAdapterPosition();

        if(tweetList != null){
            holder.tvAuthorName.setText(String.valueOf(tweetList.get(position).getUser().getUsername()));
            holder.tvMessage.setText(tweetList.get(position).getMessage());
            holder.tvLikeCount.setText(String.valueOf(tweetList.get(position).getLikes().size()));

            Glide.with(context)
                    .load(tweetList.get(position).getUser().getPhotoUrl())
                    .placeholder(R.drawable.unknown_person)
                    .into(holder.ivAvatar);

            holder.ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userId = SharedPreferencesManager.getStringValue(Constants.PREF_USER_ID);
                    String tweetId = tweetList.get(position).getId();
                    tweetViewModel.likeTweet(userId, tweetId);
                }
            });

            List<String> likes =  tweetList.get(position).getLikes();

            if(!likes.isEmpty()){
                for(String likeId : likes){
                    String userId = SharedPreferencesManager.getStringValue(Constants.PREF_USER_ID);
                    if(likeId.equalsIgnoreCase(userId)){
                        holder.ivLike.setImageDrawable(context.getDrawable(R.drawable.icon_filled_heart));
                        holder.tvLikeCount.setTextColor(Color.RED);
                        holder.tvLikeCount.setTypeface(null, Typeface.BOLD);
                    }
                }
            } else{
                holder.ivLike.setImageDrawable(context.getDrawable(R.drawable.icon_empty_heart));
                holder.tvLikeCount.setTypeface(null, Typeface.NORMAL);
                holder.tvLikeCount.setTextColor(Color.GRAY);
            }

        }
    }

    @Override
    public int getItemCount() {
        if(tweetList != null){
            return tweetList.size();
        }
        return 0;
    }

    public void setData(List<Tweet> tweets){
        this.tweetList = tweets;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView ivAvatar;
        public TextView tvAuthorName;
        public TextView tvMessage;
        public ImageView ivLike;
        public TextView tvLikeCount;

        public ViewHolder(FragmentTweetListBinding binding) {
            super(binding.getRoot());
            mView = binding.getRoot();
            ivAvatar = binding.imageProfile;
            tvAuthorName = binding.authorName;
            tvMessage = binding.message;
            ivLike = binding.like;
            tvLikeCount = binding.likeNumber;
        }
    }
}