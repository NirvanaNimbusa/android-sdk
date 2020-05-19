package com.paypal.androidsdk.interfaces;

import androidx.annotation.Nullable;

public interface KanyeListener {
    void onResult(@Nullable Exception e, @Nullable Object response);
}
