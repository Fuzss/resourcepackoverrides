# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v21.11.0-1.21.11] - 2025-12-16

### Changed

- Update to Minecraft 1.21.11
- Using the debug keys now requires both `Ctrl` / `Cmd` and the respective key to be held due to the addition of a
  search box to the pack selection screen, which would otherwise consume the text input

### Fixed

- Fix hiding packs leading to some unintended side effects, such as moving packs on the pack selection screen not
  moving the intended pack when it was blocked by a hidden pack
