package com.adeasy.advertise.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.HomeViewModel;
import com.adeasy.advertise.cloudMessaging.ServerManagement;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.ui.newPost.NewAd;
import com.adeasy.advertise.util.CommonConstants;
import com.adeasy.advertise.util.CustomDialogs;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.irfaan008.irbottomnavigation.SpaceItem;
import com.irfaan008.irbottomnavigation.SpaceNavigationView;
import com.irfaan008.irbottomnavigation.SpaceOnClickListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.adeasy.advertise.util.CommonFunctions.md5;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String SEARCH_KEY = "search_key";
    private static final String CATEGORY_SELECTED = "category_selected";
    private static final String LOCATION_SELECTED = "location_selected";

    private static final int SEARCH_BAR_RESULT = 133;
    private static final int CATEGORY_PICKER = 4662;
    private static final int LOCATION_PICKER = 6512;
    private static final int LOCATION_PICKER_SEARCH = 7896;

    SpaceNavigationView spaceNavigationView;

    Toolbar toolbar;
    Home home;
    Search search;
    Orders orders;
    Account account;
    int selectedMenueID = 0;
    String searchKey, location_selected;
    Category categorySelected;
    CustomDialogs customDialogs;

    HomeViewModel homeViewModel;

    //private BroadcastReceiver broadcastReceiver, broadcastReceiverCloudMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customDialogs = new CustomDialogs(this);

        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            new ServerManagement(ServerManagement.TYPE_PUBLIC, null).getFCMToken();
        else
            new ServerManagement(ServerManagement.TYPE_PUBLIC, FirebaseAuth.getInstance().getCurrentUser().getUid()).getFCMToken();

        //for fb auth
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.adeasy.advertise",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }


        //for cloud messaging
//        broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.i(TAG, "Messaging token: " + new SharedPreferencesManager(getApplicationContext()).getCloudMessagingToken());
//
//            }
//        };
//        registerReceiver(broadcastReceiver, new IntentFilter(MyFirebaseMessagingService.BROADCAST_MESSAGE_TOKEN));


        //Log.i(TAG, "Messaging token: " + new SharedPreferencesManager(getApplicationContext()).getCloudMessagingToken());


