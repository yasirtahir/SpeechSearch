![Search nearby Restaurants using ASR ML Kit, Site Kit and Map Kit](https://github.com/yasirtahir/SpeechSearch/raw/main/images/image1.jpg "Search nearby Restaurants using ASR ML Kit, Site Kit and Map Kit")

  

  

  

## Introduction

  

In this project we integrated Huawei ASR ML Kit and Site Kit to search and show all the nearby restaurants on the map using Huawei Map Kit.

  
## Pre-Requisites

  

Before we jump into the development, we have to integrate HMS Core in our project. Please follow the HMS Core Integration link mentioned in the References section of this article to integrate HMS Core. After integrating HMS Core, make sure you enabled Site Kit, Map Kit and ML Kit in the AGConnect Console and added the _**agconnet-service.json**_ file in the app level directory.  


  ## Classes Description

1. The _**view_search.xml**_ file in the layout folder of the res, is the layout view of Custom Search bar which is used as Action Bar in the application, contains two views i-e searchCloseView and searchOpenView.  

2. The _**activity_speech_search.xml**_ file in the layout folder of the res, is the layout view of our Activity which contains Custom SearchView, MapView and Progress Bar.  
3. The **_SearchView.java_** class inside searchview package is extended from FrameLayout contains all the code related to Search. The class also implement Custom Listener for different actions callback.
4. The **_UtilClass.java_** class has two methods i-e _**getAllSite(String queryText, SearchService searchService, SearchViewListener listener)**_ and _**showDialog(Activity activity, DialogInterface.OnClickListener dialogPositive, CharSequence message)**_.
5. This _**getAllSite**_ method is responsible to bring all the sites (places) based on provided location.

6. The _**SpeechSearchActivity.java**_ class has all the code. We have initiated the MapView, other views and checking the required permissions inside onCreate of the activity.

  
  

When user click on the search icon, the search bar is opened. User have the option to either type or use Speech to Text (ASR ML Kit) service. Once clicking on the send button, the getAllSite method of the UtilClass is called which returns the list of nearby sites and then we add them on the map with Site Name and Site Address.

  

  

## Run the Application

  

Download this repo code. Build the project, run the application and test on any Huawei phone. In this demo, We used Huawei Mate30 for testing purposes.

Using ML Kit, developers can develop different android applications with ASR search option to improve the UI/UX. They can also enhance their user engagement and behavior. Combining ML Kit with other Huawei Kits can yield amazing results.

  

![Demo](https://github.com/yasirtahir/SpeechSearch/raw/main/images/demo.gif "Demo")

  

## Point to Ponder

  

1.  You can use default Animator class with circularReveal animation to enrich Material Design look and feel.
    
2.  Make sure to add all the permissions like _**WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, RECORD_AUDIO, ACCESS_NETWORK_STATE, ACCESS_WIFI_STATE**_.
    
3.  If you miss any of these permissions, you will get Service Unavailable error message with error code 11203.
    
4.  Make sure to add run-time permissions check. In this article, we used 3rd party Permission Check library with custom Dialog if user deny any of the required permission.
    

  

  

## References

  

### Huawei ML Kit Official Documentation:

[https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/ml-asr-0000001050066212](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/ml-asr-0000001050066212)

  

### Huawei Site Kit Official Documentation:

[https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides-V5/android-sdk-introduction-0000001050158571-V5](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides-V5/android-sdk-introduction-0000001050158571-V5)

  

### Huawei Map Kit Official Documentation:

[https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/android-sdk-introduction-0000001050158633](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/android-sdk-introduction-0000001050158633)

  

### ASR Error Constants:

[https://developer.huawei.com/consumer/en/doc/development/HMS-References/asrconstants](https://developer.huawei.com/consumer/en/doc/development/HMS-References/asrconstants)

  

### ASR Capture Constants:

[https://developer.huawei.com/consumer/en/doc/development/HMS-References/MLAsrCaptureConstants-4](https://developer.huawei.com/consumer/en/doc/development/HMS-References/MLAsrCaptureConstants-4)

  

### HMS Core Integration Link:

https://medium.com/huawei-developers/android-integrating-your-apps-with-huawei-hms-core-1f1e2a090e98

 
