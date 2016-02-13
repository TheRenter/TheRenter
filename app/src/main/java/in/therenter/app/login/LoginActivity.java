package in.therenter.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;

import in.therenter.app.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(getApplicationContext());

        FragmentManager manager = getSupportFragmentManager();
        SignInFragment fragment = new SignInFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.relativeLayoutLogin, fragment, "SignInFragment");
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == SignInFragment.RC_SIGN_IN || resultCode == 64206) {
            SignInFragment fragment = (SignInFragment) getSupportFragmentManager().findFragmentByTag("SignInFragment");
            fragment.onActivityResult(requestCode, resultCode, data);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }
}
