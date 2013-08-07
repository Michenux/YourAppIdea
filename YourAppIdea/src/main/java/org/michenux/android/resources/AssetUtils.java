package org.michenux.android.resources;

import java.io.IOException;
import java.util.Arrays;

import android.content.res.AssetManager;

public class AssetUtils {

	/**
	 * @param filename searched file name
	 * @param path subpath from the asset directory
	 * @param assetManager assetManager
	 * @return
	 * @throws IOException
	 */
	public static boolean exists( String fileName, String path, AssetManager assetManager ) throws IOException  {
		for( String currentFileName : assetManager.list(path)) {
			if ( currentFileName.equals(fileName)) {
				return true ;
			}
		}
		return false ;
	}
	
	/**
	 * @param path
	 * @param assetManager
	 * @return
	 * @throws IOException 
	 */
	public static String[] list( String path, AssetManager assetManager ) throws IOException {
		String[] files = assetManager.list(path);
		Arrays.sort( files );
		return files ;
	}
}
