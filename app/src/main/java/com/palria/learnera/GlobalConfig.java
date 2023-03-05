package com.palria.learnera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.telecom.Call;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.firestore.model.Document;
import com.google.firebase.firestore.model.ServerTimestamps;
import com.google.firebase.storage.FirebaseStorage;
import com.palria.learnera.models.CurrentUserProfileDataModel;
import com.palria.learnera.models.UserActivityDataModel;
import com.palria.learnera.models.UserProfileDataModel;
import com.palria.learnera.models.WelcomeScreenItemModal;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

//authorId_libraryid_tutorialId
//performance tag
//load user profile once from here
//RULE ADD NUMBER OFF BOOK-MARKS
public class GlobalConfig {

    private static String CURRENT_USER_ID;
    private static String CURRENT_USER_TOKEN_ID;
    static OnCurrentUserProfileFetchListener onCurrentUserProfileFetchListener;
    private static ArrayList<String> lastViewedAuthorsIdArray = new ArrayList<>();
    private static ArrayList<String> lastViewedLibraryIdArray = new ArrayList<>();
    private static ArrayList<String> lastViewedTutorialsIdArray = new ArrayList<>();
    /*FIRESTORE VARIABLE KEYS
    These String keys which will be  unique in  the database
    they are used to query a particular field within a document
    they have to be unique in a particular document
    */



    /**
     * TODO: style characters and symbols for manipulating page texts
     * <p>paragraph</p>
     * <b>bold</b>
     * <h1>heading 1</h1>
     * <h2>heading 2</h2>
     * <h3>heading 3</h3>
     * <h4>heading 4</h4>
     * <h5>heading 5</h6>
     * <h6>heading 6</h6>
     * <h7>heading 7</h7>
     *
     * */

    public static final String TEXT_TYPE = "TEXT_TYPE";
    public static final String IMAGE_TYPE = "IMAGE_TYPE";
    public static final String TODO_TYPE = "TODO_TYPE";
    public static final String TABLE_TYPE = "TABLE_TYPE";


    public static final String SEARCH_KEYWORD_KEY = "SEARCH_KEYWORD";
    public static final String IS_FROM_SEARCH_CONTEXT_KEY = "IS_FROM_SEARCH_CONTEXT";




    public static final String FRAGMENT_TYPE_KEY = "FRAGMENT_TYPE";
    public static final String USER_PROFILE_FRAGMENT_TYPE_KEY = "USER_PROFILE_FRAGMENT_TYPE";
    public static final String LIBRARY_FRAGMENT_TYPE_KEY = "LIBRARY_FRAGMENT_TYPE";
    public static final String TUTORIAL_FRAGMENT_TYPE_KEY = "TUTORIAL_FRAGMENT_TYPE";

    public static final String IS_AUTHOR_OPEN_TYPE_KEY = "IS_AUTHOR_OPEN_TYPE";


    public static final String TYPE_KEY = "TYPE";
    public static final String AUTHOR_TYPE_KEY = "AUTHOR_TYPE";
    public static final String LIBRARY_TYPE_KEY = "LIBRARY_TYPE";
    public static final String TUTORIAL_TYPE_KEY = "TUTORIAL_TYPE";
    public static final String FOLDER_TYPE_KEY = "FOLDER_TYPE";
    public static final String TUTORIAL_PAGE_TYPE_KEY = "TUTORIAL_PAGE_TYPE";
    public static final String FOLDER_PAGE_TYPE_KEY = "FOLDER_PAGE_TYPE";



    //USER FIELD KEYS BEGIN
    public static final String ALL_USERS_KEY = "ALL_USERS";
    public static final String USER_PROFILE_KEY = "USER_PROFILE";
    public static final String USER_ID_KEY = "USER_ID";
    public static final String USER_DATE_OF_BIRTH_KEY = "USER_DATE_OF_BIRTH";
    public static final String IS_USER_PROFILE_COMPLETED_KEY = "IS_USER_PROFILE_COMPLETED";
    public static final String USER_RESIDENTIAL_ADDRESS_KEY = "USER_RESIDENTIAL_ADDRESS";
    public static final String USER_AGE_KEY = "USER_AGE";
    public static final String USER_DOCUMENT_SNAPSHOT_KEY = "USER_DOCUMENT_SNAPSHOT";
    public static final String USER_DATA_MODEL_KEY = "USER_DATA_MODEL_KEY";
    public static final String IS_USER_BLOCKED_KEY = "IS_USER_BLOCKED";
    public static final String USER_DISPLAY_NAME_KEY = "USER_DISPLAY_NAME";
    public static final String USER_IMAGES_KEY = "USER_IMAGES";
    public static final String LAST_SEEN_KEY = "LAST_SEEN";
    public static final String AUTHOR_CATEGORY_TAG_ARRAY_KEY = "AUTHOR_CATEGORY_TAG_ARRAY";
    public static final String IS_USER_AUTHOR_KEY = "IS_USER_AUTHOR";
    public static final String AUTHOR_DATE_KEY = "AUTHOR_DATE";
    public static final String AUTHOR_DATE_TIME_STAMP_KEY = "AUTHOR_DATE_TIME_STAMP";
    static final String USER_ACTIVITY_LOG_KEY = "USER_ACTIVITY_LOG";


    static final String LAST_VIEWED_AUTHORS_ARRAY_KEY = "LAST_VIEWED_AUTHORS_ARRAY";
    static final String LAST_VIEWED_LIBRARY_ARRAY_KEY = "LAST_VIEWED_LIBRARY_ARRAY";
    static final String LAST_VIEWED_TUTORIALS_ARRAY_KEY = "LAST_VIEWED_TUTORIALS_ARRAY";

    public static final String TOTAL_NUMBER_OF_USER_PROFILE_VISITORS_KEY = "TOTAL_NUMBER_OF_USER_PROFILE_VISITORS";
    public static final String TOTAL_NUMBER_OF_USER_PROFILE_REACH_KEY = "TOTAL_NUMBER_OF_USER_PROFILE_REACH";

    public static final String USER_DESCRIPTION_KEY = "USER_DESCRIPTION";
    public static final String USER_PROFILE_PHOTO_KEY = "USER_PROFILE_PHOTO";
    public static final String IS_USER_PROFILE_PHOTO_INCLUDED_KEY = "IS_USER_PROFILE_PHOTO_INCLUDED";
    public static final String USER_PROFILE_PHOTO_DOWNLOAD_URL_KEY = "USER_PROFILE_PHOTO_DOWNLOAD_URL";
    public static final String USER_PROFILE_PHOTO_STORAGE_REFERENCE_KEY = "USER_PROFILE_PHOTO_STORAGE_REFERENCE";
    public static final String USER_PROFILE_DATE_CREATED_KEY = "USER_PROFILE_DATE_CREATED";
    public static final String USER_PROFILE_DATE_CREATED_TIME_STAMP_KEY = "USER_PROFILE_DATE_CREATED_TIME_STAMP";
    public static final String USER_PROFILE_DATE_EDITED_KEY = "USER_PROFILE_DATE_EDITED";
    public static final String USER_PROFILE_DATE_EDITED_TIME_STAMP_KEY = "USER_PROFILE_DATE_EDITED_TIME_STAMP";
    public static final String USER_TOKEN_ID_KEY = "USER_TOKEN_ID";
    public static final String USER_GENDER_TYPE_KEY = "USER_GENDER_TYPE";
    public static final String USER_EMAIL_ADDRESS_KEY = "USER_EMAIL_ADDRESS";
    public static final String USER_CONTACT_PHONE_NUMBER_KEY = "USER_CONTACT_PHONE_NUMBER";
    public static final String USER_CONTACT_EMAIL_ADDRESS_KEY = "USER_CONTACT_EMAIL_ADDRESS";
    public static final String USER_COUNTRY_OF_RESIDENCE_KEY = "USER_COUNTRY_OF_RESIDENCE";
    public static final String USER_SEARCH_VERBATIM_KEYWORD_KEY = "USER_SEARCH_VERBATIM_KEYWORD";
    public static final String USER_SEARCH_ANY_MATCH_KEYWORD_KEY = "USER_SEARCH_ANY_MATCH_KEYWORD";
    //USER FIELD KEYS END


    public static final String DOCUMENT_CREATED_KEY = "DOCUMENT_CREATED";

    public static final String TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY = "TOTAL_NUMBER_OF_ONE_STAR_RATE";
    public static final String TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY = "TOTAL_NUMBER_OF_TWO_STAR_RATE";
    public static final String TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY = "TOTAL_NUMBER_OF_THREE_STAR_RATE";
    public static final String TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY = "TOTAL_NUMBER_OF_FOUR_STAR_RATE";
    public static final String TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY = "TOTAL_NUMBER_OF_FIVE_STAR_RATE";

    public static final String BOOK_MARKS_KEY = "BOOK_MARKS";
    public static final String IS_AUTHOR_BOOK_MARK_KEY = "IS_AUTHOR_BOOK_MARK";
    public static final String IS_LIBRARY_BOOK_MARK_KEY = "IS_LIBRARY_BOOK_MARK";
    public static final String IS_TUTORIAL_BOOK_MARK_KEY = "IS_TUTORIAL_BOOK_MARK";
    public static final String IS_FOLDER_BOOK_MARK_KEY = "IS_FOLDER_BOOK_MARK";
    public static final String IS_TUTORIAL_PAGE_BOOK_MARK_KEY = "IS_TUTORIAL_PAGE_BOOK_MARK";
    public static final String IS_FOLDER_PAGE_BOOK_MARK_KEY = "IS_FOLDER_PAGE_BOOK_MARK";
    public static final String AUTHOR_ID_KEY = "AUTHOR_ID";
    public static final String DATE_BOOK_MARKED_KEY = "DATE_BOOK_MARKED";
    public static final String DATE_TIME_STAMP_BOOK_MARKED_KEY = "DATE_TIME_STAMP_BOOK_MARKED";
    public static final String LAST_DATE_TIME_STAMP_BOOK_MARKED_KEY = "LAST_DATE_TIME_STAMP_BOOK_MARKED";
    public static final String TOTAL_NUMBER_OF_OTHER_BOOK_MARKS_KEY = "TOTAL_NUMBER_OF_OTHER_BOOK_MARKS";
    public static final String LAST_DATE_TIME_STAMP_BOOK_MARKED_OTHER_KEY = "LAST_DATE_TIME_STAMP_BOOK_MARKED_OTHER";
    public static final String TOTAL_NUMBER_OF_BOOK_MARKS_KEY = "TOTAL_NUMBER_OF_BOOK_MARKS";
    public static final String BOOK_MARKER_USER_ID_KEY = "BOOK_MARKER_USER_ID";


    public static final String REVIEWS_KEY = "REVIEWS";
    public static final String TOTAL_NUMBER_OF_AUTHOR_REVIEWS_KEY = "TOTAL_NUMBER_OF_AUTHOR_REVIEWS";
    public static final String TOTAL_NUMBER_OF_LIBRARY_REVIEWS_KEY = "TOTAL_NUMBER_OF_LIBRARY_REVIEWS";
    public static final String TOTAL_NUMBER_OF_TUTORIAL_REVIEWS_KEY = "TOTAL_NUMBER_OF_TUTORIAL_REVIEWS";
    public static final String IS_AUTHOR_REVIEW_KEY = "IS_AUTHOR_REVIEW";
    public static final String IS_LIBRARY_REVIEW_KEY = "IS_LIBRARY_REVIEW";
    public static final String IS_TUTORIAL_REVIEW_KEY = "IS_TUTORIAL_REVIEW";
    public static final String OTHER_REVIEWS_KEY = "OTHER_REVIEWS";
    public static final String REVIEW_COMMENT_KEY = "REVIEW_COMMENT";
    public static final String STAR_LEVEL_KEY = "STAR_LEVEL";
    public static final String DATE_REVIEWED_KEY = "DATE_REVIEWED";
    public static final String REVIEWER_ID_KEY = "REVIEWER_ID";
    public static final String DATE_REVIEWED_TIME_STAMP_KEY = "DATE_REVIEWED_TIME_STAMP";
    public static final String PERFORMANCE_TAG_KEY = "PERFORMANCE_TAG";


    //LIBRARY FIELD KEYS BEGIN
    public static final String IS_CREATE_NEW_LIBRARY_KEY = "IS_CREATE_NEW_LIBRARY";
    public static final String ALL_LIBRARY_KEY = "ALL_LIBRARY";
    public static final String LIBRARY_PROFILE_KEY = "LIBRARY_PROFILE";

