package com.healthymedium.arc.paths.questions;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthymedium.arc.custom.Signature;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.healthymedium.arc.library.R;
import com.healthymedium.arc.paths.templates.QuestionTemplate;
import com.healthymedium.arc.study.Study;
import com.healthymedium.arc.utilities.ViewUtil;

@SuppressLint("ValidFragment")
public class QuestionSignature extends QuestionTemplate {
    boolean allowHelp;
    Signature signature;

    public QuestionSignature(boolean allowBack, boolean allowHelp, String header, String subheader) {
        super(allowBack,header,subheader, ViewUtil.getString(R.string.button_next));
        this.allowHelp = allowHelp;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState);
        setHelpVisible(allowHelp);

        signature = new Signature(getContext());
        content.addView(signature);

        signature.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signature.mSignaturePad.clear();
            }
        });

        signature.mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
                enableNextButton();
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });

        return view;
    }

    public void enableNextButton() {
        if(!buttonNext.isEnabled()){
            buttonNext.setEnabled(true);
            onNextButtonEnabled(true);
        }
    }

    @Override
    protected void onNextRequested() {
        Bitmap bitmap = getSignature();
        Study.getRestClient().submitSignature(bitmap);
        Study.getInstance().openNextFragment();
    }

    public Bitmap getSignature(){
        Bitmap bitmap = signature.mSignaturePad.getSignatureBitmap();
        return bitmap;
    }

    @Override
    public Object onValueCollection(){
        return null;
    }
}