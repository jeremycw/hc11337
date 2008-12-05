/***************************************************************************
 *   The Motorola Freeware Assembler                              
 *         
 *   do12.c --- MC68HC12 Specific Processing
 *
 ***************************************************************************/

#include <string.h>
#include <stdio.h>

#include  "vars.h"
#include  "funcs.h"
#include  "table12.h"

char PROG_NAME[5] = "as12";
/*
 *  Function Prototypes
 */
void do_op (int opcode, int clss);
int is_index_reg (char *);
int do_indexed (int);
int do_xfer_ex (int);
int parse_reg (int);
int do_gen (int, int);
int bitop (int, int, int);

/*
 *  Variables and defines local to this module
 */
 
char hexstr1[] = { "012345678" };

/*                      addressing modes                    */

#define IMMED   100
#define INDX    101             /* for indexed x register */
#define LIMMED  103             /* long immediate */
#define OTHER   104
#define UNUSED  105

/*
 * do_op --- process mnemonic
 * 
 * Called with the base opcode and its class. Optr points to the beginning of
 * the operand field.
 */

void
do_op (int opcode, int clss)
{
  unsigned short dist;          /* relative branch distance */
  int amode;                    /* indicated addressing mode */
  int amode2;                   /* indicated addressing mode of second */
  /* operand in move instructions */
  char *peek, *peek2;
  char string[200];
  char comma_ctr;
  int Temp;

/*
 * Take an educated guess at addressing mode. If there is a
 * comma in the operand and an x, y, s, or p follows, then the
 * assembler assumes indexed addressing off of that letter.
 */

  peek = Optr;
  amode2 = OTHER;
  amode = OTHER;
  comma_ctr = 0;
  if ((clss == MVIW) || (clss == MVIB)) {
    peek2 = Operand;
    while ((!any (*peek2, " \t")) && (*peek2 != NULL))
      peek2++;
    strcpy (string, peek2);
    if (strncmp ("pshb", Op, 4) == 0) {
      *peek2 = 0;
      strcat (Operand, " 1,-sp");
      strcat (Operand, string);
    }
    if (strncmp ("pshw", Op, 4) == 0) {
      *peek2 = 0;
      strcat (Operand, " 2,-sp");
      strcat (Operand, string);
    }
    if (strncmp ("pulb", Op, 4) == 0) {
      strcpy (string, "1,sp ");
      strcat (string, Operand);
      strcpy (Operand, string);
    }
    if (strncmp ("pulw", Op, 4) == 0) {
      strcpy (string, "2,sp ");
      strcat (string, Operand);
      strcpy (Operand, string);
    }
  }

/*  This section changes comma-delimited code into space-delimited code.  */

  suck_out_commas ();
  peek = Optr;
  while ((!delim (*peek)) && (*peek != NULL)) {
    switch (*peek) {
    case ',':
      peek++;
      if (is_index_reg (peek) == 1) {
        amode2 = amode;
        amode = INDX;
      }
      peek++;
      comma_ctr++;
      break;

    case '#':
      peek++;
	  if ((clss == MVIW) || (clss == MVIB)) { // TAA ADDED
		  amode2 = amode;
		  amode = IMMED;
	  }											// TAA ADDED
      break;

    default:
      peek++;
      break;
    }
    peek = skip_white(peek);
  }
  if ((clss == MVIW) || (clss == MVIB)) {
  }
  else {
    if (*Operand == '#') {
      amode = IMMED;
    }
  }
  switch (clss) {
  case MVIW:
  case MVIB:
    emit (PAGE2);

/*
 * Covers only the case of mov #imm ext
 */
    if ((amode == IMMED) && (amode2 == OTHER)) {
      emit (opcode);
      Optr++;
      if (Debug)
        printf ("eval2\n");
      eval ();
      if (*(Op + 3) == 'b') {
        emit (lobyte (Result));
      }
      else {
        eword (Result);
      }
      Optr = skip_white (Optr);
      if (Debug)
        printf ("eval3\n");
      eval ();
      eword (Result);
      return;
    }

/*
 * Covers the case of mov #imm indx
 */
    if ((amode2 == IMMED) && (amode == INDX) && (comma_ctr == 1)) {
      emit (opcode - 3);
      Optr++;
      if (Debug)
        printf ("eval4\n");
      eval ();
      Temp = Result;
      Optr = skip_white (Optr);
      do_indexed (1);
      if (*(Op + 3) == 'b') {
        emit (lobyte (Temp));
      }
      else {
        eword (Temp);
      }
      return;
    }

/*
 *  Covers only the case of mov ext ext
 */

    if ((amode == OTHER) && (amode2 == OTHER) && (comma_ctr == 0)) {
      emit (opcode + 0x1);
      if (Debug)
        printf ("eval5\n");
      eval ();
      eword (Result);
      Optr = skip_white (Optr);
      if (Debug)
        printf ("eval6\n");
      eval ();
      eword (Result);
      return;
    }

/*
 *  Covers the case of mov indx indx
 */
    if ((amode == INDX) && (amode2 == INDX) && (comma_ctr == 2)) {
      emit (opcode - 0x1);
      do_indexed (1);
      Optr = skip_white (Optr);
      do_indexed (1);
      return;
    }

/*
 *  Covers the case of mov indx ext
 *  Covers the case of mov ext indx
 */
    if ((((amode == OTHER) && (amode2 == INDX))
         || ((amode2 == OTHER) && (amode == INDX))) && (comma_ctr == 1)) {
      peek = Optr;
      string[0] = 0;            /* used as temp here */
      while (*peek != ',') {
        if (delim (*peek))
          break;
        peek++;
        if (*peek == ',')
          string[0] = 1;
      }
      if (string[0] == 1) {     /* then indexed comes first */
        emit (opcode + 0x2);
        do_indexed (1);
        Optr = skip_white (Optr);
        if (Debug)
          printf ("eval7\n");
        eval ();/*Won't eval mov indexed"*/
        eword (Result);
        return;
      }
      else {                    /* then the tab or whitespace came before the comma */
        emit (opcode - 0x2);
        if (Debug)
          printf ("eval8\n");
        eval ();
        Temp = Result;
        Optr = skip_white (Optr);
        do_indexed (1);
        eword (Temp);
        return;
      }
    }
    error ("Illegal addressing mode for movb/movw opcode.");
    return;

  case TTFR:
    peek = Optr;
    while ((!any (*peek, ";:\n")) && (*peek != NULL)) {
      if (*peek == ',')
        *peek = ' ';
      peek++;
    }
    switch (mapdn (*Op)) {
    case 'x':
      if (!strcmp (Op + 2, "dx")) {
        strcpy (Operand, "d x");
      }
      else {
        strcpy (Operand, "d y");
      }
      strcpy (Op, "exg");
      break;
    case 't':
      switch (mapdn (*(Op + 1))) {
      case 'a':
        strcpy (Operand, "a ");
        break;

      case 'b':
        strcpy (Operand, "b ");
        break;

      case 'p':
        strcpy (Operand, "ccr ");
        break;

      case 'x':
        strcpy (Operand, "x ");
        break;

      case 'y':
        strcpy (Operand, "y ");
        break;

      case 's':
        strcpy (Operand, "sp ");
        break;
      }
      switch (mapdn (*(Op + 2))) {
      case 'a':
        strcat (Operand, "a ");
        break;

      case 'b':
        strcat (Operand, "b ");
        break;

      case 'p':
        strcat (Operand, "ccr ");
        break;

      case 'x':
        strcat (Operand, "x ");
        break;

      case 'y':
        strcat (Operand, "y ");
        break;

      case 's':
        strcat (Operand, "sp ");
        break;
      }
      strcpy (Op, "tfr");
      break;
    }

  case RTOR:
    peek = Optr;
    while ((!any (*peek, ";:\n")) && (*peek != NULL)) {
      if (*peek == ',')
        *peek = ' ';
      peek++;
    }
    switch (amode) {
    case IMMED:
      sprintf (errbuf, "Immediate mode not allowed for opcode %s.", Op);
      error (errbuf);
      break;

    default:
      emit (opcode);
      if (!(strcmp ("swpb", Op))) {
        strcat (Operand, " ");
        strcpy (string, Operand);
        strcat (Operand, string);
        emit (lobyte (do_xfer_ex (0x80)));
        Optr = Operand;
        switch (do_xfer_ex (0x0)) {
        case 0x44:              /* might be other values */
          break;

        default:
          error ("Swapb must be specified with D-accumulator only!");
          break;
        }
        return;
      }
      if (!(strcmp ("exg", Op))) {
        emit (lobyte (do_xfer_ex (0x80)));
        return;
      }
      if (!(strcmp ("sex", Op))) {
        emit (lobyte (do_xfer_ex (0x0)));
        Optr = Operand;
        switch (do_xfer_ex (0x0)) {
        case 0x04:
        case 0x05:
        case 0x06:
        case 0x07:
        case 0x14:
        case 0x15:
        case 0x16:
        case 0x17:
        case 0x24:
        case 0x25:
        case 0x26:
        case 0x27:
        case 0x03:
        case 0x13:
        case 0x23:
          break;

        default:
          sprintf (errbuf, "Opcode %s must move from 8-bit to 16-bit regs.",
                   Op);
          error (errbuf);
          break;
        }
        return;
      }
      if (!(strcmp ("tfr", Op))) {
        emit (lobyte (do_xfer_ex (0)));
        return;
      }
    }
    break;

  case BPM:
    switch (*Op) {              /* first check to see what the increment option is */
    case 'i':
      amode = 0x80;
      break;

    case 't':
      amode = 0x40;
      break;

    default:
      amode = 0x0;
      break;
    }
    if (*(Op + 2) == 'n') {     /* if n then bit 1 of postbyte = 1 */
      amode = amode | 0x20;
    }
    amode = parse_reg (0) | amode;      /* fill in post byte */

/*
 *  At this point we have the register and now only need to
 *  decode the branch destination.
 */

    Optr++;
    Optr = skip_white (Optr);
    if (Debug)
      printf ("eval9\n");
    eval ();
    dist = Result - (Pc + 3);
    emit (opcode);

    if ((dist > 255 && dist <= 0xff00) && Pass == 2)
      error ("Branch target out of range.");
    if (dist > 0x7fff)
      amode = amode | 0x10;
    emit (lobyte (amode));
    emit (lobyte (dist));
    break;

  case P2INON:
    emit (PAGE2);
    if (!(strcmp ("tbl", Op))) {
      emit (opcode);
      do_indexed (1);
      return;
    }
    if (!(strcmp ("etbl", Op))) {
      emit (opcode);
      do_indexed (1);
      return;
    }

  case TLEA:
    if (!(strcmp ("abx", Op))) {
      strcpy (Op, "leax");
      strcpy (Operand, "b,x");
      amode = INDX;
    }
    if (!(strcmp ("aby", Op))) {
      strcpy (Op, "leay");
      strcpy (Operand, "b,y");
      amode = INDX;
    }
    if (!(strcmp ("ins", Op))) {
      strcpy (Op, "leas");
      strcpy (Operand, "1,sp");
      amode = INDX;
    }
    if (!(strcmp ("des", Op))) {
      strcpy (Op, "leas");
      strcpy (Operand, "-1,sp");
      amode = INDX;
    }                           /* fall through! */

  case INDEXED:
    switch (amode) {
    case INDX:
      emit (opcode);
      do_indexed (0);
      break;

    default:
      sprintf (errbuf, "Opcode %s can only use indexed addressing mode.", Op);
      error (errbuf);
      break;
    }
    break;

  case P2INH:
    emit (PAGE2);               /* fall through! */

  case INH:                     /* inherent addressing */
    emit (opcode);
    break;

  case REL:                     /* relative branches */
    if (Debug)
      printf ("eval10\n");
    eval ();
    dist = Result - (Pc + 2);
    emit (opcode);
    if ((dist > 127 && dist < 0xff80) && Pass == 2) {
      error ("Branch target out of range.");
      emit (lobyte (-2));
      break;
    }
    emit (lobyte (dist));
    break;

  case P2REL:                   /* LONG relative branches */
    if (Debug)
      printf ("eval11\n");
    eval ();
    dist = Result - (Pc + 4);
    emit (PAGE2);
    emit (opcode);

/*
 *  The following test was removed.  You cannot create a
 *  long-branch out-of-range condition, since the CPU12 can
 *  long-branch to any address in the memory map.
 */
// if ((dist > 0x7FFF || dist < -0x8000) && Pass == 2) {
//  error("Long branch target out of range.");
//  eword(0);
//  break;
// }
    eword (dist);
    break;

  case CCRC:
    if ((*(Op) == 'c') && (*(Op + 1) == 'l')) {
      amode = IMMED;
      switch (*(Op + 2)) {
      case 'i':
        strcpy (Operand, "#$EF");
        break;

      case 'v':
        strcpy (Operand, "#$FD");
        break;

      case 'c':
        strcpy (Operand, "#$FE");
        break;

      default:
        sprintf (errbuf, "Bad clear opcode %s; must be cli, clv, or clc.",
                 Op);
        error (errbuf);
        break;
      }
      strcpy (Op, "andcc");
    }
    else {
      amode = IMMED;
      switch (*(Op + 2)) {
      case 'i':
        strcpy (Operand, "#$10");
        break;

      case 'v':
        strcpy (Operand, "#$02");
        break;

      case 'c':
        strcpy (Operand, "#$01");
        break;

      default:
        sprintf (errbuf, "Bad set opcode %s; must be sei, sev, or sec.", Op);
        error (errbuf);
        break;
      }
      strcpy (Op, "orcc");
    }

  case 2:                       /* had to use 2 here because IMM was used twice in defs */
    if (amode != IMMED) {
      sprintf (errbuf, "Opcode %s must use immediate mode.", Op);
      error (errbuf);
      break;
    }
    emit (opcode);
    Optr++;
    if (Debug)
      printf ("eval12\n");
    eval ();
    emit (Result);
    break;

  case P2GEN:
    emit (PAGE2);               /* fall through! */

  case LONGIMM:
    if (amode == IMMED) {
      amode = LIMMED;
    }                           /* fall through! */


  case NOIMM:
    if (!strcmp (Op, "lbsr")) {
      amode = INDX;
      Optr = Operand;
      if (Debug)
        printf ("eval13\n");
      eval ();
      Optr = Operand;
      sprintf (Operand, ">%d-*,pc", Result - 4);
      strcpy (Op, "jsr");
    }
    if (amode == IMMED) {
      sprintf (errbuf, "Immediate mode not allowed for opcode %s.", Op);
      error (errbuf);
      break;
    }                           /* fall through! */

  case GEN:                     /* general addressing */
    do_gen (opcode, amode);
    break;

  case P2G2:
    emit (PAGE2);               /* fall through! */

  case GRP2:
    switch (amode) {
    case INDX:
      emit (opcode);
      do_indexed (0);
      break;

    case LIMMED:
    case IMMED:
      sprintf (errbuf, "Immediate mode not allowed for opcode %s.", Op);
      error (errbuf);
      break;

    default:                    /* extended addressing only */
      if (Debug)
        printf ("eval14\n");
      eval ();
      if (!strcmp (Op, "jmp")) {
        emit (opcode + 1);
      }
      else {
        emit (opcode + 0x10);
      }
      eword (Result);
      break;
    }
    break;

  case P2EXT:
    emit (PAGE2);
    if (strcmp ("trap", Op) == 0) {
    }
    else {
      emit (opcode);
    }
    switch (amode) {
    case INDX:
      sprintf (errbuf, "Indexed mode not allowed for opcode %s.", Op);
      error (errbuf);
      break;

    case LIMMED:
    case IMMED:
      sprintf (errbuf, "Immediate mode not allowed for opcode %s.", Op);
      error (errbuf);
      break;

    default:                    /* extended addressing only */
      if (Debug)
        printf ("eval15\n");
      eval ();
      if (strcmp ("trap", Op) == 0) {
        if (((Result < 0x3a) && (Result >= 0x30))
            || ((Result < 0x100) && (Result >= 0x40))) {
          opcode = Result;
          emit (opcode);
        }
        else {
          error ("Bad range for trap opcode; must be $30-$39 or $40-$ff.");
        }
      }
      else {
        eword (Result);
      }
      break;
    }
    break;

  case BTB:                     /* bset, bclr */
  case SETCLR:                  /* brset, brclr */
  if (Pass == 2 && amode == IMMED) // TAA ADDED
		  error("Operands in wrong order (mask is second)"); // TAA ADDED
	  
    amode = OTHER;
    if (comma_ctr == 1)
      amode = INDX;
    emit (bitop (opcode, amode, clss));
    if (amode == INDX) {
      if ((do_indexed (0) == 1) && (strcmp (Op, "call") == 0))
        return;
    }
    else {
      if ((((Result > 255) || (Force_word != 0)) && (Force_byte == 0))
          || (strcmp (Op, "call") == 0)) {
        eword (Result);
      }
      else {
        emit (lobyte (Result));
      }
    }
    Optr = skip_white (Optr);
    if (Debug)
      printf ("eval16\n");
    eval ();
    emit (lobyte (Result));     /* mask */
    if (clss == BTB) {          /* if doing brset/brclr... */
      Optr = skip_white (Optr); /* get last argument */
    }
    if (clss == SETCLR) {
      if ((alphan (*Optr)) || (*Optr == '*')) {
        sprintf (errbuf, "Too many arguments for %s opcode.", Op);
        error (errbuf);
        dist = Old_pc - (Pc + 1);
        break;
      }
      return;
    }
    if (Debug)
      printf ("eval17\n");
    eval ();
    dist = Result - (Pc + 1);

    if ((dist > 127 && dist < 0xff80) && Pass == 2) {
      error ("Branch target out of range or missing.");
      dist = Old_pc - (Pc + 1);
      break;
    }
    else {
      emit (lobyte (dist));
    }
    return;

  default:
    fatal ("Internal error, mnemonic class not found.");
  }
}


