package com.covert.verify360;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.covert.verify360.BeanClasses.LoginResponse;
import com.covert.verify360.BeanClasses.User;

import java.util.List;

import Services.FactoryService;
import Services.Login;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.username_text)
    TextInputEditText textUsername;
    @BindView(R.id.password_text)
    TextInputEditText textPassword;
    @BindView(R.id.textInputLayoutUsername)
    TextInputLayout textInputLayoutUsername;
    @BindView(R.id.textInputLayoutPassword)
    TextInputLayout textInputLayoutPassword;
    @BindView(R.id.button_login)
    Button login;
    @BindView(R.id.progressBar)ProgressBar progressBar;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String usernameText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        sharedPreferences=getSharedPreferences("USER_DETAILS",MODE_PRIVATE);
        progressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.button_login)
    void login() {
        ConnectivityManager manager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = null;
        if(manager!=null){
             info=manager.getActiveNetworkInfo();
        }
        if (((info != null) && info.isConnected())) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            if (TextUtils.isEmpty(textUsername.getText())) {
                textUsername.requestFocus();
                textInputLayoutUsername.setErrorEnabled(true);
                textInputLayoutUsername.setError("Please Enter Username");
            } else if (TextUtils.isEmpty(textPassword.getText())) {
                textPassword.requestFocus();
                textInputLayoutPassword.setErrorEnabled(true);
                textInputLayoutPassword.setError("Please Enter Password");
            } else {
                progressBar.setVisibility(View.VISIBLE);
                usernameText = textUsername.getText().toString().trim();
                passwordText = textPassword.getText().toString().trim();
                Login login = FactoryService.createService(Login.class);
                Call<LoginResponse> loginCall = login.login(usernameText, passwordText);
                loginCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            final String msg = response.body().getError();
                            if (msg.equals("false")) {
                                List<User> user = response.body().getUser();
                                User userDetails = user.get(0);
    //                            Toast.makeText(LoginActivity.this, ""+userDetails.getEmployeeName(), Toast.LENGTH_SHORT).show();
                                if (userDetails.getUserLoginID().equals(usernameText)) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    editor = sharedPreferences.edit();
                                    editor.putString("USER_LOGIN_ID", userDetails.getUserLoginID());
                                    editor.putString("EMPLOYEE_NAME", userDetails.getEmployeeName());
                                    editor.putString("EMP_ID", userDetails.getEmpId());
                                    editor.putString("user_id", userDetails.getUserId());
                                    editor.putString("ROLE_NAME", userDetails.getRoleName());
                                    editor.apply();
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "User Not Found  ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(this, "Please connect to internet !!", Toast.LENGTH_SHORT).show();;
        }
    }
}
