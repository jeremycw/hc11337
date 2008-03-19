	ORG $0000
N 	FCB $02,$00

	ORG $E000
	LDX #N
	PSHX
	JSR S2
LOOP	BRA LOOP

S2	PULX ;take the pc off the stack
	PULY ;take #N of the stack
	PSHX ;put the pc on the stack
	LDX Y ;load the value of N into x
	DEX ;decrement
	STX Y ;store the value to #N
	RTS ;return
