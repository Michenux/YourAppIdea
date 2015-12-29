package org.michenux.drodrolib.resources;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

public class ResourceUtils {

	public static Drawable getDrawableByName( String name, Context context ) {
		int drawableResource = context.getResources().getIdentifier(
						name,
						"drawable",
						context.getPackageName());
		if ( drawableResource == 0 ) {
			throw new RuntimeException("Can't find drawable with name: " + name );
		}
		return ContextCompat.getDrawable(context, drawableResource);
	}
	
	public static int getDrawableIdByName( String name, Context context ) {
		int drawableResource = context.getResources().getIdentifier(
						name,
						"drawable",
						context.getPackageName());
		if ( drawableResource == 0 ) {
			throw new RuntimeException("Can't find drawable with name: " + name );
		}
		return drawableResource;
	}
}
