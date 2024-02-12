# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v20.4.2-1.20.4] - 2024-02-12
### Changed
- No longer replace internal packs, instead modify the existing packs to avoid losing custom data added by mods like grouping information

## [v20.4.1-1.20.4] - 2024-02-12
### Fixed
- Fix order of hidden packs changing when closing the resource packs screen
- Fix compatibility with latest Fabric Api

## [v20.4.0-1.20.4] - 2024-01-05
- Ported to Minecraft 1.20.4
### Added
- Added support for NeoForge
- Added a new debug key (`T`) for restoring default resource packs list and order
### Fixed
- Fixed debug keys partially working on screens other than the resource pack selection screen
