package com.nuvei.nuveisdk.model.addCard;

import com.nuvei.nuveisdk.model.ErrorResponseModel;

public interface AddCardFormListener {
    void onSuccess(boolean success);
    void onError(ErrorResponseModel error);
    void onLoading(boolean isLoading);
}