    public static final String LIBRARY_DISPLAY_NAME_KEY = "LIBRARY_DISPLAY_NAME";
    public static final String LIBRARY_DESCRIPTION_KEY = "LIBRARY_DESCRIPTION";
    public static final String LIBRARY_CATEGORY_ARRAY_KEY = "LIBRARY_CATEGORY_ARRAY";
    public static final String LIBRARY_ID_KEY = "LIBRARY_ID";
    public static final String LIBRARY_DATA_MODEL_KEY = "LIBRARY_DATA_MODEL";
    public static final String LIBRARY_DOCUMENT_SNAPSHOT_KEY = "LIBRARY_DOCUMENT_SNAPSHOT";
    public static final String LIBRARY_AUTHOR_ID_KEY = "LIBRARY_AUTHOR_ID";
    public static final String LIBRARY_DATE_CREATED_KEY = "LIBRARY_DATE_CREATED";
    public static final String LIBRARY_DATE_CREATED_TIME_STAMP_KEY = "LIBRARY_DATE_CREATED_TIME_STAMP";
    public static final String LIBRARY_SEARCH_VERBATIM_KEYWORD_KEY = "LIBRARY_SEARCH_VERBATIM_KEYWORD";
    public static final String LIBRARY_SEARCH_ANY_MATCH_KEYWORD_KEY = "LIBRARY_SEARCH_ANY_MATCH_KEYWORD";
    public static final String LAST_LIBRARY_CREATED_ID_KEY = "LAST_LIBRARY_CREATED_ID";
    public static final String LAST_LIBRARY_DATE_CREATED_KEY = "LAST_LIBRARY_DATE_CREATED";
    public static final String LIBRARY_DATE_EDITED_KEY = "LIBRARY_DATE_EDITED";
    public static final String LIBRARY_DATE_EDITED_TIME_STAMP_KEY = "LAST_LIBRARY_DATE_CREATED_TIME_STAMP";
    public static final String LAST_LIBRARY_DATE_CREATED_TIME_STAMP_KEY = "LAST_LIBRARY_DATE_CREATED_TIME_STAMP";
    public static final String TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY = "TOTAL_NUMBER_OF_LIBRARY_CREATED";
    public static final String LIBRARY_COVER_PHOTO_DOWNLOAD_URL_KEY = "LIBRARY_COVER_PHOTO_DOWNLOAD_URL";
    public static final String LIBRARY_COVER_PHOTO_STORAGE_REFERENCE_KEY = "LIBRARY_COVER_PHOTO_STORAGE_REFERENCE";
    public static final String IS_LIBRARY_COVER_PHOTO_INCLUDED_KEY = "IS_LIBRARY_COVER_PHOTO_INCLUDED";
    public static final String LIBRARY_COVER_PHOTO_KEY = "LIBRARY_COVER_PHOTO";
    public static final String LIBRARY_IMAGES_KEY = "LIBRARY_IMAGES";
    //WILL REMOVE
    public static final String TOTAL_NUMBER_OF_LIBRARY_VIEWS_KEY = "TOTAL_NUMBER_OF_LIBRARY_VIEWS";
    public static final String TOTAL_NUMBER_OF_LIBRARY_VISITOR_KEY = "TOTAL_NUMBER_OF_LIBRARY_VISITOR";
    public static final String TOTAL_NUMBER_OF_LIBRARY_REACH_KEY = "TOTAL_NUMBER_OF_LIBRARY_REACH";
    //LIBRARY FIELD KEYS END


  //TUTORIAL FIELD KEYS BEGIN
    public static final String IS_CREATE_NEW_TUTORIAL_KEY = "IS_CREATE_NEW_TUTORIAL";
    public static final String ALL_TUTORIAL_KEY = "ALL_TUTORIAL";
    public static final String LIBRARY_CONTAINER_ID_KEY = "LIBRARY_CONTAINER_ID";
    public static final String TUTORIAL_CATEGORY_KEY = "TUTORIAL_CATEGORY";
    public static final String TUTORIAL_PROFILE_KEY = "TUTORIAL_PROFILE";
    public static final String TUTORIAL_DISPLAY_NAME_KEY = "TUTORIAL_DISPLAY_NAME";
    public static final String TUTORIAL_DESCRIPTION_KEY = "TUTORIAL_DESCRIPTION";
    public static final String TUTORIAL_ID_KEY = "TUTORIAL_ID";
    public static final String TUTORIAL_DOCUMENT_SNAPSHOT_KEY = "TUTORIAL_DOCUMENT_SNAPSHOT";
    public static final String TUTORIAL_DATA_MODEL_KEY = "TUTORIAL_DATA_MODEL_KEY";
    public static final String TUTORIAL_AUTHOR_ID_KEY = "TUTORIAL_AUTHOR_ID";
    public static final String TUTORIAL_DATE_CREATED_KEY = "TUTORIAL_DATE_CREATED";
    public static final String TUTORIAL_DATE_CREATED_TIME_STAMP_KEY = "TUTORIAL_DATE_CREATED_TIME_STAMP";
    public static final String TUTORIAL_SEARCH_VERBATIM_KEYWORD_KEY = "TUTORIAL_SEARCH_VERBATIM_KEYWORD";
    public static final String TUTORIAL_SEARCH_ANY_MATCH_KEYWORD_KEY = "TUTORIAL_SEARCH_ANY_MATCH_KEYWORD";
    public static final String LAST_TUTORIAL_CREATED_ID_KEY = "LAST_TUTORIAL_CREATED_ID";
    public static final String LAST_TUTORIAL_DATE_CREATED_KEY = "LAST_TUTORIAL_DATE_CREATED";
    public static final String TUTORIAL_DATE_EDITED_KEY = "TUTORIAL_DATE_EDITED";
    public static final String TUTORIAL_DATE_EDITED_TIME_STAMP_KEY = "TUTORIAL_DATE_EDITED_TIME_STAMP";
    public static final String LAST_TUTORIAL_DATE_CREATED_TIME_STAMP_KEY = "LAST_TUTORIAL_DATE_CREATED_TIME_STAMP";
    public static final String TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY = "TOTAL_NUMBER_OF_TUTORIAL_CREATED";
    public static final String TUTORIAL_COVER_PHOTO_DOWNLOAD_URL_KEY = "TUTORIAL_COVER_PHOTO_DOWNLOAD_URL";
    public static final String TUTORIAL_COVER_PHOTO_STORAGE_REFERENCE_KEY = "TUTORIAL_COVER_PHOTO_STORAGE_REFERENCE";
    public static final String IS_TUTORIAL_COVER_PHOTO_INCLUDED_KEY = "IS_TUTORIAL_COVER_PHOTO_INCLUDED";
    public static final String TUTORIAL_COVER_PHOTO_KEY = "TUTORIAL_COVER_PHOTO";
    public static final String TUTORIAL_IMAGES_KEY = "TUTORIAL_IMAGES";
    public static final String TOTAL_NUMBER_OF_TUTORIAL_VISITOR_KEY = "TOTAL_NUMBER_OF_TUTORIAL_VISITOR";
    public static final String TOTAL_NUMBER_OF_TUTORIAL_REACH_KEY = "TOTAL_NUMBER_OF_TUTORIAL_REACH";
    public static final String TOTAL_NUMBER_OF_FOLDERS_CREATED_KEY = "TOTAL_NUMBER_OF_FOLDERS_CREATED";
    public static final String TOTAL_NUMBER_OF_PAGES_CREATED_KEY = "TOTAL_NUMBER_OF_PAGES_CREATED";

    public static final String ALL_FOLDERS_KEY = "ALL_FOLDERS";
    public static final String FOLDER_NAME_KEY = "FOLDER_NAME";
    public static final String FOLDER_DATA_MODEL_KEY = "FOLDER_DATA_MODEL";
    public static final String TOTAL_NUMBER_OF_FOLDER_VISITOR_KEY = "TOTAL_NUMBER_OF_FOLDER_VISITOR";
    public static final String TOTAL_NUMBER_OF_TUTORIAL_PAGE_VISITOR_KEY = "TOTAL_NUMBER_OF_TUTORIAL_PAGE_VISITOR";
    public static final String TOTAL_NUMBER_OF_FOLDER_PAGE_VISITOR_KEY = "TOTAL_NUMBER_OF_FOLDER_PAGE_VISITOR";
    public static final String PAGE_CONTENT_KEY = "PAGE_CONTENT";
//    public static final String PAGE_NAME_KEY = "PAGE_NAME";
    public static final String DATE_TIME_STAMP_PAGE_CREATED_KEY = "DATE_TIME_STAMP_PAGE_CREATED";
    public static final String TOTAL_NUMBER_OF_PAGE_DATA_KEY = "TOTAL_NUMBER_OF_PAGE_DATA";
    public static final String DATA_ARRAY_KEY = "DATA_ARRAY_";
    public static final String PAGE_TITLE_KEY = "PAGE_TITLE";
    public static final String PAGE_DATE_CREATED_TIME_STAMP_KEY = "PAGE_DATE_CREATED_TIME_STAMP";
    public static final String FOLDER_ID_KEY = "FOLDER_ID";
    public static final String IS_FOLDER_PAGE_KEY = "IS_FOLDER_PAGE";
    public static final String ALL_TUTORIAL_PAGES_KEY = "ALL_TUTORIAL_PAGES";
    public static final String ALL_FOLDER_PAGES_KEY = "ALL_FOLDER_PAGES";

    public static final String TUTORIAL_PAGE_ID_KEY = "TUTORIAL_PAGE_ID";
    public static final String FOLDER_PAGE_ID_KEY = "FOLDER_PAGE_ID";

    public static final String PAGE_ID_KEY = "PAGE_ID";
    public static final String IS_TUTORIAL_PAGE_KEY = "IS_TUTORIAL_PAGE";
    public static final String FOLDER_CREATED_DATE_TIME_STAMP_KEY = "FOLDER_CREATED_DATE_TIME_STAMP";
    public static final String TOTAL_NUMBER_OF_FOLDER_PAGES_KEY = "TOTAL_NUMBER_OF_FOLDER_PAGES";

    //TUTORIAL FIELD KEYS END



    public static final String IS_FIRST_VIEW_KEY = "IS_FIRST_VIEW";
    public static final String STAR_RATING_ARRAY_KEY = "STAR_RATING_ARRAY";


    public static final String IS_FROM_LIBRARY_ACTIVITY_CONTEXT_KEY = "IS_FROM_LIBRARY_ACTIVITY_CONTEXT";


    public static final String ACTION_DOER_ID_KEY = "ACTION_DOER_ID";

    public static final String EVENT_SECONDS_KEY = "EVENT_SECONDS";
    public static final String EVENT_MINUTE_KEY = "EVENT_MINUTE";
    public static final String EVENT_HOUR_KEY = "EVENT_HOUR";
    public static final String EVENT_DAY_KEY = "EVENT_DAY";
    public static final String EVENT_WEEK_KEY = "EVENT_WEEK";
    public static final String EVENT_MONTH_KEY = "EVENT_MONTH";
    public static final String EVENT_YEAR_KEY = "EVENT_YEAR";


    public static final String ACTIVITY_LOG_TYPE_KEY = "ACTIVITY_LOG_TYPE";
    public static final String LOG_TIME_STAMP_KEY = "LOG_TIME_STAMP";

    public static final String IS_AUTHOR_AFFECTED_KEY = "IS_AUTHOR_AFFECTED";
    public static final String IS_LIBRARY_AFFECTED_KEY = "IS_LIBRARY_AFFECTED";
    public static final String IS_TUTORIAL_AFFECTED_KEY = "IS_TUTORIAL_AFFECTED";
    public static final String IS_TUTORIAL_FOLDER_AFFECTED_KEY = "IS_TUTORIAL_FOLDER_AFFECTED";
    public static final String IS_TUTORIAL_PAGE_AFFECTED_KEY = "IS_TUTORIAL_PAGE_AFFECTED";
    public static final String IS_FOLDER_PAGE_AFFECTED_KEY = "IS_FOLDER_PAGE_AFFECTED";

    //log keys start
    public static final String ACTIVITY_LOG_USER_BOOK_MARK_FOLDER_TYPE_KEY = "ACTIVITY_LOG_USER_BOOK_MARK_FOLDER_TYPE";

    public static final String ACTIVITY_LOG_USER_REVIEW_AUTHOR_TYPE_KEY = "ACTIVITY_LOG_USER_REVIEW_AUTHOR_TYPE";
    public static final String ACTIVITY_LOG_USER_SIGN_UP_TYPE_KEY = "ACTIVITY_LOG_USER_SIGN_UP_TYPE";
    public static final String ACTIVITY_LOG_USER_SIGN_IN_TYPE_KEY = "ACTIVITY_LOG_USER_SIGN_IN_TYPE";
    public static final String ACTIVITY_LOG_USER_EDIT_ACCOUNT_TYPE_KEY = "ACTIVITY_LOG_USER_EDIT_ACCOUNT_TYPE";
    public static final String ACTIVITY_LOG_USER_DELETE_AUTHOR_REVIEW_TYPE_KEY = "ACTIVITY_LOG_USER_DELETE_AUTHOR_REVIEW_TYPE";
    public static final String ACTIVITY_LOG_USER_EDIT_AUTHOR_REVIEW_TYPE_KEY = "ACTIVITY_LOG_USER_EDIT_AUTHOR_REVIEW_TYPE";
    public static final String ACTIVITY_LOG_USER_VISIT_AUTHOR_TYPE_KEY = "ACTIVITY_LOG_USER_VISIT_AUTHOR_TYPE";
    public static final String ACTIVITY_LOG_USER_BOOK_MARK_AUTHOR_TYPE_KEY = "ACTIVITY_LOG_USER_BOOK_MARK_AUTHOR_TYPE";
    public static final String ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_AUTHOR_TYPE_KEY = "ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_AUTHOR_TYPE";



    public static final String ACTIVITY_LOG_USER_CREATE_NEW_LIBRARY_TYPE_KEY = "ACTIVITY_LOG_USER_CREATE_NEW_LIBRARY_TYPE";
    public static final String ACTIVITY_LOG_USER_DELETE_LIBRARY_TYPE_KEY = "ACTIVITY_LOG_USER_DELETE_LIBRARY_TYPE";
    public static final String ACTIVITY_LOG_USER_EDIT_LIBRARY_TYPE_KEY = "ACTIVITY_LOG_USER_EDIT_LIBRARY_TYPE";
    public static final String ACTIVITY_LOG_USER_REVIEW_LIBRARY_TYPE_KEY = "ACTIVITY_LOG_USER_REVIEW_LIBRARY_TYPE";
    public static final String ACTIVITY_LOG_USER_DELETE_LIBRARY_REVIEW_TYPE_KEY = "ACTIVITY_LOG_USER_DELETE_LIBRARY_REVIEW_TYPE";
    public static final String ACTIVITY_LOG_USER_EDIT_LIBRARY_REVIEW_TYPE_KEY = "ACTIVITY_LOG_USER_EDIT_LIBRARY_REVIEW_TYPE";
    public static final String ACTIVITY_LOG_USER_VISIT_LIBRARY_TYPE_KEY = "ACTIVITY_LOG_USER_VISIT_LIBRARY_TYPE";
    public static final String ACTIVITY_LOG_USER_BOOK_MARK_LIBRARY_TYPE_KEY = "ACTIVITY_LOG_USER_BOOK_MARK_LIBRARY_TYPE";
    public static final String ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_LIBRARY_TYPE_KEY = "ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_LIBRARY_TYPE";


