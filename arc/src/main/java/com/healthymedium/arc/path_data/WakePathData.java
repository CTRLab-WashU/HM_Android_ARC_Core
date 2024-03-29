package com.healthymedium.arc.path_data;

import com.healthymedium.arc.api.tests.data.BaseData;
import com.healthymedium.arc.api.tests.data.WakeSurvey;
import com.healthymedium.arc.api.tests.data.WakeSurveySection;
import com.healthymedium.arc.study.PathSegmentData;

import java.util.ArrayList;
import java.util.Map;

public class WakePathData extends PathSegmentData {

    public WakePathData() {
        super();
    }

    @Override
    protected BaseData onProcess() {
        WakeSurvey survey = new WakeSurvey();
        survey.questions = new ArrayList<>();

        int size = objects.size();
        for (int i=0;i<size;i++) {
            Map<String, Object> response = (Map<String, Object>) objects.get(i);
            WakeSurveySection surveySection = processHashMap(response,WakeSurveySection.class);

            if(i==0){
                survey.start_date = surveySection.display_time;
            } else if(survey.start_date > surveySection.display_time){
                survey.start_date = surveySection.display_time;
            }

            surveySection.question_id = "wake_" + (i+1);
            survey.questions.add(surveySection);
        }

        return survey;
    }
}
