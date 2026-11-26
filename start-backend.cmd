@echo off
chcp 65001 >nul
set "JAVA_HOME=%USERPROFILE%\.jdks\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo [Cheer Backend] Starting Spring Boot 3 with JDK 21 on http://localhost:8080 ...
call "D:\IntelliJ IDEA 2023.3.2\plugins\maven\lib\maven3\bin\mvn.cmd" -f "%~dp0backend\pom.xml" spring-boot:run
pause

