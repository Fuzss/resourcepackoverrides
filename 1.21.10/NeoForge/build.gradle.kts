plugins {
    id("fuzs.multiloader.multiloader-convention-plugins-neoforge")
}

multiloader {
    mixins {
        clientAccessor(
            "PackNeoForgeAccessor"
        )
    }
}
