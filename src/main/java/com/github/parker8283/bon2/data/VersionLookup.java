package com.github.parker8283.bon2.data;

import com.github.parker8283.bon2.data.VersionJson.MappingsJson;
import com.github.parker8283.bon2.io.AugmentedUrlInput;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;

import java.io.*;
import java.util.Map;

public enum VersionLookup
{
	
	INSTANCE;
	
	private static final String VERSION_JSON = "https://mcp.zeith.org/versions.json";
	private static final Gson GSON = new GsonBuilder().create();
	
	private VersionJson jsoncache;
	
	public String getVersionFor(String version)
	{
		if(jsoncache != null)
		{
			for(String s : jsoncache.getVersions())
			{
				MappingsJson mappings = jsoncache.getMappings(s);
				if(mappings.hasSnapshot(version) || mappings.hasStable(version))
				{
					return s;
				}
			}
		}
		return null;
	}
	
	public VersionJson getVersions()
	{
		return jsoncache;
	}
	
	public void refresh()
			throws IOException
	{
		try(AugmentedUrlInput input = AugmentedUrlInput.open(VERSION_JSON).setDefaultUserAgent(); Reader in = new InputStreamReader(input.getInput()))
		{
			INSTANCE.jsoncache = new VersionJson(GSON.fromJson(in, new TypeToken<Map<String, MappingsJson>>() {}.getType()));
		}
	}
}
