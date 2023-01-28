package com.palria.learnera;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import java.util.Map;

public class GlobalHelpers {

    /**
     *
     * @param type success | failed | warning
     * @param c context
     * @param title Alert title to show
     * @param message Alert body/message to show
     */
    public static void showAlertMessage(String type, Context c, String title, String message){

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
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setView(alert_view)
                .create();

        dialog.show();
    }

}
