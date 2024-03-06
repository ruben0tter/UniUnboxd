package com.example.uniunboxd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AuthenticationFragment extends Fragment implements View.OnClickListener {

    EditText email;
    EditText password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_authentication, container, false);

        Button sign_in = (Button) view.findViewById(R.id.sign_in_button);
        sign_in.setOnClickListener(this);
        Button sign_up = (Button) view.findViewById(R.id.sign_up_button);
        sign_up.setOnClickListener(this);

        email = (EditText) view.findViewById(R.id.email_input);
        password = (EditText) view.findViewById(R.id.password_input);

        return view;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onClick(View view) {
        int ID = view.getId();
        if (ID == R.id.sign_in_button) {
            String userEmail = email.getText().toString();
            String userPass = password.getText().toString();
            //replaceFragment(new HomeFragment());
        } else if (ID == R.id.sign_up_button) {
            System.out.println("yo2");
            //replaceFragment(new RegistrationFragment());
        }
    }
}
