@ECHO off

:: ********************************Please set the following configuration Path************************
:: NOTE: DO NOT change variable names
:: Jenkins will set CONFIGS_DELIVERY_HOME environment variable if this batch is running in a Jenkins job
:: jobName get from jenkins
:: SET CONFIGS_DELIVERY_HOME=
:: SET jobName=
:: SET buildWorkspace=
:: ***************************************************************************************************
:: Declare variables start
:: Set configuration path for target artifact
SET targetPath=%CONFIGS_DELIVERY_HOME%\%buildWorkspace%\uaa\build\libs
:: Set configuration path for destination
SET destination=%CONFIGS_DELIVERY_HOME%\%jobName%
:: Declare variables end

:: Start running script
CALL :copyWarToDestination
CALL :renameWarFile

EXIT %ERRORLEVEL%

:: Declare methods start
:copyWarToDestination
  ::SET specifiesTheFile=/COPYALL /B /SEC /MIR *.war
  SET specifiesTheFile=/MIR /XX *.war
  SET copyOptions=/R:3 /W:5 /LOG:%destination%\RoboLog.log /NS /NC /NDL
  ROBOCOPY %targetPath% %destination% %specifiesTheFile% %copyOptions% >NUL
  SET/A errlev="%ERRORLEVEL% & 24"
  IF %errlev% NEQ 0 (
	  ECHO Delivery %config_name% Failed!
	  EXIT %errlev%
  )
  SET ERRORLEVEL=%errlev%
  GOTO :EOF
  
:renameWarFile
  SET old_file_name=cloudfoundry-identity-uaa-2.7.1.war
  SET new_file_name=uaa.war
  IF EXIST %destination%\%old_file_name% (
	REN %destination%\%old_file_name% %new_file_name%
  )
  GOTO :EOF
:: Declare methods end