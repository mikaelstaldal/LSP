@echo off

rem Windows and OS/2 batch script for running lspc

rem SET LSP_HOME=D:\lsp

java -classpath %CLASSPATH%;bcel.jar;%LSP_HOME%\lspc.jar;%LSP_HOME%\lsprt.jar nu.staldal.lsp.compiler.LSPCompilerCLI %1 %2 %3 %4 %5 %6 %7 %8 %9
