/*
 * BSD 3-Clause License
 *
 * Copyright 2021  Sage Bionetworks. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1.  Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2.  Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * 3.  Neither the name of the copyright holder(s) nor the names of any contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission. No license is granted to the trademarks of
 * the copyright holders even if such marks are included in this software.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sagebionetoworks.migration

import com.healthymedium.arc.study.Participant
import com.healthymedium.arc.study.ParticipantState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.sagebionetworks.research.sagearc.SageRestApi

@RunWith(JUnit4::class)
class HmToSageMigrationTests {

    @Test
    fun testUserNeedsToMigrate() {
        // User has not signed in yet, no migration needed
        assertFalse(SageRestApi.HmToSageMigration.userNeedsToMigrate(null, null))
        val participant = MockParticipant()
        assertFalse(SageRestApi.HmToSageMigration.userNeedsToMigrate(participant, null))
        participant.state = ParticipantState()
        assertFalse(SageRestApi.HmToSageMigration.userNeedsToMigrate(participant, null))

        // User needs to migrate when they have an Arc ID (participant ID)
        // But their external ID is null, or incorrect
        participant.state.id = "000000"
        assertTrue(SageRestApi.HmToSageMigration.userNeedsToMigrate(participant, null))
        assertTrue(SageRestApi.HmToSageMigration.userNeedsToMigrate(participant, "000001"))
        // User has already migrated
        assertFalse(SageRestApi.HmToSageMigration.userNeedsToMigrate(participant, "000000"))
    }
}

open class MockParticipant: Participant() {
    override fun save() {
        // no-op, as the base class calls preferences manager which will be null in unit tests
    }
}