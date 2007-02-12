# additional installer components
Section "Apache + Subversion" INSTALL_XAMPP
    SectionIn 1
    
    SetOutPath "$INSTDIR\xampp\"
    File C:\xampp-installer\output\xampp-mindquarry-win32-installer.exe
    ExecWait "$INSTDIR\xampp\xampp-mindquarry-win32-installer.exe"
    WriteRegStr HKLM "${REGKEY}\Components" Xampp 1
SectionEnd

Section /o un.Xampp UNINSTALL_XAMPP
    RmDir /r /REBOOTOK $INSTDIR\xampp\*
    DeleteRegValue HKLM "${REGKEY}\Components" Xampp
SectionEnd

Section $(DOCS_LINK) INSTALL_DOCS
    SectionIn 2
    
    SetOutPath "$INSTDIR\docs"
    SetOverwrite on
    File /r C:\mindquarry-windows-docs\*
    WriteRegStr HKLM "${REGKEY}\Components" Docs 1
    
    !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
        SetOutPath $SMPROGRAMS\$StartMenuGroup
        SetOutPath $INSTDIR
        CreateDirectory "$SMPROGRAMS\$StartMenuGroup\$(DOCS_FOLDER)"
        CreateShortcut "$SMPROGRAMS\$StartMenuGroup\$(DOCS_FOLDER)\$(DOCS_LINK).lnk" $INSTDIR\docs\features.pdf
    !insertmacro MUI_STARTMENU_WRITE_END
SectionEnd

Section /o un.Docs UNINSTALL_DOCS
    RmDir /r /REBOOTOK $INSTDIR\docs\*
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\$(DOCS_FOLDER)\$(DOCS_LINK).lnk"
    RmDir /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\$(DOCS_FOLDER)"
    DeleteRegValue HKLM "${REGKEY}\Components" Docs
SectionEnd
