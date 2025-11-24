package com.uts.shopper.Auxiliar;

import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uts.shopper.HomeCarActivity;
import com.uts.shopper.MainActivity;
import com.uts.shopper.R;
import com.uts.shopper.UserLoginActivity;

import java.util.function.Consumer;

public class AuxNavbar {
    public static String panel = "home";
    public AuxNavbar(AppCompatActivity parent){
        LinearLayout navHome = parent.findViewById(R.id.home);
        LinearLayout navFavoritos = parent.findViewById(R.id.navFavoritos);
        LinearLayout navCarrito = parent.findViewById(R.id.navCarrito);
        LinearLayout navNotifications = parent.findViewById(R.id.notifications);
        LinearLayout navCofiguration = parent.findViewById(R.id.asConfigured);


        Consumer<Boolean> selection = e->{
            if(e){
                switch (panel){
                    case "home":
                        navHome.setBackgroundResource(R.drawable.draw_nav_option);
                        break;
                    case "favoritos":
                        navFavoritos.setBackgroundResource(R.drawable.draw_nav_option);
                        break;
                    case "carrito":
                        navCarrito.setBackgroundResource(R.drawable.draw_nav_option);
                        break;
                    case "notifications":
                        navNotifications.setBackgroundResource(R.drawable.draw_nav_option);
                        break;
                    case "config":
                        navCofiguration.setBackgroundResource(R.drawable.draw_nav_option);
                        break;

                }
            }else{
                navHome.setBackgroundResource(R.drawable.draw_nav_option_un_selected);
                navFavoritos.setBackgroundResource(R.drawable.draw_nav_option_un_selected);
                navCarrito.setBackgroundResource(R.drawable.draw_home_navbar_icon_card);
                navNotifications.setBackgroundResource(R.drawable.draw_nav_option_un_selected);
                navCofiguration.setBackgroundResource(R.drawable.draw_nav_option_un_selected);
            }
        };
        panel = "home";
        selection.accept(false);
        selection.accept(true);

        navHome.setOnClickListener(v -> {
            if (panel.equals("home")) return;
            panel = "home";
            selection.accept(false);
            selection.accept(true);
            parent.startActivity(new Intent(parent, MainActivity.class));
            //parent.finish();
        });


        navFavoritos.setOnClickListener(v -> {
            if (panel.equals("favoritos")) return;
            panel = "favoritos";
            selection.accept(false);
            selection.accept(true);
            Toast.makeText(parent, "Favoritos clickeado", Toast.LENGTH_SHORT).show();
            //parent.finish();
        });


        navCarrito.setOnClickListener(v -> {
            if (panel.equals("carrito")) return;
            panel = "carrito";
            selection.accept(false);
            selection.accept(true);
            parent.startActivity(new Intent(parent, HomeCarActivity.class));
            //parent.finish();
        });


        navNotifications.setOnClickListener(v -> {
            if (panel.equals("notifications")) return;
            panel = "notifications";
            selection.accept(false);
            selection.accept(true);
            Toast.makeText(parent, "Favoritos Notifications", Toast.LENGTH_SHORT).show();
            //parent.finish();
        });


        navCofiguration.setOnClickListener(v -> {
            if (panel.equals("config")) return;
            panel = "config";
            selection.accept(false);
            selection.accept(true);
            parent.startActivity(new Intent(parent, UserLoginActivity.class));
            //parent.finish();
        });
    }
}