    public static final String ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_TYPE_KEY = "ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_TYPE";
    public static final String ACTIVITY_LOG_USER_DELETE_TUTORIAL_TYPE_KEY = "ACTIVITY_LOG_USER_DELETE_TUTORIAL_TYPE";
    public static final String ACTIVITY_LOG_USER_EDIT_TUTORIAL_TYPE_KEY = "ACTIVITY_LOG_USER_EDIT_TUTORIAL_TYPE";
    public static final String ACTIVITY_LOG_USER_REVIEW_TUTORIAL_TYPE_KEY = "ACTIVITY_LOG_USER_REVIEW_TUTORIAL_TYPE";
    public static final String ACTIVITY_LOG_USER_DELETE_TUTORIAL_REVIEW_TYPE_KEY = "ACTIVITY_LOG_USER_DELETE_TUTORIAL_REVIEW_TYPE";
    public static final String ACTIVITY_LOG_USER_EDIT_TUTORIAL_REVIEW_TYPE_KEY = "ACTIVITY_LOG_USER_EDIT_TUTORIAL_REVIEW_TYPE";
    public static final String ACTIVITY_LOG_USER_VISIT_TUTORIAL_TYPE_KEY = "ACTIVITY_LOG_USER_VISIT_TUTORIAL_TYPE";
    public static final String ACTIVITY_LOG_USER_BOOK_MARK_TUTORIAL_TYPE_KEY = "ACTIVITY_LOG_USER_BOOK_MARK_TUTORIAL_TYPE";
    public static final String ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_TUTORIAL_TYPE_KEY = "ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_TUTORIAL_TYPE";


    public static final String ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_FOLDER_TYPE_KEY = "ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_FOLDER_TYPE";
    public static final String ACTIVITY_LOG_USER_EDIT_TUTORIAL_FOLDER_TYPE_KEY = "ACTIVITY_LOG_USER_EDIT_TUTORIAL_FOLDER_TYPE";
    public static final String ACTIVITY_LOG_USER_VISIT_FOLDER_TYPE_KEY = "ACTIVITY_LOG_USER_VISIT_FOLDER_TYPE";
    public static final String ACTIVITY_LOG_USER_DELETE_TUTORIAL_FOLDER_TYPE_KEY = "ACTIVITY_LOG_USER_DELETE_TUTORIAL_FOLDER_TYPE";
    public static final String ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_TUTORIAL_FOLDER_TYPE_KEY = "ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_TUTORIAL_FOLDER_TYPE";



    public static final String ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_PAGE_TYPE";
    public static final String ACTIVITY_LOG_USER_BOOK_MARK_TUTORIAL_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_BOOK_MARK_TUTORIAL_PAGE_TYPE";
    public static final String ACTIVITY_LOG_USER_VISIT_TUTORIAL_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_VISIT_TUTORIAL_PAGE_TYPE";
    public static final String ACTIVITY_LOG_USER_EDIT_TUTORIAL_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_EDIT_TUTORIAL_PAGE_TYPE";
    public static final String ACTIVITY_LOG_USER_DELETE_TUTORIAL_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_DELETE_TUTORIAL_PAGE_TYPE";
    public static final String ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_TUTORIAL_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_TUTORIAL_PAGE_TYPE";


    public static final String ACTIVITY_LOG_USER_CREATE_NEW_FOLDER_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_CREATE_NEW_FOLDER_PAGE_TYPE";
    public static final String ACTIVITY_LOG_USER_BOOK_MARK_FOLDER_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_BOOK_MARK_FOLDER_PAGE_TYPE";
    public static final String ACTIVITY_LOG_USER_VISIT_FOLDER_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_VISIT_FOLDER_PAGE_TYPE";
    public static final String ACTIVITY_LOG_USER_EDIT_FOLDER_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_EDIT_FOLDER_PAGE_TYPE";
    public static final String ACTIVITY_LOG_USER_DELETE_FOLDER_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_DELETE_FOLDER_PAGE_TYPE";
    public static final String ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_FOLDER_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_FOLDER_PAGE_TYPE";
//log keys ends





    public static final String _IS_PARTITION_ID_IS_FOR_IDENTIFYING_PARTITIONS_KEY = "_IS_PARTITION_ID_IS_FOR_IDENTIFYING_PARTITIONS_";





    private static FirebaseFirestore firebaseFirestoreInstance;
    private static FirebaseStorage firebaseStorageInstance;




/**
 * <p>This method performs a check to see whether a user is logged in or not</p>
 * @return {@link boolean} denoting if a user is logged in or not
 * */
    static boolean isUserLoggedIn(){

        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

/**
 * This method sets the current user's ID
 * @param currentUserId  This parameter will be initialized from the {@link FirebaseAuth#getUid()}
 *
 *<p>It has to be first invoked from the {@link SignInActivity} context</p>
 * */
   static void setCurrentUserId(@NonNull String currentUserId){
        GlobalConfig.CURRENT_USER_ID = currentUserId;
    }
      /**
       * This returns the unique ID of the current user
       *
       * @return {@link String} The unique ID of the current user
       * */
   public static String getCurrentUserId(){
       return GlobalConfig.CURRENT_USER_ID;
    }

    /**
     * This method sets the token of the current user
     * <p>This token is initialized from {@link com.google.firebase.auth.FirebaseUser#getIdToken(boolean)}</p>
     * This token is used for notification purpose
     * */
   static void setCurrentUserTokenId(@NonNull String currentUserTokenId){
        GlobalConfig.CURRENT_USER_TOKEN_ID = currentUserTokenId;
    }

    /**
     * This returns the token of the current user
     * @return {@link String} which will be used for some operations
     * */
    static String getCurrentUserTokenId(){
       return GlobalConfig.CURRENT_USER_TOKEN_ID;
    }

    /**
     * Sets the {@link FirebaseFirestore } instance
     * <p>This instance is initialized from {@link FirebaseFirestore#getInstance()}</p>
     * */
   static void setFirebaseFirestoreInstance(){
        GlobalConfig.firebaseFirestoreInstance = FirebaseFirestore.getInstance();
    }
    /**
 * Returns the global instance of the {@link FirebaseFirestore} which will be
 * used to perform actions in {@link FirebaseFirestore} database
 * @return {@link FirebaseFirestore}
 * */
   public static FirebaseFirestore getFirebaseFirestoreInstance(){
       return GlobalConfig.firebaseFirestoreInstance;
    }

    /**
     * Sets the {@link FirebaseStorage } instance
     * <p>This instance is initialized from {@link FirebaseStorage#getInstance()}</p>
     * */
   static void setFirebaseStorageInstance(){
        GlobalConfig.firebaseStorageInstance = FirebaseStorage.getInstance();
    }


    /**
     * Returns the global instance of the {@link FirebaseFirestore} which will be used to perform actions in {@link FirebaseStorage} database
     * @return {@link FirebaseStorage}
     * */
    static FirebaseStorage getFirebaseStorageInstance(){

       return GlobalConfig.firebaseStorageInstance;
    }

    /**
     * A callback method that tells when a user has either succeeded or failed in signing in to the platform
     * <p>{@link SignInListener#onSuccess(String, String)} - this method is triggered when a user signs in successfully  </p>
     * <p>{@link SignInListener#onFailed(String)} - this method is triggered when a user signs in process fails  </p>
     * <p>{@link SignInListener#onEmptyInput(boolean, boolean)} - this method is triggered when a user has an empty input  </p>
     * */
    interface SignInListener{
        void onSuccess(String email, String password);
        void onFailed(String errorMessage);
        void onEmptyInput(boolean isEmailEmpty, boolean isPasswordEmpty);
    }

    /**
     * A callback method that tells when a user has either succeeded or failed in signing in to the platform
     * <p>{@link SignUpListener#onSuccess(String, String)} - this method is triggered when a user signs in successfully  </p>
     * <p>{@link SignUpListener#onFailed(String)} - this method is triggered when a user signs in process fails  </p>
     * <p>{@link SignUpListener#onEmptyInput(boolean, boolean)} - this method is triggered when a user has an empty input  </p>
     * */
  interface SignUpListener{
        void onSuccess(String email, String password);
        void onFailed(String errorMessage);
        void onEmptyInput(boolean isEmailEmpty, boolean isPasswordEmpty);
    }

    /**
 * Signs a user in to the platform
 * @param email the email of the user
 * @param password the password of the user
 * @param signInListener a callback that tells when the sign in fails or succeeds
 * */
    static void signInUserWithEmailAndPassword(Context context,@NonNull String email, @NonNull String password,SignInListener signInListener){
        if(email != null && password != null){
            if(!email.isEmpty() && !password.isEmpty()){
              String  trimmedEmail = email.trim();
               String trimmedPassword = password.trim();
             FirebaseAuth firebaseAuth =    FirebaseAuth.getInstance();

                firebaseAuth.signInWithEmailAndPassword(trimmedEmail, trimmedPassword)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                signInListener.onFailed(e.getMessage());
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                signInListener.onSuccess(trimmedEmail, trimmedPassword);
                            }
                        });
            }else{
                signInListener.onEmptyInput(email.isEmpty(),password.isEmpty());
            }
        }else{
            signInListener.onEmptyInput(email == null,password == null);

        }
    }

    /**
     * Creates a user account
     * @param email the email of the user
     * @param password the password of the user
     * @param signUpListener a callback that tells when the sign un fails or succeeds
     * */
    static void signUpUserWithEmailAndPassword(Context context,@NonNull String email,@NonNull  String password,SignUpListener signUpListener){
        if(email != null && password != null){
            if(!email.isEmpty() && !password.isEmpty()){
                String  trimmedEmail = email.trim();
                String trimmedPassword = password.trim();
                FirebaseAuth firebaseAuth =    FirebaseAuth.getInstance();

               firebaseAuth.createUserWithEmailAndPassword(trimmedEmail, trimmedPassword)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                signUpListener.onFailed(e.getMessage());
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                signUpListener.onSuccess(trimmedEmail, trimmedPassword);
                            }
                        });
            }else{
                signUpListener.onEmptyInput(email.isEmpty(),password.isEmpty());
            }
        }else{
            signUpListener.onEmptyInput(email == null,password == null);

        }
    }


