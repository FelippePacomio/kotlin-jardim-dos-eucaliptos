package com.example.associacao_jardim_eucaliptos;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.associacao_jardim_eucaliptos.HelperClass;
import com.example.associacao_jardim_eucaliptos.LoginFragment;
import com.example.associacao_jardim_eucaliptos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpFragment extends Fragment {

    private EditText signupName, signupEmail, signupUsername, signupPassword;
    private TextView loginRedirectText;
    private Button signupButton;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_singup, container, false);

        signupName = view.findViewById(R.id.signup_name);
        signupEmail = view.findViewById(R.id.signup_email);
        signupPassword = view.findViewById(R.id.signup_password);
        signupButton = view.findViewById(R.id.signup_button);
        loginRedirectText = view.findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String nomeUsuario = signupName.getText().toString();
                String emailUsuario = signupEmail.getText().toString();
                String senhaUsuario = signupPassword.getText().toString();

                HelperClass helperClass = new HelperClass(nomeUsuario, emailUsuario, senhaUsuario);
                reference.child(nomeUsuario).setValue(helperClass);

                Toast.makeText(getContext(), "Você foi Cadastrado com Sucesso!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginFragment.class);
                startActivity(intent);
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginFragment.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
