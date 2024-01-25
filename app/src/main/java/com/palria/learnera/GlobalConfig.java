package com.palria.learnera;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.palria.learnera.models.AnswerDataModel;
import com.palria.learnera.models.CurrentUserProfileDataModel;
import com.palria.learnera.models.PageDataModel;
import com.palria.learnera.models.PageDiscussionDataModel;
import com.palria.learnera.models.QuizDataModel;
import com.palria.learnera.models.WelcomeScreenItemModal;
import com.palria.learnera.widgets.BottomSheetFormBuilderWidget;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.io.ByteArrayOutputStream;
import java.lang.ref.PhantomReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.math.BigDecimal;
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
    /** paypal client id */
    public static final String paypalClientId = "AQ6WR0is4xW9bAnhYSoG52PkfKDUDD2VTZiAEbp3gSc4kZ01xFli50nq9_NXBT2FOvMsKJJvf3r631Da";
    public static final int PAYPAL_PAYMENT_REQUEST_CODE = 12451012;
    private static String CURRENT_USER_ID;
    private static String CURRENT_USER_TOKEN_ID = "EMPTY";
    static OnCurrentUserProfileFetchListener onCurrentUserProfileFetchListener;
    private static ArrayList<String> lastViewedAuthorsIdArray = new ArrayList<>();
    private static ArrayList<String> lastViewedLibraryIdArray = new ArrayList<>();
    private static ArrayList<String> lastViewedTutorialsIdArray = new ArrayList<>();
    private static ArrayList<String> BLOCKED_ITEM_LIST = new ArrayList<>();
    private static ArrayList<String> REPORTED_ITEM_LIST = new ArrayList<>();
    private static ArrayList<String> categoryList = new ArrayList<>();
    public static ArrayList<String> recentlyMarkedCompletedQuizList = new ArrayList<>();
    public static ArrayList<String> newlyJoinedQuizList = new ArrayList<>();
    public static ArrayList<String> recentlydeletedQuizList = new ArrayList<>();
    public static ArrayList<String> authorRecentlySavedQuizAnswerIdList = new ArrayList<>();
    public static HashMap<String,ArrayList<ArrayList<String>>> authorRecentlySavedAnswersListMap = new HashMap<>();
//    public static boolean isAnswerRecentlySaved = false;
    private static boolean isCurrentUserAccountVerified = false;
    private static boolean isCurrentUserAccountVerificationDeclined = false;
    private static boolean isAccountSubmittedForVerification = false;
    private static  boolean isNightMode = false;
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


    static final String TEST_NATIVE_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110";
    static final String MainActivity_NATIVE_AD_UNIT_ID = "ca-app-pub-3296946983872591/2627538156";
    static final String PAGE_NATIVE_AD_UNIT_ID = "ca-app-pub-3296946983872591/6970624761";
    static final String LIBRARY_NATIVE_AD_UNIT_ID = "ca-app-pub-3296946983872591/2852513836";
    static final String TUTORIAL_NATIVE_AD_UNIT_ID = "ca-app-pub-3296946983872591/9520249567";
    static final String FOLDER_NATIVE_AD_UNIT_ID = "ca-app-pub-3296946983872591/7242087507";
    static final String HOST_ACTIVITY_NATIVE_AD_UNIT_ID = "ca-app-pub-3296946983872591/8866477599";
    static final String STATS_ACTIVITY_NATIVE_AD_UNIT_ID = "ca-app-pub-3296946983872591/4404488400";


    static final String ViewerSingleMainUserSinglePostActivity_REWARDED_VIDEO_AD_UNIT_ID = "ca-app-pub-3296946983872591/3315322962";
    static final String ViewerSinglePageSinglePostActivity_REWARDED_VIDEO_AD_UNIT_ID = "ca-app-pub-3296946983872591/8102699492";

    public static final String TEXT_TYPE = "TEXT_TYPE";
    public static final String IMAGE_TYPE = "IMAGE_TYPE";
    public static final String TODO_TYPE = "TODO_TYPE";
    public static final String TABLE_TYPE = "TABLE_TYPE";


    public static final String PLATFORM_CONFIGURATION_FILE_KEY = "PLATFORM_CONFIGURATION_FILE";
    public static final String IS_NIGHT_MODE_KEY = "IS_NIGHT_MODE";
    public static final String SEARCH_KEYWORD_KEY = "SEARCH_KEYWORD";
    public static final String IS_FROM_SEARCH_CONTEXT_KEY = "IS_FROM_SEARCH_CONTEXT";




    public static final String IS_FIRST_OPEN_KEY = "IS_FIRST_OPEN";


    public static final String FRAGMENT_TYPE_KEY = "FRAGMENT_TYPE";
    public static final String USER_PROFILE_FRAGMENT_TYPE_KEY = "USER_PROFILE_FRAGMENT_TYPE";
    public static final String LIBRARY_FRAGMENT_TYPE_KEY = "LIBRARY_FRAGMENT_TYPE";
    public static final String AUTHORS_FRAGMENT_TYPE_KEY = "AUTHORS_FRAGMENT_TYPE";
    public static final String TUTORIAL_FRAGMENT_TYPE_KEY = "TUTORIAL_FRAGMENT_TYPE";
    public static final String CATEGORY_FRAGMENT_TYPE_KEY = "CATEGORY_FRAGMENT_TYPE";
    public static final String DISCUSSION_FRAGMENT_TYPE_KEY = "DISCUSSION_FRAGMENT_TYPE";
    public static final String QUIZ_FRAGMENT_TYPE_KEY = "QUIZ_FRAGMENT_TYPE";
    public static final String ANSWER_FRAGMENT_TYPE_KEY = "ANSWER_FRAGMENT_TYPE";

    public static final String OPEN_TYPE_ALL_QUESTIONS_KEY = "OPEN_TYPE_ALL_QUESTIONS";
    public static final String OPEN_TYPE_KEY = "OPEN_TYPE_KEY";

    public static final String IS_AUTHOR_OPEN_TYPE_KEY = "IS_AUTHOR_OPEN_TYPE";

    public static final String IS_FOR_PREVIEW_KEY = "IS_FOR_PREVIEW";
    public static final String IS_FROM_PAGE_CONTEXT_KEY = "IS_FROM_PAGE_CONTEXT";
    public static final String IS_VIEW_ALL_DISCUSSIONS_FOR_SINGLE_PAGE_KEY = "IS_VIEW_ALL_DISCUSSIONS_FOR_SINGLE_PAGE";
    public static final String IS_VIEW_SINGLE_DISCUSSION_REPLY_KEY = "IS_VIEW_SINGLE_DISCUSSION_REPLY";


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
    public static final String IS_USER_PROFILE_COMPLETED_KEY = "IS_USER_PROFILE_COMPLETED";
    public static final String USER_RESIDENTIAL_ADDRESS_KEY = "USER_RESIDENTIAL_ADDRESS";
    public static final String USER_AGE_KEY = "USER_AGE";
    public static final String USER_PERSONAL_WEBSITE_LINK_KEY = "USER_PERSONAL_WEBSITE_LINK";

    public static final String TIME_STAMP_KEY = "TIME_STAMP";
    public static final String TOTAL_USERS_FOLLOWING_KEY = "TOTAL_USERS_FOLLOWING";
    public static final String USERS_FOLLOWING_LIST_KEY = "USERS_FOLLOWING_LIST";
    public static final String TOTAL_FOLLOWERS_KEY = "TOTAL_FOLLOWERS";
    public static final String FOLLOWERS_LIST_KEY = "FOLLOWERS_LIST";

    public static final String IS_EDITION_KEY = "IS_EDITION";

    public static final String IS_ACCOUNT_VERIFIED_KEY = "IS_ACCOUNT_VERIFIED";
    public static final String DATE_VERIFIED_TIME_STAMP_KEY = "DATE_VERIFIED_TIME_STAMP";
    public static final String DATE_UNVERIFIED_TIME_STAMP_KEY = "DATE_UNVERIFIED_TIME_STAMP";
    public static final String IS_ACCOUNT_VERIFICATION_KEY = "IS_ACCOUNT_VERIFICATION";
    public static final String IS_ACCOUNT_VERIFICATION_DECLINED_KEY = "IS_ACCOUNT_VERIFICATION_DECLINED";
    public static final String DATE_DECLINED_TIME_STAMP_KEY = "DATE_DECLINED_TIME_STAMP";
    public static final String ACCOUNT_VERIFICATION_DECLINE_REASONS_LIST_KEY = "ACCOUNT_VERIFICATION_DECLINE_REASONS_LIST";
    public static final String IS_SUBMITTED_FOR_VERIFICATION_KEY = "IS_SUBMITTED_FOR_VERIFICATION";
    public static final String IS_ACCOUNT_VERIFICATION_SEEN_KEY = "IS_ACCOUNT_VERIFICATION_SEEN";
    public static final String IS_ACCOUNT_VERIFICATION_DECLINE_SEEN_KEY = "IS_ACCOUNT_VERIFICATION_DECLINE_SEEN";
    public static final String DATE_VERIFICATION_SUBMITTED_TIME_STAMP_KEY = "DATE_VERIFICATION_SUBMITTED_TIME_STAMP";

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
    public static final String USER_BIRTH_DATE_KEY = "USER_BIRTH_DATE";
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
    public static final String SINGLE_CATEGORY_KEY = "SINGLE_CATEGORY";

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
    public static final String TOTAL_NUMBER_OF_PAGE_VISITOR_KEY = "TOTAL_NUMBER_OF_PAGE_VISITOR";
    public static final String PAGE_NUMBER_KEY = "PAGE_NUMBER_KEY";
    public static final String TOTAL_NUMBER_OF_FOLDER_PAGE_VISITOR_KEY = "TOTAL_NUMBER_OF_FOLDER_PAGE_VISITOR";
    public static final String PAGE_CONTENT_KEY = "PAGE_CONTENT";
    public static final String PAGE_COVER_PHOTO_DOWNLOAD_URL_KEY = "PAGE_COVER_PHOTO_DOWNLOAD_URL";
    public static final String PAGE_MEDIA_URL_LIST_KEY = "PAGE_MEDIA_URL_LIST";
//    public static final String PAGE_NAME_KEY = "PAGE_NAME";
    public static final String DATE_TIME_STAMP_PAGE_CREATED_KEY = "DATE_TIME_STAMP_PAGE_CREATED";
    public static final String TOTAL_NUMBER_OF_PAGE_DATA_KEY = "TOTAL_NUMBER_OF_PAGE_DATA";
    public static final String IS_CREATE_NEW_PAGE_KEY = "IS_CREATE_NEW_PAGE";
    public static final String IS_PAGE_COVER_PHOTO_CHANGED_KEY = "IS_PAGE_COVER_PHOTO_CHANGED";
    public static final String IS_PUBLIC_KEY = "IS_PUBLIC";
    public static final String DATA_ARRAY_KEY = "DATA_ARRAY_";
    public static final String PAGE_TITLE_KEY = "PAGE_TITLE";
    public static final String ACTIVE_PAGE_MEDIA_URL_LIST_KEY = "ACTIVE_PAGE_MEDIA_URL_LIST";
    public static final String PAGE_DATE_CREATED_TIME_STAMP_KEY = "PAGE_DATE_CREATED_TIME_STAMP";
    public static final String PAGE_DATE_EDITED_TIME_STAMP_KEY = "PAGE_DATE_EDITED_TIME_STAMP";
    public static final String FOLDER_ID_KEY = "FOLDER_ID";
    public static final String IS_FOLDER_PAGE_KEY = "IS_FOLDER_PAGE";
    public static final String ALL_TUTORIAL_PAGES_KEY = "ALL_TUTORIAL_PAGES";
    public static final String ALL_FOLDER_PAGES_KEY = "ALL_FOLDER_PAGES";

    public static final String TUTORIAL_PAGE_ID_KEY = "TUTORIAL_PAGE_ID";
    public static final String FOLDER_PAGE_ID_KEY = "FOLDER_PAGE_ID";

    public static final String PAGE_ID_KEY = "PAGE_ID";
    public static final String LIKED_PAGES_LIST_KEY = "LIKED_PAGES_LIST";
    public static final String IS_TUTORIAL_PAGE_KEY = "IS_TUTORIAL_PAGE";
    public static final String IS_PAGINATION_KEY = "IS_PAGINATION";
    public static final String FOLDER_CREATED_DATE_TIME_STAMP_KEY = "FOLDER_CREATED_DATE_TIME_STAMP";
    public static final String TOTAL_NUMBER_OF_FOLDER_PAGES_KEY = "TOTAL_NUMBER_OF_FOLDER_PAGES";

    //TUTORIAL FIELD KEYS END



    public static final String ALL_CATEGORY_KEY = "ALL_CATEGORY";
    public static final String CATEGORY_LIST_KEY = "CATEGORY_LIST";
    public static final String CATEGORY_KEY = "CATEGORY";
    public static final String IS_CATEGORY_LIST_SAVED_KEY = "IS_CATEGORY_LIST_SAVED";


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

    public static final String ACTIVITY_LOG_USER_FOLLOW_USER_TYPE_KEY = "ACTIVITY_LOG_USER_FOLLOW_USER_TYPE";
    public static final String ACTIVITY_LOG_USER_UNFOLLOW_USER_TYPE_KEY = "ACTIVITY_LOG_USER_UNFOLLOW_USER_TYPE";


    public static final String ACTIVITY_LOG_USER_CREATE_NEW_FOLDER_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_CREATE_NEW_FOLDER_PAGE_TYPE";
    public static final String ACTIVITY_LOG_USER_BOOK_MARK_FOLDER_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_BOOK_MARK_FOLDER_PAGE_TYPE";
    public static final String ACTIVITY_LOG_USER_VISIT_FOLDER_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_VISIT_FOLDER_PAGE_TYPE";
    public static final String ACTIVITY_LOG_USER_EDIT_FOLDER_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_EDIT_FOLDER_PAGE_TYPE";
    public static final String ACTIVITY_LOG_USER_DELETE_FOLDER_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_DELETE_FOLDER_PAGE_TYPE";
    public static final String ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_FOLDER_PAGE_TYPE_KEY = "ACTIVITY_LOG_USER_REMOVE_BOOK_MARK_FOLDER_PAGE_TYPE";
