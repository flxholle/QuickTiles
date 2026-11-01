# Changelog

All notable changes to this fork of QuickTiles. The format loosely follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## 2.0.0 - 2026-11-02

The release that makes Quick Settings tiles work while QuickTiles is **not** running, and that
turns the CI into a release pipeline.

### Android 17 support

* Tile services no longer depend on in-memory state. The current value of a tile is read from the
  system on every tap instead of being cached between clicks, so a tile service that the system
  creates fresh (which happens on every tap once the app process is gone) still has everything it
  needs.
* `getQsTile()` is never dereferenced blindly anymore. SystemUI is allowed to bind a tile service
  without handing out a tile; that used to throw and kill the whole app process, which made every
  tile of the app dead until it was opened again.
* `onClick` is final and wraps every tile action in a catch-all. A failing tile now shows a toast
  with the exception instead of silently taking the process down.
* `unlockAndRun` is only used when the device is actually locked. The keyguard callback is not
  reliable on every OEM build, and it is never invoked when no secure lock is set - tiles that
  relied on it did nothing at all.
* Tiles that start an activity use `startActivityAndCollapse(PendingIntent)` on Android 14+, which
  is the only variant that still gets the background-activity-start exemption.

### Volume panel tile

* **Fixed the tile that did nothing at all.** It called
  `adjustStreamVolume(STREAM_MUSIC, ADJUST_SAME, FLAG_SHOW_UI)`; since Android 13 the platform
  drops the UI request for apps that are not in the foreground, so the call succeeded without
  showing anything. The panel is now started as an activity instead.
* The tile shows the **short media volume slider** again (like a volume key press) through a
  transparent, instantly finishing activity, instead of the all-streams settings panel.
* **Miscellaneous → Expanded volume panel** brings back the big panel with every stream (media,
  ring, notification, alarm, call).
* **Miscellaneous → Keep Quick Settings open** asks the system for the slider straight from the tile
  service, so the Quick Settings panel is not collapsed first. Works only on ROMs that still honour
  background volume-UI requests.

### Diagnostics

* New **Settings → Troubleshooting → Tile diagnostics**: a ring buffer of the last 120 tile events
  (`onCreate`, `onBind`, `onClick`, `onStartListening`, `onStopListening`, `onDestroy`), newest
  first, with **Copy** and **Share**.
* The app records how its previous process ended (`ApplicationExitInfo`), whether it is in the
  foreground, and every uncaught exception with its first stack frames - so a background crash,
  which is invisible otherwise, can be read after the next start.
* A one line verdict on top of the log separates "the system never reached the app" from "the tile
  itself failed".
* **Settings → Troubleshooting → Why do my tiles stop working?**: battery, autostart, lock in the
  recent apps and Developer options (Don't keep activities / Background process limit), with
  shortcuts to the app info and battery pages.
* Tile callbacks are mirrored to logcat with the tag `QuickTiles`.

### Builds and releases

* CI builds a **signed release APK** instead of a debug APK.
* Every push to `master` publishes the APK as a GitHub release `v<versionName>`; other branches and
  pull requests still upload it as a run artifact. Tag pushes and manual runs can publish any tag.
* All builds share one cached keystore, so a new APK installs over the previous version without
  uninstalling.
* `compileSdk`/`targetSdk` 36, `minSdk` 24, versionCode 26.

## 1.9.0

* First attempt at making tiles work with the app closed: state handling, `getQsTile()` guards,
  permission dialogs instead of crashes, `ACTIVE_TILE` metadata removed.
* Debug APK workflow for GitHub Actions.

## Earlier

See the upstream project history: <https://github.com/flxholle/QuickTiles>.
