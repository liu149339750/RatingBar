package lw.ratingbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.shapes.Shape;

/**
 * Created by ubuntu on 18-3-16.
 */

//shader 照色器，绘制图形的时候颜色取用对应坐标的着色器颜色

public class TestShape extends Shape{

    private Context mContext;

    public TestShape(Context context) {
        mContext = context;
    }


    @Override
    public void draw(Canvas canvas, Paint paint) {

        String text = "Shader";
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.ic_launcher);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        paint.setShader(bitmapShader);
        paint.setTextSize(100);
        paint.setStrokeWidth(50);
        canvas.drawText(text,0,text.length(),150,100,paint);

        canvas.drawRect(new Rect(0,0,220,220),paint);
    }
}
