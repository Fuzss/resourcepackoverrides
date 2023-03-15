# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog].

## [v3.0.3-1.18.2] - 2023-03-15
### Fixed
- Fixed an issue with resource pack ids containing chat formatting codes

## [v3.0.2-1.18.2] - 2023-03-15
### Added
- Added support for defining groups to the `pack_overrides` section
- Added debug keys to the resource packs selection screen
    - Hold `R` to reload the config
    - Hold `D` to toggle showing internal pack ids for the hovered resource pack entry
    - Hold `C` to copy the hovered resource pack id to the clipboard

## [v3.0.1-1.18.2] - 2023-03-14
### Added
- Additional resource pack attributes for overriding: `title`, `description`, `default_position`
### Changed
- Some attributes are no longer ignored when set to `false`
### Fixed
- Fixed crash on game launch on Forge due to invalid Mixin injector

## [v3.0.0-1.18.2] - 2023-02-28
- Ported from 1.19 version

[Keep a Changelog]: https://keepachangelog.com/en/1.0.0/
