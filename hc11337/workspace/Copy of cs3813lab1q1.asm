PORTC	EQU $1003
PORTB	EQU $1004
DDRC	EQU $1007
	ORG $0000
	CLR PORTB
	CLR PORTC
	LDAA #0

COPYIN	LDAA PORTC
	STAA PORTB
	BRA COPYIN
