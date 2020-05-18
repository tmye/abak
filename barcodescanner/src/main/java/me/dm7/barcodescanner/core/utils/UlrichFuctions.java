package me.dm7.barcodescanner.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

/**
 * By abiguime on 2019/4/16.
 * email: 2597434002@qq.com
 */
public class UlrichFuctions {

    public static Bitmap getBitmapFromLink(Context context, Bitmap kabaBitmap, int QRcodeWidth, int ICON_SIZE, int colorA, int colorB, String text) {

        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    text,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                if (y > bitMatrixHeight / 2) {
                    if (x < bitMatrixHeight / 2) {
                        pixels[offset + x] = bitMatrix.get(x, y) ?
                                colorA : context.getResources().getColor(android.R.color.white);
                    } else {
                        pixels[offset + x] = bitMatrix.get(x, y) ?
                                context.getResources().getColor(android.R.color.black) : context.getResources().getColor(android.R.color.white);
                    }
                } else {
                    if (x < bitMatrixHeight / 2) {
                        pixels[offset + x] = bitMatrix.get(x, y) ?
                                context.getResources().getColor(android.R.color.black) : context.getResources().getColor(android.R.color.white);
                    } else {
                        pixels[offset + x] = bitMatrix.get(x, y) ?
                                colorB : context.getResources().getColor(android.R.color.white);
                    }
                }
            }
        }
        final Bitmap qrBitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        qrBitmap.setPixels(pixels, 0, QRcodeWidth, 0, 0, bitMatrixWidth, bitMatrixHeight);

        // overlay qr bitmap with kaba icon bitmap
//        Bitmap kabaBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.kaba_icon);
        kabaBitmap = Bitmap.createScaledBitmap(kabaBitmap, ICON_SIZE, ICON_SIZE, false);

        final Bitmap bmFusion = Bitmap.createBitmap(qrBitmap.getWidth(), qrBitmap.getHeight(), qrBitmap.getConfig());
        Canvas canvas = new Canvas(bmFusion);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(qrBitmap, new Matrix(), paint);
        canvas.drawBitmap(kabaBitmap, 3 * QRcodeWidth / 4 - ICON_SIZE / 2, 3 * QRcodeWidth / 4 - ICON_SIZE / 2, paint);

        return bmFusion;
    }
}