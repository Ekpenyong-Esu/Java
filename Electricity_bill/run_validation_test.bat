@echo off
echo ===== Starting ValidationUtil Test Runner =====
echo 1. Temporarily enabling tests in POM file...

powershell -Command "(gc pom.xml) -replace '<maven.test.skip>true</maven.test.skip>', '<maven.test.skip>false</maven.test.skip>' | Out-File -encoding UTF8 pom.xml.temp"
powershell -Command "(gc pom.xml.temp) -replace '<skipTests>true</skipTests>', '<skipTests>false</skipTests>' | Out-File -encoding UTF8 pom.xml.new"
copy /Y pom.xml pom.xml.backup >nul
copy /Y pom.xml.new pom.xml >nul
del pom.xml.temp >nul
del pom.xml.new >nul

echo 2. Running ValidationUtil test only...
call mvn test -Dtest="Electricity.util.ValidationUtilTest" -DskipTests=false

echo 3. Restoring original POM file...
copy /Y pom.xml.backup pom.xml >nul
del pom.xml.backup >nul

echo ===== Test run completed =====
pause