//log keys ends

    public static final String DATE_REPORTED_TIME_STAMP_KEY = "DATE_REPORTED_TIME_STAMP";
    public static final String TOTAL_NUMBER_OF_USERS_REPORTED_KEY = "TOTAL_NUMBER_OF_USERS_REPORTED";
    public static final String TOTAL_NUMBER_OF_LIBRARY_REPORTED_KEY = "TOTAL_NUMBER_OF_LIBRARY_REPORTED";
    public static final String TOTAL_NUMBER_OF_TUTORIALS_REPORTED_KEY = "TOTAL_NUMBER_OF_TUTORIALS_REPORTED";
    public static final String TOTAL_NUMBER_OF_REPORTS_KEY = "TOTAL_NUMBER_OF_REPORTS";
    public static final String REPORTERS_KEY = "REPORTERS";
    public static final String REPORTER_USER_ID_KEY = "REPORTER_USER_ID";
    public static final String REPORTED_ITEMS_KEY = "REPORTED_ITEMS";

    public static final String DATE_BLOCKED_TIME_STAMP_KEY = "DATE_BLOCKED_TIME_STAMP";
    public static final String TOTAL_NUMBER_OF_USERS_BLOCKED_KEY = "TOTAL_NUMBER_OF_USERS_BLOCKED";
    public static final String TOTAL_NUMBER_OF_LIBRARY_BLOCKED_KEY = "TOTAL_NUMBER_OF_LIBRARY_BLOCKED";
    public static final String TOTAL_NUMBER_OF_TUTORIALS_BLOCKED_KEY = "TOTAL_NUMBER_OF_TUTORIALS_BLOCKED";
    public static final String TOTAL_NUMBER_OF_BLOCKS_KEY = "TOTAL_NUMBER_OF_BLOCKS";
    public static final String BLOCKERS_KEY = "BLOCKERS";
    public static final String BLOCKER_USER_ID_KEY = "BLOCKER_USER_ID";
    public static final String BLOCKED_ITEMS_KEY = "BLOCKED_ITEMS";

    public static final String ACTIVITY_LOG_USER_BLOCK_USER_TYPE_KEY = "ACTIVITY_LOG_USER_BLOCK_USER_TYPE";
    public static final String ACTIVITY_LOG_USER_BLOCK_LIBRARY_TYPE_KEY = "ACTIVITY_LOG_USER_BLOCK_LIBRARY_TYPE";
    public static final String ACTIVITY_LOG_USER_BLOCK_TUTORIAL_TYPE_KEY = "ACTIVITY_LOG_USER_BLOCK_TUTORIAL_TYPE";

    public static final String ACTIVITY_LOG_USER_UNBLOCK_USER_TYPE_KEY = "ACTIVITY_LOG_USER_UNBLOCK_USER_TYPE";
    public static final String ACTIVITY_LOG_USER_UNBLOCK_LIBRARY_TYPE_KEY = "ACTIVITY_LOG_USER_UNBLOCK_LIBRARY_TYPE";
    public static final String ACTIVITY_LOG_USER_UNBLOCK_TUTORIAL_TYPE_KEY = "ACTIVITY_LOG_USER_UNBLOCK_TUTORIAL_TYPE";

    public static final String ACTIVITY_LOG_USER_REPORT_USER_TYPE_KEY = "ACTIVITY_LOG_USER_REPORT_USER_TYPE";
    public static final String ACTIVITY_LOG_USER_REPORT_LIBRARY_TYPE_KEY = "ACTIVITY_LOG_USER_REPORT_LIBRARY_TYPE";
    public static final String ACTIVITY_LOG_USER_REPORT_TUTORIAL_TYPE_KEY = "ACTIVITY_LOG_USER_REPORT_TUTORIAL_TYPE";


    public static final String ACTIVITY_LOG_USER_UNREPORT_USER_TYPE_KEY = "ACTIVITY_LOG_USER_UNREPORT_USER_TYPE";
    public static final String ACTIVITY_LOG_USER_UNREPORT_LIBRARY_TYPE_KEY = "ACTIVITY_LOG_USER_UNREPORT_LIBRARY_TYPE";
    public static final String ACTIVITY_LOG_USER_UNREPORT_TUTORIAL_TYPE_KEY = "ACTIVITY_LOG_USER_UNREPORT_TUTORIAL_TYPE";

    public static final String _IS_PARTITION_ID_IS_FOR_IDENTIFYING_PARTITIONS_KEY = "_IS_PARTITION_ID_IS_FOR_IDENTIFYING_PARTITIONS_";

    public static final String PLATFORM_NOTIFICATIONS_KEY = "PLATFORM_NOTIFICATIONS";
    public static final String NOTIFICATION_ID_KEY = "NOTIFICATION_ID";
    public static final String DATE_SEEN_LAST_TIME_STAMP_KEY = "DATE_SEEN_LAST_TIME_STAMP";
    public static final String NOTIFICATION_VIEWERS_LIST_KEY = "NOTIFICATION_VIEWERS_LIST";
    public static final String DATE_NOTIFIED_TIME_STAMP_KEY = "DATE_NOTIFIED_TIME_STAMP";
//    public static final String NOTIFICATION_VIEWERS_LIST_KEY = "NOTIFICATION_VIEWERS_LIST";
//    public static final String DATE_NOTIFIED_TIME_STAMP_KEY = "DATE_NOTIFIED_TIME_STAMP";
    public static final String NOTIFICATION_MESSAGE_KEY = "NOTIFICATION_MESSAGE";
    public static final String NOTIFICATION_TITLE_KEY = "NOTIFICATION_TITLE";


    public static final String NOTIFICATION_TYPE_KEY = "NOTIFICATION_TYPE";
    public static final String NOTIFICATION_SENDER_ID_KEY = "NOTIFICATION_SENDER_ID";
    public static final String NOTIFICATION_MODEL_INFO_LIST_KEY = "NOTIFICATION_MODEL_INFO_LIST";
    public static final String PERSONALIZED_NOTIFICATIONS_KEY = "PERSONALIZED_NOTIFICATIONS";
    public static final String IS_SEEN_KEY = "IS_SEEN";
    public static final String NOTIFICATION_TYPE_QUIZ_KEY = "NOTIFICATION_TYPE_QUIZ";
    public static final String NOTIFICATION_TYPE_QUIZ_COMPLETED_KEY = "NOTIFICATION_TYPE_QUIZ_COMPLETED";


    public static final String DISCUSSION_ID_KEY = "DISCUSSION_ID";
    public static final String PARENT_DISCUSSION_ID_KEY = "PARENT_DISCUSSION_ID";
    public static final String LIKED_DISCUSSIONS_KEY = "LIKED_DISCUSSIONS";
    public static final String LIKED_PAGE_DISCUSSION_LIST_KEY = "LIKED_PAGE_DISCUSSION_LIST";
    public static final String MY_PAGES_DISCUSSION_KEY = "MY_PAGES_DISCUSSION";
    public static final String DISCUSSION_POSTER_ID_KEY = "DISCUSSION_POSTER_ID";
    public static final String TOTAL_DISCUSSIONS_KEY = "TOTAL_DISCUSSIONS";
    public static final String DISCUSSION_DESCRIPTION_KEY = "DISCUSSION_DESCRIPTION";
    public static final String DISCUSSION_COVER_PHOTO_DOWNLOAD_URL_KEY = "DISCUSSION_COVER_PHOTO_DOWNLOAD_URL";
    public static final String HAS_PARENT_DISCUSSION_KEY = "HAS_PARENT_DISCUSSION";
    public static final String HAS_REPLIES_KEY = "HAS_REPLIES";
    public static final String IS_HIDDEN_BY_AUTHOR_KEY = "IS_HIDDEN_BY_AUTHOR";
    public static final String IS_HIDDEN_BY_POSTER_KEY = "IS_HIDDEN_BY_POSTER";
    public static final String TOTAL_REPLIES_KEY = "TOTAL_REPLIES";
    public static final String TOTAL_LIKES_KEY = "TOTAL_LIKES";
//    public static final String TOTAL_DISLIKES_KEY = "TOTAL_DISLIKES";
    public static final String REPLIERS_ID_LIST_KEY = "REPLIERS_ID_LIST";
    public static final String LIKERS_ID_LIST_KEY = "LIKERS_ID_LIST";
//    public static final String DISLIKERS_ID_LIST_KEY = "DISLIKERS_ID_LIST";
    public static final String AUTHOR_ID_LIST_KEY = "AUTHOR_ID_LIST";
    public static final String PAGE_ID_LIST_KEY = "PAGE_ID_LIST";
    public static final String PARENT_DISCUSSION_ID_LIST_KEY = "PARENT_DISCUSSION_ID_LIST";
    public static final String LIKED_PAGES_KEY = "LIKED_PAGES";
    public static final String OTHER_PAGES_DISCUSSION_KEY = "OTHER_PAGES_DISCUSSION";
    public static final String DISCUSSION_CONTRIBUTORS_ID_LIST_KEY = "DISCUSSION_CONTRIBUTORS_ID_LIST";
    public static final String DATE_CREATED_TIME_STAMP_KEY = "DATE_CREATED_TIME_STAMP";
    public static final String DATE_EDITED_TIME_STAMP_KEY = "DATE_EDITED_TIME_STAMP";

    public static final String IS_LOAD_FROM_ONLINE_KEY = "IS_LOAD_FROM_ONLINE";

    public static final String QUIZ_ID_KEY = "QUIZ_ID";
    public static final String QUIZ_DESCRIPTION_KEY = "QUIZ_DESCRIPTION";
    public static final String QUIZ_TITLE_KEY = "QUIZ_TITLE";
    public static final String TOTAL_QUESTIONS_KEY = "TOTAL_QUESTIONS";
    public static final String TOTAL_QUIZ_SCORE_KEY = "TOTAL_QUIZ_SCORE";
    public static final String TOTAL_THEORY_QUESTIONS_KEY = "TOTAL_THEORY_QUESTIONS";
    public static final String TOTAL_OBJECTIVE_QUESTIONS_KEY = "TOTAL_OBJECTIVE_QUESTIONS";
    public static final String TOTAL_TIME_LIMIT_KEY = "TOTAL_TIME_LIMIT";
    public static final String SINGLE_QUESTION_TIME_LIMIT_KEY = "SINGLE_QUESTION_TIME_LIMIT";
    public static final String TOTAL_QUIZ_KEY = "TOTAL_QUIZ";
    public static final String QUESTION_LIST_KEY = "QUESTION_LIST";
    public static final String ALL_QUIZ_KEY = "ALL_QUIZ";
    public static final String QUIZ_DATE_LIST_KEY = "QUIZ_DATE_LIST";
//    public static final String QUIZ_FEE_DESCRIPTION_KEY = "QUIZ_FEE_DESCRIPTION";
//    public static final String QUIZ_REWARD_DESCRIPTION_KEY = "QUIZ_REWARD_DESCRIPTION";

    public static final String TOTAL_QUIZ_FEE_COINS_KEY = "TOTAL_QUIZ_FEE_COINS";
    public static final String TOTAL_QUIZ_REWARD_COINS_KEY = "TOTAL_QUIZ_REWARD_COINS";

    public static final String QUIZ_DATA_MODEL_KEY = "QUIZ_DATA_MODEL";
    public static final String SUBMITTED_QUIZ_LIST_KEY = "SUBMITTED_QUIZ_LIST";
    public static final String VIEWED_QUIZ_LIST_KEY = "VIEWED_QUIZ_LIST";
    public static final String QUIZ_VIEWERS_LIST_KEY = "QUIZ_VIEWERS_LIST";
    public static final String TOTAL_NUMBER_OF_TIMES_QUIZ_VIEWED_KEY = "TOTAL_NUMBER_OF_TIMES_QUIZ_VIEWED";
    public static final String TOTAL_SCORE_KEY = "TOTAL_SCORE";
    public static final String DATE_CLOSED_TIME_STAMP_KEY = "DATE_CLOSED_TIME_STAMP";
    public static final String IS_QUIZ_MARKED_COMPLETED_KEY = "IS_QUIZ_MARKED_COMPLETED";
    public static final String DATE_QUIZ_MARKED_COMPLETED_TIME_STAMP_KEY = "DATE_QUIZ_MARKED_COMPLETED_TIME_STAMP";
    public static final String PARTICIPANT_SCORES_LIST_KEY = "PARTICIPANT_SCORES_LIST";

    public static final String QUIZ_START_DATE_LIST_KEY = "QUIZ_START_DATE_LIST";
    public static final String QUIZ_END_DATE_LIST_KEY = "QUIZ_END_DATE_LIST";

    public static final String QUIZ_SEARCH_VERBATIM_KEYWORD_KEY = "QUIZ_SEARCH_VERBATIM_KEYWORD";
    public static final String QUIZ_SEARCH_ANY_MATCH_KEYWORD_KEY = "QUIZ_SEARCH_ANY_MATCH_KEYWORD";

    public static final String IS_CLOSED_KEY = "IS_CLOSED";
    public static final String TOTAL_PARTICIPANTS_KEY = "TOTAL_PARTICIPANTS";
    public static final String PARTICIPANTS_LIST_KEY = "PARTICIPANTS_LIST";
    public static final String TOTAL_QUIZ_JOINED_KEY = "TOTAL_QUIZ_JOINED";
    public static final String QUIZ_JOINED_LIST_KEY = "QUIZ_JOINED_LIST";
    public static final String ALL_PARTICIPANTS_KEY = "ALL_PARTICIPANTS";

    public static final String TOTAL_ANSWERS_SUBMITTED_KEY = "TOTAL_ANSWERS_SUBMITTED";
    public static final String ANSWER_SUBMITTED_POSITIONS_LIST_KEY = "ANSWER_SUBMITTED_POSITIONS_LIST";
    public static final String ANSWER_SUBMITTED_TIME_STAMP_KEY = "ANSWER_SUBMITTED_TIME_STAMP";
    public static final String ANSWER_LIST_KEY = "ANSWER_LIST";
    public static final String IS_ANSWER_SUBMITTED_KEY = "IS_ANSWER_SUBMITTED";
    public static final String IS_ANSWER_MARKED_BY_AUTHOR_KEY = "IS_ANSWER_MARKED_BY_AUTHOR";
    public static final String DATE_MARKED_BY_AUTHOR_TIME_STAMP_KEY = "DATE_MARKED_BY_AUTHOR_TIME_STAMP";
    public static final String LAST_SUBMITTED_TIME_STAMP_KEY = "LAST_SUBMITTED_TIME_STAMP";
    public static final String PARTICIPANT_ID_KEY = "PARTICIPANT_ID";

    public static final String SUBMITTED_QUESTION_LIST_KEY = "SUBMITTED_QUESTION_LIST";
    public static final String VIEWED_QUESTION_LIST_KEY = "VIEWED_QUESTION_LIST";
    public static final String LAST_SUBMITTED_QUESTION_POSITION_KEY = "LAST_SUBMITTED_QUESTION_POSITION";

    public static final String QUESTION_ID_KEY = "QUESTION_ID";
    public static final String QUESTION_DATA_MODEL_KEY = "QUESTION_DATA_MODEL";
    public static final String QUESTION_BODY_KEY = "QUESTION_BODY";
    public static final String QUESTION_PHOTO_DOWNLOAD_URL_KEY = "QUESTION_PHOTO_DOWNLOAD_URL";
    public static final String ALL_QUESTIONS_KEY = "ALL_QUESTIONS";
    public static final String IS_PHOTO_INCLUDED_KEY = "IS_PHOTO_INCLUDED";
    public static final String PHOTOS_KEY = "PHOTOS";
    public static final String QUESTION_SEARCH_ANY_MATCH_KEYWORD_KEY = "QUESTION_SEARCH_ANY_MATCH_KEYWORD";
    public static final String TOTAL_NUMBER_OF_ANSWER_KEY = "TOTAL_NUMBER_OF_ANSWER";
    public static final String TOTAL_NUMBER_OF_VIEWS_KEY = "TOTAL_NUMBER_OF_VIEWS";

    public static final String ALL_ANSWERS_KEY = "ALL_ANSWERS";
    public static final String ANSWER_ID_KEY = "ANSWER_ID";
    public static final String ANSWER_BODY_KEY = "ANSWER_BODY";
    public static final String ANSWER_PHOTO_DOWNLOAD_URL_KEY = "ANSWER_PHOTO_DOWNLOAD_URL";
    public static final String CONTRIBUTOR_ID_KEY = "CONTRIBUTOR_ID";
    public static final String ANSWER_CONTRIBUTORS_LIST_KEY = "ANSWER_CONTRIBUTORS_LIST";
    public static final String TOTAL_NUMBER_OF_ANSWER_CONTRIBUTED_KEY = "TOTAL_NUMBER_OF_ANSWER_CONTRIBUTED";
    public static final String QUESTIONS_ANSWERED_LIST_KEY = "QUESTIONS_ANSWERED_LIST";
    public static final String IS_FROM_QUESTION_CONTEXT_KEY = "IS_FROM_QUESTION_CONTEXT";
    public static final String IS_VIEW_ALL_ANSWERS_FOR_SINGLE_QUESTION_KEY = "IS_VIEW_ALL_ANSWERS_FOR_SINGLE_QUESTION";
    public static final String IS_VIEW_SINGLE_ANSWER_REPLY_KEY = "IS_VIEW_SINGLE_ANSWER_REPLY";
    public static final String PARENT_ID_KEY = "PARENT_ID";
    public static final String HAS_PARENT_KEY = "HAS_PARENT";
    public static final String IS_ANSWER_KEY = "IS_ANSWER";
    public static final String IS_HIDDEN_BY_CONTRIBUTOR_KEY = "IS_HIDDEN_BY_CONTRIBUTOR";
    public static final String TOTAL_UP_VOTES_KEY = "TOTAL_UP_VOTES";
    public static final String TOTAL_DOWN_VOTES_KEY = "TOTAL_DOWN_VOTES";
    public static final String UP_VOTERS_ID_LIST_KEY = "UP_VOTERS_ID_LIST";
    public static final String DOWN_VOTERS_ID_LIST_KEY = "DOWN_VOTERS_ID_LIST";
    public static final String REPLY_CONTRIBUTORS_LIST_KEY = "REPLY_CONTRIBUTORS_LIST";


    public static final String WALLET_CREATED_TIME_STAMP_KEY = "WALLET_CREATED_TIME_STAMP";
    public static final String IS_WALLET_CREATED_KEY = "IS_WALLET_CREATED";




    public static final String IS_THEORY_QUESTION_KEY = "IS_THEORY_QUESTION";
    public static final String IS_OBJECTIVE_QUESTION_KEY = "IS_OBJECTIVE_QUESTION";

    public static final String USER_WALLET_KEY = "USER_WALLET";
    public static final String WITHDRAWABLE_COIN_BALANCE_KEY = "WITHDRAWABLE_COIN_BALANCE";
    public static final String TOTAL_COINS_EARNED_KEY = "TOTAL_COINS_EARNED";
    public static final String TOTAL_QUIZ_REWARD_COINS_EARNED_KEY = "TOTAL_QUIZ_REWARD_COINS_EARNED";
    public static final String QUIZ_EARNINGS_HISTORY_LIST_KEY = "QUIZ_EARNINGS_HISTORY_LIST";
    public static final String TOTAL_COIN_EQUITY_KEY = "TOTAL_COIN_EQUITY";
    public static final String COIN_WITHDRAWAL_HISTORY_LIST_KEY = "COIN_WITHDRAWAL_HISTORY_LIST";
    public static final String REFERAL_REWARD_HISTORY_LIST_KEY = "REFERAL_REWARD_HISTORY_LIST";
    public static final String IS_REWARD_CLAIMED_KEY = "IS_REWARD_CLAIMED";

    public static final String IS_LOAD_IMMEDIATELY_KEY = "IS_LOAD_IMMEDIATELY";

    private static FirebaseFirestore firebaseFirestoreInstance;
    private static FirebaseStorage firebaseStorageInstance;

