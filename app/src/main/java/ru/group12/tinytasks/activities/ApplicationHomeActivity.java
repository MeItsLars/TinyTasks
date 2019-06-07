package ru.group12.tinytasks.activities;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ru.group12.tinytasks.R;
import ru.group12.tinytasks.activities.fragments.CreateTaskFragment;
import ru.group12.tinytasks.activities.fragments.HomeFragment;
import ru.group12.tinytasks.activities.fragments.NotificationsFragment;
import ru.group12.tinytasks.activities.fragments.ProfileFragment;
import ru.group12.tinytasks.activities.fragments.SearchTaskFragment;
import ru.group12.tinytasks.util.database.Database;
import ru.group12.tinytasks.util.internet.Network;

public class ApplicationHomeActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicationhome);
        Network.registerInternetStateChangedListener(this);

        initializeNavigationBar();
        loadUser();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        String fragment = getIntent().getStringExtra("fragment");
        if(fragment != null) {
            if(fragment.equals("Home")) {setActiveFragment(homeFragment); setClickedElement(R.id.navigation_home_button);}
            else if(fragment.equals("SearchTask")) {setActiveFragment(searchTaskFragment); setClickedElement(R.id.navigation_search_button);}
            else if(fragment.equals("CreateTask")) {setActiveFragment(createTaskFragment); setClickedElement(R.id.navigation_create_task_button);}
            else if(fragment.equals("Notifications")) {setActiveFragment(notificationsFragment); setClickedElement(R.id.navigation_notifications_button);}
            else if(fragment.equals("Profile")) {setActiveFragment(profileFragment); setClickedElement(R.id.navigation_profile_button);}
        }
    }

    private void loadUser() {
        Database.loadCurrentUser((HomeFragment) homeFragment);
    }

    final Fragment homeFragment = new HomeFragment();
    final Fragment searchTaskFragment = new SearchTaskFragment();
    final Fragment createTaskFragment = new CreateTaskFragment();
    final Fragment notificationsFragment = new NotificationsFragment();
    final Fragment profileFragment = new ProfileFragment();

    final FragmentManager fm = getSupportFragmentManager();

    Fragment activeFragment = homeFragment;

    BottomNavigationView navigationBar;

    private void initializeNavigationBar() {
        fm.beginTransaction().add(R.id.main_container, homeFragment, "1").commit();
        fm.beginTransaction().add(R.id.main_container, searchTaskFragment, "2").hide(searchTaskFragment).commit();
        fm.beginTransaction().add(R.id.main_container, createTaskFragment, "3").hide(createTaskFragment).commit();
        fm.beginTransaction().add(R.id.main_container, notificationsFragment, "4").hide(notificationsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, profileFragment, "5").hide(profileFragment).commit();

        navigationBar = findViewById(R.id.navigation_bar);

        navigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()) {
                    case R.id.navigation_home_button:
                        setActiveFragment(homeFragment);
                        break;
                    case R.id.navigation_search_button:
                        setActiveFragment(searchTaskFragment);
                        break;
                    case R.id.navigation_create_task_button:
                        setActiveFragment(createTaskFragment);
                        break;
                    case R.id.navigation_notifications_button:
                        setActiveFragment(notificationsFragment);
                        break;
                    case R.id.navigation_profile_button:
                        setActiveFragment(profileFragment);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void setActiveFragment(Fragment fragment) {
        fm.beginTransaction().hide(activeFragment).show(fragment).commit();
        activeFragment = fragment;
        if(fragment instanceof CreateTaskFragment) {
            ((CreateTaskFragment) createTaskFragment).showFragment();
        } else if(fragment instanceof ProfileFragment) {
            ((ProfileFragment) fragment).showFragment();
        } else if(fragment instanceof NotificationsFragment) {
            ((NotificationsFragment) fragment).showFragment();
        } else if(fragment instanceof  HomeFragment) {
            ((HomeFragment) fragment).showFragment();
        }
    }

    private void setClickedElement(int id) {
        navigationBar.setSelectedItemId(id);
    }
}
