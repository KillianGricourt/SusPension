package com.example.suspension;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

public class PixelBoxView extends View {
    private Bitmap bitmap;
    private double mapWidth = 0;
    private double mapHeight = 0;

    public PixelBoxView(Context context) {
        super(context);
    }

    public PixelBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mapWidth = Controller.getInstance().mapW;
        mapHeight = Controller.getInstance().mapH;

        // Boucle sur chaque pixel de l'image bitmap et affecte une couleur
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int color = determineColor((double) x / w, (double) y / h); // Méthode pour déterminer la couleur en fonction de la position
                bitmap.setPixel(x, h - y - 1, color);
            }
        }
    }

    private int determineColor(double x, double y) {
        switch(Controller.getInstance().getLevel(new Point(mapWidth*x, -mapHeight*y))){
            case 1:
                return Color.YELLOW;
            case 2:
                return Color.MAGENTA;
            case 3:
                return Color.GREEN;
            case 4:
                return Color.BLUE;
            case 5:
                return Color.RED;
            case 6:
                return Color.DKGRAY;
            default:
                return Color.WHITE;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }
}
