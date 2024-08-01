## YTCraft Minecraft Plugin
It allow the YouTube viewers to control the Minecraft World through the Youtube's Live Chat.
The streamer can setup this plugin on it's minecraft server, and the Livestream can spawn Mobs in the Minecraft world.

### Supported by : Spigot and Paper Servers
----
### How To Setup

Prerequisite : 
Create a Google Developer Account at https://developers.google.com/ and generate a `Google_API_KEY`.
The API-KEY is required for the plugin to interact with the Youtube Server.


1. Download the Plugin `.jar` file and place it inside the `\plugins` folder of the minecraft server file structure.
2. Inside the `/plugins` folder. Create a `YTCraft` folder and create a `YTCraft\config.yml` file.
3. Inside the `config.yml` file, setup the configurations of your plugin.
   ```javascript
   API_KEY:
   /* Paste your Google-API-Key */

   VIDEO_ID:
   /* Paste the Video-Id of the Livestream */
   
   ACTIVE_TIME:
   /* The Chat-Control Active Time as an Array. Example : [5,30] - for 5min 30sec */
   
   REST_TIME:
   /* The Rest-Time. During this, the Chat Control will be disabled. Example: [3,0] - for 3min 0sec */

  
   MOBS:
     0: ['WOLF','ZOMBIE']
     1000: ['RAVAGER']

   /*
      Configure the Specific Minecraft-Mobs for Each no. of viewers. Like this, 
      Above Example :
      If Live Watching > 0 then chat can spawn WOLF and ZOMBIE mobs
      and If Watching > 1000 they can also spawn RAVAGER.
   */
   ```
  5. Once you have configure the `config.yml`. You are done and can now start the Minecraft Server.
  6. To start the Plugin use the command `/ytcraft start` in in-game chat.
---
**Note :**
   - You can setup the `API_KEY` and `Video_ID` later through command.
   - The `Video_ID` can be grabbed from the Youtube URL. `https://www.youtube.com/watch?v={VIDEO_ID}`
   - Configuring the `MOBS` List, you have to follow Standard Minecraft Entity-Type Naming.\
     Example, for Zombiefied Piglin use `ZOMBIEFIED_PIGLIN`
   
---
Also, Check out the [Entity Naming Guide](/markdowns/EntityNaming.md) and [Plugin Commands](/markdowns/PluginCommands.md) 
    
