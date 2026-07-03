# Tasks and Roadmap

## Completed Tasks (Recent)
- [x] Implement JNI bridge for Dynamic Button System.
- [x] Create `ButtonSelector` menu with search/list functionality.
- [x] Add persistence for custom buttons via `custom_buttons.ini`.
- [x] Resolve UI overlapping in selector list.
- [x] Fix touch registration for `ButtonPanel`.
- [x] Update default server IP in `Turan.h`.

## Active Tasks (Optimization Phase)
- [/] **Streaming Buffer Optimization**: Tweak `CdStream` sectors and buffer sizes for smoother object loading.
- [ ] **Thread Affinity Management**: Refine `CFPSFix` to target high-performance CPU cores.
- [ ] **Dynamic Sync Rates**: Implement logic in `localplayer.cpp` to adjust network send frequency based on activity.
- [ ] **UI Redraw Caching**: Optimize ImGui rendering to reduce overhead when the HUD is static.

## Backlog / Future Roadmap
- [ ] **Visual HUD Editor**: Allow users to drag-and-drop buttons to any position on the screen.
- [ ] **Advanced Crash Logger**: Implement a more detailed stack trace collector for easier debugging.
- [ ] **Mod Manager**: Built-in support for loading custom .img and .txd files directly from the app.
- [ ] **Voice Chat Enhancement**: Refine voice quality and noise cancellation in the integrated voice plugin.

## Known Issues
- [ ] Rare "flying meshes" during high-speed driving (under investigation in `CStreaming`).
- [ ] Texture missing warnings for certain textdraw backgrounds (server-side dependency).
