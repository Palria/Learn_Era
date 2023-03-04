package com.palria.learnera;


import static com.google.android.exoplayer2.ExoPlayerLibraryInfo.TAG;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.palria.learnera.lib.rcheditor.Utils;
import com.palria.learnera.lib.rcheditor.WYSIWYG;
import com.palria.learnera.widgets.BottomSheetFormBuilderWidget;
import com.palria.learnera.widgets.LEBottomSheetDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class UploadPageActivity extends AppCompatActivity {


    String pageId;
    String libraryId;
    String tutorialId;
    String folderId;
    LinearLayout containerLinearLayout;
    ImageButton addImageActionButton ;
    ImageButton addTodoListActionButton ;
    ImageButton addTableActionButton ;
    FloatingActionButton btn ;
    EditText pageTitleEditText;
    String pageTitle;
    String pageContent;
    boolean isTutorialPage = true;
    /**
     * A  variable for launching the gallery {@link Intent}
     * */
    ActivityResultLauncher<Intent> openGalleryLauncher;

    /**
     * A  variable for launching the camera {@link Intent}
     * */
    ActivityResultLauncher<Intent> openCameraLauncher;

    int CAMERA_PERMISSION_REQUEST_CODE = 2002;
    int GALLERY_PERMISSION_REQUEST_CODE = 23;
    int imageDisplayPosition = 0;
    int validPosition = 1;
    ImageView receiverImage;
    LinearLayout receiverLinearLayout;
    View currentFocusView;
    /**
     * loading alert dialog
     *
     */
    AlertDialog alertDialog;
//editor views

    WYSIWYG wysiwygEditor;
    ImageView action_undo;
    ImageView action_redo;
    ImageView action_bold;
    ImageView action_italic;
    ImageView action_subscript;
    ImageView action_superscript;
    ImageView action_strikethrough;
    ImageView action_underline;
    ImageView action_heading1;
      ImageView action_heading2;
        ImageView action_heading3;
        ImageView action_heading4;
        ImageView action_heading5;
        ImageView action_heading6;
         ImageView action_txt_color;
          ImageView  action_bg_color;
          ImageView  action_indent;
        ImageView action_align_left;
    ImageView action_align_center;
    ImageView action_align_right;
    ImageView action_align_justify;
    ImageView action_blockquote;
    ImageView  action_insert_bullets;
    ImageView action_insert_numbers;
     ImageView action_insert_image;
        ImageView action_insert_link;
       ImageView action_insert_checkbox;
    boolean visible = false;
        ImageView preview;
        ImageView insert_latex;
        ImageView insert_code;
       ImageView action_change_font_type;
       HorizontalScrollView latex_editor;
       Button submit_latex;
       EditText latex_equation;
       ImageView action_insert_video;

    LEBottomSheetDialog choosePhotoPickerModal;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page);

        initUI();
        fetchIntentData();
        pageId = GlobalConfig.getRandomString(60);

        openGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null) {
                    Intent data=result.getData();

                    //upload this image when uploading the page as index
                    //insert image to
                    wysiwygEditor.insertImage(data.getData().toString(),"-");

                    return;

////                    galleryImageUri = data.getData();
////                        Picasso.get().load(galleryImageUri)
////                                .centerCrop()
////                                .into(pickImageActionButton);
//                    ImageView partitionImage = new ImageView(getApplicationContext());
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(70,70);
//                    partitionImage.setLayoutParams(layoutParams);
//
//                   ImageView image = getImage();
//                    Glide.with(UploadPageActivity.this)
//                            .load(data.getData())
//                            .centerCrop()
//                            .into(image);
//                    if(containerLinearLayout.getChildCount() == (containerLinearLayout.indexOfChild(currentFocusView)+1)) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                             //   createPartition();
//
//                            }
//                        });
                    }

//                    isLibraryCoverPhotoIncluded = true;
//                    isLibraryCoverPhotoChanged = true;



            }
        });
        openCameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK) {

                    if (result.getData() != null) {
                        Intent data = result.getData();
                        Bitmap  bitmapFromCamera =(Bitmap) data.getExtras().get("data");


                        if(bitmapFromCamera != null) {
                            String imageUriString = GlobalHelpers.getImageUri(UploadPageActivity.this, bitmapFromCamera).toString();
                            //insert image also upload image in uploading page with indexing.
                            wysiwygEditor.insertImage(imageUriString,"-");
//                            cameraImageBitmap = bitmapFromCamera;
                            //coverPhotoImageView.setImageBitmap(cameraImageBitmap);

//                            ImageView partitionImage = new ImageView(getApplicationContext());
//                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(70,70);
//                            partitionImage.setLayoutParams(layoutParams);
//                            receiverLinearLayout.addView(partitionImage);
//
//                            ImageView image = getImage();
//
//                            Glide.with(UploadPageActivity.this)
//                                    .load(bitmapFromCamera)
//                                    .centerCrop()
//                                    .into(image);
//                            if(containerLinearLayout.getChildCount() == (containerLinearLayout.indexOfChild(receiverLinearLayout)+1)) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                      //  createPartition();
//
//                                    }
//                                });
//                            }
//                            isLibraryCoverPhotoIncluded = true;
//                            isLibraryCoverPhotoChanged = true;
                        }

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No image captured!", Toast.LENGTH_SHORT).show();
                }
            }
        });
