package com.example.playstationsearchjava.RCVAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.playstationsearchjava.MenuActivity;
import com.example.playstationsearchjava.PrimaryAuthActivity;
import com.example.playstationsearchjava.R;


public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;

    public int[] lst_images = {
            R.drawable.slide_image_1,
            R.drawable.slide_image_2,
            R.drawable.slide_image_3,
            R.drawable.slide_image_4,
    };

    public String[] lst_titles = {
            "Добро пожаловать!",
            "Исследуй",
            "Выбирай",
            "Делись",
    };

    public String[] lst_descriptions = {
            "Ты скачал наше приложение, а значит готов погрузится в нашу курсовую",
            "Это приложение было сделано специально для тех кому сложно найти подходящую игру",
            "В нашей библиотеки свыше пяти тысяч игр, чего ты ждёшь?",
            "Рассказывай друзьям, чтобы они могли поиграть вместе с тобой!"
    };

    public int[] lst_bacground_colors = {
            Color.rgb(239,85,85),
            Color.rgb(110,49,89),
            Color.rgb(1,188,212),
            Color.rgb(55,55,55)
    };

    public SlideAdapter(Context context)
    {
        this.context = context;
    }


    @Override
    public int getCount() {
        return lst_titles.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view;

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.slide, container, false);

        LinearLayout layout = view.findViewById(R.id.slideLinear);
        ImageView image = view.findViewById(R.id.slideImage);
        TextView title = view.findViewById(R.id.textHeader);
        TextView description = view.findViewById(R.id.textDescription);
        Button button = view.findViewById(R.id.choose_categories);
        button.setVisibility((position != 3) ? View.GONE : View.VISIBLE);
        layout.setBackgroundColor(lst_bacground_colors[position]);
        image.setImageResource(lst_images[position]);
        title.setText(lst_titles[position]);
        description.setText(lst_descriptions[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == o);
    }
}
