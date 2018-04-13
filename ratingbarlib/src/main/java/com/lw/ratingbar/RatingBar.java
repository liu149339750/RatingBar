package com.lw.ratingbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by ubuntu on 18-3-16.
 */

public class RatingBar extends View{

    private int mDiameter;
    private int mNumStar;
    private int mStarPadding;
    private int mStarColor = 0xff787878;
    private int mStarCoverColor = 0xFFFFCC44;

    private StarShape mDefaultStar;
    private StarShape mPaintedStar;
    private StarShape mRatioStar;
    private StarShape mHalfPaitedStar;
    private float mRating;
    private float mMaxRating = 10;

    private float mRSR;

    private boolean mIsIndicator;
    private int mTouchSlop;
    private PointF mTouchPoint;
    private OnRatingBarChangeListener mLinstener;

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
        boolean isIndicator = a.getBoolean(R.styleable.RatingBar_isIndicator,res.getBoolean(R.bool.default_IsIndicator));
        a.recycle();

        setNumStar(numStar);
        setStarRadius(radius);

        setIsIndicator(isIndicator);
    }

    public void setNumStar(int count) {
        mNumStar = count;
        mRSR = mMaxRating/count;
    }

    public int getNumStar() {
        return mNumStar;
    }

    public void setMaxRating(float max) {
        mMaxRating = max;
        mRSR = mMaxRating/mNumStar;
        makeRatioStar();
        invalidate();
    }

    public float getMaxRating() {
        return mMaxRating;
    }

    public void setStarPadding(int padding) {
        mStarPadding = padding;
        requestLayout();
    }

    public int getStarPadding() {
        return mStarPadding;
    }

    public void setStarRadius(int radius) {
        mDiameter = radius * 2;
        makeDefaultStar();
        makeRatioStar();
        if(!mIsIndicator) {
            makeHalfPaitedStar();
        }
        requestLayout();
    }

    public int getStarRadius() {
        return mDiameter/2;
    }

    public void setIsIndicator(boolean enable) {
        mIsIndicator = enable;
        if(!mIsIndicator) {
            mTouchPoint = new PointF();
            mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            makeHalfPaitedStar();
        }
    }

    public boolean isIndicator() {
        return mIsIndicator;
    }

    public void setOnRatingBarChangeListener (RatingBar.OnRatingBarChangeListener listener) {
        mLinstener = listener;
    }


    public void setRating(float score) {
        mRating = score;
        makeRatioStar();
        invalidate();
        if(mLinstener != null)
            mLinstener.onRatingChanged(this,mRating,false);
    }

    public float getRating() {
        return mRating;
    }

    private void makeHalfPaitedStar() {
        mHalfPaitedStar = StarShape.create(mDiameter,mDiameter,0.5f,mStarColor,mStarCoverColor);
    }


    private void makeDefaultStar() {
        if(mDiameter > 0) {
            mDefaultStar = StarShape.create(mDiameter,mDiameter,0,mStarColor,mStarCoverColor);
            mPaintedStar = StarShape.create(mDiameter,mDiameter,1,mStarColor,mStarCoverColor);
        }
    }

    private void makeRatioStar() {
        if(mDiameter > 0) {
            mRatioStar = StarShape.create(mDiameter,mDiameter,(mRating%mRSR)/mRSR,mStarColor,mStarCoverColor);
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
        mRatioStar.draw(canvas,p);
        for(int i = 0;i < defaultStars;i++) {
            canvas.translate( mDiameter + mStarPadding,0);
            mDefaultStar.draw(canvas,p);
        }
        canvas.restore();;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mIsIndicator) {
            return false;
        }
        int action = event.getAction()&MotionEvent.ACTION_MASK;
        int width = getMeasuredWidth();
        float x = event.getX();
        float y = event.getY();
        boolean invalidate = false;
        if(action == MotionEvent.ACTION_DOWN) {
            mTouchPoint.x = x;
            mTouchPoint.y = y;
            invalidate = true;
        } else if(action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_UP) {
            if(Math.abs(x - mTouchPoint.x) > mTouchSlop || Math.abs(y - mTouchPoint.y) > mTouchSlop) {
                mTouchPoint.x = x;
                mTouchPoint.y = y;
                invalidate = true;
            }
        }
        if(invalidate) {
            int count = Math.round(((x/width * mMaxRating)/mRSR)*2);
            mRating = mRSR * count/2;
            mRatioStar = count%2 == 0 ?mDefaultStar:mHalfPaitedStar;
            invalidate();
            if(mLinstener != null)
                mLinstener.onRatingChanged(this,mRating,true);
        }

        return true;
    }

    public interface OnRatingBarChangeListener {

        void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser);

    }
}
