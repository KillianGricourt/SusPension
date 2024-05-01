package com.example.suspension;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MapPosView extends View {
    double l;
    double h;

    int width;
    int height;

    Point pos;
    public MapPosView(Context context) {
        super(context);
    }

    public void init(double l, double h){
        this.l = l;
        this.h = h;
    }

    public void setPos(Point p){
        this.pos = p;
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(pos == null){
            return;
        }
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle((float) (width*(pos.x - Controller.getInstance().mapOffX)/l), (float) (-height*(pos.y-Controller.getInstance().mapOffY)/h), 5, paint);
    }
}
