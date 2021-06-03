package com.sabharish.securenotes;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FingerPrint extends AppCompatActivity {
    private static final int ERROR_NEGATIVE_BUTTON = 0;
    private BiometricPrompt biometricPrompt=null;
    private final Executor executor = Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerprint);

        if(biometricPrompt==null){
            biometricPrompt=new BiometricPrompt(this,executor,callback);
        }
        checkAndAuthenticate();
        findViewById(R.id.authenticate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndAuthenticate();
            }
        });
    }

    private void checkAndAuthenticate(){
        BiometricManager biometricManager=BiometricManager.from(this);
        switch (biometricManager.canAuthenticate())
        {
            case BiometricManager.BIOMETRIC_SUCCESS:
                BiometricPrompt.PromptInfo promptInfo = buildBiometricPrompt();
                biometricPrompt.authenticate(promptInfo);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                snack("Biometric Authentication currently unavailable");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                snack("Your device doesn't support Biometric Authentication");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                snack("Your device doesn't have any fingerprint enrolled");
                break;
        }
    }
    private BiometricPrompt.PromptInfo buildBiometricPrompt()
    {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setSubtitle("FingerPrint Authentication")
                .setDescription("Please place your finger on the sensor to unlock")
                .setNegativeButtonText("Cancel")
                .build();

    }
    private void snack(String text)
    {
        View view=findViewById(R.id.view);
        Snackbar snackbar=Snackbar.make(view,text, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    private final BiometricPrompt.AuthenticationCallback callback=new
            BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    if(errorCode==ERROR_NEGATIVE_BUTTON && biometricPrompt!=null)
                        biometricPrompt.cancelAuthentication();
                    snack((String) errString);
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    snack("Authenticated");
                }

                @Override
                public void onAuthenticationFailed() {
                    snack("The FingerPrint was not recognized.Please Try Again!");
                }
            };
}