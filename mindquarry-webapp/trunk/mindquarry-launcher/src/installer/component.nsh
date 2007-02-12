# additional installer components
Section $(DOCS_LINK) INSTALL_DOCS
    SectionIn 1
    
    SetOutPath "$INSTDIR\docs"
    SetOverwrite on
    File /r C:\mindquarry-windows-docs\*
    WriteRegStr HKLM "${REGKEY}\Components" Docs 1
    
    !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
        SetOutPath $SMPROGRAMS\$StartMenuGroup
        SetOutPath $INSTDIR
        CreateShortcut "$SMPROGRAMS\$StartMenuGroup\$(DOCS_FOLDER)\$(DOCS_LINK).lnk" $INSTDIR\docs\features.pdf
    !insertmacro MUI_STARTMENU_WRITE_END
SectionEnd

Section /o un.Docs UNINSTALL_DOCS
    RmDir /r /REBOOTOK $INSTDIR\docs\*
    DeleteRegValue HKLM "${REGKEY}\Components" Docs
SectionEnd
