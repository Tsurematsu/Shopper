package com.uts.shopper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uts.shopper.Controllers.ControllerLogin;
import com.uts.shopper.Models.ModelRequestLoginUser;

public class UserLoginActivity extends AppCompatActivity {
    ControllerLogin controllerLogin = new ControllerLogin();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_login_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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
                    Log.d("APP_API_DEBUG", "-> LOGIN error:" + ex.getMessage());
                }

            });
        });
    }
}