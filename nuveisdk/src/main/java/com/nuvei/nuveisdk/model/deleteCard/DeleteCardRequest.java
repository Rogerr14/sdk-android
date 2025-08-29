package com.nuvei.nuveisdk.model.deleteCard;

import com.google.gson.annotations.SerializedName;

public class DeleteCardRequest {
    @SerializedName("user")
   private UserDelete userDelete;
    @SerializedName("card")
   private CardDelete cardDelete;

   public DeleteCardRequest(CardDelete cardDelete, UserDelete userDelete){
       this.cardDelete = cardDelete;
       this.userDelete = userDelete;
   }
}
