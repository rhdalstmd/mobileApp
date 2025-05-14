package com.example.paintboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.paintboard.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private DrawingView drawingView;
    private int currentColor = Color.BLACK;
    private float currentStrokeWidth = 10f;

    // 색상 배열
    private final int[] colors = new int[] {
            Color.BLACK, Color.RED, Color.GREEN, Color.BLUE,
            Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.DKGRAY,
            Color.GRAY, Color.LTGRAY, Color.WHITE
    };

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawingView = findViewById(R.id.drawingView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_color) {
            openColorPickerDialog();
            return true;
        } else if (itemId == R.id.action_stroke) {
            openStrokeWidthDialog();
            return true;
        } else if (itemId == R.id.action_eraser) {
            drawingView.setEraser(true);
            return true;
        } else if (itemId == R.id.action_clear) {
            drawingView.clearCanvas();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void openColorPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("색상 선택");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 40, 40, 40);

        LinearLayout row = null;
        int buttonsPerRow = 5;

        for (int i = 0; i < colors.length; i++) {
            if (i % buttonsPerRow == 0) {
                row = new LinearLayout(this);
                row.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.addView(row);
            }

            final int colorIndex = i;
            final android.widget.Button colorBtn = new android.widget.Button(this);
            colorBtn.setBackgroundColor(colors[i]);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, 100, 1.0f);
            params.setMargins(5, 5, 5, 5);
            colorBtn.setLayoutParams(params);

            colorBtn.setOnClickListener(v -> {
                currentColor = colors[colorIndex];
                drawingView.setColor(currentColor);
                if (dialog != null) dialog.dismiss();
            });

            row.addView(colorBtn);
        }

        builder.setView(layout);
        builder.setNegativeButton("취소", null);
        dialog = builder.create();
        dialog.show();
    }

    private void openStrokeWidthDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_stroke_width);
        dialog.setTitle("선 굵기 선택");

        final SeekBar seekBar = dialog.findViewById(R.id.seekBarStrokeWidth);
        final TextView textViewStrokeWidth = dialog.findViewById(R.id.textViewStrokeWidth);
        final android.widget.Button buttonApply = dialog.findViewById(R.id.buttonApply);

        seekBar.setProgress((int) currentStrokeWidth);
        textViewStrokeWidth.setText(String.valueOf((int) currentStrokeWidth));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewStrokeWidth.setText(String.valueOf(progress));
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        buttonApply.setOnClickListener(v -> {
            currentStrokeWidth = seekBar.getProgress();
            drawingView.setBrushSize(currentStrokeWidth);
            dialog.dismiss();
        });

        dialog.show();
    }
}
