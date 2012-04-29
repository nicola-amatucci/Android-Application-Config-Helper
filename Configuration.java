//package it.nicola_amatucci.android.utils;

/*    
    Copyright (C) 2012  Nicola Amatucci

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

public class Configuration
{
	private static String TAG = "Configuration";
	public static String FILE_NAME = "configurazione.json";
	private static Configuration singleton;
		
	public static Configuration getInstance(Context ctx)
	{
		if (ctx != null && singleton == null)
			singleton = new Configuration(ctx);
		
		return singleton;
	}
	
	private Context ctx;
	private JSONObject configObject;
	
	private Configuration(Context ctx)
	{
		this.ctx = ctx;
		
		File file = this.ctx.getFileStreamPath(FILE_NAME);
		if(file.exists())
		{
			try
			{
				//legge il file
				FileInputStream is = ctx.openFileInput(FILE_NAME);
				byte [] buffer = new byte[is.available()];
				while (is.read(buffer) != -1);
				is.close();
				
				//converte in oggetto json
				this.configObject = new JSONObject(new String(buffer));				
			}
			catch (Exception e)
			{
				Log.v(TAG, e.toString());
			}
		}

		//crea un oggetto json vuoto anche se c'e' stato un errore di lettura
		if (this.configObject == null)
			this.configObject = new JSONObject();
	}
	
	public boolean put(String name, String value)
	{
		try
		{
			this.configObject.put(name, value);
			this.save();
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.v(TAG, e.toString());
		}
		
		return false;
	}
	
	public String get(String name)
	{
		try
		{
			return this.configObject.getString(name);
		}
		catch (JSONException e)
		{
			Log.v(TAG, e.toString());
		}
		
		return null;
	}
	
	public boolean putJSONObject(String name, JSONObject o)
	{
		try
		{
			this.configObject.put(name, o);
			this.save();
			return true;
		}
		catch (Exception e)
		{
			Log.v(TAG, e.toString());
		}
		
		return false;
	}

	public JSONObject getJSONObject(String name)
	{
		try
		{
			return this.configObject.getJSONObject(name);
		}
		catch (JSONException e)
		{
			Log.v(TAG, e.toString());
		}
		
		return null;
	}
	
	private void save() throws Exception
	{
		FileOutputStream fos = new ContextWrapper(ctx).openFileOutput(FILE_NAME, ContextWrapper.MODE_PRIVATE);
		fos.write(this.configObject.toString().getBytes());
		fos.close();
	}
}