//
//        tutorialId  = "TEST_ID-3";
//        folderId  = "TEST_ID-3";
//        pageId  = "TEST_ID-3";


        startService(new Intent(getApplicationContext(),UploadPageManagerService.class));
        addImageActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalConfig.createPopUpMenu(getApplicationContext(), R.menu.pick_image_menu, addImageActionButton, new GlobalConfig.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClicked(MenuItem item) {
                        if(item.getItemId() == R.id.galleryId)openGallery();
                        else if(item.getItemId() == R.id.cameraId)openCamera();

                        return true;
                    }
                });

            }
        });
        addTodoListActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTodoGroup();
            }
        });

        addTableActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
createTable();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the datas first to valid them
                wysiwygEditor.evaluateJavascript(
                        "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String html) {
                                pageContent = html;
                                pageTitle = pageTitleEditText.getText().toString();

                                if(validateForm()){
                                    //if validate form reeturns error/false
                                    return;
                                }
                                Log.e("html",html);
                                //post here (just title and page content )
                                //postPage();
                                //uploadPage();
                            }
                        });



            }
        });

    }

    private boolean validateForm() {

        if(pageTitleEditText.getText().toString().trim().equals("")){
            pageTitleEditText.setError("Please enter a title for a page.");
            pageTitleEditText.requestFocus();
            return true;
        }

        if(pageContent == null || pageContent.equals("")){
           Toast.makeText(UploadPageActivity.this, "Please enter a description.",Toast.LENGTH_LONG).show();
           return true;
        }
        return false;
    }

    private void initUI(){

    pageTitleEditText = findViewById(R.id.pageTitleEditTextId);
    //containerLinearLayout = findViewById(R.id.containerLinearLayoutId);
    addImageActionButton = findViewById(R.id.addImageActionButtonId);
    addTodoListActionButton = findViewById(R.id.addTodoListActionButtonId);
    addTableActionButton = findViewById(R.id.addTableActionButtonId);
    btn = findViewById(R.id.btn);
    //addEditText(0);
    //createPartition();


/**load the content editor for the user.
 change any in this method here
 */
    loadContentEditor();

    //init progress.
    alertDialog = new AlertDialog.Builder(UploadPageActivity.this)
            .setCancelable(false)
            .setView(getLayoutInflater().inflate(R.layout.default_loading_layout,null))
            .create();
}

    private void loadContentEditor() {
        //load the ocntent editor here and render there thats make it easy to handle without any external codes.
        wysiwygEditor=findViewById(R.id.editor);
        wysiwygEditor.setEditorHeight(600);
        wysiwygEditor.setEditorFontSize(16);
        wysiwygEditor.setPadding(10, 10, 10, 10);
        wysiwygEditor.setPlaceholder("Insert your content here...");

         action_undo=findViewById(R.id.action_undo);
         action_redo=findViewById(R.id.action_redo);
         action_bold=findViewById(R.id.action_bold);
         action_italic=findViewById(R.id.action_italic);
         action_subscript=findViewById(R.id.action_subscript);
         action_superscript=findViewById(R.id.action_superscript);
         action_strikethrough=findViewById(R.id.action_strikethrough);
         action_underline=findViewById(R.id.action_underline);
         action_heading1=findViewById(R.id.action_heading1);
         action_heading2=findViewById(R.id.action_heading2);
         action_heading3=findViewById(R.id.action_heading3);
         action_heading4=findViewById(R.id.action_heading4);
         action_heading5=findViewById(R.id.action_heading5);
         action_heading6=findViewById(R.id.action_heading6);
         action_txt_color=findViewById(R.id.action_txt_color);
          action_bg_color=findViewById(R.id.action_bg_color);
          action_indent=findViewById(R.id.action_indent);
         action_align_left=findViewById(R.id.action_align_left);
         action_align_center=findViewById(R.id.action_align_center);
         action_align_right=findViewById(R.id.action_align_right);
         action_align_justify=findViewById(R.id.action_align_justify);
         action_blockquote=findViewById(R.id.action_blockquote);
          action_insert_bullets=findViewById(R.id.action_insert_bullets);
         action_insert_numbers=findViewById(R.id.action_insert_numbers);
         action_insert_image=findViewById(R.id.action_insert_image);
         action_insert_link=findViewById(R.id.action_insert_link);
         action_insert_checkbox=findViewById(R.id.action_insert_checkbox);
         preview=findViewById(R.id.preview);
         insert_latex=findViewById(R.id.insert_latex);
         insert_code=findViewById(R.id.insert_code);
         action_change_font_type=findViewById(R.id.action_change_font_type);
         latex_editor=findViewById(R.id.latext_editor);
         submit_latex=findViewById(R.id.submit_latex);
         latex_equation=findViewById(R.id.latex_equation);

        action_insert_video=findViewById(R.id.action_insert_video);


        choosePhotoPickerModal= new LEBottomSheetDialog(UploadPageActivity.this)
                .addOptionItem("Camera", R.drawable.ic_baseline_photo_camera_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
choosePhotoPickerModal.hide();
openCamera();
                    }
                },0)
                .addOptionItem("Gallery", R.drawable.baseline_insert_photo_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        choosePhotoPickerModal.hide();
                        openGallery();
                    }
                },0)
                .addOptionItem("URL", R.drawable.baseline_link_24, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        choosePhotoPickerModal.hide();

                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.single_edit_text_layout, null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadPageActivity.this);
                        builder.setView(dialogView);
                        builder.setTitle("Insert Photo from url:")
                                .setMessage("Enter or paste photo url to insert");

                        builder.setPositiveButton("Insert", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // handle OK button click
                                EditText editText = dialogView.findViewById(R.id.edit_text);
                                String inputText = editText.getText().toString();
                                // do something with the input text
                                if(GlobalHelpers.isValidUrl(inputText)){
                                    wysiwygEditor.insertImage(inputText,"");
                                }

                            }
                        });

                        builder.setNegativeButton("Cancel", null);

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                },0)
                .render();



        action_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.undo();
            }
        });

        action_redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.redo();
            }
        });

        action_bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setBold();
            }
        });

        action_italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setItalic();
            }
        });

        action_subscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setSubscript();
            }
        });

        action_superscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setSuperscript();
            }
        });

        action_strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setStrikeThrough();
            }
        });

        action_underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setUnderline();
            }
        });

        action_heading1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setHeading(1);
            }
        });


        action_heading2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setHeading(2);
            }
        });
        action_heading3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setHeading(3);
            }
        });

        action_heading4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setHeading(4);
            }
        });

        action_heading5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setHeading(5);
            }
        });

        action_heading6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setHeading(6);
            }
        });

        action_txt_color.setOnClickListener(new View.OnClickListener() {
            boolean isChanged = false;
            @Override
            public void onClick(View view) {
                wysiwygEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
//                isChanged = !isChanged
            }
        });



        action_bg_color.setOnClickListener(new View.OnClickListener() {
            private boolean isChanged = false;
            @Override
            public void onClick(View view) {
                wysiwygEditor.setTextBackgroundColor( isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });


        action_indent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setIndent();
            }
        });


        action_align_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setAlignLeft();
            }
        });

        action_align_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setAlignCenter();
            }
        });

        action_align_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setAlignRight();
            }
        });

        action_align_justify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setAlignJustifyFull();
            }
        });

        action_blockquote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setBlockquote();
            }
        });

        action_insert_bullets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setBullets();
            }
        });

        action_insert_numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setNumbers();
            }
        });

        action_insert_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhotoPickerModal.show();
