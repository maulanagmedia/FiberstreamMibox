/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package co.id.gmedia.fiberstream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import co.id.gmedia.coremodul.ApiVolley;
import co.id.gmedia.coremodul.AppRequestCallback;
import co.id.gmedia.coremodul.CustomModel;
import co.id.gmedia.fiberstream.model.KontenStreaming;
import co.id.gmedia.fiberstream.utils.ServerURL;

public class MainFragment extends BrowseFragment {
    private static final String TAG = "MainFragment";

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;
    private static final int NUM_ROWS = 6;
    private static final int NUM_COLS = 15;

    private final Handler mHandler = new Handler();
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundUri;
    private BackgroundManager mBackgroundManager;
    private List<KontenStreaming> streaming = new ArrayList<>();
    private ArrayObjectAdapter rowsAdapter;
    private ListRowPresenter generalPresenter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        loadKontenStreaming();

        prepareBackgroundManager();

        setupUIElements();

        loadRows();

        setupEventListeners();

        initialAdapter();

        loadHeaderSlider();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mBackgroundTimer) {
            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
    }

    private void loadRows() {
//
//        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
//        CardPresenter cardPresenter = new CardPresenter();

//        int i;
//        for (i = 0; i < NUM_ROWS; i++) {
//            if (i != 0) {
//                Collections.shuffle(list);
//            }
//            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
//            for (int j = 0; j < NUM_COLS; j++) {
//                listRowAdapter.add(list.get(j % 5));
//            }
//            HeaderItem header = new HeaderItem(i, MovieList.MOVIE_CATEGORY[i]);
//            rowsAdapter.add(new ListRow(header, listRowAdapter));
//        }

//        HeaderItem gridHeader = new HeaderItem(i, "PREFERENCES");
//
//        GridItemPresenter mGridPresenter = new GridItemPresenter();
//        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
//        gridRowAdapter.add(getResources().getString(R.string.grid_view));
//        gridRowAdapter.add(getString(R.string.error_fragment));
//        gridRowAdapter.add(getResources().getString(R.string.personal_settings));
//        rowsAdapter.add(new ListRow(gridHeader, gridRowAdapter));
//
//        setAdapter(rowsAdapter);
    }

    private void initialAdapter() {

        generalPresenter = new ListRowPresenter();
        rowsAdapter = new ArrayObjectAdapter(generalPresenter);
    }

    private void loadHeaderSlider(){

        new ApiVolley(getActivity(), new JSONObject(), "GET", ServerURL.get_slider,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {

                        HeaderSlider headerSlider = new HeaderSlider();
                        try{
                            JSONArray jData = new JSONArray(response);

                            final ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(headerSlider);
                            for(int i = 0; i < jData.length(); i++){
                                JSONObject jo = jData.getJSONObject(i);
                                listRowAdapter.add(new CustomModel(jo.getString("id"), jo.getString("image"), jo.getString("url")));
                            }

                            if(jData.length() > 0){

                                HeaderItem header = new HeaderItem(0, "");
                                rowsAdapter.add(new ListRow(header, listRowAdapter));
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                        loadKategori();
                    }

                    @Override
                    public void onEmpty(String message) {
                        Log.d(TAG, "onEmpty: " +message);
                        loadKategori();
                    }

                    @Override
                    public void onFail(String message) {

                        Log.d(TAG, "onFail: "+ message);
                    }
                })
        );
    }

    private void loadKategori(){

        JSONObject obj = new JSONObject();
        new ApiVolley(getActivity(), obj, "GET", ServerURL.get_kategori,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                @Override
                public void onSuccess(String response, String message) {

                    final CardPresenter cardPresenter = new CardPresenter();
                    HeaderItem headerItem = null;
                    try{
                        JSONArray obj = new JSONArray(response);
                        Log.d(TAG,">>>>"+obj);
                        int i;
                        for(i = 0; i < obj.length(); i++){
                            final JSONObject d = obj.getJSONObject(i);
                            Log.d(TAG,d.getString("kategori"));
                            final ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);

                            JSONObject params = new JSONObject();
                            new ApiVolley(getActivity(), params, "POST", ServerURL.get_konten_streaming,
                                    new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                                        @Override
                                        public void onSuccess(String response, String message) {
                                            try {
                                                JSONArray arr = new JSONArray(response);
                                                for (int j =0; j<arr.length(); j++){
                                                    for (KontenStreaming string : streaming) {
                                                        if(string.getIdKategori().equals(d.getString("id"))){
                                                            listRowAdapter.add(string);
                                                        }
                                                    }
//                                                    listRowAdapter.add(streaming.get(j % 5));
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onEmpty(String message) {

                                        }

                                        @Override
                                        public void onFail(String message) {

                                        }
                                    })
                            );
                            headerItem = new HeaderItem(i, d.getString("kategori"));
                            rowsAdapter.add(new ListRow(headerItem, listRowAdapter));

                        }
                        setAdapter(rowsAdapter);
                    }
                    catch (JSONException e){
                    }
                }

                @Override
                public void onEmpty(String message) {
                }

                @Override
                public void onFail(String message) {
                }
            })
        );
    }

    private void loadKontenStreaming(){
        // TODO mengambil data konten streaming
        JSONObject params = new JSONObject();
//        params.put("id_kategori",d.getString("id"));
        new ApiVolley(getActivity(), params, "POST", ServerURL.get_konten_streaming,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int j =0; j<arr.length(); j++){
                                JSONObject o = arr.getJSONObject(j);
                                KontenStreaming k = new KontenStreaming(
                                        o.getString("id"),
                                        o.getString("id_kategori"),
                                        o.getString("nama"),
                                        o.getString("link"),
                                        o.getString("icon"),
                                        o.getString("package"),
                                        o.getString("url_playstore"),
                                        o.getString("url_web"),
                                        o.getString("flag")
                                );
                                streaming.add(k);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onEmpty(String message) {

                    }

                    @Override
                    public void onFail(String message) {

                    }
                })
        );
    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mDefaultBackground = ContextCompat.getDrawable(getActivity(), R.drawable.background);
        mBackgroundManager.setDrawable(getResources().getDrawable(R.drawable.background));
        mBackgroundManager.attach(getActivity().getWindow());

        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupUIElements() {
        // setBadgeDrawable(getActivity().getResources().getDrawable(
        // R.drawable.videos_by_google_banner));
        setTitle(getString(R.string.browse_title2)); // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.black_grey));

        // set search icon color
        // setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.search_opaque));
    }

    private void setupEventListeners() {
        /*setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Implement your own in-app search", Toast.LENGTH_LONG)
                        .show();
            }
        });*/

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private void updateBackground(String uri) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;
        Glide.with(getActivity())
                .load(uri)
                .centerCrop()
                .error(mDefaultBackground)
                .into(new SimpleTarget<GlideDrawable>(width, height) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable>
                                                        glideAnimation) {
                        mBackgroundManager.setDrawable(resource);
                    }
                });
        mBackgroundTimer.cancel();
    }

    private void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Movie) {
                Movie movie = (Movie) item;
                Log.d(TAG, "Item: " + item.toString());
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, movie);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME)
                        .toBundle();
                getActivity().startActivity(intent, bundle);
            } else if (item instanceof String) {
                if (((String) item).contains(getString(R.string.error_fragment))) {
                    Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {
            if (item instanceof Movie) {
                mBackgroundUri = ((Movie) item).getBackgroundImageUrl();
                startBackgroundTimer();
            }
        }
    }

    private class UpdateBackgroundTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateBackground(mBackgroundUri);
                }
            });
        }
    }

    // Tampil bagian samping
    private class GridItemPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }

    // Slider Header
    private class SliderHeader extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }


}
