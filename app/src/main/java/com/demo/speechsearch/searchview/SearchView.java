package com.demo.speechsearch.searchview;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.speechsearch.R;

public class SearchView extends FrameLayout implements View.OnClickListener {

    private Button btnOpenSearch;
    private EditText editTextSearch;
    private RelativeLayout searchOpenedView;
    private SearchViewListener searchViewListener;


    public SearchView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        inflate(context, R.layout.view_search, this);
        editTextSearch = findViewById(R.id.editTextSearch);
        searchOpenedView = findViewById(R.id.searchOpenedView);

        Button btnSendSearch = findViewById(R.id.btnSendSearch);
        btnOpenSearch = findViewById(R.id.btnOpenSearch);
        Button btnCloseSearch = findViewById(R.id.btnCloseSearch);
        Button btnVoiceSearch = findViewById(R.id.btnVoiceSearch);

        btnSendSearch.setOnClickListener(this);
        btnOpenSearch.setOnClickListener(this);
        btnCloseSearch.setOnClickListener(this);
        btnVoiceSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSendSearch:
                if(searchViewListener != null && !editTextSearch.getText().toString().trim().isEmpty()){
                    searchViewListener.onSendClicked(editTextSearch.getText().toString());
                }
                break;
            case R.id.btnOpenSearch:
                openSearch();
                if(searchViewListener != null){
                    searchViewListener.onSearchClicked();
                }
                break;
            case R.id.btnCloseSearch:
                closeSearch();
                if(searchViewListener != null){
                    searchViewListener.onCloseClicked();
                }
                break;
            case R.id.btnVoiceSearch:
                if(searchViewListener != null){
                    searchViewListener.onVoiceClicked();
                }
                break;
        }
    }

    private void openSearch(){
        editTextSearch.setText("");
        searchOpenedView.setVisibility(View.VISIBLE);
        Animator viewAnimator = ViewAnimationUtils.createCircularReveal(
                searchOpenedView,
                (btnOpenSearch.getRight() + btnOpenSearch.getLeft()) / 2,
                (btnOpenSearch.getTop() + btnOpenSearch.getBottom()) / 2,
                0f, searchOpenedView.getWidth()
        );
        viewAnimator.setDuration(300);
        viewAnimator.start();
    }

    private void closeSearch() {
        Animator viewAnimator = ViewAnimationUtils.createCircularReveal(
                searchOpenedView,
                (btnOpenSearch.getRight() + btnOpenSearch.getLeft()) / 2,
                (btnOpenSearch.getTop() + btnOpenSearch.getBottom()) / 2,
                searchOpenedView.getWidth(), 0f
        );

        viewAnimator.setDuration(300);
        viewAnimator.start();

        viewAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                searchOpenedView.setVisibility(View.INVISIBLE);
                editTextSearch.setText("");
                viewAnimator.removeAllListeners();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void setSearchViewListener(SearchViewListener searchViewListener) {
        this.searchViewListener = searchViewListener;
    }

    public String getSearchText(){
        return editTextSearch.getText().toString().trim();
    }

    public void setSearchText(String text){
        if (text == null) {
            editTextSearch.setText("");
        } else {
            editTextSearch.setText(text);
        }
    }
}