/**
 * Checks if a user's device us connected to the internet or not
 * @return  {@link boolean}
 * */
    @SuppressLint("MissingPermission")
    static boolean isConnectedOnline(Context context) {



        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if( cm.getActiveNetworkInfo() != null){
            return cm.getActiveNetworkInfo().isConnected();

        }
        return false;
    }

    /**
     * Returns a random {@link String} as long as the length specified
     * The purpose of the method is usually for unique ID generation
     * It is recommended that the length be long
     * @param length the length of the {@link String} that will be returned
     * @return {@link String} the string value
     * */
    public static String getRandomString(int length) {
        String[] characterArray = new String[]{
                "a", "A", "1",
                "b", "B", "2",
                "c", "C", "3",
                "d", "D", "4",
                "e", "E", "5",
                "f", "F", "6",
                "g", "G", "7",
                "h", "H", "8",
                "i", "I", "9",
                "j", "J", "0",
                "k", "K", "1",
                "l", "L", "2",
                "m", "M", "3",
                "n", "N", "4",
                "o", "O", "5",
                "p", "P", "6",
                "q", "Q", "7",
                "r", "R", "8",
                "s", "S", "9",
                "t", "T", "0",
                "u", "U", "1",
                "v", "V", "2",
                "w", "W", "3",
                "x", "X", "4",
                "y", "Y", "5",
                "z", "Z", "6"
        };

        StringBuilder randomString = new StringBuilder("AUNS");

        for(int i=0; i<length; i++){

            int randomPosition = new Random().nextInt(characterArray.length -1);

            randomString.append(Array.get(characterArray, randomPosition));
        }
        return randomString + "CJDM";
    }

    /**
     * This method returns the name of countries across the globe
     * @return  {@link ArrayList} the list that contains the name of countries
     * @param countryArrayList the arrayList that will hold the list of the countries, this is the same {@link Object} that will be returne
     *
     * */
    public static ArrayList<String> getCountryArrayList(@Nullable ArrayList<String> countryArrayList){
        ArrayList<String> arrayList = null;
        if(countryArrayList == null){
            arrayList = new ArrayList<>();

        }else{
            arrayList = countryArrayList;

        }

        arrayList.add("Albania");
        arrayList.add("Algeria");
        arrayList.add("American Samoa");
        arrayList.add("Andorra");
        arrayList.add("Angola");
        arrayList.add("Anguilla");
        arrayList.add("Antarctica");
        arrayList.add("Antigua and Barbuda");
        arrayList.add("Argentina");
        arrayList.add("Armenia");
        arrayList.add("Aruba");
        arrayList.add("Australia");
        arrayList.add("Austria");
        arrayList.add("Azerbaijan");
        arrayList.add("Bahamas");
        arrayList.add("Bahrain");
        arrayList.add("Bangladesh");
        arrayList.add("Barbados");
        arrayList.add("Belgium");
        arrayList.add("Belize");
        arrayList.add("Benin");
        arrayList.add("Bermuda");
        arrayList.add("Bhutan");
        arrayList.add("Bolivia");
        arrayList.add("Bonaire, Sint Eustatius and Saba");
        arrayList.add("Bosnia and Herzegovina");
        arrayList.add("Botswana");
        arrayList.add("Bouvet Island");
        arrayList.add("Brazil");
        arrayList.add("British Indian Ocean Territory");
        arrayList.add("British Virgin Islands");
        arrayList.add("Brunei Darussalam");
        arrayList.add("Bulgaria");
        arrayList.add("Burkina Faso");
        arrayList.add("Burundi");
        arrayList.add("Cambodia");
        arrayList.add("Cameroon");
        arrayList.add("Canada");
        arrayList.add("Cape Verde");
        arrayList.add("Cayman Islands");
        arrayList.add("Central African Republic");
        arrayList.add("Chad");
        arrayList.add("Chile");
        arrayList.add("China");
        arrayList.add("Christmas Island");
        arrayList.add("Cocos (Keeling) Islands");
        arrayList.add("Colombia");
        arrayList.add("Comoros");
        arrayList.add("Congo");
        arrayList.add("Cook Islands");
        arrayList.add("Costa Rica");
        arrayList.add("Cote d'Ivoire");
        arrayList.add("Croatia");
        arrayList.add("Curacao");
        arrayList.add("Cyprus");
        arrayList.add("Czech Republic");
        arrayList.add("Denmark");
        arrayList.add("Djibouti");
        arrayList.add("Dominica");
        arrayList.add("Dominican Republic");
        arrayList.add("Ecuador");
        arrayList.add("Egypt");
        arrayList.add("El Salvador");
        arrayList.add("Equatorial Guinea");
        arrayList.add("Eritrea");
        arrayList.add("Estonia");
        arrayList.add("Ethiopia");
        arrayList.add("Falkland Islands");
        arrayList.add("Faroe Islands");
        arrayList.add("Fiji");
        arrayList.add("Finland");
        arrayList.add("France");
        arrayList.add("French Guiana");
        arrayList.add("French Polynesia");
        arrayList.add("French Southern and Antarctic Lands");
        arrayList.add("Gabon");
        arrayList.add("Gambia");
        arrayList.add("Georgia");
        arrayList.add("Germany");
        arrayList.add("Ghana");
        arrayList.add("Gibraltar");
        arrayList.add("Greece");
        arrayList.add("Greenland");
        arrayList.add("Grenada");
        arrayList.add("Guadeloupe");
        arrayList.add("Guam");
        arrayList.add("Guatemala");
        arrayList.add("Guernsey");
        arrayList.add("Guinea");
        arrayList.add("Guinea-Bissau");
        arrayList.add("Guyana");
        arrayList.add("Haiti");
        arrayList.add("Heard Island and McDonald Islands");
        arrayList.add("Holy See");
        arrayList.add("Honduras");
        arrayList.add("Hong Kong");
        arrayList.add("Hungary");
        arrayList.add("Iceland");
        arrayList.add("Indonesia");
        arrayList.add("Ireland");
        arrayList.add("Isle of Man");
        arrayList.add("Israel");
        arrayList.add("Italy");
        arrayList.add("Jamaica");
        arrayList.add("Japan");
        arrayList.add("Jordan");
        arrayList.add("Kazakhstan");
        arrayList.add("Kenya");
        arrayList.add("Kiribati");
        arrayList.add("Kuwait");
        arrayList.add("Kyrgyzstan");
        arrayList.add("Laos");
        arrayList.add("Latvia");
        arrayList.add("Lebanon");
        arrayList.add("Lesotho");
        arrayList.add("Liechtenstein");
        arrayList.add("Lithuania");
        arrayList.add("Luxembourg");
        arrayList.add("Macao");
        arrayList.add("Macedonia");
        arrayList.add("Madagascar");
        arrayList.add("Malawi");
        arrayList.add("Malaysia");
        arrayList.add("Maldives");
        arrayList.add("Mali");
        arrayList.add("Malta");
        arrayList.add("Marshall Islands");
        arrayList.add("Martinique");
        arrayList.add("Mauritania");
        arrayList.add("Mauritius");
        arrayList.add("Mayotte");
        arrayList.add("Moldova");
        arrayList.add("Monaco");
        arrayList.add("Mongolia");
        arrayList.add("Montenegro");
        arrayList.add("Montserrat");
        arrayList.add("Morocco");
        arrayList.add("Mozambique");
        arrayList.add("Myanmar");
        arrayList.add("Namibia");
        arrayList.add("Nauru");
        arrayList.add("Nepal");
        arrayList.add("Netherlands");
        arrayList.add("Netherlands Antilles");
        arrayList.add("New Caledonia");
        arrayList.add("New Zealand");
        arrayList.add("Nicaragua");
        arrayList.add("Niger");
        arrayList.add("Nigeria");
        arrayList.add("Niue");
        arrayList.add("Norfolk Island");
        arrayList.add("Northern Mariana Islands");
        arrayList.add("Norway");
        arrayList.add("Oman");
        arrayList.add("Pakistan");
        arrayList.add("Palau");
        arrayList.add("Palestinian Territories");
        arrayList.add("Panama");
        arrayList.add("Papua New Guinea");
        arrayList.add("Paraguay");
        arrayList.add("Peru");
        arrayList.add("Philippines");
        arrayList.add("Pitcairn");
        arrayList.add("Poland");
        arrayList.add("Portugal");
        arrayList.add("Puerto Rico");
        arrayList.add("Qatar");
        arrayList.add("Reunion");
        arrayList.add("Romania");
        arrayList.add("Rwanda");
        arrayList.add("Saint Barthelemy");
        arrayList.add("Saint Helena");
        arrayList.add("Saint Kitts and Nevis");
        arrayList.add("Saint Lucia");
        arrayList.add("Saint Martin (French part)");
        arrayList.add("Saint Pierre and Miquelon");
        arrayList.add("Saint Vincent and the Grenadines");
        arrayList.add("Samoa");
        arrayList.add("San Marino");
        arrayList.add("Sao Tome and Principe");
        arrayList.add("Saudi Arabia");
        arrayList.add("Senegal");
        arrayList.add("Serbia");
        arrayList.add("Seychelles");
        arrayList.add("Sierra Leone");
        arrayList.add("Singapore");
        arrayList.add("Sint Maarten (Dutch part)");
        arrayList.add("Slovakia");
        arrayList.add("Slovenia");
        arrayList.add("Solomon Islands");
        arrayList.add("Somalia");
        arrayList.add("South Africa");
        arrayList.add("South Korea");
        arrayList.add("Spain");
        arrayList.add("Sri Lanka");
        arrayList.add("Suriname");
        arrayList.add("Svalbard and Jan Mayen");
        arrayList.add("Swaziland");
        arrayList.add("Sweden");
        arrayList.add("Switzerland");
        arrayList.add("Taiwan");
        arrayList.add("Tajikistan");
        arrayList.add("Tanzania");
        arrayList.add("Thailand");
        arrayList.add("Timor-Leste");
        arrayList.add("Togo");
        arrayList.add("Tokelau");
        arrayList.add("Tonga");
        arrayList.add("Trinidad and Tobago");
        arrayList.add("Tunisia");
        arrayList.add("Turkey");
        arrayList.add("Turkmenistan");
        arrayList.add("Turks and Caicos Islands");
        arrayList.add("Tuvalu");
        arrayList.add("Uganda");
        arrayList.add("Ukraine");
        arrayList.add("United Arab Emirates");
        arrayList.add("United Kingdom");
        arrayList.add("United States Minor Outlying Islands");
        arrayList.add("United States Virgin Islands");
        arrayList.add("Uruguay");
        arrayList.add("Uzbekistan");
        arrayList.add("Vanuatu");
        arrayList.add("Venezuela");
        arrayList.add("Vietnam");
        arrayList.add("Wallis and Futuna");
        arrayList.add("Western Sahara");
        arrayList.add("Zambia");
        arrayList.add("Zimbabwe");

        return arrayList;
    }

   public static  Snackbar createSnackBar(Context context , View view,String text,int lengthPeriod){
        Snackbar snackBar = Snackbar.make(view,text,lengthPeriod);
        snackBar.setAction("Hide", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackBar.dismiss();
            }
        });
        snackBar.show();

        return snackBar;
    }

    public static  void checkIfDocumentExists(DocumentReference documentReference, OnDocumentExistStatusCallback onDocumentExistStatusCallback){
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        onDocumentExistStatusCallback.onExist();
                    }else if (!documentSnapshot.exists()){
                        onDocumentExistStatusCallback.onNotExist();
                    }
                }
                else{
                        onDocumentExistStatusCallback.onFailed(task.getException().getMessage());
                }
            }
        });
    }



    public static  void reviewAuthor(String authorId,String comment,String performanceTag,long starLevel,ActionCallback actionCallback){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference authorReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(authorId).collection(GlobalConfig.REVIEWS_KEY).document(GlobalConfig.getCurrentUserId());
        HashMap<String,Object> authorReviewDetails = new HashMap<>();
        authorReviewDetails.put(GlobalConfig.REVIEWER_ID_KEY, GlobalConfig.getCurrentUserId());
        authorReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
        authorReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, starLevel);
//        authorReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
        authorReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
//        authorReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
        writeBatch.set(authorReviewDocumentReference,authorReviewDetails);

        DocumentReference authorDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(authorId);
        HashMap<String,Object> authorDetails = new HashMap<>();
        authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_AUTHOR_REVIEWS_KEY, FieldValue.increment(1L));

        switch(Math.toIntExact(starLevel)){
            case 1: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY,   FieldValue.increment(1L));
                break;
            case 2: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY,   FieldValue.increment(1L));
                break;
            case 3: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY, FieldValue.increment(1L));
                break;
            case 4: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY,  FieldValue.increment(1L));
                break;
            case 5: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY,  FieldValue.increment(1L));
                break;
        }

        writeBatch.update(authorDocumentReference,authorDetails);



        DocumentReference reviewerDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.OTHER_REVIEWS_KEY).document(authorId);
        HashMap<String,Object> reviewerReviewDetails = new HashMap<>();
        reviewerReviewDetails.put(GlobalConfig.AUTHOR_ID_KEY, authorId);
        reviewerReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
        reviewerReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, starLevel);
//        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        reviewerReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
        reviewerReviewDetails.put(GlobalConfig.IS_AUTHOR_REVIEW_KEY, true);
        writeBatch.set(reviewerDocumentReference,reviewerReviewDetails, SetOptions.merge());

        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        actionCallback.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        actionCallback.onSuccess();

                    }
                });

    }
    public static  void reviewLibrary(String authorId,String libraryId,String comment,String performanceTag,long starLevel,ActionCallback actionCallback){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference libraryReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId).collection(GlobalConfig.REVIEWS_KEY).document(GlobalConfig.getCurrentUserId());
        HashMap<String,Object> libraryReviewDetails = new HashMap<>();
        libraryReviewDetails.put(GlobalConfig.REVIEWER_ID_KEY, GlobalConfig.getCurrentUserId());
        libraryReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
        libraryReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, starLevel);
        libraryReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
        libraryReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        libraryReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
        writeBatch.set(libraryReviewDocumentReference,libraryReviewDetails,SetOptions.merge());

        DocumentReference libraryDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId);
        HashMap<String,Object> libraryDetails = new HashMap<>();
        libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REVIEWS_KEY, FieldValue.increment(1L));
        switch(Math.toIntExact(starLevel)){
            case 1: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY, FieldValue.increment(1L));
                break;
            case 2: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY,  FieldValue.increment(1L));
                break;
            case 3: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY,  FieldValue.increment(1L));
                break;
            case 4: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY, FieldValue.increment(1L));
                break;
            case 5: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY, FieldValue.increment(1L));
                break;
        }
        writeBatch.set(libraryDocumentReference,libraryDetails,SetOptions.merge());

        DocumentReference reviewerDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.OTHER_REVIEWS_KEY).document(libraryId);
        HashMap<String,Object> reviewerReviewDetails = new HashMap<>();
        reviewerReviewDetails.put(GlobalConfig.AUTHOR_ID_KEY, authorId);
        reviewerReviewDetails.put(GlobalConfig.LIBRARY_ID_KEY, libraryId);
        reviewerReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
        reviewerReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, starLevel);
//        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        reviewerReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
        reviewerReviewDetails.put(GlobalConfig.IS_LIBRARY_REVIEW_KEY, true);
        writeBatch.set(reviewerDocumentReference,reviewerReviewDetails, SetOptions.merge());

        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        actionCallback.onFailed(e.getMessage());

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        actionCallback.onSuccess();

                    }
                });

    }
    public static  void reviewTutorial(String authorId,String libraryId,String tutorialId,String comment,String performanceTag,long starLevel,ActionCallback actionCallback){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        DocumentReference tutorialReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                .document(tutorialId).collection(GlobalConfig.REVIEWS_KEY)
                .document(GlobalConfig.getCurrentUserId());

        HashMap<String,Object> tutorialReviewDetails = new HashMap<>();
        tutorialReviewDetails.put(GlobalConfig.REVIEWER_ID_KEY, GlobalConfig.getCurrentUserId());
        tutorialReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
        tutorialReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, starLevel);
//        tutorialReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
        tutorialReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        tutorialReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
        writeBatch.set(tutorialReviewDocumentReference,tutorialReviewDetails, SetOptions.merge());


        DocumentReference tutorialDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId);
        HashMap<String,Object> tutorialDetails = new HashMap<>();
        tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_REVIEWS_KEY, FieldValue.increment(1L));
        switch(Math.toIntExact(starLevel)){
            case 1: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY, FieldValue.increment(1L));
                break;
            case 2: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY,  FieldValue.increment(1L));
                break;
            case 3: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY,  FieldValue.increment(1L));
                break;
            case 4: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY, FieldValue.increment(1L));
                break;
            case 5: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY, FieldValue.increment(1L));
                break;
        }
        writeBatch.set(tutorialDocumentReference,tutorialDetails,SetOptions.merge());


        DocumentReference reviewerDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(GlobalConfig.getCurrentUserId())
                .collection(GlobalConfig.OTHER_REVIEWS_KEY)
                .document(tutorialId);

        HashMap<String,Object> reviewerReviewDetails = new HashMap<>();
        reviewerReviewDetails.put(GlobalConfig.AUTHOR_ID_KEY, authorId);
        reviewerReviewDetails.put(GlobalConfig.LIBRARY_CONTAINER_ID_KEY, libraryId);
        reviewerReviewDetails.put(GlobalConfig.TUTORIAL_ID_KEY, tutorialId);
        reviewerReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
        reviewerReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, starLevel);
        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        reviewerReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
        reviewerReviewDetails.put(GlobalConfig.IS_TUTORIAL_REVIEW_KEY, true);
        writeBatch.set(reviewerDocumentReference,reviewerReviewDetails, SetOptions.merge());

        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        actionCallback.onFailed(e.getMessage());

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        actionCallback.onSuccess();

                    }
                });

    }

    public static  void editAuthorReview(String authorId,String comment,String performanceTag,long newStarLevel,ActionCallback actionCallback){


        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(authorId).collection(GlobalConfig.REVIEWS_KEY)
                .document(GlobalConfig.getCurrentUserId()).get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                actionCallback.onFailed(e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

                long oldStarLevel = documentSnapshot.get(GlobalConfig.STAR_LEVEL_KEY) != null ? documentSnapshot.getLong(GlobalConfig.STAR_LEVEL_KEY) : 0L;

                DocumentReference authorDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(authorId);
                HashMap<String, Object> authorDetails = new HashMap<>();

                //remove the old star level
                switch (Math.toIntExact(oldStarLevel)) {
                    case 1:
                        authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 2:
                        authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 3:
                        authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 4:
                        authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 5:
                        authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                }
                switch(Math.toIntExact(newStarLevel)){
                    case 1: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY, FieldValue.increment(1L));
                        break;
                    case 2: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY,  FieldValue.increment(1L));
                        break;
                    case 3: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY,  FieldValue.increment(1L));
                        break;
                    case 4: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY, FieldValue.increment(1L));
                        break;
                    case 5: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY, FieldValue.increment(1L));
                        break;
                }
                writeBatch.set(authorDocumentReference,authorDetails,SetOptions.merge());

                DocumentReference authorReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(authorId).collection(GlobalConfig.REVIEWS_KEY).document(GlobalConfig.getCurrentUserId());
                HashMap<String,Object> authorReviewDetails = new HashMap<>();
                authorReviewDetails.put(GlobalConfig.REVIEWER_ID_KEY, GlobalConfig.getCurrentUserId());
                authorReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
                authorReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, newStarLevel);
