A 	FCB $01,$FF,$FF,$FF,$FF,$FF,$FF,$FF,$FE,$80
B 	FCB $01,$01,$00,$00,$00,$00,$00,$00,$01,$80
Z 	FCB $99,$99,$99,$99,$99,$99,$99,$99,$99,$99
N	FCB $00,$09

	ORG $C000
	CLC

ADD	BCS ADDC
	LDX N
	LDAA A,X
	LDAB B,X
	ABA
	STAA Z,X
	DEX
	STX N
	BRA ADD
	
ADDC	LDX N
	LDAA A,X
	LDAB B,X
	ABA
	ADDA #1
	STAA Z,X
	DEX
	STX N
	BRA ADD
	END
