package in.therenter.app.login;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import in.therenter.app.R;

public class SignInFragment extends Fragment {


    private Context context;
    private Activity activity;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        manager = getFragmentManager();

        final EditText editEmail = (EditText) view.findViewById(R.id.editTextEmailLogin);
        final EditText editPassword = (EditText) view.findViewById(R.id.editTextPasswordLogin);

        Button btnLogin = (Button) view.findViewById(R.id.buttonLoginLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editEmail.getText().toString().length() == 0)
                    editEmail.setBackgroundResource(R.drawable.custom_edit_text_error);
                else if (editPassword.getText().toString().length() == 0)
                    editPassword.setBackgroundResource(R.drawable.custom_edit_text_error);
                else
                    Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT).show();
            }
        });

        TextView txtForgotPassword = (TextView) view.findViewById(R.id.textViewForgotPasswordSignIn);
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordFragment fragment = new ForgotPasswordFragment();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.relativeLayoutLogin, fragment, "ForgotPasswordFragment");
                transaction.addToBackStack("forgotPassword");
                transaction.commit();
            }
        });

        TextView txtRegister = (TextView) view.findViewById(R.id.textViewRegisterSignIn);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment fragment = new SignUpFragment();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.relativeLayoutLogin, fragment, "SignUpFragment");
                transaction.addToBackStack("signUp");
                transaction.commit();
            }
        });

        ImageButton btnSignIn = (ImageButton) view.findViewById(R.id.google_plus_login_button);


        ImageButton loginButton = (ImageButton) view.findViewById(R.id.facebook_login_button);

        return view;
    }

}
