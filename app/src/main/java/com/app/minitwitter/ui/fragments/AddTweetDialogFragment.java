package com.app.minitwitter.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.minitwitter.R;
import com.app.minitwitter.common.Constants;
import com.app.minitwitter.common.SharedPreferencesManager;
import com.app.minitwitter.viewmodel.TweetViewModel;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddTweetDialogFragment extends DialogFragment implements View.OnClickListener{

    ImageView ivClose;
    Button btnCreateTweet;
    EditText etMessage;
    CircleImageView profileImage;

    public AddTweetDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_MiniTwitter_NoActionBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_addtweet_dialog, container, false);
        ivClose = view.findViewById(R.id.ivClose);
        profileImage = view.findViewById(R.id.iv_profile_image);
        etMessage = view.findViewById(R.id.et_create_tweet);
        btnCreateTweet = view.findViewById(R.id.create_tweet_button);

        ivClose.setOnClickListener(this);
        btnCreateTweet.setOnClickListener(this);

        String photoUrl = SharedPreferencesManager.getStringValue(Constants.PREF_PHOTOURL);

        Glide.with(getActivity())
                .load(photoUrl)
                .placeholder(R.drawable.unknown_person)
                .into(profileImage);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String message = etMessage.getText().toString();

        if(id == R.id.create_tweet_button){
            if(message.isEmpty()){
                Toast.makeText(getActivity(), getString(R.string.empty_text_message), Toast.LENGTH_SHORT).show();
            } else {
                String userId = SharedPreferencesManager.getStringValue(Constants.PREF_USER_ID);

                TweetViewModel tweetViewModel = new ViewModelProvider(getActivity(),
                        ViewModelProvider.Factory.from(TweetViewModel.initializer))
                        .get(TweetViewModel.class);
                tweetViewModel.createTweet(userId, message);
               getDialog().dismiss();
            }
        } else if (id == R.id.ivClose){
            showDialogConfirm();
        }
    }

    private void showDialogConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.close_dialog_title))
                .setMessage(getString(R.string.close_dialog_message))
                .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> getDialog().dismiss())
                .setNegativeButton(android.R.string.no, (dialogInterface, i) -> {}).show();
    }
}