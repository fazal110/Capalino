package com.example.fazal.capalino.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.fazal.capalino.Database.DatabaseBeen.SettingsModel;
import com.example.fazal.capalino.Database.DatabaseBeen.TrackingData;
import com.example.fazal.capalino.JavaBeen.ContentMasterModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by anas on 3/8/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.example.fazal.capalino/databases/";
    // Data Base Name.
    private static final String DATABASE_NAME = "CapalinoDataBase.sqlite";
    // Data Base Version.
    private static final int DATABASE_VERSION = 1;
    // Table Names of Data Base.

    public Context context;
    static SQLiteDatabase sqliteDataBase;
    private String query;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     * Parameters of super() are    1. Context
     *                              2. Data Base Name.
     *                              3. Cursor Factory.
     *                              4. Data Base Version.
     */
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null ,DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * By calling this method and empty database will be created into the default system path
     * of your application so we are gonna be able to overwrite that database with our database.
     * */
    public void createDataBase() throws IOException{
        //check if the database exists
        boolean databaseExist = checkDataBase();

        if(databaseExist){
            // Do Nothing.
        }else{
            this.getWritableDatabase();
            copyDataBase();
        }// end if else dbExist
    } // end createDataBase().

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase(){
        File databaseFile = new File(DB_PATH + DATABASE_NAME);
        return databaseFile.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transferring byte stream.
     * */
    private void copyDataBase() throws IOException{
        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        //transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * This method opens the data base connection.
     * First it create the path up till data base of the device.
     * Then create connection with data base.
     */
    public void openDataBase() throws SQLException{
        //Open the database
        String myPath = DB_PATH + DATABASE_NAME;
        sqliteDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * This Method is used to close the data base connection.
     */
    @Override
    public synchronized void close() {
        if(sqliteDataBase != null)
            sqliteDataBase.close();
        super.close();
    }

    /**
     * Apply your methods and class to fetch data using raw or queries on data base using
     * following demo example code as:
     */
    public Cursor getDataFromDB(String colname,String colvariable,String tablename,boolean iscon){
        if(iscon==true){
            query = "select * From "+tablename+" where "+colname+" = '"+colvariable+"'";
        }else {
            query = "select * From " + tablename;
        }
        Cursor cursor = sqliteDataBase.rawQuery(query, null);

        return cursor;
    }

    public Cursor getDataFromDB1(String colname,String colvariable,String tablename,boolean iscon){
        if(iscon==true){
            query = "select * From "+tablename+" where "+colname+" LIKE  %"+colvariable+"%";
        }else {
            query = "select * From " + tablename;
        }
        Cursor cursor = sqliteDataBase.rawQuery(query, null);

        return cursor;
    }

    public boolean UpdateContentMasterLastUpdateDate(String date,int contentid){
       try{
           query = "update ContentMaster set LastUpdate = '"+date+"' where ContentID = "+contentid;
           Cursor cursor = sqliteDataBase.rawQuery(query,null);
           cursor.moveToFirst();
           cursor.close();
           return true;
           }catch (Exception e){
               e.printStackTrace();
           return false;
           }
    }

    public Cursor getDataFromProcurementMaster(String tagtitle){
            query = "select * From ProcurementMaster join ContractValueTags on ProcurementMaster.ProcurementContractValueID = ContractValueTags.ID where ContractValueTags.TagTitle='"+tagtitle+"'";
        Cursor cursor = sqliteDataBase.rawQuery(query, null);
        return cursor;
    }

    //insert Data
    public boolean InsertUserProcurmentTracking(TrackingData been){
        try {
            query = "insert into TrackListing (ProcurementTitle,AgencyTitle,TrackDate,ProposalDeadLine,UserID,Rating) " +
                    "Values('"+been.getProcurementTitle()+"','"+been.getAgencyTitle()+"','"+been.getTrackDate()+"'," +
                    "'"+been.getProposalDeadLine()+"','"+been.getUserID()+"','"+been.getRating()+"')";
            sqliteDataBase.execSQL(query);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean InsertSettingsMaster(SettingsModel been){
        try {
            query = "Insert into SettingsMaster (GeographicCoverage,ContractValue,Certification,Capabilities) " +
                    "Values('"+been.getGeographicCoverage()+"','"+been.getContractValue()+"'," +
                    "'"+been.getCertification()+"','"+been.getCapabilities()+"')";
            sqliteDataBase.execSQL(query);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean InsertContentMaster(ContentMasterModel been){
        try {
            query = "insert or ignore into ContentMaster (ContentType,ContentTitle,ContentShortDescription,ContentLongDecsription," +
                    "ContentReferenceURL,ContentPostedByUser,ContentPostedDate,ContentStatusCode,ContentExpirationDate," +
                    "ContentRelevantDateTime,LASTUPDATE)" +
                    "Values('"+been.getContentType()+"','"+been.getContentTitle()+"','"+been.getContentShortDescription()+"'," +
                    "'"+been.getContentLongDescription()+"','"+been.getContentReferenceURL()+"','"+been.getContentPostedByUser()+"'," +
                    "'"+been.getContentPostedDate()+"','"+been.getContentStatusCode()+"','"+been.getContentExpirationDate()+"'," +
                    "'"+been.getContentRelevantDateTime()+"','"+been.getLASTUPDATE()+"')";


            sqliteDataBase.execSQL(query);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public void deleteRecord(int contentID){
        sqliteDataBase.execSQL("delete from ContentMaster where ContentID= "+contentID);
        sqliteDataBase.close();
    }

    public void delete(String tablename){
        sqliteDataBase.execSQL("delete from "+tablename);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // No need to write the create table query.
        // As we are using Pre built data base.
        // Which is ReadOnly.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No need to write the update table query.
        // As we are using Pre built data base.
        // Which is ReadOnly.
        // We should not update it as requirements of application.
    }
}
