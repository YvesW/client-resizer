package com.ywcode.clientresizer;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("ClientResizer")
public interface ClientResizerConfig extends Config {

	@ConfigSection(
			name = "Automatic Resizing",
			description = "Automatic window resizing based on the current monitor<br>"+
			"Note: It's recommended to enable 'Enable custom window chrome' in RuneLite > Window Settings",
			position = 0,
			closedByDefault = true
	)
	String automaticResizingSettings = "automaticResizingSettings";

	@ConfigSection(
			name = "Hotkey Resizing",
			description = "Hotkey based resizing<br>"
			+"Note: It's recommended to enable 'Enable custom window chrome' in RuneLite > Window Settings",
			position = 1,
			closedByDefault = true
	)
	String hotkeyResizingSettings = "hotkeyResizingSettings";

	@ConfigSection(
			name = "Current Monitor Info",
			description = "Information about the current monitor",
			position = 2,
			closedByDefault = false
	)
	String currentMonitorInfoSettings = "currentMonitorInfoSettings";

	@ConfigItem(
			keyName = "resizeAttributeUnchanged",
			name = "Resize if attribute is unchanged",
			description = "Resize the client if the attribute you've configured is the same as the one of the previous monitor.<br>"
			+"e.g. if both your monitors are 60Hz and the attribute you've set is 'Refresh Rate' and you've changed your client size,<br>"+
					"the client will not be resized when moving to the other monitor if this setting is turned OFF.",
			position = 0,
			section = automaticResizingSettings
	)
	default boolean resizeAttributeUnchanged() {
		return true;
	}

	@ConfigItem(
			keyName = "resizeWhenConfigProfileChanged",
			name = "Resize when switching RuneLite config profile",
			description = "Resize the client when you change the RuneLite config profile if the current monitor matches one of your configured monitor attributes.<br>"
			+"If you do not understand what this setting does, leave this disabled.",
			position = 1,
			section = automaticResizingSettings
	)
	default boolean resizeWhenConfigProfileChanged() {
		return false;
	}

