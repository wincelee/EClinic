package manu.apps.eclinic.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import manu.apps.eclinic.R;
import manu.apps.eclinic.classes.Config;
import manu.apps.eclinic.classes.User;

public class RegisterFragment extends Fragment implements View.OnClickListener, TextWatcher {

    TextInputLayout tilEmail, tilUsername, tilPhoneNumber, tilCounty, tilPassword;
    TextInputEditText etEmail,etUsername, etPhoneNumber, etCounty, etPassword;
    MaterialButton btnRegister;
    TextView tvLogin;
    NavController navController;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        // Getting Firebase Authentication Instance
        firebaseAuth = FirebaseAuth.getInstance();

        tilEmail = view.findViewById(R.id.til_email);
        tilUsername = view.findViewById(R.id.til_username);
        tilPhoneNumber = view.findViewById(R.id.til_phone_number);
        tilCounty = view.findViewById(R.id.til_county);
        tilPassword = view.findViewById(R.id.til_password);

        etEmail = view.findViewById(R.id.et_email);
        etUsername = view.findViewById(R.id.et_username);
        etPhoneNumber = view.findViewById(R.id.et_phone_number);
        etCounty = view.findViewById(R.id.et_county);
        etPassword = view.findViewById(R.id.et_password);

        btnRegister = view.findViewById(R.id.btn_register);
        tvLogin = view.findViewById(R.id.tv_login);

        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

        etEmail.addTextChangedListener(this);
        etUsername.addTextChangedListener(this);
        etPhoneNumber.addTextChangedListener(this);
        etCounty.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login:

                navController.navigate(R.id.nav_login_fragment);

                break;
            case R.id.btn_register:

                checkCredentials();

                break;
            default:
                break;
        }
    }

    private void checkCredentials(){

        final String userName = etUsername.getText().toString();
        final String email = etEmail.getText().toString();
        final String phoneNumber = etPhoneNumber.getText().toString();
        final String county = "county";
        String password = etPassword.getText().toString();

        if (email.isEmpty()){
            showError(tilEmail, "Email has not been entered");
        }
        if (userName.isEmpty()){
            showError(tilUsername, "Username has not been entered");
        }
        if (phoneNumber.isEmpty()){
            showError(tilPhoneNumber, "Phone number has not been entered");
        }
        if (phoneNumber.isEmpty()){
            showError(tilCounty, "County has not entered");
        }
        if (password.isEmpty()){
            showError(tilPassword, "Password has not been entered");
        }else {

            final ProgressDialog pbRegister = new ProgressDialog(getActivity());
            pbRegister.setMessage("Registering please wait .......");
            pbRegister.setCancelable(false);
            pbRegister.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        assert firebaseUser != null;
                        String userId = firebaseUser.getUid();

                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("userId", userId);
                        hashMap.put("email", email);
                        hashMap.put("userName", userName);
                        hashMap.put("phoneNumber", phoneNumber);
                        hashMap.put("county", county);
                        hashMap.put("imageUrl", "default");

                        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                    pbRegister.dismiss();

                                    final Dialog dialog = new Dialog(getActivity());
                                    dialog.setContentView(R.layout.layout_register_dialog);
                                    dialog.show();
                                    dialog.setCancelable(false);

                                    // Setting dialog background to transparent
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    // Setting size of the dialog
                                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);

                                    TextView tvRegistrationMessage = dialog.findViewById(R.id.tv_registration_message);
                                    MaterialButton btnProceed = dialog.findViewById(R.id.btn_proceed);

                                    tvRegistrationMessage.setText("Your account was created successfully" + " " + userName);

                                    btnProceed.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            navController.navigate(R.id.action_register_to_home);
                                            dialog.dismiss();
                                        }
                                    });

                                }else {
                                    Toast.makeText(getActivity(), "User registered but saving failed", Toast.LENGTH_SHORT).show();
                                    Config.showSnackBar(getActivity(), task.getException().toString());
                                }
                            }
                        });

                    }else {

                        pbRegister.dismiss();

                        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();;
                        alertDialog.setMessage("Error has occurred during registration, please try again.... ");
                        alertDialog.show();

                        Config.showSnackBar(getActivity(), task.getException().toString());

                    }
                }
            });

        }
    }

    private void showError(TextInputLayout til, String error){
        til.setError(error);
        til.requestFocus();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tilEmail.setErrorEnabled(false);
        tilUsername.setErrorEnabled(false);
        tilPhoneNumber.setErrorEnabled(false);
        tilCounty.setErrorEnabled(false);
        tilPassword.setErrorEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}