//        try {
//
//            broadcastReceiverCloudMessage = new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//                    RemoteMessage remoteMessage = intent.getParcelableExtra(CommonConstants.CLOUD_MESSAGING_DATA);
//                    Log.i(TAG, "Messaging data: " + remoteMessage.getData());
//
//                    //handleCloudMessaging(remoteMessage);
//                }
//            };
//            registerReceiver(broadcastReceiverCloudMessage, new IntentFilter(MyFirebaseMessagingService.BROADCAST_CLOUD_MESSAGE));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        try {
            if (getIntent().hasExtra(CommonConstants.CLOUD_MESSAGE_BODY))
                handleCloudDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //for google ad mob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.i(TAG, android_id);

        String deviceId = md5(android_id).toUpperCase();

        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList(deviceId)).build();
        MobileAds.setRequestConfiguration(configuration);

        try {
            searchKey = getIntent().getStringExtra(SEARCH_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spaceNavigationView = findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_home_24));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_search_24));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_shopping_cart_24_orders));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_person_24));
        spaceNavigationView.setSpaceBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        spaceNavigationView.setCentreButtonIcon(R.drawable.icon_plus_upsbrown_24);
        spaceNavigationView.setCentreButtonColor(ContextCompat.getColor(this, R.color.colorWarningDark));
        spaceNavigationView.setActiveSpaceItemColor(ContextCompat.getColor(this, R.color.mainUiColour));
        spaceNavigationView.setInActiveSpaceItemColor(ContextCompat.getColor(this, R.color.colorBlackText));
        spaceNavigationView.showIconOnly();
        spaceNavigationView.changeCurrentItem(0);
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                startActivity(new Intent(getApplicationContext(), NewAd.class));
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                initializeAllFragments();
                switch (itemIndex) {
                    case 0:
                        selectedMenueID = itemIndex;
                        handleHomeFragment();
                        break;

                    case 1:
                        selectedMenueID = itemIndex;
                        changeToolbarSearch();
                        Bundle bundle = new Bundle();
                        bundle.putString(LOCATION_SELECTED, location_selected);
                        search.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, search).commit();
                        break;

                    case 2:
                        selectedMenueID = itemIndex;
                        changeToolbarDefault();
                        getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, orders).commit();
                        break;

                    case 3:
                        selectedMenueID = itemIndex;
                        changeToolbarDefault();
                        getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, account).commit();
                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                //Toast.makeText(MainActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });

        home = new Home();
        search = new Search();
        orders = new Orders();
        account = new Account();

        handleHomeFragment();

        //bottomNavigationView = findViewById(R.id.navBottm);
        //bottomNavigationView.setOnNavigationItemSelectedListener(this);

        //bottomNavigationView.setSelectedItemId(R.id.navHome);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        homeViewModel.getCategoryMutableLiveData().observe(this, new Observer<Category>() {
            @Override
            public void onChanged(Category category) {
                categorySelected = category;
                setSelectedHomeInSpaceMenu();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        spaceNavigationView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeAllFragments();
        validateFragmentSelected();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //IntentFilter filter = new IntentFilter(MyFirebaseMessagingService.BROADCAST_CLOUD_MESSAGE);
        //LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverCloudMessage, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverCloudMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
//        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//
//        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
//
//        if (taskList.get(0).numActivities == 1 &&
//                taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
//            Log.i(TAG, "This is last activity in the stack");
//            showExitDialog();
//        } else

        if (searchKey != null) {
            searchKey = null;
            setSelectedHomeInSpaceMenu();
            //bottomNavigationView.setSelectedItemId(R.id.navHome);
        } else if (categorySelected != null) {
            searchKey = null;
            categorySelected = null;
            setSelectedHomeInSpaceMenu();
            //bottomNavigationView.setSelectedItemId(R.id.navHome);
        } else {
            showExitDialog();
        }
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)

                .setMessage("Are you sure you want to exit?")

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })

                .show();
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        Log.i(TAG, "seleected");
//        initializeAllFragments();
//        switch (menuItem.getItemId()) {
//            case R.id.navHome:
//                selectedMenueID = menuItem.getItemId();
//                handleHomeFragment();
//                return true;
//
//            case R.id.navSearch:
//                selectedMenueID = menuItem.getItemId();
//                changeToolbarSearch();
//                getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, search).commit();
//                return true;
//
//            case R.id.navAddPost:
//                changeToolbarDefault();
//                startActivity(new Intent(this, NewAd.class));
//                return true;
//
//            case R.id.navChat:
//                selectedMenueID = menuItem.getItemId();
//                changeToolbarDefault();
//                getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, orders).commit();
//                return true;
//
//            case R.id.navAccount:
//                selectedMenueID = menuItem.getItemId();
//                changeToolbarDefault();
//                getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, account).commit();
//                return true;
//        }
//
//        return false;
//    }

    public void changeToolbarHome() {
        toolbar.setVisibility(View.VISIBLE);
        //toolbar.removeAllViews();
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        View home_logo = getLayoutInflater().inflate(R.layout.toolbar_home, null);
        getSupportActionBar().setCustomView(home_logo);

        getSupportActionBar().setDisplayShowCustomEnabled(true); // enable overriding the default toolbar_home layout
        //getSupportActionBar().setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void changeToolbarDefault() {
        toolbar.setVisibility(View.VISIBLE);
        //toolbar.removeAllViews();
        //getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setDisplayShowCustomEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setSubtitle("");
    }

    public void changeToolbarSearch() {
        toolbar.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowCustomEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    //to fix force stop on loading fragments above
    private void initializeAllFragments() {
        home = new Home();
        search = new Search();
        orders = new Orders();
        account = new Account();
    }

    private void handleHomeFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_KEY, searchKey);
        bundle.putSerializable(CATEGORY_SELECTED, categorySelected);
        bundle.putString(LOCATION_SELECTED, location_selected);
        home.setArguments(bundle);
        if (searchKey != null || categorySelected != null) {
            changeToolbarDefault();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        } else {
            changeToolbarHome();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, home).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_BAR_RESULT && resultCode == RESULT_OK && data != null) {
            searchKey = data.getStringExtra(SEARCH_KEY);
            setSelectedHomeInSpaceMenu();
            //bottomNavigationView.setSelectedItemId(R.id.navHome);
        } else if (requestCode == CATEGORY_PICKER && resultCode == RESULT_OK && data != null) {
            categorySelected = (Category) data.getSerializableExtra(CATEGORY_SELECTED);
            setSelectedHomeInSpaceMenu();
            // bottomNavigationView.setSelectedItemId(R.id.navHome);
        } else if (requestCode == LOCATION_PICKER && resultCode == RESULT_OK && data != null) {
            location_selected = data.getStringExtra(LOCATION_SELECTED);
            System.out.println("main: " + location_selected);
            setSelectedHomeInSpaceMenu();
            // bottomNavigationView.setSelectedItemId(R.id.navHome);
        } else if (requestCode == LOCATION_PICKER_SEARCH && resultCode == RESULT_OK && data != null) {
            initializeAllFragments();
            location_selected = data.getStringExtra(LOCATION_SELECTED);
            System.out.println("search: " + location_selected);
            spaceNavigationView.changeCurrentItem(1);
            changeToolbarSearch();
            Bundle bundle = new Bundle();
            bundle.putString(LOCATION_SELECTED, location_selected);
            search.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.navContainer, search).commit();
            // bottomNavigationView.setSelectedItemId(R.id.navHome);
        }
    }

    private void setSelectedHomeInSpaceMenu() {
        home = new Home();
        handleHomeFragment();
        spaceNavigationView.changeCurrentItem(0);
    }

    //    @Override
