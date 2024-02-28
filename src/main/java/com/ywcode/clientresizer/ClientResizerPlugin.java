package com.ywcode.clientresizer;

import com.google.gson.*;
import com.google.gson.reflect.*;
import com.google.inject.*;
import lombok.*;
import lombok.extern.slf4j.*;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.*;
import net.runelite.client.callback.*;
import net.runelite.client.config.*;
import net.runelite.client.eventbus.*;
import net.runelite.client.events.*;
import net.runelite.client.input.*;
import net.runelite.client.input.KeyListener;
import net.runelite.client.plugins.*;
import net.runelite.client.ui.*;
import net.runelite.client.util.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.Point;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.*;

@Slf4j
@PluginDescriptor(
        name = "Client Resizer",
        description = "Allows for automatic or hotkey-based resizing and repositioning, and for snapping the client back/containing it in screen.",
        tags = {"client,resize,automatic,pixels,refresh rate,game size,size,screen size,monitor,display,screen,autoresize,hotkey,hot key,stretched mode,resizable scaling,scaling,position,reposition,location,menubar,title bar,contain,contain in screen,snap,snap back,edge,offset"}
)

public class ClientResizerPlugin extends Plugin {

    //------------- Wall of config vars -------------
    //Vars are quite heavily cached so could probably just config.configKey(). However, the best practice behavior in plugins is to have a bunch of variables to store the results of the config methods, and check it in startUp/onConfigChanged. It feels redundant, but it's better than hitting the reflective calls every frame. --LlemonDuck
    private static boolean resizeAttributeUnchanged;
    private static boolean resizeWhenConfigProfileChanged;
    private static MonitorAttribute[] attributesArray;
    private static String[] valuesStringArray;
    private static Dimension[] dimensionsArray;
    private static Boolean[] resizableScalingBoolArray;
    private static Integer[] resizableScalingPercentArray;
    private static Keybind[] resizeKeybindArray;
    private static Dimension[] hotkeyDimensionArray;
    private static Boolean[] resizableScalingHotkeyBooleanArray;
    private static Integer[] resizableScalingHotkeyPercentageArray;
    private static Keybind[] repositionKeybindArray;
    private static Integer[] hotkeyPositionXArray;
    private static Integer[] hotkeyPositionYArray;
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
    private static boolean showChatMessageContain;
    private static boolean enableHotkeysDuringBankPin;
    private static boolean draggingEdgesWorkaround;
    private static MonitorAttribute copyAttribute;
    private static boolean copyPosition;
    //------------- End of wall of config vars -------------

    private static final String CONFIG_GROUP_NAME = "ClientResizer";
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
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");
    @Getter
    private static int defaultResizableScaling = 50; //Stretched mode plugin default
    private static long previousNanoTime; // Default value is 0
    private static long previousCustomChromeChatMessageNanoTime; // Default value is 0
    private static long previousLockSizeChatMessageNanoTime; // Default value is 0
    private static long previousContainChatMessageNanoTime; // Default value is 0
    private static final long TICK_IN_NANOSECONDS = 600000000;
    private static final long TWENTY_SECONDS_IN_NANOSECONDS = 20000000000L;
    private static final long SIXTY_SECONDS_IN_NANOSECONDS = 60000000000L;
    private static boolean resizeChatMessageFlag; //Default = false
    private static boolean scaleChatMessageFlag; //Default = false
    private static boolean repositionChatMessageFlag; //Default = false
    private static boolean containInScreenChatMessageFlag; //Default = false
    private static boolean customChromeChatMessageFlag; //Default = false
    private static boolean lockWindowSizeChatMessageFlag; //Default = false
    private static boolean draggingEdgesWorkaroundEnabledFlag; //Default = false
    private static MouseInputListener mouseInputListenerMenubar;
    private static boolean mouseInMenuBar; //Default = false. Might be dragging the client while this is active.
    private static boolean isCustomChromeEnabled = true; //Assume true, otherwise set to false in StartUp
    private static final String[] dimensionsStringArray = new String[]{"autoSize1Dimension", "autoSize2Dimension", "autoSize3Dimension", "autoSize4Dimension", "autoSize5Dimension", "autoSize6Dimension", "autoSize7Dimension", "autoSize8Dimension", "autoSize9Dimension", "autoSize10Dimension", "hotkey1Dimension","hotkey2Dimension","hotkey3Dimension","hotkey4Dimension", "hotkey5Dimension","hotkey6Dimension","hotkey7Dimension","hotkey8Dimension","hotkey9Dimension", "hotkey10Dimension"}; //Used to set the default dimenion
    private static final HashSet<Integer> NUMERICAL_KEY_CODES = new HashSet<>(Arrays.asList(KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8, KeyEvent.VK_9, KeyEvent.VK_NUMPAD0, KeyEvent.VK_NUMPAD1, KeyEvent.VK_NUMPAD2, KeyEvent.VK_NUMPAD3, KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD5, KeyEvent.VK_NUMPAD6, KeyEvent.VK_NUMPAD7, KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD9)); //Used to disable numerical hotkeys while the bank pin container is open
    private static final Map.Entry<String, String> EXPORT_PREFIX = new AbstractMap.SimpleImmutableEntry<>("Client Resizer Prefix", "Client Resizer Export");
    private static final Map.Entry<String, String> EXPORT_SUFFIX = new AbstractMap.SimpleImmutableEntry<>("Client Resizer Suffix", "Client Resizer Export");
    private static Dimension draggingEdgesSecondResizeDimension;

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

