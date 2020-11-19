package com.healthymedium.arc.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatRadioButton;

import com.healthymedium.arc.font.Fonts;
import com.healthymedium.arc.library.R;

public class RadioButton extends FrameLayout {

    String text;
    AppCompatRadioButton radioButton;
    FrameLayout frameLayoutRadioButton;
    CompoundButton.OnCheckedChangeListener listener;

    int paddingLeft;
    int paddingTop;
    int paddingRight;
    int paddingBottom;

    public RadioButton(Context context) {
        super(context);
        init(context);
    }

    public RadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View view = inflate(context,R.layout.custom_radio_button,this);
        radioButton = view.findViewById(R.id.radioButton);
        radioButton.setText(text);

        frameLayoutRadioButton = view.findViewById(R.id.frameLayoutRadioButton);
        paddingLeft = frameLayoutRadioButton.getPaddingLeft();
        paddingTop = frameLayoutRadioButton.getPaddingTop();
        paddingRight = frameLayoutRadioButton.getPaddingRight();
        paddingBottom = frameLayoutRadioButton.getPaddingBottom();

        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    frameLayoutRadioButton.setBackgroundResource(R.drawable.background_accent_rounded);
                    radioButton.setTypeface(Fonts.robotoBold);
                } else {
                    frameLayoutRadioButton.setBackground(null);
                    radioButton.setTypeface(Fonts.roboto);
                }
                frameLayoutRadioButton.setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom);
                compoundButton.setChecked(b);

                if(listener!=null) {
                    listener.onCheckedChanged(compoundButton, b);
                }
            }
        });
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener){
        this.listener = listener;
    }

    public void setText(String text){
        this.text = text;
        if(radioButton!=null){
            radioButton.setText(text);
        }
    }

    public void setChecked(final boolean checked){
        radioButton.post(new Runnable() {
            @Override
            public void run() {
                radioButton.setChecked(checked);
            }
        });

    }

    public void setCheckable(boolean checkable){
        radioButton.setClickable(checkable);
    }

    public boolean isChecked(){
        return radioButton.isChecked();
    }

}
