package com.lw.ratingbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ubuntu on 18-3-16.
 */

public class RatingBar extends View{

    private int mDiameter;
    private int mNumStar;
    private int mStarPadding;
    private int mStarColor = 0xff787878;
    private int mStarCoverColor = 0xFFFFCC44;

    private FiveStarShape mDefaultStar;
    private FiveStarShape mPaintedStar;
    private FiveStarShape mHalfStar;
    private float mRating;
    private float mMaxRating = 10;

    private float mRSR;

    public RatingBar(Context context) {
        super(context);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources res = context.getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        int radius = a.getDimensionPixelSize(R.styleable.RatingBar_starRadius,res.getDimensionPixelSize(R.dimen.default_radius_star));
        int numStar = a.getInt(R.styleable.RatingBar_numStars,res.getInteger(R.integer.default_num_star));
        mRating = a.getFloat(R.styleable.RatingBar_rating,0);
        mMaxRating = a.getFloat(R.styleable.RatingBar_maxRating,Float.valueOf(res.getString(R.string.default_maxrating_star)));
        mStarPadding = a.getDimensionPixelSize(R.styleable.RatingBar_starPadding,res.getDimensionPixelSize(R.dimen.default_padding_star));
        mStarColor = a.getColor(R.styleable.RatingBar_starBackColor,res.getColor(R.color.default_star_backgroud_color));
        mStarCoverColor = a.getColor(R.styleable.RatingBar_starCoverColor,res.getColor(R.color.default_star_cover_color));
        a.recycle();

        setNumStar(numStar);
        setStarRadius(radius);

    }

    public void setNumStar(int count) {
        mNumStar = count;
        mRSR = mMaxRating/count;
    }

    public void setMaxRating(float max) {
        mMaxRating = max;
        mRSR = mMaxRating/mNumStar;
        makeRatioStar();
        invalidate();
    }

    public void setStarPadding(int padding) {
        mStarPadding = padding;
    }

    public void setStarRadius(int radius) {
        mDiameter = radius * 2;
        makeDefaultStar();
        makeRatioStar();
    }

    public void setRating(float score) {
        mRating = score;
        makeRatioStar();
        invalidate();
    }

    private void makeDefaultStar() {
        if(mDiameter > 0) {
            mDefaultStar = FiveStarShape.create(mDiameter,mDiameter,0,mStarColor,mStarCoverColor);
            mPaintedStar = FiveStarShape.create(mDiameter,mDiameter,1,mStarColor,mStarCoverColor);
        }
    }

    private void makeRatioStar() {
        if(mDiameter > 0) {
            mHalfStar = FiveStarShape.create(mDiameter,mDiameter,(mRating%mRSR)/mRSR,mStarColor,mStarCoverColor);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int paddingh = getPaddingTop() + getPaddingBottom();
        int paddingw = getPaddingLeft() + getPaddingRight();
        int hsize = mDiameter + paddingh;
        int wsize = mDiameter * mNumStar + (mNumStar - 1) * mStarPadding + paddingw;
        setMeasuredDimension(wsize,hsize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        canvas.save();
        canvas.translate(getPaddingLeft(),getPaddingTop());
        int paintedStars = (int)(mRating/mRSR);
        int defaultStars = mNumStar - paintedStars - 1 > 0? (mNumStar - paintedStars - 1):0;
        for(int i = 0;i < paintedStars;i++) {
            mPaintedStar.draw(canvas,p);
            canvas.translate( mDiameter + mStarPadding,0);
        }
        mHalfStar.draw(canvas,p);
        for(int i = 0;i < defaultStars;i++) {
            canvas.translate( mDiameter + mStarPadding,0);
            mDefaultStar.draw(canvas,p);
        }
        canvas.restore();;
    }
}
