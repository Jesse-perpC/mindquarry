Function DirState
    !define DirState `!insertmacro DirStateCall`
 
    !macro DirStateCall _PATH _RESULT
        Push `${_PATH}`
        Call DirState
        Pop ${_RESULT}
    !macroend
 
    Exch $0
    Push $1
    ClearErrors
 
    FindFirst $1 $0 '$0\*.*'
    IfErrors 0 +3
    StrCpy $0 -1
    goto end
    StrCmp $0 '.' 0 +4
    FindNext $1 $0
    StrCmp $0 '..' 0 +2
    FindNext $1 $0
    FindClose $1
    IfErrors 0 +3
    StrCpy $0 0
    goto end
    StrCpy $0 1
 
    end:
    Pop $1
    Exch $0
FunctionEnd
