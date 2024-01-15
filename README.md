# Client Resizer
Also annoyed by having to change your client's size (game size) every time you move RuneLite to a different monitor or when doing a different activity?<br>

**Client Resizer allows for automatic or hotkey-based resizing of the client.** It's especially useful for people that have multiple monitors of different sizes or resolutions, or for people that like to resize or (re)position their client in specific scenarios (e.g. per activity).<br>

**This plugin adds the following features:**
- Automatically resize your client when it's moved to a different monitor. This can be configured per monitor attribute, e.g. per ID string (differs for every monitor), resolution (dimension) or refresh rate.
- Hotkey-based resizing. Set a hotkey (e.g. Ctrl+L) and resize the client to a size of your choosing.
- Resize your client when switching RL config profiles if this isn't done by the config profiles themselves yet. Set up automatic resizing for your monitors with different game sizes per RL config profile. Then enable `Resize when switching RuneLite config profile`.
- Change your resizable scaling (stretched mode plugin), either automatically or hotkey-based.
- (Re)position your client using hotkeys. This allows you to get RL flush with your screen. Furthermore, this allows you to set the title bar offscreen at the top of the screen.

The plugin still respects all the other settings in the "RuneLite" > "Window Settings" config. For that reason, I personally suggest using the following RuneLite Window Settings:<br>
![RuneLite window settings](docs/img/runelite-window-settings.png)<br>
*Since the flatlaf update (RuneLite 1.10.20) automatically resizing the client when dragging it to a different monitor does not work anymore. Shift + Windows key + arrow or hotkey-based repositioning does still automatically resize the client though!*

## User Instructions / How to set up

### Automatic resizing
- Move the client to the monitor for which you'd like to set up automatic resizing.
- Open the config panel of Client Resizer (gear icon).
- Select one of the attribute types from the dropdown menu "Copy attribute of current monitor".<br>
E.g. ID String:<br>
![Copy attribute dropbown](docs/img/copy-attribute-dropdown.png)<br>

  - ID String: unique ID per monitor.
  - Bounds: x, y, width, height of the monitor.
  - Dimensions: width, height of the monitor in pixels (resolution).
  - Refresh Rate: refresh rate of the monitor.
- Open the "Automatic Resizing" category.
- Set the client / game size in one of the dimension options.
- Set the corresponding dropdown menu to the attribute type you previously selected.
- Paste (Ctrl+V on Windows) the value of the attribute in the corresponding "monitor attribute value" box.<br>
E.g. a fully configured monitor looks like this:<br>
![Fully configured monitor](docs/img/fully-configured-monitor.png)

If you also want to resize your client when loading a different RuneLite config profile, then enable the "Resize when switching RuneLite config profile" option. If you don't understand what this means, you should probably leave this option disabled (default).

### Hotkey-based resizing
- Open the config panel of Client Resizer (gear icon).
- Open the "Hotkey Resizing" category.
- Set the client / game size in one of the dimension options.
- Set the corresponding hotkey. Combinations such as Ctrl + L are allowed.<br>
![Fully configured hotkey](docs/img/fully-configured-hotkey.png)

### Resizable scaling (stretched mode)
*Note: Requires the stretched mode plugin to be enabled to have any effect ingame!*
- Configure automatic resizing or hotkey-based resizing per the instructions above.
- Open the config panel of Client Resizer (gear icon).
- Open the "Resizable Scaling" category.
- Tick the box you want to change resizable scaling for.
- Enter the resizable scaling percentage.<br>
![Resizable scaling](docs/img/resizable-scaling.png)

### Hotkey based positioning
- Open the config panel of Client Resizer (gear icon).
- Open the "Position" category.
- Set the "Position X" and "Position Y".
- Set the corresponding hotkey. Combinations such as Ctrl + L are allowed.<br>
  ![Fully configured position hotkey](docs/img/fully-configured-position-hotkey.png)

## Additional info
Not tested on macOS or Linux; results may vary.

If you experience any issues or have any concerns, please reach out to YvesW via a GitHub Issue, by Discord direct messaging or via the RuneLite Discord by mentioning @``YvesW``.

## Changelog
01-2024:
- Please note that the flatlaf changes kinda broke automatic resizing if you do it by dragging the client. Use Shift + Windows key + arrow or hotkey-based repositioning to move the client to another screen and make it reposition automatically, if you've set up automatic resizing.
- Added the option to change the position/location of the client using hotkeys. Thanks to Hooder for the tip!

09-2023:
- Added the option to also change "Resizable Scaling" from the stretched mode plugin when using automatic or hotkey-based resizing.

02-2023:
- Fixed the icon not being displayed.
- Added the option to automatically resize the client when changing the RuneLite config profile if the current monitor matches one of the configured monitor attributes.