//                wysiwygEditor.insertImage(
//                        "https://i.postimg.cc/JzL891Fm/maxresdefault.jpg",
//                        "Night Sky"
//                );
            }
        });

        action_insert_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.insertLink(
                        "https://github.com/onecode369",
                        "One Code"
                );
            }
        });

        action_insert_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.insertTodo();
            }
        });


        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!visible){
                    wysiwygEditor.setInputEnabled(false);
                    preview.setImageResource(R.drawable.visibility_off);
                }else{
                    wysiwygEditor.setInputEnabled(true);
                    preview.setImageResource(R.drawable.visibility);
                }
                visible = !visible;
            }
        });


        action_insert_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                wysiwygEditor.insertVideo("https://www.youtube.com/watch?v=ZChsMjm5-mk");
               // wysiwygEditor.insertVideo("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4");
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.single_edit_text_layout, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(UploadPageActivity.this);
                builder.setView(dialogView);
                builder.setTitle("Insert video url:")
                                .setMessage("enter or paste video url to insert");

                builder.setPositiveButton("Insert", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // handle OK button click
                        EditText editText = dialogView.findViewById(R.id.edit_text);
                        String inputText = editText.getText().toString();
                        // do something with the input text
                        if(GlobalHelpers.isValidUrl(inputText)){
                            wysiwygEditor.insertVideo(inputText);
                        }

                    }
                });

                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });



        insert_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setCode();
            }
        });

        insert_latex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(latex_editor.getVisibility() == View.GONE) {
                    latex_editor.setVisibility(View.VISIBLE);
                    submit_latex.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            wysiwygEditor.insertLatex(latex_equation.toString());
                        }
                    });
                }else{
                    latex_editor.setVisibility(View.GONE);
                }
            }
        });

        action_change_font_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wysiwygEditor.setFontType("Times New Roman");

            }
        });


    }



    private void fetchIntentData(){
        Intent intent = getIntent();
        isTutorialPage = intent.getBooleanExtra(GlobalConfig.IS_TUTORIAL_PAGE_KEY,true);
        tutorialId = intent.getStringExtra(GlobalConfig.TUTORIAL_ID_KEY);
        libraryId = intent.getStringExtra(GlobalConfig.LIBRARY_ID_KEY);
        folderId = intent.getStringExtra(GlobalConfig.FOLDER_ID_KEY);

}


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(getApplicationContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

            if (requestCode == CAMERA_PERMISSION_REQUEST_CODE){
                fireCameraIntent();
            }

            if (requestCode == GALLERY_PERMISSION_REQUEST_CODE){
                fireGalleryIntent();
            }

        }
    }

    public void openGallery(){
        requestForPermissionAndPickImage(GALLERY_PERMISSION_REQUEST_CODE);

    }
    public void openCamera(){
        requestForPermissionAndPickImage(CAMERA_PERMISSION_REQUEST_CODE);
    }

    public void fireGalleryIntent(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        openGalleryLauncher.launch(galleryIntent);
    }
    public void fireCameraIntent(){
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraLauncher.launch(cameraIntent);
    }
    public void requestForPermissionAndPickImage(int requestCode){
        if(getApplicationContext().checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED || getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},requestCode);
        }else{
            if(requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
                fireCameraIntent();
            }
            if(requestCode == GALLERY_PERMISSION_REQUEST_CODE) {
                fireGalleryIntent();
            }
        }



    }

    private void toggleProgress(boolean show)
    {
        if(show){
            alertDialog.show();
        }else{
            alertDialog.cancel();
        }
    }

    void createPartition(){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View partitionView  =  layoutInflater.inflate(R.layout.page_partition_layout,containerLinearLayout,false);
        EditText bodyEditText = partitionView.findViewById(R.id.bodyEditTextId);

        LinearLayout imageLinearLayout = partitionView.findViewById(R.id.imageLinearLayoutId);
        ImageButton addImageActionButton = partitionView.findViewById(R.id.addImageActionButtonId);

        addImageActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiverLinearLayout = imageLinearLayout;
                GlobalConfig.createPopUpMenu(getApplicationContext(), R.menu.pick_image_menu, addImageActionButton, new GlobalConfig.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClicked(MenuItem item) {
                        if(item.getItemId() == R.id.galleryId)openGallery();
                        else if(item.getItemId() == R.id.cameraId)openCamera();
                        currentFocusView = partitionView;

                        return true;
                    }
                });
                addTodoGroup();
            }
        });
        containerLinearLayout.addView(partitionView);
    }

    void addEditText(int validPosition){

                    EditText editText = new EditText(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    editText.setLayoutParams(layoutParams);
                    ColorDrawable white = new ColorDrawable();
                    white.setColor(getResources().getColor(R.color.white));
                    editText.setBackground(white);
                  editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                      @Override
                      public void onFocusChange(View view, boolean b) {
                          UploadPageActivity.this.validPosition = containerLinearLayout.indexOfChild(editText) + 1;

                      }
                  });
