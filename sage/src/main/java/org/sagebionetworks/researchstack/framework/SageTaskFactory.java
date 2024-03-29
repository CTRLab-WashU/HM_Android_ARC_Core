package org.sagebionetworks.researchstack.framework;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.sagebionetworks.researchstack.backbone.ResourceManager;
import org.sagebionetworks.researchstack.backbone.ResourcePathManager;
import org.sagebionetworks.researchstack.backbone.answerformat.AnswerFormat;
import org.sagebionetworks.researchstack.backbone.model.survey.QuestionSurveyItem;
import org.sagebionetworks.researchstack.backbone.model.survey.SurveyItem;
import org.sagebionetworks.researchstack.backbone.model.survey.factory.SurveyFactory;
import org.sagebionetworks.researchstack.backbone.model.taskitem.TaskItem;
import org.sagebionetworks.researchstack.backbone.model.taskitem.TaskItemAdapter;
import org.sagebionetworks.researchstack.backbone.task.Task;
import org.sagebionetworks.bridge.researchstack.onboarding.BridgeSurveyFactory;

public class SageTaskFactory extends BridgeSurveyFactory {

    private Gson gson;

    public SageTaskFactory() {
        super();
        gson = createGson();
    }

    public Task createTask(Context context, String resourceName) {
        ResourcePathManager.Resource resource = ResourceManager.getInstance().getResource(resourceName);
        String json = ResourceManager.getResourceAsString(context,
                ResourceManager.getInstance().generatePath(resource.getType(), resource.getName()));
        Gson gson = createGson(); // Do not store this gson as a member variable, it has a link to Context
        TaskItem taskItem = gson.fromJson(json, TaskItem.class);
        return super.createTask(context, taskItem);
    }

    public Gson getGson() {
        return gson;
    }

    @Override
    protected void setupCustomStepCreator() {
        setCustomStepCreator(new MpCustomStepCreator());
    }

    private Gson createGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(SurveyItem.class, new SageSurveyItemAdapter());
        builder.registerTypeAdapter(TaskItem.class, new TaskItemAdapter());
        return builder.create();
    }

    protected class MpCustomStepCreator extends BridgeCustomStepCreator {
        @Override
        public org.sagebionetworks.researchstack.backbone.step.Step createCustomStep(
                Context context, SurveyItem item, boolean isSubtaskStep, SurveyFactory factory) {
            return super.createCustomStep(context, item, isSubtaskStep, factory);
        }
    }


    @Override
    public AnswerFormat createCustomAnswerFormat(Context context, QuestionSurveyItem item) {
        return super.createCustomAnswerFormat(context, item);
    }
}
