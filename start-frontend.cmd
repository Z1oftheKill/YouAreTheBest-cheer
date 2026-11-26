@echo off
chcp 65001 >nul
cd /d "%~dp0frontend"
echo [Cheer Frontend] Starting Vite Dev Server on http://localhost:5173 ...
call npm run dev
pause