/*
 *  bitop --- adjust opcode on bit manipulation instructions
 */

int
bitop (int op, int mode, int clss)
{
  if (mode == INDX)
    return (op);
  else {
    if (Debug)
      printf ("eval18\n");
    eval ();
    if (strcmp ("call", Op) == 0) {
      return (op - 0x1);
    }
    if ((clss == SETCLR) && (Result <= 255)) {
      if (Force_word) {
        return (op + 0x10);
      }
      else {
        return (op + 0x40);
      }
    }
    if ((clss == SETCLR) && (Result > 255)) {
      if (Force_byte) {
        return (op + 0x40);
      }
      else {
        return (op + 0x10);
      }
    }
    if ((clss == BTB)
        && (((Result <= 255) && (Force_word == 0)) || (Force_byte != 0)))
      return (op + 0x40);
    if ((clss == BTB) && ((Result > 255) || (Force_word != 0)))
      return (op + 0x10);
  }
  sprintf (errbuf, "Internal bitop error, opcode = %s, operand = %s", Op,
           Operand);
  fatal (errbuf);
  return (0);
}


/*
 * do_gen --- process general addressing modes
 */

int
do_gen (int op, int mode)
{
  switch (mode) {
  case LIMMED:
    Optr++;
    emit (op);
    if (Debug)
      printf ("eval19\n");
    eval ();
    eword (Result);
    break;

  case IMMED:
    Optr++;
    emit (op);
    if (Debug)
      printf ("eval20\n");
    eval ();
    if(( Result < 0 || Result > 255) && Pass == 2)
      warn( "Value to large and has been masked to 0xFF" );	
    emit (lobyte (Result));
    break;

  case INDX:
    if (!strcmp (Op, "jsr")) {
      emit (op - 0x2);
    }
    else {
      emit (op + 0x20);
    }
    do_indexed (0);
    break;

  case OTHER:
    if (Debug)
      printf ("eval21\n");
    eval ();
    if (Force_word) {
      if (!strcmp (Op, "jsr")) {
        emit (op - 1);
      }
      else {
        emit (op + 0x30);
      }
      eword (Result);
      Cycles += 2;
      break;
    }
    if (Force_byte) {
      if (!strcmp (Op, "jsr")) {
        emit (op);
      }
      else {
        emit (op + 0x10);
      }
      if(( Result < 0 || Result > 255) && Pass == 2)
        warn( "Value to large and has been masked to 0xFF" );
      emit (lobyte (Result));
      Cycles++;
      break;
    }
    if (Result >= 0 && Result <= 0xFF) {
      if (!strcmp (Op, "jsr")) {
        emit (op);
      }
      else {
        emit (op + 0x10);
      }
      emit (lobyte (Result));
      Cycles++;
      break;
    }
    else {
      if (!strcmp (Op, "jsr")) {
        emit (op - 1);
      }
      else {
        emit (op + 0x30);
      }
      eword (Result);
      Cycles += 2;
      break;
    }

  default:
    sprintf (errbuf, "Unknown addressing mode for opcode %s, operand %s", Op,
             Operand);
    error (errbuf);
  }
  return 0;                     // to satisfy BC compiler
}