/**
 * <p>This method performs a check to see whether a user is logged in or not</p>
 * @return {@link boolean} denoting if a user is logged in or not
 * */
    static boolean isUserLoggedIn(){

        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    /**This checks if the user is Learn Era Platform or not*/
    public static boolean isLearnEraAccount(){
if(getCurrentUserId().equals("vnC7yVCJw1X6rp7bik7BSJHk6xC3")) {
    return true;
}
        return false;
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
       if(GlobalConfig.CURRENT_USER_ID == null && FirebaseAuth.getInstance().getCurrentUser() != null){
           return FirebaseAuth.getInstance().getCurrentUser().getUid();
       }
       return GlobalConfig.CURRENT_USER_ID+"";
    }

/**
 * sets the app to night mode */
   static void setIsNightMode(Context context, boolean isNightMode){
       SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),MODE_PRIVATE);
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putBoolean(GlobalConfig.IS_NIGHT_MODE_KEY,isNightMode);
       editor.apply();
         GlobalConfig.isNightMode = isNightMode;
    }
      /**
       *checks if app is in night mode*/
   public static boolean isNightMode(Context context){
       SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),MODE_PRIVATE);

       return sharedPreferences.getBoolean(GlobalConfig.IS_NIGHT_MODE_KEY,false);
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
       if(GlobalConfig.firebaseFirestoreInstance == null){
           return FirebaseFirestore.getInstance();
       }
       return GlobalConfig.firebaseFirestoreInstance;
    }
    /**
     * Sets the list of the platform's category
     * */
   static void setCategoryList(ArrayList<String> categoryList,Context context){
        GlobalConfig.categoryList = categoryList;
        StringBuilder categoryConcattedString = new StringBuilder("");
       for(int i=0; i<categoryList.size();i++){
           if(i<(categoryList.size()-1)) {
               categoryConcattedString.append(categoryList.get(i) + ",");
           } else{
                   categoryConcattedString.append(categoryList.get(i));
               }
       }
       SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putString(GlobalConfig.CATEGORY_LIST_KEY,categoryConcattedString+"");
       editor.putBoolean(GlobalConfig.IS_CATEGORY_LIST_SAVED_KEY,true);
       editor.apply();
    }
    /**
 * Returns the list of the platform's category
 * */
   public static ArrayList<String> getCategoryList(Context context){

       SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
       String categorySavedList = sharedPreferences.getString(GlobalConfig.CATEGORY_LIST_KEY,"ALL,Basic Education,How To,Technology,Business,Skills");
       boolean isSaved = sharedPreferences.getBoolean(GlobalConfig.IS_CATEGORY_LIST_SAVED_KEY,false);

//       if(GlobalConfig.categoryList == null){
//           ArrayList<String> categoryList = new ArrayList<>();
//           categoryList.add("ALL");
//           categoryList.add("Basic Education");
//           categoryList.add("How To");
//           categoryList.add("Technology");
//           categoryList.add("Business");
//           categoryList.add("Skills");
//           return categoryList;
//       }
       if(isSaved){
//           categoryList.add(categorySavedList.split(",")[0]);
           categoryList = new ArrayList<>();

           for(String category : categorySavedList.split(",")){
               if (!categoryList.contains(category)) {
                   categoryList.add(category);
//                   Toast.makeText(context, ""+category, Toast.LENGTH_SHORT).show();
               }
           }

       }
       return GlobalConfig.categoryList;
    }


    public static boolean isCategorySaved(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
        boolean isSaved = sharedPreferences.getBoolean(GlobalConfig.IS_CATEGORY_LIST_SAVED_KEY,false);

        return isSaved;
    }
    public static boolean isCurrentUserAccountVerified(){
        return isCurrentUserAccountVerified;
    }
    public static void setIsCurrentUserAccountVerified(boolean isCurrrentUserAccountVerified){
         GlobalConfig.isCurrentUserAccountVerified = isCurrrentUserAccountVerified;
    }
    public static boolean isCurrentUserAccountVerificationDeclined(){
        return isCurrentUserAccountVerificationDeclined;
    }
    public static void setIsCurrentUserAccountVerificationDeclined(boolean isCurrrentUserAccountVerificationDeclined){
         GlobalConfig.isCurrentUserAccountVerificationDeclined = isCurrrentUserAccountVerificationDeclined;
    }
    public static boolean isAccountSubmittedForVerification(){
        return isAccountSubmittedForVerification;
    }
    public static void setIsAccountSubmittedForVerification(boolean isAccountSubmittedForVerification){
         GlobalConfig.isAccountSubmittedForVerification = isAccountSubmittedForVerification;
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
    if(GlobalConfig.firebaseStorageInstance == null){
        return FirebaseStorage.getInstance();
    }
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
   public static boolean isConnectedOnline(Context context) {



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

    public static Intent getPaypalIntent(Context context, String amount){
        PayPalConfiguration payPalConfiguration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(GlobalConfig.paypalClientId);

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD", "Learn Era",PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(context, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);

        return intent;

    }

   public static  Snackbar createSnackBar(Context context , View view,String text,int lengthPeriod){
        Snackbar snackBar = Snackbar.make(view,text,lengthPeriod);
        snackBar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackBar.dismiss();
            }
        });
        snackBar.show();

        return snackBar;
    }
   public static  Snackbar createSnackBar2(Context context , View view,String text,String buttonLabel,int lengthPeriod,View.OnClickListener onClickListener){
        Snackbar snackBar = Snackbar.make(view,text,lengthPeriod);
        snackBar.setAction(buttonLabel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(view);
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
                        onDocumentExistStatusCallback.onExist(documentSnapshot);
                        /**
                         * new RatingDataModel(
                         *                                 documentSnapshot.getString("userId"),
                         *                                 documentSnapshot.getId(),
                         *                                 documentSnapshot.getId(),
                         *                                 "Library",
                         *                                 "",
                         *                                 "Hlo dami",
                         *                                 4,
                         *                                 "DownloadURl"
                         *                         )
                         */
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


    public static void deletePage(String libraryId,String tutorialId,String folderId,String pageId,boolean isTutorialPage, ActionCallback actionCallback){
        WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();

        if(isTutorialPage){

            GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(ALL_TUTORIAL_PAGES_KEY).document(pageId).get().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    ArrayList<String> urlList  = documentSnapshot.get(GlobalConfig.ACTIVE_PAGE_MEDIA_URL_LIST_KEY)!=null? (ArrayList<String>) documentSnapshot.get(GlobalConfig.ACTIVE_PAGE_MEDIA_URL_LIST_KEY) :new ArrayList<>();
                    String coverPhotoUrl =""+ documentSnapshot.get(GlobalConfig.PAGE_COVER_PHOTO_DOWNLOAD_URL_KEY);

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
//                            actionCallback.onSuccess();
                            try {
                                getFirebaseStorageInstance().getReferenceFromUrl(coverPhotoUrl).delete();
                            }catch(Exception e){}

                                for (int i = 0; i < urlList.size(); i++) {
                                   try{
                                    getFirebaseStorageInstance().getReferenceFromUrl(urlList.get(i)).delete();
                                }catch(Exception e){}
                                }
                            actionCallback.onSuccess();
                        }
                    });

                }
            });

        }else{

            GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(ALL_FOLDERS_KEY).document(folderId).collection(ALL_FOLDER_PAGES_KEY).document(pageId).get().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    actionCallback.onFailed(e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    ArrayList<String> urlList  = documentSnapshot.get(GlobalConfig.ACTIVE_PAGE_MEDIA_URL_LIST_KEY)!=null? (ArrayList<String>) documentSnapshot.get(GlobalConfig.ACTIVE_PAGE_MEDIA_URL_LIST_KEY) :new ArrayList<>();
                    String coverPhotoUrl =""+ documentSnapshot.get(GlobalConfig.PAGE_COVER_PHOTO_DOWNLOAD_URL_KEY);

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
//                            actionCallback.onSuccess();
                            try {
                                getFirebaseStorageInstance().getReferenceFromUrl(coverPhotoUrl).delete();
                            }catch(Exception e){}
                            for(int i=0; i<urlList.size(); i++){
                                try {

                                    getFirebaseStorageInstance().getReferenceFromUrl(urlList.get(i)).delete();
                                }catch(Exception e){}
                            }

                            GlobalConfig.updateActivityLog(ACTIVITY_LOG_USER_DELETE_FOLDER_PAGE_TYPE_KEY, getCurrentUserId(), libraryId, tutorialId, folderId, pageId, null,  new GlobalConfig.ActionCallback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onFailed(String errorMessage) {

                                }
                            });

                        }
                    });
                }
            });

        }
    }

    public static void deleteFolder(String libraryId,String tutorialId,String folderId, ActionCallback actionCallback){
          WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
          GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(ALL_FOLDERS_KEY).document(folderId).get().addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {

              }
          }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                  long numberOfPages = documentSnapshot.get(TOTAL_NUMBER_OF_PAGES_CREATED_KEY) != null ? documentSnapshot.getLong(TOTAL_NUMBER_OF_PAGES_CREATED_KEY) : 0L;
                  if (numberOfPages != 0) {

                      DocumentReference tutorialDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId);
                      HashMap<String, Object> details = new HashMap<>();
                      details.put(TOTAL_NUMBER_OF_FOLDERS_CREATED_KEY, FieldValue.increment(-1L));
                      writeBatch.update(tutorialDocumentReference, details);

                      DocumentReference pageDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(ALL_FOLDERS_KEY).document(folderId);
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
                              GlobalConfig.updateActivityLog(ACTIVITY_LOG_USER_DELETE_TUTORIAL_FOLDER_TYPE_KEY, getCurrentUserId(), libraryId, tutorialId, folderId, null, null,  new GlobalConfig.ActionCallback() {
                                  @Override
                                  public void onSuccess() {
                                  }

                                  @Override
                                  public void onFailed(String errorMessage) {

                                  }
                              });

                          }
                      });

                  } else {
                      actionCallback.onFailed("OOPS, Action Denied! Folders cannot be deleted if there are pages therein, Please delete all pages to enable folder deletion");
                  }
              }
          });
      }

    public static void deleteTutorial(String libraryId,String tutorialId, ActionCallback actionCallback){
          WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
          GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).get().addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {

              }
          }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                  long numberOfPages = documentSnapshot.get(TOTAL_NUMBER_OF_PAGES_CREATED_KEY) != null ? documentSnapshot.getLong(TOTAL_NUMBER_OF_PAGES_CREATED_KEY) : 0L;
                  long numberOfFolders = documentSnapshot.get(TOTAL_NUMBER_OF_FOLDERS_CREATED_KEY) != null ? documentSnapshot.getLong(TOTAL_NUMBER_OF_FOLDERS_CREATED_KEY) : 0L;
                  if (numberOfPages != 0 && numberOfFolders!=0) {

                      DocumentReference tutorialDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId);
                      HashMap<String, Object> details = new HashMap<>();
                      details.put(TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY, FieldValue.increment(-1L));
                      writeBatch.set(tutorialDocumentReference, details,SetOptions.merge());

                      DocumentReference tutorialDocumentReference2 = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(getCurrentUserId());
                      HashMap<String, Object> details2 = new HashMap<>();
                      details.put(TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY, FieldValue.increment(-1L));
                      writeBatch.set(tutorialDocumentReference2, details2,SetOptions.merge());

                      DocumentReference pageDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId);
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
                              GlobalConfig.updateActivityLog(ACTIVITY_LOG_USER_DELETE_TUTORIAL_TYPE_KEY, getCurrentUserId(), libraryId, tutorialId, null, null, null,  new GlobalConfig.ActionCallback() {
                                  @Override
                                  public void onSuccess() {
                                  }

                                  @Override
                                  public void onFailed(String errorMessage) {

                                  }
                              });

                          }
                      });

                  } else {
                      actionCallback.onFailed("OOPS, Action Denied! Tutorials cannot be deleted if there are pages or folders therein, Please delete all pages and folders to enable tutorial deletion");
                  }
              }
          });
      }

    public static void deleteLibrary(String libraryId, ActionCallback actionCallback){
          WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
          GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId).get().addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {

              }
          }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                  long numberOfTutorials = documentSnapshot.get(TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY) != null ? documentSnapshot.getLong(TOTAL_NUMBER_OF_TUTORIAL_CREATED_KEY) : 0L;
                  if (numberOfTutorials<=0) {

                      DocumentReference tutorialDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(getCurrentUserId());
                      HashMap<String, Object> details = new HashMap<>();
                      details.put(TOTAL_NUMBER_OF_LIBRARY_CREATED_KEY, FieldValue.increment(-1L));
                      writeBatch.update(tutorialDocumentReference, details);

                      DocumentReference pageDocumentReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_LIBRARY_KEY).document(libraryId);
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
                              GlobalConfig.updateActivityLog(ACTIVITY_LOG_USER_DELETE_LIBRARY_TYPE_KEY, getCurrentUserId(), libraryId, null, null, null, null,  new GlobalConfig.ActionCallback() {
                                  @Override
                                  public void onSuccess() {
                                  }

                                  @Override
                                  public void onFailed(String errorMessage) {

                                  }
                              });

                          }
                      });

                  } else {
                      actionCallback.onFailed("OOPS, Action Denied! Library cannot be deleted if there are tutorials therein, Please delete all tutorials to enable library deletion");
                  }
              }
          });



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

    public static boolean isQuizExpired(long endYear, long endMonth,long endDay, long endHour,long endMinute){


            if(getEventYear()>=endYear &&getEventMonth()>=endMonth &&getEventDay()>=endDay &&getEventHour(true)>=endHour &&getEventMinute()>endMinute){
                return true;
            }


            return false;
    }

    public static boolean isQuizStarted(long startYear, long startMonth,long startDay, long startHour,long startMinute){


            if(getEventYear()>=startYear &&getEventMonth()>=startMonth &&getEventDay()>=startDay &&getEventHour(true)>=startHour &&getEventMinute()>=startMinute){
                return true;
            }


            return false;
    }

      static  public int getEventSeconds(){
        return  Integer.parseInt(new SimpleDateFormat("ss", Locale.US).format(new Date()));
        }
      static  public int getEventMinute(){
            return  Integer.parseInt(new SimpleDateFormat("mm", Locale.US).format(new Date()));
        }
      static  public int getEventHour(boolean is24Hour){
            if(is24Hour) {
                return Integer.parseInt(new SimpleDateFormat("HH", Locale.US).format(new Date()));
            }else{
                return Integer.parseInt(new SimpleDateFormat("hh", Locale.US).format(new Date()));
            }

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
        list.add(new WelcomeScreenItemModal(R.drawable.undraw_save_to_bookmarks, "Unlimited Personal Library","You can create infinite numbers of libraries for your students or readers"));
        list.add(new WelcomeScreenItemModal(R.drawable.undraw_personal_notebook,"Bookmark your interests","You can easily customize your favourite libraries, tutorials,authors ,folders and pages."));
        list.add(new WelcomeScreenItemModal(R.drawable.undraw_online_reading_np7n,"Experience of High level reading","Experience reading high level of tutorials from authors worldwide for free"));

        return list;
    }

    public static boolean isFirstOpen(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName()+GlobalConfig.getCurrentUserId(),MODE_PRIVATE);
        return sharedPreferences.getBoolean(GlobalConfig.IS_FIRST_OPEN_KEY,true);

    }
    public static void setIsFirstOpen(Context context,boolean isFirstOpen){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName()+GlobalConfig.getCurrentUserId(),MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(GlobalConfig.IS_FIRST_OPEN_KEY,isFirstOpen);
        editor.apply();

    }
    public static void updateActivityLog(String activityLogType, String authorId, String libraryId, String tutorialId,String tutorialFolderId ,String pageId ,String reviewId , ActionCallback actionCallback){

            //We disable logging for now till further notice. So this if statement just returns and performs no action and it's always true .
        if(true){
            actionCallback.onSuccess();
            return;
        }

            String activityLogId = getRandomString(10);
            HashMap<String,Object> activityLogDetails = new HashMap<>();
//            activityLogDetails.put(LOG_NOTE_KEY,);
            activityLogDetails.put(ACTION_DOER_ID_KEY,getCurrentUserId());
            activityLogDetails.put(EVENT_SECONDS_KEY,(long)getEventSeconds());
            activityLogDetails.put(EVENT_MINUTE_KEY,(long)getEventMinute());
            activityLogDetails.put(EVENT_HOUR_KEY,(long)getEventHour(true));
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

if(isUserLoggedIn()) {
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
}else{
    actionCallback.onSuccess();

}
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
        intent.setClass(context,HostActivity.class);
        intent.putExtra(GlobalConfig.FRAGMENT_TYPE_KEY,fragmentOpenType);
        intent.putExtra(GlobalConfig.USER_ID_KEY,userId);

        return  intent;
    }

    public static ShimmerFrameLayout showShimmerLayout(Context context, ViewGroup viewGroup ){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout) layoutInflater.inflate(R.layout.progress_indicator_shimmer_layout,viewGroup,false);
