package com.palria.learnera.models;

public class UserActivityDataModel {

    /**
     * title for the activity log to show
     * eg : User created a new Library Hello First Library
     */
    private String title;
    private String date;
    /**
     * body to show as in rating body/comment
     * messages/comment by user like : eg wow i like the library. :)
     */
    private String body;
    /**
     * icon or downloadUrl of the library, tutorial, author etc... or any other if not provided
     * default icon will be rendered.
     */
    private String icon;

    //other fileds should be here
    /**
     * when user clicks where to redirect the user to that's id should be here
     */

    private int entryId;

    /**
     * model type should be an rating, tutorial, library or any other action model name
     */
    private  String modelType;


    /**
     * set model as a date divider }
     * show date divider in current list
     *
     */
    private boolean asDateDivider;

    /**
     *
     * @param title
     * @param date
     * @param body
     * @param icon
     * @param entryId
     * @param modelType
     */

    public UserActivityDataModel(String title, String date, String body, String icon, int entryId, String modelType, boolean isAsDiiver) {
        this.title = title;
        this.date = date;
        this.body = body;
        this.icon = icon;
        this.entryId = entryId;
        this.modelType = modelType;
        this.asDateDivider=isAsDiiver;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public boolean isAsDateDivider() {
        return asDateDivider;
    }

    public void setAsDateDivider(boolean asDateDivider) {
        this.asDateDivider = asDateDivider;
    }
}
