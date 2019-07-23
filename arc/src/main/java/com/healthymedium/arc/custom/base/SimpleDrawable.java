package com.healthymedium.arc.custom.base;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewOutlineProvider;


public abstract class SimpleDrawable extends Drawable {

    protected boolean drawStroke = false;
    protected boolean drawFill = false;
    protected SimpleGradient gradient;

    protected Path path;
    protected Paint fillPaint;
    protected Paint strokePaint;
    protected float strokeWidth;

    // only used for drawing
    protected Rect rect;
    protected int height;
    protected int width;

    public SimpleDrawable(){

        // initialize member variables
        rect = new Rect(0,0,0,0);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        width = bounds.width();
        height = bounds.height();

        // create a rect that's small enough that the stroke isn't cut off
        updateOffsets();

        path = getPath(rect);

        if(gradient!=null){
            fillPaint.setShader(gradient.getShader(width,height));
        }
    }

    protected void updateOffsets() {
        int offset = (int) (strokeWidth/2);
        rect.set(offset,offset,width-offset,height-offset);
    }

    @Override
    public void draw(Canvas canvas) {
        if(drawFill){
            canvas.drawPath(path,fillPaint);
        }
        if(drawStroke) {
            canvas.drawPath(path,strokePaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        fillPaint.setAlpha(alpha);
        strokePaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        fillPaint.setColorFilter(colorFilter);
        strokePaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    public void setFillColor(int color) {
        drawFill = color!=0;
        fillPaint.setColor(color);
    }

    public void setFillShader(Shader shader) {
        drawFill = true;
        fillPaint.setShader(shader);
    }

    public void setStrokeColor(int color) {
        drawStroke = color!=0;
        strokePaint.setColor(color);
    }

    public void setStrokeWidth(float width) {
        strokeWidth = width;
        strokePaint.setStrokeWidth(width);
    }

    public void setStrokeDash(float length, float spacing) {
        strokePaint.setPathEffect(new DashPathEffect(new float[]{length,spacing}, 0));
    }

    public void setGradient(int gradientId, int colorFirst, int colorSecond) {
        switch (gradientId){
            case SimpleGradient.LINEAR_HORIZONTAL:
                setHorizontalGradient(colorFirst,colorSecond);
                break;
            case SimpleGradient.LINEAR_VERTICAL:
                setVerticalGradient(colorFirst,colorSecond);
                break;
        }
    }

    public void setHorizontalGradient(int colorLeft, int colorRight) {
        drawFill = true;
        gradient = new SimpleGradient(SimpleGradient.LINEAR_HORIZONTAL);
        gradient.setColor0(colorLeft);
        gradient.setColor1(colorRight);
        gradient.setTileMode(Shader.TileMode.CLAMP);
    }

    public void setVerticalGradient(int colorTop, int colorBottom) {
        drawFill = true;
        gradient = new SimpleGradient(SimpleGradient.LINEAR_VERTICAL);
        gradient.setColor0(colorTop);
        gradient.setColor1(colorBottom);
        gradient.setTileMode(Shader.TileMode.CLAMP);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public ViewOutlineProvider getOutlineProvider() {
        return new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                if (path != null) {
                    outline.setConvexPath(path);
                    return;
                }
                outline.setRect(0, 0, width, height);
            }
        };
    }

    protected abstract Path getPath(Rect rect);

}