//    public void onBackPressed() {
//        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
//        int seletedItemId = bottomNavigationView.getSelectedItemId();
//        if (R.id.home != seletedItemId) {
//            setHomeItem(MainActivity.this);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    public static void setHomeItem(Activity activity) {
//        BottomNavigationView bottomNavigationView = (BottomNavigationView)
//                activity.findViewById(R.id.navigation);
//        bottomNavigationView.setSelectedItemId(R.id.home);
//    }

    private Fragment getCurrentFragment() {
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.navContainer);
        return currentFragment;
    }

    private void validateFragmentSelected() {
        if (getCurrentFragment() instanceof Home)
            spaceNavigationView.changeCurrentItem(0);
        else if (getCurrentFragment() instanceof Search)
            spaceNavigationView.changeCurrentItem(1);
        else if (getCurrentFragment() instanceof Orders)
            spaceNavigationView.changeCurrentItem(2);
        else if (getCurrentFragment() instanceof Account)
            spaceNavigationView.changeCurrentItem(3);
    }

    private void handleCloudDialog() {
        String header = null, body = null, image = null;

        if (getIntent().hasExtra(CommonConstants.CLOUD_MESSAGE_HEADER))
            header = getIntent().getStringExtra(CommonConstants.CLOUD_MESSAGE_HEADER);

        if (getIntent().hasExtra(CommonConstants.CLOUD_MESSAGE_BODY))
            body = getIntent().getStringExtra(CommonConstants.CLOUD_MESSAGE_BODY);

        if (getIntent().hasExtra(CommonConstants.CLOUD_MESSAGE_IMAGE))
            image = getIntent().getStringExtra(CommonConstants.CLOUD_MESSAGE_IMAGE);

        if (body != null)
            customDialogs.showCloudDialog(header, body, image);

    }

}