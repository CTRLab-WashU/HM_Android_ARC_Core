package com.healthymedium.arc.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.healthymedium.arc.font.Fonts;
import com.healthymedium.arc.library.R;

public class CheckBox extends FrameLayout {

    String text;
    AppCompatCheckBox checkBox;
    FrameLayout frameLayoutCheckBox;
    CompoundButton.OnCheckedChangeListener listener;

    int paddingLeft;
    int paddingTop;
    int paddingRight;
    int paddingBottom;

    public CheckBox(Context context) {
        super(context);
        init(context);
    }

    public CheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View view = inflate(context,R.layout.custom_checkbox,this);
        checkBox = view.findViewById(R.id.checkBox);
        checkBox.setText(text);

        frameLayoutCheckBox = view.findViewById(R.id.frameLayoutCheckBox);
        paddingLeft = frameLayoutCheckBox.getPaddingLeft();
        paddingTop = frameLayoutCheckBox.getPaddingTop();
        paddingRight = frameLayoutCheckBox.getPaddingRight();
        paddingBottom = frameLayoutCheckBox.getPaddingBottom();


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    frameLayoutCheckBox.setBackgroundResource(R.drawable.background_accent_rounded);
                    checkBox.setTypeface(Fonts.robotoBold);
                } else {
                    frameLayoutCheckBox.setBackground(null);
                    checkBox.setTypeface(Fonts.roboto);
                }
                frameLayoutCheckBox.setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom);
                listener.onCheckedChanged(compoundButton, b);
                compoundButton.setChecked(b);
            }
        });
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener){
        this.listener = listener;
    }


    public void setText(String text){
        this.text = text;
        if(checkBox!=null){
            checkBox.setText(text);
        }
    }


    public void setChecked(boolean checked){
        if(checkBox!=null){
            checkBox.setChecked(checked);
        }
    }

    public boolean isChecked(){
        if(checkBox!=null){
            return checkBox.isChecked();
        }
        return false;
    }

}