/*
 * do_indexed --- handle all weird stuff for indexed addressing
 */

int
do_indexed (int lim_5bit)
{
  char *peek;
  int post = 0;                /* will set if post increment or decrement */
  int pre = 0;                 /* will set if pre increment or decrement */
  int indirect = 0;            /* will set if indirect mode */
  int increment_value = 0;     /* contains the sign also */
  int base_reg = 0;
  int accum_offset = 0;
  int output_byte = 0;

/*
 * scan for post/pre increment/decrment and  [] operators in operand field.
 */

  peek = Optr;
  if (*peek == '[') {
    Optr++;                     /* don't worry about the brackets any more  */
    while ((!delim (*peek)) && (*peek != NULL)) {
      if (*(peek++) == ']') {
        indirect = 1;
        *(--peek) = ' ';        /* forced deletion of right bracket */
        peek++;
      }
    }
    if (indirect == 0) {
      error ("Missing right bracket in indirect addressing mode.");
      return (indirect);
    }
  }
  peek = Optr;
  while ((*peek != ',') && (*peek != NULL))
    peek++;
  peek++;
  if ((*peek == '+') || (*peek == '-')) { /*Checking for pre proccesses*/
    pre = 1;
    if (*peek == '-') {
      pre = -1;
    }
    peek++;
  }
  while (alpha (*peek))
    peek++;                     /* skip past register specification */
  if ((*peek == '+') || (*peek == '-')) {
    post = 1;
    if (*peek == '-') {
      post = -1;
    }
    peek++;
  }
  if ((pre != 0) && (post != 0)) {      /* if both pre & post... */
    error ("Cannot specify both pre- and post- operations.");
    return (indirect);
  }

  peek = Optr;
  if (*(peek + 1) == ',') {     /* assume accumulator offset mode */
    switch (mapdn (*peek)) {
    case 'a':
      if (indirect == 1) {
        error ("This indirect mode requires D-accumulator offset.");
        return (indirect);
      }
      output_byte = 0xe4;
      accum_offset = 1;
      Optr++;
      break;

    case 'b':
      if (indirect == 1) {
        error ("This indirect mode requires D-accumulator offset.");
        return (indirect);
      }
      output_byte = 0xe5;
      accum_offset = 1;
      Optr++;
      break;

    case 'd':
      output_byte = 0xe6;
      if (indirect == 1) {
        output_byte = 0xe7;
        Force_word = 0;
      }
      accum_offset = 1;
      Optr++;
      break;

    default:                    /* assume that we have a single letter label */
      if (Debug)
        printf ("eval22\n");
      eval ();
      break;
    }
  }
  else {
    if (Debug)
      printf ("eval23\n");
    eval ();
  }
  if (*Optr++ != ',') {
    error ("Comma expected but not found.");
    return (indirect);
  }
  if (pre != 0) {
    Optr++;
  }
  if (is_index_reg (Optr) == 0) {
    error ("Invalid index register specified.");
    return (indirect);
  }
  switch (mapdn (*Optr++)) {
  case 'y':
    base_reg = 1;
    break;

  case 'x':
    base_reg = 0;
    break;

  case 's':
    base_reg = 2;
    break;

  case 'p':
    if (pre) {
      error ("Preincrement/predecrement are not valid with pc register.");
      return (indirect);
    }
    if (post) {
      error ("Postincrement/postdecrement are not valid with pc register.");
      return (indirect);
    }
    base_reg = 3;
    break;

  default:
    error ("Index register required but not specified or unknown.");
    return (indirect);
  }

/*
 *  At this point, we know the modes and have error checked every mode.
 *  Now we need to start building the bytes to be emitted into the
 *  S-record file.
 */

  if (accum_offset) {
    output_byte = output_byte | (base_reg * 0x8);
    emit (output_byte);
  }
  if (pre || post) {
    increment_value = Result;
    if ((increment_value > 8) || (increment_value < 1)) {
      error ("Indexed addressing increment must be in range 1 to 8.");
      return (indirect);
    }
    if (indirect == 1) {
      error
        ("Indirect addressing only valid with D-register or 16 bit offset.");
      return (indirect);
    }
    if ((pre < 0) || (post < 0)) {
      increment_value = -increment_value;
    }
    if (pre > 0)
      increment_value = increment_value - 1;
    if (post > 0)
      increment_value = increment_value - 1;
    if (pre != 0) {
      output_byte = 0x20;
    }
    else {
      output_byte = 0x30;
    }

    output_byte = output_byte | (increment_value & 0xf);
    output_byte = output_byte | (base_reg * 0x40);
    emit (output_byte);
  }

  if (!(pre || post || accum_offset)) { /* assume constant offset mode here */

/*
 * decoding for 5 bit signed offset
 */
    if (indirect == 1) {
      Force_word = 1;
    }

    if ((Force_byte == 0) && (Force_word == 0)) {
      if ((Result >= -16) && (Result < 0)) {
        output_byte = 0x10 | (base_reg * 0x40);
        output_byte = output_byte | (Result + 16);
        emit (output_byte);
        while (alphan (*Optr))
          Optr++;               /* advances past the indexing stuff */
        return (indirect);
      }
      if ((Result < 16) && (Result >= 0)) {
        output_byte = 0x0 | (base_reg * 0x40);
        output_byte = output_byte | (Result);
        emit (output_byte);
        while (alphan (*Optr))
          Optr++;               /* advances past the indexing stuff */
        return (indirect);
      }
    }
    if (((Result < -16) || (Result >= 16)) && (lim_5bit == 1)) {
      error ("Constant offset out of range.");
      return (indirect);
    }
    /*
     * decoding for 9 and 16 bit signed offsets
     */
    if (Force_word == 0) {
      if ((Result < 0) && (Result >= -256)) {
        output_byte = 0xe1 | (base_reg * 0x8);
        emit (output_byte);
        emit (Result);
        while (alphan (*Optr))
          Optr++;               /* advances past the indexing stuff */
        return (indirect);
      }
      if ((Result >= 0) && (Result <= 255)) {
        output_byte = 0xe0 | (base_reg * 0x8);
        emit (output_byte);
        emit (Result);
        while (alphan (*Optr))
          Optr++;               /* advances past the indexing stuff */
        return (indirect);
      }
      if (((Result < -256) || (Result >= 256)) && (Force_byte != 0)) {
        error ("Offset larger than 9 bits.");
        return (indirect);
      }
    }
    output_byte = 0xe2 | (base_reg * 0x8);
    if (indirect == 1) {
      output_byte = 0xe3 | output_byte;
    }
    emit (output_byte);
    eword (Result);
  }
  while (alphan (*Optr))
    Optr++;                     /* advances past the indexing stuff */
  if (post != 0)
    Optr++;
  return (indirect);
}


