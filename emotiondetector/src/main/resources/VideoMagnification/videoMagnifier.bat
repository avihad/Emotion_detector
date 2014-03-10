
@echo off

::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
set f=%resourcePath%\\VideoMagnification\\evm

:: S=Source, R=Results, MCR=MatlabCompilerRuntime


set MCR=C:\Program Files\MATLAB\MATLAB Compiler Runtime\v80\runtime\win64

:: MCR path is preceded by any other path in system Path 
:: this prevents from other matlab or MCR installation from getting in the way
set PATH=%MCR%;%PATH%

set verNum=v80
:: set RDIR=Results
set RDIR=%outputPath%

:: mkdir %RDIR%
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::


::----------------------------------------------------------
:: cv_sample2.mp4 video with ideal_filter
set inFile=%1
set lowFreq=%2
set highFreq=%3

%f% %inFile% %RDIR% 29 color %lowFreq% %highFreq% 50 ideal 1 1

type NUL > %RDIR%\finishMagnifierExe
exit