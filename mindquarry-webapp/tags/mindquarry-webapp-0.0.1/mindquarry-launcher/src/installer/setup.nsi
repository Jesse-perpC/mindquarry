Name "Mindquarry"

# Defines
!define REGKEY "SOFTWARE\$(^Name)"
!define VERSION "0.0.1-SNAPSHOT"
!define COMPANY "Mindquarry GmbH"
!define URL "http://www.mindquarry.com"
!define DESCRIPTION "Launcher for the Mindquarry Collaboration Server. The Mindquarry Collaboration Server enables teams to collaborate using document management, wikis, task management and conversation management."
!define COPYRIGHT "Mindquarry GmbH"

# MUI defines
!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_BITMAP "images\header.bmp"
!define MUI_WELCOMEFINISHPAGE_BITMAP "images\welcome.bmp"
!define MUI_UNWELCOMEFINISHPAGE_BITMAP "images\welcome.bmp"
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install-blue.ico"
!define MUI_LICENSEPAGE_RADIOBUTTONS
!define MUI_STARTMENUPAGE_REGISTRY_ROOT HKLM
!define MUI_STARTMENUPAGE_NODISABLE
!define MUI_STARTMENUPAGE_REGISTRY_KEY "Software\Mindquarry Collaboration Server"
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME StartMenuGroup
!define MUI_STARTMENUPAGE_DEFAULT_FOLDER "Mindquarry Collaboration Server"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall-blue.ico"
!define MUI_UNFINISHPAGE_NOAUTOCLOSE
!define MUI_LANGDLL_REGISTRY_ROOT HKLM
!define MUI_LANGDLL_REGISTRY_KEY ${REGKEY}
!define MUI_LANGDLL_REGISTRY_VALUENAME InstallerLanguage
!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_FINISHPAGE_RUN
!define MUI_FINISHPAGE_RUN_TEXT $(START_LINK)
!define MUI_FINISHPAGE_RUN_FUNCTION "StartServer"
!define MUI_FINISHPAGE_SHOWREADME "$INSTDIR\README.txt"

# Included files
!include Sections.nsh
!include MUI.nsh

# Reserved Files
!insertmacro MUI_RESERVEFILE_LANGDLL
ReserveFile "${NSISDIR}\Plugins\AdvSplash.dll"

ReserveFile "settings.ini"
!insertmacro MUI_RESERVEFILE_INSTALLOPTIONS

# Variables
Var StartMenuGroup
;Var INI_VALUE

# Installer pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE ../assembly/txt/LICENSE.txt
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_STARTMENU Application $StartMenuGroup
#Page custom SettingsPage
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

!insertmacro MUI_UNPAGE_WELCOME
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES
!insertmacro MUI_UNPAGE_FINISH

# Installer languages
!insertmacro MUI_LANGUAGE English
!insertmacro MUI_LANGUAGE German

# Installer attributes
OutFile "Mindquarry Collaboration Server.exe"
InstallDir $PROGRAMFILES\mindquarry
CRCCheck on
XPStyle on
ShowInstDetails show
VIProductVersion 0.0.1.0
VIAddVersionKey /LANG=${LANG_ENGLISH} ProductName "Mindquarry Collaboration Server"
VIAddVersionKey ProductVersion "${VERSION}"
VIAddVersionKey /LANG=${LANG_ENGLISH} CompanyName "${COMPANY}"
VIAddVersionKey /LANG=${LANG_ENGLISH} CompanyWebsite "${URL}"
VIAddVersionKey /LANG=${LANG_ENGLISH} FileVersion "${VERSION}"
VIAddVersionKey /LANG=${LANG_ENGLISH} FileDescription "${DESCRIPTION}"
VIAddVersionKey /LANG=${LANG_ENGLISH} LegalCopyright "${COPYRIGHT}"
InstallDirRegKey HKLM "${REGKEY}" Path
ShowUninstDetails show

# Installer sections
Section -Main SEC0000
    SetOutPath $INSTDIR
    SetOverwrite on
    File /r C:\mindquarry-0.0.1-SNAPSHOT\*
    WriteRegStr HKLM "${REGKEY}\Components" Main 1
SectionEnd

