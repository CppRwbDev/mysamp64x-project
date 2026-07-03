# SAMP Android Client: Professional Optimization Strategy (v1.0)

Ushbu hujjat SAMP Android klientini sanoat standartlari asosida optimallashtirish rejasini belgilaydi. Maqsad — GTA SA Android dvigatelini (libGTASA.so) zamonaviy qurilmalar uchun moslashtirish va maksimal FPSga erishish.

---

## 🏗️ 1-Bosqich: Yadroni Hook Qilish (Engine Hooking)
O'yinning ichki funksiyalarini ushlab qolish va ularni samaraliroq variantlar bilan almashtirish.

### 1.1. RenderWare State Caching
*   **Muammo:** O'yin har frame uchun GPU'ga minglab `RwRenderStateSet` buyruqlarini yuboradi (masalan, AlphaBlend yoqish), hatto u allaqachon yoqilgan bo'lsa ham.
*   **Yechim:** `RwRenderStateSet` funksiyasini hook qilish va joriy holatlarni massivda kesh qilish.
*   **Natija:** GPU'ga yuklama 15-20% ga kamayadi.

### 1.2. FPS Unlocker & Frame Pacing
*   **Muammo:** Standart 30 FPS limiti va kadrlar orasidagi jitter (micro-stuttering).
*   **Yechim:** `CGame::Process` va `CTimer` funksiyalarini hook qilish. 60/90/120 Hz ekranlar uchun moslashuvchan DeltaTime hisoblash tizimini joriy etish.
*   **Natija:** O'yin silliqligi (smoothness) sezilarli darajada oshadi.

### 1.3. Fast Math (Neon SIMD)
*   **Muammo:** Matritsa va vektor hisoblari (koordinatalar, soyalar) standart CPU instruksiyalari bilan sekin ishlaydi.
*   **Yechim:** `RwMatrixMultiply` va boshqa matematik funksiyalarni ARM Neon (SIMD) assembly kodlari bilan almashtirish.

---

## 🎨 2-Bosqich: Grafika va Renderlash (Graphics Pipeline)

### 2.1. Frustum Culling Optimallashtirish
*   **Mantiq:** `CRenderer::ConstructRenderList` funksiyasida faqat kamera ko'rib turgan obyektlarni saralash.
*   **Texnika:** O'yinning standart culling algoritmini yanada tajovuzkor (aggressive) qilish, ayniqsa uzoqdagi kichik obyektlar uchun.

### 2.2. Texture Streaming & Compression
*   **Yondashuv:**
    *   `ASTC` va `ETC2` tekstura formatlaridan foydalanish.
    *   Tekstura keshini (Texture Cache) boshqarish: tez-tez ishlatiladigan skinlar va mashinalarni RAM'da saqlash.
    *   `CStreaming::FreeAllUnusedModels` funksiyasini har 60 soniyada yoki xotira to'lganda chaqirish.

### 2.3. Shadow & Reflection Optimization
*   **Amallar:** Dinamik soyalarni (Real-time shadows) soddalashtirilgan `Static Shadow` larga almashtirish yoki past sifatli qurilmalarda butunlay o'chirish.

---

## 📦 3-Bosqich: Xotira va Resurs Boshqaruvi (Memory & Pools)

### 3.1. Object Pool Expansion (Custom Allocator)
*   **Vazifa:** `CPools::Initialise` funksiyasini hook qilib, limitlarni oshirish (Vehicles: 50 -> 200, Peds: 100 -> 300).
*   **Optimizatsiya:** Standart `malloc/free` o'rniga maxsus `Pool Allocator` ishlatish (Memory fragmentationni oldini olish uchun).

### 3.2. Ped & Vehicle Streaming
*   **Mantiq:** Uzoqdagi personaj va mashinalarni o'chirish (`Destroy`), yaqinlashganda esa ularni keshdan yuklash (`Create`). Bu orqali bir vaqtning o'zida render qilinadigan entitilar sonini nazorat qilish.

---

## 🖥️ 4-Bosqich: UI va HUD Optimizatsiyasi

### 4.1. Dirty Flag Rendering
*   **Konsept:** HUD (Radar, Health Bar, Money) va Chat komponentlari har bir frame'da qayta chizilmaydi.
*   **Yechim:** Ma'lumot o'zgargandagina (masalan, HP kamaysa) chizish flagini yoqish (`isDirty = true`).

### 4.2. ImGui Efficiency
*   **Qoidalar:**
    *   `gui.cpp` ichidagi `ImGui::Begin` va `ImGui::End` bloklarini minimallashtirish.
    *   Fontlarni bir marta yuklab, keshdan foydalanish (`Font Cache`).

---

## 🌐 5-Bosqich: Network & System (RakNet & JNI)

### 5.1. Packet Batching
*   **Texnika:** RakNet paketlarini navbatga (Queue) yig'ish va ularni guruhlab yuborish. Bu modem/WiFi modulining yukini kamaytiradi.

### 5.2. JNI Call Minimization
*   **Qoida:** Java (Android UI) va Native (C++) orasidagi aloqa "qimmat" turadi. Shuning uchun barcha logikani C++ ichida bajarish va faqat yakuniy natijani Java'ga yuborish.

---

## 🛠️ Nazorat va Testlash (Verification)

1.  **Profiling:** `Android Profiler` orqali CPU va GPU vaqtini o'lchash.
2.  **Stability:** 100+ o'yinchi bor joylarda 1 soat davomida o'yinni test qilish.
3.  **Low-end Test:** 2GB RAM va Helio P35 chipli telefonlarda FPS barqarorligini tekshirish.

---
**Muallif:** AI Optimization System (Professional Developer Approach)
**Sana:** 2024-yil iyun
