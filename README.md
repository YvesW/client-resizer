# Client Resizer
Also annoyed by having to change your client's size (game size) every time you move RuneLite to a different monitor or when doing a different activity?<br>

**Client Resizer allows for automatic or hotkey-based resizing of the client.** It's especially useful for people that have multiple monitors of different sizes or resolutions, or for people that like to resize their client in specific scenarios (e.g. per activity).<br>

**This plugin adds the following features:**
- Automatically resize your client when it's moved to a different monitor. This can be configured per monitor attribute, e.g. per ID string (differs for every monitor), resolution (dimension) or refresh rate.
- Hotkey-based resizing. Set a hotkey (e.g. Ctrl+L) and resize the client to a size of your choosing.

The plugin still respects all the other settings in the "RuneLite" > "Window Settings" config. For that reason, I personally suggest using the following RuneLite Window Settings:<br>
![RuneLite window settings](src/main/resources/img/runelite-window-settings.PNG)<br>
*It is recommended to enable 'Enable custom window chrome' in 'RuneLite' > 'Window Settings' if you'd like to automatically resize the client when dragging it to a different monitor (enabled by default). Windows key + arrow does still automatically resize the client with custom chrome disabled. However, moving the client to a different monitor by dragging the title bar will not work with custom chrome disabled.*

## User Instructions / How to set up

### Automatic resizing
- Move the client to the monitor for which you'd like to set up automatic resizing.
- Open the config panel of Client Resizer (gear icon).
- Select one of the attribute types from the dropdown menu "Copy attribute of current monitor".<br>
E.g. ID String:<br>
![Copy attribute dropbown](src/main/resources/img/copy-attribute-dropdown.PNG)<br>

  - ID String: unique ID per monitor.
  - Bounds: x, y, width, height of the monitor.
  - Dimensions: width, height of the monitor in pixels (resolution).
  - Refresh Rate: refresh rate of the monitor.
- Open the "Automatic Resizing" category.
- Set the client / game size in one of the dimension options.
- Set the corresponding dropdown menu to the attribute type you previously selected.
- Paste (Ctrl+V on Windows) the value of the attribute in the corresponding "monitor attribute value" box.<br>
E.g. a fully configured monitor looks like this:<br>
![Fully configured monitor](src/main/resources/img/fully-configured-monitor.PNG)

### Hotkey-based resizing
- Open the config panel of Client Resizer (gear icon).
- Open the "Hotkey Resizing" category.
- Set the client / game size in one of the dimension options.
- Set the corresponding hotkey. Combinations such as Ctrl + L are allowed.<br>
![Fully configured hotkey](src/main/resources/img/fully-configured-hotkey.PNG)

## Additional info
Not tested on macOS or Linux; results may vary.

If you experience any issues or have any concerns, please reach out to YvesW via a GitHub Issue, by Discord direct messaging or via the RuneLite Discord by mentioning @Yves W.

## Changelog

mm-yyyy:
- No changes yet.



