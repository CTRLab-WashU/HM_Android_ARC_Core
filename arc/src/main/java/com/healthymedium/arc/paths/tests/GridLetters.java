package com.healthymedium.arc.paths.tests;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.healthymedium.arc.core.BaseFragment;
import com.healthymedium.arc.core.TimedDialog;
import com.healthymedium.arc.custom.Button;
import com.healthymedium.arc.font.Fonts;
import com.healthymedium.arc.library.R;
import com.healthymedium.arc.path_data.GridTestPathData;
import com.healthymedium.arc.path_data.GridTestPathData.Section;
import com.healthymedium.arc.study.Study;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GridLetters extends BaseFragment {

    boolean paused;
    GridLayout gridLayout;
    protected  GridTestPathData gridTest;
    protected GridTestPathData.Section section;
    protected int eCount = 0;
    protected int fCount = 0;

    protected TimedDialog dialog;
    Handler handler;
    protected Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(isVisible()){
                dialog = new TimedDialog(getString(R.string.grids_popup1),1000);
                dialog.setOnDialogDismissListener(new TimedDialog.OnDialogDismiss() {
                    @Override
                    public void dismiss() {
                        section.setECount(eCount);
                        section.setFCount(fCount);
                        gridTest.updateCurrentSection(section);
                        Study.setCurrentSegmentData(gridTest);
                        Study.openNextFragment();
                    }
                });
                dialog.show(getFragmentManager(),TimedDialog.class.getSimpleName());
            }
        }
    };

    public GridLetters() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid_letters, container, false);
        gridLayout = view.findViewById(R.id.gridLayout);

        View.OnTouchListener listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int action = event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        boolean isE = ((TextView)view).getText().toString().equals("E");
                        if(view.getTag() == null){
                            view.setTag(true);
                            if(isE){
                                eCount++;
                            } else {
                                fCount++;
                            }
                            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gridNormal));
                            return false;
                        }
                        if(view.getTag().equals(false)) {
                            view.setTag(true);
                            if(isE){
                                eCount++;
                            } else {
                                fCount++;
                            }
                            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gridNormal));
                        } else {
                            view.setTag(false);
                            if(isE){
                                eCount--;
                            } else {
                                fCount--;
                            }
                            view.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));

                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        };


        Typeface font = Fonts.georgia;
        int size = gridLayout.getChildCount();
        for(int i=0;i<size;i++){
            gridLayout.getChildAt(i).setOnTouchListener(listener);
            ((TextView)gridLayout.getChildAt(i)).setTypeface(font);
        }
        Random random = new Random(SystemClock.elapsedRealtime());
        List<Integer> items = new ArrayList<>();
        items.add(-1);
        items.add(60);
        int gap = 60;
        int lower = 0;
        int picked = 0;
        while(picked < 8){
            int var = random.nextInt(gap-1)+1;
            items.add(var+lower);
            Collections.sort(items);
            gap = 0;
            lower = 0;
            for(int i=0;i<items.size()-1;i++){
                int tempGap = items.get(i+1)-items.get(i);
                if(tempGap > gap){
                    gap = tempGap;
                    lower = items.get(i);
                }
            }
            picked++;
        }
        Collections.sort(items);
        items.remove(0);
        items.remove(items.size()-1);

        for(Integer var: items){
            getTextView(var / 6,var % 6).setText("F");
        }

        handler = new Handler();
        handler.postDelayed(runnable,8000);
        gridTest = (GridTestPathData) Study.getCurrentSegmentData();
        section = gridTest.getCurrentSection();
        return view;
    }

    private TextView getTextView(int row, int col){
        return (TextView)gridLayout.getChildAt((6*row)+col);
    }

    @Override
    public void onStart() {
        super.onStart();
        section.markDistractionDisplayed();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(paused) {
            Study.skipToNextSegment();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        if(dialog!=null){
            if(dialog.isVisible()){
                dialog.setOnDialogDismissListener(null);
                dialog.dismiss();
            }
        }
        paused = true;
    }
}
