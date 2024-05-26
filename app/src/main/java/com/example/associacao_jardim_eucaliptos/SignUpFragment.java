package com.example.associacao_jardim_eucaliptos;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpFragment extends Fragment {

    private EditText signupName, signupEmail, signupPassword;
    private TextView loginRedirectText;
    private Button signupButton;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_singup, container, false);

        mAuth = FirebaseAuth.getInstance();
        signupName = view.findViewById(R.id.signup_name);
        signupEmail = view.findViewById(R.id.signup_email);
        signupPassword = view.findViewById(R.id.signup_password);
        signupButton = view.findViewById(R.id.signup_button);
        loginRedirectText = view.findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String password = signupPassword.getText().toString();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getContext(), "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ProgressUtils.showProgressDialog(getContext());

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                ProgressUtils.hideProgressDialog();
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        String userId = user.getUid();

                                        database = FirebaseDatabase.getInstance();
                                        reference = database.getReference("users");

                                        HelperClass helperClass = new HelperClass(name, email, password, false);
                                        reference.child(userId).setValue(helperClass)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getContext(), "Você foi cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                                                            mAuth.signOut();
                                                            navigateToLoginFragment();
                                                        } else {
                                                            Toast.makeText(getContext(), "Falha ao salvar dados do usuário!", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "Falha ao salvar dados do usuário: " + task.getException());
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    Log.d(TAG, "Falha ao salvar dados do usuário: " + task.getException());
                                    Toast.makeText(getContext(), "Falha ao criar conta!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
    private void navigateToLoginFragment() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new LoginFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

