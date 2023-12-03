package com.ywcode.clientresizer;

import com.google.inject.*;
import lombok.*;
import lombok.extern.slf4j.*;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.callback.*;
import net.runelite.client.config.*;
import net.runelite.client.eventbus.*;
import net.runelite.client.events.*;
import net.runelite.client.input.*;
import net.runelite.client.plugins.*;
import net.runelite.client.ui.*;
import net.runelite.client.util.*;

import java.awt.*;
import java.awt.datatransfer.*;
import java.util.regex.*;

@Slf4j
@PluginDescriptor(
        name = "Client Resizer",
        description = "Allows for automatic or hotkey-based resizing of the client.",
        tags = {"client,resize,automatic,pixels,refresh rate,game size,size,screen size,monitor,display,screen,autoresize,hotkey,hot key,stretched mode,resizable scaling,scaling"}
)

public class ClientResizerPlugin extends Plugin {

    //------------- Wall of config vars -------------
    //Vars are quite heavily cached so could probably just config.configKey(). However, the best practice behavior in plugins is to have a bunch of variables to store the results of the config methods, and check it in startUp/onConfigChanged. It feels redundant, but it's better than hitting the reflective calls every frame. --LlemonDuck
    private static boolean resizeAttributeUnchanged;
    private static boolean resizeWhenConfigProfileChanged;
    private static Dimension autoSize1Dimension;
    private static MonitorAttribute autoSize1Attribute;
    private static String autoSize1Value;
    private static Dimension autoSize2Dimension;
    private static MonitorAttribute autoSize2Attribute;
    private static String autoSize2Value;
    private static Dimension autoSize3Dimension;
    private static MonitorAttribute autoSize3Attribute;
    private static String autoSize3Value;
    private static Dimension autoSize4Dimension;
    private static MonitorAttribute autoSize4Attribute;
    private static String autoSize4Value;
    private static Dimension autoSize5Dimension;
    private static MonitorAttribute autoSize5Attribute;
    private static String autoSize5Value;
    private static Dimension autoSize6Dimension;
    private static MonitorAttribute autoSize6Attribute;
    private static String autoSize6Value;
    private static Dimension autoSize7Dimension;
    private static MonitorAttribute autoSize7Attribute;
    private static String autoSize7Value;
    private static Dimension autoSize8Dimension;
    private static MonitorAttribute autoSize8Attribute;
    private static String autoSize8Value;
    private static Dimension autoSize9Dimension;
    private static MonitorAttribute autoSize9Attribute;
    private static String autoSize9Value;
    private static Dimension autoSize10Dimension;
    private static MonitorAttribute autoSize10Attribute;
    private static String autoSize10Value;
    private static Dimension hotkey1Dimension;
    private static Keybind hotkey1Key;
    private static Dimension hotkey2Dimension;
    private static Keybind hotkey2Key;
    private static Dimension hotkey3Dimension;
    private static Keybind hotkey3Key;
    private static Dimension hotkey4Dimension;
    private static Keybind hotkey4Key;
    private static Dimension hotkey5Dimension;
    private static Keybind hotkey5Key;
    private static Dimension hotkey6Dimension;
    private static Keybind hotkey6Key;
    private static Dimension hotkey7Dimension;
    private static Keybind hotkey7Key;
    private static Dimension hotkey8Dimension;
    private static Keybind hotkey8Key;
    private static Dimension hotkey9Dimension;
    private static Keybind hotkey9Key;
    private static Dimension hotkey10Dimension;
    private static Keybind hotkey10Key;
    private static boolean resizableScalingAutomatic1;
    private static int resizableScalingAutomatic1Percent;
    private static boolean resizableScalingAutomatic2;
    private static int resizableScalingAutomatic2Percent;
    private static boolean resizableScalingAutomatic3;
    private static int resizableScalingAutomatic3Percent;
    private static boolean resizableScalingAutomatic4;
    private static int resizableScalingAutomatic4Percent;
    private static boolean resizableScalingAutomatic5;
    private static int resizableScalingAutomatic5Percent;
    private static boolean resizableScalingAutomatic6;
    private static int resizableScalingAutomatic6Percent;
    private static boolean resizableScalingAutomatic7;
    private static int resizableScalingAutomatic7Percent;
    private static boolean resizableScalingAutomatic8;
    private static int resizableScalingAutomatic8Percent;
    private static boolean resizableScalingAutomatic9;
    private static int resizableScalingAutomatic9Percent;
    private static boolean resizableScalingAutomatic10;
    private static int resizableScalingAutomatic10Percent;
    private static boolean resizableScalingHotkey1;
    private static int resizableScalingHotkey1Percent;
    private static boolean resizableScalingHotkey2;
    private static int resizableScalingHotkey2Percent;
    private static boolean resizableScalingHotkey3;
    private static int resizableScalingHotkey3Percent;
    private static boolean resizableScalingHotkey4;
    private static int resizableScalingHotkey4Percent;
    private static boolean resizableScalingHotkey5;
    private static int resizableScalingHotkey5Percent;
    private static boolean resizableScalingHotkey6;
    private static int resizableScalingHotkey6Percent;
    private static boolean resizableScalingHotkey7;
    private static int resizableScalingHotkey7Percent;
    private static boolean resizableScalingHotkey8;
    private static int resizableScalingHotkey8Percent;
    private static boolean resizableScalingHotkey9;
    private static int resizableScalingHotkey9Percent;
    private static boolean resizableScalingHotkey10;
    private static int resizableScalingHotkey10Percent;
    private static boolean showChatMessage;
    private static MonitorAttribute copyAttribute;
    //------------- End of wall of config vars -------------