//        ShimmerFrameLayout shimmerFrameLayout  = view.findViewById(R.id.progressIndicatorShimmerId);
        shimmerFrameLayout.startShimmer();
        viewGroup.addView(shimmerFrameLayout);
        return shimmerFrameLayout;
    }
    public static void removeShimmerLayout(ViewGroup viewGroup, ShimmerFrameLayout shimmerFrameLayout){
        if(shimmerFrameLayout !=null){
            shimmerFrameLayout.stopShimmer();
            viewGroup.removeView(shimmerFrameLayout);
        }
        shimmerFrameLayout = null;
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
        details.put(TOTAL_NUMBER_OF_PAGE_VISITOR_KEY,FieldValue.increment(1L));
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
        details.put(TOTAL_NUMBER_OF_PAGE_VISITOR_KEY,FieldValue.increment(1L));
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
            index = new Random().nextInt(getLastViewedTutorialsArray().size()==0?1:getLastViewedTutorialsArray().size());
        }
        ArrayList<String> lastViewedTutorialsIdList = getLastViewedTutorialsArray();
        HashMap<String,Object> lastViewDetails = new HashMap<>();
        if(lastViewedTutorialsIdList.size()<10){
            index = lastViewedTutorialsIdList.size();
        }
        lastViewedTutorialsIdList.add(index,tutorialId);
        lastViewDetails.put(LAST_VIEWED_TUTORIALS_ARRAY_KEY,lastViewedTutorialsIdList);
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
            index = new Random().nextInt(getLastViewedLibraryArray().size()==0?1:getLastViewedLibraryArray().size());
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
            index = new Random().nextInt(getLastViewedAuthorsArray().size()==0?1:getLastViewedAuthorsArray().size());
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

    public static ArrayList<String> getBlockedItemsList(){
        if (!BLOCKED_ITEM_LIST.contains("BLOCKS")) {
            BLOCKED_ITEM_LIST.add("BLOCKS");
        }
     return BLOCKED_ITEM_LIST;
    }
    public static void setBlockedItemsList(){
        ArrayList<String> blockedList = new ArrayList<>();
        getFirebaseFirestoreInstance()
                .collection(ALL_USERS_KEY)
                .document(getCurrentUserId())
                .collection(BLOCKED_ITEMS_KEY)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null){
                            for (DocumentChange documentChange : value.getDocumentChanges()) {
                                DocumentSnapshot documentSnapshot = documentChange.getDocument();
                                String blockedItemId = documentSnapshot.getId();
                                if (!blockedList.contains(blockedItemId)) {
                                    blockedList.add(blockedItemId);
                                }
                            }
                    }
                        BLOCKED_ITEM_LIST = blockedList;

                    }
                });

    }
    public static ArrayList<String> getReportedItemsList(){
        if (!REPORTED_ITEM_LIST.contains("REPORTS")) {
            REPORTED_ITEM_LIST.add("REPORTS");
        }
     return REPORTED_ITEM_LIST;
    }
    public static void setReportedItemsList(){
        ArrayList<String> reportedList = new ArrayList<>();
        getFirebaseFirestoreInstance()
                .collection(ALL_USERS_KEY)
                .document(getCurrentUserId())
                .collection(REPORTED_ITEMS_KEY)
                .get()
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                setReportedItemsList();
            }
        })
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    String reportedItemId = documentSnapshot.getId();
                    reportedList.add(reportedItemId);
                }
                REPORTED_ITEM_LIST = reportedList;
            }
        });

    }
    
    public static void block(String type,String userId,String libraryId,String tutorialId,ActionCallback actionCallback){
        if(!getCurrentUserId().equals(userId)) {
            WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();
            DocumentReference blockedUserReference = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).collection(BLOCKED_ITEMS_KEY).document(userId);
            HashMap<String, Object> blockedDetails = new HashMap<>();
            blockedDetails.put(USER_ID_KEY, userId);
            blockedDetails.put(LIBRARY_ID_KEY, libraryId);
            blockedDetails.put(TUTORIAL_ID_KEY, tutorialId);
            blockedDetails.put(DATE_BLOCKED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
            blockedDetails.put(TYPE_KEY, type);


            DocumentReference blockerIncrementReference = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId).collection(BLOCKERS_KEY).document(getCurrentUserId());
            HashMap<String, Object> blockerIncrementDetails = new HashMap<>();
            blockerIncrementDetails.put(BLOCKER_USER_ID_KEY, getCurrentUserId());
            blockerIncrementDetails.put(DATE_BLOCKED_TIME_STAMP_KEY, FieldValue.serverTimestamp());


            DocumentReference reference1 = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId);
            HashMap<String, Object> details1 = new HashMap<>();
            details1.put(TOTAL_NUMBER_OF_BLOCKS_KEY, FieldValue.increment(1L));

            switch (type) {
                case AUTHOR_TYPE_KEY:
                    blockedUserReference = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).collection(BLOCKED_ITEMS_KEY).document(userId);
                    blockerIncrementReference = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId).collection(BLOCKERS_KEY).document(getCurrentUserId());
                    reference1 = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId);

                    DocumentReference blockedIncrementReference = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId());
                    HashMap<String, Object> blockedIncrementDetails = new HashMap<>();
                    blockedIncrementDetails.put(TOTAL_NUMBER_OF_USERS_BLOCKED_KEY, FieldValue.increment(1L));
                    writeBatch.set(blockedIncrementReference, blockedIncrementDetails, SetOptions.merge());

                    break;
                case LIBRARY_TYPE_KEY:

                    blockedUserReference = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).collection(BLOCKED_ITEMS_KEY).document(libraryId);
                    blockerIncrementReference = getFirebaseFirestoreInstance().collection(ALL_LIBRARY_KEY).document(libraryId).collection(BLOCKERS_KEY).document(getCurrentUserId());
                    reference1 = getFirebaseFirestoreInstance().collection(ALL_LIBRARY_KEY).document(libraryId);

                    DocumentReference blockedIncrementReference1 = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId());
                    HashMap<String, Object> blockedIncrementDetails1 = new HashMap<>();
                    blockedIncrementDetails1.put(TOTAL_NUMBER_OF_LIBRARY_BLOCKED_KEY, FieldValue.increment(1L));
                    writeBatch.set(blockedIncrementReference1, blockedIncrementDetails1, SetOptions.merge());

                    break;
                case TUTORIAL_TYPE_KEY:

                    blockedUserReference = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).collection(BLOCKED_ITEMS_KEY).document(tutorialId);
                    blockerIncrementReference = getFirebaseFirestoreInstance().collection(ALL_TUTORIAL_KEY).document(tutorialId).collection(BLOCKERS_KEY).document(getCurrentUserId());
                    reference1 = getFirebaseFirestoreInstance().collection(ALL_TUTORIAL_KEY).document(tutorialId);


                    DocumentReference blockedIncrementReference2 = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId());
                    HashMap<String, Object> blockedIncrementDetails2 = new HashMap<>();
                    blockedIncrementDetails2.put(TOTAL_NUMBER_OF_TUTORIALS_BLOCKED_KEY, FieldValue.increment(1L));
                    writeBatch.set(blockedIncrementReference2, blockedIncrementDetails2, SetOptions.merge());

                    break;
            }
            writeBatch.set(blockedUserReference, blockedDetails, SetOptions.merge());
            writeBatch.set(blockerIncrementReference, blockerIncrementDetails, SetOptions.merge());
            writeBatch.set(reference1, details1, SetOptions.merge());

            writeBatch.commit().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    String activityLogType = "NONE";
                    switch (type) {
                        case AUTHOR_TYPE_KEY:
                            activityLogType = ACTIVITY_LOG_USER_BLOCK_USER_TYPE_KEY;
                            break;

                        case LIBRARY_TYPE_KEY:
                            activityLogType = ACTIVITY_LOG_USER_BLOCK_LIBRARY_TYPE_KEY;
                            break;

                        case TUTORIAL_TYPE_KEY:
                            activityLogType = ACTIVITY_LOG_USER_BLOCK_TUTORIAL_TYPE_KEY;
                            break;
                    }
                    GlobalConfig.updateActivityLog(activityLogType, userId, libraryId, tutorialId, null, null, null, new GlobalConfig.ActionCallback() {
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
    }
    public static void unBlock(String type,String userId,String libraryId,String tutorialId,ActionCallback actionCallback){
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();
      DocumentReference blockedUserReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).collection(BLOCKED_ITEMS_KEY).document(userId);

      DocumentReference blockerIncrementReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId).collection(BLOCKERS_KEY).document(getCurrentUserId());



        DocumentReference reference1 =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId);
        HashMap<String,Object> details1 = new HashMap<>();
        details1.put(TOTAL_NUMBER_OF_BLOCKS_KEY,FieldValue.increment(-1L));

      switch(type){
          case AUTHOR_TYPE_KEY:
              blockedUserReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).collection(BLOCKED_ITEMS_KEY).document(userId);
              blockerIncrementReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId).collection(BLOCKERS_KEY).document(getCurrentUserId());
              reference1 =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId);

              DocumentReference blockedIncrementReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId());
              HashMap<String,Object> blockedIncrementDetails = new HashMap<>();
              blockedIncrementDetails.put(TOTAL_NUMBER_OF_USERS_BLOCKED_KEY,FieldValue.increment(-1L));
              writeBatch.set(blockedIncrementReference,blockedIncrementDetails,SetOptions.merge());

              break;
          case LIBRARY_TYPE_KEY:

              blockedUserReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).collection(BLOCKED_ITEMS_KEY).document(libraryId);
              blockerIncrementReference =  getFirebaseFirestoreInstance().collection(ALL_LIBRARY_KEY).document(libraryId).collection(BLOCKERS_KEY).document(getCurrentUserId());
              reference1 =  getFirebaseFirestoreInstance().collection(ALL_LIBRARY_KEY).document(libraryId);

              DocumentReference blockedIncrementReference1 =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId());
              HashMap<String,Object> blockedIncrementDetails1 = new HashMap<>();
              blockedIncrementDetails1.put(TOTAL_NUMBER_OF_LIBRARY_BLOCKED_KEY,FieldValue.increment(-1L));
              writeBatch.set(blockedIncrementReference1,blockedIncrementDetails1,SetOptions.merge());

              break;
          case TUTORIAL_TYPE_KEY:

              blockedUserReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).collection(BLOCKED_ITEMS_KEY).document(tutorialId);
              blockerIncrementReference =  getFirebaseFirestoreInstance().collection(ALL_TUTORIAL_KEY).document(tutorialId).collection(BLOCKERS_KEY).document(getCurrentUserId());
              reference1 =  getFirebaseFirestoreInstance().collection(ALL_TUTORIAL_KEY).document(tutorialId);


              DocumentReference blockedIncrementReference2 =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId());
              HashMap<String,Object> blockedIncrementDetails2 = new HashMap<>();
              blockedIncrementDetails2.put(TOTAL_NUMBER_OF_TUTORIALS_BLOCKED_KEY,FieldValue.increment(-1L));
              writeBatch.set(blockedIncrementReference2,blockedIncrementDetails2,SetOptions.merge());

              break;
      }
        writeBatch.delete(blockedUserReference);
        writeBatch.delete(blockerIncrementReference);
        writeBatch.set(reference1,details1,SetOptions.merge());

      writeBatch.commit().addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {

          }
      }).addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void unused) {
            String activityLogType = "NONE";
              switch(type){
                  case AUTHOR_TYPE_KEY:
                      activityLogType = ACTIVITY_LOG_USER_UNBLOCK_USER_TYPE_KEY;
                      break;

                  case LIBRARY_TYPE_KEY:
                      activityLogType = ACTIVITY_LOG_USER_UNBLOCK_LIBRARY_TYPE_KEY;
                      break;

                  case TUTORIAL_TYPE_KEY:
                      activityLogType = ACTIVITY_LOG_USER_UNBLOCK_TUTORIAL_TYPE_KEY;
                      break;
              }
              GlobalConfig.updateActivityLog(activityLogType, userId, libraryId, tutorialId, null, null, null,  new GlobalConfig.ActionCallback() {
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

    public static void report(String type,String userId,String libraryId,String tutorialId,ActionCallback actionCallback){
        if(!getCurrentUserId().equals(userId)) {
            WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();
            DocumentReference reportedUserReference = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).collection(REPORTED_ITEMS_KEY).document(userId);
            HashMap<String, Object> reportedDetails = new HashMap<>();
            reportedDetails.put(USER_ID_KEY, userId);
            reportedDetails.put(LIBRARY_ID_KEY, libraryId);
            reportedDetails.put(TUTORIAL_ID_KEY, tutorialId);
            reportedDetails.put(DATE_BLOCKED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
            reportedDetails.put(TYPE_KEY, type);


            DocumentReference reporterIncrementReference = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId).collection(REPORTERS_KEY).document(getCurrentUserId());
            HashMap<String, Object> reporterIncrementDetails = new HashMap<>();
            reporterIncrementDetails.put(REPORTER_USER_ID_KEY, getCurrentUserId());
            reporterIncrementDetails.put(DATE_REPORTED_TIME_STAMP_KEY, FieldValue.serverTimestamp());


            DocumentReference reference1 = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId);
            HashMap<String, Object> details1 = new HashMap<>();
            details1.put(TOTAL_NUMBER_OF_REPORTS_KEY, FieldValue.increment(1L));

            switch (type) {
                case AUTHOR_TYPE_KEY:
                    reportedUserReference = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).collection(REPORTED_ITEMS_KEY).document(userId);
                    reporterIncrementReference = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId).collection(REPORTERS_KEY).document(getCurrentUserId());
                    reference1 = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId);

                    DocumentReference reportedIncrementReference = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId());
                    HashMap<String, Object> reportedIncrementDetails = new HashMap<>();
                    reportedIncrementDetails.put(TOTAL_NUMBER_OF_USERS_REPORTED_KEY, FieldValue.increment(1L));
                    writeBatch.set(reportedIncrementReference, reportedIncrementDetails, SetOptions.merge());

                    break;
                case LIBRARY_TYPE_KEY:

                    reportedUserReference = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).collection(REPORTED_ITEMS_KEY).document(libraryId);
                    reporterIncrementReference = getFirebaseFirestoreInstance().collection(ALL_LIBRARY_KEY).document(libraryId).collection(REPORTERS_KEY).document(getCurrentUserId());
                    reference1 = getFirebaseFirestoreInstance().collection(ALL_LIBRARY_KEY).document(libraryId);

                    DocumentReference reportedIncrementReference1 = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId());
                    HashMap<String, Object> reportedIncrementDetails1 = new HashMap<>();
                    reportedIncrementDetails1.put(TOTAL_NUMBER_OF_LIBRARY_REPORTED_KEY, FieldValue.increment(1L));
                    writeBatch.set(reportedIncrementReference1, reportedIncrementDetails1, SetOptions.merge());

                    break;
                case TUTORIAL_TYPE_KEY:

                    reportedUserReference = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).collection(REPORTED_ITEMS_KEY).document(tutorialId);
                    reporterIncrementReference = getFirebaseFirestoreInstance().collection(ALL_TUTORIAL_KEY).document(tutorialId).collection(REPORTERS_KEY).document(getCurrentUserId());
                    reference1 = getFirebaseFirestoreInstance().collection(ALL_TUTORIAL_KEY).document(tutorialId);


                    DocumentReference reportedIncrementReference2 = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId());
                    HashMap<String, Object> reportedIncrementDetails2 = new HashMap<>();
                    reportedIncrementDetails2.put(TOTAL_NUMBER_OF_TUTORIALS_REPORTED_KEY, FieldValue.increment(1L));
                    writeBatch.set(reportedIncrementReference2, reportedIncrementDetails2, SetOptions.merge());

                    break;
            }
            writeBatch.set(reportedUserReference, reportedDetails, SetOptions.merge());
            writeBatch.set(reporterIncrementReference, reporterIncrementDetails, SetOptions.merge());
            writeBatch.set(reference1, details1, SetOptions.merge());

            writeBatch.commit().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    String activityLogType = "NONE";
                    switch (type) {
                        case AUTHOR_TYPE_KEY:
                            activityLogType = ACTIVITY_LOG_USER_REPORT_USER_TYPE_KEY;
                            break;

                        case LIBRARY_TYPE_KEY:
                            activityLogType = ACTIVITY_LOG_USER_REPORT_LIBRARY_TYPE_KEY;
                            break;

                        case TUTORIAL_TYPE_KEY:
                            activityLogType = ACTIVITY_LOG_USER_REPORT_TUTORIAL_TYPE_KEY;
                            break;
                    }
                    GlobalConfig.updateActivityLog(activityLogType, userId, libraryId, tutorialId, null, null, null, new GlobalConfig.ActionCallback() {
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
    }
    public static void unReport(String type,String userId,String libraryId,String tutorialId,ActionCallback actionCallback){
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();
      DocumentReference reportedUserReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).collection(REPORTED_ITEMS_KEY).document(userId);

      DocumentReference reporterIncrementReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId).collection(REPORTERS_KEY).document(getCurrentUserId());



        DocumentReference reference1 =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId);
        HashMap<String,Object> details1 = new HashMap<>();
        details1.put(TOTAL_NUMBER_OF_REPORTS_KEY,FieldValue.increment(-1L));

      switch(type){
          case AUTHOR_TYPE_KEY:
              reportedUserReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).collection(REPORTED_ITEMS_KEY).document(userId);
              reporterIncrementReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId).collection(REPORTERS_KEY).document(getCurrentUserId());
              reference1 =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId);

              DocumentReference reportedIncrementReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId());
              HashMap<String,Object> reportedIncrementDetails = new HashMap<>();
              reportedIncrementDetails.put(TOTAL_NUMBER_OF_USERS_REPORTED_KEY,FieldValue.increment(-1L));
              writeBatch.set(reportedIncrementReference,reportedIncrementDetails,SetOptions.merge());

              break;
          case LIBRARY_TYPE_KEY:

              reportedUserReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).collection(REPORTED_ITEMS_KEY).document(libraryId);
              reporterIncrementReference =  getFirebaseFirestoreInstance().collection(ALL_LIBRARY_KEY).document(libraryId).collection(REPORTERS_KEY).document(getCurrentUserId());
              reference1 =  getFirebaseFirestoreInstance().collection(ALL_LIBRARY_KEY).document(libraryId);

              DocumentReference reportedIncrementReference1 =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId());
              HashMap<String,Object> reportedIncrementDetails1 = new HashMap<>();
              reportedIncrementDetails1.put(TOTAL_NUMBER_OF_LIBRARY_REPORTED_KEY,FieldValue.increment(-1L));
              writeBatch.set(reportedIncrementReference1,reportedIncrementDetails1,SetOptions.merge());
              break;

          case TUTORIAL_TYPE_KEY:

              reportedUserReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId()).collection(REPORTED_ITEMS_KEY).document(tutorialId);
              reporterIncrementReference =  getFirebaseFirestoreInstance().collection(ALL_TUTORIAL_KEY).document(tutorialId).collection(REPORTERS_KEY).document(getCurrentUserId());
              reference1 =  getFirebaseFirestoreInstance().collection(ALL_TUTORIAL_KEY).document(tutorialId);


              DocumentReference reportedIncrementReference2 =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId());
              HashMap<String,Object> reportedIncrementDetails2 = new HashMap<>();
              reportedIncrementDetails2.put(TOTAL_NUMBER_OF_TUTORIALS_REPORTED_KEY,FieldValue.increment(-1L));
              writeBatch.set(reportedIncrementReference2,reportedIncrementDetails2,SetOptions.merge());

              break;
      }
        writeBatch.delete(reportedUserReference);
        writeBatch.delete(reporterIncrementReference);
        writeBatch.set(reference1,details1,SetOptions.merge());

      writeBatch.commit().addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {

          }
      }).addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void unused) {
            String activityLogType = "NONE";
              switch(type){
                  case AUTHOR_TYPE_KEY:
                      activityLogType = ACTIVITY_LOG_USER_UNREPORT_USER_TYPE_KEY;
                      break;

                  case LIBRARY_TYPE_KEY:
                      activityLogType = ACTIVITY_LOG_USER_UNREPORT_LIBRARY_TYPE_KEY;
                      break;

                  case TUTORIAL_TYPE_KEY:
                      activityLogType = ACTIVITY_LOG_USER_UNREPORT_TUTORIAL_TYPE_KEY;
                      break;
              }
              GlobalConfig.updateActivityLog(activityLogType, userId, libraryId, tutorialId, null, null, null,  new GlobalConfig.ActionCallback() {
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

    public static void followUser(Context context,String userId,ActionCallback actionCallback){
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();
        DocumentReference userReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId());
        HashMap<String,Object> userDetails = new HashMap<>();
        userDetails.put(TOTAL_USERS_FOLLOWING_KEY,FieldValue.increment(1L));
        userDetails.put(USERS_FOLLOWING_LIST_KEY,FieldValue.arrayUnion(userId));
        userDetails.put(TIME_STAMP_KEY,FieldValue.serverTimestamp());
        writeBatch.set(userReference,userDetails,SetOptions.merge());


        DocumentReference authorReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId);
        HashMap<String,Object> authorDetails = new HashMap<>();
        userDetails.put(TOTAL_FOLLOWERS_KEY,FieldValue.increment(1L));
        userDetails.put(FOLLOWERS_LIST_KEY,FieldValue.arrayUnion(getCurrentUserId()));
        userDetails.put(TIME_STAMP_KEY,FieldValue.serverTimestamp());
        writeBatch.set(authorReference,authorDetails,SetOptions.merge());


        writeBatch.commit().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                actionCallback.onFailed(e.getMessage());

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                actionCallback.onSuccess();
                addToUsersFollowingList(context,userId);

                GlobalConfig.updateActivityLog(ACTIVITY_LOG_USER_FOLLOW_USER_TYPE_KEY, userId, "", "", null, null, null,  new GlobalConfig.ActionCallback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onFailed(String errorMessage) {

                    }
                });


            }
        });
    }
    public static void unFollowUser(Context context,String userId,ActionCallback actionCallback){
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();
        DocumentReference userReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(getCurrentUserId());
        HashMap<String,Object> userDetails = new HashMap<>();
        userDetails.put(TOTAL_USERS_FOLLOWING_KEY,FieldValue.increment(-1L));
        userDetails.put(USERS_FOLLOWING_LIST_KEY,FieldValue.arrayRemove(userId));
        userDetails.put(TIME_STAMP_KEY,FieldValue.serverTimestamp());
        writeBatch.set(userReference,userDetails,SetOptions.merge());


        DocumentReference authorReference =  getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(userId);
        HashMap<String,Object> authorDetails = new HashMap<>();
        userDetails.put(TOTAL_FOLLOWERS_KEY,FieldValue.increment(-1L));
        userDetails.put(FOLLOWERS_LIST_KEY,FieldValue.arrayRemove(getCurrentUserId()));
        userDetails.put(TIME_STAMP_KEY,FieldValue.serverTimestamp());
        writeBatch.set(authorReference,authorDetails,SetOptions.merge());


        writeBatch.commit().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                actionCallback.onFailed(e.getMessage());

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                actionCallback.onSuccess();
                removeUserFromUsersFollowingList(context,userId);

                GlobalConfig.updateActivityLog(ACTIVITY_LOG_USER_UNFOLLOW_USER_TYPE_KEY, userId, "", "", null, null, null,  new GlobalConfig.ActionCallback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onFailed(String errorMessage) {

                    }
                });


            }
        });
    }

    public static boolean isFollowing(Context context,String userId){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName()+getCurrentUserId(), MODE_PRIVATE);
        String followingList = sharedPreferences.getString(GlobalConfig.USERS_FOLLOWING_LIST_KEY,"");

        return followingList.contains(userId);
    }
    public static void addToUsersFollowingList(Context context,String userId){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName()+getCurrentUserId(), MODE_PRIVATE);
        String oldList = sharedPreferences.getString(GlobalConfig.USERS_FOLLOWING_LIST_KEY,"");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!oldList.contains(userId)) {
            editor.putString(GlobalConfig.USERS_FOLLOWING_LIST_KEY, oldList + userId + "-NEXT-");
        }
        editor.apply();

    }
    public static void removeUserFromUsersFollowingList(Context context,String userId){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName()+getCurrentUserId(), MODE_PRIVATE);
        String oldList = sharedPreferences.getString(GlobalConfig.USERS_FOLLOWING_LIST_KEY,"");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(oldList.contains(userId)) {
           String newList = oldList.replace(userId + "-NEXT-","");
            editor.putString(GlobalConfig.USERS_FOLLOWING_LIST_KEY, newList);
        }
        editor.apply();

    }
    public static void discuss(PageDiscussionDataModel pageDiscussionDataModel, ActionCallback actionCallback ){
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();
        DocumentReference documentReference1 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(pageDiscussionDataModel.getAuthorId()).collection(GlobalConfig.MY_PAGES_DISCUSSION_KEY).document(pageDiscussionDataModel.getDiscussionId());
        HashMap<String,Object> discussionDetails = new HashMap<>();
        discussionDetails.put(GlobalConfig.DISCUSSION_POSTER_ID_KEY,pageDiscussionDataModel.getDiscussionPosterId());
        discussionDetails.put(GlobalConfig.DISCUSSION_DESCRIPTION_KEY,pageDiscussionDataModel.getDescription());
        discussionDetails.put(GlobalConfig.DISCUSSION_COVER_PHOTO_DOWNLOAD_URL_KEY,pageDiscussionDataModel.getCoverDownloadUrl());
        discussionDetails.put(GlobalConfig.PAGE_ID_KEY,pageDiscussionDataModel.getPageId());
        discussionDetails.put(GlobalConfig.AUTHOR_ID_KEY,pageDiscussionDataModel.getAuthorId());
        discussionDetails.put(GlobalConfig.PARENT_DISCUSSION_ID_KEY,pageDiscussionDataModel.getParentDiscussionId());
        discussionDetails.put(GlobalConfig.DISCUSSION_ID_KEY,pageDiscussionDataModel.getDiscussionId());
        discussionDetails.put(GlobalConfig.TUTORIAL_ID_KEY,pageDiscussionDataModel.getTutorialId());
        discussionDetails.put(GlobalConfig.FOLDER_ID_KEY,pageDiscussionDataModel.getFolderId());
        discussionDetails.put(GlobalConfig.IS_TUTORIAL_PAGE_KEY,pageDiscussionDataModel.isTutorialPage());
        discussionDetails.put(GlobalConfig.HAS_PARENT_DISCUSSION_KEY,pageDiscussionDataModel.hasParentDiscussion());
        discussionDetails.put(GlobalConfig.HAS_REPLIES_KEY,pageDiscussionDataModel.hasReplies());
        discussionDetails.put(GlobalConfig.IS_HIDDEN_BY_AUTHOR_KEY,pageDiscussionDataModel.isHiddenByAuthor());
        discussionDetails.put(GlobalConfig.IS_HIDDEN_BY_POSTER_KEY,pageDiscussionDataModel.isHiddenByPoster());
//        discussionDetails.put(GlobalConfig.TOTAL_REPLIES_KEY,FieldValue.increment(1L));
//        discussionDetails.put(GlobalConfig.TOTAL_LIKES_KEY,FieldValue.increment(1L));
//        discussionDetails.put(GlobalConfig.TOTAL_DISLIKES_KEY,FieldValue.increment(1L));
        discussionDetails.put(GlobalConfig.REPLIERS_ID_LIST_KEY,pageDiscussionDataModel.getRepliersIdList());
        discussionDetails.put(GlobalConfig.LIKERS_ID_LIST_KEY,pageDiscussionDataModel.getLikersIdList());
//        discussionDetails.put(GlobalConfig.DISLIKERS_ID_LIST_KEY,pageDiscussionDataModel.getDisLikersIdList());
        discussionDetails.put(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY,FieldValue.serverTimestamp());
        writeBatch.set(documentReference1,discussionDetails,SetOptions.merge());

        DocumentReference documentReference2 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(getCurrentUserId()).collection(GlobalConfig.OTHER_PAGES_DISCUSSION_KEY).document(pageDiscussionDataModel.getPageId());
        HashMap<String,Object> discussionDetails2 = new HashMap<>();
        discussionDetails2.put(GlobalConfig.AUTHOR_ID_LIST_KEY,FieldValue.arrayUnion(pageDiscussionDataModel.getAuthorId()));
        discussionDetails2.put(GlobalConfig.PAGE_ID_LIST_KEY,FieldValue.arrayUnion(pageDiscussionDataModel.getPageId()));
        if(pageDiscussionDataModel.hasParentDiscussion()) {
            discussionDetails2.put(GlobalConfig.PARENT_DISCUSSION_ID_LIST_KEY, FieldValue.arrayUnion(pageDiscussionDataModel.getPageId()));
        }
        writeBatch.set(documentReference2,discussionDetails2,SetOptions.merge());


        if(pageDiscussionDataModel.hasParentDiscussion()) {
            DocumentReference documentReference4 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(pageDiscussionDataModel.getAuthorId()).collection(GlobalConfig.MY_PAGES_DISCUSSION_KEY).document(pageDiscussionDataModel.getParentDiscussionId());
            HashMap<String, Object> discussionDetails4 = new HashMap<>();
            discussionDetails4.put(GlobalConfig.TOTAL_REPLIES_KEY, FieldValue.increment(1L));
            writeBatch.set(documentReference4, discussionDetails4, SetOptions.merge());
        }else{
        if(pageDiscussionDataModel.isTutorialPage()) {
            DocumentReference documentReference3 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(pageDiscussionDataModel.getTutorialId()).collection(GlobalConfig.ALL_TUTORIAL_PAGES_KEY).document(pageDiscussionDataModel.getPageId());
            HashMap<String, Object> discussionDetails3 = new HashMap<>();
            discussionDetails3.put(GlobalConfig.TOTAL_DISCUSSIONS_KEY, FieldValue.increment(1L));
            discussionDetails3.put(GlobalConfig.DISCUSSION_CONTRIBUTORS_ID_LIST_KEY, FieldValue.arrayUnion(getCurrentUserId()));
            writeBatch.set(documentReference3, discussionDetails3, SetOptions.merge());
        }else{
            DocumentReference documentReference4 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(pageDiscussionDataModel.getTutorialId()).collection(GlobalConfig.ALL_FOLDERS_KEY).document(pageDiscussionDataModel.getFolderId()).collection(GlobalConfig.ALL_FOLDER_PAGES_KEY).document(pageDiscussionDataModel.getPageId());
            HashMap<String, Object> discussionDetails4 = new HashMap<>();
            discussionDetails4.put(GlobalConfig.TOTAL_DISCUSSIONS_KEY, FieldValue.increment(1L));
            discussionDetails4.put(GlobalConfig.DISCUSSION_CONTRIBUTORS_ID_LIST_KEY, FieldValue.arrayUnion(getCurrentUserId()));
            writeBatch.set(documentReference4, discussionDetails4, SetOptions.merge());
        }
        }


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
    public static void deleteDiscussion(String discussionId,String parentDiscussionId,String pageId, String tutorialId,String folderId, String authorId, boolean isTutorialPage,boolean hasParentDiscussion, ActionCallback actionCallback ){
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();
        DocumentReference documentReference1 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(authorId).collection(GlobalConfig.MY_PAGES_DISCUSSION_KEY).document(discussionId);
        writeBatch.delete(documentReference1);


        if(hasParentDiscussion) {
            DocumentReference documentReference4 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(authorId).collection(GlobalConfig.MY_PAGES_DISCUSSION_KEY).document(parentDiscussionId);
            HashMap<String, Object> discussionDetails4 = new HashMap<>();
            discussionDetails4.put(GlobalConfig.TOTAL_DISCUSSIONS_KEY, FieldValue.increment(-1L));
            writeBatch.set(documentReference4, discussionDetails4, SetOptions.merge());
        }else{
        if(isTutorialPage) {
            DocumentReference documentReference3 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_TUTORIAL_PAGES_KEY).document(pageId);
            HashMap<String, Object> discussionDetails3 = new HashMap<>();
            discussionDetails3.put(GlobalConfig.TOTAL_DISCUSSIONS_KEY, FieldValue.increment(-1L));
            writeBatch.set(documentReference3, discussionDetails3, SetOptions.merge());
        }else{
            DocumentReference documentReference4 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_FOLDERS_KEY).document(folderId).collection(GlobalConfig.ALL_FOLDER_PAGES_KEY).document(pageId);
            HashMap<String, Object> discussionDetails4 = new HashMap<>();
            discussionDetails4.put(GlobalConfig.TOTAL_DISCUSSIONS_KEY, FieldValue.increment(-1L));
            writeBatch.set(documentReference4, discussionDetails4, SetOptions.merge());
        }
        }


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


    public static void likePage(Context context, PageDataModel pageDataModel, String pageId, String tutorialId, String folderId, String authorId, boolean isTutorialPage, boolean isIncreaseCount, ActionCallback actionCallback ) {
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();

        DocumentReference documentReference1 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(getCurrentUserId()).collection(GlobalConfig.LIKED_PAGES_KEY).document(pageId);
        if(isIncreaseCount){
        HashMap<String, Object> pageDetails1 = new HashMap<>();
            pageDetails1.put(GlobalConfig.IS_TUTORIAL_PAGE_KEY, isTutorialPage);
            pageDetails1.put(GlobalConfig.AUTHOR_ID_KEY, authorId);
            pageDetails1.put(GlobalConfig.TUTORIAL_ID_KEY, tutorialId);
            pageDetails1.put(GlobalConfig.FOLDER_ID_KEY, folderId);
            pageDetails1.put(GlobalConfig.PAGE_ID_KEY, pageId);
            writeBatch.set(documentReference1, pageDetails1, SetOptions.merge());
    }else{
            writeBatch.delete(documentReference1);
        }
        if(isTutorialPage) {
            DocumentReference documentReference3 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_TUTORIAL_PAGES_KEY).document(pageId);
            HashMap<String, Object> discussionDetails3 = new HashMap<>();
            if(isIncreaseCount){
                discussionDetails3.put(GlobalConfig.TOTAL_LIKES_KEY, FieldValue.increment(1L));
                discussionDetails3.put(GlobalConfig.LIKERS_ID_LIST_KEY, FieldValue.arrayUnion(getCurrentUserId()));

            }else{
                discussionDetails3.put(GlobalConfig.TOTAL_LIKES_KEY, FieldValue.increment(-1L));
                discussionDetails3.put(GlobalConfig.LIKERS_ID_LIST_KEY, FieldValue.arrayRemove(getCurrentUserId()));

            }
            writeBatch.set(documentReference3, discussionDetails3, SetOptions.merge());
        }else{
            DocumentReference documentReference4 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_FOLDERS_KEY).document(folderId).collection(GlobalConfig.ALL_FOLDER_PAGES_KEY).document(pageId);
            HashMap<String, Object> discussionDetails4 = new HashMap<>();
            if(isIncreaseCount){
                discussionDetails4.put(GlobalConfig.TOTAL_LIKES_KEY, FieldValue.increment(1L));
                discussionDetails4.put(GlobalConfig.LIKERS_ID_LIST_KEY, FieldValue.arrayUnion(getCurrentUserId()));
            }else{
                discussionDetails4.put(GlobalConfig.TOTAL_LIKES_KEY, FieldValue.increment(-1L));
                discussionDetails4.put(GlobalConfig.LIKERS_ID_LIST_KEY, FieldValue.arrayRemove(getCurrentUserId()));
            }
            writeBatch.set(documentReference4, discussionDetails4, SetOptions.merge());
        }

        DocumentReference userReference3 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(getCurrentUserId());
        HashMap<String, Object> userDetails3 = new HashMap<>();
        if(isIncreaseCount){
            userDetails3.put(GlobalConfig.LIKED_PAGES_LIST_KEY, FieldValue.arrayUnion(pageId));
        }else{
            userDetails3.put(GlobalConfig.LIKED_PAGES_LIST_KEY, FieldValue.arrayRemove(pageId));
        }
        writeBatch.set(userReference3, userDetails3, SetOptions.merge());

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
                        recordLikedPage( context, pageId,isIncreaseCount);
                        if(pageDataModel!=null) {
                            if (isIncreaseCount) {
                                if (!pageDataModel.getLikersIdList().contains(getCurrentUserId())) {
                                    pageDataModel.getLikersIdList().add(getCurrentUserId());
                                }
                            } else {
                                pageDataModel.getLikersIdList().remove(getCurrentUserId());
                            }
                        }
                    }
                });
    }

    public static boolean isPageLiked(Context context,String pageId){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName()+getCurrentUserId(), MODE_PRIVATE);
        String likedList = sharedPreferences.getString(GlobalConfig.LIKED_PAGES_LIST_KEY,"");

        return likedList.contains(pageId);
    }
    public static void recordLikedPage(Context context,String pageId,boolean isIncreaseCount){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName()+getCurrentUserId(), MODE_PRIVATE);
        String oldList = sharedPreferences.getString(GlobalConfig.LIKED_PAGES_LIST_KEY,"");
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(!oldList.contains(pageId)) {

            editor.putString(GlobalConfig.LIKED_PAGES_LIST_KEY, oldList + pageId + "-NEXT-");
        }else{
            if(!isIncreaseCount) {
                oldList = oldList.replace(pageId,"");
                editor.putString(GlobalConfig.LIKED_PAGES_LIST_KEY, oldList + pageId + "-NEXT-");
            }
        }
        editor.apply();

    }


    public static void likePageDiscussion(Context context,PageDiscussionDataModel pageDiscussionDataModel,String discussionId, String pageId, String tutorialId,String folderId, String authorId, boolean isTutorialPage,boolean isIncreaseCount, ActionCallback actionCallback ) {
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();

        DocumentReference documentReference1 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(getCurrentUserId()).collection(GlobalConfig.LIKED_DISCUSSIONS_KEY).document(discussionId);
        if(isIncreaseCount){
        HashMap<String, Object> discussionDetails1 = new HashMap<>();
        discussionDetails1.put(GlobalConfig.IS_TUTORIAL_PAGE_KEY, isTutorialPage);
        discussionDetails1.put(GlobalConfig.AUTHOR_ID_KEY, authorId);
        discussionDetails1.put(GlobalConfig.TUTORIAL_ID_KEY, tutorialId);
        discussionDetails1.put(GlobalConfig.FOLDER_ID_KEY, folderId);
        discussionDetails1.put(GlobalConfig.PAGE_ID_KEY, pageId);
        discussionDetails1.put(GlobalConfig.DISCUSSION_ID_KEY, discussionId);
        discussionDetails1.put(GlobalConfig.LIKERS_ID_LIST_KEY, FieldValue.arrayUnion(getCurrentUserId()));
        writeBatch.set(documentReference1, discussionDetails1, SetOptions.merge());
    }else{
            writeBatch.delete(documentReference1);
        }

        DocumentReference documentReference4 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(authorId).collection(GlobalConfig.MY_PAGES_DISCUSSION_KEY).document(discussionId);
            HashMap<String, Object> discussionDetails4 = new HashMap<>();
            if(isIncreaseCount){
                discussionDetails4.put(GlobalConfig.TOTAL_LIKES_KEY, FieldValue.increment(1L));
            }else{
                discussionDetails4.put(GlobalConfig.TOTAL_LIKES_KEY, FieldValue.increment(-1L));
            }
            writeBatch.set(documentReference4, discussionDetails4, SetOptions.merge());


        DocumentReference userReference3 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(getCurrentUserId());
        HashMap<String, Object> userDetails3 = new HashMap<>();
        if(isIncreaseCount){
            userDetails3.put(GlobalConfig.LIKED_PAGE_DISCUSSION_LIST_KEY, FieldValue.arrayUnion(discussionId));
        }else{
            userDetails3.put(GlobalConfig.LIKED_PAGE_DISCUSSION_LIST_KEY, FieldValue.arrayRemove(discussionId));
        }
        writeBatch.set(userReference3, userDetails3, SetOptions.merge());

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
//                        recordLikedPageDiscussion( context, discussionId,isIncreaseCount);
                                                if(isIncreaseCount) {
                                                    if (!pageDiscussionDataModel.getLikersIdList().contains(getCurrentUserId())) {
                                                        pageDiscussionDataModel.getLikersIdList().add(getCurrentUserId());
                                                    }
                                                }else{
                                                    pageDiscussionDataModel.getLikersIdList().remove(getCurrentUserId());
                                                }
                    }
                });
    }
    public static boolean isPageDiscussionLiked(Context context,String discussionId){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName()+getCurrentUserId(), MODE_PRIVATE);
        String likedList = sharedPreferences.getString(GlobalConfig.LIKED_PAGE_DISCUSSION_LIST_KEY,"");

        return likedList.contains(discussionId);
    }
    public static void recordLikedPageDiscussion(Context context,String discussionId,boolean isIncreaseCount){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName()+getCurrentUserId(), MODE_PRIVATE);
        String oldList = sharedPreferences.getString(GlobalConfig.LIKED_PAGE_DISCUSSION_LIST_KEY,"");
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(!oldList.contains(discussionId)) {

            editor.putString(GlobalConfig.LIKED_PAGE_DISCUSSION_LIST_KEY, oldList + discussionId + "-NEXT-");
        }else{
            if(!isIncreaseCount) {
                oldList = oldList.replace(discussionId,"");
                editor.putString(GlobalConfig.LIKED_PAGE_DISCUSSION_LIST_KEY, oldList + discussionId + "-NEXT-");
            }
        }
        editor.apply();

    }
