
@echo off

::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
set f=evm

:: S=Source, R=Results, MCR=MatlabCompilerRuntime


set MCR=C:\Program Files\MATLAB\MATLAB Compiler Runtime\v80\runtime\win64

:: MCR path is preceded by any other path in system Path 
:: this prevents from other matlab or MCR installation from getting in the way
set PATH=%MCR%;%PATH%

set verNum=v80
:: set RDIR=Results
set RDIR=./

:: mkdir %RDIR%
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::


::----------------------------------------------------------
:: cv_sample2.mp4 video with ideal_filter
set inFile=%1

%f% %inFile% %RDIR% 25 color 50/60 60/60 50 ideal 1 6