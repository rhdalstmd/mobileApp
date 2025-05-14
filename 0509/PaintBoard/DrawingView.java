package com.example.paintboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawingView extends View {

    private Path path;
    private Paint drawPaint, canvasPaint;
    private int paintColor = Color.BLACK;
    private Canvas drawCanvas;
    private android.graphics.Bitmap canvasBitmap;
    private float brushSize = 10f;
    private ArrayList<PathInfo> paths = new ArrayList<>();
    private boolean isEraser = false;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        path = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = android.graphics.Bitmap.createBitmap(w, h, android.graphics.Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);

        // 현재 그리고 있는 경로 그리기
        if (!isEraser) {
            canvas.drawPath(path, drawPaint);
        }

        // 저장된 모든 경로 그리기
        for (PathInfo pathInfo : paths) {
            canvas.drawPath(pathInfo.path, pathInfo.paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path = new Path();
                path.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(path, drawPaint);

                // 경로와 페인트 정보 저장
                Paint newPaint = new Paint(drawPaint);
                paths.add(new PathInfo(path, newPaint));

                path = new Path();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void setColor(int newColor) {
        paintColor = newColor;
        drawPaint.setColor(paintColor);
        isEraser = false;
    }

    public void setBrushSize(float newSize) {
        brushSize = newSize;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setEraser(boolean isErase) {
        isEraser = isErase;
        if (isEraser) {
            drawPaint.setColor(Color.WHITE);
        } else {
            drawPaint.setColor(paintColor);
        }
    }

    public void clearCanvas() {
        paths.clear();
        drawCanvas.drawColor(Color.WHITE);
        invalidate();
    }

    // 경로와 페인트 정보를 함께 저장하는 클래스
    private static class PathInfo {
        private Path path;
        private Paint paint;

        PathInfo(Path path, Paint paint) {
            this.path = path;
            this.paint = paint;
        }
    }
}
