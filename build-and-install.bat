@echo off
cd /d "%~dp0"

where java >nul 2>nul
if errorlevel 1 (
  echo Java not found. Install JDK 21 from https://adoptium.net ^(Temurin 21^), then run this file again.
  pause & exit /b 1
)

set GRADLE_VER=8.12.1
set GRADLE_HOME_LOCAL=%~dp0.gradle-local\gradle-%GRADLE_VER%

if not exist "%GRADLE_HOME_LOCAL%\bin\gradle.bat" (
  echo Downloading Gradle %GRADLE_VER% ^(one time, about 130 MB^)...
  if not exist ".gradle-local" mkdir ".gradle-local"
  powershell -NoProfile -ExecutionPolicy Bypass -Command "[Net.ServicePointManager]::SecurityProtocol=[Net.SecurityProtocolType]::Tls12; Invoke-WebRequest 'https://services.gradle.org/distributions/gradle-%GRADLE_VER%-bin.zip' -OutFile '.gradle-local\gradle.zip'; Expand-Archive -Force '.gradle-local\gradle.zip' '.gradle-local'"
  if errorlevel 1 (
    echo Gradle download failed. Check your internet connection.
    pause & exit /b 1
  )
)

call "%GRADLE_HOME_LOCAL%\bin\gradle.bat" build --no-daemon
if errorlevel 1 (
  echo.
  echo Build failed. Copy the text above and send it to me.
  pause & exit /b 1
)

set JAR_NAME=shara-client-extended-0.1.0.jar
if not exist "build\libs\%JAR_NAME%" (
  echo Build succeeded but JAR not found: build\libs\%JAR_NAME%
  pause & exit /b 1
)

set MCDIR=%APPDATA%\.minecraftnset MODS_DIR=%MCDIR%\mods
if not exist "%MODS_DIR%" mkdir "%MODS_DIR%"
copy /Y "build\libs\%JAR_NAME%" "%MODS_DIR%\"
echo.
echo Done. JAR copied to %MODS_DIR%\%JAR_NAME%
echo Put Fabric API into the same mods folder and start the fabric-loader-1.21.4 profile.
explorer "%MODS_DIR%"
pause