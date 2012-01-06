package de.rueckemann.hello.DrawableViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;

public class CustomDrawable extends View {
    private ShapeDrawable mDrawable;

    public CustomDrawable(Context context) {
        super(context);

        int x = 10;
        int y = 10;
        int width = 16;
        int height = 16;

        mDrawable = new ShapeDrawable(new RectShape());
        mDrawable.getPaint().setColor(0xff74AC23);
        mDrawable.setBounds(x, y, x + width, y + height);
    }

    protected void onDraw(Canvas canvas) {
        mDrawable.draw(canvas);
    }
}