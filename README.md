# Resource Pack Overrides

A Minecraft mod. Downloads can be found on [CurseForge](https://www.curseforge.com/members/fuzs_/projects) and [Modrinth](https://modrinth.com/user/Fuzs).

![](https://raw.githubusercontent.com/Fuzss/modresources/main/pages/data/resourcepackoverrides/banner.png)

## About the project
Resource Pack Overrides exists to help mod pack makers with managing pre-installed resource packs. It is not intended to be used with data packs, effectively making it a client-only mod.

## Configuration
Resource Pack Overrides is configured via a single config file located at `.minecraft/config/resourcepackoverrides.json`. The file does **NOT** generate when absent. You do have to create it manually.

Resource Pack Overrides has two main features which can be managed in the mentioned file: 
+ Defining a default list and order of resource packs.
+ Overriding certain resource pack display attributes for the resource pack selection list.

### Resource pack ids

Resource packs are referenced via their internal id. Ids follow the following format: `file/FILE_NAME`. Note that the file extension is included. So, e.g. a pack put at `.minecraft/resourcepacks/my_awesome_pack.zip` has the id `file/my_awesome_pack.zip`. The id for an already enabled pack can easily be found in `options.txt` in the `resourcePacks` option list. 

### Default resource pack list and order
This happens for a new install when `options.txt` is still absent/blank, or after resource pack loading has failed and resets to a bare-bones state.

This is not the same as shipping a pre-configured list in `options.txt`. The list in there will be overridden and reset to vanilla whenever resource pack loading fails, this new method makes sure even then the mod pack defined resource packs stay intact and only additional resource packs enabled by the user are disabled.

The default resource pack list is included in the config file as a JSON array labelled as `default_packs`. Example:

```json
"default_packs": [
    "file/my_awesome_pack.zip",
    "file/my_other_awesome_pack.zip"
]
```

The list is applied in reverse, meaning one resource pack placed above another will appear below that other pack in the resource pack selection screen. 

Only external resource packs from `.minecraft/resourcepacks` must be included in the list, built-in resource packs like `vanilla` and `modresources` (Forge) / `Fabric Mods` (Fabric) are always enabled. They can still be included in the list though for ordering purposes using their respective ids (which do not begin with `file/` since they are internal).

It is important to note, that when including built-in resources in the list to be able to define an order, those resources must always be placed above vanilla assets to allow for mods to override those.

As it's possible when resource pack reloading fails that the issue lies within the resource packs enabled by default, there is a maximum number of times applying the default resource packs list will be tried when recovering from a failed resource pack loading attempt. By default, this is 5 times and can be configured by setting the value `failed_reloads_per_session`. Example:

```json
"failed_reloads_per_session": 5
```

### Overriding resource pack display attributes
Display attribute overrides are configurable in two ways: One possibility is the `default_overrides` block that applies to all resource packs. Example: 
```json
"default_overrides": {
  "force_compatible": true,
  "hidden": false
}
```

A more specific way is the `pack_overrides` section where overrides are configured per individual pack taking precedence over the default block. Example:
```json
"pack_overrides": {
    "file/my_awesome_pack.zip": {
    "hidden": true
  }
}
```

Not all overrides must be defined every time. If absent, the default value will be used. All possible overrides include:

| Attribute          | Description                                                                                                                                                                                          | Default Value |
|--------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|
| `force_compatible` | Guarantees compatibility with the current Minecraft version, no longer shows a warning when an incompatible pack is selected. Pack adapters for legacy pack formats are still applied when selected. | `false`       |
| `fixed_position`   | The pack has a fixed position in the list of enabled resource packs and cannot be moved up or down (might not always work).                                                                          | `false`       |
| `required`         | The pack is required and cannot be disabled once enabled (like the vanilla assets pack).                                                                                                             | `false`       |
| `hidden`           | The pack is hidden on the resource pack screen (useful for enabled packs the user is not supposed to be able to modify in any way).                                                                  | `false`       |

### Complete example
`.minecraft/config/resourcepackoverrides.json`:

```json
{
  "schema_version": 1,
  "failed_reloads_per_session": 5,
  "default_packs": [
    "file/my_awesome_pack.zip",
    "file/my_other_awesome_pack.zip"
  ],
  "default_overrides": {
    "force_compatible": true,
    "fixed_position": false,
    "required": false,
    "hidden": false
  },
  "pack_overrides": {
    "file/my_awesome_pack.zip": {
      "hidden": true
    },
    "file/my_other_awesome_pack.zip": {
      "required": true,
      "hidden": true
    }
  }
}
```
