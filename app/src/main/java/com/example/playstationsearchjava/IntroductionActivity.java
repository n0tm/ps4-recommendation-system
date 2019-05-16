package com.example.playstationsearchjava;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.playstationsearchjava.RCVAdapters.SlideAdapter;

public class IntroductionActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SlideAdapter slideAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        viewPager = findViewById(R.id.viewPager);
        slideAdapter = new SlideAdapter(this);
        viewPager.setAdapter(slideAdapter);
    }

    public void redirect(View view) {
        Intent intent = new Intent(this, PrimaryAuthActivity.class);
        this.startActivity(intent);
    }
}
