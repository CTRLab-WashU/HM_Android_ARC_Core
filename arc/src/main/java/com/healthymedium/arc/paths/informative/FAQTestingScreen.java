package com.healthymedium.arc.paths.informative;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.healthymedium.arc.core.BaseFragment;
import com.healthymedium.arc.font.Fonts;
import com.healthymedium.arc.library.R;
import com.healthymedium.arc.misc.TransitionSet;
import com.healthymedium.arc.navigation.NavigationManager;
import com.healthymedium.arc.utilities.ViewUtil;

public class FAQTestingScreen extends BaseFragment {

    TextView textViewBack;
    TextView header;
    TextView textViewSubheader;

    RelativeLayout test_q1;
    RelativeLayout test_q2;
    RelativeLayout test_q3;
    RelativeLayout test_q4;
    RelativeLayout test_q5;
    RelativeLayout test_q6;
    RelativeLayout test_q7;
    RelativeLayout test_q8;
    RelativeLayout test_q9;
    RelativeLayout test_q10;

    public FAQTestingScreen() {
        allowBackPress(false);
        setTransitionSet(TransitionSet.getSlidingDefault());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq_testing, container, false);

        textViewBack = view.findViewById(R.id.textViewBack);
        textViewBack.setTypeface(Fonts.robotoMedium);
        textViewBack.setText(ViewUtil.getString(R.string.button_back));
        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationManager.getInstance().popBackStack();
            }
        });
        textViewBack.setVisibility(View.VISIBLE);

        header = view.findViewById(R.id.textViewHeader);
        header.setTypeface(Fonts.robotoMedium);

        textViewSubheader = view.findViewById(R.id.subheader);
        textViewSubheader.setText(Html.fromHtml(ViewUtil.getString(R.string.faq_subpage_subheader)));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setPadding(0, ViewUtil.getStatusBarHeight(),0,0);
    }

}
