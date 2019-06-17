package com.healthymedium.arc.paths.setup;

import android.annotation.SuppressLint;

import com.healthymedium.arc.library.R;
import com.healthymedium.arc.path_data.SetupPathData;
import com.healthymedium.arc.paths.templates.SetupTemplate;
import com.healthymedium.arc.study.Study;
import com.healthymedium.arc.utilities.ViewUtil;

@SuppressLint("ValidFragment")
public class SetupParticipant extends SetupTemplate {

    public SetupParticipant(int firstDigitCount, int secondDigitCount) {
        super(false,firstDigitCount,secondDigitCount, ViewUtil.getString(R.string.login_enter_ARCID));
    }

    @Override
    protected void onNextRequested() {
        ((SetupPathData) Study.getCurrentSegmentData()).id = characterSequence.toString();
        super.onNextRequested();
    }

}
