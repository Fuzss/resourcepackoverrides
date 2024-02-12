# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog].

## [v8.0.3-1.20.1] - 2024-02-12
### Fixed
- Fix order of hidden packs changing when closing the resource packs screen

## [v8.0.2-1.20.1] - 2023-08-05
### Changed
- The `resourcepackoverrides.json` file now generates as an empty file when absent
- `schema_version` is now `2`, also a wrong format will no longer prevent a config from loading, it will be attempted to be made compatible
- The identifier for groups must now begin with `$$` instead of just a single `$`
### Fixed
- Fixed packs that are hidden by default on Forge showing up even when they have not been changed in the config

## [v8.0.1-1.20.1] - 2023-08-05
### Fixed
- Fixed order buttons showing when all but one packs are hidden

## [v8.0.0-1.20.1] - 2023-06-27
- Ported to Minecraft 1.20.1

[Keep a Changelog]: https://keepachangelog.com/en/1.0.0/
