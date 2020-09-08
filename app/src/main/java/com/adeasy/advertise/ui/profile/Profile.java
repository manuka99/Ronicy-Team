package com.adeasy.advertise.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.adeasy.advertise.R;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class Profile extends AppCompatActivity {

    UserDetails userDetails;
    EditProfile editProfile;
    ProfileSecurity profileSecurity;

    FrameLayout detailsFrame;
    List<MenuItem> menuItems;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_activity_profile);

        detailsFrame = findViewById(R.id.profileFrame);
        userDetails = new UserDetails();
        editProfile = new EditProfile();
        profileSecurity = new ProfileSecurity();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.com_facebook_blue));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        onCreateOptionsMenu(toolbar.getMenu());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        menuItems = new ArrayList<>();

        showPersonalDetails();
    }

    private void showPersonalDetails() {
        getSupportActionBar().setTitle("Personal details");
        menuItems = new ArrayList<>();
        menuItems.add(toolbar.getMenu().findItem(R.id.action_profile_update));
        invalidateOptionsMenu();
        getSupportFragmentManager().beginTransaction().replace(detailsFrame.getId(), userDetails).commit();
    }

    private void showEditDetails() {
        getSupportActionBar().setTitle("Update profile");
        menuItems = new ArrayList<>();
        menuItems.add(toolbar.getMenu().findItem(R.id.action_profile_edit));
        menuItems.add(toolbar.getMenu().findItem(R.id.action_profile_security));
        invalidateOptionsMenu();
        getSupportFragmentManager().beginTransaction().replace(detailsFrame.getId(), editProfile).commit();
    }

    private void showSecurityDetails(){
        getSupportActionBar().setTitle("Profile security");
        menuItems = new ArrayList<>();
        menuItems.add(toolbar.getMenu().findItem(R.id.action_profile_update));
        menuItems.add(toolbar.getMenu().findItem(R.id.action_profile_security));
        invalidateOptionsMenu();
        getSupportFragmentManager().beginTransaction().replace(detailsFrame.getId(), profileSecurity).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        if (menuItems != null && menuItems.size() > 0){
            for(MenuItem menuItem: menuItems){
                menu.findItem(menuItem.getItemId()).setVisible(false);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile_update) {
            return false;
        } else if (id == R.id.action_profile_edit) {
            showEditDetails();
            return true;
        } else if (id == R.id.action_profile_security) {
            showSecurityDetails();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment() instanceof EditProfile || getCurrentFragment() instanceof ProfileSecurity)
            showPersonalDetails();
        else
            super.onBackPressed();
    }

    private Fragment getCurrentFragment() {
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.profileFrame);
        return currentFragment;
    }

}