//    public static void dislikePage(String pageId,boolean isIncreaseCount, String tutorialId,String folderId, boolean isTutorialPage, ActionCallback actionCallback ){
//        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();
//        if(isTutorialPage) {
//            DocumentReference documentReference3 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_TUTORIAL_PAGES_KEY).document(pageId);
//            HashMap<String, Object> discussionDetails3 = new HashMap<>();
//            if(isIncreaseCount){
//                discussionDetails3.put(GlobalConfig.TOTAL_LIKES_KEY, FieldValue.increment(1L));
//            }else{
//                discussionDetails3.put(GlobalConfig.TOTAL_LIKES_KEY, FieldValue.increment(-1L));
//            }
//            writeBatch.set(documentReference3, discussionDetails3, SetOptions.merge());
//        }else{
//            DocumentReference documentReference4 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_TUTORIAL_KEY).document(tutorialId).collection(GlobalConfig.ALL_FOLDERS_KEY).document(folderId).collection(GlobalConfig.ALL_FOLDER_PAGES_KEY).document(pageId);
//            HashMap<String, Object> discussionDetails4 = new HashMap<>();
//            if(isIncreaseCount){
//                discussionDetails4.put(GlobalConfig.TOTAL_LIKES_KEY, FieldValue.increment(1L));
//            }else{
//                discussionDetails4.put(GlobalConfig.TOTAL_LIKES_KEY, FieldValue.increment(-1L));
//            }
//            writeBatch.set(documentReference4, discussionDetails4, SetOptions.merge());
//        }
//
//
//        writeBatch.commit()
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        actionCallback.onFailed(e.getMessage());
//                    }
//                })
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        actionCallback.onSuccess();
//                    }
//                });
//    }


    @SuppressLint("MissingPermission")
    public static void loadNativeAd(Context context,int position,String nativeAdUnitId,ViewGroup viewGroup, boolean isFromCountDown, NativeAd.OnNativeAdLoadedListener onNativeAdLoadedListener){
          // TODO remove this if statement during production release
            if(true)return;
        if (context != null) {
//            String nativeAdUnitId = null;
            //if(GlobalConfig.isLearnEraAccount()){
//                nativeAdUnitId = GlobalConfig.TEST_NATIVE_AD_UNIT_ID;
//            }else{
//            nativeAdUnitId = GlobalConfig.MainActivity_NATIVE_AD_UNIT_ID;
//            }


            if(!isFromCountDown) {
                //load Ad Once
                new CountDownTimer(10000, 10000) {
                    @Override
                    public void onTick(long l) {

                        AdLoader.Builder builder = new AdLoader.Builder(context, nativeAdUnitId);

                        builder.forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                                NativeAd nativeAdToLoad = nativeAd;
                                onNativeAdLoadedListener.onNativeAdLoaded(nativeAdToLoad);
//                    Toast.makeText(context, "adloader is ", Toast.LENGTH_SHORT).show();

                            }
                        });

                        AdLoader adLoader = builder.withAdListener(new AdListener() {
                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                            }

                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
//                  Toast.makeText(getContext(), "failed at "+adLoadCounter + loadAdError , Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                            }

                            @Override
                            public void onAdOpened() {
                                super.onAdOpened();
//                  Toast.makeText(getContext(), "opened at "+adLoadCounter  , Toast.LENGTH_SHORT).show();

                            }
                        }).build();
                        adLoader.loadAd(new AdRequest.Builder().build());
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();

                //Load ad consequently
                new CountDownTimer(6100000, 35000) {
                    @Override
                    public void onTick(long l) {
//                    Toast.makeText(context, "counted: " + l, Toast.LENGTH_SHORT).show();

                        loadNativeAd(context, 0, nativeAdUnitId,viewGroup,true, new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {

                                NativeAd nativeAdToLoad = nativeAd;
                                View view = GlobalConfig.getNativeAdView(context, viewGroup, nativeAdToLoad, nativeAdUnitId, true);
                                if (view != null) {
                                    viewGroup.removeAllViews();
                                    viewGroup.addView(view);
                                }
                            }
                        });

                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }
            else{

                AdLoader.Builder builder = new AdLoader.Builder(context, nativeAdUnitId);

                builder.forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        NativeAd nativeAdToLoad = nativeAd;
                        onNativeAdLoadedListener.onNativeAdLoaded(nativeAdToLoad);
//                    Toast.makeText(context, "adloader is ", Toast.LENGTH_SHORT).show();

                    }
                });

                AdLoader adLoader = builder.withAdListener(new AdListener() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
//                  Toast.makeText(getContext(), "failed at "+adLoadCounter + loadAdError , Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
//                  Toast.makeText(getContext(), "opened at "+adLoadCounter  , Toast.LENGTH_SHORT).show();

                    }
                }).build();
                adLoader.loadAd(new AdRequest.Builder().build());
            }
        }



    }

    //         *
