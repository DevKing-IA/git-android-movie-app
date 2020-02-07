package com.evilkingmediabeta.sports.ekmsportsembed.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.evilkingmediabeta.R;
import com.evilkingmediabeta.sports.ekmsportsembed.DetailsActivity;
import com.evilkingmediabeta.sports.ekmsportsembed.ItemMovieActivity;
import com.evilkingmediabeta.sports.ekmsportsembed.ItemSeriesActivity;
import com.evilkingmediabeta.sports.ekmsportsembed.ItemTVActivity;
import com.evilkingmediabeta.sports.ekmsportsembed.adapter.GenreHomeAdapter;
import com.evilkingmediabeta.sports.ekmsportsembed.adapter.HomePageAdapter;
import com.evilkingmediabeta.sports.ekmsportsembed.adapter.LiveTvHomeAdapter;
import com.evilkingmediabeta.sports.ekmsportsembed.model.CommonModels;
import com.evilkingmediabeta.sports.ekmsportsembed.model.GenreModel;
import com.evilkingmediabeta.sports.ekmsportsembed.util.ApiResources;
import com.evilkingmediabeta.sports.ekmsportsembed.util.NetworkInst;
import com.evilkingmediabeta.sports.ekmsportsembed.util.VolleySingleton;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    ViewPager viewPager;
    CirclePageIndicator indicator;

    private List<CommonModels> listSlider=new ArrayList<>();

    private Timer timer;

    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerViewMovie,recyclerViewTv,recyclerViewTvSeries,recyclerViewGenre;
    private HomePageAdapter adapterMovie,adapterSeries;
    private LiveTvHomeAdapter adapterTv;
    private List<CommonModels> listMovie =new ArrayList<>();
    private List<CommonModels> listTv =new ArrayList<>();
    private List<CommonModels> listSeries =new ArrayList<>();
    private ApiResources apiResources;
    private Button btnMoreMovie,btnMoreTv,btnMoreSeries;

    private int checkPass =0;

    private SliderAdapter sliderAdapter;

    private VolleySingleton singleton;
    private TextView tvNoItem;
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ScrollView scrollView;


    private List<GenreModel> listGenre = new ArrayList<>();

    private GenreHomeAdapter genreHomeAdapter;
    private View sliderLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiResources=new ApiResources();

        singleton=new VolleySingleton(getActivity());

        btnMoreSeries=view.findViewById(R.id.btn_more_series);
        btnMoreTv=view.findViewById(R.id.btn_more_tv);
        btnMoreMovie=view.findViewById(R.id.btn_more_movie);
        shimmerFrameLayout=view.findViewById(R.id.shimmer_view_container);
        viewPager=view.findViewById(R.id.viewPager);
        indicator=view.findViewById(R.id.indicator);
        tvNoItem=view.findViewById(R.id.tv_noitem);
        coordinatorLayout=view.findViewById(R.id.coordinator_lyt);
        swipeRefreshLayout=view.findViewById(R.id.swipe_layout);
        scrollView=view.findViewById(R.id.scrollView);
        sliderLayout=view.findViewById(R.id.slider_layout);



        sliderAdapter=new SliderAdapter(getActivity(),listSlider);
        viewPager.setAdapter(sliderAdapter);
        indicator.setViewPager(viewPager);

        //----init timer slider--------------------
        timer = new Timer();


        //----btn click-------------
        btnClick();


        //----featured tv recycler view-----------------
        recyclerViewTv = view.findViewById(R.id.recyclerViewTv);
        recyclerViewTv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerViewTv.setHasFixedSize(true);
        recyclerViewTv.setNestedScrollingEnabled(false);
        adapterTv = new LiveTvHomeAdapter(getContext(), listTv);
        recyclerViewTv.setAdapter(adapterTv);


        //----movie's recycler view-----------------
        recyclerViewMovie = view.findViewById(R.id.recyclerView);
        recyclerViewMovie.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerViewMovie.setHasFixedSize(true);
        recyclerViewMovie.setNestedScrollingEnabled(false);
        adapterMovie = new HomePageAdapter(getContext(), listMovie);
        recyclerViewMovie.setAdapter(adapterMovie);

        //----series's recycler view-----------------
        recyclerViewTvSeries = view.findViewById(R.id.recyclerViewTvSeries);
        recyclerViewTvSeries.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerViewTvSeries.setHasFixedSize(true);
        recyclerViewTvSeries.setNestedScrollingEnabled(false);
        adapterSeries = new HomePageAdapter(getContext(), listSeries);
        recyclerViewTvSeries.setAdapter(adapterSeries);

        //----genre's recycler view--------------------
        recyclerViewGenre=view.findViewById(R.id.recyclerView_by_genre);
        recyclerViewGenre.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewGenre.setHasFixedSize(true);
        recyclerViewGenre.setNestedScrollingEnabled(false);
        genreHomeAdapter = new GenreHomeAdapter(getContext(),listGenre);
        recyclerViewGenre.setAdapter(genreHomeAdapter);






        shimmerFrameLayout.startShimmer();


        if (new NetworkInst(getContext()).isNetworkAvailable()){

            getFeaturedTV();
            getSlider(apiResources.getSlider());
            getLatestSeries();
            getLatestMovie();
            getDataByGenre();


        }else {
            tvNoItem.setText(getString(R.string.no_internet));
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            coordinatorLayout.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                recyclerViewMovie.removeAllViews();
                recyclerViewTv.removeAllViews();
                recyclerViewTvSeries.removeAllViews();
                recyclerViewGenre.removeAllViews();

                listMovie.clear();
                listSeries.clear();
                listSlider.clear();
                listTv.clear();
                listGenre.clear();


                if (new NetworkInst(getContext()).isNetworkAvailable()){
                    getFeaturedTV();
                    getSlider(apiResources.getSlider());
                    getLatestSeries();
                    getLatestMovie();
                    getDataByGenre();
                }else {
                    tvNoItem.setText(getString(R.string.no_internet));
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    coordinatorLayout.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                }
            }
        });

    }

    private void btnClick(){

        btnMoreMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ItemMovieActivity.class);
                intent.putExtra("url",apiResources.getGet_movie());
                intent.putExtra("title","Movies");
                getActivity().startActivity(intent);
            }
        });
        btnMoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ItemTVActivity.class);
                intent.putExtra("url",apiResources.getGet_live_tv());
                intent.putExtra("title","Live TV");
                getActivity().startActivity(intent);
            }
        });
        btnMoreSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ItemSeriesActivity.class);
                intent.putExtra("url",apiResources.getTvSeries());
                intent.putExtra("title","TV Series");
                getActivity().startActivity(intent);
            }
        });

    }

    private void getDataByGenre(){

        JsonArrayRequest jsonArrayRequest =new JsonArrayRequest(Request.Method.GET, new ApiResources().getGenreMovieURL(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i =0;i<response.length();i++){

                    try {

                        JSONObject jsonObject=response.getJSONObject(i);

                        GenreModel models=new GenreModel();

                        models.setName(jsonObject.getString("name"));
                        models.setId(jsonObject.getString("genre_id"));

                        JSONArray jsonArray = jsonObject.getJSONArray("videos");
                        //listGenreMovie.clear();
                        List<CommonModels> listGenreMovie = new ArrayList<>();
                        for (int j = 0;j<jsonArray.length();j++){

                            JSONObject movieObject = jsonArray.getJSONObject(j);

                            CommonModels commonModels = new CommonModels();

                            commonModels.setId(movieObject.getString("videos_id"));
                            commonModels.setTitle(movieObject.getString("title"));
                            commonModels.setVideoType("movie");
                            commonModels.setImageUrl(movieObject.getString("poster_url"));

                            listGenreMovie.add(commonModels);

                        }


                        models.setList(listGenreMovie);

                        listGenre.add(models);
                        genreHomeAdapter.notifyDataSetChanged();
//                        Log.e("LIST 2 SIZE ::", String.valueOf(listGenreMovie.size()));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);


    }

    private void getSlider(String url){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    swipeRefreshLayout.setRefreshing(false);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    coordinatorLayout.setVisibility(View.GONE);


                    if (response.getString("slider_type").equals("disable")){
                        sliderLayout.setVisibility(View.GONE);
                    }

                    else if (response.getString("slider_type").equals("movie")){

                        JSONArray jsonArray=response.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            CommonModels models =new CommonModels();
                            models.setImageUrl(jsonObject.getString("poster_url"));
                            models.setTitle(jsonObject.getString("title"));
                            models.setVideoType("movie");
                            models.setId(jsonObject.getString("videos_id"));

                            listSlider.add(models);
                        }

                    }else {
                        JSONArray jsonArray=response.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            CommonModels models =new CommonModels();
                            models.setImageUrl(jsonObject.getString("image_link"));
                            models.setTitle(jsonObject.getString("title"));
                            models.setVideoType("image");
                            listSlider.add(models);

                        }
                    }

                    sliderAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                coordinatorLayout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);

            }
        });

        Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);


    }



    private void getLatestSeries(){

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, apiResources.getLatestTvSeries(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

                for (int i=0;i<response.length();i++){

                    try {
                        JSONObject jsonObject=response.getJSONObject(i);
                        CommonModels models =new CommonModels();
                        models.setImageUrl(jsonObject.getString("thumbnail_url"));
                        models.setTitle(jsonObject.getString("title"));
                        models.setVideoType("tvseries");
                        models.setId(jsonObject.getString("videos_id"));
                        listSeries.add(models);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapterSeries.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        singleton.addToRequestQueue(jsonArrayRequest);

    }

    private void getLatestMovie(){

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, apiResources.getLatest_movie(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                for (int i=0;i<response.length();i++){
                    try {
                        JSONObject jsonObject=response.getJSONObject(i);
                        CommonModels models =new CommonModels();
                        models.setImageUrl(jsonObject.getString("thumbnail_url"));
                        models.setTitle(jsonObject.getString("title"));
                        models.setVideoType("movie");
                        models.setId(jsonObject.getString("videos_id"));
                        listMovie.add(models);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapterMovie.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        singleton.addToRequestQueue(jsonArrayRequest);

    }

    private void getFeaturedTV(){

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, apiResources.getGet_featured_tv(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                for (int i=0;i<response.length();i++){
                    try {
                        JSONObject jsonObject=response.getJSONObject(i);
                        CommonModels models =new CommonModels();
                        models.setImageUrl(jsonObject.getString("poster_url"));
                        models.setTitle(jsonObject.getString("tv_name"));
                        models.setVideoType("tv");
                        models.setId(jsonObject.getString("live_tv_id"));
                        listTv.add(models);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapterTv.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        singleton.addToRequestQueue(jsonArrayRequest);

    }

    @Override
    public void onStart() {
        super.onStart();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        timer=new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 5000, 5000);
    }

    //----timer for auto slide------------------
    private class SliderTimer extends TimerTask {

        @Override
        public void run() {

            if (getActivity()!=null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() < listSlider.size() - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }

        }
    }

    //----adapter for slider-------------
    public class SliderAdapter extends PagerAdapter {

        private Context context;
        private List<CommonModels> list=new ArrayList<>();

        public SliderAdapter(Context context, List<CommonModels> list) {
            this.context = context;
            this.list=list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_slider, null);

            View lyt_parent = view.findViewById(R.id.lyt_parent);

            final CommonModels models=list.get(position);

            TextView textView = view.findViewById(R.id.textView);

            textView.setText(models.getTitle());

            ImageView imageView=view.findViewById(R.id.imageview);

            Picasso.get().load(models.getImageUrl()).into(imageView);


            ViewPager viewPager = (ViewPager) container;
            viewPager.addView(view, 0);

            lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (models.getVideoType().equals("movie")){

                        Intent intent=new Intent(getContext(), DetailsActivity.class);
                        intent.putExtra("vType",models.getVideoType());
                        intent.putExtra("id",models.getId());
                        startActivity(intent);

                    }else {

                    }
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ViewPager viewPager = (ViewPager) container;
            View view = (View) object;
            viewPager.removeView(view);
        }
    }


}
