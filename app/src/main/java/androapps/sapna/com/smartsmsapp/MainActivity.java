package androapps.sapna.com.smartsmsapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import androapps.sapna.com.smartsmsapp.Adapter.FragmentAdapter;
import androapps.sapna.com.smartsmsapp.Fragment.DraftFragment;
import androapps.sapna.com.smartsmsapp.Fragment.MessageFragment;
import androapps.sapna.com.smartsmsapp.Fragment.ScheduleFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
@BindView(R.id.tabLayout)
    TabLayout tabLayout;

@BindView(R.id.viewPager)
ViewPager viewPager;

@BindView(R.id.toolbar)
Toolbar toolbar;
//
//@BindView(R.id.fab)
//    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
         getSupportActionBar().setTitle("SmartSMS");
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        if (checkCallingOrSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_SMS},
                    0);

        if (checkCallingOrSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    1);







    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new DraftFragment(), "DRAFT");
        adapter.addFragment(new MessageFragment(), "MESSAGES");
        adapter.addFragment(new ScheduleFragment(), "SCHEDULED SMS");
        viewPager.setAdapter(adapter);
    }
}
