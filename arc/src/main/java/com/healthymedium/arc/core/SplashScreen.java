package com.healthymedium.arc.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.healthymedium.arc.api.RestClient;
import com.healthymedium.arc.font.FontFactory;
import com.healthymedium.arc.font.Fonts;
import com.healthymedium.arc.hints.Hints;
import com.healthymedium.arc.library.R;
import com.healthymedium.arc.notifications.Proctor;
import com.healthymedium.arc.study.Study;
import com.healthymedium.arc.study.TestCycle;

@SuppressLint("ValidFragment")
public class SplashScreen extends BaseFragment {

    boolean paused = false;
    boolean ready = false;
    boolean skipSegment = false;
    protected boolean visible = true;

    public SplashScreen() {
    }

    public SplashScreen(boolean visible) {
        this.visible = visible;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(viewLayout(), container, false);
        if(!visible) {
            view.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    public @LayoutRes int viewLayout() {
        return R.layout.core_fragment_splash;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        initializeApp();
    }

    private void initializeApp() {
        Context context = getContext();
        getMainActivity().setupKeyboardWatcher();

        Application.getInstance().updateLocale(getContext());

        if(FontFactory.getInstance()==null) {
            FontFactory.initialize(context);
        }

        if(!Fonts.areLoaded()){
            Fonts.load();
            FontFactory.getInstance().setDefaultFont(Fonts.roboto);
            FontFactory.getInstance().setDefaultBoldFont(Fonts.robotoBold);
        }

        Hints.load();

        TestCycle cycle = Study.getCurrentTestCycle();
        if(cycle != null){
            if(cycle.getActualStartDate().isBeforeNow() && cycle.getActualEndDate().isAfterNow()){
                Proctor.startService(getContext());
            }
        }

        // We need to check to see if we're currently in the middle of a test session.
        // If we are, and if the state machine has valid fragments, we should let it continue
        // displaying those.
        // Otherwise, just run the Study instance, and let it figure out where it needs to be.

        if(Study.getParticipant().isCurrentlyInTestSession()
                && Study.getParticipant().checkForTestAbandonment() == false
                && Study.getStateMachine().hasValidFragments()
        ) {
            skipSegment = true;
        } else {
            skipSegment = false;
            Study.getInstance().run();
        }

        ready = true;

        getMainActivity().getWindow().setBackgroundDrawableResource(R.drawable.core_background);

        if(!paused){
            exit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(paused && ready) {
            if(Study.isValid()){
                exit();
            }
        }
        paused = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        paused = true;
    }

    public void exit(){
        if(getFragmentManager() != null) {
            getFragmentManager().popBackStack();
            if(skipSegment) {
                Study.getInstance().skipToNextSegment();
            } else {
                Study.getInstance().openNextFragment();
            }
        }
    }
}
