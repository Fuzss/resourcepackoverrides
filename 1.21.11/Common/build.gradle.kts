plugins {
    id("fuzs.multiloader.multiloader-convention-plugins-common")
}

multiloader {
    mixins {
        clientMixin(
            "MinecraftMixin",
            "OptionsMixin",
            "PackRepositoryMixin"
        )
    }
}
