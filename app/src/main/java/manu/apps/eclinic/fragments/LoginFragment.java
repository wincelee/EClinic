package manu.apps.eclinic.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import manu.apps.eclinic.R;
import manu.apps.eclinic.classes.Config;
import manu.apps.eclinic.classes.CustomTextWatcher;

public class LoginFragment extends Fragment implements View.OnClickListener {

    TextView tvRegister;
    NavController navController;

    TextInputLayout tilEmail, tilPassword;
    TextInputEditText etEmail, etPassword;

    MaterialButton btnLogin;

    private FirebaseAuth firebaseAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        navController = Navigation.findNavController(view);

        tvRegister = view.findViewById(R.id.tv_register);

        tilEmail = view.findViewById(R.id.til_email);
        tilPassword = view.findViewById(R.id.til_password);

        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);

        btnLogin = view.findViewById(R.id.btn_login);

        tvRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        // Setting Text Watchers
        etEmail.addTextChangedListener(new CustomTextWatcher(tilEmail));
        etPassword.addTextChangedListener(new CustomTextWatcher(tilPassword));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:

                navController.navigate(R.id.action_login_to_register);

                break;
            case R.id.btn_login:

                checkCredentials();

                break;
            default:
                break;
        }
    }

    private void checkCredentials() {

        final String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty()) {
            showError(tilEmail, "Email has not been entered");
        }
        if (password.isEmpty()) {
            showError(tilPassword, "Password has not been entered");
        } else {

            final ProgressDialog pbLogin = new ProgressDialog(getActivity());
            pbLogin.setMessage("Please wait ......");
            pbLogin.setCancelable(false);
            pbLogin.show();

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        navController.navigate(R.id.action_login_to_home);

                        pbLogin.dismiss();

                    } else {

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.layout_invalid_credentials);
                        dialog.show();
                        dialog.setCancelable(false);

                        // Setting dialog background to transparent
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        // Setting size of the dialog
                        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);

                        MaterialButton tryAgain = dialog.findViewById(R.id.btn_try_again);

                        tryAgain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();

                            }
                        });
                    }
                }
            });
        }
    }

    private void showError(TextInputLayout til, String error) {
        til.setError(error);
        til.requestFocus();

    }
}