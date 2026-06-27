if(NOT TARGET shadowhook::shadowhook)
add_library(shadowhook::shadowhook SHARED IMPORTED)
set_target_properties(shadowhook::shadowhook PROPERTIES
    IMPORTED_LOCATION "/home/darkdev/.gradle/caches/transforms-3/1adceab8c7f32be996e574bc726ba6a9/transformed/shadowhook-1.0.9/prefab/modules/shadowhook/libs/android.arm64-v8a/libshadowhook.so"
    INTERFACE_INCLUDE_DIRECTORIES "/home/darkdev/.gradle/caches/transforms-3/1adceab8c7f32be996e574bc726ba6a9/transformed/shadowhook-1.0.9/prefab/modules/shadowhook/include"
    INTERFACE_LINK_LIBRARIES ""
)
endif()

