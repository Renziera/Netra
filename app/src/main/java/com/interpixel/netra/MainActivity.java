package com.interpixel.netra;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.Stack;

public class MainActivity extends AppCompatActivity implements FlingListener {

    private final int[] topMenu = {
            R.drawable.account_info,
            R.drawable.transfer,
            R.drawable.payment,
            R.drawable.qr_payment,
            R.drawable.m_commerce,
            R.drawable.account_setting,
    };

    private final int[] accountInfoMenu = {
            R.drawable.balance_info,
            R.drawable.mini_statement,
    };

    private int[] currentMenu = topMenu;
    private Stack<Integer> selectedMenu = new Stack<>();
    private Adapter adapter;
    private NetraViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView splashLogo = findViewById(R.id.splash_logo);

        new Handler().postDelayed(() -> splashLogo.setVisibility(View.GONE), 1000);

        adapter = new Adapter(getSupportFragmentManager());

        viewpager = findViewById(R.id.viewpager);
        viewpager.setAdapter(adapter);
        viewpager.setFlingListener(this);
        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.d("Hmm", "onPageSelected: " + position);
                selectedMenu.pop();
                selectedMenu.push(position);
            }
        });
        selectedMenu.push(0);
    }

    @Override
    public void menuPrev() {
        if(selectedMenu.size() < 2) return;
        currentMenu = topMenu;
        adapter.notifyDataSetChanged();
        selectedMenu.pop();
        viewpager.setCurrentItem(selectedMenu.peek(), false);
        Log.d("Hmm", "menuPrev: " + selectedMenu.toString());
    }

    @Override
    public void menuNext() {
        currentMenu = accountInfoMenu;
        adapter.notifyDataSetChanged();
        selectedMenu.push(0);
        viewpager.setCurrentItem(selectedMenu.peek(), false);
    }

    class Adapter extends FragmentStatePagerAdapter {

        Adapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return new MenuFragment(currentMenu[position]);
        }

        @Override
        public int getCount() {
            return currentMenu.length;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }
}
