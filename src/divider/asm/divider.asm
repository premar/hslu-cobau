; import the required external symbols for the system call functions
extern _read
extern _write
extern _exit

global _start

LENGTH EQU 128

Section .data
    QUESTION: db 'Bitte geben Sie eine Zahl ein:', 13, 10
    QUESTIONLEN equ $-QUESTION

    ANSWER: db 'Das Resultat ist: '
    ANSWERLEN equ $-ANSWER

    CRLF    db  10, 13
    CRLFLEN equ $-CRLF

section .bss
    alignb 8
    BUFFER resb LENGTH

section .text

_start:
    mov rdi, QUESTION
    mov rsi, QUESTIONLEN
    call _write

    mov rdi, BUFFER
    mov rsi, LENGTH
    call _read

    mov rdi, BUFFER
    mov rsi, rax
    call atoi

    sar rax, 1

    mov rdi, BUFFER
    mov rsi, rax
    call itoa

    push rax ; rax wird verändert?

    mov rdi, ANSWER
    mov rsi, ANSWERLEN
    call _write

    pop rax

    mov rdi, BUFFER
    mov rsi, rax
    call _write

    mov rdi, CRLF
    mov rsi, CRLFLEN
    call _write

    call exit

; ASCII to Int
;
; in rdi - buffer pointer
; in rsi - buffer length
;
; out rax - int value
atoi:
    push rdi        ; safe rdi
    push rsi        ; safe rsi
    push rdx        ; safe rdx
    push rcx        ; safe rcx
    xor rax, rax    ; register to store int value
    xor rcx, rcx    ; register to store negative flag

atoi_loop:
    xor rdx, rdx    ; register to store char value
    mov dl, [rdi]   ; store buffer char to char register

    cmp dl, 45      ; check char value for '-'
    je atoi_negative; jump to set negative flag

    cmp dl, 48      ; check if value is lower than zero
    jl error        ; jump to error

    cmp dl, 57      ; check if value is higher than nine
    jg error        ; jump to error

    sub dl, 48      ; cast char to int local value

    imul rax, 10    ; move rax by tens digit
    add rax, rdx    ; add local in value to int value

    inc rdi         ; inc buffer pointer
    dec rsi         ; dec buffer counter

    cmp rsi, 1      ; check if last buffer element
    je atoi_check   ; jump to negative check

    jmp atoi_loop   ; still elements in buffer

atoi_negative:
    mov rcx, 1      ; set negative flag to one
    inc rdi         ; inc buffer pointer
    dec rsi         ; dec buffer counter
    jmp atoi_loop   ; jump back to loop

atoi_check:
    cmp rcx, 0      ; check if positive number
    je atoi_end     ; jump to end

    xor rdx, rdx    ; register to store negative value
    sub rdx, rax    ; sub value from zero
    mov rax, rdx    ; store negative back into register

atoi_end:
    pop rcx        ; restore rcx
    pop rdx        ; restore rdx
    pop rsi        ; restore rsi
    pop rdi        ; restore rdi
    ret

; Int to ASCII
;
; in rdi - buffer pointer
; in rsi - int value
;
; out rax - length buffer
itoa:
    push rdi        ; safe rdi
    push rsi        ; safe rsi
    push rcx        ; safe rcx
    push rdx        ; safe rdx
    push r8         ; safe r8
    push r9         ; safe r9
    mov rax, rsi    ; move value into rax register
    xor rcx, rcx    ; register to store counter
    xor r8, r8      ; register to store negative flag
    xor rsi, rsi    ; register to store temp value

    cmp rax, 0      ; check if number negative
    jl itoa_negative; jump to negative number

    jmp itoa_loop1  ; jump to itoa loop

itoa_negative:
    neg rax         ; invert negative value
    mov r8, 1       ; set negative flag to one

itoa_loop1:
    inc rcx         ; inc counter value
    xor rdx, rdx    ; set rdx to zero
    mov rsi, 10     ; set rdx to 10 divisor
    div rsi         ; div rax by 10

    add rdx, 48     ; convert value to ascii

    push rdx        ; safe char to stack

    cmp rax, 0      ; check for last number
    je itoa_check1  ; jump to check negative

    jmp itoa_loop1  ; still numbers in value

itoa_check1:
    cmp r8, 0       ; check if positive number
    je itoa_check2  ; jump to end

    mov rdx, 45     ; add '-' char
    push rdx        ; safe char to stack
    inc rcx         ; inc counter

itoa_check2:
    mov r9, rcx     ; safe counter value

itoa_loop2:
    cmp rcx, 0      ; check if counter zero
    je itoa_end     ; jump to end
    pop rdx         ; get last char value
    mov [rdi], rdx  ; move char to buffer
    inc rdi         ; move buffer pointer
    dec rcx         ; dec counter
    jmp itoa_loop2  ; buffer length

itoa_end:
    mov rax, r9    ; move counter into rax
    pop r9         ; restore r9
    pop r8         ; restore r8
    pop rdx        ; restore rdx
    pop rcx        ; restore rcx
    pop rsi        ; restore rsi
    pop rdi        ; restore rdi
    ret

exit:
    mov rdi, 0
    call _exit

error:
    mov rdi, 99
    call _exit