//                    editText.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            UploadPageActivity.this.validPosition = containerLinearLayout.indexOfChild(editText) + 1;
//                        }
//                    });
                    containerLinearLayout.addView(editText,validPosition);
//        UploadPageActivity.this.validPosition = containerLinearLayout.indexOfChild(editText) + 1;


    }

    private ImageView getImage(){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View imageView  =  layoutInflater.inflate(R.layout.page_image_layout,containerLinearLayout,false);
        ImageView image = imageView.findViewById(R.id.imageViewId);
        ImageView removeImage = imageView.findViewById(R.id.removeImageId);

        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText textBefore = (EditText) containerLinearLayout.getChildAt(containerLinearLayout.indexOfChild(imageView)-1);
                EditText textAfter = (EditText) containerLinearLayout.getChildAt(containerLinearLayout.indexOfChild(imageView)+1);

                textBefore.append(textAfter.getText().toString());
                containerLinearLayout.removeView(textAfter);
                containerLinearLayout.removeView(imageView);
            }
        });
        containerLinearLayout.addView(imageView,validPosition);
        addEditText(containerLinearLayout.indexOfChild(imageView)+1);
        return image;
    }

    void uploadPage(){
        ArrayList<ArrayList<String>> allPageTextPartitionsDataDetailsArrayList = new ArrayList<>();

        UploadPageManagerService.addUploadListeners(new UploadPageManagerService.OnPageUploadListener() {
            @Override
            public void onNewPage(String pageId) {
                Toast.makeText(getApplicationContext(), "New page id: "+ pageId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String pageId) {
                Toast.makeText(getApplicationContext(), "page upload failed: "+ pageId, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onProgress(String pageId, int progressCount) {
                Toast.makeText(getApplicationContext(), "New page uploading: "+ pageId, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(String pageId) {
                Toast.makeText(getApplicationContext(), "New page upload succeeded: "+ pageId, Toast.LENGTH_SHORT).show();

            }
        });
        UploadPageManagerService.setInitialVariables(pageId);

        for(int i=0; i<containerLinearLayout.getChildCount(); i++){
            String partitionId = GlobalConfig.getRandomString(10) + GlobalConfig._IS_PARTITION_ID_IS_FOR_IDENTIFYING_PARTITIONS_KEY;

            LinearLayout partitionLinearLayout = (LinearLayout) containerLinearLayout.getChildAt(i);
            TextView textDataPartitionTextView =(TextView) partitionLinearLayout.getChildAt(0);
            String textDataPartition = textDataPartitionTextView.getText().toString();
            ArrayList<String>textPartitionsDataDetailsArrayList = new ArrayList<>();
            if(!textDataPartition.isEmpty()){
                textPartitionsDataDetailsArrayList.add(textDataPartition);

            }
            if(!textPartitionsDataDetailsArrayList.contains(partitionId)){
                textPartitionsDataDetailsArrayList.add(partitionId);
            }
            //add text partition array to the general page partitions array
            allPageTextPartitionsDataDetailsArrayList.add(textPartitionsDataDetailsArrayList);

            LinearLayout imageLinearLayout =(LinearLayout) partitionLinearLayout.getChildAt(1);
            ArrayList<byte[]> imagePartitionByteArrayList = new ArrayList<>();

            if(imageLinearLayout.getChildCount() > 0) {
                for (int j = 0; j < imageLinearLayout.getChildCount(); j++) {
                    ConstraintLayout constraintLayout = (ConstraintLayout) imageLinearLayout.getChildAt(1);
                    ImageView imagePartitionImageView = (ImageView) constraintLayout.findViewById(R.id.imageViewId);
                    imagePartitionImageView.setDrawingCacheEnabled(true);
                    Bitmap bitmap = imagePartitionImageView.getDrawingCache();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    imagePartitionByteArrayList.add(bytes);
                }
            }
            //call the service method for uploading partition images.
            UploadPageManagerService.uploadPartitionImageDataToPage(libraryId,tutorialId,pageId,partitionId,imagePartitionByteArrayList,"IMAGE_PARTITION_ARRAY_"+i,containerLinearLayout.getChildCount());

        }
        //call the service method here for uploading text data partition at once
        UploadPageManagerService.uploadPartitionTextDataToPage(libraryId,tutorialId,pageId,allPageTextPartitionsDataDetailsArrayList,containerLinearLayout.getChildCount());

    }

    void addTodoItem(View todoGroupView,LinearLayout linearLayout){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View todoItemView  =  layoutInflater.inflate(R.layout.page_to_do_item_layout,linearLayout,false);
        ImageView removeItem  =  todoItemView.findViewById(R.id.removeTodoItemActionImageId);
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(linearLayout.getChildCount()==1){

                    EditText textBefore = (EditText) containerLinearLayout.getChildAt(containerLinearLayout.indexOfChild(todoGroupView)-1);
                    EditText textAfter = (EditText) containerLinearLayout.getChildAt(containerLinearLayout.indexOfChild(todoGroupView)+1);

                    textBefore.append(textAfter.getText().toString());
                    containerLinearLayout.removeView(textAfter);
                    containerLinearLayout.removeView(todoGroupView);
                }else{
                    linearLayout.removeView(todoItemView);
                }

            }
        });
        linearLayout.addView(todoItemView);
    }

 void addTodoGroup(){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View todoGroupView  =  layoutInflater.inflate(R.layout.page_to_do_group_layout,containerLinearLayout,false);
        LinearLayout todoLinearLayout = todoGroupView.findViewById(R.id.todoItemLinearLayoutId);
        ImageView addMoreItemActionButton = todoGroupView.findViewById(R.id.addMoreItemActionButtonId);
     addTodoItem(todoGroupView,todoLinearLayout);

     addMoreItemActionButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             addTodoItem(todoGroupView,todoLinearLayout);
         }
     });

        containerLinearLayout.addView(todoGroupView,validPosition);

     addEditText(containerLinearLayout.indexOfChild(todoGroupView)+1);

 }
