package com.demo.speechsearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.speechsearch.searchview.SearchView;
import com.demo.speechsearch.searchview.SearchViewListener;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.MapsInitializer;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.MarkerOptions;
import com.huawei.hms.mlplugin.asr.MLAsrCaptureActivity;
import com.huawei.hms.mlplugin.asr.MLAsrCaptureConstants;
import com.huawei.hms.mlsdk.common.MLApplication;
import com.huawei.hms.site.api.SearchService;
import com.huawei.hms.site.api.SearchServiceFactory;
import com.huawei.hms.site.api.model.Site;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SpeechSearchActivity extends AppCompatActivity implements SearchViewListener, OnMapReadyCallback {

    private static final int ML_ASR_CAPTURE_CODE = 2002;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private SearchView searchView;
    private ProgressBar progressView;
    private SearchService mSearchService;
    private MapView mMapView;// Huawei Map
    private HuaweiMap mHuaweiMap; // Huawei Map

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_search);

        initVariables(savedInstanceState); // Init All the required variables

        // Check required permission at runtime
        checkPermissions();
    }

    private void initVariables(Bundle savedInstanceState) {
        // Init required Views
        mMapView = findViewById(R.id.mapView);
        progressView = findViewById(R.id.progressView);
        searchView = findViewById(R.id.searchView);
        searchView.setSearchViewListener(this); // This will set the listener to this class

        AGConnectServicesConfig config = AGConnectServicesConfig.fromContext(this);
        MLApplication.getInstance().setApiKey(config.getString("client/api_key")); // Set API Key for ASR ML Kit

        try {
            mSearchService = SearchServiceFactory.create(this, URLEncoder.encode(config.getString("client/api_key"), "utf-8")); // Take instance of Search Service to be used later
        } catch (UnsupportedEncodingException e) {
            Log.e(SpeechSearchActivity.class.getSimpleName(), "encode apikey error");
        }


        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        MapsInitializer.setApiKey(config.getString("client/api_key"));
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

    private void startListeningAudio() {
        Intent intent = new Intent(this, MLAsrCaptureActivity.class)
                .putExtra(MLAsrCaptureConstants.LANGUAGE, "en-US") // Language set as English
                .putExtra(MLAsrCaptureConstants.FEATURE, MLAsrCaptureConstants.FEATURE_WORDFLUX);
        startActivityForResult(intent, ML_ASR_CAPTURE_CODE); // This Code is used in onActivityResult to get the response
        overridePendingTransition(R.anim.mlkit_asr_popup_slide_show, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ML_ASR_CAPTURE_CODE) {
            switch (resultCode) {
                case MLAsrCaptureConstants.ASR_SUCCESS:
                    if (data != null) {
                        String text = "";
                        Bundle bundle = data.getExtras();
                        if (bundle != null && bundle.containsKey(MLAsrCaptureConstants.ASR_RESULT)) {
                            text = bundle.getString(MLAsrCaptureConstants.ASR_RESULT);
                        }
                        if (text != null && !"".equals(text)) {
                            searchView.setSearchText(text); // This will set the text spoken by the person
                            Log.d(SpeechSearchActivity.class.getSimpleName(), text);
                        }
                    }
                    break;
                case MLAsrCaptureConstants.ASR_FAILURE:
                    if(data != null) {
                        Bundle bundle = data.getExtras();
                        if(bundle != null && bundle.containsKey(MLAsrCaptureConstants.ASR_ERROR_CODE)) {
                            int errorCode = bundle.getInt(MLAsrCaptureConstants.ASR_ERROR_CODE);
                            Log.d(SpeechSearchActivity.class.getSimpleName(), "ASR Error Code --> " + errorCode);
                        }
                        if(bundle != null && bundle.containsKey(MLAsrCaptureConstants.ASR_ERROR_MESSAGE)){
                            String errorMsg = bundle.getString(MLAsrCaptureConstants.ASR_ERROR_MESSAGE);
                            Log.d(SpeechSearchActivity.class.getSimpleName(), "ASR Error Messge --> " + errorMsg);
                        }
                        if(bundle != null && bundle.containsKey(MLAsrCaptureConstants.ASR_SUB_ERROR_CODE)) {
                            int subErrorCode = bundle.getInt(MLAsrCaptureConstants.ASR_SUB_ERROR_CODE);
                            Log.d(SpeechSearchActivity.class.getSimpleName(), "ASR Sub Error Code --> " + subErrorCode);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onVoiceClicked() {
        startListeningAudio();
    }

    @Override
    public void onSendClicked(String text) {
        if (text != null && !text.trim().isEmpty()) {
            showLoader();
            UtilClass.getAllSite(text, mSearchService, new SearchViewListener() {
                @Override
                public void onSearchResultComplete(ArrayList<Site> allNearBySites, Error error) {
                    if (error != null) {
                        Log.d(SpeechSearchActivity.class.getSimpleName(), "Error Message --> " + error.getMessage());
                        Toast.makeText(SpeechSearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    if (allNearBySites != null && allNearBySites.size() > 0) {
                        Log.d(SpeechSearchActivity.class.getSimpleName(), "Total Site --> " + allNearBySites.size());
                        addPlacesOnMap(allNearBySites);
                    }

                    hideLoader();
                }
            });
        }
    }

    private void addPlacesOnMap(ArrayList<Site> allNearBySites) {
        if (mHuaweiMap != null) {
            mHuaweiMap.clear();

            for (Site site : allNearBySites) {
                // Adding User location
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(site.getLocation().getLat(), site.getLocation().getLng())).title(site.getName()).snippet(site.getFormatAddress());
                mHuaweiMap.addMarker(markerOptions);
            }
        }
    }

    @Override
    public void onMapReady(HuaweiMap map) {
        // Get map instance in a callback method
        Log.d(SpeechSearchActivity.class.getSimpleName(), "onMapReady: ");
        mHuaweiMap = map;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    private void showLoader() {
        if (progressView != null) {
            progressView.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoader() {
        if (progressView != null) {
            progressView.setVisibility(View.GONE);
        }
    }

    private void checkPermissions() {
        String[] permissions = {android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
        Permissions.check(this, permissions, getResources().getString(R.string.permission_request), null, new PermissionHandler() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                UtilClass.showDialog(SpeechSearchActivity.this, (dialog, which) -> dialog.dismiss(), getResources().getString(R.string.app_permissions)).show();
            }
        });
    }


}