//        authorReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
                authorReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        authorReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
                writeBatch.set(authorReviewDocumentReference,authorReviewDetails);



                DocumentReference reviewerDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.OTHER_REVIEWS_KEY).document(authorId);
                HashMap<String,Object> reviewerReviewDetails = new HashMap<>();
                reviewerReviewDetails.put(GlobalConfig.AUTHOR_ID_KEY, authorId);
                reviewerReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
                reviewerReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, newStarLevel);
//        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
                reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        reviewerReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
                reviewerReviewDetails.put(GlobalConfig.IS_AUTHOR_REVIEW_KEY, true);
                writeBatch.set(reviewerDocumentReference,reviewerReviewDetails, SetOptions.merge());

                writeBatch.commit()
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                actionCallback.onFailed(e.getMessage());

                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                actionCallback.onSuccess();

                            }
                        });
            }
        });



    }
    public static  void editLibraryReview(String authorId,String libraryId,String comment,String performanceTag,long newStarLevel,ActionCallback actionCallback){


        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_LIBRARY_KEY)
                .document(libraryId).collection(GlobalConfig.REVIEWS_KEY)
                .document(GlobalConfig.getCurrentUserId()).get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                actionCallback.onFailed(e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

                long oldStarLevel = documentSnapshot.get(GlobalConfig.STAR_LEVEL_KEY) != null ? documentSnapshot.getLong(GlobalConfig.STAR_LEVEL_KEY) : 0L;

                DocumentReference libraryDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId);
                HashMap<String, Object> libraryDetails = new HashMap<>();

                //remove the old star level
                switch (Math.toIntExact(oldStarLevel)) {
                    case 1:
                        libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 2:
                        libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 3:
                        libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 4:
                        libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 5:
                        libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                }
                switch(Math.toIntExact(newStarLevel)){
                    case 1: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY, FieldValue.increment(1L));
                        break;
                    case 2: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY,  FieldValue.increment(1L));
                        break;
                    case 3: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY,  FieldValue.increment(1L));
                        break;
                    case 4: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY, FieldValue.increment(1L));
                        break;
                    case 5: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY, FieldValue.increment(1L));
                        break;
                }
                writeBatch.set(libraryDocumentReference,libraryDetails,SetOptions.merge());

                DocumentReference libraryReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId).collection(GlobalConfig.REVIEWS_KEY).document(GlobalConfig.getCurrentUserId());
                HashMap<String,Object> libraryReviewDetails = new HashMap<>();
                libraryReviewDetails.put(GlobalConfig.REVIEWER_ID_KEY, GlobalConfig.getCurrentUserId());
                libraryReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
                libraryReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, newStarLevel);
                libraryReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
                libraryReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
                libraryReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
                writeBatch.set(libraryReviewDocumentReference,libraryReviewDetails,SetOptions.merge());


                DocumentReference reviewerDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId()).collection(GlobalConfig.OTHER_REVIEWS_KEY).document(libraryId);
                HashMap<String,Object> reviewerReviewDetails = new HashMap<>();
                reviewerReviewDetails.put(GlobalConfig.AUTHOR_ID_KEY, authorId);
                reviewerReviewDetails.put(GlobalConfig.LIBRARY_ID_KEY, libraryId);
                reviewerReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
                reviewerReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, newStarLevel);
//        reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
                reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
                reviewerReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
                reviewerReviewDetails.put(GlobalConfig.IS_LIBRARY_REVIEW_KEY, true);
                writeBatch.set(reviewerDocumentReference,reviewerReviewDetails, SetOptions.merge());

                writeBatch.commit()
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                actionCallback.onFailed(e.getMessage());

                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                actionCallback.onSuccess();

                            }
                        });
            }
        });



    }
    public static  void editTutorialReview(String authorId,String libraryId,String tutorialId,String comment,String performanceTag,long newStarLevel,ActionCallback actionCallback){


        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                .document(tutorialId).collection(GlobalConfig.REVIEWS_KEY)
                .document(GlobalConfig.getCurrentUserId()).get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                actionCallback.onFailed(e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

                long oldStarLevel = documentSnapshot.get(GlobalConfig.STAR_LEVEL_KEY) != null ? documentSnapshot.getLong(GlobalConfig.STAR_LEVEL_KEY) : 0L;

                DocumentReference tutorialDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId);
                HashMap<String, Object> tutorialDetails = new HashMap<>();

                //remove the old star level
                switch (Math.toIntExact(oldStarLevel)) {
                    case 1:
                        tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 2:
                        tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 3:
                        tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 4:
                        tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 5:
                        tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                }
                switch(Math.toIntExact(newStarLevel)){
                    case 1: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY, FieldValue.increment(1L));
                        break;
                    case 2: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY,  FieldValue.increment(1L));
                        break;
                    case 3: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY,  FieldValue.increment(1L));
                        break;
                    case 4: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY, FieldValue.increment(1L));
                        break;
                    case 5: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY, FieldValue.increment(1L));
                        break;
                }
                writeBatch.set(tutorialDocumentReference,tutorialDetails,SetOptions.merge());

                DocumentReference tutorialReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                        .document(tutorialId).collection(GlobalConfig.REVIEWS_KEY)
                        .document(GlobalConfig.getCurrentUserId());

                HashMap<String,Object> tutorialReviewDetails = new HashMap<>();
                tutorialReviewDetails.put(GlobalConfig.REVIEWER_ID_KEY, GlobalConfig.getCurrentUserId());
                tutorialReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
                tutorialReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, newStarLevel);
//        tutorialReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
                tutorialReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
                tutorialReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
                writeBatch.set(tutorialReviewDocumentReference,tutorialReviewDetails, SetOptions.merge());

                DocumentReference reviewerDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.OTHER_REVIEWS_KEY)
                        .document(tutorialId);

                HashMap<String,Object> reviewerReviewDetails = new HashMap<>();
                reviewerReviewDetails.put(GlobalConfig.AUTHOR_ID_KEY, authorId);
                reviewerReviewDetails.put(GlobalConfig.LIBRARY_CONTAINER_ID_KEY, libraryId);
                reviewerReviewDetails.put(GlobalConfig.TUTORIAL_ID_KEY, tutorialId);
                reviewerReviewDetails.put(GlobalConfig.REVIEW_COMMENT_KEY, comment);
                reviewerReviewDetails.put(GlobalConfig.STAR_LEVEL_KEY, newStarLevel);
//                reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_KEY, GlobalConfig.getDate());
                reviewerReviewDetails.put(GlobalConfig.DATE_REVIEWED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
                reviewerReviewDetails.put(GlobalConfig.PERFORMANCE_TAG_KEY, performanceTag);
                reviewerReviewDetails.put(GlobalConfig.IS_TUTORIAL_REVIEW_KEY, true);
                writeBatch.set(reviewerDocumentReference,reviewerReviewDetails, SetOptions.merge());

                writeBatch.commit()
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                actionCallback.onFailed(e.getMessage());

                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                actionCallback.onSuccess();

                            }
                        });
            }
        });



    }

    public static  void removeAuthorReview(String authorId,ActionCallback actionCallback){

        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(authorId).collection(GlobalConfig.REVIEWS_KEY)
                .document(GlobalConfig.getCurrentUserId())
                .get()
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                actionCallback.onFailed(e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

                long starLevel = documentSnapshot.get(GlobalConfig.STAR_LEVEL_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.STAR_LEVEL_KEY) : 0L;

                DocumentReference authorDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(authorId);
                HashMap<String,Object> authorDetails = new HashMap<>();
                authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_AUTHOR_REVIEWS_KEY, FieldValue.increment(-1L));
                switch(Math.toIntExact(starLevel)){
                    case 1: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 2: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY,  FieldValue.increment(-1L));
                        break;
                    case 3: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY,  FieldValue.increment(-1L));
                        break;
                    case 4: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 5: authorDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                }
                writeBatch.set(authorDocumentReference,authorDetails,SetOptions.merge());


                DocumentReference authorReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(authorId).collection(GlobalConfig.REVIEWS_KEY)
                        .document(GlobalConfig.getCurrentUserId());
                writeBatch.delete(authorReviewDocumentReference);



                DocumentReference reviewerDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.OTHER_REVIEWS_KEY)
                        .document(authorId);
                writeBatch.delete(reviewerDocumentReference);
                writeBatch.commit().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        actionCallback.onSuccess();
                    }
                });

            }
        });
    }
    public static  void removeLibraryReview(String libraryId,ActionCallback actionCallback){

        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_LIBRARY_KEY)
                .document(libraryId).collection(GlobalConfig.REVIEWS_KEY)
                .document(GlobalConfig.getCurrentUserId()).get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                actionCallback.onFailed(e.getMessage());
            }
        })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

                long starLevel = documentSnapshot.get(GlobalConfig.STAR_LEVEL_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.STAR_LEVEL_KEY) : 0L;

                DocumentReference libraryDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId);
                HashMap<String,Object> libraryDetails = new HashMap<>();
                libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_LIBRARY_REACH_KEY, FieldValue.increment(-1L));
                switch(Math.toIntExact(starLevel)){
                    case 1: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 2: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY,  FieldValue.increment(-1L));
                        break;
                    case 3: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY,  FieldValue.increment(-1L));
                        break;
                    case 4: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 5: libraryDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                }
                writeBatch.set(libraryDocumentReference,libraryDetails,SetOptions.merge());


                DocumentReference libraryReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_LIBRARY_KEY)
                        .document(libraryId).collection(GlobalConfig.REVIEWS_KEY)
                        .document(GlobalConfig.getCurrentUserId());
                writeBatch.delete(libraryReviewDocumentReference);



                DocumentReference reviewerDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.OTHER_REVIEWS_KEY)
                        .document(libraryId);
                writeBatch.delete(reviewerDocumentReference);
                writeBatch.commit().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        actionCallback.onSuccess();
                    }
                });

            }
        });






    }
    public static  void removeTutorialReview(String tutorialId,ActionCallback actionCallback){

        GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                .document(tutorialId).collection(GlobalConfig.REVIEWS_KEY)
                .document(GlobalConfig.getCurrentUserId()).get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                actionCallback.onFailed(e.getMessage());
            }
        })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

                long starLevel = documentSnapshot.get(GlobalConfig.STAR_LEVEL_KEY)!=null ? documentSnapshot.getLong(GlobalConfig.STAR_LEVEL_KEY) : 0L;

                DocumentReference tutorialDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId);
                HashMap<String,Object> tutorialDetails = new HashMap<>();
                tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TUTORIAL_REVIEWS_KEY, FieldValue.increment(-1L));
                switch(Math.toIntExact(starLevel)){
                    case 1: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ONE_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 2: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TWO_STAR_RATE_KEY,  FieldValue.increment(-1L));
                        break;
                    case 3: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_THREE_STAR_RATE_KEY,  FieldValue.increment(-1L));
                        break;
                    case 4: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FOUR_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                    case 5: tutorialDetails.put(GlobalConfig.TOTAL_NUMBER_OF_FIVE_STAR_RATE_KEY, FieldValue.increment(-1L));
                        break;
                }
                writeBatch.set(tutorialDocumentReference,tutorialDetails,SetOptions.merge());


                DocumentReference tutorialReviewDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                        .document(tutorialId).collection(GlobalConfig.REVIEWS_KEY)
                        .document(GlobalConfig.getCurrentUserId());
                writeBatch.delete(tutorialReviewDocumentReference);



                DocumentReference reviewerDocumentReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.OTHER_REVIEWS_KEY)
                        .document(tutorialId);
                writeBatch.delete(reviewerDocumentReference);
                writeBatch.commit().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        actionCallback.onSuccess();
                    }
                });

            }
        });






    }


    public static void deletePage(String authorId,String tutorialId,String folderId,String pageId,boolean isTutorialPage, ActionCallback actionCallback){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        if(isTutorialPage){
            DocumentReference tutorialDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId);
            HashMap<String,Object> details = new HashMap<>();
            details.put(TOTAL_NUMBER_OF_PAGES_CREATED_KEY,FieldValue.increment(-1L));
            writeBatch.update(tutorialDocumentReference,details);

            DocumentReference pageDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(ALL_TUTORIAL_PAGES_KEY).document(pageId);
            writeBatch.delete(pageDocumentReference);

            writeBatch.commit().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    actionCallback.onFailed(e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    actionCallback.onSuccess();
                }
            });


        }else{
            DocumentReference folderDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(ALL_FOLDERS_KEY).document(folderId);
            HashMap<String,Object> details = new HashMap<>();
            details.put(TOTAL_NUMBER_OF_PAGES_CREATED_KEY,FieldValue.increment(-1L));
            writeBatch.update(folderDocumentReference,details);

            DocumentReference pageDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(ALL_FOLDERS_KEY).document(folderId).collection(ALL_FOLDER_PAGES_KEY).document(pageId);
            writeBatch.delete(pageDocumentReference);

            writeBatch.commit().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    actionCallback.onFailed(e.getMessage());

                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    actionCallback.onSuccess();
                }
            });
        }
    }

    /**
     * Inflates a menu from the resource folder
     * @param context the context of the invocation
     * @param menuRes the int that represents the menu to be inflated from the res folder
     * @param anchorView the view that will be used to indicate the point to inflate the menu
     * @param onMenuItemClickListener the listener that triggers when the menu is clicked
     * */
    public static void createPopUpMenu(Context context, int menuRes, View anchorView, OnMenuItemClickListener onMenuItemClickListener) {


        PopupMenu popupMenu = new PopupMenu(context, anchorView);
        popupMenu.getMenuInflater().inflate(menuRes, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMenuItemClickListener.onMenuItemClicked(item);
                return false;

            }
        });
        popupMenu.show();


    }

    /**
     * This method generates keywords which is used for performing search operation in the database, it trims the given {@link String}
     * into words which enables us query and match each word for similarity in the database.
     * @param itemName the {@link String} to be trimmed
     * @return {@link ArrayList} the list that contains the keywords
     * */
    static ArrayList<String> generateSearchVerbatimKeyWords(@NonNull String itemName){
        ArrayList<String> searchVerbatimKeywordsArrayList = new ArrayList<>();

        if(itemName != null && !itemName.isEmpty()) {
            itemName = itemName.toLowerCase();
            int itemLength = itemName.length();
            searchVerbatimKeywordsArrayList.add(itemName);
            searchVerbatimKeywordsArrayList.addAll(Arrays.asList(itemName.split(" ")));

        }else{
            searchVerbatimKeywordsArrayList = new ArrayList<>();
        }

        return searchVerbatimKeywordsArrayList;
    }

    /**
     * This method generates keywords which is used for performing search operation in the database, it trims the given {@link String}
     * into {@link Character} which enables us query and match each word for similarity in the database.
     * @param itemName the {@link String} to be trimmed
     * @return {@link ArrayList} the list that contains the keywords
     * */

    static ArrayList<String> generateSearchAnyMatchKeyWords(@NonNull String itemName) {
        ArrayList<String> searchAnyMatchKeywordsArrayList = new ArrayList<>();

        if (itemName != null && !itemName.isEmpty()) {
            itemName = itemName.toLowerCase();
            int itemLength = itemName.length();

            if (itemLength != 0) {
                for (int i = 0; i < itemLength; i++) {
                    searchAnyMatchKeywordsArrayList.add(itemName.substring(i));


                    for (int j = itemLength - 1; j > i; j--) {

                        searchAnyMatchKeywordsArrayList.add(itemName.substring(i, j));


                    }


                }

                for (int i = 0; i < itemLength; i++) {
                    for (int k = i + 1; k < itemLength; k++) {
                        if (!searchAnyMatchKeywordsArrayList.contains(itemName.charAt(i) + "" + itemName.charAt(k))) {

                            searchAnyMatchKeywordsArrayList.add(itemName.charAt(i) + "" + itemName.charAt(k));

                        }
                    }

                }
            }
        }
            return searchAnyMatchKeywordsArrayList;
        }

        /**
         * Returns the local date when an action was performed
         * @return {@link String} the string that represents the date value
         * */
        @Deprecated
      static  public String getDate(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd', 'HH:mm:ss", Locale.US);
        Date date = new Date();
        String formattedDate = simpleDateFormat.format(date);


        return formattedDate;
    }

      static  public int getEventSeconds(){
        return  Integer.parseInt(new SimpleDateFormat("ss", Locale.US).format(new Date()));
        }
      static  public int getEventMinute(){
            return  Integer.parseInt(new SimpleDateFormat("mm", Locale.US).format(new Date()));
        }
      static  public int getEventHour(){
          return  Integer.parseInt(new SimpleDateFormat("HH", Locale.US).format(new Date()));

        }
      static  public int getEventDay(){
          return  Integer.parseInt(new SimpleDateFormat("dd", Locale.US).format(new Date()));

        }
      static  public int getEventWeekOfYear(){
//          return  Integer.parseInt(new SimpleDateFormat("dd", Locale.US).format(new Date()));
          return  Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);

        }
      static  public int getEventMonth(){
          return  Integer.parseInt(new SimpleDateFormat("MM", Locale.US).format(new Date()));

        }
      static  public int getEventYear(){
        return  Integer.parseInt(new SimpleDateFormat("yyyy", Locale.US).format(new Date()));

    }


    static  public FieldValue getServerTimeStamp(){

        return FieldValue.serverTimestamp();
    }


    public static ArrayList<WelcomeScreenItemModal> getWelcomeScreenItemsList(){
        ArrayList<WelcomeScreenItemModal> list = new ArrayList<>();
        list.add(new WelcomeScreenItemModal(R.drawable.undraw_save_to_bookmarks, "Unlimited Personal Library","You can create infinite numbers of libraries for personal use"));
        list.add(new WelcomeScreenItemModal(R.drawable.undraw_personal_notebook,"Bookmark your interests","You can easily customize your favourite books and libraries."));
        list.add(new WelcomeScreenItemModal(R.drawable.undraw_online_reading_np7n,"Experience of High level reading","Experience the reading high level of books from authors worldwide for free"));

        return list;
    }
