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


public class TweetListRecyclerViewAdapter extends RecyclerView.Adapter<TweetListRecyclerViewAdapter.ViewHolder>  {
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
        String userId = SharedPreferencesManager.getStringValue(Constants.PREF_USER_ID);
        String photoUrl = SharedPreferencesManager.getStringValue(Constants.PREF_PHOTOURL);
        Tweet tweet = tweetList.get(position);

        if(tweetList != null){
            holder.tvAuthorName.setText(context.getString(R.string.formatted_username, tweet.getUser().getUsername()));
            holder.tvMessage.setText(tweet.getMessage());
            holder.tvLikeCount.setText(String.valueOf(tweet.getLikes().size()));

            Glide.with(context)
                    .load(tweet.getUser().getPhotoUrl())
                    .placeholder(R.drawable.unknown_person)
                    .into(holder.ivAvatar);

            holder.ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tweetId = tweet.getId();
                    tweetViewModel.likeTweet(userId, tweetId);
                }
            });

            if(userId.equalsIgnoreCase(tweet.getUser().getId())){
                Glide.with(context)
                        .load(photoUrl)
                        .placeholder(R.drawable.unknown_person)
                        .into(holder.ivAvatar);

                holder.ivDownArrow.setVisibility(View.VISIBLE);
                holder.ivDownArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tweetViewModel.openBottomTweetMenu(context, tweetList.get(position).getId());
                    }
                });
            } else {
                holder.ivDownArrow.setVisibility(View.GONE);
            }

            List<String> likes =  tweet.getLikes();

            holder.ivLike.setImageDrawable(context.getDrawable(R.drawable.icon_empty_heart));
            holder.tvLikeCount.setTypeface(null, Typeface.NORMAL);
            holder.tvLikeCount.setTextColor(Color.GRAY);

            for(String likeId : likes){
                String prefUserId = SharedPreferencesManager.getStringValue(Constants.PREF_USER_ID);
                if(likeId.equalsIgnoreCase(prefUserId)){
                    holder.ivLike.setImageDrawable(context.getDrawable(R.drawable.icon_filled_heart));
                    holder.tvLikeCount.setTextColor(Color.RED);
                    holder.tvLikeCount.setTypeface(null, Typeface.BOLD);
                }
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
        public ImageView ivDownArrow;
        public TextView tvMessage;
        public ImageView ivLike;
        public TextView tvLikeCount;

        public ViewHolder(FragmentTweetListBinding binding) {
            super(binding.getRoot());
            mView = binding.getRoot();
            ivAvatar = binding.imageProfile;
            ivDownArrow = binding.downArrow;
            tvAuthorName = binding.authorName;
            tvMessage = binding.message;
            ivLike = binding.like;
            tvLikeCount = binding.likeNumber;
        }
    }

    interface EmptyListener {
        public void isListEmpty(Boolean isEmpty);
    }
}