Section -post SEC0001
    WriteRegStr HKLM "${REGKEY}" Path $INSTDIR
    WriteUninstaller $INSTDIR\uninstall.exe
    !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
    SetOutPath $SMPROGRAMS\$StartMenuGroup
    CreateShortcut "$SMPROGRAMS\$StartMenuGroup\$(^UninstallLink).lnk" $INSTDIR\uninstall.exe
    SetOutPath $INSTDIR
    CreateShortcut "$SMPROGRAMS\$StartMenuGroup\$(START_LINK).lnk" $INSTDIR\start.bat
    CreateShortcut "$SMPROGRAMS\$StartMenuGroup\$(STOP_LINK).lnk" $INSTDIR\stop.bat
    !insertmacro MUI_STARTMENU_WRITE_END
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayName "$(^Name)"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayVersion "${VERSION}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" Publisher "${COMPANY}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" URLInfoAbout "${URL}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayIcon $INSTDIR\uninstall.exe
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" UninstallString $INSTDIR\uninstall.exe
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoModify 1
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoRepair 1
SectionEnd

# Macro for selecting uninstaller sections
!macro SELECT_UNSECTION SECTION_NAME UNSECTION_ID
    Push $R0
    ReadRegStr $R0 HKLM "${REGKEY}\Components" "${SECTION_NAME}"
    StrCmp $R0 1 0 next${UNSECTION_ID}
    !insertmacro SelectSection "${UNSECTION_ID}"
    GoTo done${UNSECTION_ID}
next${UNSECTION_ID}:
    !insertmacro UnselectSection "${UNSECTION_ID}"
done${UNSECTION_ID}:
    Pop $R0
!macroend

# Uninstaller sections
Section /o un.Main UNSEC0000
    # stop server if it is running
    ExecWait "$INSTDIR\stop.bat"
    
    # remove sources
    RmDir /r /REBOOTOK $INSTDIR\*
    DeleteRegValue HKLM "${REGKEY}\Components" Main
SectionEnd

Section un.post UNSEC0001
    DeleteRegKey HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\$(^UninstallLink).lnk"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\$(START_LINK).lnk"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\$(STOP_LINK).lnk"
    Delete /REBOOTOK $INSTDIR\uninstall.exe
    DeleteRegValue HKLM "${REGKEY}" StartMenuGroup
    DeleteRegValue HKLM "${REGKEY}" Path
    DeleteRegKey /IfEmpty HKLM "${REGKEY}\Components"
    DeleteRegKey /IfEmpty HKLM "${REGKEY}"
    RmDir /REBOOTOK $SMPROGRAMS\$StartMenuGroup
    RmDir /REBOOTOK $INSTDIR
SectionEnd

# Installer functions
Function .onInit
    InitPluginsDir
    Push $R1
    File /oname=$PLUGINSDIR\spltmp.bmp "images\splash.bmp"
    advsplash::show 4000 10 400 -1 $PLUGINSDIR\spltmp
    Pop $R1
    Pop $R1
    !insertmacro MUI_LANGDLL_DISPLAY
    
    ;Extract InstallOptions INI files
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "settings.ini"
FunctionEnd

#Function SettingsPage
#  !insertmacro MUI_HEADER_TEXT "$(TEXT_SETTINGS_TITLE)" "$(TEXT_SETTINGS_SUBTITLE)"
#  !insertmacro MUI_INSTALLOPTIONS_DISPLAY "settings.ini"
#FunctionEnd

# Uninstaller functions
Function un.onInit
    ReadRegStr $INSTDIR HKLM "${REGKEY}" Path
    ReadRegStr $StartMenuGroup HKLM "${REGKEY}" StartMenuGroup
    !insertmacro MUI_UNGETLANGUAGE
    !insertmacro SELECT_UNSECTION Main ${UNSEC0000}
FunctionEnd

# cusomt functions
Function StartServer
    ExecShell "" "$INSTDIR\start.bat"
FunctionEnd

# Installer Language Strings
# TODO Update the Language Strings with the appropriate translations.

LangString ^UninstallLink ${LANG_ENGLISH} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_GERMAN} "Deinstallieren der $(^Name)"

# cusomt page strings
LangString TEXT_SETTINGS_TITLE ${LANG_ENGLISH} "Settings"
LangString TEXT_SETTINGS_SUBTITLE ${LANG_ENGLISH} "Please enter your settings."

LangString TEXT_SETTINGS_TITLE ${LANG_GERMAN} "Einstellungen"
LangString TEXT_SETTINGS_SUBTITLE ${LANG_GERMAN} "Bitte geben sie ihre Einstellungen ein."

# start menu strings
LangString START_LINK ${LANG_ENGLISH} "Start Mindquarry Collaboration Server"
LangString STOP_LINK ${LANG_ENGLISH} "Stop Mindquarry Collaboration Server"

LangString START_LINK ${LANG_GERMAN} "Mindquarry Collaboration Server starten"
LangString STOP_LINK ${LANG_GERMAN} "Mindquarry Collaboration Server beenden"