/*
 * decode the post byte for tranfer and exchange instructions.
 */

int
do_xfer_ex (int post_byte)
{
  post_byte = post_byte | ((parse_reg (0) * 0x10) & 0x70);
  Optr = skip_white (Optr);
  post_byte = post_byte | (parse_reg (1) & 0x7);
  return (post_byte);
}


int
parse_reg (int which_temp)
{
  int base_reg;

  switch (mapdn (*Optr++)) {
  case 't':
    if (mapdn (*Optr) == 'e')
      Optr++;
    if (mapdn (*Optr) == 'm') {
      Optr++;
      if (mapdn (*Optr) == 'p')
        Optr++;
    }
    if (which_temp == 0) {
      if (*Optr++ != '3') {
        error ("Must specify valid temp register t3.");
        return (0);
      }
    }
    if (which_temp == 1) {
      if (*Optr++ != '2') {
        error ("Must specify valid temp register t2.");
        return (0);
      }
    }
    base_reg = 3;
    break;

  case 'y':
    base_reg = 6;
    break;

  case 'x':
    base_reg = 5;
    break;

  case 'd':
    base_reg = 4;
    break;

  case 'a':
    base_reg = 0;
    break;

  case 'b':
    base_reg = 1;
    break;

  case 'c':
    if (*Optr != ' ') {
      if (mapdn (*Optr++) != 'c') {
        warn ("Assuming ccr for the register name in this instruction.");
        return (0);
      }
      if (mapdn (*Optr++) != 'r') {
        warn ("Assuming ccr for the register name in this instruction.");
        return (0);
      }
    }
    base_reg = 2;
    break;

  case 's':
    if (*Optr != ' ') {
      if (mapdn (*Optr++) != 'p') {
        warn ("Assuming -sp- for the register name in this instruction");
        return (0);
      }
    }
    base_reg = 7;
    break;
  }
  if ((alphan (*Optr))) {
    error ("Standard register required; use a, b, ccr, x, y, sp, or d.");
    return (0);
  }
  if ((*Optr == '+') || (*Optr == '-'))
    Optr++;                     /* skip inc/dec symbol */
  return (base_reg);
}


/*
 * Hand this routine a pointer pointing to the first comma in a string that you
 * want to determine if is a x,y,sp,pc register and it will return a 1 if it
 * is and a zero if it is not.
 */

int
is_index_reg (char *peek1)
{
  int i;

  i = 0;
  if ((*peek1 == '+') || (*peek1 == '-')) {
    i = 1;
  }
  switch (mapdn (*(peek1 + i))) {
  case 'y':
    if (!alphan (*(peek1 + 1 + i))) {
      return (1);
    }
    break;

  case 'x':
    if (!alphan (*(peek1 + 1 + i))) {
      return (1);
    }
    break;

  case 's':
    if (((!alphan (mapdn (*(peek1 + 2 + i))))
         && (mapdn (*(peek1 + 1 + i)) == 'p'))
        || (!alphan (mapdn (*(peek1 + 2 + i))))) {
      return (1);
    }
    break;

  case 'p':
    if (((!alphan (mapdn (*(peek1 + 2 + i))))
         && (mapdn (*(peek1 + 1 + i)) == 'c'))
        || (!alphan (mapdn (*(peek1 + 2 + i))))) {
      return (1);
    }
    break;
  }
  return (0);
}

const int NMNE = (sizeof(table)/ sizeof(struct oper));

