package com.app.minitwitter.ui.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.minitwitter.R;
import com.app.minitwitter.common.Constants;
import com.app.minitwitter.common.SharedPreferencesManager;
import com.app.minitwitter.data.TwitterRepository;
import com.app.minitwitter.retrofit.response.UpdatedUser;
import com.app.minitwitter.ui.customElements.ProgressButton;
import com.app.minitwitter.viewmodel.TweetViewModel;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    TweetViewModel tweetViewModel;
    ImageView ivUserImage;
    EditText etUsername, etEmail, etPassword;
    ActivityResultLauncher<String> mGetContent;
    String IMAGE_MIME_TYPE = "image/*";
    Uri userImageURI;
    String defaultUsername, defaultEmail, defaultPassword, defaultImageUrl, userId;
    View progressButtonView;
    ProgressButton progressButton;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        userId = SharedPreferencesManager.getStringValue(Constants.PREF_USER_ID);
        defaultImageUrl = SharedPreferencesManager.getStringValue(Constants.PREF_PHOTOURL);
        defaultUsername = SharedPreferencesManager.getStringValue(Constants.PREF_USER_NAME);
        defaultEmail = SharedPreferencesManager.getStringValue(Constants.PREF_USER_EMAIL);
        defaultPassword = SharedPreferencesManager.getStringValue(Constants.PREF_USER_PASSWORD);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweetViewModel = new ViewModelProvider(getActivity(),
                ViewModelProvider.Factory.from(TweetViewModel.initializer))
                .get(TweetViewModel.class);

       mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if(uri != null){
                        Log.i("TAG", String.valueOf(uri));
                        userImageURI = uri;
                        Glide.with(getActivity())
                                .load(uri)
                                .into(ivUserImage);
                    }
                });
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        bindViews(view);

        setDefaultUserData();

        setViewListeners();

        return view;
    }

    private void bindViews(View view) {
        ivUserImage = view.findViewById(R.id.image_profile);
        etUsername = view.findViewById(R.id.et_username);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        progressButtonView = view.findViewById(R.id.custom_progress_button);
        progressButton = new ProgressButton(getActivity(), progressButtonView, "Save");
        progressButton.setThemeColor(R.color.colorAccent, R.color.white);
    }

    private void setViewListeners() {
        ivUserImage.setOnClickListener(view -> {
            pickImage();
        });

        progressButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError(getResources().getString(R.string.editText_username_error_message));
                } else if (etEmail.getText().toString().isEmpty()) {
                    etEmail.setError(getResources().getString(R.string.editText_email_error_message));
                } else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError(getResources().getString(R.string.editText_password_error_message));
                } else {
                    String newUsername = etUsername.getText().toString();
                    String newEmail = etEmail.getText().toString();
                    String newPassword = etPassword.getText().toString();

                    if (newUsername.equals(defaultUsername)) {
                        newUsername = defaultUsername;
                    }

                    if (newEmail.equals(defaultEmail)) {
                        newEmail = defaultEmail;
                    }

                    if (newPassword.equals(defaultPassword)) {
                        newPassword = defaultPassword;
                    }

                    saveUserData(newUsername, newEmail, newPassword);
                }
            }
        });
    }

    private void setDefaultUserData() {
        Glide.with(getActivity())
                        .load(defaultImageUrl)
                        .placeholder(R.drawable.unknown_person)
                        .into(ivUserImage);
        etUsername.setText(defaultUsername);
        etEmail.setText(defaultEmail);
        etPassword.setText(defaultPassword);
    }

    public InputStream openInputStreamFromUri(){
        ContentResolver contentResolver = getActivity().getContentResolver();

        InputStream inputStream = null;
        try {
            inputStream = contentResolver.openInputStream(userImageURI);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return inputStream;
    }

    private byte[] getByteArray(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while (true) {
            try {
                if (!((len = inputStream.read(buffer)) != -1)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byteArrayOutputStream.write(buffer, 0, len);
        }
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void pickImage() {
        mGetContent.launch(IMAGE_MIME_TYPE);
    }

    private void saveUserData(String username, String email, String password){
        MultipartBody.Part imagePart = null;

        if(userImageURI != null){
            InputStream inputStream = openInputStreamFromUri();

            byte[] bytes = getByteArray(inputStream);

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),bytes);
            imagePart = MultipartBody.Part.createFormData("image", userImageURI.getPath(), requestFile);
        }

        RequestBody requestUserId = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody requestUsername = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody requestEmail = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody requestPassword = RequestBody.create(MediaType.parse("text/plain"), password);

        progressButton.activateButton();
        TwitterRepository twitterRepository = new TwitterRepository();
        Call<UpdatedUser> response =  twitterRepository.updateUserData(
                requestUserId, imagePart, requestUsername, requestEmail, requestPassword);

        response.enqueue(new Callback<UpdatedUser>() {
            @Override
            public void onResponse(Call<UpdatedUser> call, Response<UpdatedUser> response) {
                if(response.isSuccessful()){
                    Log.i("TAG", String.valueOf(response.body().getPhotoUrl()));
                    Log.i("TAG", String.valueOf(response.body().getUsername()));
                    Log.i("TAG", String.valueOf(response.body().getEmail()));
                    Log.i("TAG", String.valueOf(response.body().getPassword()));
                    progressButton.onFinishedButtonAction();
                    if(response.body().getPhotoUrl() != null){
                        SharedPreferencesManager.setStringValue(Constants.PREF_PHOTOURL, response.body().getPhotoUrl());
                        updateActionBarImage();
                    }
                    SharedPreferencesManager.setStringValue(Constants.PREF_USER_NAME, response.body().getUsername());
                    SharedPreferencesManager.setStringValue(Constants.PREF_USER_EMAIL, response.body().getEmail());
                    SharedPreferencesManager.setStringValue(Constants.PREF_USER_PASSWORD, response.body().getPassword());
                }
            }

            @Override
            public void onFailure(Call<UpdatedUser> call, Throwable t) {
                progressButton.onFinishedButtonAction();
                Log.i("TAG", String.valueOf(t));
            }
        });
    }

    private void updateActionBarImage() {
        Window window = getActivity().getWindow();
        View v = window.getDecorView();
        CircleImageView circleImageView = v.findViewById(R.id.profile_image);
        circleImageView.setImageURI(userImageURI);
    }
}