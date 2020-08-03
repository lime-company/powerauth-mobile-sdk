/*
 * Copyright 2020 Wultra s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.getlime.security.powerauth.integration.support.endpoints;

import io.getlime.security.powerauth.integration.support.model.ActivationOtpValidation;

public class InitActivationRequest {

    private String activationOtp;
    private ActivationOtpValidation activationOtpValidation;
    private long applicationId;
    private long maxFailureCount;
    private String userId;

    public String getActivationOtp() {
        return activationOtp;
    }

    public void setActivationOtp(String activationOtp) {
        this.activationOtp = activationOtp;
    }

    public ActivationOtpValidation getActivationOtpValidation() {
        return activationOtpValidation;
    }

    public void setActivationOtpValidation(ActivationOtpValidation activationOtpValidation) {
        this.activationOtpValidation = activationOtpValidation;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public long getMaxFailureCount() {
        return maxFailureCount;
    }

    public void setMaxFailureCount(long maxFailureCount) {
        this.maxFailureCount = maxFailureCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
