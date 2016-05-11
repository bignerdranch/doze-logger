## Doze Logger

Simple app used to demonstrate the effects of doze mode on your background schedulers.

App displays persisted log messages from the app that chronicle events relating to Doze and the background schedulers. Log messages can be updated by hitting "Refresh" in the options menu.

Bottom of the app allows the user to schedule the following background tasks:
 - Repeating JobScheduler Job that kicks off every 10 minutes
 - One off JobScheduler Job that requires the device to be plugged in and has a deadline of 10 minutes
 - Inexact repeating AlarmManager Alarm every 15 minutes
 - One time AlarmManager Alarm scheduled 10 minutes from now, scheduled with `setExactAndAllowWhileIdle()`

The user can also:
 - Pop up request to whitelist app (when app is already whitelisted will direct the user to battery optimization settings)
 - Cancel all of the alarms and jobs.

 The app will log out when the device enters and exits idle mode.