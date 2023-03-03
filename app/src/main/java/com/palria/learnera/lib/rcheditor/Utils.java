package com.palria.learnera.lib.rcheditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
        mv = {1, 8, 0},
        k = 1,
        d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0004"},
        d2 = {"Lcom/palria/learnera/lib/rcheditor/Utils;", "", "()V", "Companion", "Learn_Era.app.main"}
)
public final class Utils {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker)null);

    private Utils() throws Throwable {
        throw (Throwable)(new InstantiationException("This class is not for instantiation"));
    }

    @Metadata(
            mv = {1, 8, 0},
            k = 1,
            d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ\u000e\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\u0012R\u0011\u0010\u0003\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0013"},
            d2 = {"Lcom/palria/learnera/lib/rcheditor/Utils$Companion;", "", "()V", "currentTime", "", "getCurrentTime", "()J", "decodeResource", "Landroid/graphics/Bitmap;", "context", "Landroid/content/Context;", "resId", "", "toBase64", "", "bitmap", "toBitmap", "drawable", "Landroid/graphics/drawable/Drawable;", "Learn_Era.app.main"}
    )
    public static final class Companion {
        @NotNull
        public final String toBase64(@NotNull Bitmap bitmap) {
            Intrinsics.checkNotNullParameter(bitmap, "bitmap");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, (OutputStream)baos);
            byte[] var10000 = baos.toByteArray();
            Intrinsics.checkNotNullExpressionValue(var10000, "baos.toByteArray()");
            byte[] bytes = var10000;
            String var4 = Base64.encodeToString(bytes, 2);
            Intrinsics.checkNotNullExpressionValue(var4, "Base64.encodeToString(bytes, Base64.NO_WRAP)");
            return var4;
        }

        @NotNull
        public final Bitmap toBitmap(@NotNull Drawable drawable) {
            Intrinsics.checkNotNullParameter(drawable, "drawable");
            if (drawable instanceof BitmapDrawable) {
                Bitmap var10000 = ((BitmapDrawable)drawable).getBitmap();
                Intrinsics.checkNotNullExpressionValue(var10000, "drawable.bitmap");
                return var10000;
            } else {
                int width = drawable.getIntrinsicWidth();
                width = width > 0 ? width : 1;
                int height = drawable.getIntrinsicHeight();
                height = height > 0 ? height : 1;
                Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                Intrinsics.checkNotNullExpressionValue(bitmap, "bitmap");
                return bitmap;
            }
        }

        @NotNull
        public final Bitmap decodeResource(@NotNull Context context, int resId) {
            Intrinsics.checkNotNullParameter(context, "context");
            Bitmap var10000 = BitmapFactory.decodeResource(context.getResources(), resId);
            Intrinsics.checkNotNullExpressionValue(var10000, "BitmapFactory.decodeReso…xt.getResources(), resId)");
            return var10000;
        }

        public final long getCurrentTime() {
            return System.currentTimeMillis();
        }

        private Companion() {
        }

        // $FF: synthetic method
        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
