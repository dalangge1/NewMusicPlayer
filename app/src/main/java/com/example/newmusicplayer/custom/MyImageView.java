package com.example.newmusicplayer.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.newmusicplayer.R;

public class MyImageView extends androidx.appcompat.widget.AppCompatImageView {
    Context mContext;

    public MyImageView(Context context) {
        super(context);
        mContext = context;
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    public Bitmap getDispBitmap(Bitmap pic, Context context) {
        if (getWidth() <= 0) {
            return null;
        }
        Bitmap disp = BitmapFactory.decodeResource(context.getResources(), R.drawable.disp);
        disp = Bitmap.createScaledBitmap(disp, getWidth(), getWidth(), true);

        if (pic != null) {
            pic = Bitmap.createScaledBitmap(pic, getWidth() - 120, getWidth() - 120, true);
        }

        Log.d("sandyzhang:MyImageView", getWidth() + " " + disp.getWidth());


        Bitmap bm = Bitmap.createBitmap(getWidth(), getWidth(), Bitmap.Config.ARGB_8888);


        Canvas canvas = new Canvas(bm);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawCircle(getWidth() / 2.0f, getWidth() / 2.0f, getWidth() / 2.0f - 30, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        if (pic != null) {
            canvas.drawBitmap(pic, 60, 60, paint);
        }

        paint.reset();
        canvas.drawBitmap(disp, 0, 0, null);

        return bm;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("sandyzhang:MyImageView", "onDraw");
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        Log.d("sandyzhang:MyImageView", "============");
        super.setImageBitmap(getDispBitmap(bm, mContext));

    }
}
