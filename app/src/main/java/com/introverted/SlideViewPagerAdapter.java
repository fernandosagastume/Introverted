package com.introverted;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SlideViewPagerAdapter extends PagerAdapter {

    Context context;

    public SlideViewPagerAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater lyi = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = lyi.inflate(R.layout.slide_screen, container, false);

        ImageView logoOnB = view.findViewById(R.id.logoOB);
        ImageView ind1 = view.findViewById(R.id.indicator);
        ImageView ind2 = view.findViewById(R.id.indicator1);
        ImageView ind3 = view.findViewById(R.id.indicator2);
        ImageView ind4 = view.findViewById(R.id.indicator3);
        ImageView ind5 = view.findViewById(R.id.indicator4);

        TextView title = view.findViewById(R.id.tituloOB1);
        TextView texto = view.findViewById(R.id.textoOB1);

        ImageView adelante = view.findViewById(R.id.adelante);
        ImageView atras = view.findViewById(R.id.atras);

        Button getstart = view.findViewById(R.id.get_started);
        getstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, Login.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
            }
        });
        adelante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Onboarding.viewpage.setCurrentItem(position + 1);
            }
        });
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Onboarding.viewpage.setCurrentItem(position - 1);
            }
        });

        switch (position){
            case 0:
                logoOnB.setImageResource(R.drawable.logo);
                ind1.setImageResource(R.drawable.selected);
                ind2.setImageResource(R.drawable.unselected);
                ind3.setImageResource(R.drawable.unselected);
                ind4.setImageResource(R.drawable.unselected);
                ind5.setImageResource(R.drawable.unselected);
                title.setText("Conoce personas");
                texto.setText("Vive una experiencia distinta a las aplicaciones para conocer personas convencionales");
                atras.setVisibility(View.GONE);
                adelante.setVisibility(View.VISIBLE);
                break;
            case 1:
                logoOnB.setImageResource(R.drawable.ic_people);
                ind1.setImageResource(R.drawable.unselected);
                ind2.setImageResource(R.drawable.selected);
                ind3.setImageResource(R.drawable.unselected);
                ind4.setImageResource(R.drawable.unselected);
                ind5.setImageResource(R.drawable.unselected);
                title.setText("Haz match a tu manera");
                texto.setText("Conoce a personas con tus mismos gustos");
                atras.setVisibility(View.VISIBLE);
                adelante.setVisibility(View.VISIBLE);
                break;
            case 2:
                logoOnB.setImageResource(R.drawable.ic_paper_plane);
                ind1.setImageResource(R.drawable.unselected);
                ind2.setImageResource(R.drawable.unselected);
                ind3.setImageResource(R.drawable.selected);
                ind4.setImageResource(R.drawable.unselected);
                ind5.setImageResource(R.drawable.unselected);
                title.setText("Envia mensajes privados");
                texto.setText("Mensajea con personas con las que hagas match");
                atras.setVisibility(View.VISIBLE);
                adelante.setVisibility(View.VISIBLE);
                break;
            case 3:
                logoOnB.setImageResource(R.drawable.ic_phone);
                ind1.setImageResource(R.drawable.unselected);
                ind2.setImageResource(R.drawable.unselected);
                ind3.setImageResource(R.drawable.unselected);
                ind4.setImageResource(R.drawable.selected);
                ind5.setImageResource(R.drawable.unselected);
                title.setText("Interactúa con actividades");
                texto.setText("Desde leer un libro, hasta jugar juegos multijugador, rompe el hielo con tu match de una forma diferente");
                atras.setVisibility(View.VISIBLE);
                adelante.setVisibility(View.VISIBLE);
                break;
            case 4:
                logoOnB.setImageResource(R.drawable.ic_notifications);
                ind1.setImageResource(R.drawable.unselected);
                ind2.setImageResource(R.drawable.unselected);
                ind3.setImageResource(R.drawable.unselected);
                ind4.setImageResource(R.drawable.unselected);
                ind5.setImageResource(R.drawable.selected);
                title.setText("Recibe notificaciones");
                texto.setText("Recibe notificaciones cuando hagas match o recibas mensajes");
                atras.setVisibility(View.VISIBLE);
                adelante.setVisibility(View.GONE);
                break;
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