    private static GraphicsConfiguration graphicsConfig;
    private static GraphicsDevice currentMonitor;
    private static GraphicsDevice previousMonitor;
    private static GameState currentGameState;
    private static boolean hasProfileChanged; //The default value for a boolean (primitive) is false.
    private static String previousIDstring;
    private static Rectangle previousBounds;
    private static Dimension previousDimensions;
    private static int previousRefreshRate;
    private static final Pattern numericPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    @Getter
    private static int defaultResizableScaling = 50; //Stretched mode plugin default

    @Inject
    private Client client;

    @Inject
    private ClientResizerConfig config;

    @Inject
    private ConfigManager configManager;

    @Inject
    private ClientUI clientUI;

    @Inject
    private KeyManager keyManager;

    @Inject
    private ClientThread clientThread;

    @Override
    public void startUp() throws Exception {
        defaultResizableScaling = configManager.getConfiguration("stretchedmode", "scalingFactor", Integer.class); //Default might be set to 50 initially, but will set to its current value on reset at least. PM Can't do this on value initialization because then configManager hasn't been injected yet.
        updateConfig();
        setDefaultDimensions();
        registerHotkeyListeners();
    }

    @Override
    public void shutDown() throws Exception {
        unregisterHotkeyListeners();
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged configChanged) {
        if (configChanged.getGroup().equals("ClientResizer")) {
            updateConfig();
            if (configChanged.getKey().equals("copyAttribute")) { //Only called when the user changes the config value and hasMonitorChanged
                copyAttributeToClipboard();
            }
        }
    }

