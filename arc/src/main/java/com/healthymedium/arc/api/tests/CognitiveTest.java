package com.healthymedium.arc.api.tests;

import com.healthymedium.arc.api.tests.data.ChronotypeSurvey;
import com.healthymedium.arc.api.tests.data.ContextSurvey;
import com.healthymedium.arc.api.tests.data.GridTest;
import com.healthymedium.arc.api.tests.data.PriceTest;
import com.healthymedium.arc.api.tests.data.SymbolTest;
import com.healthymedium.arc.api.tests.data.WakeSurvey;

public class CognitiveTest extends BaseTest {

    public ChronotypeSurvey chronotype_survey;
    public ContextSurvey context_survey;
    public WakeSurvey wake_survey;
    public SymbolTest symbol_test;
    public PriceTest price_test;
    public GridTest grid_test;

    public CognitiveTest(){
        type = "cognitive";
    }

    @Override
    public int getProgress(boolean testCompleted){
        if(testCompleted){
            return 100;
        }

        int progress = 0;
        int divisor = 4;

        if(wake_survey!=null){
            progress += wake_survey.getProgress();
            divisor++;
        }
        if(chronotype_survey!=null){
            progress += chronotype_survey.getProgress();
            divisor++;
        }
        if(context_survey!=null){
            progress += context_survey.getProgress();
        }
        if(grid_test!=null){
            progress += grid_test.getProgress();
        }
        if(price_test!=null){
            progress += price_test.getProgress();
        }
        if(symbol_test!=null){
            progress += symbol_test.getProgress();
        }
        progress /= divisor;

        return progress;
    }

}
