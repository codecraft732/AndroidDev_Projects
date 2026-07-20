# Doc Scanner Using ML-KIT

## What This App Does
1. User taps **"Scan Document"**
2. Google ML Kit's built-in scanner UI opens (camera, crop, filter — you don't write any of this, it's a Google library)
3. User finishes scanning
4. App saves the result **permanently**:
    - Each scanned page → `Pictures/DocScanner/` (shows up in your Gallery app)
    - Combined PDF → `Documents/DocScanner/` (shows up in your Files app)
5. Scanned pages are displayed on-screen in a scrollable list

---

## How the Flow Connects (Code-Level)

```
Button click
→ scanner.getStartScanIntent()
→ scannerLauncher.launch(...)          [opens Google's scanner UI]
→ onResult callback fires when user is done
→ extract page URIs + pdf URI from result
→ saveImageToGallery() for each page
→ savePdfToDocuments() for the pdf
→ imageUris state updated → Compose recomposes → images show on screen
```


---

## Required Setup Checklist

- [ ] Copy the permission lines from `AndroidManifest_permissions.xml` into your real `AndroidManifest.xml`
- [ ] Make sure `minSdk` in `build.gradle` is **21+** (required by ML Kit Document Scanner)
- [ ] Confirm these dependencies exist in `build.gradle`:
    - `com.google.android.gms:play-services-mlkit-document-scanner`
    - `io.coil-kt.coil3:coil-compose` (for `AsyncImage`)
- [ ] Test on a real device or emulator with Google Play Services — this API does **not** work on emulators without Play Store

---