void createTable(){
    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View tableView  =  layoutInflater.inflate(R.layout.page_table_layout,containerLinearLayout,false);
    LinearLayout tableLinearLayout = tableView.findViewById(R.id.tableLinearLayoutId);
    ImageView addMoreColumnActionImageView = tableView.findViewById(R.id.addMoreColumnActionImageViewId);
    ImageView addMoreRowActionImageView = tableView.findViewById(R.id.addMoreRowActionImageViewId);
    createTableRow(tableView,tableLinearLayout);
    createTableColumn(tableLinearLayout);
    createTableColumn(tableLinearLayout);
    addMoreRowActionImageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            createTableRow(tableView,tableLinearLayout);
        }
    });

    addMoreColumnActionImageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            createTableColumn(tableLinearLayout);
        }
    });
    containerLinearLayout.addView(tableView,validPosition);
    addEditText(containerLinearLayout.indexOfChild(tableView)+1);

}

void createTableRow(View tableView,LinearLayout tableLinearLayout){
    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View tableRowView  =  layoutInflater.inflate(R.layout.page_table_row_layout,containerLinearLayout,false);
    ImageView removeRowActionImageView = tableRowView.findViewById(R.id.removeRowActionImageViewId);
    LinearLayout tableRowLinearLayout = tableRowView.findViewById(R.id.tableRowLinearLayoutId);
    if(tableLinearLayout.getChildCount()!=0){
        LinearLayout firstRow = (LinearLayout) tableLinearLayout.getChildAt(0);
        LinearLayout rowLinearLayout = (LinearLayout) firstRow.getChildAt(0);
        int numberOfColumns = rowLinearLayout.getChildCount();
        for(int i=0; i<numberOfColumns; i++){
        addTableEditTextCell(tableRowLinearLayout);
        }
    }

    removeRowActionImageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(tableLinearLayout.getChildCount() == 1){
                EditText textBefore = (EditText) containerLinearLayout.getChildAt(containerLinearLayout.indexOfChild(tableView)-1);
                EditText textAfter = (EditText) containerLinearLayout.getChildAt(containerLinearLayout.indexOfChild(tableView)+1);

                textBefore.append(textAfter.getText().toString());
                containerLinearLayout.removeView(textAfter);
                containerLinearLayout.removeView(tableView);
            }else {
                tableLinearLayout.removeView(tableRowView);
            }

        }
    });
    tableLinearLayout.addView(tableRowView);
}

