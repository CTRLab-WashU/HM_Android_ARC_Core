package com.healthymedium.arc.study;

import com.healthymedium.arc.study.CircadianClock;
import com.healthymedium.arc.study.Participant;
import com.healthymedium.arc.study.ParticipantState;

public class Participants {

    public static Participant getDefault(){
        ParticipantState state = new ParticipantState();
        state.circadianClock = CircadianClocks.getDefault();
        state.id = "123456";

        Participant participant = new Participant();
        participant.setState(state,false);
        return participant;
    }



}