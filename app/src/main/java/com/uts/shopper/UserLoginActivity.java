package com.uts.shopper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uts.shopper.App.AppData;
import com.uts.shopper.App.AppSessionUserManager;
import com.uts.shopper.Controllers.ControllerLogin;
import com.uts.shopper.Models.ModelRequestLoginUser;
import com.uts.shopper.Models.ModelSessionUser;

public class UserLoginActivity extends AppCompatActivity {
    private AppSessionUserManager appSessionUserManager;
    ControllerLogin controllerLogin = new ControllerLogin();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appSessionUserManager = new AppSessionUserManager(this);

        EdgeToEdge.enable(this);
        setContentView(R.layout.user_login_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView volver = findViewById(R.id.volver);
        volver.setOnClickListener(e->{
            finish();
        });

        TextView linkRegister = findViewById(R.id.link_registrarse);
        linkRegister.setOnClickListener(e->{
            Intent intent = new Intent(this, UserRegisterActivity.class);
            startActivity(intent);
        });

        TextView btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(e->{

            EditText inputUsuario = findViewById(R.id.usuario);
            EditText inputPassword = findViewById(R.id.password);
            String textUsuario = String.valueOf(inputUsuario.getText());
            String textPassword = String.valueOf(inputPassword.getText());
            ModelRequestLoginUser requestData = new ModelRequestLoginUser(
                    textUsuario,
                    textPassword
            );
            controllerLogin.LoginPersona(requestData, (response)->{
                try {
                    runOnUiThread(() -> {
                        if (!response.logged){
                            Toast.makeText(this, "Usuario o contraseña invalidos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        appSessionUserManager.saveUserSession(new ModelSessionUser(
                                response.id,
                                response.email,
                                response.usuario,
                                response.isAdmin
                        ));

                        if (response.isAdmin){
                            Toast.makeText(this, "WELLCOME Admin", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UserLoginActivity.this, AdminPanelActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }

                        Toast.makeText(this, "WELLCOME Client", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } catch (Exception ex) {
                    Log.e("APP_API_DEBUG", "-> LOGIN error:" + ex.getMessage());
                }

            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ModelSessionUser user = appSessionUserManager.getUserSession();
        if (!user.usuario.isEmpty()){
            Intent intent = new Intent(UserLoginActivity.this, user.isAdmin? AdminPanelActivity.class: ClientPanelActivity.class);
            startActivity(intent);
            finish();
        }

    }
}