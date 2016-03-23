package inf8405.tp2.letsmeet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseGroup extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment_ = null;
    private boolean viewIsAtHome_;
    private Bitmap actualUserImage;
    ImageView mViewImage = null;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_group);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //initNavigationDrawer();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.nav_header_choose_group);
                mViewImage =(ImageView)findViewById(R.id.imageViewHeaderH);
                actualUserImage = DBContent.getInstance().getActualUser().getPhotoEnBitmap();
                mViewImage.setImageBitmap(actualUserImage);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.txtVwHeaderProfileEmailH);
        tv_email.setText(DBContent.getInstance().getActualUser().getCourriel());
        mViewImage =(ImageView)header.findViewById(R.id.imageViewHeaderH);
        actualUserImage = DBContent.getInstance().getActualUser().getPhotoEnBitmap();
        mViewImage.setImageBitmap(actualUserImage);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        displayView(R.id.nav_home);
//        initNavigationDrawer();


    }

//    public void initNavigationDrawer() {
//
//        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem menuItem) {
//
//                int id = menuItem.getItemId();
//
//                switch (id){
//                    case R.id.home:
//                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawers();
//                        break;
//                    case R.id.settings:
//                        Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
//                        break;
//
//                }
//                return true;
//            }
//        });
//        View header = navigationView.getHeaderView(0);
//        TextView tv_email = (TextView)header.findViewById(R.id.txtVwHeaderProfileEmailH);
//        tv_email.setText(DBContent.getInstance().getActualUser().getCourriel());
//        mViewImage =(ImageView)header.findViewById(R.id.imageViewHeaderH);
//        actualUserImage = DBContent.getInstance().getActualUser().getPhotoEnBitmap();
//        mViewImage.setImageBitmap(actualUserImage);
//        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
//
//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
//
//            @Override
//            public void onDrawerClosed(View v){
//                super.onDrawerClosed(v);
//            }
//
//            @Override
//            public void onDrawerOpened(View v) {
//                super.onDrawerOpened(v);
//            }
//        };
//        drawerLayout.setDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();
//    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewIsAtHome_) { //if the current view is not the home fragment
            displayView(R.id.nav_home); //display the home fragment
        } else {
            moveTaskToBack(true);  //If view is in home fragment, exit application
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.choose_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displayView(item.getItemId());
        return true;
    }
    public void displayView(int viewId) {
        boolean isEditProfileSelected = false;

        String title = getString(R.string.app_name);
        switch (viewId) {
            case R.id.nav_home:
                fragment_ = new NavHomeFragment();
                title  = "Home";
                viewIsAtHome_ = true;

                break;
            case R.id.nav_edtProfile:

                Intent i = new Intent(getBaseContext(), EditProfile.class);
                startActivity(i);

                viewIsAtHome_ = false;
                break;
        }


        //if(!isEditProfileSelected) {
            if (fragment_ != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment_);
                ft.commit();
            }

            // set the toolbar title
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(title);
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        //}
    }
}
