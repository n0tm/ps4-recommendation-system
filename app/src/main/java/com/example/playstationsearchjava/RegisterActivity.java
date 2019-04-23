package com.example.playstationsearchjava;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.playstationsearchjava.Utils.Api.Models.ResponseModel;
import com.example.playstationsearchjava.Utils.Api.ResponseStatusTypes;
import com.example.playstationsearchjava.Utils.Api.YoApi;
import com.example.playstationsearchjava.Utils.ToolsMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText Login;
    private EditText Password;
    private EditText Email;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Login = findViewById(R.id.login);
        Password = findViewById(R.id.password);
        Email = findViewById(R.id.email);

        Button mEmailSignInButton = findViewById(R.id.register);
        mEmailSignInButton.setOnClickListener(view -> attemptLogin());

        mLoginFormView = findViewById(R.id.email_login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String login = Login.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            Password.setError(getString(R.string.error_field_required));
            focusView = Password;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            Password.setError(getString(R.string.error_invalid_login));
            focusView = Password;
            cancel = true;
        }

        if (TextUtils.isEmpty(login)) {
            Login.setError(getString(R.string.error_field_required));
            focusView = Login;
            cancel = true;
        } else if (!isPasswordValid(login)) {
            Login.setError(getString(R.string.error_invalid_login));
            focusView = Login;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            Email.setError(getString(R.string.error_field_required));
            focusView = Email;
            cancel = true;
        } else if (!isEmailValid(email)) {
            Email.setError(getString(R.string.error_invalid_email));
            focusView = Email;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            Thread thread = new Thread(registerThread);
            thread.start();
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    Runnable registerThread = new Runnable() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {

            String email = Email.getText().toString();
            String password = Password.getText().toString();
            String login = Login.getText().toString();

            Message handlerMessage = handler.obtainMessage();
            Bundle bundle = new Bundle();
            ResponseModel Response;
            YoApi api = new YoApi();

            RequestBody body;
            Request request;

            body = new FormBody.Builder()
                    .build();

            String passwordHash = ToolsMethods.sha256(password);

            request = api.getRequestQuery("register")
                    .addHeader("login", login)
                    .addHeader("password", passwordHash)
                    .addHeader("email", email)
                    .method("POST", body)
                    .build();

            Response = api.request(request);

            Log.d("Response from request", Response.getResponse().toString());
            Log.d("StatusCode", String.valueOf(Response.getStatusCode()));
            Log.d("StatusMsg", Response.getStatusMsg());
            Log.d("Headers", Response.getHeaders().toString());

            if (Response.getStatusCode().equals(ResponseStatusTypes.EXISTING_LOGIN)) {
                bundle.putInt("action", ResponseStatusTypes.EXISTING_LOGIN);
                handlerMessage.setData(bundle);
                handler.sendMessage(handlerMessage);
                return;
            }

            body = new FormBody.Builder()
                    .build();

            request = api.getRequestQuery("auth")
                    .addHeader("login", login)
                    .addHeader("password", passwordHash)
                    .method("POST", body)
                    .build();

            Response = api.request(request);

            Log.d("Response from request", Response.getResponse().toString());
            Log.d("StatusCode", String.valueOf(Response.getStatusCode()));
            Log.d("StatusMsg", Response.getStatusMsg());
            Log.d("Headers", Response.getHeaders().toString());


            SharedPreferences settings = getSharedPreferences("Auth", 0);
            SharedPreferences.Editor editor = settings.edit();

            editor.putString("Cookie", Response.getHeaders().get("Set-Cookie"));

            editor.apply();

            bundle.putInt("action", 0);
            handlerMessage.setData(bundle);
            handler.sendMessage(handlerMessage);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            Integer action = bundle.getInt("action");

            if (action.equals(ResponseStatusTypes.EXISTING_LOGIN)) {
                Integer prefix = new Random().nextInt(1000 + 1)  + 100;
                Toast.makeText(getBaseContext(), String.format("Этот логин уже зарегистрирован, попробуйте другой, например - %s%s", Login.getText().toString(), prefix), Toast.LENGTH_LONG).show();
            } else if (action.equals(0)) { // registration
                SharedPreferences settings = getSharedPreferences("Auth", 0);
                String cookie = settings.getString("Cookie", "");

                Log.d("Cookie: ",  cookie);

                showProgress(false);
                redirectToPrimaryAuth();
            } else {
                System.out.println("Uknown ERROR!");
            }

            showProgress(false);
        }
    };

    private void redirectToPrimaryAuth()
    {
        Intent intent_AllTransaction = new Intent(this, PrimaryAuthActivity.class);
        startActivity(intent_AllTransaction);
    }

}

