package com.ywcode.clientresizer;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("ClientResizer")
public interface ClientResizerConfig extends Config {

    @ConfigSection(
            name = "Automatic Resizing",
            description = "Automatic window resizing based on the current monitor.<br>"
                    + "Note: It's recommended to enable 'Enable custom window chrome' in 'RuneLite' > 'Window Settings'",
            position = 0,
            closedByDefault = true
    )
    String automaticResizingSettings = "automaticResizingSettings";

    @ConfigSection(
            name = "Hotkey Resizing",
            description = "Hotkey based resizing.<br>"
                    + "Tip: This allows the user to override contain in screen.",
            position = 1,
            closedByDefault = true
    )
    String hotkeyResizingSettings = "hotkeyResizingSettings";

    @ConfigSection(
            name = "Resizable Scaling",
            description = "Resizable Scaling (stretched mode plugin).<br>"
                    + "Note: Requires the stretched mode plugin to be enabled.",
            position = 2,
            closedByDefault = true
    )
    String resizableScalingSettings = "resizableScalingSettings";

    @ConfigSection(
            name = "Hotkey repositioning",
            description = "Change the position of the client with hotkeys.",
            position = 3,
            closedByDefault = true
    )
    String positionSettings = "positionSettings";

    @ConfigSection(
            name = "Contain in screen",
            description = "Contain the client in the screen (sides, offset, hard/soft). Will snap back once your cursor leaves the menubar (title bar).<br>"
                    + "Note: It's recommended to enable 'Enable custom window chrome' in 'RuneLite' > 'Window Settings'<br>"
                    + "Tip: you can always switch monitors by using hotkey repositioning (see above).",
            position = 4,
            closedByDefault = true
    )
    String containInScreenSettings = "containInScreenSettings";

    @ConfigSection(
            name = "Advanced",
            description = "Advanced settings",
            position = 5,
            closedByDefault = true
    )
    String advancedSettings = "advancedSettings";

    @ConfigSection(
            name = "Current Monitor Info",
            description = "Information about the current monitor",
            position = 6
            //closedByDefault = false //Redundant default parameter value assignment; default is false already.
    )
    String currentMonitorInfoSettings = "currentMonitorInfoSettings";

