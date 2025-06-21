@echo off
cls
chcp 65001 >nul

echo Compiling program...

mkdir out\production\Library_management 2>nul

javac -d out\production\Library_management src\app\*.java src\model\*.java src\data\*.java

if errorlevel 1 (
    echo.
    echo Compilation error
    pause
    exit /b
)

cls

echo ==================================================
echo   For better readability, press Alt + Enter
echo   to switch the console to full-screen mode
echo ==================================================

java "-Dfile.encoding=UTF-8" -cp out\production\Library_management app.Main

pause