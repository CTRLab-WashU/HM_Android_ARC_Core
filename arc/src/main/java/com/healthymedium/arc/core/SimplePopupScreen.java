package com.healthymedium.arc.core;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthymedium.arc.study.Study;

@SuppressLint("ValidFragment")
public class SimplePopupScreen extends BaseFragment {

    boolean paused;
    boolean shouldSkipSegmentIfPaused;
    SimpleDialog dialog;

    public SimplePopupScreen(String bodyText, String buttonText, long delayTime, long maxTime, boolean shouldSkipSegmentIfPaused) {
        this.shouldSkipSegmentIfPaused = shouldSkipSegmentIfPaused;
        dialog = new SimpleDialog(bodyText,buttonText,delayTime,maxTime);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState);

        dialog.setOnDialogDismissListener(new SimpleDialog.OnDialogDismiss() {
            @Override
            public void dismiss() {
                if(!paused) {
                    Study.openNextFragment();
                }
            }
        });

        dialog.show(getFragmentManager(),TimedDialog.class.getSimpleName());
        return view;
    }

    public void skipSegmentIfPaused(){
        shouldSkipSegmentIfPaused = true;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(paused) {
            if(shouldSkipSegmentIfPaused){
                Study.skipToNextSegment();
            } else {
                Study.openNextFragment();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(dialog.isVisible()){
            dialog.setOnDialogDismissListener(null);
            dialog.dismiss();
        }
        paused = true;
    }

}
