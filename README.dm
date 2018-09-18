# **DemoProject**

## **A structure of project modules:**
- install - Base application module to install the app on a device (basically doesn't have any source code, has dependences to each other modules);
- instant - Base application module to run the app via an url (doesn't have any source code; to run the app in play market "try now");
- base - The module has base functions which are used by each other feature modules (musn't be large);
- main - Has main logic for the app;
- repository - The library manages storage data; 
- storage - It's a library to provide data; 
## **Standart directories:**
- view:
  - activity - A directory for application activities (Android component for full screen);
  - fragment - A directory for application fragments (Android component for a part of a screen);
  - model - Contains ViewModels for an activity/fragment (Android architecture component);