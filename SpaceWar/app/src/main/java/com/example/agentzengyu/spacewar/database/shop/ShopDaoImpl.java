package com.example.agentzengyu.spacewar.database.shop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.agentzengyu.spacewar.application.Constant;
import com.example.agentzengyu.spacewar.entity.set.ShopLibrary;
import com.example.agentzengyu.spacewar.entity.single.ShopItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agent ZengYu on 2017/7/13.
 */

/**
 * 商品数据库调用接口
 */
public class ShopDaoImpl implements ShopDao {
    private static ShopDaoImpl instance = null;
    private ShopHelper helper = null;
    private SQLiteDatabase database = null;

    private ShopDaoImpl(Context context) {
        Log.e("ShopDaoImpl", "init");
        if (helper == null) {
            helper = new ShopHelper(context, Constant.Database.Shop.DBName, null, 1);
            database = helper.getWritableDatabase();
        }
    }

    public static ShopDaoImpl getInstance(Context context) {
        if (instance == null) {
            synchronized (ShopDaoImpl.class) {
                if (instance == null) {
                    instance = new ShopDaoImpl(context);
                }
            }
        } else {
            Log.e("ShopDaoImpl", "instance");
        }
        return instance;
    }

    @Override
    public void insert(@Constant.Database.Shop.TableName String tableName, ShopItem item) {
        ContentValues values = new ContentValues();
        values.put(Constant.Database.Shop.ColumnName.NAME, item.getName());
        values.put(Constant.Database.Shop.ColumnName.IMAGE, item.getImage());
        values.put(Constant.Database.Shop.ColumnName.VALUE, item.getValue());
        values.put(Constant.Database.Shop.ColumnName.LEVEL, item.getLevel());
        values.put(Constant.Database.Shop.ColumnName.PRICE, item.getPrice());
        database.insert(tableName, null, values);
    }

    @Override
    public void update(@Constant.Database.Shop.TableName String tableName, ShopItem item) {
        ContentValues values = new ContentValues();
        values.put(Constant.Database.Shop.ColumnName.NAME, item.getName());
        values.put(Constant.Database.Shop.ColumnName.IMAGE, item.getImage());
        values.put(Constant.Database.Shop.ColumnName.VALUE, item.getValue());
        values.put(Constant.Database.Shop.ColumnName.LEVEL, item.getLevel());
        values.put(Constant.Database.Shop.ColumnName.PRICE, item.getPrice());
        String[] whereArgs = new String[]{String.valueOf(item.getLevel())};
        database.update(tableName, values, Constant.Database.Shop.ColumnName.LEVEL + "=?", whereArgs);
    }

    @Override
    public void delete(@Constant.Database.Shop.TableName String tableName, ShopItem item) {
        String[] whereArgs = new String[]{String.valueOf(item.getName())};
        database.delete(tableName, Constant.Database.Shop.ColumnName.NAME + "=?", whereArgs);
    }

    @Override
    public ShopLibrary findAll() {
        ShopLibrary library = new ShopLibrary();
        if (library.setLives(findEachTable(Constant.Database.Shop.TableName.LIFE)) &&
                library.setDefenses(findEachTable(Constant.Database.Shop.TableName.DEFENSE)) &&
                library.setAgilities(findEachTable(Constant.Database.Shop.TableName.AGILITY)) &&
                library.setShields(findEachTable(Constant.Database.Shop.TableName.SHIELD)) &&
                library.setPowers(findEachTable(Constant.Database.Shop.TableName.POWER)) &&
                library.setSpeeds(findEachTable(Constant.Database.Shop.TableName.SPEED)) &&
                library.setRanges(findEachTable(Constant.Database.Shop.TableName.RANGE)) &&
                library.setLasers(findEachTable(Constant.Database.Shop.TableName.LASER))) {
            return library;
        }
        return null;
    }

    @Override
    public void close() {
        if (database != null) {
            database.close();
        }
    }

    /**
     * 查找每张表的数据
     *
     * @param tableName 表名
     * @return
     */
    private List<ShopItem> findEachTable(@Constant.Database.Shop.TableName String tableName) {
        List<ShopItem> items = null;
        Cursor cursor = database.query(tableName, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            items = new ArrayList<>();
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(Constant.Database.Shop.ColumnName.NAME));
                int image = cursor.getInt(cursor.getColumnIndex(Constant.Database.Shop.ColumnName.IMAGE));
                int value = cursor.getInt(cursor.getColumnIndex(Constant.Database.Shop.ColumnName.VALUE));
                int level = cursor.getInt(cursor.getColumnIndex(Constant.Database.Shop.ColumnName.LEVEL));
                int price = cursor.getInt(cursor.getColumnIndex(Constant.Database.Shop.ColumnName.PRICE));
                if (!"".equals(name) && image > 0 && value > 0 && level >= 0 && price > 0) {
                    ShopItem item = new ShopItem(name, image, value, level, price);
                    items.add(item);
                }
            }
        }
        return items;
    }
}