//
    public static void updateActivityLog(String activityLogType, String authorId, String libraryId, String tutorialId,String tutorialFolderId ,String pageId ,String reviewId , ActionCallback actionCallback){
            String activityLogId = getRandomString(10);
            HashMap<String,Object> activityLogDetails = new HashMap<>();
//            activityLogDetails.put(LOG_NOTE_KEY,);
            activityLogDetails.put(ACTION_DOER_ID_KEY,getCurrentUserId());
            activityLogDetails.put(EVENT_SECONDS_KEY,(long)getEventSeconds());
            activityLogDetails.put(EVENT_MINUTE_KEY,(long)getEventMinute());
            activityLogDetails.put(EVENT_HOUR_KEY,(long)getEventHour());
            activityLogDetails.put(EVENT_DAY_KEY,(long)getEventDay());
            activityLogDetails.put(EVENT_WEEK_KEY,(long)getEventWeekOfYear());
            activityLogDetails.put(EVENT_MONTH_KEY,(long)getEventMonth());
            activityLogDetails.put(EVENT_YEAR_KEY,(long)getEventYear());
            activityLogDetails.put(LOG_TIME_STAMP_KEY,FieldValue.serverTimestamp());
            activityLogDetails.put(ACTIVITY_LOG_TYPE_KEY,activityLogType);
            activityLogDetails.put(AUTHOR_ID_KEY,authorId);
            activityLogDetails.put(LIBRARY_ID_KEY,libraryId);
            activityLogDetails.put(TUTORIAL_ID_KEY,tutorialId);
            activityLogDetails.put(FOLDER_ID_KEY,tutorialFolderId);
            activityLogDetails.put(PAGE_ID_KEY,pageId);
//            activityLogDetails.put(TUTORIAL_PAGE_ID_KEY,tutorialPageId);
//            activityLogDetails.put(FOLDER_PAGE_ID_KEY,folderPageId);
            activityLogDetails.put(REVIEWER_ID_KEY,reviewId);
//
//            activityLogDetails.put(IS_AUTHOR_AFFECTED_KEY,isAuthorAffected);
//            activityLogDetails.put(IS_LIBRARY_AFFECTED_KEY,isLibraryAffected);
//            activityLogDetails.put(IS_TUTORIAL_AFFECTED_KEY,isTutorialAffected);
//            activityLogDetails.put(IS_TUTORIAL_FOLDER_AFFECTED_KEY,isTutorialFolderAffected);
//            activityLogDetails.put(IS_TUTORIAL_PAGE_AFFECTED_KEY,isTutorialPageAffected);
//            activityLogDetails.put(IS_FOLDER_PAGE_AFFECTED_KEY,isFolderPageAffected);


        getFirebaseFirestoreInstance()
                .collection(ALL_USERS_KEY)
                .document(getCurrentUserId())
                .collection(USER_ACTIVITY_LOG_KEY)
                .document(activityLogId)
                .set(activityLogDetails)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        actionCallback.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        actionCallback.onSuccess();
                    }
                });

        switch(activityLogType){
            case GlobalConfig.ACTIVITY_LOG_USER_REVIEW_AUTHOR_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_DELETE_AUTHOR_REVIEW_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_AUTHOR_REVIEW_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_SIGN_UP_TYPE_KEY:

                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_SIGN_IN_TYPE_KEY:

                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_ACCOUNT_TYPE_KEY:

                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_VISIT_AUTHOR_TYPE_KEY:

                ;
                return;

            case GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_LIBRARY_TYPE_KEY:

                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_DELETE_LIBRARY_TYPE_KEY:

                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_LIBRARY_TYPE_KEY:

                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_REVIEW_LIBRARY_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_LIBRARY_REVIEW_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_DELETE_LIBRARY_REVIEW_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_VISIT_LIBRARY_TYPE_KEY:

                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_BOOK_MARK_LIBRARY_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_LIBRARY_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_TYPE_KEY:

                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_VISIT_TUTORIAL_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_REVIEW_TUTORIAL_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_DELETE_TUTORIAL_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_DELETE_TUTORIAL_REVIEW_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_TUTORIAL_REVIEW_TYPE_KEY:

                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_TUTORIAL_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_BOOK_MARK_TUTORIAL_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_TUTORIAL_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_FOLDER_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_TUTORIAL_FOLDER_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_DELETE_TUTORIAL_FOLDER_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_TUTORIAL_PAGE_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_TUTORIAL_PAGE_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_DELETE_TUTORIAL_PAGE_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_CREATE_NEW_FOLDER_PAGE_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_EDIT_FOLDER_PAGE_TYPE_KEY:
                ;
                return;
            case GlobalConfig.ACTIVITY_LOG_USER_DELETE_FOLDER_PAGE_TYPE_KEY:
                ;
                return;
        }

    }

    /**This method returns the average number of the item's star rating
     *
     * */
    static float getStar(long fiveStar,long fourStar, long threeStar, long twoStar, long oneStar) {

        if(fiveStar==fourStar && fiveStar==threeStar && fiveStar==twoStar && fiveStar==oneStar){
            if(fiveStar==0){
                return 0.0F;
            }else {
                return 3.0F;
            }
        }

        if(fiveStar==fourStar && fiveStar==threeStar && fiveStar==twoStar && fiveStar>oneStar){
            return 3.0F;
        }

        if(fiveStar==fourStar && fiveStar==threeStar && fiveStar>twoStar && fiveStar>oneStar){
            return 3.5F;
        }

        if(fiveStar==fourStar && fiveStar>threeStar && fiveStar==twoStar && fiveStar>oneStar){
            return 3.0F;
        }
        if(fiveStar>fourStar && fiveStar==threeStar && fiveStar>twoStar && fiveStar==oneStar){
            return 2.5F;
        }
        if(threeStar>fiveStar && threeStar>fourStar && threeStar==twoStar && threeStar==oneStar){
            return 2.0F;
        }

        if(fiveStar==fourStar && fiveStar==threeStar && fiveStar>twoStar && fiveStar==oneStar){
            return 3.0F;
        }

        if(fiveStar==fourStar && fiveStar>threeStar && fiveStar==twoStar && fiveStar==oneStar){
            return 2.0F;
        }

        if(fiveStar>fourStar && fiveStar==threeStar && fiveStar==twoStar && fiveStar==oneStar){
            return 1.5F;
        }
        if(fourStar>fiveStar && fourStar==threeStar && fourStar==twoStar && fourStar==oneStar){
                    return 2F;
                }
        if(fourStar>fiveStar && fourStar==threeStar && fourStar>twoStar && fourStar==oneStar){
                    return 2.5F;
                }

        if(fiveStar>fourStar && fiveStar>threeStar && fiveStar>twoStar && fiveStar>oneStar){
            return 5.0F;
        }
        if(fiveStar==fourStar && fiveStar>threeStar && fiveStar>twoStar && fiveStar>oneStar){
            return 4.5F;
        }
        if(fiveStar>fourStar && fiveStar==threeStar && fiveStar>twoStar && fiveStar>oneStar){
            return 4.0F;
        }
        if(fiveStar>fourStar && fiveStar>threeStar && fiveStar==twoStar && fiveStar>oneStar){
            return 3.0F;
        }
        if(fiveStar>fourStar && fiveStar>threeStar && fiveStar>twoStar && fiveStar==oneStar){
            return 2.5F;
        }
        if(fourStar>fiveStar && fourStar>threeStar && fourStar>twoStar && fourStar>oneStar){
            return 4.0F;
        }
        if(fourStar>fiveStar && fourStar==threeStar && fourStar>twoStar && fourStar>oneStar){
            return 3.5F;
        }
        if(fourStar>fiveStar && fourStar>threeStar && fourStar==twoStar && fourStar>oneStar){
            return 2.5F;
        }
        if(fourStar>fiveStar && fourStar>threeStar && fourStar>twoStar && fourStar==oneStar){
            return 1.5F;
        }
        if(threeStar>fiveStar && threeStar>fourStar && threeStar>twoStar && threeStar>oneStar){
            return 3.0F;
        }
        if(threeStar>fiveStar && threeStar>fourStar && threeStar==twoStar && threeStar>oneStar){
            return 2.5F;
        }
        if(threeStar>fiveStar && threeStar>fourStar && threeStar>twoStar && threeStar==oneStar){
            return 2.0F;
        }
        if(twoStar>fiveStar && twoStar>fourStar && twoStar>threeStar && twoStar>oneStar){
            return 2.0F;
        }
        if(twoStar>fiveStar && twoStar>fourStar && twoStar>threeStar && twoStar==oneStar){
            return 1.5F;
        }
        if(oneStar>fiveStar && oneStar>fourStar && oneStar>threeStar && oneStar>twoStar){
            return 1.0F;
        }



        return 0.0F;
    }

    public static Intent getHostActivityIntent(Context context,@Nullable Intent intent,String fragmentOpenType, String userId){
        if(intent == null){
            intent = new Intent(context,HostActivity.class);
        }

        intent.putExtra(GlobalConfig.FRAGMENT_TYPE_KEY,fragmentOpenType);
        intent.putExtra(GlobalConfig.USER_ID_KEY,userId);

        return  intent;
    }


    public static void removeBookmark(String authorId, String libraryId, String tutorialId,String folderId,String pageId,String type, ActionCallback actionCallback) {
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();

        DocumentReference bookMarkOwnerReference = null;
        switch (type) {
            case AUTHOR_TYPE_KEY:
                bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(authorId);
                break;

            case LIBRARY_TYPE_KEY:
                bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(libraryId);
                break;

            case TUTORIAL_TYPE_KEY:
                bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(tutorialId);
                break;


            case FOLDER_TYPE_KEY:
                bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(folderId);
                break;

            case TUTORIAL_PAGE_TYPE_KEY:
                bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(pageId);
                break;


            case FOLDER_PAGE_TYPE_KEY:
                bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(pageId);
                break;

        }
        writeBatch.delete(bookMarkOwnerReference);


        DocumentReference bookMarkReference = null;
        switch (type) {
            case AUTHOR_TYPE_KEY:
                bookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(authorId)
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(GlobalConfig.getCurrentUserId());
                break;

            case LIBRARY_TYPE_KEY:
                bookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_LIBRARY_KEY)
                        .document(libraryId)
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(GlobalConfig.getCurrentUserId());
                break;

            case TUTORIAL_TYPE_KEY:
                bookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                        .document(tutorialId)
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(GlobalConfig.getCurrentUserId());
                break;

            case FOLDER_TYPE_KEY:
                bookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                        .document(tutorialId)
                        .collection(ALL_FOLDERS_KEY)
                        .document(folderId)
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(GlobalConfig.getCurrentUserId());
                break;

            case TUTORIAL_PAGE_TYPE_KEY:
                bookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                        .document(tutorialId)
                        .collection(ALL_TUTORIAL_PAGES_KEY)
                        .document(pageId)
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(GlobalConfig.getCurrentUserId());
                break;

            case FOLDER_PAGE_TYPE_KEY:
                bookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                        .document(tutorialId)
                        .collection(ALL_FOLDERS_KEY)
                        .document(folderId)
                        .collection(ALL_FOLDER_PAGES_KEY)
                        .document(pageId)
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(GlobalConfig.getCurrentUserId());
                break;


        }
        writeBatch.delete(bookMarkReference);


        DocumentReference numOfBookMarkReference = null;
        switch (type) {
            case AUTHOR_TYPE_KEY:
                numOfBookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(authorId);
                break;

            case LIBRARY_TYPE_KEY:
            numOfBookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_LIBRARY_KEY)
                    .document(libraryId);
                break;

            case TUTORIAL_TYPE_KEY:
            numOfBookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                    .document(tutorialId);
                break;

            case FOLDER_TYPE_KEY:
            numOfBookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                    .document(tutorialId)
                    .collection(ALL_FOLDERS_KEY)
                    .document(folderId);
                break;

            case TUTORIAL_PAGE_TYPE_KEY:
            numOfBookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                    .document(tutorialId)
                    .collection(ALL_TUTORIAL_PAGES_KEY)
                    .document(pageId);
                break;

            case FOLDER_PAGE_TYPE_KEY:
            numOfBookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                    .document(tutorialId)
                    .collection(ALL_FOLDERS_KEY)
                    .document(folderId)
                    .collection(ALL_FOLDER_PAGES_KEY)
                    .document(pageId);
            break;


    }
        //INCREASE THE NUMBER OF BOOKMARKS MADE ON ANY OF THESE OBJECTS LIKE "AUTHOR,LIBRARY,TUTORIAL, etc..."
        HashMap<String,Object> numOfBookmarkDetails = new HashMap<>();
        numOfBookmarkDetails.put(TOTAL_NUMBER_OF_BOOK_MARKS_KEY, FieldValue.increment(-1L));
        writeBatch.set(numOfBookMarkReference,numOfBookmarkDetails,SetOptions.merge());


        //INCREASE NUMBER OF BOOKMARKS THIS USER HAS MADE
      DocumentReference  bookMarkerReference = GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(GlobalConfig.getCurrentUserId());

        HashMap<String,Object> bookMarkerDetails = new HashMap<>();
        bookMarkerDetails.put(TOTAL_NUMBER_OF_OTHER_BOOK_MARKS_KEY, FieldValue.increment(-1L));
        writeBatch.set(bookMarkerReference,bookMarkerDetails,SetOptions.merge());



        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        actionCallback.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String activityLogType = "NONE";
                        switch (type) {
                            case AUTHOR_TYPE_KEY:
                            activityLogType = GlobalConfig.ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_AUTHOR_TYPE_KEY;
                                break;

                            case LIBRARY_TYPE_KEY:
                                activityLogType = GlobalConfig.ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_LIBRARY_TYPE_KEY;
                                break;

                            case TUTORIAL_TYPE_KEY:
                                activityLogType = GlobalConfig.ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_TUTORIAL_TYPE_KEY;
                                break;

                            case FOLDER_TYPE_KEY:
                                activityLogType = GlobalConfig.ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_TUTORIAL_FOLDER_TYPE_KEY;
                                break;

                            case TUTORIAL_PAGE_TYPE_KEY:
                                activityLogType = GlobalConfig.ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_TUTORIAL_PAGE_TYPE_KEY;
                                break;

                            case FOLDER_PAGE_TYPE_KEY:
                                activityLogType = GlobalConfig.ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_FOLDER_PAGE_TYPE_KEY;
                                break;
                    }

                            GlobalConfig.updateActivityLog(activityLogType, authorId, libraryId, tutorialId, folderId, pageId, null,  new GlobalConfig.ActionCallback() {
                                @Override
                                public void onSuccess() {
                                actionCallback.onSuccess();
                                }

                                @Override
                                public void onFailed(String errorMessage) {
                                    actionCallback.onSuccess();

                                }
                            });


                    }
                });

    }

    public static void addToBookmark(String authorId, String libraryId, String tutorialId,String folderId,String pageId,String type, ActionCallback actionCallback) {
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();

        DocumentReference bookMarkOwnerReference = null;
        switch (type) {
            case AUTHOR_TYPE_KEY:
                bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(authorId);
                break;

            case LIBRARY_TYPE_KEY:
                bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(libraryId);
                break;

            case TUTORIAL_TYPE_KEY:
                bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(tutorialId);
                break;


            case FOLDER_TYPE_KEY:
                bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(folderId);
                break;

            case TUTORIAL_PAGE_TYPE_KEY:
                bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(pageId);
                break;


            case FOLDER_PAGE_TYPE_KEY:
                bookMarkOwnerReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(GlobalConfig.getCurrentUserId())
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(pageId);
                break;

        }

        HashMap<String, Object> bookmarkOwnerDetails = new HashMap<>();
        bookmarkOwnerDetails.put(TYPE_KEY, type);
        bookmarkOwnerDetails.put(AUTHOR_ID_KEY, authorId);
        bookmarkOwnerDetails.put(LIBRARY_ID_KEY, libraryId);
        bookmarkOwnerDetails.put(TUTORIAL_ID_KEY, tutorialId);
        bookmarkOwnerDetails.put(FOLDER_ID_KEY, folderId);
        bookmarkOwnerDetails.put(PAGE_ID_KEY, pageId);
//        bookmarkOwnerDetails.put(FOLDER_PAGE_ID_KEY,pageId);
//        bookmarkOwnerDetails.put(TUTORIAL_PAGE_ID_KEY,pageId);
        bookmarkOwnerDetails.put(DATE_TIME_STAMP_BOOK_MARKED_KEY, FieldValue.serverTimestamp());
        writeBatch.set(bookMarkOwnerReference, bookmarkOwnerDetails, SetOptions.merge());


        DocumentReference bookMarkReference = null;
        switch (type) {
            case AUTHOR_TYPE_KEY:
                bookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(authorId)
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(GlobalConfig.getCurrentUserId());
                break;

            case LIBRARY_TYPE_KEY:
                bookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_LIBRARY_KEY)
                        .document(libraryId)
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(GlobalConfig.getCurrentUserId());
                break;

            case TUTORIAL_TYPE_KEY:
                bookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                        .document(tutorialId)
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(GlobalConfig.getCurrentUserId());
                break;

            case FOLDER_TYPE_KEY:
                bookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                        .document(tutorialId)
                        .collection(ALL_FOLDERS_KEY)
                        .document(folderId)
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(GlobalConfig.getCurrentUserId());
                break;

            case TUTORIAL_PAGE_TYPE_KEY:
                bookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                        .document(tutorialId)
                        .collection(ALL_TUTORIAL_PAGES_KEY)
                        .document(pageId)
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(GlobalConfig.getCurrentUserId());
                break;

            case FOLDER_PAGE_TYPE_KEY:
                bookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                        .document(tutorialId)
                        .collection(ALL_FOLDERS_KEY)
                        .document(folderId)
                        .collection(ALL_FOLDER_PAGES_KEY)
                        .document(pageId)
                        .collection(GlobalConfig.BOOK_MARKS_KEY).document(GlobalConfig.getCurrentUserId());
                break;


        }
        HashMap<String, Object> bookmarkDetails = new HashMap<>();
        bookmarkDetails.put(BOOK_MARKER_USER_ID_KEY, getCurrentUserId());
        bookmarkDetails.put(AUTHOR_ID_KEY, authorId);
        bookmarkDetails.put(LIBRARY_ID_KEY, libraryId);
        bookmarkDetails.put(TUTORIAL_ID_KEY, tutorialId);
        bookmarkDetails.put(FOLDER_ID_KEY, folderId);
        bookmarkDetails.put(PAGE_ID_KEY, pageId);
