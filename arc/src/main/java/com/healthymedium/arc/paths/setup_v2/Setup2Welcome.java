package com.healthymedium.arc.paths.setup_v2;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.healthymedium.arc.core.BaseFragment;
import com.healthymedium.arc.font.Fonts;
import com.healthymedium.arc.library.R;
import com.healthymedium.arc.navigation.NavigationManager;
import com.healthymedium.arc.paths.informative.AboutScreen;
import com.healthymedium.arc.study.Study;
import com.healthymedium.arc.ui.Button;
import com.healthymedium.arc.ui.SizeAwareTextView;
import com.healthymedium.arc.utilities.VersionUtil;
import com.healthymedium.arc.utilities.ViewUtil;

public class Setup2Welcome extends BaseFragment {

    Button button;
    AppCompatTextView textViewHeader;
    SizeAwareTextView textViewAboutApp;
    SizeAwareTextView textViewPrivacyPolicy;
    // TextView textViewAppName;
    TextView textViewVersion;

    public Setup2Welcome() {
        //Locale.update(Locale.getDefault(), getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_welcome, container, false);
        textViewHeader = view.findViewById(R.id.textViewHeader);
        textViewHeader.setText(Html.fromHtml(ViewUtil.getString(R.string.gen_welcome_key)));
        ViewUtil.autosizeTextView(textViewHeader,10,26);

        // textViewAppName = view.findViewById(R.id.textViewAppName);
        // textViewAppName.setText(ViewUtil.getString(R.string.app_name)+" app");

        textViewAboutApp = view.findViewById(R.id.textViewAboutApp);
        textViewAboutApp.setMaxLines(1);
        textViewAboutApp.setTypeface(Fonts.robotoBold);
        textViewAboutApp.setPaintFlags(textViewAboutApp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewAboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutScreen aboutScreen = new AboutScreen();
                NavigationManager.getInstance().open(aboutScreen);
            }
        });
        textViewAboutApp.setOnTextSizeChangedListener(new SizeAwareTextView.OnTextSizeChangedListener() {
            @Override
            public void onTextSizeChanged(SizeAwareTextView view, float px) {
                textViewAboutApp.setTextSize(TypedValue.COMPLEX_UNIT_PX,px);
            }
        });
        ViewUtil.autosizeTextView(textViewAboutApp,8,16);

        textViewPrivacyPolicy = view.findViewById(R.id.textViewPrivacyPolicy);
        textViewPrivacyPolicy.setMaxLines(1);
        textViewPrivacyPolicy.setTypeface(Fonts.robotoBold);
        textViewPrivacyPolicy.setPaintFlags(textViewPrivacyPolicy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Study.getPrivacyPolicy().show(getContext());
            }
        });
        textViewPrivacyPolicy.setOnTextSizeChangedListener(new SizeAwareTextView.OnTextSizeChangedListener() {
            @Override
            public void onTextSizeChanged(SizeAwareTextView view, float px) {
                textViewAboutApp.setTextSize(TypedValue.COMPLEX_UNIT_PX,px);
            }
        });
        ViewUtil.autosizeTextView(textViewPrivacyPolicy,8,16);


        textViewVersion = view.findViewById(R.id.textViewVersion);
        String ver = "v"+VersionUtil.getAppVersionName();
        textViewVersion.setText(ver);

        button = view.findViewById(R.id.button);
        button.setText(ViewUtil.getHtmlString(R.string.button_signin));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Study.openNextFragment();
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
