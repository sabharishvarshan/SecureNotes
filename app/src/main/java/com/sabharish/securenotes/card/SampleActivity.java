package com.sabharish.securenotes.card;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.sabharish.securenotes.R;

public class SampleActivity extends AppCompatActivity {
    RecyclerView cardlists;
    FirebaseFirestore fStore;
    FirestoreRecyclerAdapter<Card,CardViewHolder> CardAdapter;
    FirebaseUser user;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_layout);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        Query query = fStore.collection("notes").document(user.getUid()).collection("myCard").orderBy("NAME", Query.Direction.DESCENDING);
        // query notes > uuid > mynotes

        FirestoreRecyclerOptions<Card> allCards = new FirestoreRecyclerOptions.Builder<Card>()
                .setQuery(query,Card.class)
                .build();

        CardAdapter = new FirestoreRecyclerAdapter<Card,CardViewHolder>(allCards) {



            @Override
            protected void onBindViewHolder(@NonNull CardViewHolder holder, int position, @NonNull final Card card) {
                holder.Title.setText(card.getNAME());
                Log.i("Sabharish", "" +card.getNAME());
                holder.CardNO.setText(card.getCARDNO());
                holder.CVV.setText(card.getCVV());
                holder.Expdate.setText(card.getEXPDATE());


                final String docId = CardAdapter.getSnapshots().getSnapshot(position).getId();
                Button delete=holder.view.findViewById(R.id.button3);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentReference docRef = fStore.collection("notes").document(user.getUid()).collection("myCard").document(docId);
                        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(SampleActivity.this,"Sucessfully Deleted",Toast.LENGTH_SHORT);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SampleActivity.this,"Error in Deletion",Toast.LENGTH_SHORT);
                            }
                        });

                    }
                });

            }

            @NonNull
            @Override
            public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter_layout,parent,false);
                return new CardViewHolder(view);
            }
        };
        cardlists = findViewById(R.id.recyclerView);
        cardlists.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));

        cardlists.setAdapter(CardAdapter);

        FloatingActionButton fab = findViewById(R.id.addcardFloat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), BaseCardFormActivity.class));
                overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                finish();
            }
        });


    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        TextView Title,CardNO,CVV,Expdate;
        View view;
        CardView mCardView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.cardname);
            CardNO = itemView.findViewById(R.id.no);
            CVV=itemView.findViewById(R.id.cvvno);
            Expdate=itemView.findViewById(R.id.date);
            mCardView = itemView.findViewById(R.id.carView);
            view = itemView;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        CardAdapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (CardAdapter != null) {
            CardAdapter.stopListening();
        }
    }
    }
