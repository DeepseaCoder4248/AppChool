package com.hscompany.appchool;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class GetItemData {

    String itemId;
    Drawable itemImg;
    String itemApiUri;

    public GetItemData() {
    }

    public String getItemApiUri() {
        return itemApiUri;
    }
    public void setItemApiUri(String apiUri) {
        this.itemApiUri = apiUri;
    }

    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Drawable getItemImg() {
        return itemImg;
    }
    public void setItemImg(Drawable itemImg) {
        this.itemImg = itemImg;
    }
}



