package vianh.nva.moneymanager.ui.orders;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vianh.nva.moneymanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OderFragment extends Fragment {
    private SwitchCompat switchPassword;
    private CreatePasswordViewModel viewModel;

    public OderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this).get(CreatePasswordViewModel.class);
        View view = inflater.inflate(R.layout.fragment_oder, container, false);
        switchPassword = view.findViewById(R.id.switchPass);
        init();
        return view;
    }

    private void init () {
        if (viewModel.getUserPassword() != null) {
            switchPassword.setChecked(true);
        } else {
            switchPassword.setChecked(false);
        }

        switchPassword.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b && viewModel.getUserPassword() == null) {
                Intent intent = new Intent(getContext(), CreatePasswordActivity.class);
                getContext().startActivity(intent);
            } else {
                viewModel.deletePassword();
            }
        });
    }

}
