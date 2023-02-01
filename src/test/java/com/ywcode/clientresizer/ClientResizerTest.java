package com.ywcode.clientresizer;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ClientResizerTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ClientResizerPlugin.class);
		RuneLite.main(args);
	}
}