package com.palria.learnera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.util.IOUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobalHelpers {


    static private AlertDialog loadingAlertDialog;

    /**
     *
     * @param type success | failed | warning
     * @param c context
     * @param title Alert title to show
     * @param message Alert body/message to show
     */

    public static void showAlertMessage(String type, Context c, String title, String message){
        type = type.toLowerCase();
        LayoutInflater layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alert_view = layoutInflater.inflate(R.layout.custom_alert_box,null);
        ImageView iconView = alert_view.findViewById(R.id.icon);

        //default
        iconView.setBackgroundResource(R.drawable.ic_baseline_warning_amber_24);
        ColorStateList colorStateList = null;

        TextView titleView = alert_view.findViewById(R.id.title);
        titleView.setText(title);

        TextView messageView = alert_view.findViewById(R.id.message);
        messageView.setText(message);

        //set icon according to the type
        switch (type){
            case "success":
                iconView.setBackgroundResource(R.drawable.ic_baseline_check_circle_outline_24);
               colorStateList= new ColorStateList(
                        new int[][]{ new int[]{android.R.attr.state_pressed},
                                // pressed
                                new int[]{},
                                // default
                                new int[]{}
                        },
                        new int[]{ c.getColor(R.color.success_green),
                                // pressed
                                c.getColor(R.color.success_green),
                                // default
                                c.getColor(R.color.success_green)
                        });
                iconView.setBackgroundTintList(colorStateList);
                break;

            case "error":
                iconView.setBackgroundResource(R.drawable.ic_baseline_error_outline_24);
                colorStateList= new ColorStateList(
                        new int[][]{ new int[]{android.R.attr.state_pressed},
                                // pressed
                                new int[]{},
                                // default
                                new int[]{}
                        },
                        new int[]{ c.getColor(R.color.error_red),
                                // pressed
                                c.getColor(R.color.error_red),
                                // default
                                c.getColor(R.color.error_red)
                        });
                iconView.setBackgroundTintList(colorStateList);
                break;

            case "warning":
                iconView.setBackgroundResource(R.drawable.ic_baseline_warning_amber_24);
                colorStateList= new ColorStateList(
                        new int[][]{ new int[]{android.R.attr.state_pressed},
                                // pressed
                                new int[]{},
                                // default
                                new int[]{}
                        },
                        new int[]{ c.getColor(R.color.color_warning),
                                // pressed
                                c.getColor(R.color.color_warning),
                                // default
                                c.getColor(R.color.color_warning)
                        });
                iconView.setBackgroundTintList(colorStateList);
                break;
            default:
                break;
        }


    //show mesage
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(c);
        dialog.setView(alert_view);


        dialog.show();
    }


    public static String calculateAverageRating(int[] ratings){

        int totalStarsCount=0;

        for(int r : ratings){
            totalStarsCount+=r;
        }
        double average_rate = (ratings[0] + 2 * ratings[1] + 3 * ratings[2] + 4 * ratings[3] + 5 * ratings[4]) / (totalStarsCount+0.0);

        DecimalFormat df = new DecimalFormat("#.#");
        String rating_string_average =  df.format(average_rate);
        return rating_string_average;
    }

//url validator

    /**
     *
     * @param url to match/test
     * @return boolean (true if valid url otherwise false)
     */
    public static boolean isValidUrl(String url) {
        // Regular expression pattern to match URLs
        Pattern pattern = Pattern.compile("^(http|https)://[a-z0-9]+([-.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    public static String uriToBase64(String link, Context context) throws IOException {
        Uri uri = Uri.parse(link);
        InputStream inputStream = context.getContentResolver().openInputStream(uri);

        byte[] bytes = IOUtils.toByteArray(inputStream);

        String base64String = Base64.encodeToString(bytes, Base64.DEFAULT);
        return base64String;

    }

    public static String uriToFullPathUri(String u, Context context){
        Uri uri = Uri.parse(u);

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range")
            String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            String fileUrl = Uri.fromFile(new File(filePath)).toString();
            cursor.close();
            // use the fileUrl as needed
            return filePath;
        }
        return null;
    }

    // Helper method to get a data URI from a Bitmap
    public static Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

}
