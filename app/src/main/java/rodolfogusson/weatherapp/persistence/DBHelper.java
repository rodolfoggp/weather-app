package rodolfogusson.weatherapp.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import rodolfogusson.weatherapp.model.CityWeather;
import rodolfogusson.weatherapp.model.Weather;

import static android.provider.BaseColumns._ID;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.CITIES_TABLE;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.CityWeatherColumns.CITY;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.CityWeatherColumns.COUNTRY;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WEATHERS_TABLE;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.DATE;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.LOCATION_ID;

/**
 * Created by rodolfo on 5/19/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db.db";
    private static final int DATABASE_VERSION = 3;
    private static final String FOREIGN_KEYS_ON = "PRAGMA foreign_keys=ON;";

    private static DBHelper instance;
    private SQLiteDatabase db;

    private DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public static DBHelper getInstance(Context context){
        if (instance == null){
            instance = new DBHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FOREIGN_KEYS_ON);
        db.execSQL("CREATE TABLE " +
                CITIES_TABLE +
                "(" +
                "_id integer primary key autoincrement not null," +
                "latitude real," +
                "longitude real," +
                "country text," +
                "city text" +
                ");");
        db.execSQL("CREATE TABLE " +
                WEATHERS_TABLE +
                "(" +
                "_id integer primary key autoincrement not null," +
                "location_id integer," +
                "date text," +
                "weather_id integer," +
                "humidity integer," +
                "pressure real," +
                "description text," +
                "condition text," +
                "icon_code text," +
                "temp_now real," +
                "min_temp real," +
                "max_temp real," +
                "morning real," +
                "day real," +
                "evening real," +
                "night real," +
                "icon blob," +
                "foreign key(location_id) references " +
                CITIES_TABLE + "(_id)" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cleanDB();
        onCreate(db);
    }

    public CityWeather findCityWeather(String cityAndCountry){
        String[] parts = cityAndCountry.split(", ");
        String city = parts[0];
        String country = parts[1];
        db = getReadableDatabase();
        String query = "SELECT * FROM " + CITIES_TABLE +
                " WHERE " + COUNTRY + "=? " +
                "AND " + CITY + "=?";
        List<CityWeather> list = new ArrayList<>();
        db.beginTransaction();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{country, city});
            if(cursor != null && cursor.getCount() > 0){
                while (cursor.moveToNext()) {
                    CityWeatherDeserializer deserializer = new CityWeatherDeserializer();
                    CityWeather cityWeather = deserializer.deserialize(cursor, this);
                    list.add(cityWeather);
                }
            }
            db.setTransactionSuccessful();
        }catch(Exception e){
            Log.d("EXCEPTION: ",e.getMessage());
        }finally{
            closeCursor(cursor);
            db.endTransaction();
        }
        if(!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    List<Weather> findWeathersFor(long locationID){
        db = getReadableDatabase();
        String query = "SELECT * FROM " + WEATHERS_TABLE
                + " WHERE " + LOCATION_ID + "=?";
        List<Weather> list = new ArrayList<>();
        db.beginTransaction();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery(query, new String[]{String.valueOf(locationID)});
            while(cursor.moveToNext()){
                WeatherDeserializer deserializer = new WeatherDeserializer();
                Weather weather = deserializer.deserialize(cursor);
                list.add(weather);
            }
            db.setTransactionSuccessful();
        }finally{
            closeCursor(cursor);
            db.endTransaction();
        }
        return list;
    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }


    public void save(CityWeather cityWeather){
        ContentValues values = CityWeatherSerializer.serialize(cityWeather);
        boolean operationWasSuccessful;
        db = getWritableDatabase();
        db.beginTransaction();
        try{
            CityWeather cityWeatherOnDB = findCityWeather(
                            cityWeather.getLocation().getCity() +
                            ", " + cityWeather.getLocation().getCountry());
            if(cityWeatherOnDB != null){ //if there is already data about this city on DB:
                long id = cityWeatherOnDB.getId();
                String query = "DELETE FROM " + WEATHERS_TABLE + " WHERE " + LOCATION_ID + " =?";
                //Cleaning all old weather data about the city in question:
                db.execSQL(query, new String[]{String.valueOf(id)});
                //int result = db.delete(WEATHERS_TABLE, LOCATION_ID + " = ?", new String[] { Long.toString(id) });
                int numberOfRowsAffected = db.update(CITIES_TABLE, values, _ID+"="+id,null);
                if(numberOfRowsAffected != -1){
                    operationWasSuccessful = saveWeathers(cityWeather, id);
                    if (operationWasSuccessful) {
                        db.setTransactionSuccessful();
                    }
                }
            }else{
                long row = db.insert(CITIES_TABLE,null,values);
                if (row != -1) { //meaning the previous insertion was successful
                    operationWasSuccessful = saveWeathers(cityWeather, row);
                    if (operationWasSuccessful) {
                        db.setTransactionSuccessful();
                    }
                }
            }
        }finally{
            db.endTransaction();
        }
    }

    private boolean saveWeathers(CityWeather cityWeather, long row){
        boolean operationWasSuccessful = true;
        for (Weather weather : cityWeather.getWeatherList()) {
            ContentValues subValues = WeatherSerializer.serialize(weather, row);
            long subRow = db.insert(WEATHERS_TABLE, null, subValues);
            if (subRow == -1) {
                operationWasSuccessful = false;
                break;
            }
        }
        return operationWasSuccessful;
    }

    public void removeExpiredWeathers(){
        db = getWritableDatabase();
        String today = new LocalDate().toString();
        String query = "DELETE FROM " + WEATHERS_TABLE + " WHERE "
                + DATE + " < '" + today + "';";
        db.beginTransaction();
        try{
            db.execSQL(query);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void cleanDB(){
        db = getReadableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("DROP TABLE IF EXISTS " + CITIES_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + WEATHERS_TABLE);
            onCreate(db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }


}
