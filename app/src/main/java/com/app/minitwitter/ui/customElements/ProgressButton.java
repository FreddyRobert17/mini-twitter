package com.app.minitwitter.ui.customElements;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.minitwitter.R;

public class ProgressButton {
    private Context context;
    private CardView cardView;
    private ConstraintLayout constraintLayout;
    private ProgressBar progressBar;
    private TextView tvButtonText;
    String buttonText;

    public ProgressButton(Context context, View view, String buttonText){
        this.context = context;
        cardView = (CardView) view;
        constraintLayout = view.findViewById(R.id.constraint_layout);
        progressBar =  view.findViewById(R.id.progress_bar);
        tvButtonText = view.findViewById(R.id.button_text);
        tvButtonText.setText(buttonText);
        this.buttonText = buttonText;
    }

    public void activateButton(){
        progressBar.setVisibility(View.VISIBLE);
        cardView.setEnabled(false);
        cardView.setAlpha(0.9f);
        tvButtonText.setText(R.string.please_wait_message);
    }

    public void onFinishedButtonAction(){
        progressBar.setVisibility(View.GONE);
        cardView.setEnabled(true);
        cardView.setAlpha(1.0f);
        tvButtonText.setText(buttonText);
    }

    public void setThemeColor(int backgroundColor, int textColor){
        cardView.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(backgroundColor)));
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(context.getResources().getColor(textColor)));
        tvButtonText.setTextColor(context.getResources().getColor(textColor));
    }
}
