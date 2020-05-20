## SafeChat

"Keep your chat safe!"

## Features

This plugin currently provides the following features , all aim towards protecting your chat from dangerous content:

 1. IPv4 addresses check (blacklist & whitelist)
 2. IPv6 addresses check (in work)
 3. Domains check (blacklist & whitelist)
 4. Words blacklist
 5. Words replacement
 6. Per check warning messages
 7. Per check hover messages
 8. Per check executables
 9. Different databases support
 10. Auto-Save feature
 11. Real time checks\config.yml reloading
 12. Console logging
 13. Config comments explanations

## IPv4 Addresses check
This check uses a high performance **RegEx** to check and match any blacklisted IPv4 inside chat messages.
If you want to allow any specific IPv4 address to not trigger this check you can simply add them in the `addresses.whitelisted` section from the config.yml.
NOTE: Both the blacklist and whitelist sections of this check support and use RegEx.
This check can be disabled

# Domains Check
This check uses a high performance **RegEx** to check and match any domain inside chat messages.
To avoid possible *plugin's permissions* accidentally matching in the chat, at the end of the RegEx all major TLDs have to be specified by hand.
All the major ones are there, but if you desire to insert some other specific ones, you can simply add them from the `domains.regex` section.
This check can be disabled

# Words Blacklist & Replacement
This plugin uses a smart system to automatically find **forbidden** words, and block them , or even better replace them!
Taking the advantage of the great power of RegEx you can create a custom check to match a word\a specific part of the word and specify a replacement for the found match;
you can also write **'NONE'** as a replacement to only cancel the message. The config comes with **3 pre-made** samples, so you can better understand how this works:

![words](https://i.imgur.com/Tg52ZRb.png)

# Warning messages
Every check that has been previously listed, has a warning message section.
This section contains a list of messages that will be sent to the player upon failing any of the chat checks.
They currently support the `%PLAYER%` placeholder, which will get replaced with the player's username.
The plugin also supports color codes by using the `&` placeholder.
<br>You can find a list of color codes here: https://www.digminecraft.com/lists/color_list_pc.php
<br>NOTE: if you desire to not send any message, you can use a YAML empty array (this -> `[]`).

# Hover messages

Every check that has been previously listed, also has a warning message section right below the message one.
This section contains a list of messages that will be added to the current message as **Hover** text.
What does that mean?
<br>When your mouse will hover on the warning messages, the text written in the hover message section will be displayed to the user!
They currently support the `%PLAYER%` placeholder, which will get replaced with the player's username.
The plugin also supports color codes by using the `&` placeholder (same as warning messages).
<br>NOTE: if you desire to not send any message, you can use a YAML empty array (this -> `[]`).
<br>You also can't have a hover specified if the warning message is empty (it doesn't make sense, right?)!

# Executables Objects

Every check that has been previously listed, also has a section named `executables`, right in the bottom of the check.
This feature allows you to specify a custom list of commands that shall be executed at a given number of check flags.
<br>Every executable object has a list of commands in the `executables.commands` that support the `%PLAYER%` placeholder.
You can specify the number of flags required to trigger the commands in the `executables.flags` section.

![executables](https://i.imgur.com/r8y34gS.png)

# Databases Support
At the beginning of the config.yml file, you will find a `database` section, which is really important, because it allows you to specify your preferred database type.
This plugin uses databases in order to store player flags, the default one is **SQLite** which will generate a local database into the plugin's folder.

There also is support for other databases, such as:
- MySQL
- PostgreSQL
- MongoDB (coming soon)

In order to properly use any of these you'll need to configure the database access and credentials sections.
You'll need first to specify the preferred DB type , then you need to edit the username, password, port, address, and database fields, and you will be ready to go!

# Auto-Save feature
This plugin has a smart feature that makes you able to automatically save your data onto the database with a given interval.
This is mainly to avoid data loss in case of eventual system\server crashes.
You can find this section at `database.auto-save`, note that this interval is in minutes.

# Real-time reloading

This plugin has a great feature that allows you to modify and update the values from the config.yml at any time.
In order to achieve this you can use the `/safechat reload` command, either from console or in-game. This will update every value, checks included.

This is really useful as it allows you to enable\disable checks, or modify them to your liking without restarting the whole server.

# Console logging

This feature comes disabled by default, however you can set this to true from the config.yml file.
What this does is enabling a basic logging info for failed checks. Every check , when failed will send a message to the console with the player and the name of the failed check.
 
# Commands and Permissions

This plugin comes with in-game or console commands in order to help the users read their data.
Every command starts with `safechat` and can be used from both console and in-game.

* sql search
<br> - This command allows you to search for a specific player and get his current flags.
<br> You can use this syntax in order to retrieve a specific flag count from the player
<br> - Usage: `/safechat sql search [ipv4|domains|words] <player>`
<br> You can user this other syntax to get all flags from the player
<br> - Usage: `/safechat sql search <player>`
<br> ![search](https://i.imgur.com/tYJTokW.png)
* sql top
<br> - This comand allows you to get a list of the user with the highest count of the specified flag
<br> - Usage: `/safechat sql top <number> [ipv4|domains|words]`
<br> ![top](https://i.imgur.com/fGTvoID.png)

# Incoming features

- GUIs [Priority: LOW]
- Clear player data [Priority: HIGH]
- More databases support [Priority: LOW]
- 1.16 RGB support [Priority: LOW]

## YAML Configuration file
This plugin's configuration will be automatically generated in your server inside the SafeChat/ folder.
The file is called config.yml and contains important settings that allow you to modify your plugin checks:

 ## Issues
 You can report any issue on this plugin issues page , remember to provide these essentials info that will help me provide help:
 
 - If you have any console error, please paste it on a website (such as pastebin, hastebin etc..)
 - Remember to specify your minecraft server version and fork name
 - Remember to specify the JVM version of your server
 - Be polite
 ---
 Thank you , TheViperShow.
