package com.nuvei.nuveisdk.model.addCard;

import com.google.gson.annotations.SerializedName;

public class BrowserResponse {
    @SerializedName("challenge_request")
    private String challengeRequest;
    @SerializedName("challenge_request")
    private String hiddenIframe;

    public String getChallengeRequest() { return challengeRequest; }
    public void setChallengeRequest(String value) { this.challengeRequest = value; }

    public String getHiddenIframe() { return hiddenIframe; }
    public void setHiddenIframe(String value) { this.hiddenIframe = value; }
}
