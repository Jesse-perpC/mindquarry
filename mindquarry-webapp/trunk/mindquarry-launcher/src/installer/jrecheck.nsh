Var JREPath
Var InstallJRE

!define JRE_VERSION "1.6.0"

Function CheckInstalledJRE
  MessageBox MB_OK "Checking Installed JRE Version"
  Push "${JRE_VERSION}"
  Call DetectJRE
  Messagebox MB_OK "Done checking JRE version"
  Exch $0   # Get return value from stack
  StrCmp $0 "0" NoFound
  StrCmp $0 "-1" FoundOld
  Goto JREAlreadyInstalled
  
FoundOld:
  MessageBox MB_OK "Old JRE found" 
  
  !insertmacro MUI_INSTALLOPTIONS_WRITE "jre.ini" "Field 1" "Text" "$(^Name) requires a more recent version of the Java Runtime Environment than the one found on your computer. The installation of JRE ${JRE_VERSION} will start."
  !insertmacro MUI_HEADER_TEXT "$(JRE_TITLE)" "$(JRE_SUBTITLE)"
  !insertmacro MUI_INSTALLOPTIONS_DISPLAY_RETURN "jre.ini"
  Goto MustInstallJRE
 
NoFound:
  MessageBox MB_OK "JRE not found"
  !insertmacro MUI_INSTALLOPTIONS_WRITE "jre.ini" "Field 1" "Text" "No Java Runtime Environment could be found on your computer. The installation of JRE v${JRE_VERSION} will start."
  !insertmacro MUI_HEADER_TEXT "$(JRE_TITLE)" "$(JRE_SUBTITLE)"
  !insertmacro MUI_INSTALLOPTIONS_DISPLAY_RETURN "jre.ini"
  Goto MustInstallJRE
 
MustInstallJRE:
  Exch $0   # $0 now has the installoptions page return value
  
  # Do something with return value here
  Pop $0    # Restore $0
  StrCpy $InstallJRE "yes"
  Return
  
JREAlreadyInstalled:
  MessageBox MB_OK "JRE already installed"
  StrCpy $InstallJRE "no"
  !insertmacro MUI_INSTALLOPTIONS_WRITE "jre.ini" "UserDefinedSection" "JREPath" $JREPath
  Pop $0        # Restore $0
  Return
FunctionEnd

; Returns: 0 - JRE not found. -1 - JRE found but too old. Otherwise - Path to JAVA EXE
 
; DetectJRE. Version requested is on the stack.
; Returns (on stack)    "0" on failure (java too old or not installed), otherwise path to java interpreter
; Stack value will be overwritten!
Function DetectJRE
  Exch $0   ; Get version requested  
        ; Now the previous value of $0 is on the stack, and the asked for version of JDK is in $0
  Push $1   ; $1 = Java version string (ie 1.5.0)
  Push $2   ; $2 = Javahome
  Push $3   ; $3 and $4 are used for checking the major/minor version of java
  Push $4
  MessageBox MB_OK "Detecting JRE"
  ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  MessageBox MB_OK "Read : $1"
  StrCmp $1 "" DetectTry2
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$1" "JavaHome"
  MessageBox MB_OK "Read 3: $2"
  StrCmp $2 "" DetectTry2
  Goto GetJRE
 
DetectTry2:
  ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
  MessageBox MB_OK "Detect Read : $1"
  StrCmp $1 "" NoFound
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$1" "JavaHome"
  MessageBox MB_OK "Detect Read 3: $2"
  StrCmp $2 "" NoFound
 
GetJRE:
; $0 = version requested. $1 = version found. $2 = javaHome
  MessageBox MB_OK "Getting JRE"
  IfFileExists "$2\bin\java.exe" 0 NoFound
  StrCpy $3 $0 1            ; Get major version. Example: $1 = 1.5.0, now $3 = 1
  StrCpy $4 $1 1            ; $3 = major version requested, $4 = major version found
  MessageBox MB_OK "Want $3 , found $4"
  IntCmp $4 $3 0 FoundOld FoundNew
  StrCpy $3 $0 1 2
  StrCpy $4 $1 1 2          ; Same as above. $3 is minor version requested, $4 is minor version installed
  MessageBox MB_OK "Want $3 , found $4" 
  IntCmp $4 $3 FoundNew FoundOld FoundNew
 
NoFound:
  MessageBox MB_OK "JRE not found"
  Push "0"
  Goto DetectJREEnd
 
FoundOld:
  MessageBox MB_OK "JRE too old: $3 is older than $4"
;  Push ${TEMP2}
  Push "-1"
  Goto DetectJREEnd  
FoundNew:
  MessageBox MB_OK "JRE is new: $3 is newer than $4"
 
  Push "$2\bin\java.exe"
;  Push "OK"
;  Return
   Goto DetectJREEnd
DetectJREEnd:
    ; Top of stack is return value, then r4,r3,r2,r1
    Exch    ; => r4,rv,r3,r2,r1,r0
    Pop $4  ; => rv,r3,r2,r1r,r0
    Exch    ; => r3,rv,r2,r1,r0
    Pop $3  ; => rv,r2,r1,r0
    Exch    ; => r2,rv,r1,r0
    Pop $2  ; => rv,r1,r0
    Exch    ; => r1,rv,r0
    Pop $1  ; => rv,r0
    Exch    ; => r0,rv
    Pop $0  ; => rv 
FunctionEnd
