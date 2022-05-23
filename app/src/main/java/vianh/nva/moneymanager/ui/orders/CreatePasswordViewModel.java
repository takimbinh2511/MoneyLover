package vianh.nva.moneymanager.ui.orders;

import android.app.Application;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import vianh.nva.moneymanager.RequireLoginActivity;
import vianh.nva.moneymanager.Utils.AfterTextChangedWatcher;
import vianh.nva.moneymanager.data.SharePrefHelper;

public class CreatePasswordViewModel extends AndroidViewModel {
    private SharePrefHelper sharePrefHelper;
    public ObservableField<String> password = new ObservableField<>();
    public ObservableField<String> passwordConfirm = new ObservableField<>();
    public ObservableBoolean isOk = new ObservableBoolean(false);
    public MutableLiveData<Boolean> done = new MutableLiveData<>(false);
    public TextWatcher passwordWatcher = new AfterTextChangedWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            if (!editable.toString().trim().equals("") && editable.toString().trim().equals(passwordConfirm.get())) {
                Log.d("password", editable.toString());
                isOk.set(true);
            } else {
                isOk.set(false);
            }
        }
    };

    public TextWatcher passwordConfirmWatcher = new AfterTextChangedWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            if (!editable.toString().trim().equals("") && editable.toString().trim().equals(password.get())) {
                isOk.set(true);
            } else {
                isOk.set(false);
            }
        }
    };

    public CreatePasswordViewModel(@NonNull Application application) {
        super(application);
        this.sharePrefHelper = SharePrefHelper.getInstance(application);
    }

    public void createPassword() {
        sharePrefHelper.savePassword(password.get());
        RequireLoginActivity.requireLogin = false;
        Log.d("Set password", "-----------------------------");
        done.setValue(true);
    }

    @Nullable
    public String getUserPassword () {
        return sharePrefHelper.getPassword();
    }

    public void deletePassword () {
        sharePrefHelper.clearPassword();
    }
}
