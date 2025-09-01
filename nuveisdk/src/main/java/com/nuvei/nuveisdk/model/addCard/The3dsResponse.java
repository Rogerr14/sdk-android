package com.nuvei.nuveisdk.model.addCard;

public class The3dsResponse {
    private AuthenticationResponse authentication;
    private BrowserResponse browserResponse;
    private SdkResponse sdkResponse;

    public AuthenticationResponse getAuthentication() { return authentication; }
    public void setAuthentication(AuthenticationResponse value) { this.authentication = value; }

    public BrowserResponse getBrowserResponse() { return browserResponse; }
    public void setBrowserResponse(BrowserResponse value) { this.browserResponse = value; }

    public SdkResponse getSDKResponse() { return sdkResponse; }
    public void setSDKResponse(SdkResponse value) { this.sdkResponse = value; }
}
