package in.therenter.app.login;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import in.therenter.app.R;

public class SignInFragment extends Fragment {

    private Context context;
    private Activity activity;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    private Firebase firebase;

    private GoogleApiClient gac;
    private GoogleSignInOptions gso;

    private CallbackManager callbackManager;
    private JSONObject fbResponse;
    private final String[] permission = {"public_profile", "email"};

    protected static final int RC_SIGN_IN = 9001;

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

        firebase = new Firebase("https://the-renter-test.firebaseio.com/");
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();

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

        Button google = (Button) view.findViewById(R.id.google_login_button);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        System.out.println("Facebook Login Successful!");
                        System.out.println("Logged in user Details : ");
                        System.out.println("--------------------------");
                        System.out.println("User ID  : " + loginResult.getAccessToken().getUserId());
                        System.out.println("Authentication Token : " + loginResult.getAccessToken().getToken());

                        AccessToken token = loginResult.getAccessToken();

                        firebase.authWithOAuthToken("facebook", token.toString(), new Firebase.AuthResultHandler() {
                            @Override
                            public void onAuthenticated(AuthData authData) {
                                Toast.makeText(context, "Facebook user auth with firebase", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAuthenticationError(FirebaseError firebaseError) {
                                Toast.makeText(context, "Facebook user not auth with firebase", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Toast.makeText(activity, "Login Successful!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(activity, "Login cancelled by user!", Toast.LENGTH_LONG).show();
                        System.out.println("Facebook Login failed!!");
                    }

                    @Override
                    public void onError(FacebookException e) {
                        Toast.makeText(activity, "Login unsuccessful!", Toast.LENGTH_LONG).show();
                        System.out.println("Facebook Login failed!!");
                        Log.e("facebook login error", e.getMessage());
                    }
                });

        Button facebook = (Button) view.findViewById(R.id.facebook_login_button);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList(permission));
            }
        });

        return view;
    }

    private void signInWithGoogle() {
        if (gac != null) {
            gac.disconnect();
        }

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .build();
        gac = new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(gac);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {

                GoogleSignInAccount acct = result.getSignInAccount();

                String idToken = acct.getIdToken();
//                Toast.makeText(context, idToken, Toast.LENGTH_SHORT).show();
                firebase.authWithOAuthToken("google", idToken, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Toast.makeText(context, "Google user auth with firebase", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast.makeText(context, "Google user not auth with firebase", Toast.LENGTH_SHORT).show();
                        Log.e("Firebase error message", firebaseError.getMessage());
                        Log.e("Firebase error details", firebaseError.getDetails());
                    }
                });

            } else {
                Toast.makeText(context, "Some error occurred in google auth", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 64206) { //facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

}
