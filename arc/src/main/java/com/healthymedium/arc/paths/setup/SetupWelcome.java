package com.healthymedium.arc.paths.setup;

import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.healthymedium.arc.core.BaseFragment;
import com.healthymedium.arc.custom.Button;
import com.healthymedium.arc.font.Fonts;
import com.healthymedium.arc.library.R;
import com.healthymedium.arc.paths.informative.AboutScreen;
import com.healthymedium.arc.paths.informative.ContactScreen;
import com.healthymedium.arc.paths.informative.PrivacyScreen;
import com.healthymedium.arc.study.Study;
import com.healthymedium.arc.utilities.NavigationManager;
import com.healthymedium.arc.utilities.VersionUtil;
import com.healthymedium.arc.utilities.ViewUtil;

public class SetupWelcome extends BaseFragment {

    Button button;
    TextView textViewHeader;
    TextView textViewAboutApp;
    TextView textViewPrivacyPolicy;
    // TextView textViewAppName;
    TextView textViewVersion;

    public SetupWelcome() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_welcome, container, false);
        textViewHeader = view.findViewById(R.id.textViewHeader);

        // textViewAppName = view.findViewById(R.id.textViewAppName);
        // textViewAppName.setText(ViewUtil.getString(R.string.app_name)+" app");

        textViewAboutApp = view.findViewById(R.id.textViewAboutApp);
        textViewAboutApp.setTypeface(Fonts.robotoMedium);

        textViewAboutApp.setPaintFlags(textViewAboutApp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewAboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutScreen aboutScreen = new AboutScreen();
                NavigationManager.getInstance().open(aboutScreen);
            }
        });

        textViewPrivacyPolicy = view.findViewById(R.id.textViewPrivacyPolicy);
        textViewPrivacyPolicy.setTypeface(Fonts.robotoMedium);
        textViewPrivacyPolicy.setPaintFlags(textViewPrivacyPolicy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Study.getPrivacyPolicy().show(getContext());
            }
        });

        textViewVersion = view.findViewById(R.id.textViewVersion);
        String ver = "v"+VersionUtil.getAppVersionName();
        textViewVersion.setText(ver);

        button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Study.openNextFragment();
            }
        });

        return view;
    }

}