    private void updateConfig() {
        resizeAttributeUnchanged = config.resizeAttributeUnchanged();
        resizeWhenConfigProfileChanged = config.resizeWhenConfigProfileChanged();
        autoSize1Dimension = config.autoSize1Dimension();
        autoSize1Attribute = config.autoSize1Attribute();
        autoSize1Value = config.autoSize1Value();
        autoSize2Dimension = config.autoSize2Dimension();
        autoSize2Attribute = config.autoSize2Attribute();
        autoSize2Value = config.autoSize2Value();
        autoSize3Dimension = config.autoSize3Dimension();
        autoSize3Attribute = config.autoSize3Attribute();
        autoSize3Value = config.autoSize3Value();
        autoSize4Dimension = config.autoSize4Dimension();
        autoSize4Attribute = config.autoSize4Attribute();
        autoSize4Value = config.autoSize4Value();
        autoSize5Dimension = config.autoSize5Dimension();
        autoSize5Attribute = config.autoSize5Attribute();
        autoSize5Value = config.autoSize5Value();
        autoSize6Dimension = config.autoSize6Dimension();
        autoSize6Attribute = config.autoSize6Attribute();
        autoSize6Value = config.autoSize6Value();
        autoSize7Dimension = config.autoSize7Dimension();
        autoSize7Attribute = config.autoSize7Attribute();
        autoSize7Value = config.autoSize7Value();
        autoSize8Dimension = config.autoSize8Dimension();
        autoSize8Attribute = config.autoSize8Attribute();
        autoSize8Value = config.autoSize8Value();
        autoSize9Dimension = config.autoSize9Dimension();
        autoSize9Attribute = config.autoSize9Attribute();
        autoSize9Value = config.autoSize9Value();
        autoSize10Dimension = config.autoSize10Dimension();
        autoSize10Attribute = config.autoSize10Attribute();
        autoSize10Value = config.autoSize10Value();
        hotkey1Dimension = config.hotkey1Dimension();
        hotkey1Key = config.hotkey1Key();
        hotkey2Dimension = config.hotkey2Dimension();
        hotkey2Key = config.hotkey2Key();
        hotkey3Dimension = config.hotkey3Dimension();
        hotkey3Key = config.hotkey3Key();
        hotkey4Dimension = config.hotkey4Dimension();
        hotkey4Key = config.hotkey4Key();
        hotkey5Dimension = config.hotkey5Dimension();
        hotkey5Key = config.hotkey5Key();
        hotkey6Dimension = config.hotkey6Dimension();
        hotkey6Key = config.hotkey6Key();
        hotkey7Dimension = config.hotkey7Dimension();
        hotkey7Key = config.hotkey7Key();
        hotkey8Dimension = config.hotkey8Dimension();
        hotkey8Key = config.hotkey8Key();
        hotkey9Dimension = config.hotkey9Dimension();
        hotkey9Key = config.hotkey9Key();
        hotkey10Dimension = config.hotkey10Dimension();
        hotkey10Key = config.hotkey10Key();
        resizableScalingAutomatic1 = config.resizableScalingAutomatic1();
        resizableScalingAutomatic1Percent = config.resizableScalingAutomatic1Percent();
        resizableScalingAutomatic2 = config.resizableScalingAutomatic2();
        resizableScalingAutomatic2Percent = config.resizableScalingAutomatic2Percent();
        resizableScalingAutomatic3 = config.resizableScalingAutomatic3();
        resizableScalingAutomatic3Percent = config.resizableScalingAutomatic3Percent();
        resizableScalingAutomatic4 = config.resizableScalingAutomatic4();
        resizableScalingAutomatic4Percent = config.resizableScalingAutomatic4Percent();
        resizableScalingAutomatic5 = config.resizableScalingAutomatic5();
        resizableScalingAutomatic5Percent = config.resizableScalingAutomatic5Percent();
        resizableScalingAutomatic6 = config.resizableScalingAutomatic6();
        resizableScalingAutomatic6Percent = config.resizableScalingAutomatic6Percent();
        resizableScalingAutomatic7 = config.resizableScalingAutomatic7();
        resizableScalingAutomatic7Percent = config.resizableScalingAutomatic7Percent();
        resizableScalingAutomatic8 = config.resizableScalingAutomatic8();
        resizableScalingAutomatic8Percent = config.resizableScalingAutomatic8Percent();
        resizableScalingAutomatic9 = config.resizableScalingAutomatic9();
        resizableScalingAutomatic9Percent = config.resizableScalingAutomatic9Percent();
        resizableScalingAutomatic10 = config.resizableScalingAutomatic10();
        resizableScalingAutomatic10Percent = config.resizableScalingAutomatic10Percent();
        resizableScalingHotkey1 = config.resizableScalingHotkey1();
        resizableScalingHotkey1Percent = config.resizableScalingHotkey1Percent();
        resizableScalingHotkey2 = config.resizableScalingHotkey2();
        resizableScalingHotkey2Percent = config.resizableScalingHotkey2Percent();
        resizableScalingHotkey3 = config.resizableScalingHotkey3();
        resizableScalingHotkey3Percent = config.resizableScalingHotkey3Percent();
        resizableScalingHotkey4 = config.resizableScalingHotkey4();
        resizableScalingHotkey4Percent = config.resizableScalingHotkey4Percent();
        resizableScalingHotkey5 = config.resizableScalingHotkey5();
        resizableScalingHotkey5Percent = config.resizableScalingHotkey5Percent();
        resizableScalingHotkey6 = config.resizableScalingHotkey6();
        resizableScalingHotkey6Percent = config.resizableScalingHotkey6Percent();
        resizableScalingHotkey7 = config.resizableScalingHotkey7();
        resizableScalingHotkey7Percent = config.resizableScalingHotkey7Percent();
        resizableScalingHotkey8 = config.resizableScalingHotkey8();
        resizableScalingHotkey8Percent = config.resizableScalingHotkey8Percent();
        resizableScalingHotkey9 = config.resizableScalingHotkey9();
        resizableScalingHotkey9Percent = config.resizableScalingHotkey9Percent();
        resizableScalingHotkey10 = config.resizableScalingHotkey10();
        resizableScalingHotkey10Percent = config.resizableScalingHotkey10Percent();
        showChatMessage = config.showChatMessage();
        copyAttribute = config.copyAttribute();
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        currentGameState = gameStateChanged.getGameState();
    }

