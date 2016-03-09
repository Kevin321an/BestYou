package com.bestofyou.fm.bestofyou.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import com.bestofyou.fm.bestofyou.RecyclerListAdapter;
import com.bestofyou.fm.bestofyou.Summary;

/**
 * Created by FM on 12/7/2015.
 */
public class SummaryProvider extends ContentProvider {

    static final int TOTAL = 1;
    static final int RUBRIC = 2;
    static final int HISTORY = 3;
    static SummaryHelper mOpenHelper;

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        mOpenHelper = new SummaryHelper(getContext());
        return true;
    }

    static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SummaryContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, SummaryContract.PATH_HISTORY, HISTORY);
        matcher.addURI(authority, SummaryContract.PATH_RUBRIC, RUBRIC);
        matcher.addURI(authority, SummaryContract.PATH_TOTAL, TOTAL);
        //matcher.addURI(authority, SummaryContract.PATH_RUBRIC + "/*/#", SUMMARY);
        //"PATH" - matches "PATH" exactly
        //"PATH/#" matches "PATH" Followed by a number
        //"PATH/*" - MATCHES "PATH" followed by any string
        //"Path/*/other/#" matches "PATH" followed by a string followed by "other" followed by a number
        return matcher;
    }


    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case HISTORY:
                return SummaryContract.UsrHistory.CONTENT_TYPE;
            case RUBRIC:
                return SummaryContract.Rubric.CONTENT_TYPE;
            case TOTAL:
                return SummaryContract.Total.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        /*Implement this to handle requests to update one or more rows.
        The implementation should update all rows matching the selection to set the columns according to the provided values map.
         As a courtesy, call notifyChange() after updating. This method can be called from multiple threads, as described in Processes and Threads.

            Parameters
            uri	The URI to query. This can potentially have a record ID if this is an update request for a specific record.
            values	A set of column_name/value pairs to update in the database. This must not be null.
            selection	An optional filter to match rows to update.
            Returns
            the number of rows affected.*/
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case HISTORY:
               // normalizeDate(values);
                rowsUpdated = db.update(SummaryContract.UsrHistory.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case RUBRIC:
                rowsUpdated = db.update(SummaryContract.Rubric.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case TOTAL:
                rowsUpdated = db.update(SummaryContract.Total.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }


    public Uri insert(Uri uri, ContentValues values) {
        /*Implement this to handle requests to insert a new row. As a courtesy, call notifyChange() after inserting.
        This method can be called from multiple threads, as described in Processes and Threads.

        Parameters
        uri	The content:// URI of the insertion request. This must not be null.
        values	A set of column_name/value pairs to add to the database. This must not be null.
        Returns
        The URI for the newly inserted item.*/

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case HISTORY: {
                //normalizeDate(values); //call methods of normalizeDate and WeatherContract.normalizeDate to set the Date in the database
                long _id = db.insert(SummaryContract.UsrHistory.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = SummaryContract.UsrHistory.buildHistoryUri(_id);//build the content Uri follow by ID
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case RUBRIC:{
                long _id=db.insert(SummaryContract.Rubric.TABLE_NAME,null,values);
                if(_id>0)
                    returnUri=SummaryContract.Rubric.buildRubricUri(_id);//build the content Uri follow by ID
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TOTAL:{
                long _id=db.insert(SummaryContract.Total.TABLE_NAME,null,values);
                if(_id>0)
                    returnUri=SummaryContract.Total.buildTotalUri(_id);//build the content Uri follow by ID
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        /*Implement this to handle requests to delete one or more rows.
        The implementation should apply the selection clause when performing deletion, allowing the operation to affect multiple rows in a directory.
        As a courtesy, call notifyChange() after deleting. This method can be called from multiple threads, as described in Processes and Threads.

        The implementation is responsible for parsing out a row ID at the end of the URI, if a specific row is being deleted.
        That is, the client would pass in content://contacts/people/22 and the implementation is responsible for parsing the record number (22) when creating a SQL statement.

        Parameters
        uri	The full URI to query, including a row ID (if a specific record is requested).
        selection	An optional restriction to apply to rows when deleting.
        Returns
        The number of rows affected.*/
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case HISTORY:
                rowsDeleted = db.delete(
                        SummaryContract.UsrHistory.TABLE_NAME, selection, selectionArgs);
                break;
            case RUBRIC:
                rowsDeleted = db.delete(
                        SummaryContract.Rubric.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case HISTORY: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        SummaryContract.UsrHistory.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case RUBRIC: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        SummaryContract.Rubric.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TOTAL: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        SummaryContract.Total.TABLE_NAME, // a. table
                        projection,// b. column names
                        selection,// c. selections
                        selectionArgs,// d. selections args
                        null,// e. group by
                        null,// f. having
                        sortOrder// g. order by
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }



    public static long insertRubric(Context contentResolver, String description,float weight ,float popularity){

        ContentValues rubric = new ContentValues();
        rubric.put(SummaryContract.Rubric.NAME, description);
        rubric.put(SummaryContract.Rubric.WEIGHT, weight);
        rubric.put(SummaryContract.Rubric.POPULARITY, popularity);
        Uri insertedUri =  contentResolver.getContentResolver().insert(
                SummaryContract.Rubric.CONTENT_URI,
                rubric
        );
        long locationId = ContentUris.parseId(insertedUri);
        return locationId;
    }

    public static long insertRubric(Context contentResolver, String description,float weight){
        return insertRubric(contentResolver, description, weight ,0F);
    }


    public static int getCountAll(){
        //String countQuery = "SELECT * FROM " + SummaryContract.UsrHistory.TABLE_NAME;
        String countQuery = "SELECT * FROM " + SummaryContract.Total.TABLE_NAME;
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();
        return cursor.getCount();
    }

    public static void insertTotal(Context mContext,float weight ){



        if (getCountAll() ==0){
            //initial the table
            ContentValues values = new ContentValues();
            values.put(SummaryContract.Total.NAME, "Kevin");
            values.put(SummaryContract.Total.P_IN_Total, 0F);
            values.put(SummaryContract.Total.N_IN_Total, 0F);
            mContext.getContentResolver().insert(SummaryContract.Total.CONTENT_URI,values);
        }

        // TODO: 2/28/2016 If weight > 0 then get the pTotal and add the weight on it if < 0 add to nTotal
        //get the total table
        Cursor c = mContext.getContentResolver().query(SummaryContract.Total.CONTENT_URI, RecyclerListAdapter.TOTAL_COLUMNS, null, null, null);
        c.moveToPosition(0);
        String name =   c.getString(RecyclerListAdapter.COL_TOTAL_NAME);
        Float pPoint =   c.getFloat(RecyclerListAdapter.COL_TOTAL_P_TOTAL);
        Float nPoint =   c.getFloat(RecyclerListAdapter.COL_TOTAL_N_TOTAL);

        Toast.makeText(mContext, name + " " + pPoint.toString() + "  " + nPoint.toString(), Toast.LENGTH_LONG).show();
        ContentValues historyValue = new ContentValues();
        if (weight>=0) {
            pPoint +=weight;
            historyValue.put(SummaryContract.UsrHistory.P_History, weight);
        }
        else {
            nPoint +=weight;
            historyValue.put(SummaryContract.UsrHistory.N_History,weight);
        }
        //upgrade the history table
        mContext.getContentResolver().insert(SummaryContract.UsrHistory.CONTENT_URI,historyValue);
        //update the total table
        ContentValues v = new ContentValues();
        v.put(SummaryContract.Total.NAME,name);
        v.put(SummaryContract.Total.P_IN_Total,pPoint);
        v.put(SummaryContract.Total.N_IN_Total, nPoint);
        mContext.getContentResolver().update(SummaryContract.Total.CONTENT_URI,v,null,null);
    }

    public static String[] getTotal(Context mContext){

        //get the total table
        Cursor c = mContext.getContentResolver().query(SummaryContract.Total.CONTENT_URI, RecyclerListAdapter.TOTAL_COLUMNS, null, null, null);
        c.moveToPosition(0);
        String name =   c.getString(RecyclerListAdapter.COL_TOTAL_NAME);
        Float pPoint =   c.getFloat(RecyclerListAdapter.COL_TOTAL_P_TOTAL);
        Float nPoint =   c.getFloat(RecyclerListAdapter.COL_TOTAL_N_TOTAL);
        String pointInTotal[] = {pPoint.toString(),nPoint.toString()};
        return pointInTotal;
    }
    public static String getPPoint(Context mContext){
        String []pPoint = getTotal(mContext);
        return pPoint[0];
    }
    public static String getNPoint(Context mContext){
        String []pPoint = getTotal(mContext);
        return pPoint[1];
    }









}
