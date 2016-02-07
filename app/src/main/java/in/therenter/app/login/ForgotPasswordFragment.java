package in.therenter.app.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import in.therenter.app.R;


public class ForgotPasswordFragment extends Fragment {


    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        final EditText editForgotPassword = (EditText) view.findViewById(R.id.editTextEmailForgotPassword);
        final TextView textForgotPassword = (TextView) view.findViewById(R.id.textViewForgotPassword);

        final Button btnSubmit = (Button) view.findViewById(R.id.buttonSubmitForgotPassword);

        editForgotPassword.setEnabled(true);
        textForgotPassword.setVisibility(View.INVISIBLE);
        textForgotPassword.setText("");
        btnSubmit.setEnabled(true);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editForgotPassword.getText().toString().length() == 0)
                    editForgotPassword.setBackgroundResource(R.drawable.custom_edit_text_error);
                else {
                    textForgotPassword.setVisibility(View.VISIBLE);
                    textForgotPassword.setText("Email sent. please follow instructions");
                    editForgotPassword.setEnabled(false);
                    btnSubmit.setEnabled(false);
                }
            }
        });

        return view;

    }


}
