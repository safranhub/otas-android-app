package si.um.ietk.safran_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static android.R.attr.type;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<String> outerMap;

    public static ArrayList<OglasnaDeska> deskaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Intent intent = getIntent();
        outerMap = (ArrayList<String>)
                intent.getSerializableExtra("outerMap");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Log.d("MainActivity-odeska", outerMap.toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        MainActivity.ViewPagerAdapter adapter = new MainActivity.ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        List novica = getNovice();
        bundle.putSerializable("outerMap", (Serializable) novica);
        //Log.d("outerMap", outerMap.toString());
        // set Fragmentclass Arguments
        PrviFragment fragobj = new PrviFragment();
		DrugiFragment fragobj2 = new DrugiFragment();
        fragobj.setArguments(bundle);
        //Log.d("Bundle", bundle.toString());
        //Log.d("Bundle", bundle.toString());

        adapter.addFragment(fragobj, "ODESKA");
        adapter.addFragment(fragobj2, "OBVESTILA");

        viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

	// moj simple nacin posredovanja podatkov cez zanko for :)
    private List<OglasnaDeska> getNovice() {
        deskaList.clear();

            for (int i = 0; i < outerMap.size(); i+=4)
            {
                OglasnaDeska novica = new OglasnaDeska(outerMap.get(i).toString(), outerMap.get(i+1).toString(), outerMap.get(i+2).toString(), outerMap.get(i+3).toString());
                deskaList.add(novica);
            }

        return deskaList;
    }

}
