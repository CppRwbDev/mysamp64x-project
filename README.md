# TuranX64 Mobile Client

TuranX64 is a high-performance, feature-rich mobile client for GTA: San Andreas Multiplayer (SAMP) on Android. This project aims to provide a stable and customizable experience for players on PC-adapted and mobile-first servers.

## Key Features

*   **x64 Support**: Modern architecture support for better performance and compatibility with newer Android versions.
*   **Dynamic Button System**: A unique, fully customizable virtual button configurator. Add any keyboard key (CTRL, SHIFT, ENTER, etc.) directly to your HUD.
*   **Hybrid HUD**: A seamless blend of native Java UI and high-performance C++ (ImGui) rendering.
*   **Advanced Synchronization**: High-fidelity player and vehicle sync logic optimized for mobile networks.
*   **Integrated Optimization**: Built-in FPS fixes, thread affinity management, and efficient resource streaming.

## Getting Started

### Prerequisites
*   Android Studio Ladybug or newer.
*   Android NDK 26b+.
*   CMake 3.22.1+.

### Building
1. Clone the repository.
2. Open the project in Android Studio.
3. Sync Gradle and build the `app` module.
4. Deploy to your Android device.

## Project Structure

*   `app/src/main/cpp/multiplayer`: Core C++ logic, SAMP hooks, and networking.
*   `app/src/main/cpp/multiplayer/gui`: UI system (ImGui-based) and custom widgets.
*   `app/src/main/java/com/turan/game`: Android-specific UI, HUD, and activity management.
*   `docs/`: Detailed project documentation (PM, TIZI, Tasks).

## Recent Updates
*   **Dynamic Button Configurator**: Implemented a menu-driven system to add/remove virtual keys.
*   **Server Migration**: Updated default connection to `188.127.241.74:2838`.
*   **UI Bugfixes**: Resolved overlapping text and visibility issues in the HUD.

## Documentation
For more detailed information, please refer to the files in the `docs/` folder:
- [PM.md](docs/PM.md): Project Management and Vision.
- [TIZI.md](docs/TIZI.md): Technical Infrastructure and System Integration.
- [TASKS.md](docs/TASKS.md): Current development tasks and roadmap.

## License
This project is for educational and entertainment purposes. All original GTA:SA assets belong to Rockstar Games.
