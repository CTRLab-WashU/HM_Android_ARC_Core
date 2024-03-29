package com.healthymedium.arc.paths.informative;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthymedium.arc.core.BaseFragment;
import com.healthymedium.arc.font.Fonts;
import com.healthymedium.arc.library.R;
import com.healthymedium.arc.misc.TransitionSet;
import com.healthymedium.arc.study.Study;
import com.healthymedium.arc.ui.Button;

public class FinishedCycleScreen extends BaseFragment {

    ImageView confetti;

    public FinishedCycleScreen() {
        allowBackPress(false);
        setTransitionSet(TransitionSet.getFadingDefault());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finished_cycle, container, false);

        // get inflated views ----------------------------------------------------------------------

        Button button = view.findViewById(R.id.button);
        confetti = view.findViewById(R.id.imageViewConfetti);

        TextView header = view.findViewById(R.id.textViewHeader);
        header.setTypeface(Fonts.robotoMedium);

        // display progress views ------------------------------------------------------------------

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
        confetti.animate().translationYBy(200).setDuration(1000);
        confetti.animate().alpha(1.0f).setDuration(1000);
    }

}
