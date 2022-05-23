package vianh.nva.moneymanager.ui.orders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import java.util.Objects;

import vianh.nva.moneymanager.R;
import vianh.nva.moneymanager.databinding.ActivityCreatePasswordBinding;

public class CreatePasswordActivity extends AppCompatActivity {
    private CreatePasswordViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_password);
        ActivityCreatePasswordBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_create_password);
        viewModel = ViewModelProviders.of(this).get(CreatePasswordViewModel.class);
        viewModel.done.observe(this, aBoolean -> {
            if (aBoolean) {
                finish();
            }
        });
        binding.setViewModel(viewModel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
