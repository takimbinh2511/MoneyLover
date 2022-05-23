package vianh.nva.moneymanager.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import vianh.nva.moneymanager.MainActivity;
import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.RequireLoginActivity;
import vianh.nva.moneymanager.Utils.AfterTextChangedWatcher;
import vianh.nva.moneymanager.ui.orders.CreatePasswordViewModel;

public class LoginActivity extends AppCompatActivity {
    private EditText txbPassword;
    private Button btnLogin;
    private TextView tvWrongPassword;
    private CreatePasswordViewModel viewModel;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = ViewModelProviders.of(this).get(CreatePasswordViewModel.class);
        password = viewModel.getUserPassword();
        if (password == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        txbPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvWrongPassword = findViewById(R.id.txtInfo);
        btnLogin.setEnabled(false);
        setupView();
    }

    private void setupView() {
        txbPassword.addTextChangedListener(new AfterTextChangedWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });

        btnLogin.setOnClickListener( v -> login());
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    private void login() {
        if (password.equals(txbPassword.getText().toString())) {
            RequireLoginActivity.requireLogin = false;
            finish();
        } else {
            tvWrongPassword.setVisibility(View.VISIBLE);
        }
    }
}
