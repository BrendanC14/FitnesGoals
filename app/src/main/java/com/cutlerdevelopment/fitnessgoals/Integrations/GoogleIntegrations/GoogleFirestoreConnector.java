package com.cutlerdevelopment.fitnessgoals.Integrations.GoogleIntegrations;

import androidx.annotation.NonNull;

import com.cutlerdevelopment.fitnessgoals.Models.Team;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class GoogleFirestoreConnector {
    private static GoogleFirestoreConnector instance = null;

    public static GoogleFirestoreConnector getInstance() {
        if (instance != null) {
            return instance;
        }
        return new GoogleFirestoreConnector();
    }

    private GoogleFirestoreConnector( ) {
        instance = this;
    }

    public interface FirestoreListener {
        void gotTeamsFromFirestore();
    }

    static FirestoreListener listener;
    public void setListener(FirestoreListener listener) {this.listener = listener; }

    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void populateNewGameTeamsFromFirestore() {
        db.collection("Teams").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {

                                new Team(
                                        doc.getString("Name"),
                                        doc.getString("PrimaryColour"),
                                        doc.getLong("League").intValue());
                            }
                            listener.gotTeamsFromFirestore();
                        }
                    }
                });
    }


}
