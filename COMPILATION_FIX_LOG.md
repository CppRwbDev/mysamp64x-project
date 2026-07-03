# Project Update Log - TURAN ARM-32x64

**Oxirgi yangilanish:** 2026-07-03 17:07:35

## Amalga oshirilgan ishlar:

1.  **FPS Info funksiyasi to'liq olib tashlandi:**
    *   Foydalanuvchi talabiga binoan, krasga sabab bo'lgan va kerak bo'lmagan FPS/POS/MEM paneli koddan butunlay o'chirildi.
    *   `gui.cpp`, `gui.h` va `settings.cpp` fayllari avvalgi barqaror (stable) holatiga qaytarildi.
    *   Chatdagi `/fpsinfo` buyrug'i olib tashlandi.

2.  **Spidometr uchun GetSpeed() saqlab qolindi:**
    *   Dastlabki kompilyatsiya xatosini tuzatish uchun qo'shilgan `CVehicle::GetSpeed()` metodi saqlab qolindi.
    *   `UI::ShowSpeed()` funksiyasi optimallashtirildi va xavfsiz holatga keltirildi. Endi u kras bermasdan faqat transport tezligini yuboradi.

3.  **Loyiha barqarorlashtirildi:**
    *   Barcha xavfli xotira murojaatlari (null pointers) olib tashlandi.
    *   Loyiha optimallashgan va kraslarsiz holatga keltirildi.

---
**Status:** Oldingi barqaror holatga qaytarildi (Rollback successful).
