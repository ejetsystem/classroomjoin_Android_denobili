/*
package com.denobili.app.helper_utils;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.denobili.app.R;
import com.denobili.app.studentallEventsPage.AllStudentEvent;

import rx.Subscriber;

public class ProductList_Fr_Level1 extends AppCompatActivity {

    private RecyclerView recyclerView;
    String productId;
    private LoadingDialog pDialog;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = PAGE_START;
    private int TOTAL_PAGES ;

    ProductLevel1Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        //MainBus.getInstance().busObservable.ofType(AllStudentEvent::class.java).subscribe(eventobserver)
         MainBus.getInstance().getBusObservable().ofType(AllStudentEvent.class).subscribe(eventobserver);
        recyclerView = (RecyclerView) findViewById(R.id.product_list_level);
        LinearLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        showProgressDialog();
        loadProductData("1",productId);
        adapter = new ProductLevel1Adapter(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        callNextPage("" + currentPage);
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }


    private  void callNextPage(String pageNo){

        loadProductData(pageNo,productId);

    }
    Subscriber<AllStudentEvent> eventobserver=new Subscriber<AllStudentEvent>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(AllStudentEvent event) {
            switch (event.getEvent())
            {
                case Event.RESULT:

                    break;
                case Event.NO_RESULT:

                    break;
                default:
                    break;
            }
        }
    };
*/
/*
    private val eventobserver = object : Observer<AllStudentEvent> {
        override fun onCompleted() {}

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }

        override fun onNext(event: AllStudentEvent) {
            if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
            if (progressdialog!!.isShowing) progressdialog!!.dismiss()
            when (event.event) {
                Event.RESULT -> {

                    adapter!!.addItems(true, event.results)
                    adapter!!.notifyDataSetChanged()
                    showResults()
                }
                Event.NO_RESULT ->
                showNoResults(getString(R.string.no_messages_parent))
                Event.NO_INTERNET -> {
                    if (adapter!!.itemCount == 0)
                    showNoInternet()
                }
                Event.SERVER_ERROR -> if (adapter!!.itemCount == 0) showServerError()
                Event.ERROR -> if (adapter!!.itemCount == 0) showError(event.error)
                else -> {
                }
            }
        }
    }
*//*

  */
/*  void loadProductData(final String pageNum,final String productId){

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, Common.BASE_URLL + "category_page.php",
                new com.android.volley.Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        hideProgressDialog();
                        try {

                            JSONObject obj = new JSONObject(response);

                            JSONArray productCategoryJSONArray = new JSONArray();

                            productCategoryJSONArray=obj.getJSONArray("category_list");

                            if(productCategoryJSONArray.length()>0) {
                                TOTAL_PAGES = Integer.parseInt(obj.getString("total_page"));

                                if(pageNum.equalsIgnoreCase("1")) {
                                    loadFirstPage(productCategoryJSONArray);
                                }else{
                                    loadNextPage(productCategoryJSONArray);
                                }
                            }else{
                                hideProgressDialog();
                                Toast.makeText(getActivity(),"There are no data found !",Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            hideProgressDialog();
                            Toast.makeText(getActivity(),"Something wrong on server side...",Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (error instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }

                        //Snackbar.make(findViewById(R.id.loginlayout),message,Snackbar.LENGTH_LONG).setAction("Action",null).show();
                        new Common().showAlert(getActivity(),message);
                        hideProgressDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ref_id", productId);
                params.put("pagen", pageNum);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }*//*



    private void loadFirstPage(JSONArray jsonArray) {

        try {
            List<ProductListModelHome> modellist=ProductListModelHome.getJsonDatafromAllCategories(jsonArray,adapter.getItemCount());

            hideProgressDialog();

            adapter.addAll(modellist);

            if (currentPage < TOTAL_PAGES) adapter.addLoadingFooter();
            else isLastPage = true;

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void loadNextPage(JSONArray jsonArray) {

        try {
            List<ProductListModelHome> modellist=ProductListModelHome.getJsonDatafromAllCategories(jsonArray,adapter.getItemCount());
            adapter.removeLoadingFooter();
            isLoading = false;
            adapter.addAll(modellist);

            if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
            else isLastPage = true;

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
*/