//    * https://googlemobileadssdk.page.link/admob-android-update-manifest         *
//    * to add a valid App ID inside the AndroidManifest.                          *
//    * Google Ad Manager publishers should follow instructions here:              *
//    * https://googlemobileadssdk.page.link/ad-manager-android-update-manifest.   *
//    ******************************************************************************
    public static NativeAdView getNativeAdView(Context context,ViewGroup viewGroup, NativeAd nativeAd, String nativeAdUnitId, boolean isFromCountDown) {
//        totalNumberOfNativeAdsOpened++;
        NativeAdView adView = null;
        if (context != null ) {
            LayoutInflater layoutInflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            adView = (NativeAdView) layoutInflater.inflate(R.layout.native_adview_template_layout, viewGroup, false);
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setMediaView(adView.findViewById(R.id.ad_media));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));
            adView.setPriceView(adView.findViewById(R.id.ad_price));
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            adView.setStoreView(adView.findViewById(R.id.ad_store));
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

            // The headline and mediaContent are guaranteed to be in every NativeAd.
            if (adView.getHeadlineView() != null) {
                ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
            }
            if (adView.getMediaView() != null) {
                adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
            } else {
                adView.findViewById(R.id.ad_media).setVisibility(View.GONE);
            }
            // These assets aren't guaranteed to be in every NativeAd, so it's important to
            // check before trying to display them.
            if (nativeAd.getBody() == null) {
                adView.getBodyView().setVisibility(View.INVISIBLE);
            } else {
                adView.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }

            if (nativeAd.getCallToAction() == null) {
                adView.getCallToActionView().setVisibility(View.INVISIBLE);
            } else {
                adView.getCallToActionView().setVisibility(View.VISIBLE);
                ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }

            if (nativeAd.getIcon() == null) {
                adView.getIconView().setVisibility(View.GONE);
            } else {
                ((ImageView) adView.getIconView()).setImageDrawable(
                        nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }

            if (nativeAd.getPrice() == null) {
                adView.getPriceView().setVisibility(View.INVISIBLE);
            } else {
                adView.getPriceView().setVisibility(View.VISIBLE);
                ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
            }

            if (nativeAd.getStore() == null) {
                adView.getStoreView().setVisibility(View.INVISIBLE);
            } else {
                adView.getStoreView().setVisibility(View.VISIBLE);
                ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
            }

            if (nativeAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.INVISIBLE);
            } else {
                ((RatingBar) adView.getStarRatingView())
                        .setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }

            if (nativeAd.getAdvertiser() == null) {
                adView.getAdvertiserView().setVisibility(View.INVISIBLE);
            } else {
                ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }
            // native ad view with this native ad.
            adView.setNativeAd(nativeAd);

            // Get the video controller for the ad. One will always be provided, even if the ad doesn't
            // have a video asset.
            VideoController vc = nativeAd.getMediaContent().getVideoController();

            // Updates the UI to say whether or not this ad has a video asset.
            if (vc.hasVideoContent()) {


                // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
                // VideoController will call methods on this object when events occur in the video
                // lifecycle.
                vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                    @Override
                    public void onVideoEnd() {
                        // Publishers should allow native ads to complete video playback before
                        // refreshing or replacing them with another ad in the same UI location.
//                   refresh.setEnabled(true);
//                   videoStatus.setText("Video status: Video playback has ended.");
                        super.onVideoEnd();
                    }
                });
            } else {
//           videoStatus.setText("Video status: Ad does not contain a video asset.");
//           refresh.setEnabled(true);
            }

            nativeAd = null;
        }
