package com.healthymedium.arc.paths.informative;

import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.healthymedium.arc.core.Application;
import com.healthymedium.arc.core.BaseFragment;
import com.healthymedium.arc.font.Fonts;
import com.healthymedium.arc.library.R;
import com.healthymedium.arc.misc.TransitionSet;
import com.healthymedium.arc.navigation.NavigationManager;
import com.healthymedium.arc.utilities.ViewUtil;

public class AboutScreen extends BaseFragment {

    public static int aboutBody = R.string.about_body;

    TextView textViewBack;
    TextView textViewHeader;
    TextView textViewSubHeader;
    Button button3rdParty;

    public AboutScreen() {
        allowBackPress(false);
        setTransitionSet(TransitionSet.getSlidingDefault());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_app, container, false);

        textViewHeader = view.findViewById(R.id.textViewHeader);
        textViewHeader.setTypeface(Fonts.robotoMedium);

        textViewSubHeader = view.findViewById(R.id.textViewSubHeader);
        textViewSubHeader.setText(ViewUtil.getHtmlString(aboutBody));
        textViewSubHeader.setLineSpacing(ViewUtil.dpToPx(3),1.0f);

        textViewBack = view.findViewById(R.id.textViewBack);
        textViewBack.setText(ViewUtil.getHtmlString(R.string.button_back));
        textViewBack.setTypeface(Fonts.robotoMedium);
        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationManager.getInstance().popBackStack();
            }
        });

        textViewBack.setVisibility(View.VISIBLE);

        Resources res = Application.getInstance().getResources();
        String name = res.getString(R.string.app_name);

//        button3rdParty = view.findViewById(R.id.button3rdParty);
//
//        if (!name.equals("CRI-ARC") && !name.equals("CRI-ARC QA") && !name.equals("CRI-ARC DEV")) {
//            button3rdParty.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    ThirdPartyMaterialsScreen thirdPartyScreen = new ThirdPartyMaterialsScreen();
//                    NavigationManager.getInstance().open(thirdPartyScreen);
//                }
//            });
//        } else {
//            button3rdParty.setVisibility(View.GONE);
//        }

        return view;
    }

}
