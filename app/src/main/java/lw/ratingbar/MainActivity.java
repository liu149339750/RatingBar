package lw.ratingbar;

import android.graphics.drawable.ShapeDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lw.ratingbar.RatingBar;
import com.lw.ratingbar.StarShape;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StarShape shape = StarShape.create(500,500,0.5f);
        shape.setRatio(0.5f);
        ShapeDrawable drawable = new ShapeDrawable(shape);
        drawable.setIntrinsicWidth(500);
        drawable.setIntrinsicHeight(500);
        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageDrawable(drawable);

        final RatingBar ratingBar = (RatingBar) findViewById(R.id.test2);
        ratingBar.setIsIndicator(false);
        ratingBar.setStarRadius(30);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                System.out.println("rating = " + rating);
            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ratingBar.setStarRadius(ratingBar.getStarRadius() + 5);
                ratingBar.setStarPadding(ratingBar.getStarPadding() + 2);
            }
        });

//        RatingBar view = new RatingBar(this);
//        view.setRating(5f);
//        view.setNumStar(5);
//        view.setStarPadding(20);
//        view.setStarRadius(30);
//        setContentView(view);z
    }
}
