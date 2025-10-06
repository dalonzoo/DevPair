# Launcher Icon Generation

## Problem Solved
Removed the `README_ICONS.md` file from the `mipmap-mdpi` folder because Android only accepts `.xml` or `.png` files in resource folders.

## How to Generate Icons

### Option 1: Android Studio Image Asset Studio (Recommended)

1. **Right-click** on `app` folder in Android Studio
2. **New → Image Asset**
3. Choose:
   - **Icon Type**: Launcher Icons (Adaptive and Legacy)
   - **Foreground Layer**: `Clip Art` or upload image
   - **Background Layer**: Solid Color `#1976D2` (DevPair blue)
4. Click **Next** → **Finish**

Android Studio will automatically generate all densities (mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi).

### Option 2: Online Tool

Use: https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html

1. Upload an image or choose clipart
2. Configure padding and colors
3. Download ZIP
4. Extract into `app/src/main/res/` folder

### Option 3: Use Existing Adaptive Icons

The app already uses adaptive icons defined in:
- `app/src/main/res/drawable/ic_launcher_foreground.xml` (code brackets icon)
- `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml` (adaptive icon)
- `app/src/main/res/values/ic_launcher_background.xml` (background color)

On Android 8.0+ (API 26+) these adaptive icons will work automatically.

### Note for Building

To build without legacy PNG icons (pre-Android 8):
- The app will build correctly
- On Android 8.0+ it will use adaptive icons
- On Android 7.x and earlier it might use the system default icon

**For a production app, generate complete icons with Image Asset Studio.**

## Quick Fix: Minimal Icons

If you just want to build the app quickly without custom icons, Android Studio will automatically use the existing adaptive icons on Android 8+.

Per Android 7.x, puoi aggiungere manualmente dei PNG placeholder, ma non è necessario per il MVP demo.
