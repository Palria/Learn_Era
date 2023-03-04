package com.palria.learnera.lib.rcheditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
        mv = {1, 8, 0},
        k = 1,
        d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0018\u0002\n\u0002\b6\u0018\u0000 l2\u00020\u0001:\u0006klmnopB\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004B\u0019\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0002\u0010\u0007B!\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u001a\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0002J\u0010\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\fH\u0002J\u0006\u0010\u001f\u001a\u00020\u001cJ\u0010\u0010 \u001a\u00020\f2\u0006\u0010!\u001a\u00020\tH\u0002J\f\u0010\"\u001a\u00060#R\u00020\u0000H\u0004J\u0010\u0010$\u001a\u00020\u001c2\u0006\u0010%\u001a\u00020\fH\u0004J\u0006\u0010&\u001a\u00020\u001cJ\u0016\u0010'\u001a\u00020\u001c2\u0006\u0010(\u001a\u00020\f2\u0006\u0010)\u001a\u00020\fJ\u000e\u0010*\u001a\u00020\u001c2\u0006\u0010+\u001a\u00020\fJ\u0016\u0010,\u001a\u00020\u001c2\u0006\u0010-\u001a\u00020\f2\u0006\u0010.\u001a\u00020\fJ\u0006\u0010/\u001a\u00020\u001cJ\u0010\u00100\u001a\u00020\u001c2\u0006\u0010%\u001a\u00020\fH\u0002J\u000e\u00101\u001a\u00020\u001c2\u0006\u00102\u001a\u00020\fJ\u0006\u00103\u001a\u00020\u001cJ\u0006\u00104\u001a\u00020\u001cJ\u0006\u00105\u001a\u00020\u001cJ\u0006\u00106\u001a\u00020\u001cJ\u0006\u00107\u001a\u00020\u001cJ\u0006\u00108\u001a\u00020\u001cJ\u0010\u00109\u001a\u00020\u001c2\u0006\u0010:\u001a\u00020;H\u0016J\u000e\u00109\u001a\u00020\u001c2\u0006\u0010(\u001a\u00020\fJ\u0010\u0010<\u001a\u00020\u001c2\u0006\u0010!\u001a\u00020\tH\u0016J\u0010\u0010=\u001a\u00020\u001c2\u0006\u0010>\u001a\u00020\tH\u0016J\u0006\u0010?\u001a\u00020\u001cJ\u0006\u0010@\u001a\u00020\u001cJ\u0006\u0010A\u001a\u00020\u001cJ\u0006\u0010B\u001a\u00020\u001cJ\u000e\u0010C\u001a\u00020\u001c2\u0006\u0010!\u001a\u00020\tJ\u000e\u0010D\u001a\u00020\u001c2\u0006\u0010!\u001a\u00020\tJ\u000e\u0010E\u001a\u00020\u001c2\u0006\u0010F\u001a\u00020\tJ\u000e\u0010G\u001a\u00020\u001c2\u0006\u0010F\u001a\u00020\tJ\u000e\u0010H\u001a\u00020\u001c2\u0006\u0010F\u001a\u00020\tJ\u000e\u0010I\u001a\u00020\u001c2\u0006\u0010J\u001a\u00020\tJ\u000e\u0010K\u001a\u00020\u001c2\u0006\u0010L\u001a\u00020\fJ\u000e\u0010M\u001a\u00020\u001c2\u0006\u0010N\u001a\u00020\tJ\u0006\u0010O\u001a\u00020\u001cJ\u000e\u0010P\u001a\u00020\u001c2\u0006\u0010Q\u001a\u00020\u0013J\u0006\u0010R\u001a\u00020\u001cJ\u0006\u0010S\u001a\u00020\u001cJ\u0010\u0010T\u001a\u00020\u001c2\b\u0010U\u001a\u0004\u0018\u00010\u0016J\u0010\u0010V\u001a\u00020\u001c2\b\u0010U\u001a\u0004\u0018\u00010\u0018J\u0010\u0010W\u001a\u00020\u001c2\b\u0010U\u001a\u0004\u0018\u00010\u001aJ\u0006\u0010X\u001a\u00020\u001cJ(\u0010Y\u001a\u00020\u001c2\u0006\u0010Z\u001a\u00020\t2\u0006\u0010[\u001a\u00020\t2\u0006\u0010\\\u001a\u00020\t2\u0006\u0010]\u001a\u00020\tH\u0016J(\u0010^\u001a\u00020\u001c2\u0006\u0010_\u001a\u00020\t2\u0006\u0010[\u001a\u00020\t2\u0006\u0010`\u001a\u00020\t2\u0006\u0010]\u001a\u00020\tH\u0016J\u000e\u0010a\u001a\u00020\u001c2\u0006\u0010b\u001a\u00020\fJ\u0006\u0010c\u001a\u00020\u001cJ\u0006\u0010d\u001a\u00020\u001cJ\u0006\u0010e\u001a\u00020\u001cJ\u000e\u0010f\u001a\u00020\u001c2\u0006\u0010!\u001a\u00020\tJ\u000e\u0010g\u001a\u00020\u001c2\u0006\u0010!\u001a\u00020\tJ\u0006\u0010h\u001a\u00020\u001cJ\u0010\u0010i\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\fH\u0002J\u0006\u0010j\u001a\u00020\u001cR(\u0010\r\u001a\u0004\u0018\u00010\f2\b\u0010\u000b\u001a\u0004\u0018\u00010\f8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006q"},
        d2 = {"Lcom/palria/learnera/lib/rcheditor/WYSIWYG;", "Landroid/webkit/WebView;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "contents", "", "html", "getHtml", "()Ljava/lang/String;", "setHtml", "(Ljava/lang/String;)V", "isReady", "", "mContents", "mDecorationStateListener", "Lcom/palria/learnera/lib/rcheditor/WYSIWYG$OnDecorationStateListener;", "mLoadListener", "Lcom/palria/learnera/lib/rcheditor/WYSIWYG$AfterInitialLoadListener;", "mTextChangeListener", "Lcom/palria/learnera/lib/rcheditor/WYSIWYG$OnTextChangeListener;", "applyAttributes", "", "callback", "text", "clearFocusEditor", "convertHexColorString", "color", "createWebviewClient", "Lcom/palria/learnera/lib/rcheditor/WYSIWYG$EditorWebViewClient;", "exec", "trigger", "focusEditor", "insertImage", "url", "alt", "insertLatex", "latexEquation", "insertLink", "href", "title", "insertTodo", "load", "loadCSS", "cssFile", "redo", "removeFormat", "setAlignCenter", "setAlignJustifyFull", "setAlignLeft", "setAlignRight", "setBackground", "background", "Landroid/graphics/drawable/Drawable;", "setBackgroundColor", "setBackgroundResource", "resid", "setBlockquote", "setBold", "setBullets", "setCode", "setEditorBackgroundColor", "setEditorFontColor", "setEditorFontSize", "px", "setEditorHeight", "setEditorWidth", "setFontSize", "fontSize", "setFontType", "font", "setHeading", "heading", "setIndent", "setInputEnabled", "inputEnabled", "setItalic", "setNumbers", "setOnDecorationChangeListener", "listener", "setOnInitialLoadListener", "setOnTextChangeListener", "setOutdent", "setPadding", "left", "top", "right", "bottom", "setPaddingRelative", "start", "end", "setPlaceholder", "placeholder", "setStrikeThrough", "setSubscript", "setSuperscript", "setTextBackgroundColor", "setTextColor", "setUnderline", "stateCheck", "undo", "AfterInitialLoadListener", "Companion", "EditorWebViewClient", "OnDecorationStateListener", "OnTextChangeListener", "Type", "Learn_Era.app.main"}
)
public final class WYSIWYG extends WebView {
    private boolean isReady;
    private String mContents;
    private OnTextChangeListener mTextChangeListener;
    private OnDecorationStateListener mDecorationStateListener;
    private AfterInitialLoadListener mLoadListener;
    private static final String SETUP_HTML = "file:///android_asset/editor.html";
    private static final String CALLBACK_SCHEME = "re-callback://";
    private static final String STATE_SCHEME = "re-state://";
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker)null);

    @NotNull
    protected final EditorWebViewClient createWebviewClient() {
        return new EditorWebViewClient();
    }

    public final void setOnTextChangeListener(@Nullable OnTextChangeListener listener) {
        this.mTextChangeListener = listener;
    }

    public final void setOnDecorationChangeListener(@Nullable OnDecorationStateListener listener) {
        this.mDecorationStateListener = listener;
    }

    public final void setOnInitialLoadListener(@Nullable AfterInitialLoadListener listener) {
        this.mLoadListener = listener;
    }

    private final void callback(String text) {

        CharSequence var2 = (CharSequence)text;
        String var3 = "re-callback://";
        Regex var5 = new Regex(var3);
        String var4 = "";
        this.mContents = var5.replaceFirst(var2, var4);
        if (this.mTextChangeListener != null) {
            OnTextChangeListener var10000 = this.mTextChangeListener;
            Intrinsics.checkNotNull(var10000);
            var10000.onTextChange(this.mContents);
        }

    }

    private final void stateCheck(String text) {
        CharSequence var3 = (CharSequence)text;
        String var4 = "re-state://";
        Regex var10 = new Regex(var4);
        String var5 = "";
        String var8 = var10.replaceFirst(var3, var5);
        Locale var10000 = Locale.ENGLISH;
        Intrinsics.checkNotNullExpressionValue(var10000, "Locale.ENGLISH");
        Locale var11 = var10000;
        String var6 = var8.toUpperCase(var11);
        Intrinsics.checkNotNullExpressionValue(var6, "this as java.lang.String).toUpperCase(locale)");
        String state = var6;
        List types = (List)(new ArrayList());
        Type[] var15 = WYSIWYG.Type.values();
        int var7 = var15.length;

        for(int var13 = 0; var13 < var7; ++var13) {
            Type type = var15[var13];
            if (TextUtils.indexOf((CharSequence)state, (CharSequence)type.name()) != -1) {
                types.add(type);
            }
        }

        if (this.mDecorationStateListener != null) {
            OnDecorationStateListener var14 = this.mDecorationStateListener;
            Intrinsics.checkNotNull(var14);
            var14.onStateChangeListener(state, types);
        }

    }

    private final void applyAttributes(Context context, AttributeSet attrs) {
        int[] attrsArray = new int[]{16842927};
        TypedArray var10000 = context.obtainStyledAttributes(attrs, attrsArray);
        Intrinsics.checkNotNullExpressionValue(var10000, "context.obtainStyledAttributes(attrs, attrsArray)");
        TypedArray ta = var10000;
        int gravity = ta.getInt(0, -1);
        switch (gravity) {
            case 1:
                this.exec("javascript:editor.setTextAlign(\"center\")");
                break;
            case 3:
                this.exec("javascript:editor.setTextAlign(\"left\")");
                break;
            case 5:
                this.exec("javascript:editor.setTextAlign(\"right\")");
                break;
            case 16:
                this.exec("javascript:editor.setVerticalAlign(\"middle\")");
                break;
            case 17:
                this.exec("javascript:editor.setVerticalAlign(\"middle\")");
                this.exec("javascript:editor.setTextAlign(\"center\")");
                break;
            case 48:
                this.exec("javascript:editor.setVerticalAlign(\"top\")");
                break;
            case 80:
                this.exec("javascript:editor.setVerticalAlign(\"bottom\")");
        }

        ta.recycle();
    }

    @Nullable
    public final String getHtml() {
        //has bugs here
        if(this.mContents==null|| this.mContents.equals("")){
            final String[] res = {""};
            this.evaluateJavascript(
                    "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                    new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String html) {
                            Log.e("HTML", html);
                                res[0] = html;
                                mContents=html;
                            // code here
                        }
                    });

            return res[0];
        }else{
            return this.mContents;
        }
    }

    public final void setHtml(@Nullable String contents) {
         contents = contents;
        if (contents == null) {
            contents = "";
        }

        try {
            this.exec("javascript:editor.setHtml('" + URLEncoder.encode(contents, "UTF-8").toString() + "');");
        } catch (UnsupportedEncodingException var4) {
        }

        this.mContents = contents;
    }

    public final void setEditorFontColor(int color) {
        String hex = this.convertHexColorString(color);
        this.exec("javascript:editor.setBaseTextColor('" + hex + "');");
    }

    public final void setEditorFontSize(int px) {
        this.exec("javascript:editor.setBaseFontSize('" + px + "px');");
    }

    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        this.exec("javascript:editor.setPadding('" + left + "px', '" + top + "px', '" + right + "px', '" + bottom + "px');");
    }

    public void setPaddingRelative(int start, int top, int end, int bottom) {
        this.setPadding(start, top, end, bottom);
    }

    public final void setEditorBackgroundColor(int color) {
        this.setBackgroundColor(color);
    }

    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    public void setBackgroundResource(int resid) {
        Utils.Companion var10000 = Utils.Companion;
        Context var10001 = this.getContext();
        Intrinsics.checkNotNullExpressionValue(var10001, "context");
        Bitmap bitmap = var10000.decodeResource(var10001, resid);
        String base64 = Utils.Companion.toBase64(bitmap);
        bitmap.recycle();
        this.exec("javascript:editor.setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
    }

    public void setBackground(@NotNull Drawable background) {
        Intrinsics.checkNotNullParameter(background, "background");
        Bitmap bitmap = Utils.Companion.toBitmap(background);
        String base64 = Utils.Companion.toBase64(bitmap);
        bitmap.recycle();
        this.exec("javascript:editor.setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
    }

    public final void setBackground(@NotNull String url) {
        Intrinsics.checkNotNullParameter(url, "url");
        this.exec("javascript:editor.setBackgroundImage('url(" + url + ")');");
    }

    public final void setEditorWidth(int px) {
        this.exec("javascript:editor.setWidth('" + px + "px');");
    }

    public final void setEditorHeight(int px) {
        this.exec("javascript:editor.setHeight('" + px + "px');");
    }

    public final void setPlaceholder(@NotNull String placeholder) {
        Intrinsics.checkNotNullParameter(placeholder, "placeholder");
        this.exec("javascript:editor.setPlaceholder('" + placeholder + "');");
    }

    public final void setInputEnabled(boolean inputEnabled) {
        this.exec("javascript:editor.setInputEnabled(" + inputEnabled + ')');
    }

    public final void loadCSS(@NotNull String cssFile) {
        Intrinsics.checkNotNullParameter(cssFile, "cssFile");
        String jsCSSImport = "(function() {    var head  = document.getElementsByTagName(\"head\")[0];    var link  = document.createElement(\"link\");    link.rel  = \"stylesheet\";    link.type = \"text/css\";    link.href = \"" + cssFile + "\";" + "    link.media = \"all\";" + "    head.appendChild(link);" + "}) ();";
        this.exec("javascript:" + jsCSSImport);
    }

    public final void undo() {
        this.exec("javascript:editor.undo();");
    }

    public final void redo() {
        this.exec("javascript:editor.redo();");
    }

    public final void setBold() {
        this.exec("javascript:editor.setBold();");
    }

    public final void setItalic() {
        this.exec("javascript:editor.setItalic();");
    }

    public final void setSubscript() {
        this.exec("javascript:editor.setSubscript();");
    }

    public final void setSuperscript() {
        this.exec("javascript:editor.setSuperscript();");
    }

    public final void setStrikeThrough() {
        this.exec("javascript:editor.setStrikeThrough();");
    }

    public final void setUnderline() {
        this.exec("javascript:editor.setUnderline();");
    }

    public final void setTextColor(int color) {
        this.exec("javascript:editor.prepareInsert();");
        String hex = this.convertHexColorString(color);
        this.exec("javascript:editor.setTextColor('" + hex + "');");
    }

    public final void setTextBackgroundColor(int color) {
        this.exec("javascript:editor.prepareInsert();");
        String hex = this.convertHexColorString(color);
        this.exec("javascript:editor.setTextBackgroundColor('" + hex + "');");
    }

    public final void setFontSize(int fontSize) {
        if (fontSize > 7 || fontSize < 1) {
            Log.e("WYSIWYG", "Font size should have a value between 1-7");
        }

        this.exec("javascript:editor.setFontSize('" + fontSize + "');");
    }

    public final void removeFormat() {
        this.exec("javascript:editor.removeFormat();");
    }

    public final void setHeading(int heading) {
        this.exec("javascript:editor.setHeading('" + heading + "');");
    }

    public final void setCode() {
        this.exec("javascript:editor.setCode();");
    }

    public final void setFontType(@NotNull String font) {
        Intrinsics.checkNotNullParameter(font, "font");
        this.exec("javascript:editor.setFontType('" + font + "');");
    }

    public final void setIndent() {
        this.exec("javascript:editor.setIndent();");
    }

    public final void setOutdent() {
        this.exec("javascript:editor.setOutdent();");
    }

    public final void setAlignLeft() {
        this.exec("javascript:editor.setJustifyLeft();");
    }

    public final void setAlignCenter() {
        this.exec("javascript:editor.setJustifyCenter();");
    }

    public final void setAlignJustifyFull() {
        this.exec("javascript:editor.setJustifyFull();");
    }

    public final void setAlignRight() {
        this.exec("javascript:editor.setJustifyRight();");
    }

    public final void setBlockquote() {
        this.exec("javascript:editor.setBlockquote();");
    }

    public final void setBullets() {
        this.exec("javascript:editor.setBullets();");
    }

    public final void setNumbers() {
        this.exec("javascript:editor.setNumbers();");
    }

    public final void insertLatex(@NotNull String latexEquation) {
        Intrinsics.checkNotNullParameter(latexEquation, "latexEquation");
        String newS = "";
        int i = 0;
        int var4 = latexEquation.length() - 2;
        if (i <= var4) {
            while(true) {
                if (latexEquation.charAt(i) != latexEquation.charAt(i + 1) || latexEquation.charAt(i) != ' ') {
                    if (latexEquation.charAt(i) == ' ') {
                        newS = newS + "";
                    } else if (latexEquation.charAt(i) == '\n') {
                        newS = newS + "";
                    } else if (latexEquation.charAt(i) == '\\') {
                        newS = newS + "\\\\";
                    } else {
                        newS = newS + String.valueOf(latexEquation.charAt(i));
                    }
                }

                if (i == var4) {
                    break;
                }

                ++i;
            }
        }

        if (latexEquation.charAt(latexEquation.length() - 1) != ' ') {
            newS = newS + String.valueOf(latexEquation.charAt(latexEquation.length() - 1));
        }

        this.exec("javascript:editor.insertLatex('" + newS + "');");
    }

    public final void insertImage(@NotNull String url, @NotNull String alt) {

        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(alt, "alt");
        this.exec("javascript:editor.prepareInsert();");
        this.exec("javascript:editor.insertImage('" + url + "', '" + alt + "');");
    }


    public final void insertVideo(@NotNull String url) {
        Intrinsics.checkNotNullParameter(url, "url");
        this.exec("javascript:editor.prepareInsert();");
        this.exec("javascript:editor.insertVideo('" + url + "')");
    }

    public final void insertLink(@NotNull String href, @NotNull String title) {
        Intrinsics.checkNotNullParameter(href, "href");
        Intrinsics.checkNotNullParameter(title, "title");
        this.exec("javascript:editor.prepareInsert();");
        this.exec("javascript:editor.insertLink('" + href + "', '" + title + "');");
    }

    public final void insertTodo() {
        this.exec("javascript:editor.prepareInsert();");
        this.exec("javascript:editor.setTodo('" + String.valueOf(Utils.Companion.getCurrentTime()) + "');");
    }

    public final void focusEditor() {
        this.requestFocus();
        this.exec("javascript:editor.focus();");
    }

    public final void clearFocusEditor() {
        this.exec("javascript:editor.blurFocus();");
    }

    private final String convertHexColorString(int color) {
        StringCompanionObject var2 = StringCompanionObject.INSTANCE;
        String var3 = "#%06X";
        Object[] var4 = new Object[]{16777215 & color};
        String var5 = String.format(var3, Arrays.copyOf(var4, var4.length));
        Intrinsics.checkNotNullExpressionValue(var5, "format(format, *args)");
        return var5;
    }

    protected final void exec(@NotNull final String trigger) {
        Intrinsics.checkNotNullParameter(trigger, "trigger");
        if (this.isReady) {
            this.load(trigger);
        } else {
            this.postDelayed((Runnable)(new Runnable() {
                public final void run() {
                    WYSIWYG.this.exec(trigger);
                }
            }), 100L);
        }

    }

    private final void load(String trigger) {
        this.evaluateJavascript(trigger, (ValueCallback)null);
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public WYSIWYG(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Intrinsics.checkNotNullParameter(context, "context");
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
        WebSettings var10000 = this.getSettings();
        Intrinsics.checkNotNullExpressionValue(var10000, "settings");
        var10000.setJavaScriptEnabled(true);
        this.setWebChromeClient(new WebChromeClient());
        this.setWebViewClient((WebViewClient)this.createWebviewClient());
        this.loadUrl("file:///android_asset/editor.html");
        this.applyAttributes(context, attrs);
    }

    public WYSIWYG(@NotNull Context context) {
        this(context, (AttributeSet)null);
        Intrinsics.checkNotNullParameter(context, "context");
        
    }

    public WYSIWYG(@NotNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 16842885);
        Intrinsics.checkNotNullParameter(context, "context");
       
    }

    // $FF: synthetic method
    public static final void access$setMLoadListener$p(WYSIWYG $this, AfterInitialLoadListener var1) {
        $this.mLoadListener = var1;
    }

    @Metadata(
            mv = {1, 8, 0},
            k = 1,
            d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0014\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013j\u0002\b\u0014¨\u0006\u0015"},
            d2 = {"Lcom/palria/learnera/lib/rcheditor/WYSIWYG$Type;", "", "(Ljava/lang/String;I)V", "BOLD", "ITALIC", "SUBSCRIPT", "SUPERSCRIPT", "STRIKETHROUGH", "UNDERLINE", "H1", "H2", "H3", "H4", "H5", "H6", "ORDEREDLIST", "UNORDEREDLIST", "JUSTIFYCENTER", "JUSTIFYFULL", "JUSTUFYLEFT", "JUSTIFYRIGHT", "Learn_Era.app.main"}
    )
    public static enum Type {
        BOLD,
        ITALIC,
        SUBSCRIPT,
        SUPERSCRIPT,
        STRIKETHROUGH,
        UNDERLINE,
        H1,
        H2,
        H3,
        H4,
        H5,
        H6,
        ORDEREDLIST,
        UNORDEREDLIST,
        JUSTIFYCENTER,
        JUSTIFYFULL,
        JUSTUFYLEFT,
        JUSTIFYRIGHT;
    }

    @Metadata(
            mv = {1, 8, 0},
            k = 1,
            d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H&¨\u0006\u0006"},
            d2 = {"Lcom/palria/learnera/lib/rcheditor/WYSIWYG$OnTextChangeListener;", "", "onTextChange", "", "text", "", "Learn_Era.app.main"}
    )
    public interface OnTextChangeListener {
        void onTextChange(@Nullable String var1);
    }

    @Metadata(
            mv = {1, 8, 0},
            k = 1,
            d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\"\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u000e\u0010\u0006\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0007H&¨\u0006\t"},
            d2 = {"Lcom/palria/learnera/lib/rcheditor/WYSIWYG$OnDecorationStateListener;", "", "onStateChangeListener", "", "text", "", "types", "", "Lcom/palria/learnera/lib/rcheditor/WYSIWYG$Type;", "Learn_Era.app.main"}
    )
    public interface OnDecorationStateListener {
        void onStateChangeListener(@Nullable String var1, @Nullable List var2);
    }

    @Metadata(
            mv = {1, 8, 0},
            k = 1,
            d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0006"},
            d2 = {"Lcom/palria/learnera/lib/rcheditor/WYSIWYG$AfterInitialLoadListener;", "", "onAfterInitialLoad", "", "isReady", "", "Learn_Era.app.main"}
    )
    public interface AfterInitialLoadListener {
        void onAfterInitialLoad(boolean var1);
    }

    @Metadata(
            mv = {1, 8, 0},
            k = 1,
            d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0084\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\u000b"},
            d2 = {"Lcom/palria/learnera/lib/rcheditor/WYSIWYG$EditorWebViewClient;", "Landroid/webkit/WebViewClient;", "(Lcom/palria/learnera/lib/rcheditor/WYSIWYG;)V", "onPageFinished", "", "view", "Landroid/webkit/WebView;", "url", "", "shouldOverrideUrlLoading", "", "Learn_Era.app.main"}
    )
    protected final class EditorWebViewClient extends WebViewClient {
        public void onPageFinished(@NotNull WebView view, @NotNull String url) {
            Intrinsics.checkNotNullParameter(view, "view");
            Intrinsics.checkNotNullParameter(url, "url");
            WYSIWYG.this.isReady = StringsKt.equals(url, "file:///android_asset/editor.html", true);
            if (WYSIWYG.this.mLoadListener != null) {
                AfterInitialLoadListener var10000 = WYSIWYG.this.mLoadListener;
                Intrinsics.checkNotNull(var10000);
                var10000.onAfterInitialLoad(WYSIWYG.this.isReady);
            }

        }

        public boolean shouldOverrideUrlLoading(@NotNull WebView view, @NotNull String url) {
            Intrinsics.checkNotNullParameter(view, "view");
            Intrinsics.checkNotNullParameter(url, "url");
            String decode = null;

            String var4;
            try {
                String var10000 = URLDecoder.decode(url, "UTF-8");
                Intrinsics.checkNotNullExpressionValue(var10000, "URLDecoder.decode(url, \"UTF-8\")");
                var4 = var10000;
            } catch (UnsupportedEncodingException var6) {
                return false;
            }

            if (TextUtils.indexOf((CharSequence)url, (CharSequence)"re-callback://") == 0) {
                WYSIWYG.this.callback(var4);
                return true;
            } else if (TextUtils.indexOf((CharSequence)url, (CharSequence)"re-state://") == 0) {
                WYSIWYG.this.stateCheck(var4);
                return true;
            } else {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }

        public EditorWebViewClient() {
        }
    }

    @Metadata(
            mv = {1, 8, 0},
            k = 1,
            d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0007"},
            d2 = {"Lcom/palria/learnera/lib/rcheditor/WYSIWYG$Companion;", "", "()V", "CALLBACK_SCHEME", "", "SETUP_HTML", "STATE_SCHEME", "Learn_Era.app.main"}
    )
    public static final class Companion {
        private Companion() {
        }

        // $FF: synthetic method
        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