//        Toast.makeText(context, "returned", Toast.LENGTH_SHORT).show();
        viewGroup.removeAllViews();
        return adView;
    }

    public static void loadVideoAd(Context context,String adId, RewardedAdLoadCallback rewardedAdLoadCallback){
    AdRequest adRequest = new AdRequest.Builder().build();
    RewardedAd.load(context, adId, adRequest, new RewardedAdLoadCallback() {
        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            rewardedAdLoadCallback.onAdFailedToLoad(loadAdError);
        }
        @Override
        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
            super.onAdLoaded(rewardedAd);
            rewardedAdLoadCallback.onAdLoaded(rewardedAd);

        }
    });
    }
    public static void showVideoAd(Context context, Activity activity, @NonNull RewardedAd rewardedAd, FullScreenContentCallback fullScreenContentCallback, OnUserEarnedRewardListener onUserEarnedRewardListener ){
        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                fullScreenContentCallback.onAdClicked();
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                fullScreenContentCallback.onAdDismissedFullScreenContent();

            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                fullScreenContentCallback.onAdFailedToShowFullScreenContent(adError);
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                fullScreenContentCallback.onAdImpression();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                fullScreenContentCallback.onAdShowedFullScreenContent();
            }

        });

        if(rewardedAd!=null){
            rewardedAd.show(activity, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    onUserEarnedRewardListener.onUserEarnedReward(rewardItem);
                }
            });
        }
    }
    public static void createQuiz(Context context, String quizId, String quizTitle,String category, int totalQuestions,long totalQuizScore,long totalTheoryQuestions,long totalObjectiveQuestions, int totalTimeLimit, String quizDescription, int totalQuizFeeCoins, int totalQuizRewardCoins, ArrayList<ArrayList<String>> questionArrayList, ArrayList<Long> quizDateList, boolean isEdition, boolean isPublic, ActionCallback actionCallback){
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();
        DocumentReference documentReference1 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUIZ_KEY).document(quizId);
        HashMap<String,Object> quizDetails = new HashMap<>();
        quizDetails.put(GlobalConfig.QUIZ_ID_KEY,quizId);
        quizDetails.put(GlobalConfig.AUTHOR_ID_KEY,getCurrentUserId());
        quizDetails.put(GlobalConfig.QUIZ_DESCRIPTION_KEY,quizDescription);
        quizDetails.put(GlobalConfig.QUIZ_TITLE_KEY,quizTitle);
        quizDetails.put(GlobalConfig.CATEGORY_KEY,category);
        quizDetails.put(GlobalConfig.TOTAL_QUESTIONS_KEY,totalQuestions);
        quizDetails.put(GlobalConfig.TOTAL_QUIZ_SCORE_KEY,totalQuizScore);
        quizDetails.put(GlobalConfig.TOTAL_THEORY_QUESTIONS_KEY,totalTheoryQuestions);
        quizDetails.put(GlobalConfig.TOTAL_OBJECTIVE_QUESTIONS_KEY,totalObjectiveQuestions);
        //add extra 30 seconds in case of delay in network connectivity
        quizDetails.put(GlobalConfig.TOTAL_TIME_LIMIT_KEY,totalTimeLimit+30);
//      quizDetails.put(GlobalConfig.SINGLE_QUESTION_TIME_LIMIT_KEY,singleQuestionTimeLimit);
        quizDetails.put(GlobalConfig.IS_PUBLIC_KEY,isPublic);
        quizDetails.put(GlobalConfig.IS_CLOSED_KEY,false);
        quizDetails.put(GlobalConfig.TOTAL_QUIZ_FEE_COINS_KEY,totalQuizFeeCoins);
        quizDetails.put(GlobalConfig.TOTAL_QUIZ_REWARD_COINS_KEY,totalQuizRewardCoins);
        quizDetails.put(GlobalConfig.QUIZ_DATE_LIST_KEY,quizDateList);
        if(isEdition){
            quizDetails.put(GlobalConfig.DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());

        }else {
            quizDetails.put(GlobalConfig.DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
            quizDetails.put(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
        }

        for(int i=0; i<questionArrayList.size(); i++){
            quizDetails.put(GlobalConfig.QUESTION_LIST_KEY+"-"+i,questionArrayList.get(i));
        }

        writeBatch.set(documentReference1,quizDetails,SetOptions.merge());

        if (!isEdition) {
            DocumentReference userReference = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(getCurrentUserId());
            HashMap<String, Object> userDetails = new HashMap<>();
            userDetails.put(GlobalConfig.TOTAL_QUIZ_KEY, FieldValue.increment(1L));
            writeBatch.set(userReference, userDetails, SetOptions.merge());
        }


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
    public static void deleteQuiz(Context context, String quizId, ActionCallback actionCallback){
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();
        DocumentReference documentReference1 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUIZ_KEY).document(quizId);

        writeBatch.delete(documentReference1);

            DocumentReference userReference = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(getCurrentUserId());
            HashMap<String, Object> userDetails = new HashMap<>();
            userDetails.put(GlobalConfig.TOTAL_QUIZ_KEY, FieldValue.increment(-1L));
            writeBatch.set(userReference, userDetails, SetOptions.merge());


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
    public static void joinQuiz(Context context, QuizDataModel quizDataModel, ActionCallback actionCallback){
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();
        DocumentReference participantReference1 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUIZ_KEY).document(quizDataModel.getQuizId()).collection(GlobalConfig.ALL_PARTICIPANTS_KEY).document(getCurrentUserId());
        HashMap<String,Object> participantDetails = new HashMap<>();
        participantDetails.put(GlobalConfig.QUIZ_ID_KEY,quizDataModel.getQuizId());
        participantDetails.put(GlobalConfig.PARTICIPANT_ID_KEY,getCurrentUserId());
        participantDetails.put(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY,FieldValue.serverTimestamp());
        participantDetails.put(GlobalConfig.IS_ANSWER_SUBMITTED_KEY,false);
        participantDetails.put(GlobalConfig.IS_ANSWER_MARKED_BY_AUTHOR_KEY,false);
        writeBatch.set(participantReference1,participantDetails,SetOptions.merge());

        DocumentReference quizReference = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUIZ_KEY).document(quizDataModel.getQuizId());
        HashMap<String,Object> quizDetails = new HashMap<>();
        quizDetails.put(GlobalConfig.TOTAL_PARTICIPANTS_KEY, FieldValue.increment(1L));
        quizDetails.put(GlobalConfig.PARTICIPANTS_LIST_KEY, FieldValue.arrayUnion(getCurrentUserId()));
        writeBatch.set(quizReference,quizDetails,SetOptions.merge());


        DocumentReference userReference = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(getCurrentUserId());
        HashMap<String,Object> userDetails = new HashMap<>();
        userDetails.put(GlobalConfig.TOTAL_QUIZ_JOINED_KEY, FieldValue.increment(1L));
        userDetails.put(GlobalConfig.QUIZ_JOINED_LIST_KEY, FieldValue.arrayUnion(quizDataModel.getQuizId()));
        writeBatch.set(userReference,userDetails,SetOptions.merge());

//Deduct coin fee from wallet
        DocumentReference walletReference = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(getCurrentUserId()).collection(GlobalConfig.USER_WALLET_KEY).document(GlobalConfig.USER_WALLET_KEY);
        HashMap<String,Object> walletDetails = new HashMap<>();
        walletDetails.put(GlobalConfig.TOTAL_COIN_EQUITY_KEY, FieldValue.increment(-quizDataModel.getTotalQuizFeeCoins()));
        walletDetails.put(GlobalConfig.QUIZ_EARNINGS_HISTORY_LIST_KEY,FieldValue.arrayUnion("COIN-"+quizDataModel.getTotalQuizFeeCoins()+"-DESC-was deducted from your wallet for joining quiz"+"-DATE-"+GlobalConfig.getDate()));
        writeBatch.set(walletReference,walletDetails,SetOptions.merge());


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
                        GlobalConfig.newlyJoinedQuizList.add(quizDataModel.getQuizId());

                    }
                });
    }
    public static void incrementQuizViews(Context context, String quizId){
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();

        DocumentReference quizReference = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUIZ_KEY).document(quizId);
        HashMap<String,Object> quizDetails = new HashMap<>();
        quizDetails.put(GlobalConfig.TOTAL_NUMBER_OF_VIEWS_KEY, FieldValue.increment(1L));
        quizDetails.put(GlobalConfig.QUIZ_VIEWERS_LIST_KEY, FieldValue.arrayRemove(getCurrentUserId()));
        quizDetails.put(GlobalConfig.QUIZ_VIEWERS_LIST_KEY, FieldValue.arrayUnion(getCurrentUserId()));
        writeBatch.set(quizReference,quizDetails,SetOptions.merge());


        DocumentReference userReference = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(getCurrentUserId());
        HashMap<String,Object> userDetails = new HashMap<>();
        userDetails.put(GlobalConfig.TOTAL_NUMBER_OF_TIMES_QUIZ_VIEWED_KEY, FieldValue.increment(1L));
        userDetails.put(GlobalConfig.VIEWED_QUIZ_LIST_KEY, FieldValue.arrayRemove(quizId));
        userDetails.put(GlobalConfig.VIEWED_QUIZ_LIST_KEY, FieldValue.arrayUnion(quizId));
        writeBatch.set(userReference,userDetails,SetOptions.merge());


        writeBatch.commit();
    }
    //submits participant answer to the database
    public static void submitQuiz(Context context, String quizId,ArrayList<ArrayList<String>> answerList, ActionCallback actionCallback){
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();
        DocumentReference participantReference1 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUIZ_KEY).document(quizId).collection(GlobalConfig.ALL_PARTICIPANTS_KEY).document(getCurrentUserId());
        HashMap<String,Object> participantDetails = new HashMap<>();
        participantDetails.put(GlobalConfig.IS_ANSWER_SUBMITTED_KEY,true);
        participantDetails.put(GlobalConfig.TOTAL_SCORE_KEY,0L);
        participantDetails.put(GlobalConfig.ANSWER_SUBMITTED_TIME_STAMP_KEY,FieldValue.serverTimestamp());
        for(int position=0; position<answerList.size();position++) {
            participantDetails.put(GlobalConfig.ANSWER_LIST_KEY + "-" + position, answerList.get(position));
        }
        writeBatch.set(participantReference1,participantDetails,SetOptions.merge());


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
                                recordSubmittedQuiz(context,quizId);

                        actionCallback.onSuccess();
                    }
                });
    }
    public static void saveAuthorQuizAnswer(Context context, String quizId,ArrayList<ArrayList<String>> answerList, ActionCallback actionCallback){
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();
        DocumentReference participantReference1 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUIZ_KEY).document(quizId);
        HashMap<String,Object> participantDetails = new HashMap<>();
        participantDetails.put(GlobalConfig.IS_ANSWER_SUBMITTED_KEY,true);
        participantDetails.put(GlobalConfig.ANSWER_SUBMITTED_TIME_STAMP_KEY,FieldValue.serverTimestamp());
        for(int position=0; position<answerList.size();position++) {
            participantDetails.put(GlobalConfig.ANSWER_LIST_KEY + "-" + position, answerList.get(position));
        }
        writeBatch.set(participantReference1,participantDetails,SetOptions.merge());


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
                           recordSubmittedQuiz(context,quizId);

                        actionCallback.onSuccess();
                    }
                });
    }
    public static void markQuizAsClosed(Context context, String quizId, ActionCallback actionCallback){
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();
        DocumentReference participantReference1 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUIZ_KEY).document(quizId);
        HashMap<String,Object> participantDetails = new HashMap<>();
        participantDetails.put(GlobalConfig.IS_CLOSED_KEY,true);
        participantDetails.put(GlobalConfig.DATE_CLOSED_TIME_STAMP_KEY,FieldValue.serverTimestamp());

        writeBatch.set(participantReference1,participantDetails,SetOptions.merge());


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
                           recordSubmittedQuiz(context,quizId);

                        actionCallback.onSuccess();
                    }
                });
    }

