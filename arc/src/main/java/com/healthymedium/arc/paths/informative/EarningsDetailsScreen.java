package com.healthymedium.arc.paths.informative;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthymedium.arc.api.models.EarningDetails;
import com.healthymedium.arc.core.Application;
import com.healthymedium.arc.core.BaseFragment;
import com.healthymedium.arc.library.R;
import com.healthymedium.arc.study.Earnings;
import com.healthymedium.arc.study.Study;
import com.healthymedium.arc.time.JodaUtil;
import com.healthymedium.arc.ui.Button;
import com.healthymedium.arc.ui.earnings.EarningsDetailedCycleView;
import com.healthymedium.arc.utilities.NavigationManager;
import com.healthymedium.arc.utilities.ViewUtil;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

public class EarningsDetailsScreen extends BaseFragment {

    Button viewFaqButton;
    TextView studyTotal;
    TextView lastSync;
    LinearLayout cycleLayout;

    public EarningsDetailsScreen() {
        allowBackPress(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_earnings_details, container, false);

        viewFaqButton = view.findViewById(R.id.viewFaqButton);
        viewFaqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FAQScreen faqScreen = new FAQScreen();
                NavigationManager.getInstance().open(faqScreen);
            }
        });

        Earnings earnings = Study.getParticipant().getEarnings();
        EarningDetails details = earnings.getDetails();

        if(details==null){
            return view;
        }

        studyTotal = view.findViewById(R.id.studyTotal);
        studyTotal.setText(details.total_earnings);

        String syncString = getString(R.string.earnings_sync) + " ";
        DateTime lastSyncTime = earnings.getDetailsRefreshTime();
        if(lastSyncTime != null) {
            if(lastSyncTime.plusMinutes(1).isAfterNow()) {
                String date = JodaUtil.format(lastSyncTime, R.string.format_date, Application.getInstance().getLocale());
                String time = JodaUtil.format(lastSyncTime, R.string.format_time, Application.getInstance().getLocale());
                String dateTime = getString(R.string.earnings_sync_datetime);

                dateTime = ViewUtil.replaceToken(dateTime,R.string.token_date,date);
                dateTime = ViewUtil.replaceToken(dateTime,R.string.token_time,time);

                syncString += dateTime;
            } else {
                syncString += getString(R.string.earnings_sync_justnow);
            }
        }
        lastSync = view.findViewById(R.id.textViewLastSync);
        lastSync.setText(syncString);

        cycleLayout = view.findViewById(R.id.cycleLayout);
        for(EarningDetails.Cycle cycle : details.cycles){
            EarningsDetailedCycleView cycleView = new EarningsDetailedCycleView(getContext(),cycle);
            cycleLayout.addView(cycleView);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int top = view.getPaddingTop();
        view.setPadding(0,top,0,0);
        getMainActivity().showNavigationBar();
    }
}
