package com.demo.speechsearch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.ContextThemeWrapper;

import com.demo.speechsearch.searchview.SearchViewListener;
import com.huawei.hms.site.api.SearchResultListener;
import com.huawei.hms.site.api.SearchService;
import com.huawei.hms.site.api.model.Coordinate;
import com.huawei.hms.site.api.model.HwLocationType;
import com.huawei.hms.site.api.model.SearchStatus;
import com.huawei.hms.site.api.model.Site;
import com.huawei.hms.site.api.model.TextSearchRequest;
import com.huawei.hms.site.api.model.TextSearchResponse;

import java.util.ArrayList;

public class UtilClass {

    private static Coordinate riyadhCoordinates = new Coordinate(24.744151, 46.702933); // Fixed Coordinates for our example

    public static void getAllSite(String queryText, SearchService searchService, SearchViewListener listener) {
        TextSearchRequest textSearchRequest = new TextSearchRequest();
        textSearchRequest.setQuery(queryText);
        textSearchRequest.setLocation(riyadhCoordinates); // We have to integrate Location Kit here to get user current location. In this sample, we have provided fix coordinates
        textSearchRequest.setHwPoiType(HwLocationType.RESTAURANT); // In this Application, we are only search for nearby restaurants
        searchService.textSearch(textSearchRequest, new SearchResultListener<TextSearchResponse>() {
            @Override
            public void onSearchResult(TextSearchResponse textSearchResponse) {
                ArrayList<Site> siteList = new ArrayList<>();
                if (textSearchResponse == null || textSearchResponse.getTotalCount() <= 0 ||
                        textSearchResponse.getSites() == null || textSearchResponse.getSites().size() <= 0) {
                    if (listener != null) {
                        listener.onSearchResultComplete(siteList, new Error("No Results Found"));
                    }
                    return;
                }
                siteList.clear();
                siteList.addAll(textSearchResponse.getSites());
                if (listener != null) {
                    listener.onSearchResultComplete(siteList, null);
                }
            }

            @Override
            public void onSearchError(SearchStatus searchStatus) {
                Log.e(SpeechSearchActivity.class.getSimpleName(), "onSearchError is: " + searchStatus.getErrorCode());
                if (listener != null) {
                    listener.onSearchResultComplete(new ArrayList<>(), new Error("onSearchError is: " + searchStatus.getErrorCode()));
                }
            }
        });
    }

    public static Dialog showDialog(Activity activity, DialogInterface.OnClickListener dialogPositive, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AppTheme));
        builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage(message).setCancelable(true)
                .setPositiveButton(android.R.string.yes, dialogPositive);
        return builder.create();
    }

}
