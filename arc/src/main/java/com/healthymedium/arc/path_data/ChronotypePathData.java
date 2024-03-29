package com.healthymedium.arc.path_data;

import com.healthymedium.arc.api.tests.data.BaseData;
import com.healthymedium.arc.api.tests.data.ChronotypeSurvey;
import com.healthymedium.arc.api.tests.data.ChronotypeSurveySection;
import com.healthymedium.arc.study.PathSegmentData;

import java.util.ArrayList;
import java.util.Map;

public class ChronotypePathData extends PathSegmentData {

    public ChronotypePathData(){
        super();
    }

    @Override
    protected BaseData onProcess() {

        ChronotypeSurvey survey = new ChronotypeSurvey();
        survey.questions = new ArrayList<>();

        int size = objects.size();
        for (int i=0;i<size;i++) {
            Map<String, Object> response = (Map<String, Object>) objects.get(i);
            ChronotypeSurveySection surveySection = processHashMap(response,ChronotypeSurveySection.class);

            if(i==0){
                survey.start_date = surveySection.display_time;
            } else if(survey.start_date > surveySection.display_time){
                survey.start_date = surveySection.display_time;
            }

            surveySection.question_id = "chronotype_" + (i+1);
            survey.questions.add(surveySection);
        }

        return survey;

    }
}