    @Subscribe
    public void onBeforeRender(BeforeRender beforeRender) {
        //onGameTick only fires while logged in! Use onBeforeRender or onPostClientTick if not logged in!
        if (currentGameState != GameState.LOGGED_IN && currentGameState != GameState.UNKNOWN && currentGameState != GameState.STARTING) {
            monitorCheck();
        }
    }

    @Subscribe
    public void onGameTick(GameTick gameTick) {
        //onGameTick only fires while logged in!
        monitorCheck();
    }

    @Subscribe
    public void onProfileChanged(ProfileChanged profileChanged) {
        updateConfig();
        if (resizeWhenConfigProfileChanged) {
            hasProfileChanged = true; //Attribute doesn't change when switching profiles so use bool as workaround in case "Resize if attribute is unchanged" is disabled.
            resizeClient();
        }
    }

    private void setDefaultDimensions() {
        Dimension[] DimensionsArray = new Dimension[]{autoSize1Dimension, autoSize2Dimension, autoSize3Dimension, autoSize4Dimension, autoSize5Dimension, autoSize6Dimension, autoSize7Dimension, autoSize8Dimension, autoSize9Dimension, autoSize10Dimension, hotkey1Dimension, hotkey2Dimension, hotkey3Dimension, hotkey4Dimension, hotkey5Dimension, hotkey6Dimension, hotkey7Dimension, hotkey8Dimension, hotkey9Dimension, hotkey10Dimension};
        String[] DimensionsStringArray = new String[]{"autoSize1Dimension", "autoSize2Dimension", "autoSize3Dimension", "autoSize4Dimension", "autoSize5Dimension", "autoSize6Dimension", "autoSize7Dimension", "autoSize8Dimension", "autoSize9Dimension", "autoSize10Dimension", "hotkey1Dimension","hotkey2Dimension","hotkey3Dimension","hotkey4Dimension", "hotkey5Dimension","hotkey6Dimension","hotkey7Dimension","hotkey8Dimension","hotkey9Dimension", "hotkey10Dimension"};
        //Set default dimensions to current game size so someone doesn't accidentally set their game size to Dimension(Constants.GAME_FIXED_WIDTH, Constants.GAME_FIXED_HEIGHT)
        for (int i = 0; i < DimensionsArray.length; i++) {
            setDefaultDimension(DimensionsArray[i], DimensionsStringArray[i]);
        }
        //Alternatively, you can move setConfiguration(copyAttribute) and updateConfig to onStartup and probably change the Config to just return:
        //return configManager.getConfiguration("runelite", "gameSize", Dimension.class);
        //Only potential advantage of current application is probably that the user can reset it to 0,0 but then on next restart it gets changed anyway so meh.
        configManager.setConfiguration("ClientResizer", "copyAttribute", MonitorAttribute.Disabled); //reset copyAttribute setting back to disabled on plugin start
        updateConfig(); //configManager.setConfiguration doesn't seem to trigger onConfigChanged?
    }

    private void setDefaultDimension(Dimension dimension, String dimensionName) {
        Dimension defaultDimension = new Dimension(0, 0);
        if (dimension.equals(defaultDimension)) {
            Dimension currentSize = configManager.getConfiguration("runelite", "gameSize", Dimension.class);
            configManager.setConfiguration("ClientResizer", dimensionName, currentSize);
        }
    }

