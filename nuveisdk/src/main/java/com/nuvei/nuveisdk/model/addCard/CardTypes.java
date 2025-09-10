package com.nuvei.nuveisdk.model.addCard;

import java.util.Arrays;
import java.util.List;

public class CardTypes {
    public static final List<CardInfoModel> ALL = Arrays.asList(
            new CardInfoModel(
                    "Visa",
                    "^4",
                    "#### #### #### ####",
                    3,
                    Arrays.asList(13, 16, 19),
                    "vi",
                    "https://github.com/paymentez/paymentez-ios/blob/master/PaymentSDK/PaymentAssets.xcassets/stp_card_visa.imageset/stp_card_visa@3x.png?raw=true",
                    Arrays.asList("#1a1f71", "#3d5bf6")
            ),
            new CardInfoModel(
                    "Mastercard",
                    "^(5[1-5]|2[2-7])",
                    "#### #### #### ####",
                    3,
                    Arrays.asList(16),
                    "mc",
                    "https://github.com/paymentez/paymentez-ios/blob/master/PaymentSDK/PaymentAssets.xcassets/stp_card_mastercard.imageset/stp_card_mastercard@3x.png?raw=true",
                    Arrays.asList("#eb001b", "#f79e1b")
            ),
            new CardInfoModel(
                    "American Express",
                    "^3[47]",
                    "#### ###### #####",
                    4,
                    Arrays.asList(15),
                    "ax",
                    "https://github.com/paymentez/paymentez-ios/blob/master/PaymentSDK/PaymentAssets.xcassets/stp_card_amex.imageset/stp_card_amex@3x.png?raw=true",
                    Arrays.asList("#2e77bb", "#1e5799")
            ),
            new CardInfoModel(
                    "Diners",
                    "^3(0[0-5]|[68])",
                    "#### ###### ####",
                    3,
                    Arrays.asList(14),
                    "di",
                    "https://github.com/paymentez/paymentez-ios/blob/master/PaymentSDK/PaymentAssets.xcassets/stp_card_diners.imageset/stp_card_diners@3x.png?raw=true",
                    Arrays.asList("#006ba1", "#00b5e2")
            )
    );

}