//    public static void submitQuiz(Context context, String quizId,ArrayList<ArrayList<String>> answerList, ActionCallback actionCallback){
//        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();
//        DocumentReference participantReference1 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUIZ_KEY).document(quizId).collection(GlobalConfig.ALL_PARTICIPANTS_KEY).document(getCurrentUserId());
//        HashMap<String,Object> participantDetails = new HashMap<>();
////        participantDetails.put(GlobalConfig.TOTAL_ANSWERS_SUBMITTED_KEY, FieldValue.increment(1L));
////        participantDetails.put(GlobalConfig.ANSWER_SUBMITTED_POSITIONS_LIST_KEY, FieldValue.arrayUnion(questionSubmittedPosition));
//        participantDetails.put(GlobalConfig.ANSWER_SUBMITTED_TIME_STAMP_KEY,FieldValue.serverTimestamp());
//        for(int position=0; position<answerList.size();position++) {
//            participantDetails.put(GlobalConfig.ANSWER_LIST_KEY + "-" + position, answerList.get(position));
//        }
////        participantDetails.put(GlobalConfig.LAST_SUBMITTED_QUESTION_POSITION_KEY,questionSubmittedPosition);
////        participantDetails.put(GlobalConfig.LAST_SUBMITTED_TIME_STAMP_KEY,FieldValue.serverTimestamp());
//        writeBatch.set(participantReference1,participantDetails,SetOptions.merge());
//
//
//        writeBatch.commit()
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        actionCallback.onFailed(e.getMessage());
//                    }
//                })
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        actionCallback.onSuccess();
//                    }
//                });
//
////        recordSubmittedQuestion(context,quizId,questionSubmittedPosition);
//    }



    public static void getQuiz(Context context,String quizId,QuizCallback quizCallback){
        getFirebaseFirestoreInstance()
                .collection(GlobalConfig.ALL_QUIZ_KEY).document(quizId)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        quizCallback.onFailed(e.getMessage());

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        quizCallback.onSuccess(documentSnapshot);

                    }
                });

    }

    public static void recordViewedQuiz(Context context,String quizId){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName()+getCurrentUserId(), MODE_PRIVATE);
        String oldList = sharedPreferences.getString(GlobalConfig.VIEWED_QUIZ_LIST_KEY,"");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!oldList.contains(quizId)) {
            editor.putString(GlobalConfig.VIEWED_QUIZ_LIST_KEY, oldList + quizId + "-NEXT-");
        }
        editor.apply();

    }

    public static boolean isQuizViewed(Context context,String quizId){

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName()+getCurrentUserId(), MODE_PRIVATE);
        String viewedList = sharedPreferences.getString(GlobalConfig.VIEWED_QUIZ_LIST_KEY,"");

        return viewedList.contains(quizId);
    }
    public static void recordSubmittedQuiz(Context context,String quizId){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName()+getCurrentUserId(), MODE_PRIVATE);
        String oldList = sharedPreferences.getString(GlobalConfig.SUBMITTED_QUIZ_LIST_KEY,"");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!oldList.contains(quizId)) {
            editor.putString(GlobalConfig.SUBMITTED_QUIZ_LIST_KEY, oldList + quizId + "-NEXT-");
        }
        editor.apply();

    }

    public static boolean isQuizSubmitted(Context context,String quizId){

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName()+getCurrentUserId(), MODE_PRIVATE);
        String submittedList = sharedPreferences.getString(GlobalConfig.SUBMITTED_QUIZ_LIST_KEY,"");

        return submittedList.contains(quizId);
    }


    public static BottomSheetFormBuilderWidget getAnswerForm(Context context,String authorId,String questionId,String parentId,String answerId, boolean isEdition, boolean isAnswer, @Nullable AnswerDataModel answerDataModel, @NonNull AnswerCallback answerCallback){

        boolean[] isPhotoIncluded = new boolean[1];

        BottomSheetFormBuilderWidget  bottomSheetCatalogFormBuilderWidget =  new BottomSheetFormBuilderWidget(context);



        LinearLayout imageLayout = new LinearLayout(context);
        imageLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        imageLayout.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_LTR);
        imageLayout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView answerImageView = new ImageView(context);
        answerImageView.setLayoutParams(new LinearLayout.LayoutParams(0,200,1));
        answerImageView.setImageResource(R.drawable.ic_baseline_photo_camera_24);
        answerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GlobalConfig.createPopUpMenu(context, R.menu.pick_image_menu, answerImageView, new GlobalConfig.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClicked(MenuItem item) {
                        if(item.getItemId() == R.id.galleryId){
                            answerCallback.onImageGallerySelected(answerImageView);
                        }
                        else if(item.getItemId() == R.id.cameraId){
                            answerCallback.onCameraSelected(answerImageView);
                        }
                        return true;
                    }
                });

            }
        });

        ImageView removeImageView = new ImageView(context);
        removeImageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        removeImageView.setImageResource(R.drawable.ic_outline_cancel_24);
        removeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerImageView.setImageResource(R.drawable.ic_baseline_photo_camera_24);
                isPhotoIncluded[0] = false;
            }
        });
        imageLayout.addView(answerImageView,0);
        imageLayout.addView(removeImageView,1);

        bottomSheetCatalogFormBuilderWidget.addView(imageLayout);
        bottomSheetCatalogFormBuilderWidget.setTitle("Contribute your answer to the question")
                .setPositiveTitle("Answer");

        BottomSheetFormBuilderWidget.EditTextInput answerEditTextInput = new BottomSheetFormBuilderWidget.EditTextInput(context);
        answerEditTextInput.setHint("Enter your answer");
        bottomSheetCatalogFormBuilderWidget.addInputField(answerEditTextInput);

        if(isEdition && answerDataModel!=null) {
            answerId = answerDataModel.getAnswerId();
            answerEditTextInput.setText(answerDataModel.getAnswer());
            isPhotoIncluded[0] = answerDataModel.isPhotoIncluded();
            if( isPhotoIncluded[0]){
            Glide.with(context)
                    .load(answerDataModel.getAnswerPhotoDownloadUrl())
                    .centerCrop()
                    .into(answerImageView);
        }
        }

        final String finalAnswerId = answerId;
        bottomSheetCatalogFormBuilderWidget.setOnSubmit(new BottomSheetFormBuilderWidget.OnSubmitHandler(){
            @Override
            public void onSubmit(String[] body) {
                //super.onSubmit(body);

//                if(answerImageView.getDrawable().equals(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_photo_camera_24,getTheme()))){
//                    isPhotoIncluded[0] = false;
//                }else{
//                    isPhotoIncluded[0] = true;
//                }
//

//                    if(isPhotoIncluded[0]){
//
//                    }else{
////                    postAnswer(body[0]);
//
//                    }
                if(body[0].isEmpty() && !isPhotoIncluded[0]){
                    Toast.makeText(context, "Please enter an answer", Toast.LENGTH_SHORT).show();
                }else {
                    answerCallback.onStart(finalAnswerId);
                    startPostAnswer(context, authorId, questionId,parentId, finalAnswerId, body[0], answerImageView, isPhotoIncluded[0], isEdition,isAnswer, new GlobalConfig.ActionCallback() {
                        @Override
                        public void onSuccess() {
                            answerCallback.onSuccess(finalAnswerId);
                        }

                        @Override
                        public void onFailed(String errorMessage) {
                            answerCallback.onFailed(errorMessage);

                        }
                    });
                }
            }
        });
        bottomSheetCatalogFormBuilderWidget
                .render();

        return bottomSheetCatalogFormBuilderWidget;
    }
    private static void sendAnswerDetailsToDatabase(Context context,String authorId,String questionId,String answer,String parentId,String answerId,boolean isPhotoIncluded,String answerPhotoDownloadUrl,boolean isEdition,boolean isAnswer,@NonNull GlobalConfig.ActionCallback actionCallback) {
            WriteBatch writeBatch = GlobalConfig.getFirebaseFirestoreInstance().batch();
            DocumentReference documentReference1 = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUESTIONS_KEY).document(questionId).collection(GlobalConfig.ALL_ANSWERS_KEY).document(answerId);
            HashMap<String,Object> quizDetails = new HashMap<>();
            quizDetails.put(GlobalConfig.QUESTION_ID_KEY,questionId);
            quizDetails.put(GlobalConfig.ANSWER_ID_KEY,answerId);
            quizDetails.put(GlobalConfig.PARENT_ID_KEY,parentId);
            quizDetails.put(GlobalConfig.AUTHOR_ID_KEY,authorId);
            quizDetails.put(GlobalConfig.CONTRIBUTOR_ID_KEY,GlobalConfig.getCurrentUserId());
            quizDetails.put(GlobalConfig.ANSWER_BODY_KEY,answer);
            quizDetails.put(GlobalConfig.ANSWER_PHOTO_DOWNLOAD_URL_KEY,answerPhotoDownloadUrl);
            quizDetails.put(GlobalConfig.IS_PHOTO_INCLUDED_KEY,isPhotoIncluded);
            quizDetails.put(GlobalConfig.IS_ANSWER_KEY,isAnswer);
            if(isEdition){
                quizDetails.put(GlobalConfig.DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());

            }else {
                quizDetails.put(GlobalConfig.DATE_EDITED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
                quizDetails.put(GlobalConfig.DATE_CREATED_TIME_STAMP_KEY, FieldValue.serverTimestamp());
            }
            writeBatch.set(documentReference1,quizDetails, SetOptions.merge());

            if (!isEdition) {
                if (isAnswer) {
                    //if it is answer then increment the number of answers to a question but if it's just a reply do otherwise
                    DocumentReference questionReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUESTIONS_KEY).document(questionId);
                    HashMap<String, Object> questionDetails = new HashMap<>();
                    questionDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ANSWER_KEY, FieldValue.increment(1L));
                    questionDetails.put(GlobalConfig.ANSWER_CONTRIBUTORS_LIST_KEY, FieldValue.arrayUnion(GlobalConfig.getCurrentUserId()));
                    writeBatch.set(questionReference, questionDetails, SetOptions.merge());


                    DocumentReference userReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_USERS_KEY).document(GlobalConfig.getCurrentUserId());
                    HashMap<String, Object> userDetails = new HashMap<>();
                    userDetails.put(GlobalConfig.TOTAL_NUMBER_OF_ANSWER_CONTRIBUTED_KEY, FieldValue.increment(1L));
                    userDetails.put(GlobalConfig.QUESTIONS_ANSWERED_LIST_KEY, FieldValue.arrayUnion(questionId));
                    writeBatch.set(userReference, userDetails, SetOptions.merge());

                }else{
                    DocumentReference answerReference = GlobalConfig.getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUESTIONS_KEY).document(questionId).collection(GlobalConfig.ALL_ANSWERS_KEY).document(parentId);
                    HashMap<String, Object> answernDetails = new HashMap<>();
                    answernDetails.put(GlobalConfig.TOTAL_REPLIES_KEY, FieldValue.increment(1L));
                    answernDetails.put(GlobalConfig.REPLY_CONTRIBUTORS_LIST_KEY, FieldValue.arrayUnion(GlobalConfig.getCurrentUserId()));
                    writeBatch.set(answerReference, answernDetails, SetOptions.merge());

                }

                }


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
    private static void startPostAnswer(Context context,String authorId,String questionId,String parentId,String answerId, String answer, ImageView answerPhoto, boolean isPhotoIncluded, boolean isEdition,boolean isAnswer,@NonNull GlobalConfig.ActionCallback actionCallback){

        if(isPhotoIncluded || answerPhoto!=null) {
            StorageReference answerPhotoStorageReference = GlobalConfig.getFirebaseStorageInstance().getReference().child(GlobalConfig.ALL_QUESTIONS_KEY + "/" + questionId + "/" + GlobalConfig.ALL_ANSWERS_KEY + "/" + answerId + "/" + GlobalConfig.PHOTOS_KEY + ".PNG");
            answerPhoto.setDrawingCacheEnabled(true);
            Bitmap coverPhotoBitmap = answerPhoto.getDrawingCache();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            coverPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 5, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            UploadTask coverPhotoUploadTask = answerPhotoStorageReference.putBytes(bytes);

            coverPhotoUploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    answerPhotoUploadListener.onFailed(e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    coverPhotoUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            return answerPhotoStorageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String answerPhotoDownloadUrl = String.valueOf(task.getResult());
//                            answerPhotoUploadListener.onSuccess(answer, isPhotoIncluded, isEdition, answerPhotoDownloadUrl);

                            sendAnswerDetailsToDatabase(context,authorId,questionId,answer,parentId, answerId, isPhotoIncluded, answerPhotoDownloadUrl, isEdition,isAnswer, new ActionCallback() {
                                @Override
                                public void onSuccess() {
                                    actionCallback.onSuccess();
                                }

                                @Override
                                public void onFailed(String errorMessage) {
                                    actionCallback.onFailed(errorMessage);

                                }
                            });
                        }
                    });
                }
            });
        }else{
            sendAnswerDetailsToDatabase(context,authorId,questionId,answer,parentId, answerId, isPhotoIncluded, "", isEdition,isAnswer, new GlobalConfig.ActionCallback() {
                @Override
                public void onSuccess() {
                    actionCallback.onSuccess();

                }

                @Override
                public void onFailed(String errorMessage) {
                    actionCallback.onFailed(errorMessage);

                }
            });
        }

    }

    public static void deleteAnswer(String questionId,@Nullable String parentId,String answerId, String authorId,boolean isAnswer, ActionCallback actionCallback ){

        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();
        DocumentReference documentReference1 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUESTIONS_KEY).document(questionId).collection(GlobalConfig.ALL_ANSWERS_KEY).document(answerId);
        writeBatch.delete(documentReference1);


        if(isAnswer) {
            DocumentReference documentReference3 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUESTIONS_KEY).document(questionId);
            HashMap<String, Object> answerDetails3 = new HashMap<>();
            answerDetails3.put(GlobalConfig.TOTAL_NUMBER_OF_ANSWER_KEY, FieldValue.increment(-1L));
            writeBatch.set(documentReference3, answerDetails3, SetOptions.merge());

        }else{
            DocumentReference documentReference4 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUESTIONS_KEY).document(questionId).collection(GlobalConfig.ALL_ANSWERS_KEY).document(parentId);
            HashMap<String, Object> answerDetails4 = new HashMap<>();
            answerDetails4.put(GlobalConfig.TOTAL_REPLIES_KEY, FieldValue.increment(-1L));
            writeBatch.set(documentReference4, answerDetails4, SetOptions.merge());
        }


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

    public static void voteAnswer(AnswerDataModel answerDataModel,boolean isUpVote, ActionCallback actionCallback){
       //check if user has upVoted or downVoted, if so, then return;
        if((isUpVote && answerDataModel.getUpVotersIdList().contains(getCurrentUserId())) || (!isUpVote && answerDataModel.getDownVotersIdList().contains(getCurrentUserId())))return;
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();

            DocumentReference documentReference4 = getFirebaseFirestoreInstance().collection(GlobalConfig.ALL_QUESTIONS_KEY).document(answerDataModel.getQuestionId()).collection(GlobalConfig.ALL_ANSWERS_KEY).document(answerDataModel.getAnswerId());
            HashMap<String, Object> answerDetails4 = new HashMap<>();
            if(isUpVote){
            answerDetails4.put(GlobalConfig.TOTAL_UP_VOTES_KEY, FieldValue.increment(1L));
            answerDetails4.put(GlobalConfig.UP_VOTERS_ID_LIST_KEY, FieldValue.arrayUnion(getCurrentUserId()));

                if(answerDataModel.getDownVotersIdList().contains(getCurrentUserId())){
                answerDetails4.put(GlobalConfig.TOTAL_DOWN_VOTES_KEY, FieldValue.increment(-1L));
                answerDetails4.put(GlobalConfig.DOWN_VOTERS_ID_LIST_KEY, FieldValue.arrayRemove(getCurrentUserId()));
            }
            }else{
                answerDetails4.put(GlobalConfig.TOTAL_DOWN_VOTES_KEY, FieldValue.increment(1L));
                answerDetails4.put(GlobalConfig.DOWN_VOTERS_ID_LIST_KEY, FieldValue.arrayUnion(getCurrentUserId()));

                if(answerDataModel.getUpVotersIdList().contains(getCurrentUserId())){
                    answerDetails4.put(GlobalConfig.TOTAL_UP_VOTES_KEY, FieldValue.increment(-1L));
                    answerDetails4.put(GlobalConfig.UP_VOTERS_ID_LIST_KEY, FieldValue.arrayRemove(getCurrentUserId()));
                }
            }
            writeBatch.set(documentReference4, answerDetails4, SetOptions.merge());


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
                        //UPDATE THE VOTERS LIST IN THE DATA MODEL OF THE ANSWER
                        if(isUpVote){
                            answerDataModel.getUpVotersIdList().add(getCurrentUserId());
                            answerDataModel.setTotalUpVotes(answerDataModel.getTotalUpVotes()+1);

                            if(answerDataModel.getDownVotersIdList().contains(getCurrentUserId())){
                                answerDataModel.getDownVotersIdList().remove(getCurrentUserId());
                                answerDataModel.setTotalDownVotes(answerDataModel.getTotalDownVotes()-1);

                            }
                        }else{
                            answerDataModel.getDownVotersIdList().add(getCurrentUserId());
                            answerDataModel.setTotalDownVotes(answerDataModel.getTotalDownVotes()+1);

                            if(answerDataModel.getUpVotersIdList().contains(getCurrentUserId())){
                                answerDataModel.getUpVotersIdList().remove(getCurrentUserId());
                                answerDataModel.setTotalUpVotes(answerDataModel.getTotalUpVotes()-1);

                            }
                        }
                        actionCallback.onSuccess();
                    }
                });
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

    public static void sendNotificationToUsers(String notificationType,String notificationId,ArrayList<String>receiversIdList,ArrayList<String> modelInfoList,String title,String message,ActionCallback actionCallback){
        WriteBatch writeBatch = getFirebaseFirestoreInstance().batch();


        for(int i=0; i<receiversIdList.size();i++){
            DocumentReference notificationReference = getFirebaseFirestoreInstance().collection(ALL_USERS_KEY).document(receiversIdList.get(i)).collection(PERSONALIZED_NOTIFICATIONS_KEY).document(notificationId);
            HashMap<String,Object> notesInfo = new HashMap<>();
            notesInfo.put(NOTIFICATION_TYPE_KEY,notificationType);
            notesInfo.put(NOTIFICATION_ID_KEY,notificationId);
            notesInfo.put(NOTIFICATION_SENDER_ID_KEY,getCurrentUserId());
            notesInfo.put(NOTIFICATION_TITLE_KEY,title);
            notesInfo.put(NOTIFICATION_MESSAGE_KEY,message);
            notesInfo.put(NOTIFICATION_MODEL_INFO_LIST_KEY,modelInfoList);
            notesInfo.put(DATE_NOTIFIED_TIME_STAMP_KEY,FieldValue.serverTimestamp());
            notesInfo.put(IS_SEEN_KEY,false);
            writeBatch.set(notificationReference,notesInfo,SetOptions.merge());
        }
        writeBatch.commit()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(actionCallback !=null) {
                            actionCallback.onFailed(e.getMessage());
                        }
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if(actionCallback !=null) {
                            actionCallback.onSuccess();
                        }
                    }
                });
        return;

    }

    public static void addCoinsToWallet(){

    }
    public static void deductCoinsFromWallet(){

    }

    public static void withdrawCoinsFromWallet(){

    }


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

    public interface OnDocumentExistStatusCallback {

        void onExist(DocumentSnapshot documentSnapshot);
        void onNotExist();
        void onFailed(@NonNull String errorMessage);

    }
    public interface AnswerCallback{

        void onImageGallerySelected(ImageView imageView);
        void onCameraSelected(ImageView imageView);
        void onVideoGallery(View view);
        void onStart(String answerId);
        void onSuccess(String answerId);
        void onFailed(String errorMessage);

    }

    public interface ActionCallback{
            void onSuccess();
            void onFailed(String errorMessage);
    }
    public interface QuizCallback{
            void onSuccess(DocumentSnapshot documentSnapshot);
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
