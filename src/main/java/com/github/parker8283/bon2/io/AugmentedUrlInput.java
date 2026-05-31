package com.github.parker8283.bon2.io;

import java.io.*;
import java.net.*;

public class AugmentedUrlInput
		implements AutoCloseable
{
	public static final String DEFAULT_USER_AGENT = "BON2/ZeithEdition";
	private final HttpURLConnection request;
	private boolean open;
	
	public AugmentedUrlInput(HttpURLConnection request)
	{
		this.request = request;
	}
	
	protected void ensureUnconnected()
	{
		if(open)
			throw new IllegalStateException("Already connected");
	}
	
	public AugmentedUrlInput setHeader(String name, String value)
	{
		ensureUnconnected();
		request.setRequestProperty(name, value);
		return this;
	}
	
	public AugmentedUrlInput setDefaultUserAgent()
	{
		return setHeader("User-Agent", DEFAULT_USER_AGENT);
	}
	
	public void connect()
			throws IOException
	{
		if(open) throw new IllegalStateException("Already connected");
		request.connect();
		open = true;
	}
	
	public InputStream getInput()
			throws IOException
	{
		if(!open) connect();
		return request.getInputStream();
	}
	
	public static AugmentedUrlInput open(String url)
			throws IOException
	{
		HttpURLConnection request = (HttpURLConnection) new URL(url).openConnection();
		return new AugmentedUrlInput(request);
	}
	
	@Override
	public void close()
			throws IOException
	{
		if(open)
		{
			request.disconnect();
			open = false;
		}
	}
}