package com.sabharish.securenotes.card;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardEditText;
import com.braintreepayments.cardform.view.CardForm;
import com.braintreepayments.cardform.view.SupportedCardTypesView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sabharish.securenotes.R;

import java.util.HashMap;
import java.util.Map;

public class BaseCardFormActivity extends AppCompatActivity implements OnCardFormSubmitListener,
        CardEditText.OnCardTypeChangedListener {
    FirebaseFirestore fStore;

    FirebaseUser user;

    private static final CardType[] SUPPORTED_CARD_TYPES = { CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
            CardType.AMEX, CardType.DINERS_CLUB, CardType.JCB, CardType.MAESTRO, CardType.UNIONPAY,
            CardType.HIPER, CardType.HIPERCARD };

    private SupportedCardTypesView mSupportedCardTypesView;

    protected CardForm mCardForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_form);
        fStore = FirebaseFirestore.getInstance();

        mSupportedCardTypesView = findViewById(R.id.supported_card_types);
        mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);

        mCardForm = findViewById(R.id.card_form);
        user = FirebaseAuth.getInstance().getCurrentUser();

        mCardForm.cardRequired(true)
                .maskCardNumber(true)
                .maskCvv(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .saveCardCheckBoxChecked(false)
                .saveCardCheckBoxVisible(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .mobileNumberExplanation("Make sure SMS is enabled for this mobile number")
                .setup(this);
        mCardForm.setOnCardFormSubmitListener(this);
        mCardForm.setOnCardTypeChangedListener(this);





        // Warning: this is for development purposes only and should never be done outside of this example app.
        // Failure to set FLAG_SECURE exposes your app to screenshots allowing other apps to steal card information.
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    public void onCardTypeChanged(CardType cardType) {
        if (cardType == CardType.EMPTY) {
            mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);
        } else {
            mSupportedCardTypesView.setSelected(cardType);
        }
    }

    @Override
    public void onCardFormSubmit() {
        if (mCardForm.isValid()) {
            Button Submit = findViewById(R.id.button2);
            Submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                String Cno= mCardForm.getCardNumber();
                String date=mCardForm.getExpirationMonth();mCardForm.getExpirationYear();
                String Cvv= mCardForm.getCvv();
                String Name = mCardForm.getCardholderName();

                    // save note
                    DocumentReference docref = fStore.collection("notes").document(user.getUid()).collection("myCard").document();
                    Map<String,Object> card = new HashMap<>();
                    card.put("NAME",Name);
                    card.put("CARDNO",Cno);
                    card.put("EXPDATE",date);
                    card.put("CVV",Cvv);

                    docref.set(card).addOnSuccessListener(aVoid -> {
                        Toast.makeText(BaseCardFormActivity.this, "Card Added.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SampleActivity.class);
                        startActivity(intent);
                        finish();

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BaseCardFormActivity.this, "Error, Try again.", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            });
            Toast.makeText(this, R.string.valid, Toast.LENGTH_SHORT).show();
        } else {
            mCardForm.validate();
            Toast.makeText(this, R.string.invalid, Toast.LENGTH_SHORT).show();
        }
    }
}