	@ConfigItem(
			keyName = "autoSize1Dimension",
			name = "Size 1 dimension",
			description = "The game will resize to this dimension when moved to the specified monitor.",
			position = 2,
			section = automaticResizingSettings
	)
	default Dimension autoSize1Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "autoSize1Attribute",
			name = "Size 1 monitor attribute type",
			description = "The type of monitor attribute (i.e. the type of the monitor attribute value you've inputted below).",
			position = 3,
			section = automaticResizingSettings
	)
	default MonitorAttribute autoSize1Attribute() {
		return MonitorAttribute.Disabled;
	}

	@ConfigItem(
			keyName = "autoSize1Value",
			name = "Size 1 monitor attribute value",
			description = "The value of the monitor attribute the plugin looks for.<br>"+
			"Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"+
			"Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"+
			"After pasting, click in the game window once for the variable to be registered.",
			position = 4,
			section = automaticResizingSettings
	)
	default String autoSize1Value()	{
		return "";
	}

	@ConfigItem(
			keyName = "autoSize2Dimension",
			name = "Size 2 dimension",
			description = "The game will resize to this dimension when moved to the specified monitor.",
			position = 5,
			section = automaticResizingSettings
	)
	default Dimension autoSize2Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "autoSize2Attribute",
			name = "Size 2 monitor attribute type",
			description = "The type of monitor attribute (i.e. the type of the monitor attribute value you've inputted below).",
			position = 6,
			section = automaticResizingSettings
	)
	default MonitorAttribute autoSize2Attribute() {
		return MonitorAttribute.Disabled;
	}

	@ConfigItem(
			keyName = "autoSize2Value",
			name = "Size 2 monitor attribute value",
			description = "The value of the monitor attribute the plugin looks for.<br>"+
					"Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"+
					"Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"+
					"After pasting, click in the game window once for the variable to be registered.",
			position = 7,
			section = automaticResizingSettings
	)
	default String autoSize2Value()	{
		return "";
	}

	@ConfigItem(
			keyName = "autoSize3Dimension",
			name = "Size 3 dimension",
			description = "The game will resize to this dimension when moved to the specified monitor.",
			position = 8,
			section = automaticResizingSettings
	)
	default Dimension autoSize3Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "autoSize3Attribute",
			name = "Size 3 monitor attribute type",
			description = "The type of monitor attribute (i.e. the type of the monitor attribute value you've inputted below).",
			position = 9,
			section = automaticResizingSettings
	)
	default MonitorAttribute autoSize3Attribute() {
		return MonitorAttribute.Disabled;
	}

	@ConfigItem(
			keyName = "autoSize3Value",
			name = "Size 3 monitor attribute value",
			description = "The value of the monitor attribute the plugin looks for.<br>"+
					"Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"+
					"Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"+
					"After pasting, click in the game window once for the variable to be registered.",
			position = 10,
			section = automaticResizingSettings
	)
	default String autoSize3Value() {
		return "";
	}

	@ConfigItem(
			keyName = "autoSize4Dimension",
			name = "Size 4 dimension",
			description = "The game will resize to this dimension when moved to the specified monitor.",
			position = 11,
			section = automaticResizingSettings
	)
	default Dimension autoSize4Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "autoSize4Attribute",
			name = "Size 4 monitor attribute type",
			description = "The type of monitor attribute (i.e. the type of the monitor attribute value you've inputted below).",
			position = 12,
			section = automaticResizingSettings
	)
	default MonitorAttribute autoSize4Attribute() {
		return MonitorAttribute.Disabled;
	}

	@ConfigItem(
			keyName = "autoSize4Value",
			name = "Size 4 monitor attribute value",
			description = "The value of the monitor attribute the plugin looks for.<br>"+
					"Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"+
					"Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"+
					"After pasting, click in the game window once for the variable to be registered.",
			position = 13,
			section = automaticResizingSettings
	)
	default String autoSize4Value() {
		return "";
	}

	@ConfigItem(
			keyName = "autoSize5Dimension",
			name = "Size 5 dimension",
			description = "The game will resize to this dimension when moved to the specified monitor.",
			position = 14,
			section = automaticResizingSettings
	)
	default Dimension autoSize5Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "autoSize5Attribute",
			name = "Size 5 monitor attribute type",
			description = "The type of monitor attribute (i.e. the type of the monitor attribute value you've inputted below).",
			position = 15,
			section = automaticResizingSettings
	)
	default MonitorAttribute autoSize5Attribute() {
		return MonitorAttribute.Disabled;
	}

	@ConfigItem(
			keyName = "autoSize5Value",
			name = "Size 5 monitor attribute value",
			description = "The value of the monitor attribute the plugin looks for.<br>"+
					"Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"+
					"Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"+
					"After pasting, click in the game window once for the variable to be registered.",
			position = 16,
			section = automaticResizingSettings
	)
	default String autoSize5Value() {
		return "";
	}

	@ConfigItem(
			keyName = "autoSize6Dimension",
			name = "Size 6 dimension",
			description = "The game will resize to this dimension when moved to the specified monitor.",
			position = 17,
			section = automaticResizingSettings
	)
	default Dimension autoSize6Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "autoSize6Attribute",
			name = "Size 6 monitor attribute type",
			description = "The type of monitor attribute (i.e. the type of the monitor attribute value you've inputted below).",
			position = 18,
			section = automaticResizingSettings
	)
	default MonitorAttribute autoSize6Attribute() {
		return MonitorAttribute.Disabled;
	}

	@ConfigItem(
			keyName = "autoSize6Value",
			name = "Size 6 monitor attribute value",
			description = "The value of the monitor attribute the plugin looks for.<br>"+
					"Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"+
					"Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"+
					"After pasting, click in the game window once for the variable to be registered.",
			position = 19,
			section = automaticResizingSettings
	)
	default String autoSize6Value() {
		return "";
	}

	@ConfigItem(
			keyName = "autoSize7Dimension",
			name = "Size 7 dimension",
			description = "The game will resize to this dimension when moved to the specified monitor.",
			position = 20,
			section = automaticResizingSettings
	)
	default Dimension autoSize7Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "autoSize7Attribute",
			name = "Size 7 monitor attribute type",
			description = "The type of monitor attribute (i.e. the type of the monitor attribute value you've inputted below).",
			position = 21,
			section = automaticResizingSettings
	)
	default MonitorAttribute autoSize7Attribute() {
		return MonitorAttribute.Disabled;
	}

	@ConfigItem(
			keyName = "autoSize7Value",
			name = "Size 7 monitor attribute value",
			description = "The value of the monitor attribute the plugin looks for.<br>"+
					"Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"+
					"Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"+
					"After pasting, click in the game window once for the variable to be registered.",
			position = 22,
			section = automaticResizingSettings
	)
	default String autoSize7Value() {
		return "";
	}

	@ConfigItem(
			keyName = "autoSize8Dimension",
			name = "Size 8 dimension",
			description = "The game will resize to this dimension when moved to the specified monitor.",
			position = 23,
			section = automaticResizingSettings
	)
	default Dimension autoSize8Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "autoSize8Attribute",
			name = "Size 8 monitor attribute type",
			description = "The type of monitor attribute (i.e. the type of the monitor attribute value you've inputted below).",
			position = 24,
			section = automaticResizingSettings
	)
	default MonitorAttribute autoSize8Attribute() {
		return MonitorAttribute.Disabled;
	}

	@ConfigItem(
			keyName = "autoSize8Value",
			name = "Size 8 monitor attribute value",
			description = "The value of the monitor attribute the plugin looks for.<br>"+
					"Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"+
					"Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"+
					"After pasting, click in the game window once for the variable to be registered.",
			position = 25,
			section = automaticResizingSettings
	)
	default String autoSize8Value() {
		return "";
	}

	@ConfigItem(
			keyName = "autoSize9Dimension",
			name = "Size 9 dimension",
			description = "The game will resize to this dimension when moved to the specified monitor.",
			position = 26,
			section = automaticResizingSettings
	)
	default Dimension autoSize9Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "autoSize9Attribute",
			name = "Size 9 monitor attribute type",
			description = "The type of monitor attribute (i.e. the type of the monitor attribute value you've inputted below).",
			position = 27,
			section = automaticResizingSettings
	)
	default MonitorAttribute autoSize9Attribute() {
		return MonitorAttribute.Disabled;
	}

	@ConfigItem(
			keyName = "autoSize9Value",
			name = "Size 9 monitor attribute value",
			description = "The value of the monitor attribute the plugin looks for.<br>"+
					"Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"+
					"Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"+
					"After pasting, click in the game window once for the variable to be registered.",
			position = 28,
			section = automaticResizingSettings
	)
	default String autoSize9Value() {
		return "";
	}

	@ConfigItem(
			keyName = "autoSize10Dimension",
			name = "Size 10 dimension",
			description = "The game will resize to this dimension when moved to the specified monitor.",
			position = 29,
			section = automaticResizingSettings
	)
	default Dimension autoSize10Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "autoSize10Attribute",
			name = "Size 10 monitor attribute type",
			description = "The type of monitor attribute (i.e. the type of the monitor attribute value you've inputted below).",
			position = 30,
			section = automaticResizingSettings
	)
	default MonitorAttribute autoSize10Attribute() {
		return MonitorAttribute.Disabled;
	}

	@ConfigItem(
			keyName = "autoSize10Value",
			name = "Size 10 monitor attribute value",
			description = "The value of the monitor attribute the plugin looks for.<br>"+
					"Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"+
					"Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"+
					"After pasting, click in the game window once for the variable to be registered.",
			position = 31,
			section = automaticResizingSettings
	)
	default String autoSize10Value() {
		return "";
	}

	@ConfigItem(
			keyName = "hotkey1Dimension",
			name = "Size 1 dimension",
			description = "The game will resize to this dimension when the specified hotkey is pressed.",
			position = 0,
			section = hotkeyResizingSettings
	)
	default Dimension hotkey1Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "hotkey1Key",
			name = "Size 1 hotkey",
			description = "The hotkey you'd like to press to activate size 1 (above).<br>"+
			"Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
			position = 1,
			section = hotkeyResizingSettings
	)
	default Keybind hotkey1Key() {
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "hotkey2Dimension",
			name = "Size 2 dimension",
			description = "The game will resize to this dimension when the specified hotkey is pressed.",
			position = 2,
			section = hotkeyResizingSettings
	)
	default Dimension hotkey2Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "hotkey2Key",
			name = "Size 2 hotkey",
			description = "The hotkey you'd like to press to activate size 2 (above).<br>"+
			"Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
			position = 3,
			section = hotkeyResizingSettings
	)
	default Keybind hotkey2Key() {
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "hotkey3Dimension",
			name = "Size 3 dimension",
			description = "The game will resize to this dimension when the specified hotkey is pressed.",
			position = 4,
			section = hotkeyResizingSettings
	)
	default Dimension hotkey3Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "hotkey3Key",
			name = "Size 3 hotkey",
			description = "The hotkey you'd like to press to activate size 3 (above).<br>"+
			"Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
			position = 5,
			section = hotkeyResizingSettings
	)
	default Keybind hotkey3Key() {
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "hotkey4Dimension",
			name = "Size 4 dimension",
			description = "The game will resize to this dimension when the specified hotkey is pressed.",
			position = 6,
			section = hotkeyResizingSettings
	)
	default Dimension hotkey4Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "hotkey4Key",
			name = "Size 4 hotkey",
			description = "The hotkey you'd like to press to activate size 4 (above).<br>"+
			"Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
			position = 7,
			section = hotkeyResizingSettings
	)
	default Keybind hotkey4Key() {
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "hotkey5Dimension",
			name = "Size 5 dimension",
			description = "The game will resize to this dimension when the specified hotkey is pressed.",
			position = 8,
			section = hotkeyResizingSettings
	)
	default Dimension hotkey5Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "hotkey5Key",
			name = "Size 5 hotkey",
			description = "The hotkey you'd like to press to activate size 5 (above).<br>"+
			"Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
			position = 9,
			section = hotkeyResizingSettings
	)
	default Keybind hotkey5Key() {
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "hotkey6Dimension",
			name = "Size 6 dimension",
			description = "The game will resize to this dimension when the specified hotkey is pressed.",
			position = 10,
			section = hotkeyResizingSettings
	)
	default Dimension hotkey6Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "hotkey6Key",
			name = "Size 6 hotkey",
			description = "The hotkey you'd like to press to activate size 6 (above).<br>"+
			"Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
			position = 11,
			section = hotkeyResizingSettings
	)
	default Keybind hotkey6Key() {
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "hotkey7Dimension",
			name = "Size 7 dimension",
			description = "The game will resize to this dimension when the specified hotkey is pressed.",
			position = 12,
			section = hotkeyResizingSettings
	)
	default Dimension hotkey7Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "hotkey7Key",
			name = "Size 7 hotkey",
			description = "The hotkey you'd like to press to activate size 7 (above).<br>"+
			"Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
			position = 13,
			section = hotkeyResizingSettings
	)
	default Keybind hotkey7Key() {
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "hotkey8Dimension",
			name = "Size 8 dimension",
			description = "The game will resize to this dimension when the specified hotkey is pressed.",
			position = 14,
			section = hotkeyResizingSettings
	)
	default Dimension hotkey8Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "hotkey8Key",
			name = "Size 8 hotkey",
			description = "The hotkey you'd like to press to activate size 8 (above).<br>"+
			"Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
			position = 15,
			section = hotkeyResizingSettings
	)
	default Keybind hotkey8Key() {
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "hotkey9Dimension",
			name = "Size 9 dimension",
			description = "The game will resize to this dimension when the specified hotkey is pressed.",
			position = 16,
			section = hotkeyResizingSettings
	)
	default Dimension hotkey9Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "hotkey9Key",
			name = "Size 9 hotkey",
			description = "The hotkey you'd like to press to activate size 9 (above).<br>"+
			"Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
			position = 17,
			section = hotkeyResizingSettings
	)
	default Keybind hotkey9Key() {
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "hotkey10Dimension",
			name = "Size 10 dimension",
			description = "The game will resize to this dimension when the specified hotkey is pressed.",
			position = 18,
			section = hotkeyResizingSettings
	)
	default Dimension hotkey10Dimension() {
		return new Dimension(0, 0);
	}

	@ConfigItem(
			keyName = "hotkey10Key",
			name = "Size 10 hotkey",
			description = "The hotkey you'd like to press to activate size 10 (above).<br>"+
			"Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
			position = 19,
			section = hotkeyResizingSettings
	)
	default Keybind hotkey10Key() {
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "copyAttribute",
			name = "Copy attribute of current monitor",
			description = "Copy this attribute of the current monitor to the clipboard<br>"+
			"Gets disabled automatically on client or plugin restart to not clutter your clipboard.",
			position = 0,
			section = currentMonitorInfoSettings
	)
	default MonitorAttribute copyAttribute() {
		return MonitorAttribute.Disabled;
	}
}