package org.michenux.drodrolib.resources;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class ResourceUtils {

	public static Drawable getDrawableByName( String name, Context context ) {
		int drawableResource = context.getResources().getIdentifier(
						name,
						"drawable",
						context.getPackageName());
		if ( drawableResource == 0 ) {
			throw new RuntimeException("Can't find drawable with name: " + name );
		}
		return context.getResources().getDrawable(drawableResource);
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