//        bookmarkDetails.put(TUTORIAL_PAGE_ID_KEY,pageId);
//        bookmarkDetails.put(FOLDER_PAGE_ID_KEY,pageId);
        bookmarkDetails.put(DATE_TIME_STAMP_BOOK_MARKED_KEY, FieldValue.serverTimestamp());
        writeBatch.set(bookMarkReference, bookmarkDetails, SetOptions.merge());


        DocumentReference numOfBookMarkReference = null;
        switch (type) {
            case AUTHOR_TYPE_KEY:
                numOfBookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                        .collection(GlobalConfig.ALL_USERS_KEY)
                        .document(authorId);
                break;

            case LIBRARY_TYPE_KEY:
            numOfBookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_LIBRARY_KEY)
                    .document(libraryId);
                break;

            case TUTORIAL_TYPE_KEY:
            numOfBookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                    .document(tutorialId);
                break;

            case FOLDER_TYPE_KEY:
            numOfBookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                    .document(tutorialId)
                    .collection(ALL_FOLDERS_KEY)
                    .document(folderId);
                break;

            case TUTORIAL_PAGE_TYPE_KEY:
            numOfBookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                    .document(tutorialId)
                    .collection(ALL_TUTORIAL_PAGES_KEY)
                    .document(pageId);
                break;

            case FOLDER_PAGE_TYPE_KEY:
            numOfBookMarkReference = GlobalConfig.getFirebaseFirestoreInstance()
                    .collection(GlobalConfig.ALL_TUTORIAL_KEY)
                    .document(tutorialId)
                    .collection(ALL_FOLDERS_KEY)
                    .document(folderId)
                    .collection(ALL_FOLDER_PAGES_KEY)
                    .document(pageId);
            break;


    }
        //INCREASE THE NUMBER OF BOOKMARKS MADE ON ANY OF THESE OBJECTS LIKE "AUTHOR,LIBRARY,TUTORIAL, etc..."
        HashMap<String,Object> numOfBookmarkDetails = new HashMap<>();
        numOfBookmarkDetails.put(TOTAL_NUMBER_OF_BOOK_MARKS_KEY, FieldValue.increment(1L));
        numOfBookmarkDetails.put(LAST_DATE_TIME_STAMP_BOOK_MARKED_KEY, FieldValue.serverTimestamp());
        writeBatch.set(numOfBookMarkReference,numOfBookmarkDetails,SetOptions.merge());


        //INCREASE NUMBER OF BOOKMARKS THIS USER HAS MADE
      DocumentReference  bookMarkerReference = GlobalConfig.getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_USERS_KEY)
                .document(GlobalConfig.getCurrentUserId());

        HashMap<String,Object> bookMarkerDetails = new HashMap<>();
        bookMarkerDetails.put(TOTAL_NUMBER_OF_OTHER_BOOK_MARKS_KEY, FieldValue.increment(1L));
        bookMarkerDetails.put(LAST_DATE_TIME_STAMP_BOOK_MARKED_OTHER_KEY, FieldValue.serverTimestamp());
        writeBatch.set(bookMarkerReference,bookMarkerDetails,SetOptions.merge());



        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        actionCallback.onFailed(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String activityLogType = "NONE";
                        switch (type) {
                            case AUTHOR_TYPE_KEY:
                            activityLogType = GlobalConfig.ACTIVITY_LOG_USER_BOOK_MARK_AUTHOR_TYPE_KEY;
                                break;

                            case LIBRARY_TYPE_KEY:
                                activityLogType = GlobalConfig.ACTIVITY_LOG_USER_BOOK_MARK_LIBRARY_TYPE_KEY;
                                break;

                            case TUTORIAL_TYPE_KEY:
                                activityLogType = GlobalConfig.ACTIVITY_LOG_USER_BOOK_MARK_TUTORIAL_TYPE_KEY;
                                break;

                            case FOLDER_TYPE_KEY:
                                activityLogType = GlobalConfig.ACTIVITY_LOG_USER_BOOK_MARK_FOLDER_TYPE_KEY;
                                break;

                            case TUTORIAL_PAGE_TYPE_KEY:
                                activityLogType = GlobalConfig.ACTIVITY_LOG_USER_BOOK_MARK_TUTORIAL_PAGE_TYPE_KEY;
                                break;

                            case FOLDER_PAGE_TYPE_KEY:
                                activityLogType = GlobalConfig.ACTIVITY_LOG_USER_BOOK_MARK_FOLDER_PAGE_TYPE_KEY;
                                break;
                    }

                            GlobalConfig.updateActivityLog(activityLogType, authorId, libraryId, tutorialId, folderId, pageId, null,  new GlobalConfig.ActionCallback() {
                                @Override
                                public void onSuccess() {
//
//                                                GlobalHelpers.showAlertMessage("success",
//                                                        CreateNewLibraryActivity.this,
//                                                        "Library Created Successfully.",
//                                                        "You have successfully created your library,thanks and go ahead and contribute to Learn Era ");
                                actionCallback.onSuccess();
                                }

                                @Override
                                public void onFailed(String errorMessage) {
                                    actionCallback.onSuccess();

                                }
                            });


                    }
                });

    }

    public static void incrementNumberOfVisitors(String authorId, String libraryId, String tutorialId,String folderId,String pageId, boolean isAuthor,  boolean isLibrary,  boolean isTutorial,   boolean isFolder,   boolean isTutorialPage,   boolean isFolderPage){
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();

    if(isAuthor){
        GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_VISIT_AUTHOR_TYPE_KEY, authorId, null, null, null,  null, null, new GlobalConfig.ActionCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(String errorMessage) {
            }
        });
        DocumentReference documentReference = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(authorId);
        HashMap<String,Object> details = new HashMap<>();
        details.put(TOTAL_NUMBER_OF_USER_PROFILE_VISITORS_KEY,FieldValue.increment(1L));
        writeBatch.update(documentReference,details);

        writeBatch.commit();
        recordLastViewedAuthors(authorId);
        return;
    }
    else if(isLibrary){
        GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_VISIT_LIBRARY_TYPE_KEY, authorId, libraryId, null, null, null, null, new GlobalConfig.ActionCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(String errorMessage) {
            }
        });
        DocumentReference documentReference = getFirebaseFirestoreInstance().collection(ALL_LIBRARY_KEY).document(libraryId);
        HashMap<String,Object> details = new HashMap<>();
        details.put(TOTAL_NUMBER_OF_LIBRARY_VISITOR_KEY,FieldValue.increment(1L));
        writeBatch.update(documentReference,details);

        writeBatch.commit();
        recordLastViewedLibrary(libraryId);
        return;
    }
    else if(isTutorial){
        GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_VISIT_TUTORIAL_TYPE_KEY, authorId, libraryId, tutorialId, null, null, null, new GlobalConfig.ActionCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(String errorMessage) {
            }
        });
        DocumentReference documentReference = getFirebaseFirestoreInstance().collection(ALL_TUTORIAL_KEY).document(tutorialId);
        HashMap<String,Object> details = new HashMap<>();
        details.put(TOTAL_NUMBER_OF_TUTORIAL_VISITOR_KEY,FieldValue.increment(1L));
        writeBatch.update(documentReference,details);

        writeBatch.commit();
        recordLastViewedTutorials(tutorialId);
        return;
    }
    else if(isFolder){
        GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_VISIT_FOLDER_TYPE_KEY, authorId, libraryId, tutorialId, folderId,  null, null, new GlobalConfig.ActionCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(String errorMessage) {
            }
        });
        DocumentReference documentReference = getFirebaseFirestoreInstance().collection(ALL_TUTORIAL_KEY).document(tutorialId).collection(ALL_FOLDERS_KEY).document(folderId);
        HashMap<String,Object> details = new HashMap<>();
        details.put(TOTAL_NUMBER_OF_FOLDER_VISITOR_KEY,FieldValue.increment(1L));
        writeBatch.update(documentReference,details);

        writeBatch.commit();
        return;
    }
    else if(isTutorialPage){
        GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_VISIT_TUTORIAL_PAGE_TYPE_KEY, authorId, libraryId, tutorialId, null, pageId,  null, new GlobalConfig.ActionCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(String errorMessage) {
            }
        });
        DocumentReference documentReference = getFirebaseFirestoreInstance().collection(ALL_TUTORIAL_KEY).document(tutorialId).collection(ALL_TUTORIAL_PAGES_KEY).document(pageId);
        HashMap<String,Object> details = new HashMap<>();
        details.put(TOTAL_NUMBER_OF_TUTORIAL_PAGE_VISITOR_KEY,FieldValue.increment(1L));
        writeBatch.update(documentReference,details);

        writeBatch.commit();
        return;
    }
    else if(isFolderPage){
        GlobalConfig.updateActivityLog(GlobalConfig.ACTIVITY_LOG_USER_VISIT_FOLDER_PAGE_TYPE_KEY, authorId, libraryId, tutorialId, null,  pageId, null, new GlobalConfig.ActionCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(String errorMessage) {
            }
        });
        DocumentReference documentReference = getFirebaseFirestoreInstance().collection(ALL_TUTORIAL_KEY).document(tutorialId).collection(ALL_FOLDERS_KEY).document(folderId).collection(ALL_FOLDER_PAGES_KEY).document(pageId);
        HashMap<String,Object> details = new HashMap<>();
        details.put(TOTAL_NUMBER_OF_FOLDER_PAGE_VISITOR_KEY,FieldValue.increment(1L));
        writeBatch.update(documentReference,details);

        writeBatch.commit();
        return;
    }
    }

    public static void setHtmlText(Context context,@NonNull TextView textView, String text){
        if(android.os.Build.VERSION.SDK_INT >=26){
            textView.setText(Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY));

        }else{
            textView.setText(Html.fromHtml(text));

        }
    }

    public static SpannableStringBuilder interpretStyles(Context context, StringBuilder stringBuilder){
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(stringBuilder);
//        SpannableString spannableString = new SpannableString(string);

        String stringForm = stringBuilder.toString();
        if(stringForm.contains("<p>") && stringForm.contains("</p>") ) {
            String[] strings = stringForm.split("<p>");
            for (String stringHalfParagraph : strings) {
                if(stringHalfParagraph.contains("</p>")){
                    String stringFullParagraph = stringHalfParagraph.split("</p>")[0];

                    int startIndex = stringForm.indexOf("<p>"+stringFullParagraph+"</p>");
                    int endIndex = startIndex + ("<p>"+stringFullParagraph+"</p>").length();

                    ForegroundColorSpan foregroundColorSpan1 = new ForegroundColorSpan(context.getResources().getColor(R.color.teal_200, context.getTheme()));
                    spannableStringBuilder.setSpan(foregroundColorSpan1, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//
//                spannableStringBuilder.replace(endIndex, endIndex+4,"\n");
//                spannableStringBuilder.replace(startIndex-3, startIndex ,"\n    ");
//                stringBuilder.replace(endIndex , endIndex+4,"\n");
//                stringBuilder.replace(startIndex-3, startIndex,  "\n    ");

                    spannableStringBuilder.replace(endIndex-4, endIndex,"\n");
                    spannableStringBuilder.replace(startIndex , startIndex +3,"\n    ");
                    stringBuilder.replace(endIndex-4 , endIndex,"\n");
                    stringBuilder.replace(startIndex, startIndex +3,  "\n    ");
//
//                spannableStringBuilder.delete(endIndex, endIndex+4);
//                spannableStringBuilder.delete(startIndex-3, startIndex );
//                stringBuilder.delete(endIndex , endIndex+4);
//                stringBuilder.delete(startIndex-3, startIndex  );


                    stringForm = stringBuilder.toString();
                    strings = stringForm.split("<p>");
                }
            }
        }
        return spannableStringBuilder;
    }


    public static ArrayList<String> getLastViewedTutorialsArray(){

           return lastViewedTutorialsIdArray;
    }
    public static void setLastViewedTutorialsArray(ArrayList<String> lastViewedTutorialsIdArray){

            GlobalConfig.lastViewedTutorialsIdArray = lastViewedTutorialsIdArray;
    }
    public static void recordLastViewedTutorials(String tutorialId){
        if(getLastViewedTutorialsArray().contains(tutorialId))return;
        int index;
        if(getLastViewedTutorialsArray().size()==0){
            index = 0;
        }else{
            index = new Random().nextInt(getLastViewedLibraryArray().size());
        }
        ArrayList<String> lastViewedTutorialsIdList = getLastViewedTutorialsArray();
        HashMap<String,Object> lastViewDetails = new HashMap<>();
        if(lastViewedTutorialsIdList.size()<10){
            index = lastViewedTutorialsIdList.size();
        }
        lastViewedTutorialsIdList.add(index,tutorialId);
        lastViewDetails.put(LAST_VIEWED_LIBRARY_ARRAY_KEY,lastViewedTutorialsIdList);
        getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).update(lastViewDetails);
    }


    public static ArrayList<String> getLastViewedLibraryArray(){

           return lastViewedLibraryIdArray;
    }
    public static void setLastViewedLibraryArray(ArrayList<String> lastViewedLibraryIdArray){

            GlobalConfig.lastViewedLibraryIdArray = lastViewedLibraryIdArray;
    }
    public static void recordLastViewedLibrary(String libraryId){
        if(getLastViewedLibraryArray().contains(libraryId))return;
        int index;
        if(getLastViewedLibraryArray().size()==0){
            index = 0;
        }else{
            index = new Random().nextInt(getLastViewedLibraryArray().size());
        }
        ArrayList<String> lastViewedLibraryIdList = getLastViewedLibraryArray();
        HashMap<String,Object> lastViewDetails = new HashMap<>();
        if(lastViewedLibraryIdList.size()<10){
            index = lastViewedLibraryIdList.size();
        }
        lastViewedLibraryIdList.add(index,libraryId);
        lastViewDetails.put(LAST_VIEWED_LIBRARY_ARRAY_KEY,lastViewedLibraryIdList);
        getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).update(lastViewDetails);
    }


    public static ArrayList<String> getLastViewedAuthorsArray(){

           return lastViewedAuthorsIdArray;
    }
    public static void setLastViewedAuthorsArray(ArrayList<String> lastViewedAuthorsIdArray){

            GlobalConfig.lastViewedAuthorsIdArray = lastViewedAuthorsIdArray;
    }
    public static void recordLastViewedAuthors(String authorId){
        if(getLastViewedAuthorsArray().contains(authorId))return;
        int index;
        if(getLastViewedAuthorsArray().size()==0){
            index = 0;
        }else{
            index = new Random().nextInt(getLastViewedLibraryArray().size());
        }
        ArrayList<String> lastViewedAuthorsIdList = getLastViewedAuthorsArray();
        HashMap<String,Object> lastViewDetails = new HashMap<>();
        if(lastViewedAuthorsIdList.size()<10){
            index = lastViewedAuthorsIdList.size();
        }
        lastViewedAuthorsIdList.add(index,authorId);
        lastViewDetails.put(LAST_VIEWED_AUTHORS_ARRAY_KEY,lastViewedAuthorsIdList);
        getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).update(lastViewDetails);
    }


    /*
        static HashMap<String,Double> getStarMap(int fiveStar,int fourStar, int threeStar, int twoStar, int oneStar){
        HashMap<String,Double> starHashMap = new HashMap<>();
        double fiveStar_2 = fiveStar * 4;
        double fourStar_2 = fourStar * 4;
        double threeStar_2 = threeStar * 4;
        double twoStar_2 = twoStar * 4;
        double oneStar_2 = oneStar * 4;

        if(fiveStar!= 0) {
            for (int i = 0; i < fiveStar; i++) {
                fourStar_2 = fourStar_2 - 0.5;
                threeStar_2 = threeStar_2 - 2;
                twoStar_2 = twoStar_2 - 3;
                oneStar_2 = oneStar_2 - 4;
            }
        }
        if(fourStar!= 0){
            for(int i=0; i<fourStar; i++){
                fiveStar_2 = fiveStar_2-0.5;
                threeStar_2 = threeStar_2-0.5;
                twoStar_2 = twoStar_2-1.5;
                oneStar_2 = oneStar_2-3;
            }
        }
       if(threeStar!= 0){
            for(int i=0; i<threeStar; i++){
                fiveStar_2 = fiveStar_2-2.5;
                fourStar_2 = fourStar_2-0.5;
                twoStar_2 = twoStar_2-0.5;
                oneStar_2 = oneStar_2-2;
            }
        }
        if(twoStar!= 0){
            for(int i=0; i<twoStar; i++){
                fiveStar_2 = fiveStar_2-3;
                fourStar_2 = fourStar_2-1.5;
                threeStar_2 = threeStar_2-0.5;
                oneStar_2 = oneStar_2-1;
            }
        }

        if(oneStar!= 0) {
            for (int i = 0; i < oneStar; i++) {
                fiveStar_2 = fiveStar_2 - 4;
                fourStar_2 = fourStar_2 - 2.5;
                threeStar_2 = threeStar_2 - 1.5;
                twoStar_2 = twoStar_2 - 0.5;
            }
        }


        starHashMap.put("FIVE",fiveStar_2);
        starHashMap.put("FOUR",fourStar_2);
        starHashMap.put("THREE",threeStar_2);
        starHashMap.put("TWO",twoStar_2);
        starHashMap.put("ONE",oneStar_2);
        return starHashMap;
    }
*/
     //
    //INTERFACES
    //
    /**
     * An interface for manipulatiing menu
     * */
    public interface OnMenuItemClickListener {
        /**triggered when menu is clicked*/
        boolean onMenuItemClicked(MenuItem item);

    }

    public interface OnDocumentExistStatusCallback{

        void onExist();
        void onNotExist();
        void onFailed(@NonNull String errorMessage);

    }

    public interface ActionCallback{
            void onSuccess();
            void onFailed(String errorMessage);
    }
    interface OnCurrentUserProfileFetchListener{
            void onSuccess(CurrentUserProfileDataModel currentUserProfileDataModel);
            void onFailed(String errorMessage);
    }
//public static void setOnCurrentUserProfileFetchListener(OnCurrentUserProfileFetchListener onCurrentUserProfileFetchListener){
//    onCurrentUserProfileFetchListener = GlobalConfig.onCurrentUserProfileFetchListener ;
//}
//public static  OnCurrentUserProfileFetchListener getOnCurrentUserProfileFetchListener(){
//        return     GlobalConfig.onCurrentUserProfileFetchListener;
//}

}
