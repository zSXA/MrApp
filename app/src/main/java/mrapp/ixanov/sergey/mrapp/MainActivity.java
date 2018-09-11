package mrapp.ixanov.sergey.mrapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.cleveroad.pulltorefresh.firework.FireworkyPullToRefreshLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import butterknife.BindView;
import butterknife.ButterKnife;
import mrapp.ixanov.sergey.mrapp.Fragments.Discount;
import mrapp.ixanov.sergey.mrapp.Fragments.Maps;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final long RIPPLE_DURATION = 250;
    private FireworkyPullToRefreshLayout mPullToRefreshLayout;
    BottomNavigationView navigation;
    Fragment selectedFragment = Discount.newInstance();
    android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //FragmentTransaction transaction = (FragmentTransaction) getFragmentManager().beginTransaction();
            android.support.v4.app.FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_discount:
                    selectedFragment = Discount.newInstance();
                    break;
                case R.id.navigation_maps:
                    selectedFragment = Maps.newInstance();
                    break;
            }
            transaction1.replace(R.id.fragments, selectedFragment);
            transaction1.commit();
            return true;
        }
    };

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.root)
    FrameLayout root;
    @BindView(R.id.content_hamburger)
    View contentHamburger;
    Discount fragDis;
    Maps fragMaps;
    final static String VariableKey = "NAME_VARIABLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        // [END subscribe_topics]

        // Log and toast
        String msg = getString(R.string.msg_subscribed);
        Log.d(TAG, msg);

        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        String msg1 = getString(R.string.msg_token_fmt, token);
        Log.d(TAG, msg1);

        fragMaps = new Maps();
        fragDis = new Discount();

        transaction.replace(R.id.fragments, selectedFragment);
        transaction.commit();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        /*Guillotine ===============================================================*/
        ButterKnife.bind(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }



        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);

        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();

    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;
        for (Fragment fragment: fm.getFragments()) {
            if (fragment instanceof  OnBackPressedListener) {
                backPressedListener = (OnBackPressedListener) fragment;
                break;
            }
        }

        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}
