package com.palria.learnera;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
/*There may be no need to implement an activity for adding a folder to a tutorial,
*it has to be a dialog with some widgets like edit text for entering the folder name.
*/
public class CreateNewTutorialFolderActivity extends AppCompatActivity {
String folderName;
String folderId;
String tutorialContainerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_tutorial_folder);
    }


}