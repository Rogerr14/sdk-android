package com.nuvei.nuveisdk.model.deleteCard;

public class DeleteCardRequest {
   private UserDelete userDelete;
   private CardDelete cardDelete;

   public DeleteCardRequest(CardDelete cardDelete, UserDelete userDelete){
       this.cardDelete = cardDelete;
       this.userDelete = userDelete;
   }
}
