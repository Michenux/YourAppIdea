package org.michenux.drodrolib.db.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import org.michenux.drodrolib.db.utils.SqlParser;
import org.michenux.drodrolib.resources.AssetUtils;

import java.io.IOException;

/**
 * @author Michenux
 *
 */
public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

	/**
	 * Sub path in assets directory for sql files
	 */
	private static final String SQL_DIR = "sql" ;
	
	/**
	 * Init sql file
	 */
	private static final String CREATEFILE = "create.sql";
	
	/**
	 * Upgrade Sql File prefix
	 */
	private static final String UPGRADEFILE_PREFIX = "upgrade-";
	
	/**
	 * Upgrade Sql File suffix
	 */
	private static final String UPGRADEFILE_SUFFIX = ".sql";
	
	/**
	 * Android context
	 */
	private Context context ;
	
	/**
	 * @param context androdi context
	 * @param name database name
	 * @param factory cursor factory
	 * @param version database version
	 */
	public SQLiteDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context ;
	}

	/** 
	 * {@inheritDoc}
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			execSqlFile( CREATEFILE, db );
		} catch( IOException exception ) {
			throw new RuntimeException("Database creation failed", exception );
		}
	}

	/**
	 * {@inheritDoc}
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			for( String sqlFile : AssetUtils.list(SQL_DIR, this.context.getAssets())) {
				if ( sqlFile.startsWith(UPGRADEFILE_PREFIX)) {
					int fileVersion = Integer.parseInt(sqlFile.substring( UPGRADEFILE_PREFIX.length(),  sqlFile.length() - UPGRADEFILE_SUFFIX.length())); 
					if ( fileVersion > oldVersion && fileVersion <= newVersion ) {
						execSqlFile( sqlFile, db );
					}
				}
			}
		} catch( IOException exception ) {
			throw new RuntimeException("Database upgrade failed", exception );
		}
	}
	
	/**
	 * @param sqlFile
	 * @param db
	 * @throws SQLException
	 * @throws IOException
	 */
	protected void execSqlFile(String sqlFile, SQLiteDatabase db ) throws SQLException, IOException {
		for( String sqlInstruction : SqlParser.parseSqlFile(SQL_DIR + "/" + sqlFile, this.context.getAssets())) {
			db.execSQL(sqlInstruction);
		}
	}
}