    private void copyAttributeToClipboard() { //Copy value of specified attribute to clipboard so it can be pasted in the String input boxes
        if (copyAttribute != MonitorAttribute.Disabled && currentMonitorValueForAttribute(copyAttribute) != null) {
            String valueToCopy = currentMonitorValueForAttribute(copyAttribute).toString(); //Hush IntelliJ, already checked if currentMonitorValueForAttribute(copyAttribute) != null so .toString() cannot produce a NPE AFAIK...
            if (copyAttribute == MonitorAttribute.Bounds) {
                valueToCopy = valueToCopy.replace("java.awt.Rectangle[", "").replace("]", ""); //remove java.awt.Rectangle[ and ] from string
            }
            if (copyAttribute == MonitorAttribute.Dimensions) {
                valueToCopy = valueToCopy.replace("java.awt.Dimension[", "").replace("]", ""); //remove java.awt.Dimension[ and ] from string
            }
            //Could also leave these in and later do this during processAttributeString(), but this probably makes more sense for the user
            StringSelection stringSelection = new StringSelection(valueToCopy);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }

    private void monitorCheck() {
        graphicsConfig = clientUI.getGraphicsConfiguration();
        currentMonitor = graphicsConfig.getDevice(); // Actually relevant here to refresh the current monitor since I opted to use static variables instead of local variable that update per method.
        if (hasMonitorChanged()) {
            copyAttributeToClipboard();
            resizeClient();
        }
        //PS To get all monitors, you can do:
        //GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //GraphicsDevice[] allMonitors = graphicsEnvironment.getScreenDevices();

        //TODO: potentially add a widget that displays all the MonitorAttributes.
        // However, typing the attributes will likely result in a lot of user error, so opted not to for now and just to use the copy option.
        // If added:
        // Add boolean to Config above copyAttribute (default false)
        // Add to updateConfig()
        // Refresh widget content + repaint if hasMonitorChanged()
        // Potentially add warning if custom chrome is off to widget
    }

    private boolean hasMonitorChanged() {
        //PM If you ever want the resize on client to be turned into a config option, add a previousMonitor == null => return resizeOnStartup probably. Plus don't forget to potentially change stuff like hasAttributeChanged then!
        if (!currentMonitor.equals(previousMonitor)) { //No previousMonitor == null check because unnecessary before equals() call
            previousMonitor = currentMonitor;
            return true;
        }
        previousMonitor = currentMonitor;
        return false;
    }

    private Object currentMonitorValueForAttribute(MonitorAttribute attributeType) { //Return the value for the current monitor for the specific MonitorAttribute
        switch (attributeType) {
            case IDstring:
                return currentMonitor.getIDstring();
            case Bounds:
                return graphicsConfig.getBounds();
            case Dimensions:
                return new Dimension(currentMonitor.getDisplayMode().getWidth(), currentMonitor.getDisplayMode().getHeight());
            case RefreshRate:
                return currentMonitor.getDisplayMode().getRefreshRate();
        }
        return null;
    }

    private void resizeClient() {
        //Update arrays to most recent values
        MonitorAttribute[] AttributesArray = new MonitorAttribute[]{autoSize1Attribute, autoSize2Attribute, autoSize3Attribute, autoSize4Attribute, autoSize5Attribute, autoSize6Attribute, autoSize7Attribute, autoSize8Attribute, autoSize9Attribute, autoSize10Attribute};
        String[] ValuesStringArray = new String[]{autoSize1Value, autoSize2Value, autoSize3Value, autoSize4Value, autoSize5Value, autoSize6Value, autoSize7Value, autoSize8Value, autoSize9Value, autoSize10Value};
        Dimension[] DimensionsArray = new Dimension[]{autoSize1Dimension, autoSize2Dimension, autoSize3Dimension, autoSize4Dimension, autoSize5Dimension, autoSize6Dimension, autoSize7Dimension, autoSize8Dimension, autoSize9Dimension, autoSize10Dimension};
        Boolean[] resizableScalingBoolArray = new Boolean[]{resizableScalingAutomatic1, resizableScalingAutomatic2, resizableScalingAutomatic3, resizableScalingAutomatic4, resizableScalingAutomatic5, resizableScalingAutomatic6, resizableScalingAutomatic7, resizableScalingAutomatic8, resizableScalingAutomatic9, resizableScalingAutomatic10};
        Integer[] resizableScalingPercentArray = new Integer[]{resizableScalingAutomatic1Percent, resizableScalingAutomatic2Percent, resizableScalingAutomatic3Percent, resizableScalingAutomatic4Percent, resizableScalingAutomatic5Percent, resizableScalingAutomatic6Percent, resizableScalingAutomatic7Percent, resizableScalingAutomatic8Percent, resizableScalingAutomatic9Percent, resizableScalingAutomatic10Percent};

        for (int i = 0; i < AttributesArray.length; i++) {
            //if MonitorAttribute != disabled, the Value/String input by the user is properly processed (+ has proper format) and if processed Value/String matches value of current monitor, then setGameSize to the inputted Dimension
            if (AttributesArray[i] != MonitorAttribute.Disabled &&
                    processAttributeString(AttributesArray[i], ValuesStringArray[i]) != null &&
                    processAttributeString(AttributesArray[i], ValuesStringArray[i]).equals(currentMonitorValueForAttribute(AttributesArray[i]))) {
                if (resizeAttributeUnchanged) {
                    setGameSize(DimensionsArray[i]);
                    if (resizableScalingBoolArray[i]) {
                        setResizableScaling(resizableScalingPercentArray[i]);
                    }
                }
                if (!resizeAttributeUnchanged && (hasAttributeChanged(AttributesArray[i]) || hasProfileChanged)) { //If the user disables the option to resize when the current monitor has changed but the value of the specified attribute (Dimensions or Refresh Rate) is the same
                    setGameSize(DimensionsArray[i]);
                    if (resizableScalingBoolArray[i]) {
                        setResizableScaling(resizableScalingPercentArray[i]);
                    }
                }
            }
        }
        hasProfileChanged = false;
        updatePreviousAttributes(); //To remember attributes so a proper comparison can be done with the previous attributes
    }

    private Object processAttributeString(MonitorAttribute attributeType, String attributeString) {
        if (!(attributeString == null || attributeString.isEmpty() || attributeType == MonitorAttribute.Disabled)) {
            attributeString = attributeString.replaceAll("\\s+", ""); //Removes all whitespaces and non-visible characters (e.g., tab, \n)
            switch (attributeType) {
                case IDstring:
                    return attributeString;
                case Bounds:
                    String[] splitAttributeBoundsString = attributeString.split(",");
                    if (splitAttributeBoundsString.length == 4) { //Faster/more efficient than to try and catch an ArrayIndexOutOfBoundsException probably
                        //Remove all the non-numbers from the string
                        String xBounds = splitAttributeBoundsString[0].replace("x=", "").replace("y=", "").replace("width=", "").replace("height=", "");
                        String yBounds = splitAttributeBoundsString[1].replace("x=", "").replace("y=", "").replace("width=", "").replace("height=", "");
                        String widthBounds = splitAttributeBoundsString[2].replace("x=", "").replace("y=", "").replace("width=", "").replace("height=", "");
                        String heightBounds = splitAttributeBoundsString[3].replace("x=", "").replace("y=", "").replace("width=", "").replace("height=", "");
                        if (isNumeric(xBounds) && isNumeric(yBounds) && isNumeric(widthBounds) && isNumeric(heightBounds)) { //Faster/more efficient than to try and catch a MalformedInputException probably
                            return new Rectangle(Integer.parseInt(xBounds), Integer.parseInt(yBounds), Integer.parseInt(widthBounds), Integer.parseInt(heightBounds));
                        }
                    }
                    break;
                case Dimensions:
                    String[] splitAttributeDimensionsString = attributeString.split(",");
                    if (splitAttributeDimensionsString.length == 2) { //Faster/more efficient than to try and catch an ArrayIndexOutOfBoundsException probably
                        String widthDimensions = splitAttributeDimensionsString[0].replace("width=", "").replace("height=", "");
                        String heightDimensions = splitAttributeDimensionsString[1].replace("width=", "").replace("height=", "");
                        if (isNumeric(widthDimensions) && isNumeric(heightDimensions)) {
                            return new Dimension(Integer.parseInt(widthDimensions), Integer.parseInt(heightDimensions));
                        }
                    }
                    break;
                case RefreshRate:
                    if (isNumeric(attributeString)) {
                        return Integer.parseInt(attributeString);
                    }
                    break;
            }
        }
        return null; //If incorrectly formatted or if MonitorAttribute = disabled => return null
    }

    private boolean isNumeric(String inputString) {
        if (inputString == null) {
            return false;
        }
        return numericPattern.matcher(inputString).matches();
    }

    private void setGameSize(Dimension dimension) {
        //Processing probably irrelevant since ClientUI.java contains the same code, but why not.
        int processedWidth = Math.max(Math.min(dimension.width, 7680), Constants.GAME_FIXED_WIDTH);
        int processedHeight = Math.max(Math.min(dimension.height, 2160), Constants.GAME_FIXED_HEIGHT);
        Dimension processedGameSize = new Dimension(processedWidth, processedHeight);
        Dimension currentSize = configManager.getConfiguration("runelite", "gameSize", Dimension.class);
        //Check if current size in config does not equal the size the config is going to get set to.
        //Otherwise, every time it'd match but not resize, a chat message would be spammed.
        //If client dimensions are unlocked and the edges are dragged, setting the config to the same value as already in there does not change it.
        //Thus doing configManager.setConfiguration("runelite", "gameSize", processedGameSize) while currentSize.equals(processedGameSize) does nothing, meaning that doing that'd be pointless
        if (!currentSize.equals(processedGameSize)) {
            configManager.setConfiguration("runelite", "gameSize", processedGameSize);
            //mouseReleased/mouseEntered doesn't report when clicked on undecorated title bar (custom chrome off)
            //While dragging the client with custom chrome off, the resize gets briefly applied but then removed.
            //Even bad workarounds like applying +1 and then back to preferable dimension when mouseReleased or mouseEntered (cursor can't be in title bar), didn't work. => Setting changed but client did not resize. Does still work on Win+Arrow key though for some reason...
            //Same for injecting the client Applet and checking the size of that, then keep setting/switching the value till the Applet dimension = preferred dimension

            if (showChatMessage) {
                //client.addChatMessage has to be called on clientThread. Doesn't cause any error if not called on client.getGameState() == GameState.LOGGED_IN
                clientThread.invokeLater(() -> {
                    client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Your RuneLite game size / client size was changed by the Client Resizer plugin. Check your config if you'd like to change this.", "");
                });
            }
        }
    }

    private void setResizableScaling(int resizableScalingPercent) {
        int currentResizableScalingPercent = configManager.getConfiguration("stretchedmode", "scalingFactor", Integer.class);
        //Check if current resizable scaling percent in config does not equal the resizable scaling percent the config is going to get set to.
        //Otherwise, every time it'd match but not resize, a chat message would be spammed.
        if (currentResizableScalingPercent != resizableScalingPercent) {
            configManager.setConfiguration("stretchedmode", "scalingFactor", resizableScalingPercent);

            if (showChatMessage) {
                //client.addChatMessage has to be called on clientThread. Doesn't cause any error if not called on client.getGameState() == GameState.LOGGED_IN
                clientThread.invokeLater(() -> {
                    client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Your resizable scaling (stretched mode plugin) was changed by the Client Resizer plugin. Check your config if you'd like to change this.", "");
                });
            }
        }
    }

    private boolean hasAttributeChanged(MonitorAttribute inputAttribute) { //Check if attribute has changed (true on startup due to null check)
        //Potentially change if you want to make the game size changing on startup configurable
        return previousAttributeValue(inputAttribute) == null || !previousAttributeValue(inputAttribute).equals(currentMonitorValueForAttribute(inputAttribute));
        //Hush, IntelliJ. I don't see how previousAttributeValue(inputAttribute).equals() can produce an NPE since the return statement is already true if it's null, so the condition after || shouldn't even be checked then...
    }

    private Object previousAttributeValue(MonitorAttribute inputAttribute) { //Return previous value of the selected attribute
        switch (inputAttribute) {
            case IDstring:
                return previousIDstring;
            case Bounds:
                return previousBounds;
            case Dimensions:
                return previousDimensions;
            case RefreshRate:
                return previousRefreshRate;
        }
        return null;
    }

    private void updatePreviousAttributes() {
        previousIDstring = (String) currentMonitorValueForAttribute(MonitorAttribute.IDstring);
        previousBounds = (Rectangle) currentMonitorValueForAttribute(MonitorAttribute.Bounds);
        previousDimensions = (Dimension) currentMonitorValueForAttribute(MonitorAttribute.Dimensions);
        previousRefreshRate = (int) currentMonitorValueForAttribute(MonitorAttribute.RefreshRate);
    }

    private void registerHotkeyListeners() {
        keyManager.registerKeyListener(hotkeyListener1);
        keyManager.registerKeyListener(hotkeyListener2);
        keyManager.registerKeyListener(hotkeyListener3);
        keyManager.registerKeyListener(hotkeyListener4);
        keyManager.registerKeyListener(hotkeyListener5);
        keyManager.registerKeyListener(hotkeyListener6);
        keyManager.registerKeyListener(hotkeyListener7);
        keyManager.registerKeyListener(hotkeyListener8);
        keyManager.registerKeyListener(hotkeyListener9);
        keyManager.registerKeyListener(hotkeyListener10);
    }

    private void unregisterHotkeyListeners() {
        keyManager.unregisterKeyListener(hotkeyListener1);
        keyManager.unregisterKeyListener(hotkeyListener2);
        keyManager.unregisterKeyListener(hotkeyListener3);
        keyManager.unregisterKeyListener(hotkeyListener4);
        keyManager.unregisterKeyListener(hotkeyListener5);
        keyManager.unregisterKeyListener(hotkeyListener6);
        keyManager.unregisterKeyListener(hotkeyListener7);
        keyManager.unregisterKeyListener(hotkeyListener8);
        keyManager.unregisterKeyListener(hotkeyListener9);
        keyManager.unregisterKeyListener(hotkeyListener10);
    }

    // ------------- Wall of HotkeyListeners -------------
    private final HotkeyListener hotkeyListener1 = new HotkeyListener(() -> hotkey1Key) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setGameSize(hotkey1Dimension);
            if (resizableScalingHotkey1) {
                setResizableScaling(resizableScalingHotkey1Percent);
            }
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListener2 = new HotkeyListener(() -> hotkey2Key) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setGameSize(hotkey2Dimension);
            if (resizableScalingHotkey2) {
                setResizableScaling(resizableScalingHotkey2Percent);
            }
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListener3 = new HotkeyListener(() -> hotkey3Key) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setGameSize(hotkey3Dimension);
            if (resizableScalingHotkey3) {
                setResizableScaling(resizableScalingHotkey3Percent);
            }
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListener4 = new HotkeyListener(() -> hotkey4Key) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setGameSize(hotkey4Dimension);
            if (resizableScalingHotkey4) {
                setResizableScaling(resizableScalingHotkey4Percent);
            }
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListener5 = new HotkeyListener(() -> hotkey5Key) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setGameSize(hotkey5Dimension);
            if (resizableScalingHotkey5) {
                setResizableScaling(resizableScalingHotkey5Percent);
            }
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListener6 = new HotkeyListener(() -> hotkey6Key) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setGameSize(hotkey6Dimension);
            if (resizableScalingHotkey6) {
                setResizableScaling(resizableScalingHotkey6Percent);
            }
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListener7 = new HotkeyListener(() -> hotkey7Key) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setGameSize(hotkey7Dimension);
            if (resizableScalingHotkey7) {
                setResizableScaling(resizableScalingHotkey7Percent);
            }
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListener8 = new HotkeyListener(() -> hotkey8Key) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setGameSize(hotkey8Dimension);
            if (resizableScalingHotkey8) {
                setResizableScaling(resizableScalingHotkey8Percent);
            }
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListener9 = new HotkeyListener(() -> hotkey9Key) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setGameSize(hotkey9Dimension);
            if (resizableScalingHotkey9) {
                setResizableScaling(resizableScalingHotkey9Percent);
            }
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListener10 = new HotkeyListener(() -> hotkey10Key) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setGameSize(hotkey10Dimension);
            if (resizableScalingHotkey10) {
                setResizableScaling(resizableScalingHotkey10Percent);
            }
        }

        @Override
        public void hotkeyReleased() {
        }
    };
    // ------------- End of wall of HotkeyListeners -------------

    @Provides
    ClientResizerConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(ClientResizerConfig.class);
    }
}