package com.classroomjoin.app.helper_utils;


import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.classroomjoin.app.R;


public abstract class BaseListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG ="BaseActivity" ;
    private BaseRecyclerviewAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView imageView;
    private TextView textView;
    private SwipeRefreshLayout swipeContainer;
    private int layoutid;
    private LinearLayoutManager linearLayoutManager;
    private ContentLoadingProgressBar contentLoadingProgressBar;


    public ContentLoadingProgressBar getContentLoadingProgressBar() {
        return contentLoadingProgressBar;
    }

    public void setContentLoadingProgressBar(ContentLoadingProgressBar contentLoadingProgressBar) {
        this.contentLoadingProgressBar = contentLoadingProgressBar;
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return linearLayoutManager;
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public int getLayoutid() {
        return layoutid;
    }

    public void setLayoutid(int layoutid) {
        this.layoutid = layoutid;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }


    private int noResultsTextId,noResultsImageId,serverErrorImgId,serverErrorTextId
            ,errorImgId,noInternetErrorTxtId,noInternetErrorImgId;

    public int getNoResultsImageId() {
        return noResultsImageId;
    }

    public void setNoResultsImageId(int noResultsImageId) {
        this.noResultsImageId = noResultsImageId;
    }

    public void initviews(View view){

        setRecyclerView((RecyclerView)view.findViewById(R.id.recycler_home));
        getRecyclerView().setLayoutManager(new LinearLayoutManager(getActivity()));
        setSwipeContainer((SwipeRefreshLayout)view.findViewById(R.id.include_content));
        getSwipeContainer().setOnRefreshListener(this);
        setImageView((ImageView)view.findViewById(R.id.base_home_image));
        setTextView((TextView)view.findViewById(R.id.base_home_textview));
        setContentLoadingProgressBar((ContentLoadingProgressBar)view.findViewById(R.id.contentLoading));

        noInternetErrorImgId=R.drawable.internetnotavailable;
        serverErrorImgId=R.drawable.ic_error_black_24dp;
        noResultsImageId=R.drawable.datanotfound3;
        errorImgId=R.drawable.ic_error_black_24dp;

        noInternetErrorTxtId=R.string.noInternet;
        noResultsTextId=R.string.noResults;
        serverErrorTextId=R.string.serverError;

    }

    public void refreshContent() {
    }

    public SwipeRefreshLayout getSwipeContainer() {
        return swipeContainer;
    }

    public void setSwipeContainer(SwipeRefreshLayout swipeContainer) {
        this.swipeContainer = swipeContainer;
    }

    public BaseRecyclerviewAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(BaseRecyclerviewAdapter adapter) {
        this.adapter = adapter;
    }

    public void showNoResults(){
        onError();
        textView.setText(noResultsTextId);
        imageView.setImageResource(noResultsImageId);
    }

    public void showNoResults(String errorMsg){
        onError();
        textView.setText(errorMsg);
        imageView.setImageResource(noResultsImageId);
    }
    public void showServerError(){
        onError();
        textView.setText(serverErrorTextId);
        imageView.setImageResource(serverErrorImgId);
    }
    public void showError(String errorMsg){
        onError();
        textView.setText(errorMsg);
        imageView.setImageResource(errorImgId);
    }
    public void showNoInternet(){
        onError();
        textView.setText(noInternetErrorTxtId);
        imageView.setImageResource(noInternetErrorImgId);


    }
    private void onError(){
        getContentLoadingProgressBar().hide();
        recyclerView.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
    }
    public void showResults(){
        getContentLoadingProgressBar().hide();
        recyclerView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
    }


    @Override
    public void onRefresh() {

    }
}
