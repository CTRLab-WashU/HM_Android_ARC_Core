package com.healthymedium.arc.core;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.healthymedium.arc.custom.Button;
import com.healthymedium.arc.library.R;
import com.healthymedium.arc.notifications.NotificationManager;
import com.healthymedium.arc.study.ParticipantState;
import com.healthymedium.arc.study.Study;
import com.healthymedium.arc.study.StudyState;
import com.healthymedium.arc.study.TestSession;
import com.healthymedium.arc.utilities.NavigationManager;

public class DebugDialog extends DialogFragment {

    Button button;
    TextView textViewSend;
    TextView textView;

    OnDialogDismiss listener;

    static public void launch(){
        DebugDialog dialog = new DebugDialog();
        dialog.show(NavigationManager.getInstance().getFragmentManager(),DebugDialog.class.getSimpleName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        setCancelable(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_debug, container, false);
        button = v.findViewById(R.id.buttonDebugDialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        textViewSend = v.findViewById(R.id.textViewSend);
        textViewSend.setPaintFlags(textViewSend.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, textView.getText());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Where to?"));
            }
        });

        StudyState studyState = Study.getStateMachine().getState();
        ParticipantState participantState = Study.getParticipant().getState();

        String status = "lifecycle: "+Study.getStateMachine().getLifecycleName(studyState.lifecycle).toLowerCase()+"\n";
        status += "path: "+Study.getStateMachine().getPathName(studyState.currentPath).toLowerCase()+"\n\n";

        status += "visit: "+participantState.currentVisit +"\n";
        status += "test: "+participantState.currentTestSession+"\n";
        status += "\nscheduled tests:\n\n";
        if(participantState.visits.size()>participantState.currentVisit) {
            Log.e("Test Count",String.valueOf(participantState.visits.get(participantState.currentVisit).getTestSessions().size()));
            for (TestSession session : participantState.visits.get(participantState.currentVisit).getTestSessions()) {
                status += session.getScheduledTime().toString("MM/dd/yyyy   hh:mm:ss a\n");
            }
        }
        
        if(!participantState.isStudyRunning){
            return v;
        }

        // Get current visit
        int currVisitId = participantState.visits.get(participantState.currentVisit).getId();

        // Notification one month before next visit
        NotificationManager.Node month = NotificationManager.getInstance().getNode(NotificationManager.VISIT_NEXT_MONTH, currVisitId);
        if (month != null) {
            status += "month notification: " + month.time + "\n";
        }

        // Notification one week before next visit
        NotificationManager.Node week = NotificationManager.getInstance().getNode(NotificationManager.VISIT_NEXT_WEEK, currVisitId);
        if (week != null) {
            status += "week notification: " + week.time + "\n";
        }

        // Notification one day before next visit
        NotificationManager.Node day = NotificationManager.getInstance().getNode(NotificationManager.VISIT_NEXT_DAY, currVisitId);
        if (day != null) {
            status += "day notification: " + day.time + "\n";
        }

        textView = v.findViewById(R.id.textviewState);
        textView.setText(status);

        return v;
    }

    public void setOnDialogDismissListener(OnDialogDismiss listener){
        this.listener = listener;
    }

    public interface OnDialogDismiss{
        void dismiss(String time);
    }

}