void createTableColumn(LinearLayout tableLinearLayout){
        if(tableLinearLayout.getChildCount()!=0){
            for(int i=0; i<tableLinearLayout.getChildCount(); i++){
                LinearLayout rowContainerLinearLayout = (LinearLayout) tableLinearLayout.getChildAt(i);
                LinearLayout rowLinearLayout = (LinearLayout) rowContainerLinearLayout.getChildAt(0);
                addTableEditTextCell(rowLinearLayout);
            }
        }
}

void addTableEditTextCell(LinearLayout rowLinearLayout){
    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View tableCell  =  layoutInflater.inflate(R.layout.page_table_cell_edit_text,rowLinearLayout,false);
    EditText editTextCell =tableCell.findViewById(R.id.editTextCellId);
//    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,40,1);
//    layoutParams.setMargins(5,5,5,5);
//    editTextCell.setLayoutParams(layoutParams);
    editTextCell.setHint("?");
//    editTextCell.requestFocus();
//    editTextCell.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
//    ColorDrawable gray = new ColorDrawable();
//    gray.setColor(getResources().getColor(R.color.gray));
//    editTextCell.setBackground(gray);

    rowLinearLayout.addView(tableCell);

}

void postPage(){
    pageId = GlobalConfig.getRandomString(60);
GlobalConfig.createSnackBar(this,containerLinearLayout,"Creating "+pageTitleEditText.getText()+" page", Snackbar.LENGTH_INDEFINITE);

//        toggleProgress(true);
preparePage();

    UploadPageManagerService.addUploadListeners(new UploadPageManagerService.OnPageUploadListener() {
        @Override
        public void onNewPage(String pageId) {
            Toast.makeText(getApplicationContext(), "New page id: "+ pageId, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed(String pageId) {
            Toast.makeText(getApplicationContext(), "page upload failed: "+ pageId, Toast.LENGTH_SHORT).show();
            toggleProgress(false);

        }

        @Override
        public void onProgress(String pageId, int progressCount) {
            Toast.makeText(getApplicationContext(), progressCount+" page progressing: "+ pageId, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onSuccess(String pageId) {
            Toast.makeText(getApplicationContext(), "New page upload succeeded: "+ pageId, Toast.LENGTH_SHORT).show();
            toggleProgress(false);
            GlobalHelpers.showAlertMessage("success",
                    UploadPageActivity.this,
                    "Page created successfully",
                    "You have successfully created your page, go ahead and contribute to Learn Era ");


        }
    });
    UploadPageManagerService.setInitialVariables(pageId);

    ArrayList<ArrayList<String>> allPageTextDataDetailsArrayList = new ArrayList<>();

    Toast.makeText(getApplicationContext(), containerLinearLayout.getChildCount()+"", Toast.LENGTH_SHORT).show();
    int numOfChildrenData = containerLinearLayout.getChildCount();
    int totalNumberOfImages = 0;
    ArrayList<ArrayList<Object>> allImageArrayList = new ArrayList<>();

    for(int i=0; i<containerLinearLayout.getChildCount(); i++){

            if(containerLinearLayout.getChildAt(i) instanceof  EditText){
                //A plain text
                EditText editText = (EditText) containerLinearLayout.getChildAt(i);
                ArrayList<String> pageTextDataTypeDetailsArrayList = new ArrayList<>();

                pageTextDataTypeDetailsArrayList.add(0,GlobalConfig.TEXT_TYPE);
                pageTextDataTypeDetailsArrayList.add(1,containerLinearLayout.indexOfChild(editText)+"");
                pageTextDataTypeDetailsArrayList.add(2,editText.getText().toString());
                allPageTextDataDetailsArrayList.add(pageTextDataTypeDetailsArrayList);

                       // for(int r =0; r<pageTextDataTypeDetailsArrayList.size(); r++){
                        EditText editText1 =  (EditText) containerLinearLayout.getChildAt(0);
//                        editText1.append(pageTextDataTypeDetailsArrayList.get(0)+"-"+pageTextDataTypeDetailsArrayList.get(1)+"-"+pageTextDataTypeDetailsArrayList.get(2)+"_"+containerLinearLayout.indexOfChild(editText)+"_");
                      //  }

//                Toast.makeText(getApplicationContext(), "it is edittext", Toast.LENGTH_SHORT).show();
                if(editText.getText().toString().isEmpty()){
//                    Toast.makeText(getApplicationContext(), editText.getText()+" is removed", Toast.LENGTH_SHORT).show();
//                    containerLinearLayout.removeView(editText);

                }
            }
            else if(containerLinearLayout.getChildAt(i).getId() == R.id.imageConstraintLayoutId){
//                Toast.makeText(getApplicationContext(), "it is image", Toast.LENGTH_SHORT).show();
                totalNumberOfImages++;
                ImageView imageView = containerLinearLayout.getChildAt(i).findViewById(R.id.imageViewId);

                ArrayList<Object> imageArrayList = new ArrayList<>();
                imageArrayList.add(0,containerLinearLayout.indexOfChild(containerLinearLayout.getChildAt(i)));

                imageView.setDrawingCacheEnabled(true);
                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                imageArrayList.add(1,bytes);
                allImageArrayList.add(imageArrayList);
//
//                EditText editText = (EditText) containerLinearLayout.getChildAt(0);
//                for(int i1 = 0; i<200; i++) {
//
//                    editText.append(bytes[i] + "");
//                }

            }

            else if(containerLinearLayout.getChildAt(i).getId() == R.id.tableConstraintLinearLayoutId){
//                Toast.makeText(getApplicationContext(), "it is table", Toast.LENGTH_SHORT).show();

                LinearLayout tableLinearLayout = containerLinearLayout.getChildAt(i).findViewById(R.id.tableLinearLayoutId);
                int numberOfRows = tableLinearLayout.getChildCount();

                LinearLayout tableRowHorizontalLinearLayout = tableLinearLayout.getChildAt(0).findViewById(R.id.tableRowLinearLayoutId);
                int numberOfColumns = tableRowHorizontalLinearLayout.getChildCount();

                ArrayList<String> pageTableTextDataTypeDetailsArrayList = new ArrayList<>();

                pageTableTextDataTypeDetailsArrayList.add(0,GlobalConfig.TABLE_TYPE);
                pageTableTextDataTypeDetailsArrayList.add(1,containerLinearLayout.indexOfChild(containerLinearLayout.getChildAt(i)) +"");
                pageTableTextDataTypeDetailsArrayList.add(2,numberOfRows+"");
                pageTableTextDataTypeDetailsArrayList.add(3,numberOfColumns+"");

                StringBuilder tableItems = new StringBuilder();

                for(int i1 = 0; i1<numberOfRows; i1++){
                    LinearLayout tableRowHorizontalLinearLayout2 = tableLinearLayout.getChildAt(i1).findViewById(R.id.tableRowLinearLayoutId);
                    int numberOfColumns2 = tableRowHorizontalLinearLayout2.getChildCount();
                    for(int i2 = 0; i2<numberOfColumns2; i2++){
                        EditText cell = tableRowHorizontalLinearLayout2.getChildAt(i2).findViewById(R.id.editTextCellId);
                        String text = cell.getText()+"";
                        if(i2 != numberOfColumns2-1) {
                            tableItems.append(text).append(",");
                        }else{
                            tableItems.append(text);

                        }
//                        Toast.makeText(getApplicationContext(), cell.getText(), Toast.LENGTH_SHORT).show();
                    }
                    if(i1 != numberOfRows-1) {
                        tableItems.append("_");
                    }


                }
                pageTableTextDataTypeDetailsArrayList.add(4,tableItems+"");
                allPageTextDataDetailsArrayList.add(pageTableTextDataTypeDetailsArrayList);

//                for(int r =0; r<pageTableTextDataTypeDetailsArrayList.size(); r++){
                    EditText editText1 =  (EditText) containerLinearLayout.getChildAt(0);
//                    editText1.append(pageTableTextDataTypeDetailsArrayList.get(0)+"-"+pageTableTextDataTypeDetailsArrayList.get(1)+"-"+pageTableTextDataTypeDetailsArrayList.get(3)+"_"+pageTableTextDataTypeDetailsArrayList.get(4)+"_"+containerLinearLayout.indexOfChild(containerLinearLayout.getChildAt(i))+"_");
//                }



            }

            else if(containerLinearLayout.getChildAt(i).getId() == R.id.todoGroupLinearLayoutId){
                Toast.makeText(getApplicationContext(), "it is todo list", Toast.LENGTH_SHORT).show();
                LinearLayout todoItemLinearLayout = containerLinearLayout.getChildAt(i).findViewById(R.id.todoItemLinearLayoutId);
                int numberOfItems = todoItemLinearLayout.getChildCount();

                ArrayList<String> pageTodoTextDataTypeDetailsArrayList = new ArrayList<>();

                pageTodoTextDataTypeDetailsArrayList.add(0,GlobalConfig.TODO_TYPE);
                pageTodoTextDataTypeDetailsArrayList.add(1,""+containerLinearLayout.indexOfChild(containerLinearLayout.getChildAt(i)));
                pageTodoTextDataTypeDetailsArrayList.add(2,numberOfItems+"");
                StringBuilder todoItems = new StringBuilder();

                for(int i1 = 0; i1<numberOfItems; i1++){
                    EditText todoEditText = todoItemLinearLayout.getChildAt(i1).findViewById(R.id.todoEditTextId);
                    String item = todoEditText.getText().toString();
//                    Toast.makeText(getApplicationContext(), todoEditText.getText(), Toast.LENGTH_SHORT).show();

                    if(i1  != numberOfItems-1){
                        todoItems.append(item).append(",");
                    }else{
                        todoItems.append(item);
                    }

                }
                pageTodoTextDataTypeDetailsArrayList.add(3,todoItems+"");
                allPageTextDataDetailsArrayList.add(pageTodoTextDataTypeDetailsArrayList);

//                for(int r =0; r<pageTodoTextDataTypeDetailsArrayList.size(); r++){
                    EditText editText1 =  (EditText) containerLinearLayout.getChildAt(0);
//                    editText1.append(pageTodoTextDataTypeDetailsArrayList.get(0)+"-"+pageTodoTextDataTypeDetailsArrayList.get(1)+"-"+pageTodoTextDataTypeDetailsArrayList.get(2)+"_"+pageTodoTextDataTypeDetailsArrayList.get(3)+"_"+containerLinearLayout.indexOfChild(containerLinearLayout.getChildAt(i))+"_");
//                }
            }

        }

    UploadPageManagerService.uploadImageDataToPage( libraryId,  tutorialId,  folderId,  pageId, pageTitle,allImageArrayList, totalNumberOfImages,numOfChildrenData,isTutorialPage);
    UploadPageManagerService.uploadTextDataToPage( libraryId,  tutorialId,  folderId,  pageId, pageTitle, allPageTextDataDetailsArrayList,  numOfChildrenData,isTutorialPage);

    }

    private void recordTextStyles(){



    }


void preparePage(){


    //now save the html and title to the database here .
    //pageContent;

    Toast.makeText(getApplicationContext(), containerLinearLayout.getChildCount()+"", Toast.LENGTH_SHORT).show();

    for(int i=0; i<containerLinearLayout.getChildCount(); i++){

            if(containerLinearLayout.getChildAt(i) instanceof  EditText){
                //A plain text
//                Toast.makeText(getApplicationContext(), "it is edittext", Toast.LENGTH_SHORT).show();
                EditText editText = (EditText) containerLinearLayout.getChildAt(i);
                if(editText.getText().toString().isEmpty()){
//                    Toast.makeText(getApplicationContext(), editText.getText()+" is removed", Toast.LENGTH_SHORT).show();
                    containerLinearLayout.removeView(editText);

                }
            }
            /*
            else if(containerLinearLayout.getChildAt(i).getId() == R.id.imageConstraintLayoutId){
                Toast.makeText(getApplicationContext(), "it is image", Toast.LENGTH_SHORT).show();

                ImageView imageView = containerLinearLayout.getChildAt(i).findViewById(R.id.imageViewId);

            }

            else if(containerLinearLayout.getChildAt(i).getId() == R.id.tableConstraintLinearLayoutId){
                Toast.makeText(getApplicationContext(), "it is table", Toast.LENGTH_SHORT).show();

                LinearLayout tableLinearLayout = containerLinearLayout.getChildAt(i).findViewById(R.id.tableLinearLayoutId);
                int numberOfRows = tableLinearLayout.getChildCount();
                boolean isAllTableEmpty = true;
                for(int i1 = 0; i1<numberOfRows; i1++){
                    LinearLayout tableRowHorizontalLinearLayout = tableLinearLayout.getChildAt(i1).findViewById(R.id.tableRowLinearLayoutId);
                    int numberOfColumns = tableRowHorizontalLinearLayout.getChildCount();
                    boolean isAllRowEmpty = true;
                    for(int i2 = 0; i2<numberOfColumns; i2++){
                        EditText cell = tableRowHorizontalLinearLayout.getChildAt(i2).findViewById(R.id.editTextCellId);
                        Toast.makeText(getApplicationContext(), cell.getText(), Toast.LENGTH_SHORT).show();
                        if (!cell.getText().toString().isEmpty()){
                            isAllTableEmpty=false;
                            isAllRowEmpty = false;
                        }
                        if(isAllRowEmpty){
                            //remove this row if they are all empty (if there is need to do that)
//                            tableLinearLayout.removeView(tableRowHorizontalLinearLayout);
                        }
                    }

                }
                if(isAllTableEmpty){
                    //remove this table if they are all empty (if there is need to do that)

//                    containerLinearLayout.removeView(tableLinearLayout);
                }



            }

            else if(containerLinearLayout.getChildAt(i).getId() == R.id.todoGroupLinearLayoutId){
                Toast.makeText(getApplicationContext(), "it is todo list", Toast.LENGTH_SHORT).show();
                LinearLayout todoItemLinearLayout = containerLinearLayout.getChildAt(i).findViewById(R.id.todoItemLinearLayoutId);
                int numberOfItems = todoItemLinearLayout.getChildCount();
                for(int i1 = 0; i1<numberOfItems; i1++){
                    EditText todoEditText = todoItemLinearLayout.getChildAt(i1).findViewById(R.id.todoEditTextId);
                    Toast.makeText(getApplicationContext(), todoEditText.getText(), Toast.LENGTH_SHORT).show();

                }
            }
*/
        }
}


}
