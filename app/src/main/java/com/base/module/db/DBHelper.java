package com.base.module.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;


/***
 *
 * @ClassName: DBHelper
 * @Description: 数据库操作
 * @author huangyc
 * @date 2014-11-4 上午10:05:31
 *
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper mDBHelper;

    /**
     * 写操作
     **/
    private SQLiteDatabase w_db;
    /**
     * 读操作
     ***/
    private SQLiteDatabase r_db;

    /**
     * 保存数据库实例
     ***/
    private Vector<Class<?>> mEntityClass = new Vector<>();

    private DBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private DBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    /**
     * @param @param context 参数
     * @return void 返回类型
     * @Title: createInstance
     * @Description: 创建实例
     * @author huangyc
     * @date 2014-11-4 上午10:03:24
     */
    public static void createInstance(Context context) {
        createInstance(context, context.getPackageName());
    }

    /**
     * @param @param context
     * @param @param name 参数
     * @return void 返回类型
     * @throws
     * @Title: createInstance
     * @Description: 创建实例
     * @author huangyc
     * @date 2014-11-4 上午10:03:42
     */
    public static void createInstance(Context context, String name) {
        createInstance(context, name, 1);
    }

    /**
     * @param @param context
     * @param @param name
     * @param @param version 参数
     * @return void 返回类型
     * @Title: createInstance
     * @Description: 创建实例
     * @author huangyc
     * @date 2014-11-4 上午10:03:53
     */
    public static void createInstance(Context context, String name, int version) {
        mDBHelper = new DBHelper(context, name, version);
    }

    /**
     * @param @return 参数
     * @return DBHelper 返回类型
     * @Title: getInstance
     * @Description: 获取实例
     * @author huangyc
     * @date 2014-11-4 上午10:04:10
     */
    public static DBHelper getInstance() {
        return mDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        creatTables(db);
    }

    private AtomicLong num = new AtomicLong(0);

    /**
     * @return void 返回类型
     * @Title: closeDB
     * @Description: 关闭数据库
     * @author huangyc
     * @date 2014-11-4 上午10:08:23
     */
    public void closeDB() {
        if (num.addAndGet(-1) > 0) {
            return;
        }
        num.set(0);
        try {
            if (w_db != null && w_db.isOpen()) {
                w_db.close();
            }
            if (r_db != null && r_db.isOpen()) {
                r_db.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * @return void 返回类型
     * @Title: openDB
     * @Description: 打开数据库
     * @author huangyc
     * @date 2014-11-4 上午10:10:14
     */
    public void openDB() {
        if (num.addAndGet(1) > 1) {
            return;
        }
        try {
            if (w_db == null || !w_db.isOpen()) {
                w_db = getWritableDatabase();
            }
            if (r_db == null || !r_db.isOpen()) {
                r_db = getReadableDatabase();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /***
     *
     * @Title: addEntityCalss
     * @Description: 添加实体类
     * @param @param classz 参数
     * @return void 返回类型
     * @author huangyc
     * @date 2014-11-4 上午10:11:42
     */
    public void addEntityClass(Class<?> classz) {
        if (mEntityClass.contains(classz)) {
            return;
        }
        mEntityClass.add(classz);
    }

    /***
     *
     * @Title: removeEntityClass
     * @Description: 移除数据库实体
     * @param @param classz 参数
     * @return void 返回类型
     * @author huangyc
     * @date 2014-11-4 上午10:12:59
     */
    public void removeEntityClass(Class<?> classz) {
        if (!mEntityClass.contains(classz)) {
            return;
        }
        mEntityClass.remove(classz);
    }


    /***
     * 执行查询SQL语句
     * @param sql
     * @param params
     * @return
     */
    public JSONArray execQuerySQL(String sql, String params[]) {
        JSONArray ret = new JSONArray();
        try {
            Cursor c = r_db.rawQuery(sql, params);
            while (c.moveToNext()) {
                JSONObject obj = new JSONObject();
                for (String columnName : c.getColumnNames()) {
                    try {
                        obj.put(columnName, c.getString(c.getColumnIndex(columnName)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (obj.keys().hasNext()) {
                    ret.put(obj);
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return ret;
    }


    /***
     * 执行SQL语句
     * @param sql
     * @param params
     */
    public boolean execSql(String sql, String[] params) {
        try {
            w_db.execSQL(sql, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param @return 参数
     * @return List<String> 返回类型
     * @throws
     * @Title: getCreateTableSQL
     * @Description: 获取创建表数据
     * @author huangyc
     * @date 2014-11-4 上午10:17:07
     */
//    private List<String> getCreateTableSQL() {
//        List<String> list = new ArrayList<String>();
//        Iterator<Class<?>> entitys = mEntityClass.iterator();
//        while (entitys.hasNext()) {
//            Class<?> entity = entitys.next();
//            HDEntity e = entity.getAnnotation(HDEntity.class);
//            if (e == null) {
//                continue;
//            }
//            String SQL = "create table if not exists  ";
//            String tableName = e.tableName();
//            if (CommonUtil.checkNull(tableName)) {
//                tableName = entity.getSimpleName();
//            }
//            Field fields[] = entity.getDeclaredFields();
//            StringBuffer sqlBuff = new StringBuffer();
//            for (Field f : fields) {
//                String colName = getColName(f);
//                Class<?> field_type = f.getType();
//                String type = "";
//                if (field_type.equals(Byte.class) || field_type.equals(byte.class)) {
//                    type = "byte";
//                } else if (field_type.equals(String.class) || field_type.equals(Character.class)) {
//                    type = "varchar";
//                } else if (field_type.equals(Integer.class) || field_type.equals(int.class) || field_type.equals(Short.class) || field_type.equals(short.class) || field_type.equals(Long.class) || field_type.equals(long.class)) {
//                    type = "integer";
//                } else if (field_type.equals(Date.class)) {
//                    type = "varchar(32)";
//                } else if (field_type.equals(Timestamp.class)) {
//                    type = "integer";
//                } else if (field_type.equals(Float.class) || field_type.equals(float.class)) {
//                    type = "float";
//                } else if (field_type.equals(Double.class) || field_type.equals(double.class)) {
//                    type = "double";
//                }
//                String colmnSql = colName + " " + type;
//                HDMaxLength maxlength = f.getAnnotation(HDMaxLength.class);
//                if (maxlength != null) {
//                    if (maxlength.value() >= 512) {
//                        type = "text";
//                        colmnSql = colName + " " + type;
//                    } else {
//                        colmnSql += "(" + maxlength.value() + ")";
//                    }
//                }
//                HDNotNull notNull = f.getAnnotation(HDNotNull.class);
//                if (notNull != null) {
//                    colmnSql += " not null ";
//                }
//                HDId id = f.getAnnotation(HDId.class);
//                if (id != null) {
//                    colmnSql += "  primary key ";
//                    if (id.auoIncreateMent() && type.equals("integer") && (!field_type.equals(Timestamp.class))) {
//                        colmnSql += " autoincrement ";
//                    }
//                    sqlBuff.insert(0, colmnSql + " , ");
//                } else {
//                    sqlBuff.append(colmnSql + " , ");
//                }
//
//            }
//            String colmnsqls = sqlBuff.toString().trim();
//            SQL += " " + tableName + " ( " + colmnsqls.substring(0, colmnsqls.length() - 1) + " )";
//            list.add(SQL);
//        }
//        return list;
//    }

    /***
     *
     * @Title: creatTables
     * @Description: 创建表
     * @param @param db 参数
     * @return void 返回类型
     * @author huangyc
     * @date 2014-11-4 上午10:04:35
     */
//    private void creatTables(SQLiteDatabase db) {
//        try {
//            List<String> sqlList = getCreateTableSQL();
//            db.beginTransaction();
//            for (String sql : sqlList) {
//                db.execSQL(sql);
//            }
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//        }
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
//            creatTables(db);
        }
    }

    public void beginTransaction() {
        w_db.beginTransaction();
    }

    public void commitTransaction() {
        w_db.setTransactionSuccessful();
        w_db.endTransaction();
    }

    /***
     *
     * @Title: insert
     * @Description: 添加数据
     * @param @param entity
     * @param @return 参数
     * @return long 返回ID,如果-1标识没有添加成功
     * @author huangyc
     * @date 2014-11-4 上午10:23:56
     */
//    public long insert(Object entity) {
//        try {
//            String tableName = getTableName(entity.getClass());
//            ContentValues cv = getContenValuesFormEntity(entity);
//            return w_db.insert(tableName, null, cv);
//        } catch (Throwable e) {
//            return -1;
//        }
//    }

    /**
     * @param @param  object
     * @param @param  where
     * @param @param  whereArgs
     * @param @return 参数
     * @return int 如果-1标识操作失败
     * @Title: delete
     * @Description: 删除数据
     * @author huangyc
     * @date 2014-11-4 上午10:25:43
     */
//    public int delete(Class<?> object, String where, String[] whereArgs) {
//        try {
//            String tableName = getTableName(object);
//            return w_db.delete(tableName, where, whereArgs);
//        } catch (Throwable e) {
//            return -1;
//        }
//    }

    /**
     * @param @param  object
     * @param @param  id
     * @param @return 参数
     * @return int 如果-1标识操作失败
     * @Title: deleteById
     * @Description: 通过主键ID删除数据
     * @author huangyc
     * @date 2014-11-4 上午10:28:59
     */
//    public int deleteById(Class<?> object, Object id) {
//        try {
//            String tableName = getTableName(object);
//            String idName = findEntityIDName(object);
//            return w_db.delete(tableName, idName + "=?", new String[]{String.valueOf(id)});
//        } catch (Throwable e) {
//            return -1;
//        }
//    }

    /**
     * @param @param  table
     * @param @return 参数
     * @return boolean 返回类型
     * @Title: dropTable
     * @Description: 删除某张表
     * @author huangyc
     * @date 2014-11-4 上午10:30:59
     */
//    public boolean dropTable(Class<?> table) {
//        try {
//            String v_tableName = getTableName(table);
//            String SQL = "drop table " + v_tableName;
//            w_db.execSQL(SQL);
//            return true;
//        } catch (Throwable e) {
//            return false;
//        }
//    }

    /**
     * @return void 返回类型
     * @Title: dropAllTable
     * @Description: 删除所有表
     * @author huangyc
     * @date 2014-11-4 上午10:31:42
     */
    public void dropAllTable() {
        Iterator<Class<?>> entitys = mEntityClass.iterator();
        while (entitys.hasNext()) {
//            dropTable(entitys.next());
        }
    }

    /**
     * @param @param  objectClass
     * @param @param  id
     * @param @return 参数
     * @return List<T> 返回类型
     * @Title: query
     * @Description: 查询数据
     * @author huangyc
     * @date 2014-11-4 上午10:37:30
     */
//    public <T> List<T> query(Class<T> objectClass, Object id) {
//        List<T> list = new ArrayList<T>();
//        String tableName = getTableName(objectClass);
//        String idSql = null;
//        ArrayList<String> cols = new ArrayList<String>();
//        Field fs[] = objectClass.getDeclaredFields();
//        for (Field f : fs) {
//            String colName = getColName(f);
//            HDId col_id = f.getAnnotation(HDId.class);
//            if (col_id != null) {
//                idSql = colName;
//            }
//            cols.add(colName);
//        }
//        Cursor c;
//        if (id != null) {
//            c = r_db.query(tableName, cols.toArray(new String[]{}), idSql + "=?", new String[]{String.valueOf(id)}, null, null, idSql);
//        } else {
//            c = r_db.query(tableName, cols.toArray(new String[]{}), null, null, null, null, idSql);
//        }
//        if (c == null) {
//            return list;
//        }
//        c.moveToFirst();
//        try {
//            while (!c.isAfterLast()) {
//                String culsName[] = c.getColumnNames();
//                T v_entity = objectClass.newInstance();
//                for (String col : culsName) {
//                    setFieldValue(v_entity, col, c);
//                }
//                list.add(v_entity);
//                c.moveToNext();
//            }
//        } catch (Throwable ee) {
//        } finally {
//            if (c != null) {
//                c.close();
//            }
//        }
//        return list;
//    }

    /**
     * @param @param  objectClass
     * @param @param  id
     * @param @return 参数
     * @return T 返回类型
     * @Title: queryOne
     * @Description: 查询数据，只返回一条
     * @author huangyc
     * @date 2014-11-4 上午10:37:43
     */
//    public <T> T queryOne(Class<T> objectClass, Object id) {
//        List<T> v_list = query(objectClass, id);
//        if (v_list.size() > 0) {
//            return v_list.get(0);
//        }
//        return null;
//    }

    /***
     *
     * @Title: query
     * @Description: 分页查询数据库
     * @param @param objectClass
     * @param @param orderBy
     * @param @param pageIndex
     * @param @param pageSize
     * @param @return 参数
     * @return List<T> 返回类型
     * @throws
     * @author huangyc
     * @date 2014-11-4 上午10:39:54
     */
//    public <T> List<T> query(Class<T> objectClass, String orderBy, int pageIndex, int pageSize) {
//        List<T> list = new ArrayList<T>();
//        String tableName = getTableName(objectClass);
//        int offset = (pageIndex - 1) * pageSize;
//        if (offset < 0) {
//            offset = 0;
//        }
//        Cursor c = null;
//        try {
//            c = r_db.rawQuery("select * from " + tableName + orderBy + "  limit " + pageSize + " offsize " + offset, null);
//            if (c == null) {
//                return list;
//            }
//            c.moveToFirst();
//            while (!c.isAfterLast()) {
//                String culsName[] = c.getColumnNames();
//                T v_entity = objectClass.newInstance();
//                for (String col : culsName) {
//                    setFieldValue(v_entity, col, c);
//                }
//                list.add(v_entity);
//                c.moveToNext();
//            }
//        } catch (Throwable ee) {
//        } finally {
//            if (c != null) {
//                c.close();
//            }
//        }
//        return list;
//    }

    /***
     *
     * @Title: query
     * @Description: 查询数据库
     * @param @param objectClass
     * @param @param where
     * @param @param params
     * @param @param groupBy
     * @param @param having
     * @param @param orderBy
     * @param @param pageIndex
     * @param @param pageSize
     * @param @return 参数
     * @return List<T> 返回类型
     * @author huangyc
     * @date 2014-11-4 上午10:41:47
     */
//    public <T> List<T> query(Class<T> objectClass, String where, String[] params, String groupBy, String having, String orderBy, Integer pageIndex, Integer pageSize) {
//        Integer offset = null;
//        if (pageIndex != null && pageSize != null) {
//            offset = (pageIndex - 1) * pageSize;
//            offset = offset > 0 ? offset : 0;
//        }
//        List<T> list = new ArrayList<T>();
//        String tableName = getTableName(objectClass);
//        ArrayList<String> cols = getEntityColumns(objectClass);
//        Cursor c = null;
//        try {
//            String v_limit = "";
//            if (offset != null) {
//                v_limit += "" + offset + ",";
//            }
//            if (pageSize != null) {
//                v_limit += "" + pageSize;
//            }
//            if (CommonUtil.checkNull(v_limit)) {
//                v_limit = null;
//            }
//            c = r_db.query(tableName, cols.toArray(new String[]{}), where, params, groupBy, having, orderBy, v_limit);
//            if (c == null) {
//                return list;
//            }
//            c.moveToFirst();
//            while (!c.isAfterLast()) {
//                String culsName[] = c.getColumnNames();
//                T v_entity = objectClass.newInstance();
//                for (String col : culsName) {
//                    setFieldValue(v_entity, col, c);
//                }
//                list.add(v_entity);
//                c.moveToNext();
//            }
//        } catch (Throwable ee) {
//
//        }
//        return list;
//    }

    /**
     * @param @param  object
     * @param @param  where
     * @param @param  whereArgs
     * @param @return 参数
     * @return int 如果-1标识没有操作成功
     * @Title: update
     * @Description: 更新数据库
     * @author huangyc
     * @date 2014-11-4 上午10:43:25
     */
//    public <T> int update(T object, String where, String[] whereArgs) {
//        try {
//            String tableName = getTableName(object.getClass());
//            ContentValues cv = getContenValuesFormEntity(object);
//            return w_db.update(tableName, cv, where, whereArgs);
//        } catch (Throwable e) {
//            return -1;
//        }
//    }

    /**
     * @param @param  object
     * @param @param  IdParams
     * @param @return 参数
     * @return int 如果-1标识没有操作成功
     * @Title: updateById
     * @Description: 通过ID更新数据库
     * @author huangyc
     * @date 2014-11-4 上午10:44:23
     */
//    public <T> int updateById(T object, Object IdParams) {
//        try {
//            String tableName = getTableName(object.getClass());
//            String idName = findEntityIDName(object.getClass());
//            ContentValues cv = getContenValuesFormEntity(object);
//            cv.remove(idName);
//            return w_db.update(tableName, cv, idName + "=?", new String[]{String.valueOf(IdParams)});
//        } catch (Throwable e) {
//            return -1;
//        }
//    }

    /**
     * @param @param  entity
     * @param @param  id
     * @param @return 参数
     * @return long 返回类型
     * @Title: insertOrUpdateById
     * @Description: 根据主键更新或者添加
     * @author huangyc
     * @date 2014-11-4 上午10:46:12
     */
//    public long insertOrUpdateById(Object entity, Object id) {
//        List<?> v_list = query(entity.getClass(), id);
//        if (v_list == null || v_list.size() == 0) {
//            return insert(entity);
//        } else {
//            return updateById(entity, id);
//        }
//    }

    /***
     *
     * @Title: getEntityColumns
     * @Description: 获取表字段
     * @param @param objectClass
     * @param @return 参数
     * @return ArrayList<String> 返回类型
     * @author huangyc
     * @date 2014-11-4 上午10:41:06
     */
//    private ArrayList<String> getEntityColumns(Class<?> objectClass) {
//        ArrayList<String> cols = new ArrayList<String>();
//        Field fs[] = objectClass.getDeclaredFields();
//        for (Field f : fs) {
//            HDColumn column = f.getAnnotation(HDColumn.class);
//            String colName = "";
//            if (column == null || CommonUtil.checkNull(column.name())) {
//                colName = f.getName();
//            } else {
//                colName = column.name();
//            }
//            cols.add(colName);
//        }
//        return cols;
//    }

    /***
     *
     * @Title: setFieldValue
     * @Description: 将查询数据库的值反射到实体中
     * @param @param obj
     * @param @param name
     * @param @param cursor
     * @param @throws IllegalArgumentException
     * @param @throws IllegalAccessException 参数
     * @return void 返回类型
     * @author huangyc
     * @date 2014-11-4 上午10:38:27
     */
//    private void setFieldValue(Object obj, String name, Cursor cursor) throws IllegalArgumentException, IllegalAccessException {
//        Field fs[] = obj.getClass().getDeclaredFields();
//        Field col_field = null;
//        for (Field f : fs) {
//            String fieldName = getColName(f);
//            if (name.equals(fieldName)) {
//                col_field = f;
//                break;
//            }
//        }
//        if (col_field != null) {
//            int id = cursor.getColumnIndex(name);
//            Class<?> type = col_field.getType();
//            col_field.setAccessible(true);
//            if (type.equals(String.class) && cursor.getString(id) != null) {
//                col_field.set(obj, cursor.getString(id));
//            } else if ((type.equals(byte.class) || type.equals(Byte.class)) && cursor.getBlob(id) != null) {
//                col_field.set(obj, cursor.getBlob(id)[0]);
//            } else if (type.isArray() && cursor.getBlob(id) != null && (type.equals(byte.class) || type.equals(Byte.class))) {
//                col_field.set(obj, cursor.getBlob(id));
//            } else if ((type.equals(short.class) || type.equals(Short.class))) {
//                col_field.set(obj, cursor.getShort(id));
//            } else if (type.equals(Integer.class) || type.equals(int.class)) {
//                col_field.set(obj, cursor.getInt(id));
//            } else if (type.equals(Float.class) || type.equals(Float.class)) {
//                col_field.set(obj, cursor.getFloat(id));
//            } else if (type.equals(Double.class) || type.equals(double.class)) {
//                col_field.set(obj, cursor.getDouble(id));
//            } else if (type.equals(Timestamp.class)) {
//                col_field.set(obj, new Timestamp(cursor.getLong(id)));
//            } else if (type.equals(Date.class)) {
//                col_field.set(obj, new Date(cursor.getString(id)));
//            } else if (type.equals(Long.class) || type.equals(long.class)) {
//                col_field.set(obj, cursor.getLong(id));
//            }
//        }
//    }

    /***
     *
     * @Title: findEntityIDField
     * @Description: 查找主键名称
     * @param @param object
     * @param @return
     * @param @throws DBException 参数
     * @return String 返回类型
     * @throws
     * @author huangyc
     * @date 2014-11-4 上午10:27:29
     */
//    private String findEntityIDName(Class<?> object) {
//        Field fs[] = object.getDeclaredFields();
//        String idName = null;
//        for (Field f : fs) {
//            HDId an_id = f.getAnnotation(HDId.class);
//            if (an_id == null) {
//                continue;
//            }
//            HDColumn column = f.getAnnotation(HDColumn.class);
//            if (column == null || CommonUtil.checkNull(column.name())) {
//                idName = f.getName();
//            } else {
//                idName = column.name();
//            }
//        }
//        if (idName == null) {
//
//        }
//        return idName;
//    }

    /**
     * @param @param  entity
     * @param @return
     * @param @throws DBException 参数
     * @return String 返回类型
     * @throws
     * @Title: getTableName
     * @Description: 获取表名称
     * @author huangyc
     * @date 2014-11-4 上午10:20:13
     */
//    private String getTableName(Class<?> entity) {
//        HDEntity e = entity.getAnnotation(HDEntity.class);
//        if (e == null) {
//            DefalutLogger.getInstance().OnError("不是实体类...");
//            return null;
//        }
//        String tableName = e.tableName();
//        if (CommonUtil.checkNull(tableName)) {
//            tableName = entity.getSimpleName();
//        }
//        return tableName;
//    }

    /***
     *
     * @Title: getContenValuesFormEntity
     * @Description: 将实体转换为ContenValues
     * @param @param entity
     * @param @return 参数
     * @return ContentValues 返回类型
     * @throws
     * @author huangyc
     * @date 2014-11-4 上午10:26:51
     */
//    private ContentValues getContenValuesFormEntity(Object entity) {
//        ContentValues cv = new ContentValues();
//        Field fileds[] = entity.getClass().getDeclaredFields();
//        try {
//            for (Field f : fileds) {
//                Class<?> v_class = f.getType();
//                f.setAccessible(true);
//                String colName = getColName(f);
//                if (f.get(entity) == null) {
//                    continue;
//                }
//                if (v_class.equals(Byte.class) || v_class.equals(byte.class)) {
//                    cv.put(colName, f.getByte(entity));
//                } else if (v_class.equals(Timestamp.class)) {
//                    cv.put(colName, ((Timestamp) f.get(entity)).getTime());
//                } else if (v_class.equals(Date.class)) {
//                    cv.put(colName, ((Date) f.get(entity)).toString());
//                } else {
//                    cv.put(colName, f.get(entity).toString());
//                }
//            }
//        } catch (Throwable e) {
//            return null;
//        }
//        return cv;
//    }

    /**
     * @param @param  f
     * @param @return 参数
     * @return String 返回类型
     * @throws
     * @Title: getColName
     * @Description: 获取字段名称
     * @author huangyc
     * @date 2014-11-4 上午10:15:41
     */
//    private String getColName(Field f) {
//        String v_colName = f.getName();
//        HDColumn v_col = f.getAnnotation(HDColumn.class);
//        if (v_col != null && v_col.name().trim().length() > 0) {
//            v_colName = v_col.name();
//        }
//        return v_colName;
//    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		super.onDowngrade(db, oldVersion, newVersion);
    }
}
