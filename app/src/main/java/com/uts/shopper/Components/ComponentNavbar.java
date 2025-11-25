package com.uts.shopper.Components;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.uts.shopper.HomeCarActivity;
import com.uts.shopper.HomeNotificationsActivity;
import com.uts.shopper.HomeViewProductActivity;
import com.uts.shopper.MainActivity;
import com.uts.shopper.R;
import com.uts.shopper.UserLoginActivity;

public class ComponentNavbar {
    public static void apply(AppCompatActivity parent, String ActualPanel){

        LinearLayout inicio = parent.findViewById(R.id.inicio);
        LinearLayout favoritos = parent.findViewById(R.id.favoritos);
        LinearLayout carrito = parent.findViewById(R.id.carrito);
        LinearLayout entrada = parent.findViewById(R.id.entrada);
        LinearLayout mas = parent.findViewById(R.id.mas);

        inicio.setOnClickListener(v->{
            if (ActualPanel.equals("inicio")) return;
            Intent intent = new Intent(parent, MainActivity.class);
            parent.startActivity(intent);
            parent.overridePendingTransition(0, 0);
        });

        favoritos.setOnClickListener(v->{
            if (ActualPanel.equals("favoritos")) return;
            Intent intent = new Intent(parent, HomeCarActivity.class);
            parent.startActivity(intent);
            parent.overridePendingTransition(0, 0);
        });

        carrito.setOnClickListener(v->{
            if (ActualPanel.equals("carrito")) return;
            Intent intent = new Intent(parent, HomeCarActivity.class);
            parent.startActivity(intent);
            parent.overridePendingTransition(0, 0);
        });

        entrada.setOnClickListener(v->{
            if (ActualPanel.equals("entrada")) return;
            Intent intent = new Intent(parent, HomeNotificationsActivity.class);
            parent.startActivity(intent);
            parent.overridePendingTransition(0, 0);
        });

        mas.setOnClickListener(v->{
            if (ActualPanel.equals("mas")) return;
            Intent intent = new Intent(parent, UserLoginActivity.class);
            parent.startActivity(intent);
            parent.overridePendingTransition(0, 0);
        });

    }
}
