#!/usr/bin/env bash
set -e
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

if ! command -v java >/dev/null 2>&1; then
  echo "Java not found. Install JDK 21 (Temurin 21) and re-run this script."
  exit 1
fi

GRADLE_VER=8.12.1
GRADLE_HOME_LOCAL="$SCRIPT_DIR/.gradle-local/gradle-$GRADLE_VER"

if [ ! -x "$GRADLE_HOME_LOCAL/bin/gradle" ]; then
  echo "Downloading Gradle $GRADLE_VER (one time, ~130MB)..."
  mkdir -p .gradle-local
  curl -fsSL "https://services.gradle.org/distributions/gradle-$GRADLE_VER-bin.zip" -o .gradle-local/gradle.zip
  unzip -o .gradle-local/gradle.zip -d .gradle-local
fi

"$GRADLE_HOME_LOCAL/bin/gradle" build --no-daemon

JAR_NAME=shara-client-extended-0.1.0.jar
if [ ! -f "build/libs/$JAR_NAME" ]; then
  echo "Build succeeded but JAR not found: build/libs/$JAR_NAME"
  exit 1
fi

MC_DIR="$HOME/.minecraft"
MODS_DIR="$MC_DIR/mods"
mkdir -p "$MODS_DIR"
cp -f "build/libs/$JAR_NAME" "$MODS_DIR/"

echo "Done. JAR copied to $MODS_DIR/$JAR_NAME"
echo "Put Fabric API into the same mods folder and start the fabric-loader-1.21.4 profile."
xdg-open "$MODS_DIR" 2>/dev/null || open "$MODS_DIR" 2>/dev/null || true
