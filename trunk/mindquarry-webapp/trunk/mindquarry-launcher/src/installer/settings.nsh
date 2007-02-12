Var DOMAIN
Var TITLE
Var APACHE_LOCATION

# functions for the settings page
Function SettingsPage
    !insertmacro MUI_HEADER_TEXT "$(SETTINGS_TITLE)" "$(SETTINGS_SUBTITLE)"
    !insertmacro MUI_INSTALLOPTIONS_DISPLAY "settings.ini"
FunctionEnd

Function ValidateSettings
    # check domain settings
    !insertmacro MUI_INSTALLOPTIONS_READ $DOMAIN "settings.ini" "Field 2" "State"
    ${If} $DOMAIN == ""
        MessageBox MB_ICONEXCLAMATION|MB_OK "$(DOMAIN_ERROR)"
        Abort
    ${EndIf}

    # check title settings
    !insertmacro MUI_INSTALLOPTIONS_READ $TITLE "settings.ini" "Field 4" "State"
    ${If} $TITLE == ""
        MessageBox MB_ICONEXCLAMATION|MB_OK "$(TITLE_ERROR)"
        Abort
    ${EndIf}
    
    # check Apache settings
    !insertmacro MUI_INSTALLOPTIONS_READ $APACHE_LOCATION "settings.ini" "Field 6" "State"
    ${If} $APACHE_LOCATION == ""
        MessageBox MB_ICONEXCLAMATION|MB_OK "$(APACHE_LOCATION_ERROR)"
        Abort
    ${EndIf}
    
    ${DirState} "$APACHE_LOCATION" $R0
    ${If} $R0 == -1
        MessageBox MB_ICONEXCLAMATION|MB_OK "$(APACHE_LOCATION_ERROR)"
        Abort
    ${EndIf}
FunctionEnd

Function ApplySettings
    MessageBox MB_ICONINFORMATION|MB_OK "$INSTDIR"
FunctionEnd
