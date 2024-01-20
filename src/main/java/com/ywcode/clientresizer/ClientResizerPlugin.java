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

import javax.swing.*;
import java.awt.*;
import java.awt.Point;
import java.awt.datatransfer.*;
import java.util.regex.*;

@Slf4j
@PluginDescriptor(
        name = "Client Resizer",
        description = "Allows for automatic or hotkey-based resizing, or repositioning of the client.",
        tags = {"client,resize,automatic,pixels,refresh rate,game size,size,screen size,monitor,display,screen,autoresize,hotkey,hot key,stretched mode,resizable scaling,scaling,position,reposition,location"}
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
    private static int hotkey1PositionX;
    private static int hotkey1PositionY;
    private static Keybind hotkey1PositionKey;
    private static int hotkey2PositionX;
    private static int hotkey2PositionY;
    private static Keybind hotkey2PositionKey;
    private static int hotkey3PositionX;
    private static int hotkey3PositionY;
    private static Keybind hotkey3PositionKey;
    private static int hotkey4PositionX;
    private static int hotkey4PositionY;
    private static Keybind hotkey4PositionKey;
    private static int hotkey5PositionX;
    private static int hotkey5PositionY;
    private static Keybind hotkey5PositionKey;
    private static int hotkey6PositionX;
    private static int hotkey6PositionY;
    private static Keybind hotkey6PositionKey;
    private static int hotkey7PositionX;
    private static int hotkey7PositionY;
    private static Keybind hotkey7PositionKey;
    private static int hotkey8PositionX;
    private static int hotkey8PositionY;
    private static Keybind hotkey8PositionKey;
    private static int hotkey9PositionX;
    private static int hotkey9PositionY;
    private static Keybind hotkey9PositionKey;
    private static int hotkey10PositionX;
    private static int hotkey10PositionY;
    private static Keybind hotkey10PositionKey;
    private static boolean containInScreenTop;
    private static boolean containInScreenRight;
    private static boolean containInScreenBottom;
    private static boolean containInScreenLeft;
    private static int containInScreenTopOffset;
    private static int containInScreenRightOffset;
    private static int containInScreenBottomOffset;
    private static int containInScreenLeftOffset;
    private static int containInScreenSnapBackPx;
    private static boolean showChatMessage;
    private static boolean showChatMessageReposition;
    private static boolean showChatMessageContain; //todo: use this boolean
    private static MonitorAttribute copyAttribute;
    private static boolean copyPosition;
    //------------- End of wall of config vars -------------

    private static GraphicsConfiguration graphicsConfig;
    private static GraphicsDevice currentMonitor;
    private static GraphicsDevice previousMonitor;
    private static GraphicsConfiguration previousGraphicsConfig;
    private static GameState currentGameState;
    private static boolean hasProfileChanged; //The default value for a boolean (primitive) is false.
    private static String previousIDstring;
    private static Rectangle previousBounds;
    private static Dimension previousDimensions;
    private static int previousRefreshRate;
    private static final Pattern numericPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    @Getter
    private static int defaultResizableScaling = 50; //Stretched mode plugin default
    private static long previousNanoTime; // Default value is 0
    private static final long TICK_IN_NANOSECONDS = 600000000;
    private static boolean resizeChatMessageFlag; //Default = false
    private static boolean scaleChatMessageFlag; //Default = false
    private static boolean repositionChatMessageFlag; //Default = false
    private static boolean containInScreenChatMessageFlag; //Default = false

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

    /* todo: check if you can do a fake contain in screen
        3) check if you should use componentlistener oncomponent moved (potentially an ongametick flag to make less visually jarring via config setting)or ongametick for contain in screen
        3a) If u do the ongametick contain, it should do this before the hasmonitorchanged check and use the previousmonitor set in there to get bounds etc.
        3b)  Also think about when this is still null etc. Maybe also when getbounds is null but idk.
    * Make offset 4 integer boxes probs and add to those:
    @Units(
                Units.PIXELS
        )
    * Override current always in screen probs via reposition hoykey option (add this to readme as well, maybe to config category as well or always keep in shizzel).
    * Potentially add int config value (default 100000 of zo) to make it hard contain in screen or soft contain in screen. Add to it probs:
        @Units(
                Units.PIXELS
        )
    * add chat message but only send the contain in screen chatmessage every 30 seconds or so
    * En fix je eigen autismo 1 pixel thing left monitor (mss al fixed met een soft contain in screen van bijv 10 px?)
    * check how it interacts with dragging, with hotkeys, with hotkeybased repositioning
    * check how it interacts with restarting the client (remember client position; does it reset it to default or no)
    * add to tags 2x
    * add to readme changelog, options, text above, instructions

        //System.out.println("size: "+topFrame.getSize());
        //System.out.println("bounds: "+topFrame.getBounds());
        //System.out.println("width: "+topFrame.getBounds().width);
        //System.out.println("bounds: "+topFrame.getBounds().height);

    todo: check mouselistener mousedragged, check componentlistener componentmoved etc and then set the size +1 -1 1 gametick afterwards probs?
    potentially check the real client size as well for this (e.g. can you always add something to game size, or even get the game size directly? by e.g. doing getChild?)
    todo: test 2x and 0.5 scale in rl configure! zowel voor automatic resizing als contain in screen! how does this respond the pixels? are the bounds etc 2x as well or still 1x?
    todo: edit readme screenshots probs

    todo: change description in case also contain in screen/monitor/edge/snap/snap back/offeset/whatever name is added
    todo: maybe try to add some very hacky antidrag using component or mouselisteners but mehhh
    todo: if you used mouselistener ondrag, check if it works with custom chrome off or not (add this to readme!) and check if it doesn't if u drag ingame or the sidebar
    todo: set boolean in startup if customChrome is enabled or not based on the null. if custom chrome active: Add componentlistener for componentmoved topClientFrame and run monitorCheck in there probs. If customChromeEnabled => do not run monitorCheck in onGameTick and onBeforeRender. This this out. If so, edit the readme! Check of dit met een right offset van 8 dan nog steeds je autistische blauwe rand shit fixt
     */

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
        hotkey1PositionX = config.hotkey1PositionX();
        hotkey1PositionY = config.hotkey1PositionY();
        hotkey1PositionKey = config.hotkey1PositionKey();
        hotkey2PositionX = config.hotkey2PositionX();
        hotkey2PositionY = config.hotkey2PositionY();
        hotkey2PositionKey = config.hotkey2PositionKey();
        hotkey3PositionX = config.hotkey3PositionX();
        hotkey3PositionY = config.hotkey3PositionY();
        hotkey3PositionKey = config.hotkey3PositionKey();
        hotkey4PositionX = config.hotkey4PositionX();
        hotkey4PositionY = config.hotkey4PositionY();
        hotkey4PositionKey = config.hotkey4PositionKey();
        hotkey5PositionX = config.hotkey5PositionX();
        hotkey5PositionY = config.hotkey5PositionY();
        hotkey5PositionKey = config.hotkey5PositionKey();
        hotkey6PositionX = config.hotkey6PositionX();
        hotkey6PositionY = config.hotkey6PositionY();
        hotkey6PositionKey = config.hotkey6PositionKey();
        hotkey7PositionX = config.hotkey7PositionX();
        hotkey7PositionY = config.hotkey7PositionY();
        hotkey7PositionKey = config.hotkey7PositionKey();
        hotkey8PositionX = config.hotkey8PositionX();
        hotkey8PositionY = config.hotkey8PositionY();
        hotkey8PositionKey = config.hotkey8PositionKey();
        hotkey9PositionX = config.hotkey9PositionX();
        hotkey9PositionY = config.hotkey9PositionY();
        hotkey9PositionKey = config.hotkey9PositionKey();
        hotkey10PositionX = config.hotkey10PositionX();
        hotkey10PositionY = config.hotkey10PositionY();
        hotkey10PositionKey = config.hotkey10PositionKey();
        containInScreenTop = config.containInScreenTop();
        containInScreenRight = config.containInScreenRight();
        containInScreenBottom = config.containInScreenBottom();
        containInScreenLeft = config.containInScreenLeft();
        containInScreenTopOffset = config.containInScreenTopOffset();
        containInScreenRightOffset = config.containInScreenRightOffset();
        containInScreenBottomOffset = config.containInScreenBottomOffset();
        containInScreenLeftOffset = config.containInScreenLeftOffset();
        containInScreenSnapBackPx = config.containInScreenSnapBackPx();
        showChatMessage = config.showChatMessage();
        showChatMessageReposition = config.showChatMessageReposition();
        showChatMessageContain = config.showChatMessageContain();
        copyAttribute = config.copyAttribute();
        copyPosition = config.copyPosition();
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        currentGameState = gameStateChanged.getGameState();
    }

    @Subscribe
    public void onBeforeRender(BeforeRender beforeRender) {
        //onGameTick only fires while logged in! Use onBeforeRender or onPostClientTick if not logged in!
        if (currentGameState != GameState.LOGGED_IN && currentGameState != GameState.UNKNOWN && currentGameState != GameState.STARTING) {
            //Simulate gametick behavior to make BeforeRender and GameTick feel more similar.
            long currentNanoTime = System.nanoTime();
            if (currentNanoTime - previousNanoTime > TICK_IN_NANOSECONDS) { //It will fire immediately once when starting the plugin in the right gamestate (desired behavior IMO) and also immediately when logging out, but that is fine probs.
                monitorCheck();
                previousNanoTime = currentNanoTime;
            }
        }
    }

    @Subscribe
    public void onGameTick(GameTick gameTick) {
        //onGameTick only fires while logged in!
        checkChatMessageFlags();
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
        configManager.setConfiguration("ClientResizer", "copyPosition", "False"); //reset copyPosition setting back to disabled on plugin start
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
        //Alternatively use client.getCanvas().getGraphicsConfiguration() if this breaks!
        graphicsConfig = clientUI.getGraphicsConfiguration();
        currentMonitor = graphicsConfig.getDevice(); // Actually relevant here to refresh the current monitor since I opted to use static variables instead of local variable that update per method.
        containInScreen(); //Contain before refreshing monitors in hasMonitorChanged!
        if (hasMonitorChanged()) {
            copyAttributeToClipboard();
            resizeClient();
        }
        copyPositionToClipboard(); // Already checks if the boolean config var is enabled. Should not only run when the monitor has changed.
        previousGraphicsConfig = graphicsConfig;
        //PS To get all monitors, you can do:
        //GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //GraphicsDevice[] allMonitors = graphicsEnvironment.getScreenDevices();

        //Potentially add a widget that displays all the MonitorAttributes.
        // However, typing the attributes will likely result in a lot of user error, so opted not to for now and just to use the copy option.
        // If added:
        // Add boolean to Config above copyAttribute (default false)
        // Add to updateConfig()
        // Refresh widget content + repaint if hasMonitorChanged()
        // Potentially add warning if custom chrome is off to widget
    }

    private void containInScreen() {
        //Hacky way to somewhat emulate contain in screen.
        if (containInScreenTop || containInScreenRight || containInScreenBottom || containInScreenLeft) {
            //if contain in screen is enabled
            if (previousGraphicsConfig != null && previousGraphicsConfig.getBounds() != null) { //previousGraphicsConfig doesn't exist in the beginning. getBounds != null is probs superfluous or probs not working the way I intend it, but maybe it'll help in case a display gets disconnected or something weird?
                //Get the screen bounds. Since pixels should not contain decimals, just typecasting to int or using Double.intValue() is used instead of using Math.round and then typecasting that long to an int.
                //Point is (x,y) with 0,0 being in the top left corner!
                Rectangle previousScreenBounds = previousGraphicsConfig.getBounds();
                int screenBoundsXLeft = (int) previousScreenBounds.getX();
                int screenBoundsXRight = screenBoundsXLeft + (int) previousScreenBounds.getWidth();
                int screenBoundsYTop = (int) previousScreenBounds.getY();
                int screenBoundsYBottom = screenBoundsYTop + (int) previousScreenBounds.getHeight();
                //Add the offsets as configured in the config
                screenBoundsXLeft = screenBoundsXLeft - containInScreenLeftOffset;
                screenBoundsXRight = screenBoundsXRight + containInScreenRightOffset;
                screenBoundsYTop = screenBoundsYTop - containInScreenTopOffset;
                screenBoundsYBottom = screenBoundsYBottom + containInScreenBottomOffset;

                //Get the top JFrame to get the clientBounds
                JFrame topFrameClient = (JFrame) SwingUtilities.getWindowAncestor(client.getCanvas());
                int clientWidth = topFrameClient.getWidth();
                int clientHeight = topFrameClient.getHeight();

                //Get the client bounds
                //Alternatively use e.g. int clientBoundsXLeft = (int) topFrameClient.getBounds().getX();
                int clientBoundsXLeft = topFrameClient.getX();
                int clientBoundsXRight = clientBoundsXLeft + clientWidth;
                int clientBoundsYTop = topFrameClient.getY();
                int clientBoundsYBottom = clientBoundsYTop + clientHeight;

                boolean shouldReposition = false; //Should we reposition?
                int coordsToRepositionToX = clientBoundsXLeft; //Coords to reposition to if we need to. Set current values in case the contain option is not enabled for this side.
                int coordsToRepositionToY = clientBoundsYTop; //Coords to reposition to if we need to. Set current values in case the contain option is not enabled for this side.

                //if contain is enabled and the client is outside the screenbounds (+ config offset) => set the coords to reposition to and set the boolean so we know we need to reposition
                //If the client is too big to even fit on the screen... Let's align the top right side with the screen so you can still press the x and access the config if all 4 contain options are enabled (so the order below is based on that)
                //Point is (x,y) with 0,0 being in the top left corner! 1920,1080 or smth like that is right bottom corner
                if (containInScreenLeft && clientBoundsXLeft < screenBoundsXLeft) {
                    coordsToRepositionToX = screenBoundsXLeft;
                    shouldReposition = true;
                }
                if (containInScreenRight && clientBoundsXRight > screenBoundsXRight) {
                    coordsToRepositionToX = screenBoundsXRight - clientWidth;
                    shouldReposition = true;
                }
                if (containInScreenBottom && clientBoundsYBottom > screenBoundsYBottom) {
                    coordsToRepositionToY = screenBoundsYBottom - clientHeight;
                    shouldReposition = true;
                }
                if (containInScreenTop && clientBoundsYTop < screenBoundsYTop) {
                    coordsToRepositionToY = screenBoundsYTop;
                    shouldReposition = true;
                }

                //If we need to reposition, then reposition to the new coords
                if (shouldReposition) {
                    if (showChatMessageContain) {
                        sendGameChatMessage(ResizerMessageType.CONTAIN_IN_SCREEN);
                    }
                    topFrameClient.setLocation(coordsToRepositionToX, coordsToRepositionToY);
                }

                System.out.println("screenbounds (x, xbottom, y, ybottom): "+screenBoundsXLeft+", "+screenBoundsXRight + ", "+screenBoundsYTop + ", "+screenBoundsYBottom);
                System.out.println("clientbounds (x, xbottom, y, ybottom): "+clientBoundsXLeft+", "+clientBoundsXRight + ", "+clientBoundsYTop + ", "+clientBoundsYBottom);

            }
        }
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
        //Process the attributes that are entered as string, return null if incorrectly formatted or MonitorAttribute.Disabled
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
                sendGameChatMessage(ResizerMessageType.RESIZE);
            }
        }
    }

    private void sendGameChatMessage(ResizerMessageType type) {
        //Send a chat message and if not logged in (i.e. resizing client upon opening the client), set a flag to send a message when the player logs in.
        //type = the type of message (are we resizing, are we scaling (stretched mode), are we repositioning, are we containing in screen etc.)
        switch (type) {
            case RESIZE:
                if (currentGameState == GameState.LOGGED_IN || currentGameState == GameState.LOADING) {
                    actuallySendMessage("Your RuneLite game size / client size was changed by the Client Resizer plugin. Check your config if you'd like to change this.");
                } else {
                    resizeChatMessageFlag = true;
                }
                break;
            case SCALE:
                if (currentGameState == GameState.LOGGED_IN || currentGameState == GameState.LOADING) {
                    actuallySendMessage("Your resizable scaling (stretched mode plugin) was changed by the Client Resizer plugin. Check your config if you'd like to change this.");
                } else {
                    scaleChatMessageFlag = true;
                }
                break;
            case REPOSITION:
                if (currentGameState == GameState.LOGGED_IN || currentGameState == GameState.LOADING) {
                    actuallySendMessage("Your client was repositioned by the Client Resizer plugin. Check your config if you'd like to change this.");
                } else {
                    repositionChatMessageFlag = true;
                }
                break;
            case CONTAIN_IN_SCREEN:
                if (currentGameState == GameState.LOGGED_IN || currentGameState == GameState.LOADING) {
                    actuallySendMessage("Your client was contained in the screen by the Client Resizer plugin. Check your config if you'd like to change this.");
                } else {
                    containInScreenChatMessageFlag = true;
                }
        }
    }

    private void actuallySendMessage(String message) {
        //Used in the method above, which actually sends the message
        //client.addChatMessage has to be called on clientThread. Doesn't cause any error if not called on client.getGameState() == GameState.LOGGED_IN
        clientThread.invokeLater(() -> {
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", message, "");
        });
    }

    private void setResizableScaling(int resizableScalingPercent) {
        int currentResizableScalingPercent = configManager.getConfiguration("stretchedmode", "scalingFactor", Integer.class);
        //Check if current resizable scaling percent in config does not equal the resizable scaling percent the config is going to get set to.
        //Otherwise, every time it'd match but not resize, a chat message would be spammed.
        if (currentResizableScalingPercent != resizableScalingPercent) {
            configManager.setConfiguration("stretchedmode", "scalingFactor", resizableScalingPercent);

            if (showChatMessage) {
                sendGameChatMessage(ResizerMessageType.SCALE);
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
        //Updates the previous attributes.
        previousIDstring = (String) currentMonitorValueForAttribute(MonitorAttribute.IDstring);
        previousBounds = (Rectangle) currentMonitorValueForAttribute(MonitorAttribute.Bounds);
        previousDimensions = (Dimension) currentMonitorValueForAttribute(MonitorAttribute.Dimensions);
        previousRefreshRate = (int) currentMonitorValueForAttribute(MonitorAttribute.RefreshRate);
    }

    private void copyPositionToClipboard() {
        //Copies the position of the client to the clipboard.
        //Makes dragging the client a tad laggy if done in onBeforeRender, but this gets solved by simulating gameticks.
        if (copyPosition) {
            try {
                //Get topJFrame and get position
                JFrame topFrameClient = (JFrame) SwingUtilities.getWindowAncestor(client.getCanvas());
                Point currentPosition = topFrameClient.getLocation();
                //toString position, remove part of the string, set it to a string selection, set the clipboard content
                String valueToCopy = currentPosition.toString();
                valueToCopy = valueToCopy.replace("java.awt.Point[", "").replace("]", ""); //remove java.awt.Point[ and ] from string
                StringSelection stringSelection = new StringSelection(valueToCopy);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            } catch (IllegalStateException ignored) {
                //Ignore java.lang.IllegalStateException: cannot open system clipboard
            }
        }
    }

    private void setClientPosition(int pointX, int pointY) {
        //Sets the client position
        JFrame topFrameClient = (JFrame) SwingUtilities.getWindowAncestor(client.getCanvas());
        //To reset to middle, use:
        //topFrameClient.setLocationRelativeTo(topFrame.getOwner());

        //TODO: potentially add automatic repositioning of the client like automatic resizing, but this might automatically throw the client offscreen if configured very incorrectly.
        // Thus, for let's not add this for now. Can reconsider in the future.
        // If this is added, check how you check how you did resizeClient() (copy most of it probs), use processAtributeString, and check how you did setGameSize (copy most of it probs).

        if (showChatMessageReposition) {
            sendGameChatMessage(ResizerMessageType.REPOSITION);
        }
        topFrameClient.setLocation(pointX, pointY);
    }

    private void checkChatMessageFlags() {
        //Check if any of the message flags are set to true. If so, send the message and disable the flag.
        if (resizeChatMessageFlag) {
            resizeChatMessageFlag = false;
            sendGameChatMessage(ResizerMessageType.RESIZE);
        }
        if (scaleChatMessageFlag) {
            scaleChatMessageFlag = false;
            sendGameChatMessage(ResizerMessageType.SCALE);
        }
        if (repositionChatMessageFlag) {
            repositionChatMessageFlag = false;
            sendGameChatMessage(ResizerMessageType.REPOSITION);
        }
        if (containInScreenChatMessageFlag) {
            containInScreenChatMessageFlag = false;
            sendGameChatMessage(ResizerMessageType.CONTAIN_IN_SCREEN);
        }
    }

    private void registerHotkeyListeners() {
        //Called in onStartuo to register the hotkeylisteners.
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
        keyManager.registerKeyListener(hotkeyListenerPosition1);
        keyManager.registerKeyListener(hotkeyListenerPosition2);
        keyManager.registerKeyListener(hotkeyListenerPosition3);
        keyManager.registerKeyListener(hotkeyListenerPosition4);
        keyManager.registerKeyListener(hotkeyListenerPosition5);
        keyManager.registerKeyListener(hotkeyListenerPosition6);
        keyManager.registerKeyListener(hotkeyListenerPosition7);
        keyManager.registerKeyListener(hotkeyListenerPosition8);
        keyManager.registerKeyListener(hotkeyListenerPosition9);
        keyManager.registerKeyListener(hotkeyListenerPosition10);
    }

    private void unregisterHotkeyListeners() {
        //Called in onShutDown to unregister all hotkeylisteners.
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
        keyManager.unregisterKeyListener(hotkeyListenerPosition1);
        keyManager.unregisterKeyListener(hotkeyListenerPosition2);
        keyManager.unregisterKeyListener(hotkeyListenerPosition3);
        keyManager.unregisterKeyListener(hotkeyListenerPosition4);
        keyManager.unregisterKeyListener(hotkeyListenerPosition5);
        keyManager.unregisterKeyListener(hotkeyListenerPosition6);
        keyManager.unregisterKeyListener(hotkeyListenerPosition7);
        keyManager.unregisterKeyListener(hotkeyListenerPosition8);
        keyManager.unregisterKeyListener(hotkeyListenerPosition9);
        keyManager.unregisterKeyListener(hotkeyListenerPosition10);
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

    private final HotkeyListener hotkeyListenerPosition1 = new HotkeyListener(() -> hotkey1PositionKey) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setClientPosition(hotkey1PositionX, hotkey1PositionY);
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListenerPosition2 = new HotkeyListener(() -> hotkey2PositionKey) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setClientPosition(hotkey2PositionX, hotkey2PositionY);
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListenerPosition3 = new HotkeyListener(() -> hotkey3PositionKey) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setClientPosition(hotkey3PositionX, hotkey3PositionY);
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListenerPosition4 = new HotkeyListener(() -> hotkey4PositionKey) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setClientPosition(hotkey4PositionX, hotkey4PositionY);
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListenerPosition5 = new HotkeyListener(() -> hotkey5PositionKey) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setClientPosition(hotkey5PositionX, hotkey5PositionY);
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListenerPosition6 = new HotkeyListener(() -> hotkey6PositionKey) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setClientPosition(hotkey6PositionX, hotkey6PositionY);
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListenerPosition7 = new HotkeyListener(() -> hotkey7PositionKey) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setClientPosition(hotkey7PositionX, hotkey7PositionY);
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListenerPosition8 = new HotkeyListener(() -> hotkey8PositionKey) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setClientPosition(hotkey8PositionX, hotkey8PositionY);
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListenerPosition9 = new HotkeyListener(() -> hotkey9PositionKey) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setClientPosition(hotkey9PositionX, hotkey9PositionY);
        }

        @Override
        public void hotkeyReleased() {
        }
    };

    private final HotkeyListener hotkeyListenerPosition10 = new HotkeyListener(() -> hotkey10PositionKey) {
        @Override
        public boolean isEnabledOnLoginScreen() {
            return true;
        }

        @Override
        public void hotkeyPressed() {
            setClientPosition(hotkey10PositionX, hotkey10PositionY);
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