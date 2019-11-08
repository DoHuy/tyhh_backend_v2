package com.stadio.model.documents;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "google_purchase")
@Data
public class Purchase {

    @JsonProperty(value = "mItemType")
    private String mItemType;  // ITEM_TYPE_INAPP or ITEM_TYPE_SUBS

    @JsonProperty(value = "mOrderId")
    private String mOrderId;

    @JsonProperty(value = "mPackageName")
    private String mPackageName;

    @JsonProperty(value = "mSku")
    private String mSku;

    @JsonProperty(value = "mPurchaseTime")
    private long mPurchaseTime;

    @JsonProperty(value = "mPurchaseState")
    private int mPurchaseState;

    @JsonProperty(value = "mDeveloperPayload")
    private String mDeveloperPayload;

    @JsonProperty(value = "mToken")
    private String mToken;

    @JsonProperty(value = "mOriginalJson")
    private String mOriginalJson;

    @JsonProperty(value = "mSignature")
    private String mSignature;

    @JsonProperty(value = "mIsAutoRenewing")
    private boolean mIsAutoRenewing;

    @JsonProperty(value = "userId")
    private String userId;
}
