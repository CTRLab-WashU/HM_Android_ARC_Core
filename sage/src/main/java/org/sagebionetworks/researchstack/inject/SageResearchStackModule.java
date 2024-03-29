package org.sagebionetworks.researchstack.inject;

import android.content.Context;

import org.sagebionetworks.researchstack.framework.SageTaskFactory;
import org.sagebionetworks.researchstack.framework.SageResearchStack;
import org.sagebionetworks.researchstack.framework.SageResearchStack.SageDataProvider;
import org.sagebionetworks.researchstack.backbone.ResearchStack;
import org.sagebionetworks.researchstack.backbone.StorageAccess;
import org.sagebionetworks.bridge.android.di.BridgeApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class SageResearchStackModule {

    public static final String PIN_CODE = "1234";

    @Provides
    @BridgeApplicationScope
    static SageTaskFactory provideMpTaskFactory() {
        return new SageTaskFactory();
    }

    @Provides
    @BridgeApplicationScope
    static SageDataProvider provideMpDataProvider() {
        return SageDataProvider.getInstance();
    }

    @Provides
    @BridgeApplicationScope
    static ResearchStack provideResearchStack(Context context) {
        SageResearchStack researchStack = new SageResearchStack(context);
        ResearchStack.init(context, researchStack);
        mockAuthenticate(context);
        return researchStack;
    }

    /**
     * Call to mock authenticate to remove pin code auth screen for ResearchStack based activities
     *
     * @param context
     *         can be app or activity
     */
    public static void mockAuthenticate(Context context) {
        // We don't use a pin code for MPower, so just plug one in for the app to always use
        if (StorageAccess.getInstance().hasPinCode(context)) {
            StorageAccess.getInstance().authenticate(context, PIN_CODE);
        } else {
            StorageAccess.getInstance().createPinCode(context, PIN_CODE);
        }
    }
}
