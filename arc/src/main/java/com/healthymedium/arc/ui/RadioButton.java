package com.healthymedium.arc.ui;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

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
        init(context, null);
    }

    public RadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        View view = inflate(context,R.layout.custom_radio_button,this);
        radioButton = view.findViewById(R.id.radioButton);
        radioButton.setText(text);
        radioButton.setTypeface(Fonts.robotoMedium);

        frameLayoutRadioButton = view.findViewById(R.id.frameLayoutRadioButton);
        paddingLeft = frameLayoutRadioButton.getPaddingLeft();
        paddingTop = frameLayoutRadioButton.getPaddingTop();
        paddingRight = frameLayoutRadioButton.getPaddingRight();
        paddingBottom = frameLayoutRadioButton.getPaddingBottom();

        // attributes
        TypedArray options = context.obtainStyledAttributes(attrs, R.styleable.RadioButton, 0, 0);
        boolean showButton = options.getBoolean(R.styleable.RadioButton_showButton, true);
        if(!showButton) {
            radioButton.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }
        int labelPosition = options.getInteger(R.styleable.RadioButton_labelPosition, 0);
        if(labelPosition == 2) {
            radioButton.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        }

        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    frameLayoutRadioButton.setBackgroundResource(R.drawable.background_accent_rounded);
                    radioButton.setTypeface(Fonts.robotoBlack);
                } else {
                    frameLayoutRadioButton.setBackgroundResource(R.drawable.btn_border_unselected);
                    radioButton.setTypeface(Fonts.robotoMedium);
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
    public String getText() {
        if(this.text != null){
            return this.text;
        }
        return null;
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

    public void showButton(boolean showButton) {
        radioButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.radiobutton),null,null,null);
        if(!showButton) {
            radioButton.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }
    }

    public void setLabelPosition(int position) {
        radioButton.setTextAlignment(position);
    }


}
