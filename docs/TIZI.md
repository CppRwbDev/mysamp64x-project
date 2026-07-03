# Technical Infrastructure and System Integration (TIZI)

## System Architecture
TuranX64 operates on a complex integration of native Android layers and low-level game engine hooks.

### Core Components
1.  **Native Bridge (JNI)**: Handles communication between the Java UI (`com.turan.game.ui.Hud`) and the native C++ engine (`main.cpp`).
2.  **ImGui GUI System**: A high-performance UI library used for complex in-game menus, chat, and the new button selector.
3.  **Hooks Layer**: Uses `armhook` to intercept `libGTASA.so` functions, enabling custom sync logic, rendering modifications, and input handling.
4.  **Networking (RakNet)**: Manages the BitStream-based communication with SAMP servers.

## Integration Details
### Dynamic Button System
*   **Java Side**: The "|||" button in `hud.xml` triggers a JNI call `showButtonSelector()`.
*   **C++ Side**: `UI` class manages the `ButtonSelector` widget. When a key is added, it's stored in `ButtonPanel` and saved to `custom_buttons.ini`.
*   **Input Injection**: Buttons in `ButtonPanel` inject keys directly into the `LocalPlayerKeys.bKeys` array, which is read by the patched `CPad` functions.

### Resource Streaming
*   Managed via `CStreaming` and `CdStream`.
*   Hooks in `Streaming.cpp` allow for pre-loading of custom assets and controlling memory management to prevent crashes on modern devices.

## Build System
*   **Gradle**: Manages Android dependencies and packaging.
*   **CMake**: Orchestrates the compilation of the massive C++ codebase, targeting both `arm64-v8a` and `armeabi-v7a`.

## Storage
*   Config files and textures are stored in `/Android/data/com.turan.game/files/SAMP/`.
*   Key settings: `settings.ini`, `custom_buttons.ini`.
