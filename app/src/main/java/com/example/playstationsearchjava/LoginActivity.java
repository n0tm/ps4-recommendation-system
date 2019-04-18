package com.example.playstationsearchjava;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.playstationsearchjava.Utils.ToolsMethods;
import com.example.playstationsearchjava.Utils.YoApi;
import com.google.common.hash.Hashing;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieStore;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.example.playstationsearchjava.Utils.ToolsMethods.sha256;

/**
 * A login screen that offers login via email/password.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class LoginActivity extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private String login;
    private String password;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.email_login_form);
        mProgressView = findViewById(R.id.login_progress);

        Button mEmailSignInButton = findViewById(R.id.login_button);
        mEmailSignInButton.setOnClickListener(view -> {
            setLoginAndPassword();
            Login();
        });

        Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(view -> {
            setLoginAndPassword();
            Register();
        });

    }

    private void setLoginAndPassword()
    {
        EditText users_login = findViewById(R.id.login);
        EditText users_password = findViewById(R.id.password);

        login = users_login.getText().toString();
        password = users_password.getText().toString();
    }

    Runnable loginThread = new Runnable() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {
            YoApi api = new YoApi();

            RequestBody body = new FormBody.Builder()
                    .add("someData", "something")
                    .build();

            String passwordHash = ToolsMethods.sha256(password);

            Request request = api.getRequestQuery("login")
                    .addHeader("Login", login)
                    .addHeader("password", passwordHash)
                    .post(body)
                    .head()
                    .build();

            JSONObject response = api.request(request);

            Message handlerMessage = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("action", "login");
            handlerMessage.setData(bundle);
            handler.sendMessage(handlerMessage);
        }
    };

    Runnable registerThread = new Runnable() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {
            YoApi api = new YoApi();

            RequestBody body = new FormBody.Builder()
                    .add("someData", "something")
                    .build();

            String passwordHash = ToolsMethods.sha256(password);

            Request request = api.getRequestQuery("register")
                    .addHeader("Login", login)
                    .addHeader("password", passwordHash)
                    .post(body)
                    .build();

            JSONObject response = api.request(request);

            Message handlerMessage = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("action", "register");
            handlerMessage.setData(bundle);
            handler.sendMessage(handlerMessage);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String action = bundle.getString("action");

            switch (action) {
                case "login":
                    showProgress(false);
                    redirectToMenu();
                    break;

                case "register":
                    showProgress(false);
                    redirectToPrimaryAuth();
                    break;
            }

        }
    };

    private void Register()
    {
        boolean validation = inputsValidation(login, password);

        if (!validation) return;

        showProgress(true);

        Thread thread = new Thread(registerThread);
        thread.start();
    }

    private void Login()
    {
        boolean input_validation = inputsValidation(login, password);

        if (!input_validation) return;

        showProgress(true);

        Thread thread = new Thread(loginThread);
        thread.start();

    }

    private void redirectToMenu()
    {
        Intent intent_AllTransaction = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(intent_AllTransaction);
    }

    private void redirectToPrimaryAuth()
    {
        Intent intent_AllTransaction = new Intent(LoginActivity.this, PrimaryAuthActivity.class);
        startActivity(intent_AllTransaction);
    }

    private boolean inputsValidation(String username, String password)
    {
        Boolean isEmailValid = isEmailValid(username);
        Boolean isPasswordValid = isPasswordValid(password);

        String warning = "";

        if (!isEmailValid) warning += "Введите коррентный email...\n";
        if (!isPasswordValid) warning += "Пароль должен быть больше 4 символов...\n";

        if (!warning.isEmpty()) {
            Toast.makeText(this, warning, Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }

    private boolean isEmailValid(String email) {
        return email.length() >= 4;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

