package com.healthymedium.arc.paths.templates;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthymedium.arc.core.BaseFragment;
import com.healthymedium.arc.font.Fonts;
import com.healthymedium.arc.library.R;
import com.healthymedium.arc.paths.informative.ContactScreen;
import com.healthymedium.arc.utilities.BottomNavigationViewHelper;
import com.healthymedium.arc.utilities.NavigationManager;
import com.healthymedium.arc.utilities.ViewUtil;

@SuppressLint("ValidFragment")
public class LandingTemplate extends BaseFragment {

    String stringHeader;
    String stringSubheader;

    protected TextView textViewHeader;
    protected TextView textViewSubheader;
    protected LinearLayout content;
    protected FrameLayout frameLayoutContact;
    protected TextView textViewContact;
    protected BottomNavigationView bottomNavigationView;

    public LandingTemplate(String header, String subheader) {
        stringHeader = header;
        stringSubheader = subheader;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.template_landing, container, false);
        content = view.findViewById(R.id.linearLayoutContent);
        textViewHeader = view.findViewById(R.id.textViewHeader);
        textViewHeader.setText(Html.fromHtml(stringHeader));

        textViewSubheader = view.findViewById(R.id.textViewSubHeader);
        textViewSubheader.setText(Html.fromHtml(stringSubheader));

//        textViewContact = view.findViewById(R.id.textViewContact);
//        ViewUtil.underlineTextView(textViewContact);

        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

//        frameLayoutContact = view.findViewById(R.id.frameLayoutContact);
//        frameLayoutContact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ContactScreen contactScreen = new ContactScreen();
//                NavigationManager.getInstance().open(contactScreen);
//            }
//        });

        setupDebug(view,R.id.textViewHeader);

        return view;
    }

}
