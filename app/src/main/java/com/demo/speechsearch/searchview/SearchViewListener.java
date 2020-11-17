package com.demo.speechsearch.searchview;

import com.huawei.hms.site.api.model.Site;

import java.util.ArrayList;

public interface SearchViewListener {

    default void onSearchClicked() {
    }

    default void onCloseClicked() {
    }

    default void onSendClicked(String text) {
    }

    default void onVoiceClicked() {
    }

    default void onSearchResultComplete(ArrayList<Site> allNearBySites, Error error) {
    }

}
