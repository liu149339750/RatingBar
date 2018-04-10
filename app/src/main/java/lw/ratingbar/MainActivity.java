package lw.ratingbar;

import android.graphics.drawable.ShapeDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

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


//        RatingBar view = new RatingBar(this);
//        view.setRating(5f);
//        view.setNumStar(5);
//        view.setStarPadding(20);
//        view.setStarRadius(30);
//        setContentView(view);
    }
}
