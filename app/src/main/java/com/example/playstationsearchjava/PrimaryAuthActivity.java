package com.example.playstationsearchjava;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.playstationsearchjava.Database.Database;
import com.example.playstationsearchjava.Database.Models.Category;
import com.example.playstationsearchjava.Database.Models.FavoriteGame;
import com.example.playstationsearchjava.RCVAdapters.CategoriesCardsAdapter;
import com.example.playstationsearchjava.Utils.Api.Models.ResponseModel;
import com.example.playstationsearchjava.Utils.Api.ResponseStatusTypes;
import com.example.playstationsearchjava.Utils.Api.YoApi;
import com.example.playstationsearchjava.Utils.ToolsMethods;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PrimaryAuthActivity extends AppCompatActivity implements CategoriesCardsAdapter.ItemClickListener {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private ArrayList<String> FavoriteGenres = new ArrayList<>();
    private View mContentView;
    CategoriesCardsAdapter adapter;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private Integer Step = 1;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_primary_auth);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        Button dummy_button = findViewById(R.id.dummy_button);

        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
        mContentView.startAnimation(animFadeIn);

        dummy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect();
            }
        });

        mContentView.animate().setDuration(1500).alpha(
                1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mContentView.animate().setDuration(1500).alpha(
                        0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Thread thread = new Thread(loadCategoriesThread);
                        thread.start();
                        show();
                    }
                });
            }
        });

    }

    private void redirect()
    {
        Boolean validate = validateCategories();
        if (!validate) {
            Toast.makeText(this, "Отметьте хотя бы одну категорию...", Toast.LENGTH_SHORT).show();
            return;
        }

        Database db = new Database(this);

        for (int i = 0; i < FavoriteGenres.size(); i++) {
            Category category = new Category();
            category.setID(FavoriteGenres.get(i));
            db.Categories().add(category);
        }


        Intent Intent = new Intent(PrimaryAuthActivity.this, MenuActivity.class);
        startActivity(Intent);
    }


    @SuppressLint("HandlerLeak")
    final
    Handler handler = new Handler() {

        private final Integer DRAW_CATEGORIES = 1;

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            Integer action = bundle.getInt("action");

            if (action == DRAW_CATEGORIES) {
                ArrayList<String> categories = bundle.getStringArrayList("Genres");
                initCategories(categories);
                return;
            }
        }
    };

    Runnable loadCategoriesThread = new Runnable() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {

            YoApi api = new YoApi();
            Message handlerMessage = handler.obtainMessage();
            Bundle bundle = new Bundle();

            SharedPreferences settings = getSharedPreferences("Auth", 0);
            String Cookie = settings.getString("Cookie", null);

            ResponseModel response = api.Genres().get(Cookie);

            JSONArray genres;

            try {
                genres = response.getFullResponse().getJSONArray("Result");
            } catch (JSONException e) {
                genres = new JSONArray();
                e.printStackTrace();
            }

            ArrayList<String> GenresArray = new ArrayList<>();

            for (int i = 0; i < genres.length(); i++) {
                try {
                    GenresArray.add(genres.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            bundle.putStringArrayList("Genres", GenresArray);
            bundle.putInt("action", 1);
            handlerMessage.setData(bundle);
            handler.sendMessage(handlerMessage);
        }
    };


    private void initCategories(List<String> categories)
    {
        RecyclerView recyclerView = findViewById(R.id.categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoriesCardsAdapter(this, categories);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d("Position", String.valueOf(position));
        Log.d("IsInIndex", String.valueOf(FavoriteGenres.indexOf(adapter.getItem(position))));
        CardView card = view.findViewById(R.id.card);
        if (FavoriteGenres.indexOf(adapter.getItem(position)) == -1) {
            FavoriteGenres.add(adapter.getItem(position));
            card.setCardBackgroundColor(Color.parseColor("#ADFF2F"));
        } else {
            FavoriteGenres.remove(adapter.getItem(position));
            card.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    private Boolean validateCategories()
    {
        return !FavoriteGenres.isEmpty();
    }

    private ArrayList<View> getViewsByTag(ViewGroup root, String tag){
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }

        }
        return views;
    }
}