    @Inject
    private Gson gson;

    //TODO: potentially test 2x and 0.5 scale in RL configure in the future. To do this, add -Dsun.java2d.uiScale.enabled=true -Dsun.java2d.uiScale=2.0 as VM args (source: RL launcher -> LauncherSettings.java). Something maybe worth testing in the future for automatic resizing and contain in screen! How does this count the pixels? Are the bounds etc 2x as well or still 1x?

    @Override
    public void startUp() throws Exception {
        currentGameState = client.getGameState();
        defaultResizableScaling = configManager.getConfiguration("stretchedmode", "scalingFactor", Integer.class); //Default might be set to 50 initially, but will set to its current value on reset at least. PM Can't do this on value initialization because then configManager hasn't been injected yet.
        updateConfig();
        setDefaultConfigSettings();
        keyManager.registerKeyListener(keyListener);

        mouseInputListenerMenubar = new MouseInputListener() {
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                mouseInMenuBar = true;
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                mouseInMenuBar = false;
            }

            //These methods are unused but required to be present in a MouseInputListener implementation
            //---------------------------------------------------------------
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                //This is never procced.
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                //This is never procced.
            }

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                //This is never procced.
                //If you ever want to use mouseDragged and mouseMoved also add a MouseMotionListener by doing customChromeMenuBar.addMouseMotionListener(mouseInputListenerMenubar);
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                //This works while moving the mouse over the menu bar, but during dragging, the mouse is not moved. Thus, this is pretty useless.
                //Could technically use this with a boolean to know when the mouse is not dragging (because the mouse is moving), but no proper way to set this boolean back to false, so does not really work.
                //If you ever want to use mouseDragged and mouseMoved also add a MouseMotionListener by doing customChromeMenuBar.addMouseMotionListener(mouseInputListenerMenubar);
            }
            //---------------------------------------------------------------
        };

        //Get topFrameClient to get the menubar to add the mouseInputListener to it.
        JFrame topFrameClient = (JFrame) SwingUtilities.getWindowAncestor(client.getCanvas());
        JMenuBar customChromeMenuBar = topFrameClient.getJMenuBar();
        if (customChromeMenuBar != null) { //Prevent NPE in case custom chrome is disabled
            customChromeMenuBar.addMouseListener(mouseInputListenerMenubar);
        } else {
            //Custom chrome is disabled
            isCustomChromeEnabled = false;
        }
    }

    @Override
    public void shutDown() throws Exception {
        //Unregister all listeners
        keyManager.unregisterKeyListener(keyListener);
        JFrame topFrameClient = (JFrame) SwingUtilities.getWindowAncestor(client.getCanvas());
        JMenuBar customChromeMenuBar = topFrameClient.getJMenuBar();
        if (isCustomChromeEnabled) { //Prevent NPE in case custom chrome is disabled
            customChromeMenuBar.removeMouseListener(mouseInputListenerMenubar);
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged configChanged) {
        String configGroupChanged = configChanged.getGroup();
        if (configGroupChanged.equals(CONFIG_GROUP_NAME)) {
            updateConfig();
            String configKey = configChanged.getKey();
            String newConfigValue = configChanged.getNewValue();
            switch (configKey) {
                case "copyAttribute": //Only called when the user changes the config value and hasMonitorChanged
                    copyAttributeToClipboard();
                    break;
                case "copyPosition":
                    if (newConfigValue.equals("true")) {
                        copyPositionToClipboard();
                    }
                    break;
                //Send messages when automatic resizing is changed
                case "autoSize1Attribute":
                case "autoSize2Attribute":
                case "autoSize3Attribute":
                case "autoSize4Attribute":
                case "autoSize5Attribute":
                case "autoSize6Attribute":
                case "autoSize7Attribute":
                case "autoSize8Attribute":
                case "autoSize9Attribute":
                case "autoSize10Attribute":
                    if (!newConfigValue.equals("Disabled")) {
                        checkChromeLockSettings();
                    }
                    break;
                //Send messages when contain in screen is changed
                case "containInScreenTop":
                case "containInScreenRight":
                case "containInScreenBottom":
                case "containInScreenLeft":
                    if (newConfigValue.equals("true")) {
                        checkChromeLockSettings();
                    }
                    break;
            }
        }
        if (configGroupChanged.equals("runelite")) {
            String configKey = configChanged.getKey();
            String newConfigValue = configChanged.getNewValue();
            switch (configKey) {
                case "uiEnableCustomChrome":
                case "lockWindowSize":
                    if (newConfigValue.equals("false")) {
                        checkAutomaticResizeContainSettings(configKey);
                    }
                    setDraggingEdgesWorkaround(newConfigValue);
                    break;
            }
        }
    }

    private void updateConfig() {
        resizeAttributeUnchanged = config.resizeAttributeUnchanged();
        resizeWhenConfigProfileChanged = config.resizeWhenConfigProfileChanged();
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
        enableHotkeysDuringBankPin = config.enableHotkeysDuringBankPin();
        draggingEdgesWorkaround = config.draggingEdgesWorkaround();
        copyAttribute = config.copyAttribute();
        copyPosition = config.copyPosition();

        //Update arrays to most recent values
        attributesArray = new MonitorAttribute[]{config.autoSize1Attribute(), config.autoSize2Attribute(), config.autoSize3Attribute(), config.autoSize4Attribute(), config.autoSize5Attribute(), config.autoSize6Attribute(), config.autoSize7Attribute(), config.autoSize8Attribute(), config.autoSize9Attribute(), config.autoSize10Attribute()};
        valuesStringArray = new String[]{config.autoSize1Value(), config.autoSize2Value(), config.autoSize3Value(), config.autoSize4Value(), config.autoSize5Value(), config.autoSize6Value(), config.autoSize7Value(), config.autoSize8Value(), config.autoSize9Value(), config.autoSize10Value()};
        dimensionsArray = new Dimension[]{config.autoSize1Dimension(), config.autoSize2Dimension(), config.autoSize3Dimension(), config.autoSize4Dimension(), config.autoSize5Dimension(), config.autoSize6Dimension(), config.autoSize7Dimension(), config.autoSize8Dimension(), config.autoSize9Dimension(), config.autoSize10Dimension()};
        resizableScalingBoolArray = new Boolean[]{config.resizableScalingAutomatic1(), config.resizableScalingAutomatic2(), config.resizableScalingAutomatic3(), config.resizableScalingAutomatic4(), config.resizableScalingAutomatic5(), config.resizableScalingAutomatic6(), config.resizableScalingAutomatic7(), config.resizableScalingAutomatic8(), config.resizableScalingAutomatic9(), config.resizableScalingAutomatic10()};
        resizableScalingPercentArray = new Integer[]{config.resizableScalingAutomatic1Percent(), config.resizableScalingAutomatic2Percent(), config.resizableScalingAutomatic3Percent(), config.resizableScalingAutomatic4Percent(), config.resizableScalingAutomatic5Percent(), config.resizableScalingAutomatic6Percent(), config.resizableScalingAutomatic7Percent(), config.resizableScalingAutomatic8Percent(), config.resizableScalingAutomatic9Percent(), config.resizableScalingAutomatic10Percent()};
        resizeKeybindArray = new Keybind[]{config.hotkey1Key(), config.hotkey2Key(), config.hotkey3Key(), config.hotkey4Key(), config.hotkey5Key(), config.hotkey6Key(), config.hotkey7Key(), config.hotkey8Key(), config.hotkey9Key(), config.hotkey10Key()};
        hotkeyDimensionArray = new Dimension[]{config.hotkey1Dimension(), config.hotkey2Dimension(), config.hotkey3Dimension(), config.hotkey4Dimension(), config.hotkey5Dimension(), config.hotkey6Dimension(), config.hotkey7Dimension(), config.hotkey8Dimension(), config.hotkey9Dimension(), config.hotkey10Dimension()};
        resizableScalingHotkeyBooleanArray = new Boolean[]{config.resizableScalingHotkey1(), config.resizableScalingHotkey2(), config.resizableScalingHotkey3(), config.resizableScalingHotkey4(), config.resizableScalingHotkey5(), config.resizableScalingHotkey6(), config.resizableScalingHotkey7(), config.resizableScalingHotkey8(), config.resizableScalingHotkey9(), config.resizableScalingHotkey10()};
        resizableScalingHotkeyPercentageArray = new Integer[]{config.resizableScalingHotkey1Percent(), config.resizableScalingHotkey2Percent(), config.resizableScalingHotkey3Percent(), config.resizableScalingHotkey4Percent(), config.resizableScalingHotkey5Percent(), config.resizableScalingHotkey6Percent(), config.resizableScalingHotkey7Percent(), config.resizableScalingHotkey8Percent(), config.resizableScalingHotkey9Percent(), config.resizableScalingHotkey10Percent()};
        repositionKeybindArray = new Keybind[]{config.hotkey1PositionKey(), config.hotkey2PositionKey(), config.hotkey3PositionKey(), config.hotkey4PositionKey(), config.hotkey5PositionKey(), config.hotkey6PositionKey(), config.hotkey7PositionKey(), config.hotkey8PositionKey(), config.hotkey9PositionKey(), config.hotkey10PositionKey()};
        hotkeyPositionXArray = new Integer[]{config.hotkey1PositionX(), config.hotkey2PositionX(), config.hotkey3PositionX(), config.hotkey4PositionX(), config.hotkey5PositionX(), config.hotkey6PositionX(), config.hotkey7PositionX(), config.hotkey8PositionX(), config.hotkey9PositionX(), config.hotkey10PositionX()};
        hotkeyPositionYArray = new Integer[]{config.hotkey1PositionY(), config.hotkey2PositionY(), config.hotkey3PositionY(), config.hotkey4PositionY(), config.hotkey5PositionY(), config.hotkey6PositionY(), config.hotkey7PositionY(), config.hotkey8PositionY(), config.hotkey9PositionY(), config.hotkey10PositionY()};
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

    @Subscribe
    public void onCommandExecuted(CommandExecuted commandExecuted) {
        String command = Text.standardize(commandExecuted.getCommand());
        switch (command) {
            case "clientresizerexport":
                exportConfigToClipboard();
                break;
            case "clientresizerimportoverwrite":
                importConfigFromClipboard();
                break;
        }
    }

    private void setDefaultConfigSettings() {
        //Set default dimensions to current game size so someone doesn't accidentally set their game size to Dimension(Constants.GAME_FIXED_WIDTH, Constants.GAME_FIXED_HEIGHT)
        //PM configManager.setConfiguration does fire a ConfigChanged event, but obviously only if the config value is actually changed (not if the config value is already set to that value).
        for (int i = 0; i < dimensionsArray.length; i++) {
            setDefaultDimension(dimensionsArray[i], dimensionsStringArray[i]);
        }
        //Alternatively, you can move setConfiguration(copyAttribute) and updateConfig to onStartup and probably change the Config to just return:
        //return configManager.getConfiguration("runelite", "gameSize", Dimension.class);
        //Only potential advantage of current application is probably that the user can reset it to 0,0 but then on next restart it gets changed anyway so meh.

        configManager.setConfiguration(CONFIG_GROUP_NAME, "copyAttribute", MonitorAttribute.Disabled); //reset copyAttribute setting back to disabled on plugin start
        configManager.setConfiguration(CONFIG_GROUP_NAME, "copyPosition", "False"); //reset copyPosition setting back to disabled on plugin start

        //Set draggingEdgesWorkaround based on the user's RuneLite settings during first startup
        //Check if the draggingEdgesWorkaround config has already been set once. If it has, then don't change it again
        //Using an integer instead of a boolean here in case I want to implement a version "2" instead of "1" for some reason in the future
        if (configManager.getConfiguration(CONFIG_GROUP_NAME, "draggingEdgesWorkaroundDefaultSet", Integer.class) == null) { //Config value has never been set
            //Retrieve lock window size config value in RuneLiteConfig and if it's not enabled, enable the workaround
            boolean lockWindowSizeRLConfig = configManager.getConfiguration("runelite", "lockWindowSize", Boolean.class);
            if (!lockWindowSizeRLConfig) {
                configManager.setConfiguration(CONFIG_GROUP_NAME, "draggingEdgesWorkaround", "True"); //Enable workaround; it's disabled by default
                sendGameChatMessage(ResizerMessageType.DRAGGING_EDGES_WORKAROUND_ENABLED);
            }
            //Don't send a message when the workaround is not enabled, because leaving the workaround disabled with lock window size enabled, does not have any potentially negative effects for the user.
            configManager.setConfiguration(CONFIG_GROUP_NAME, "draggingEdgesWorkaroundDefaultSet", 1); //Set a config value, so we know the defaults for the workaround have been set once
        }
    }

    private void setDefaultDimension(Dimension dimension, String dimensionName) {
        //Actually set the default dimension
        Dimension defaultDimension = new Dimension(0, 0);
        if (dimension.equals(defaultDimension)) {
            Dimension currentSize = configManager.getConfiguration("runelite", "gameSize", Dimension.class);
            configManager.setConfiguration(CONFIG_GROUP_NAME, dimensionName, currentSize);
        }
    }

    private void copyAttributeToClipboard() { //Copy value of specified attribute to clipboard, so it can be pasted in the String input boxes
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

    private void checkChromeLockSettings() {
        //Check if custom chrome is disabled and/or lock window size is disabled => check if enough time has passed, so we don't spam the user => send message
        if (configManager.getConfiguration("runelite", "uiEnableCustomChrome", Boolean.class).equals(false)) {
            sendCustomChromeWarning();
        }
        if (configManager.getConfiguration("runelite", "lockWindowSize", Boolean.class).equals(false)) {
            sendLockWindowWarning();
        }
    }

    private void sendCustomChromeWarning() {
        //Check if enough time has passed as to not spam the user => send custom chrome message
        long currentNanoTime = System.nanoTime();
        if (currentNanoTime - previousCustomChromeChatMessageNanoTime > TWENTY_SECONDS_IN_NANOSECONDS) {
            sendGameChatMessage(ResizerMessageType.CUSTOM_CHROME);
            previousCustomChromeChatMessageNanoTime = currentNanoTime;
        }
    }

    private void sendLockWindowWarning() {
        //Check if enough time has passed as to not spam the user => send lock window size message
        long currentNanoTime = System.nanoTime();
        if (currentNanoTime - previousLockSizeChatMessageNanoTime > TWENTY_SECONDS_IN_NANOSECONDS) {
            sendGameChatMessage(ResizerMessageType.LOCK_WINDOW_SIZE);
            previousLockSizeChatMessageNanoTime = currentNanoTime;
        }
    }

    private void checkAutomaticResizeContainSettings(String configKey) {
        //Check if automatic resizing / contain in screen is enabled => if so, check if enough time has passed as to not spam the user (performed my the method that gets called) => send message
        boolean shouldSendMessage = false;
        for (MonitorAttribute monitorAttribute : attributesArray) {
            if (monitorAttribute != MonitorAttribute.Disabled) {
                shouldSendMessage = true;
                break; //Boolean already set to true so can exit the for loop
            }
        }
        if (containInScreenTop || containInScreenRight || containInScreenBottom || containInScreenLeft) {
            shouldSendMessage = true;
        }
        if (shouldSendMessage) { //Send msg if code above determined that it should be sent.
            switch (configKey) {
                case "uiEnableCustomChrome":
                    sendCustomChromeWarning();
                    break;
                case "lockWindowSize":
                    sendLockWindowWarning();
                    break;
            }
        }
    }

    private void setDraggingEdgesWorkaround(String newConfigValue) {
        //Set the dragging edges workaround when the config value changes
        //Check the gamestate first, so it's not done on every RL startup
        if (currentGameState == null || currentGameState == GameState.STARTING || currentGameState == GameState.UNKNOWN) {
            return;
        }

        if (newConfigValue.equals("false")) {
            //Enable workaround when disabling lock window size
            configManager.setConfiguration(CONFIG_GROUP_NAME, "draggingEdgesWorkaround", "True");
            sendGameChatMessage(ResizerMessageType.DRAGGING_EDGES_WORKAROUND_ENABLED); //Send a game message that the config value has been enabled. The user can opt to disable the advanced config value if they so desire.
        } else {
            //Disable workaround when enabling lock window size
            configManager.setConfiguration(CONFIG_GROUP_NAME, "draggingEdgesWorkaround", "False");
            //Don't send a message because disabling the workaround when the window size is locked, does not have any potentially negative effects for the user.
        }
    }

    private void monitorCheck() {
        //Specifically opted to not use an EventListener/ComponentListener for window positioning and running the code in there with custom chrome disabled =>
        // Problem is that it might be jarring with custom chrome disabled (MouseListener to disable the code while the cursor is in the MenuBar does not work) and the 'soft' snap back would not work since it polls it too frequently instead of once a tick.
        // With custom chrome enabled, this would technically not be a problem because the code will not activate while dragging (not jarring, and it won't check too frequently)
        // However, it does not seem to like working. Maybe it reports too frequently so SwingUtilities.invokelater isn't done yet or something like that? Also makes moving the client very laggy when copyPosition is enabled due to the reporting frequency. Might revisit at some point with a fresh look when I'm not this tired and/or got more ideas.

        if (!mouseInMenuBar) { //Wait till we are done dragging the client! Might still be dragging if mouse in the menu bar (resulting in problems for both automatic resizing and containing in screen/snapping back)!
            containInScreen(); //Contain before getting the graphicsConfig (which does contain the current monitor!)
            //Alternatively use client.getCanvas().getGraphicsConfiguration() if this breaks!
            SwingUtilities.invokeLater(() -> {
                graphicsConfig = clientUI.getGraphicsConfiguration();
                currentMonitor = graphicsConfig.getDevice(); // Actually relevant here to refresh the current monitor since I opted to use static variables instead of local variable that update per method.
            });
            if (draggingEdgesWorkaround && draggingEdgesSecondResizeDimension != null) {
                //This will trigger the second resize when the workaround is enabled
                setGameSize(draggingEdgesSecondResizeDimension);
            }
            if (hasMonitorChanged()) {
                copyAttributeToClipboard();
                resizeClient();
            }
        }
        copyPositionToClipboard(); // Already checks if the boolean config var is enabled. Should not only run when the monitor has changed.
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
        //Method containing containInScreen already contains if(!mouseInMenuBar) so we don't have to check it again here!
        //If buggy for some reason, try readding if(!mouseInMenuBar) again I suppose
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
                //TODO: potentially add monitor specific offset overrides (new config category then) at some point, in case people want e.g. different offsets for their 4k monitor than for their 1080p monitor.

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

                //if contain is enabled and the client is outside the screenbounds (+ config offset) => set the coords to reposition to and set the boolean, so we know we need to reposition
                //If the client is too big to even fit on the screen... Let's align the top right side with the screen, so you can still press the x and access the config if all 4 contain options are enabled (so the order below is based on that)
                //Point is (x,y) with 0,0 being in the top left corner! 1920,1080 or smth like that is right bottom corner
                //First check if contain boolean is enabled, then if the client is outside the bounds, then check if it is equal to or less than the snapback pixels (hard or soft snap)
                if (containInScreenLeft && clientBoundsXLeft < screenBoundsXLeft && screenBoundsXLeft - clientBoundsXLeft <= containInScreenSnapBackPx) {
                    coordsToRepositionToX = screenBoundsXLeft;
                    shouldReposition = true;
                }
                if (containInScreenRight && clientBoundsXRight > screenBoundsXRight && clientBoundsXRight - screenBoundsXRight <= containInScreenSnapBackPx) {
                    coordsToRepositionToX = screenBoundsXRight - clientWidth;
                    shouldReposition = true;
                }
                if (containInScreenBottom && clientBoundsYBottom > screenBoundsYBottom && clientBoundsYBottom - screenBoundsYBottom <= containInScreenSnapBackPx) {
                    coordsToRepositionToY = screenBoundsYBottom - clientHeight;
                    shouldReposition = true;
                }
                if (containInScreenTop && clientBoundsYTop < screenBoundsYTop && screenBoundsYTop - clientBoundsYTop  <= containInScreenSnapBackPx) {
                    coordsToRepositionToY = screenBoundsYTop;
                    shouldReposition = true;
                }

                //If we need to reposition, then reposition to the new coords
                if (shouldReposition) {
                    if (showChatMessageContain) {
                        //Send a chat message, but only do it every 30 seconds as to not spam the user
                        long currentNanoTime = System.nanoTime();
                        if (currentNanoTime - previousContainChatMessageNanoTime > SIXTY_SECONDS_IN_NANOSECONDS) { //It will fire immediately once when starting the plugin in the right gamestate (desired behavior IMO) and also immediately when logging out, but that is fine probs.
                            sendGameChatMessage(ResizerMessageType.CONTAIN_IN_SCREEN);
                            previousContainChatMessageNanoTime = currentNanoTime;
                        }
                    }
                    //Reposition the client to the new coords conform the config values of contain in screen
                    setClientPosition(coordsToRepositionToX, coordsToRepositionToY);
                }
            }
        }
        //Save the graphicsConfig, so we can check next time if it moved
        SwingUtilities.invokeLater(() -> {
            previousGraphicsConfig = client.getCanvas().getGraphicsConfiguration();
        });
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
                break;
            case CUSTOM_CHROME:
                if (currentGameState == GameState.LOGGED_IN || currentGameState == GameState.LOADING) {
                    actuallySendMessage("<col=FF0000>Client Resizer plugin: It is recommended to enable custom chrome when using automatic resizing or snap back/contain in screen. You can enable this in the 'RuneLite' config > 'Windows Settings' > 'Enable custom window chrome'");
                } else {
                    customChromeChatMessageFlag = true;
                }
                break;
            case LOCK_WINDOW_SIZE:
                if (currentGameState == GameState.LOGGED_IN || currentGameState == GameState.LOADING) {
                    actuallySendMessage("Client Resizer plugin: It is recommended to lock the window size when using automatic resizing or snap back/contain in screen, so your client does not snap-resize when dragging. You can change this in the 'RuneLite' config. The plugin is compatible with an unlocked window size though.");
                } else {
                    lockWindowSizeChatMessageFlag = true;
                }
                break;
            case DRAGGING_EDGES_WORKAROUND_ENABLED:
                if (currentGameState == GameState.LOGGED_IN || currentGameState == GameState.LOADING) {
                    actuallySendMessage("Client Resizer plugin: The workaround to make client resizer compatible with resizing the client by dragging its edges has been enabled in the advanced config section, because you disabled 'Lock window size' in the 'RuneLite' config.");
                } else {
                    draggingEdgesWorkaroundEnabledFlag = true;
                }
                break;
        }
    }

    private void actuallySendMessage(String message) {
        //Used in the method above. This method actually sends the message.
        //client.addChatMessage has to be called on clientThread. Doesn't cause any error if not called on client.getGameState() == GameState.LOGGED_IN
        clientThread.invokeLater(() -> {
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", message, "");
        });
    }

    private void setClientPosition(int pointX, int pointY) {
        //Sets the client position
        JFrame topFrameClient = (JFrame) SwingUtilities.getWindowAncestor(client.getCanvas());
        //To reset to middle, use:
        //topFrameClient.setLocationRelativeTo(topFrame.getOwner());
        topFrameClient.setLocation(pointX, pointY);
        //For some reason the graphicsConfiguration is incorrect directly after using setLocation...
        //This is probably because some stuff about window positioning is possibly an async operation (thx Hooder!) https://docs.oracle.com/javase/8/docs/api/java/awt/Window.html
        //Setting the frame to .setVisible(false) and .setVisible(true) solves this problem, but this is obviously not a great solution (https://stackoverflow.com/questions/17916542/graphicsconfiguration-getdevice-returning-the-wrong-monitor-id). The impact on the user should not be too much though since the client is being repositioned in this case, so it's not like their gameplay is being interrupted.
        //However, a better solution is using SwingUtilities.invokeLater
        //Alternatively I could also use an event listener for window positioning and run the rest of the code in there => problem is that it might be jarring with custom chrome disabled (MouseListener to disable the code while the cursor is in the MenuBar does not work) and the 'soft' snap back would not work since it polls it too frequently instead of once a tick.
        SwingUtilities.invokeLater(() -> {
            previousGraphicsConfig = client.getCanvas().getGraphicsConfiguration();
        });
    }

    private boolean hasMonitorChanged() {
        //PM If you ever want the resize on client to be turned into a config option, add a previousMonitor == null => return resizeOnStartup probably. Plus don't forget to potentially change stuff like hasAttributeChanged then!
        if (currentMonitor != null && !currentMonitor.equals(previousMonitor)) { //No previousMonitor == null check because unnecessary before equals() call
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
        for (int i = 0; i < attributesArray.length; i++) {
            //if MonitorAttribute != disabled, the Value/String input by the user is properly processed (+ has proper format) and if processed Value/String matches value of current monitor, then setGameSize to the inputted Dimension
            if (attributesArray[i] != MonitorAttribute.Disabled &&
                    processAttributeString(attributesArray[i], valuesStringArray[i]) != null &&
                    processAttributeString(attributesArray[i], valuesStringArray[i]).equals(currentMonitorValueForAttribute(attributesArray[i]))) {
                if (resizeAttributeUnchanged) {
                    setGameSize(dimensionsArray[i]);
                    if (resizableScalingBoolArray[i]) {
                        setResizableScaling(resizableScalingPercentArray[i]);
                    }
                }
                if (!resizeAttributeUnchanged && (hasAttributeChanged(attributesArray[i]) || hasProfileChanged)) { //If the user disables the option to resize when the current monitor has changed but the value of the specified attribute (Dimensions or Refresh Rate) is the same
                    setGameSize(dimensionsArray[i]);
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
        return NUMERIC_PATTERN.matcher(inputString).matches();
    }

    private void setGameSize(Dimension dimension) {
        //Actually set the game size

        //Processing probably irrelevant since ClientUI.java contains the same code, but why not.
        int processedWidth = Math.max(Math.min(dimension.width, 7680), Constants.GAME_FIXED_WIDTH);
        int processedHeight = Math.max(Math.min(dimension.height, 2160), Constants.GAME_FIXED_HEIGHT);
        Dimension processedGameSize = new Dimension(processedWidth, processedHeight);
        Dimension currentSize = configManager.getConfiguration("runelite", "gameSize", Dimension.class);

        //Workaround for people that also resize the client by dragging its edges (dragging does not change the config value in the RuneLite config).
        //Originally this workaround just increased the width by 1 so the config value changed, then currentSize would not equal processedGameSize, so it'd trigger a second resize.
        //In practice, it turns out that for some reason the client cannot process these config changes quickly enough to actually resize.
        //Config values do get changed correctly based on printlns of configManager.getConfiguration("runelite", "gameSize", Dimension.class)
        //But it does not change the size of the client if the edges have been dragged manually. I've not looked into core to find out why this is happening.
        //I just added a workaround that adds a tick of delay in between the first and second resizes if the current config value equals the value it's going to get set to.
        if (draggingEdgesWorkaround && draggingEdgesSecondResizeDimension == null && currentSize.equals(processedGameSize)) {
            //draggingEdgesSecondResizeDimension == null means it's not doing the second resize. This is important because otherwise it could get stuck in a loop, increasing the client width by 1 every tick
            //The workaround only activates when resizing to a size that's the same as the config value to prevent unnecessary +1 width resizes.
            Dimension processedGameSizePlus1 = new Dimension(processedWidth+1, processedHeight);
            configManager.setConfiguration("runelite", "gameSize", processedGameSizePlus1);
            draggingEdgesSecondResizeDimension = processedGameSize;
        } else {
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
            draggingEdgesSecondResizeDimension = null; //Reset the dimension for the second resize to null
        }
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

    private void setClientPositionHotkey(int pointX, int pointY) {
        //Sets the client position when using hotkey
        //TODO: potentially add automatic repositioning of the client like automatic resizing, but this might automatically throw the client offscreen if configured very incorrectly.
        // Thus, for let's not add this for now. Can reconsider in the future.
        // If this is added, check how you check how you did resizeClient() (copy most of it probs), use processAttributeString, and check how you did setGameSize (copy most of it probs).
        setClientPosition(pointX, pointY);
        if (showChatMessageReposition) {
            sendGameChatMessage(ResizerMessageType.REPOSITION);
        }
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
        if (customChromeChatMessageFlag) {
            customChromeChatMessageFlag = false;
            sendGameChatMessage(ResizerMessageType.CUSTOM_CHROME);
        }
        if (lockWindowSizeChatMessageFlag) {
            lockWindowSizeChatMessageFlag = false;
            sendGameChatMessage(ResizerMessageType.LOCK_WINDOW_SIZE);
        }
        if (draggingEdgesWorkaroundEnabledFlag) {
            draggingEdgesWorkaroundEnabledFlag = false;
            sendGameChatMessage(ResizerMessageType.DRAGGING_EDGES_WORKAROUND_ENABLED);
        }
    }

    private void exportConfigToClipboard() {
        //Used to export the config in a json Map<String, String> format to the clipboard
        //Get list of all config keys
        List<String> configKeys = configManager.getConfigurationKeys(CONFIG_GROUP_NAME);
        Map<String, String> exportedValues = new LinkedHashMap<>(); //Use LinkeHashMap so insertion order is maintained for prefix and suffix, and add prefix
        exportedValues.put(EXPORT_PREFIX.getKey(), EXPORT_PREFIX.getValue()); //Add prefix, so we can check later on that it's the correct file
        //Loop over config keys and add the key & value to the map
        for (String configKey : configKeys) {
            String processedConfigKey = configKey.replace(CONFIG_GROUP_NAME + ".", ""); //Remove config key prefix
            String configValue = configManager.getConfiguration(CONFIG_GROUP_NAME, processedConfigKey); //Get config values
            exportedValues.put(processedConfigKey, configValue); //Add config key & value to LinkedHashMap
        }
        exportedValues.put(EXPORT_SUFFIX.getKey(), EXPORT_SUFFIX.getValue()); //Add suffix
        String jsonArray = gson.toJson(exportedValues); //Use Gson because otherwise bad things happen, like trying to separate height=1,width=2 into two separate entries.
        //Copy to clipboard
        StringSelection stringSelection = new StringSelection(jsonArray);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        actuallySendMessage("The Client Resizer config has been copied to clipboard. Keep in mind that importing will override your Client Resizer config settings.");
    }

    private void importConfigFromClipboard() {
        try {
            String clipboardDataString = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor); //Get data from clipboard and interpret it as a string
            Map<String, String> clipboardData = gson.fromJson(clipboardDataString, new TypeToken<Map<String, String>>() {}.getType()); //Convert Gson string to non-Gson Map<String, String>

            //Check if the data contains the EXPORT_PREFIX and EXPORT_SUFFIX as the first and last entry. If it does not, the user did something wrong with copying.
            Map.Entry<String, String> firstEntry = clipboardData.entrySet().iterator().next(); //Get first entry
            Map.Entry<String, String> lastEntry = clipboardData.entrySet().stream().skip(clipboardData.size() - 1).findFirst().orElse(new AbstractMap.SimpleEntry<>("entry", "not found")); //Get the final entry by skipping the other entries, and if it can't find it, return the ("entry", "not found") entry.
            if (!firstEntry.equals(EXPORT_PREFIX) || !lastEntry.equals(EXPORT_SUFFIX)) {
                actuallySendMessage("<col=FF0000>Error importing Client Resizer config from clipboard. Make sure it's the right format and properly copied to clipboard. Malformed format.");
            } else {
                //Should be the right format now because it contains the prefix and suffix. They can now be removed
                clipboardData.remove(EXPORT_PREFIX.getKey());
                clipboardData.remove(EXPORT_SUFFIX.getKey());
                //Get existing config keys, so we can later check if the imported config key is an actually existing config key
                List<String> configKeys = configManager.getConfigurationKeys(CONFIG_GROUP_NAME);
                Map<String, String> skippedKeyValues = new LinkedHashMap<>();
                for (String configKey : clipboardData.keySet()) { //Prefix and suffix have already been removed
                    String configValue = clipboardData.get(configKey);
                    String preprocessedConfigKey = CONFIG_GROUP_NAME + "." + configKey; //Add the config prefix again, so you can use the value to check if it's an existing config key.
                    if (configKeys.contains(preprocessedConfigKey)) { //Check if the config actually exists, otherwise don't import it and send a message at the end that this key/value has not been imported.
                        configManager.setConfiguration(CONFIG_GROUP_NAME, configKey, configValue); //Set the configvalue. Tested for Strings, ints, booleans, dimensions, and MonitorAttributes and they all work.
                    } else {
                        //If a key does not exist in the current config keys, do not import it and add it to a Map to later be outputted.
                        skippedKeyValues.put(configKey, configValue);
                    }
                }
                actuallySendMessage("The Client Resizer config importing process has been completed. The config values have been replaced. Please close and reopen the Client Resizer config to review the changes in case it was open during importing.");
                if (!skippedKeyValues.isEmpty()) { //If a key was skipped, output a message
                    actuallySendMessage("<col=FF0000>Some config keys were not imported because they do not exist in the current config. This is not a problem if the config key was created by an older version of the plugin. Alternatively, the clipboard contents were incorrectly formatted.");
                    actuallySendMessage("<col=FF0000>Config keys/values that were skipped: " + skippedKeyValues);
                }
            }
        } catch (IOException | UnsupportedFlavorException error) {
            actuallySendMessage("<col=FF0000>Error importing Client Resizer config from clipboard. Make sure it's the right format and properly copied to clipboard. Incorrect clipboard content.");
        }
    }

    private boolean shouldHotkeyBeAcceptedBankPin(KeyEvent keyEvent) {
        //Should the hotkey be accepted because the advanced option is enabled, the bank pin window is not on the screen and/or the hotkey is not purely numerical?
        if (enableHotkeysDuringBankPin) {
            //Hotkeys are enabled during bank pin, so don't ignore the hotkeys
            return true;
        }

        Widget bankPinContainer = client.getWidget(ComponentID.BANK_PIN_CONTAINER);
        if (bankPinContainer == null || bankPinContainer.isSelfHidden()) {
            //Maybe using WidgetLoaded/Closed is more efficient, but even then, this method only runs when the user presses the keybind for one of their client resizer hotkeys. That does not happen frequently.
            //bankPinContainer is null or hidden, so don't ignore the hotkeys
            return true;
        }

        //getModifiersEx() == 0 => ctrl/alt/shift/whatever is not being pressed
        //numericalKeyCodes should contain all the 0-9 and numpad 0-9 keycodes that can be used to enter the bank pin
        return keyEvent.getModifiersEx() != 0 || !NUMERICAL_KEY_CODES.contains(keyEvent.getKeyCode());
    }

    // ------------- Wall of the KeyListener -------------
    private final KeyListener keyListener = new KeyListener() {

        @Override
        public boolean isEnabledOnLoginScreen() {
            //Enable on login screen so user can resize/reposition their client(s) before logging in.
            return true;
        }

        @Override
        public void keyTyped(KeyEvent keyEvent) {
            //Must be implemented
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            boolean matchFound = false; //Used to determine if the keyEvent should be consumed
            //Loop over the resize hotkeys, and if it matches and the key should not be ignored during the bank pin, set the game size and potentially the resizable scaling based on the other arrays.
            for (int i = 0; i < resizeKeybindArray.length; i++) {
                if (resizeKeybindArray[i].matches(keyEvent) && shouldHotkeyBeAcceptedBankPin(keyEvent)) {
                    setGameSize(hotkeyDimensionArray[i]);
                    if (resizableScalingHotkeyBooleanArray[i]) {
                        setResizableScaling(resizableScalingHotkeyPercentageArray[i]);
                    }
                    matchFound = true;
                }
            }
            //Loop over the reposition hotkeys, and if it matches and the key should not be ignored during the bank pin, reposition the client based on the other arrays.
            for (int i = 0; i < repositionKeybindArray.length; i++) {
                if (repositionKeybindArray[i].matches(keyEvent) && shouldHotkeyBeAcceptedBankPin(keyEvent)) {
                    setClientPositionHotkey(hotkeyPositionXArray[i], hotkeyPositionYArray[i]);
                    matchFound = true;
                }
            }
            if (matchFound) {
                //We found a match so consume the keyEvent
                keyEvent.consume();
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            //Must be implemented
        }
    };
    // ------------- End of wall of the KeyListener -------------

    @Provides
    ClientResizerConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(ClientResizerConfig.class);
    }
}