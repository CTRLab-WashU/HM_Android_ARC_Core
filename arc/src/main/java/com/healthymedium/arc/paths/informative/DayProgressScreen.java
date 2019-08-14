package com.healthymedium.arc.paths.informative;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthymedium.arc.core.BaseFragment;
import com.healthymedium.arc.misc.TransitionSet;
import com.healthymedium.arc.study.Participant;
import com.healthymedium.arc.study.ParticipantState;
import com.healthymedium.arc.ui.Button;
import com.healthymedium.arc.ui.CircleProgressView;
import com.healthymedium.arc.ui.base.RoundedFrameLayout;
import com.healthymedium.arc.library.R;
import com.healthymedium.arc.study.Study;
import com.healthymedium.arc.study.TestDay;
import com.healthymedium.arc.study.TestSession;
import com.healthymedium.arc.utilities.ViewUtil;

import org.joda.time.DateTime;
import java.util.List;

public class DayProgressScreen extends BaseFragment {

    int margin = ViewUtil.dpToPx(4);

    CircleProgressView latestView;
    int latestProgress = 0;

    ImageView confetti;

    public DayProgressScreen() {
        allowBackPress(false);
        setTransitionSet(TransitionSet.getFadingDefault());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_progress, container, false);

        // get inflated views ----------------------------------------------------------------------

        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        TextView textViewTestsComplete = view.findViewById(R.id.textViewTestsComplete);
        TextView textViewTestsLeft = view.findViewById(R.id.textViewTestsLeft);
        RoundedFrameLayout frameLayoutDone = view.findViewById(R.id.frameLayoutDone);
        Button button = view.findViewById(R.id.button);
        confetti = view.findViewById(R.id.imageViewConfetti);

        // display progress views ------------------------------------------------------------------
        Participant participant = Study.getParticipant();
        ParticipantState state = participant.getState();

        TestDay testDay = participant.getCurrentTestDay();


        int sessionIndex = state.currentTestSession-1;
        int dayIndex = state.currentTestDay;
        int cycleIndex = state.currentTestCycle;

        if(sessionIndex<0) {
            dayIndex--;
            if (dayIndex < 0) {
                cycleIndex--;
                dayIndex = state.testCycles.get(cycleIndex).getNumberOfTestDays() - 1;
            }
            testDay = state.testCycles.get(cycleIndex).getTestDay(dayIndex);
            sessionIndex = testDay.getNumberOfTests() - 1;
        }

        List<TestSession> sessions = testDay.getTestSessions();
        int sessionsFinished = testDay.getNumberOfTestsFinished();

        // add progress views
        for(int i=0;i<sessions.size();i++){
            CircleProgressView progressView = new CircleProgressView(getContext());
            linearLayout.addView(progressView);

            progressView.setBaseColor(R.color.primary);
            progressView.setCheckmarkColor(R.color.secondary);
            progressView.setSweepColor(R.color.secondaryAccent);
            progressView.setMargin(margin,0,margin,0);
            if(i!=sessionIndex) {
                progressView.setProgress(sessions.get(i).getProgress(), false);
            } else {
                latestView = progressView;
                latestProgress = sessions.get(i).getProgress();
            }
        }

        // display proper test ---------------------------------------------------------------------

        textViewTestsComplete.setText(sessionsFinished + (sessionsFinished==1?" Session Complete!":" Sessions Complete!"));

        if(testDay.getNumberOfTestsLeft()==0){
            textViewTestsLeft.setVisibility(View.GONE);
            frameLayoutDone.setVisibility(View.VISIBLE);
        } else {
            String before= "Only ";
            String highlight =  testDay.getNumberOfTestsLeft()+" more ";
            String after= "to go today.";
            String text = before+highlight+after;
            Spannable spannable = new SpannableString(text);
            spannable.setSpan(new ForegroundColorSpan(
                    ViewUtil.getColor(getContext(),R.color.hintDark)),
                    text.indexOf(highlight),
                    text.indexOf(highlight)+ highlight.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textViewTestsLeft.setText(spannable);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Study.openNextFragment();
            }
        });
        confetti.animate().translationYBy(-200);

        return view;
    }

    @Override
    protected void onEnterTransitionEnd(boolean popped) {
        super.onEnterTransitionEnd(popped);
        if(latestView!=null) {
            latestView.setProgress(latestProgress,true);
        }
        confetti.animate().translationYBy(200).setDuration(1000);
        confetti.animate().alpha(1.0f).setDuration(1000);
    }

}
