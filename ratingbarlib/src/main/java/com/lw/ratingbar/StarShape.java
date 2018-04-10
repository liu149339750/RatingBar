package com.lw.ratingbar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.shapes.Shape;

/**
 * Created by ubuntu on 18-3-15.
 */

public class StarShape extends Shape{
    private Path    mPath;
    private float   mStdWidth;
    private float   mStdHeight;
    private float   mScaleX = 1;    // cached from onResize
    private float   mScaleY = 1;

    private Bitmap mDes;
    private Bitmap mSrc;

    private float mRadio = -1;

    private int mBackgroudColor = 0xff787878;
    private int mCoverColor = 0xFFFFCC44;

    /**
     * PathShape constructor.
     *
     * @param path      a Path that defines the geometric paths for this shape
     * @param stdWidth  the standard width for the shape. Any changes to the
     *                  width with resize() will result in a width scaled based
     *                  on the new width divided by this width.
     * @param stdHeight the standard height for the shape. Any changes to the
     *                  height with resize() will result in a height scaled based
     */
    public StarShape(Path path, float stdWidth, float stdHeight, float radio, int bc, int cc) {
        this.mStdWidth = stdWidth;
        this.mStdHeight = stdHeight;
        mPath = path;
        mBackgroudColor = bc;
        mCoverColor = cc;
        mDes = makeDst((int)mStdWidth,(int)mStdHeight);
        setRatio(radio);
    }

    public void setRatio(float ratio) {
        if(mRadio != ratio) {
            mRadio = ratio;
            mSrc = makeSrc((int)mStdWidth,(int)mStdHeight);
        }
    }

    private Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.FILL);
        p.setColor(mCoverColor);
        c.drawRect(new Rect(0,0,(int)(w * mRadio),h),p);
        return bm;
    }

    Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(mBackgroudColor);
        c.drawPath(mPath,p);
        return bm;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.save();
        canvas.scale(mScaleX, mScaleY);
        int sc = canvas.saveLayer(0, 0, mStdWidth, mStdHeight, null,
                Canvas.MATRIX_SAVE_FLAG |
                        Canvas.CLIP_SAVE_FLAG |
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                        Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                        Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        canvas.drawBitmap(mDes,0,0,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(mSrc,0,0,paint);
        paint.setXfermode(null);
        canvas.restoreToCount(sc);
        canvas.restore();
    }


    @Override
    protected void onResize(float width, float height) {
        mScaleX = width / mStdWidth;
        mScaleY = height / mStdHeight;
    }

    private static float sin(int degree) {
        return (float) Math.sin(Math.toRadians(degree));
    }

    private static float cos(int degree) {
        return (float) Math.cos(Math.toRadians(degree));
    }


    public static StarShape create(float stdWidth, float stdHeight, float radio, int bc, int cc) {
        float l = stdHeight > stdWidth ? stdWidth:stdHeight;
        float radius = l/2;
        Path path = getPath(radius);
        return new StarShape(path,l,l,radio,bc,cc);
    }

    /**五个角分别为ABCDE，五个内角分别为A1，B1，C1，D1，E1*/
    public static StarShape create(float stdWidth, float stdHeight, float radio) {
        float l = stdHeight > stdWidth ? stdWidth:stdHeight;
        float radius = l/2;
        Path path = getPath(radius);
        return new StarShape(path,l,l,radio,0xff787878,0xFFFFCC44);
    }

    private static Path getPath(float radius) {
        Path path = new Path();
        float xo = radius;
        float yo = radius;
        float sideLen = (float) (radius/Math.sin(Math.toRadians(126)) * Math.sin(Math.toRadians(36)));
        PointF a = new PointF(xo,yo - radius);
        PointF a1 = new PointF(xo + sideLen*sin(18),yo - (radius - sideLen*cos(18)));
        PointF b = new PointF(xo + radius * cos(18),a1.y);
        PointF b1 = new PointF(b.x - sideLen*cos(36),b.y + sideLen * sin(36));
        PointF c = new PointF(xo + radius * sin(36),yo + radius * cos(36));
        //正弦公式求三角形边长
        PointF c1 = new PointF(xo,yo + radius/sin(126) * sin(18));

        float translationY = (radius * 2 - c.y)/2;
        a.y = a.y + translationY;
        a1.y = a1.y + translationY;
        b.y = b.y + translationY;
        b1.y = b1.y + translationY;
        c.y = c.y + translationY;
        c1.y = c1.y + translationY;

        //中心线对称
        PointF d = new PointF(2 * xo - c.x,c.y);
        PointF d1 = new PointF(2 * xo - b1.x,b1.y);
        PointF e = new PointF(2 * xo - b.x,b.y);
        PointF e1 = new PointF(2 * xo - a1.x,a1.y);

        path.moveTo(a.x,a.y);
        path.lineTo(a1.x,a1.y);
        path.lineTo(b.x,b.y);
        path.lineTo(b1.x,b1.y);
        path.lineTo(c.x,c.y);
        path.lineTo(c1.x,c1.y);
        path.lineTo(d.x,d.y);
        path.lineTo(d1.x,d1.y);
        path.lineTo(e.x,e.y);
        path.lineTo(e1.x,e1.y);
        path.close();
        return path;
    }
}
