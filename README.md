## SafeChat

"Keep your chat safe!"

## Features

This plugin currently provides the following features , all aim towards protecting your chat from dangerous content:

 1. IPv4 addresses check
 2. IPv6 addresses check (in work)
 3. Domains check
 4. Domains configurable whitelist
 5. Domains blacklist configurable regex
 6. IPv4 configurable whitelist
 7. Words blacklist
 8. Words match and replace feature

The IPv4 addresses check will block any form of valid IPv4 in the game chat, this to avoid other people spamming their server. The localhost IPv4s "127.0.0.1" and "0.0.0.0" are whitelisted by default as they're commonly used to indicate local addresses which are not harmful for your server, but of course you can remove them.

The domains check blocks any message that contains a domains, it also blocks domain with commas instead of dots as they look very similar in game. The check is completely configurable and with a little of knowledge and research about regular expressions, you can improve it or modify to your likings. The default whitelisted domains are: "**play.hypixel.net**" and "**www.minecraft.net**"

## YAML Configuration file
This plugin's configuration will be automatically generated in your server inside the SafeChat/ folder.
The file is called config.yml and contains important settings that allow you to modify your plugin checks:
![config.yml file](https://i.imgur.com/7hqmWiW.png)
## Database features
SafeChat is currently able to use a PostgreSQL or SQLite database to manage player flags and warnings.
You should only be putting it on enabled if you're planning to use it!
Current features:

 - Per player flags
 - Player flags search
 - Top flags search
 - Data Configuration reload
 - Completely customizable RegEx checks
 
 Every player in your server will have a flags counter, which represents how many times he has tried to break the chat rules.
 SafeChat provides command to get the current flags of a specific player, or even better to get the players with the most flags list:
 ![SafeChat syntax](https://i.imgur.com/y6VLLDY.png)
 
 Example of **sql top** command:
 
 ![enter image description here](https://i.imgur.com/MEwv86D.png)
 ---
 ## Issues
 You can report any issue on this plugin issues page , remember to provide these essentials info that will help me provide help:
 
 - If you have any console error, please paste it on a website (such as pastebin, hastebin etc..)
 - Remember to specify your minecraft server version and fork name
 - Remember to specify the JVM version of your server
 - Be polite
 ---
 Thank you , TheViperShow.