    @ConfigItem(
            keyName = "resizeAttributeUnchanged",
            name = "Resize if attribute is unchanged",
            description = "Resize the client if the attribute you've configured is the same as the one of the previous monitor.<br>"
                    + "e.g. if both your monitors are 60Hz and the attribute you've set is 'Refresh Rate' and you've changed your client size,<br>"
                    + "the client will not be resized when moving to the other monitor if this setting is turned OFF.",
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
                    + "If you do not understand what this setting does, leave this disabled.",
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
            description = "The value of the monitor attribute the plugin looks for.<br>"
                    + "Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"
                    + "Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"
                    + "After pasting, click in the game window once for the variable to be registered.",
            position = 4,
            section = automaticResizingSettings
    )
    default String autoSize1Value() {
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
            description = "The value of the monitor attribute the plugin looks for.<br>"
                    + "Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"
                    + "Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"
                    + "After pasting, click in the game window once for the variable to be registered.",
            position = 7,
            section = automaticResizingSettings
    )
    default String autoSize2Value() {
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
            description = "The value of the monitor attribute the plugin looks for.<br>"
                    + "Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"
                    + "Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"
                    + "After pasting, click in the game window once for the variable to be registered.",
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
            description = "The value of the monitor attribute the plugin looks for.<br>"
                    + "Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"
                    + "Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"
                    + "After pasting, click in the game window once for the variable to be registered.",
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
            description = "The value of the monitor attribute the plugin looks for.<br>"
                    + "Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"
                    + "Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"
                    + "After pasting, click in the game window once for the variable to be registered.",
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
            description = "The value of the monitor attribute the plugin looks for.<br>"
                    + "Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"
                    + "Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"
                    + "After pasting, click in the game window once for the variable to be registered.",
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
            description = "The value of the monitor attribute the plugin looks for.<br>"
                    + "Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"
                    + "Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"
                    + "After pasting, click in the game window once for the variable to be registered.",
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
            description = "The value of the monitor attribute the plugin looks for.<br>"
                    + "Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"
                    + "Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"
                    + "After pasting, click in the game window once for the variable to be registered.",
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
            description = "The value of the monitor attribute the plugin looks for.<br>"
                    + "Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"
                    + "Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"
                    + "After pasting, click in the game window once for the variable to be registered.",
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
            description = "The value of the monitor attribute the plugin looks for.<br>"
                    + "Get this value by selecting it in the dropdown in the most bottom category ('Copy attribute of current monitor') and pasting it here.<br>"
                    + "Make sure the type of attribute above matches the type of attribute you are pasting (e.g. 'Refresh Rate' if you are pasting a refresh rate).<br>"
                    + "After pasting, click in the game window once for the variable to be registered.",
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
            description = "The hotkey you'd like to press to activate size 1 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
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
            description = "The hotkey you'd like to press to activate size 2 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
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
            description = "The hotkey you'd like to press to activate size 3 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
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
            description = "The hotkey you'd like to press to activate size 4 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
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
            description = "The hotkey you'd like to press to activate size 5 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
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
            description = "The hotkey you'd like to press to activate size 6 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
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
            description = "The hotkey you'd like to press to activate size 7 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
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
            description = "The hotkey you'd like to press to activate size 8 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
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
            description = "The hotkey you'd like to press to activate size 9 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
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
            description = "The hotkey you'd like to press to activate size 10 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
            position = 19,
            section = hotkeyResizingSettings
    )
    default Keybind hotkey10Key() {
        return Keybind.NOT_SET;
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic1",
            name = "Automatic 1",
            description = "Enable setting resizable scaling (stretched mode) for automatic resizing 1.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 0,
            section = resizableScalingSettings
    )
    default boolean resizableScalingAutomatic1() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic1Percent",
            name = "Automatic 1 percentage",
            description = "Resizable scaling percentage (stretched mode) for automatic resizing 1.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 1,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingAutomatic1Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic2",
            name = "Automatic 2",
            description = "Enable setting resizable scaling (stretched mode) for automatic resizing 2.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 2,
            section = resizableScalingSettings
    )
    default boolean resizableScalingAutomatic2() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic2Percent",
            name = "Automatic 2 percentage",
            description = "Resizable scaling percentage (stretched mode) for automatic resizing 2.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 3,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingAutomatic2Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic3",
            name = "Automatic 3",
            description = "Enable setting resizable scaling (stretched mode) for automatic resizing 3.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 4,
            section = resizableScalingSettings
    )
    default boolean resizableScalingAutomatic3() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic3Percent",
            name = "Automatic 3 percentage",
            description = "Resizable scaling percentage (stretched mode) for automatic resizing 3.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 5,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingAutomatic3Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic4",
            name = "Automatic 4",
            description = "Enable setting resizable scaling (stretched mode) for automatic resizing 4.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 6,
            section = resizableScalingSettings
    )
    default boolean resizableScalingAutomatic4() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic4Percent",
            name = "Automatic 4 percentage",
            description = "Resizable scaling percentage (stretched mode) for automatic resizing 4.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 7,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingAutomatic4Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic5",
            name = "Automatic 5",
            description = "Enable setting resizable scaling (stretched mode) for automatic resizing 5.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 8,
            section = resizableScalingSettings
    )
    default boolean resizableScalingAutomatic5() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic5Percent",
            name = "Automatic 5 percentage",
            description = "Resizable scaling percentage (stretched mode) for automatic resizing 5.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 9,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingAutomatic5Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic6",
            name = "Automatic 6",
            description = "Enable setting resizable scaling (stretched mode) for automatic resizing 6.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 10,
            section = resizableScalingSettings
    )
    default boolean resizableScalingAutomatic6() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic6Percent",
            name = "Automatic 6 percentage",
            description = "Resizable scaling percentage (stretched mode) for automatic resizing 6.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 11,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingAutomatic6Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic7",
            name = "Automatic 7",
            description = "Enable setting resizable scaling (stretched mode) for automatic resizing 7.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 12,
            section = resizableScalingSettings
    )
    default boolean resizableScalingAutomatic7() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic7Percent",
            name = "Automatic 7 percentage",
            description = "Resizable scaling percentage (stretched mode) for automatic resizing 7.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 13,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingAutomatic7Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic8",
            name = "Automatic 8",
            description = "Enable setting resizable scaling (stretched mode) for automatic resizing 8.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 14,
            section = resizableScalingSettings
    )
    default boolean resizableScalingAutomatic8() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic8Percent",
            name = "Automatic 8 percentage",
            description = "Resizable scaling percentage (stretched mode) for automatic resizing 8.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 15,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingAutomatic8Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic9",
            name = "Automatic 9",
            description = "Enable setting resizable scaling (stretched mode) for automatic resizing 9.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 16,
            section = resizableScalingSettings
    )
    default boolean resizableScalingAutomatic9() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic9Percent",
            name = "Automatic 9 percentage",
            description = "Resizable scaling percentage (stretched mode) for automatic resizing 9.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 17,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingAutomatic9Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic10",
            name = "Automatic 10",
            description = "Enable setting resizable scaling (stretched mode) for automatic resizing 10.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 18,
            section = resizableScalingSettings
    )
    default boolean resizableScalingAutomatic10() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingAutomatic10Percent",
            name = "Automatic 10 percentage",
            description = "Resizable scaling percentage (stretched mode) for automatic resizing 10.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 19,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingAutomatic10Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey1",
            name = "Hotkey 1",
            description = "Enable setting resizable scaling (stretched mode) for hotkey resizing 1.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 20,
            section = resizableScalingSettings
    )
    default boolean resizableScalingHotkey1() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey1Percent",
            name = "Hotkey 1 percentage",
            description = "Resizable scaling percentage (stretched mode) for hotkey resizing 1.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 21,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingHotkey1Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey2",
            name = "Hotkey 2",
            description = "Enable setting resizable scaling (stretched mode) for Hotkey resizing 2.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 22,
            section = resizableScalingSettings
    )
    default boolean resizableScalingHotkey2() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey2Percent",
            name = "Hotkey 2 percentage",
            description = "Resizable scaling percentage (stretched mode) for Hotkey resizing 2.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 23,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingHotkey2Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey3",
            name = "Hotkey 3",
            description = "Enable setting resizable scaling (stretched mode) for Hotkey resizing 3.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 24,
            section = resizableScalingSettings
    )
    default boolean resizableScalingHotkey3() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey3Percent",
            name = "Hotkey 3 percentage",
            description = "Resizable scaling percentage (stretched mode) for Hotkey resizing 3.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 25,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingHotkey3Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey4",
            name = "Hotkey 4",
            description = "Enable setting resizable scaling (stretched mode) for Hotkey resizing 4.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 26,
            section = resizableScalingSettings
    )
    default boolean resizableScalingHotkey4() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey4Percent",
            name = "Hotkey 4 percentage",
            description = "Resizable scaling percentage (stretched mode) for Hotkey resizing 4.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 27,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingHotkey4Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey5",
            name = "Hotkey 5",
            description = "Enable setting resizable scaling (stretched mode) for Hotkey resizing 5.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 28,
            section = resizableScalingSettings
    )
    default boolean resizableScalingHotkey5() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey5Percent",
            name = "Hotkey 5 percentage",
            description = "Resizable scaling percentage (stretched mode) for Hotkey resizing 5.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 29,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingHotkey5Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey6",
            name = "Hotkey 6",
            description = "Enable setting resizable scaling (stretched mode) for Hotkey resizing 6.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 30,
            section = resizableScalingSettings
    )
    default boolean resizableScalingHotkey6() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey6Percent",
            name = "Hotkey 6 percentage",
            description = "Resizable scaling percentage (stretched mode) for Hotkey resizing 6.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 31,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingHotkey6Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey7",
            name = "Hotkey 7",
            description = "Enable setting resizable scaling (stretched mode) for Hotkey resizing 7.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 32,
            section = resizableScalingSettings
    )
    default boolean resizableScalingHotkey7() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey7Percent",
            name = "Hotkey 7 percentage",
            description = "Resizable scaling percentage (stretched mode) for Hotkey resizing 7.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 33,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingHotkey7Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey8",
            name = "Hotkey 8",
            description = "Enable setting resizable scaling (stretched mode) for Hotkey resizing 8.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 34,
            section = resizableScalingSettings
    )
    default boolean resizableScalingHotkey8() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey8Percent",
            name = "Hotkey 8 percentage",
            description = "Resizable scaling percentage (stretched mode) for Hotkey resizing 8.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 35,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingHotkey8Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey9",
            name = "Hotkey 9",
            description = "Enable setting resizable scaling (stretched mode) for Hotkey resizing 9.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 36,
            section = resizableScalingSettings
    )
    default boolean resizableScalingHotkey9() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey9Percent",
            name = "Hotkey 9 percentage",
            description = "Resizable scaling percentage (stretched mode) for Hotkey resizing 9.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 37,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingHotkey9Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey10",
            name = "Hotkey 10",
            description = "Enable setting resizable scaling (stretched mode) for hotkey resizing 10.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 38,
            section = resizableScalingSettings
    )
    default boolean resizableScalingHotkey10() {
        return false;
    }

    @ConfigItem(
            keyName = "resizableScalingHotkey10Percent",
            name = "Hotkey 10 percentage",
            description = "Resizable scaling percentage (stretched mode) for hotkey resizing 10.<br>"
                    + "This requires the stretched mode plugin to be enabled.",
            position = 39,
            section = resizableScalingSettings
    )
    @Units(
            Units.PERCENT
    )
    default int resizableScalingHotkey10Percent() {
        return ClientResizerPlugin.getDefaultResizableScaling();
    }

    @ConfigItem(
            keyName = "hotkey1PositionX",
            name = "Position 1 X",
            description = "The client will move itself to this position (X) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 0,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey1PositionX() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey1PositionY",
            name = "Position 1 Y",
            description = "The client will move itself to this position (Y) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 1,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey1PositionY() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey1PositionKey",
            name = "Position 1 hotkey",
            description = "The hotkey you'd like to press to activate position 1 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
            position = 2,
            section = positionSettings
    )
    default Keybind hotkey1PositionKey() {
        return Keybind.NOT_SET;
    }

    @ConfigItem(
            keyName = "hotkey2PositionX",
            name = "Position 2 X",
            description = "The client will move itself to this position (X) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 3,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey2PositionX() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey2PositionY",
            name = "Position 2 Y",
            description = "The client will move itself to this position (Y) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 4,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey2PositionY() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey2PositionKey",
            name = "Position 2 hotkey",
            description = "The hotkey you'd like to press to activate position 2 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
            position = 5,
            section = positionSettings
    )
    default Keybind hotkey2PositionKey() {
        return Keybind.NOT_SET;
    }

    @ConfigItem(
            keyName = "hotkey3PositionX",
            name = "Position 3 X",
            description = "The client will move itself to this position (X) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 6,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey3PositionX() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey3PositionY",
            name = "Position 3 Y",
            description = "The client will move itself to this position (Y) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 7,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey3PositionY() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey3PositionKey",
            name = "Position 3 hotkey",
            description = "The hotkey you'd like to press to activate position 3 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
            position = 8,
            section = positionSettings
    )
    default Keybind hotkey3PositionKey() {
        return Keybind.NOT_SET;
    }

    @ConfigItem(
            keyName = "hotkey4PositionX",
            name = "Position 4 X",
            description = "The client will move itself to this position (X) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 9,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey4PositionX() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey4PositionY",
            name = "Position 4 Y",
            description = "The client will move itself to this position (Y) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 10,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey4PositionY() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey4PositionKey",
            name = "Position 4 hotkey",
            description = "The hotkey you'd like to press to activate position 4 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
            position = 11,
            section = positionSettings
    )
    default Keybind hotkey4PositionKey() {
        return Keybind.NOT_SET;
    }

    @ConfigItem(
            keyName = "hotkey5PositionX",
            name = "Position 5 X",
            description = "The client will move itself to this position (X) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 12,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey5PositionX() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey5PositionY",
            name = "Position 5 Y",
            description = "The client will move itself to this position (Y) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 13,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey5PositionY() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey5PositionKey",
            name = "Position 5 hotkey",
            description = "The hotkey you'd like to press to activate position 5 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
            position = 14,
            section = positionSettings
    )
    default Keybind hotkey5PositionKey() {
        return Keybind.NOT_SET;
    }

    @ConfigItem(
            keyName = "hotkey6PositionX",
            name = "Position 6 X",
            description = "The client will move itself to this position (X) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 15,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey6PositionX() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey6PositionY",
            name = "Position 6 Y",
            description = "The client will move itself to this position (Y) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 16,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey6PositionY() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey6PositionKey",
            name = "Position 6 hotkey",
            description = "The hotkey you'd like to press to activate position 6 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
            position = 17,
            section = positionSettings
    )
    default Keybind hotkey6PositionKey() {
        return Keybind.NOT_SET;
    }

    @ConfigItem(
            keyName = "hotkey7PositionX",
            name = "Position 7 X",
            description = "The client will move itself to this position (X) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 18,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey7PositionX() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey7PositionY",
            name = "Position 7 Y",
            description = "The client will move itself to this position (Y) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 19,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey7PositionY() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey7PositionKey",
            name = "Position 7 hotkey",
            description = "The hotkey you'd like to press to activate position 7 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
            position = 20,
            section = positionSettings
    )
    default Keybind hotkey7PositionKey() {
        return Keybind.NOT_SET;
    }

    @ConfigItem(
            keyName = "hotkey8PositionX",
            name = "Position 8 X",
            description = "The client will move itself to this position (X) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 21,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey8PositionX() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey8PositionY",
            name = "Position 8 Y",
            description = "The client will move itself to this position (Y) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 22,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey8PositionY() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey8PositionKey",
            name = "Position 8 hotkey",
            description = "The hotkey you'd like to press to activate position 8 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
            position = 23,
            section = positionSettings
    )
    default Keybind hotkey8PositionKey() {
        return Keybind.NOT_SET;
    }

    @ConfigItem(
            keyName = "hotkey9PositionX",
            name = "Position 9 X",
            description = "The client will move itself to this position (X) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 24,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey9PositionX() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey9PositionY",
            name = "Position 9 Y",
            description = "The client will move itself to this position (Y) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 25,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey9PositionY() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey9PositionKey",
            name = "Position 9 hotkey",
            description = "The hotkey you'd like to press to activate position 9 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
            position = 26,
            section = positionSettings
    )
    default Keybind hotkey9PositionKey() {
        return Keybind.NOT_SET;
    }

    @ConfigItem(
            keyName = "hotkey10PositionX",
            name = "Position 10 X",
            description = "The client will move itself to this position (X) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 27,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey10PositionX() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey10PositionY",
            name = "Position 10 Y",
            description = "The client will move itself to this position (Y) when the specified hotkey is pressed.<br>"
                    + "Negative values are valid if e.g. (0,0) does not move your client to the top left enough.",
            position = 28,
            section = positionSettings
    )
    @Range(
            min = -100000
    )
    default int hotkey10PositionY() {
        return 0;
    }

    @ConfigItem(
            keyName = "hotkey10PositionKey",
            name = "Position 10 hotkey",
            description = "The hotkey you'd like to press to activate position 10 (above).<br>"
                    + "Can be a combination of keys (e.g. ctrl+L). Reset the key (to 'Not set') to disable this setting.",
            position = 29,
            section = positionSettings
    )
    default Keybind hotkey10PositionKey() {
        return Keybind.NOT_SET;
    }

    //Alternatively use a Set here but meh. The set is harder to use for the user and I feel that 4 booleans are still ok.
    @ConfigItem(
            keyName = "containInScreenTop",
            name = "Contain top side",
            description = "Try to contain the top side of the client in the screen.<br>"
                    + "Note: It's recommended to enable 'Enable custom window chrome' in 'RuneLite' > 'Window Settings'<br>"
                    + "It is recommended to enable 'Lock windows size' in the 'RuneLite' config. Otherwise Windows will potentially try to snap resize the client.<br>",
            position = 0,
            section = containInScreenSettings
    )
    default boolean containInScreenTop() {
        return false;
    }

    @ConfigItem(
            keyName = "containInScreenRight",
            name = "Contain right side",
            description = "Try to contain the right side of the client in the screen.<br>"
                    + "Note: It's recommended to enable 'Enable custom window chrome' in 'RuneLite' > 'Window Settings'<br>"
                    + "It is recommended to enable 'Lock windows size' in the 'RuneLite' config. Otherwise Windows will potentially try to snap resize the client.<br>",
            position = 1,
            section = containInScreenSettings
    )
    default boolean containInScreenRight() {
        return false;
    }

    @ConfigItem(
            keyName = "containInScreenBottom",
            name = "Contain bottom side",
            description = "Try to contain the bottom side of the client in the screen.<br>"
                    + "Note: It's recommended to enable 'Enable custom window chrome' in 'RuneLite' > 'Window Settings'<br>"
                    + "It is recommended to enable 'Lock windows size' in the 'RuneLite' config. Otherwise Windows will potentially try to snap resize the client.<br>",
            position = 2,
            section = containInScreenSettings
    )
    default boolean containInScreenBottom() {
        return false;
    }

    @ConfigItem(
            keyName = "containInScreenLeft",
            name = "Contain left side",
            description = "Try to contain the left side of the client in the screen.<br>"
                    + "Note: It's recommended to enable 'Enable custom window chrome' in 'RuneLite' > 'Window Settings'<br>"
                    + "It is recommended to enable 'Lock windows size' in the 'RuneLite' config. Otherwise Windows will potentially try to snap resize the client.<br>",
            position = 3,
            section = containInScreenSettings
    )
    default boolean containInScreenLeft() {
        return false;
    }

    @ConfigItem(
            keyName = "containInScreenTopOffset",
            name = "Top offset",
            description = "Offset for the top side of the client.<br>"
                    + "Increase/decrease if you want the client contained in screen more/less towards this side of the screen",
            position = 4,
            section = containInScreenSettings
    )
    @Range(
            min = -2000
    )
    @Units(
            Units.PIXELS
    )
    default int containInScreenTopOffset() {
        return 0;
    }

    @ConfigItem(
            keyName = "containInScreenRightOffset",
            name = "Right offset",
            description = "Offset for the right side of the client.<br>"
                    + "Increase/decrease if you want the client contained in screen more/less towards this side of the screen",
            position = 5,
            section = containInScreenSettings
    )
    @Range(
            min = -2000
    )
    @Units(
            Units.PIXELS
    )
    default int containInScreenRightOffset() {
        return 0;
    }

    @ConfigItem(
            keyName = "containInScreenBottomOffset",
            name = "Bottom offset",
            description = "Offset for the bottom side of the client.<br>"
                    + "Increase/decrease if you want the client contained in screen more/less towards this side of the screen",
            position = 6,
            section = containInScreenSettings
    )
    @Range(
            min = -2000
    )
    @Units(
            Units.PIXELS
    )
    default int containInScreenBottomOffset() {
        return 0;
    }

    @ConfigItem(
            keyName = "containInScreenLeftOffset",
            name = "Left offset",
            description = "Offset for the left side of the client.<br>"
                    + "Increase/decrease if you want the client contained in screen more/less towards this side of the screen",
            position = 7,
            section = containInScreenSettings
    )
    @Range(
            min = -2000
    )
    @Units(
            Units.PIXELS
    )
    default int containInScreenLeftOffset() {
        return 0;
    }

    @ConfigItem(
            keyName = "containInScreenSnapBackPx",
            name = "Snap back pixels",
            description = "Amount of pixels the client can be moved past the screen edge (plus potential offset) in a tick while still snapping back to the screen.<br>"
                    + "I.e. a high value will cause a 'hard' contain in screen (client will always snap back/be contained in screen when moving),<br>"
                    + "a low value (e.g. 10px) will cause a 'soft' contain in screen. In that case the client would only be contained in screen<br>"
                    + "if it was moved a maximum of e.g. 10 pixels over the edge of the screen (+ potential offset) in a single tick.<br>"
                    + "Tip: you can always switch monitors by using hotkey repositioning (see above).",
            position = 8,
            section = containInScreenSettings
    )
    @Range(
            min = 1
    )
    //Don't do Units.Pixels because then the default value does not fit inside the box
    default int containInScreenSnapBackPx() {
        return 100000 ;
    }

    @ConfigItem(
            keyName = "showChatMessage",
            name = "Show resize chat message",
            description = "Show the chat message when changing the game size / client size or when changing the resizable scaling (stretched mode plugin).<br>"
                    + "This setting is enabled by default. If you disable it, then DO remember that you are using this plugin!<br>"
                    + "Do NOT complain in the RuneLite Discord that your client randomly changes in size or the UI scaling randomly changes.",
            position = 0,
            section = advancedSettings
    )
    default boolean showChatMessage() {
        return true;
    }

    @ConfigItem(
            keyName = "showChatMessageReposition",
            name = "Show reposition chat message",
            description = "Show the chat message when repositioning the client.<br>"
                    + "This setting is enabled by default. If you disable it, then DO remember that you are using this plugin!<br>"
                    + "Do NOT complain in the RuneLite Discord that your client randomly repositions.",
            position = 1,
            section = advancedSettings
    )
    default boolean showChatMessageReposition() {
        return true;
    }

    @ConfigItem(
            keyName = "showChatMessageContain",
            name = "Show contain chat message",
            description = "Show the chat message when trying to contain the client in screen.<br>"
                    + "This setting is enabled by default. If you disable it, then DO remember that you are using this plugin!<br>"
                    + "Do NOT complain in the RuneLite Discord that your client randomly repositions/tries to contain itself in the screen.",
            position = 2,
            section = advancedSettings
    )
    default boolean showChatMessageContain() {
        return true;
    }

    @ConfigItem(
            keyName = "copyAttribute",
            name = "Copy attribute of current monitor",
            description = "Copy this attribute of the current monitor to the clipboard<br>"
                    + "Gets disabled automatically on client or plugin restart to not clutter your clipboard.",
            position = 0,
            section = currentMonitorInfoSettings
    )
    default MonitorAttribute copyAttribute() {
        return MonitorAttribute.Disabled;
    }

    @ConfigItem(
            keyName = "copyPosition",
            name = "Copy client position",
            description = "Copy the client position to the clipboard every game tick. You can paste this to e.g. notepad to find out your client's current position.<br>"
                    + "Gets disabled automatically on client or plugin restart to not clutter your clipboard.",
            position = 1,
            section = currentMonitorInfoSettings
    )
    default boolean copyPosition() {
        return false;
    }
}