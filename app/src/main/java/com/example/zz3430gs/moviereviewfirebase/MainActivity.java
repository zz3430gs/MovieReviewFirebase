package com.example.zz3430gs.moviereviewfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText mMovieNameET;
    EditText mMovieReviewTextET;
    RatingBar mMovieStars;
    Button mSaveButton;
    Button mShowAllReviewsButton;
    TextView mAllMoviesTV;

    private static final String ALL_REVIEWS_KEY = "All reviews";
    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //References for components
        mMovieNameET = (EditText) findViewById(R.id.movie_name_et);
        mMovieReviewTextET = (EditText) findViewById(R.id.movie_review_et);
        mMovieStars = (RatingBar) findViewById(R.id.movie_stars_rb);

        mSaveButton = (Button) findViewById(R.id.save_review_button);
        mShowAllReviewsButton = (Button) findViewById(R.id.show_all_reviews_button);

        mAllMoviesTV = (TextView) findViewById(R.id.all_movie_data_tv);

        //Register listeners for buttons
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReview();
            }
        });

        mShowAllReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchAllReviews();
            }
        });


        //Configure Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();
        fetchAllReviews();

    }

    private void saveReview() {
        String name = mMovieNameET.getText().toString();
        String reviewText = mMovieReviewTextET.getText().toString();
        float stars = mMovieStars.getRating();

        MovieReview review = new MovieReview(name, reviewText, stars);

        DatabaseReference newReview = dbReference.child(ALL_REVIEWS_KEY).push();
        newReview.setValue(review);

        Toast.makeText(this, "Review saved", Toast.LENGTH_LONG).show();

    }

    private void fetchAllReviews() {

        Query getAllReviews = dbReference.child(ALL_REVIEWS_KEY);

        getAllReviews.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("Movie Reviews", dataSnapshot.toString());

                ArrayList<MovieReview> reviews = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    MovieReview review = ds.getValue(MovieReview.class);

                    reviews.add(review);
                }

                mAllMoviesTV.setText(reviews.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e("Movie Reviews", "Database error fetching all reviews", databaseError.toException());

            }
        });

    }
}
