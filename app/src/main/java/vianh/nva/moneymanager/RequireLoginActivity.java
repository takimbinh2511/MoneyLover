package vianh.nva.moneymanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import vianh.nva.moneymanager.ui.login.LoginActivity;
import vianh.nva.moneymanager.ui.orders.CreatePasswordViewModel;

public class RequireLoginActivity extends AppCompatActivity {
    private CreatePasswordViewModel viewModel;
    public static boolean requireLogin = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(CreatePasswordViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requireLogin && viewModel.getUserPassword() != null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}