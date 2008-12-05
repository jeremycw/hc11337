/***************************************************************************
 *   The Motorola Freeware Assembler                              
 *         
 *   util.c --- Utility functions supporting the assembler
 *
 ***************************************************************************/
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

#include  "vars.h"
#include  "funcs.h"

/*
 *  Variables and defines local to this module.
 */

char *hexstr = { "0123456789ABCDEF" };


/*
 *  fatal      print message and die immediately
 */

void
fatal (char *str)
{
  char buffer[80];

  sprintf (buffer, "Fatal error -- %s\n", str);
  Bprintf (buffer);
  exit (EXIT_FAILURE);
}

/*
 *  error      display error message
 *
 *  Print line number and error message, then return.  The error format
 *  was designed to support the Macintosh MPW development environment, but
 *  hopefully will support DOS tools as well.
 */

void
error (char *str)
{
  char buffer[255];

  ErrWarnCnt++;                 // counts both errors and warnings
  if (!ErrorsStopped) {
    if (ErrWarnCnt > 300) {
      Bprintf ("\nMaximum limit of 300 warnings/errors - reporting has stopped\n");
      ErrorsStopped = 1;
    }
    else {
      /* EWE: Chg Lprintf to BPrintf so we can see the errors in both listfile
         and stdout. Change format of error message to save space. */
      sprintf (buffer, "%s:%d: Error - %s\n", CurrentFilename, Line_num, str);
      Bprintf (buffer);
    }
  }

  Err_count++;
}


/*
 *      warn --- trivial error in a line
 *               print line number and error
 */

void
warn (char *str)
{
  char buffer[255];

  ErrWarnCnt++;                 // counts both errors and warnings
  if (!ErrorsStopped) {
    if (ErrWarnCnt > 300) {
      Bprintf ("\nMaximum limit of 300 warnings/errors - reporting has stopped\n");
      ErrorsStopped = 1;
    }
    else if (!WarnStopped) {
      /* EWE: Chg Lprintf to BPrintf so we can see the warnings in both listfile
         and stdout. Change format of error message to save space. */
      sprintf (buffer, "%s:%d: Warning - %s\n", CurrentFilename, Line_num, str);
      Bprintf (buffer);
    }
  }

  Warn_count++;
}


/*
 *      delim --- check if character is a delimiter
 */

int
delim (char c)
{
  if (any (c, " \t\n;:"))
    return (YES);
  return (NO);
}

/*
 *	isoperator -- check to see if character is an expression operator
 */
int
isoperator (char c) {
   if (any (*Optr, " \t\n,+-*/%&|^()"))
      return (YES);
   return (NO);
}
/*
 *      skip_white --- move pointer to next non-whitespace char
 */

char *
skip_white (char *ptr)
{
  while (*ptr == BLANK || *ptr == TAB || *ptr == ':')
    ptr++;
  return (ptr);
}


/* 
 *      eword --- emit a word to code file
 */

void
eword (int wd)
{
  emit (hibyte (wd));
  emit (lobyte (wd));
}


/*
 *      emit --- emit a byte to code file
 */

int
emit (int byte)
{
  char buffer[80];

  if (Debug != 0) {
    sprintf (buffer, "%2x @ %4x\n", byte, Pc);
    printf (buffer);
  }
  if (Pass == 1) {
    Pc++;
    return (YES);
  }
  if (P_total < P_LIMIT)
    P_bytes[P_total++] = byte;
  E_bytes[E_total++] = byte;
  Pc++;
  if (E_total == E_LIMIT)
    f_record();
  return (0);
}


/*
 *      s0_record --- flush record out in `S0' format
 */

int
s0_record (void)
{
  int i, num_bytes, j, count, e_total;
  int chksum;
  char out_string[100];


  if (Pass == 1)
    return (0);

  num_bytes = strlen (S0_buffer);
  e_total = 0;
  chksum = 0;
  for (j = 0; j < num_bytes; j++) {
    out_string[e_total] = toascii (S0_buffer[j]);
    e_total++;
    if ((e_total == 26) && (j > 0)) {   /* do after 16 bytes! */
      chksum = e_total + 3;     /* total bytes in this record */
      fprintf (Objfil, "S0");   /* record header preamble */
      hexout (e_total + 3);     /* byte count +3 */
      hexout (0);               /* high byte of PC */
      hexout (0);               /* low byte of PC */
      for (i = 0; i < e_total; i++) {
        chksum += lobyte (out_string[i]);
        hexout (lobyte (out_string[i]));        /* data byte */
      }
      chksum = ~chksum;         /* one's complement */
      hexout (lobyte (chksum)); /* checksum */
      fprintf (Objfil, "\n");
      e_total = 0;
      chksum = 0;
    }
  }
  chksum = e_total + 3;         /* total bytes in this record */
  fprintf (Objfil, "S0");       /* record header preamble */
  hexout (e_total + 3);         /* byte count +3 */
  hexout (0);                   /* high byte of PC */
  hexout (0);                   /* low byte of PC */
  for (i = 0; i < e_total; i++) {
    chksum += lobyte (out_string[i]);
    hexout (lobyte (out_string[i]));    /* data byte */
  }
  chksum = ~chksum;             /* one's complement */
  hexout (lobyte (chksum));     /* checksum */
  fprintf (Objfil, "\n");
  return (0);
}


/*
 *      f_record --- flush record out in `S1' format
 */

int
f_record (void)
{
  int i;
  int chksum;

  if (Pass == 1)
    return (0);
  if (E_total == 0) {
    E_pc = Pc;
    return (0);
  }
  F_total += E_total;           /* total bytes in file ver (TER)2.01 19 Jun 89 */
  chksum = E_total + 3;         /* total bytes in this record */
  chksum += lobyte (E_pc);
  chksum += E_pc >> 8;
  fprintf (Objfil, "S1");       /* record header preamble */
  hexout (E_total + 3);         /* byte count +3 */
  hexout (E_pc >> 8);           /* high byte of PC */
  hexout (lobyte (E_pc));       /* low byte of PC */
  for (i = 0; i < E_total; i++) {
    chksum += lobyte (E_bytes[i]);
    hexout (lobyte (E_bytes[i]));       /* data byte */
  }
  chksum = ~chksum;             /* one's complement */
  hexout (lobyte (chksum));     /* checksum */
  fprintf (Objfil, "\n");
  E_pc = Pc;
  E_total = 0;
  return (0);
}


void
hexout (int byte)
{
  byte = lobyte (byte);
  fprintf (Objfil, "%c%c", hexstr[byte >> 4], hexstr[byte & 017]);
}


/*
 *      print_line --- pretty print input line
 */

void
print_line (void)
{
  int i;
  int num_ch, for_verilog;
  register char *ptr;
  register char *ptr2;
  char buffer[256];
  
  if (LineFlag) {
    sprintf (buffer, "%04d ", Line_num);
    Lprintf (buffer);
  }
  if (P_total || P_force) {     /* This section prints out the PC */
    sprintf (buffer, "%04x", Old_pc & 0xffff);  /* TAA -- don't show more than 16 bits worth */
    Lprintf (buffer);
  }
  else {
    Lprintf ("    ");
  }
  for (i = 0; i < P_total && i < 6; i++) {      /* this section prints out JUST the bytes */
    sprintf (buffer, " %02x", lobyte (P_bytes[i]));
    Lprintf (buffer);
  }
  for (; i < 6; i++) {          /* this section is to keep the spacing equal */
    Lprintf ("   ");
  }
  if (Cflag) {
     if(Cycles) {                  /* display the cycle count */
      sprintf (buffer, " [%2d][%3d] ", Cycles, Ctotal);
      Lprintf (buffer);
      }
     else
      Lprintf ("           ");
  }
  if (LineFlag && Cflag)
    Lprintf("   ");
  else if (LineFlag)
    Lprintf("     ");
  else if (Cflag)
    Lprintf("       ");
  else
    Lprintf("  ");
    

/*
 * Now that addresses and such have been taken care of, 
 * print out the line directly as it was sent from the
 * asm input file.
 */

  ptr = Line;
  ptr2 = buffer;
  if ((*ptr == '&') || (*ptr == ';'))
    for_verilog = 1;

  if (*ptr == '\n')	/*It's a blank line*/
     *ptr2 = *ptr;
  while (*ptr != '\n')
  {        /* just echo the line back out */

/* 
 * Handle the tick marks - do not expand inside of comment lines.
 * The tick marks are for the pseudo local variables so that you don't
 * have to worry so much about making unique variables names (see LOC in
 * pseudo.c).
 */

    if (*ptr == '`') {
      *ptr2++ = *ptr;
      if (for_verilog && (*(ptr + 1) == '`')) {
        *ptr2++ = Loc_Lab[0];
        *ptr2++ = Loc_Lab[1];
        *ptr2++ = Loc_Lab[2];
        ptr += 2;
      }
      else if (!for_verilog) {
        *ptr2++ = Loc_Lab[0];
        *ptr2++ = Loc_Lab[1];
        *ptr2++ = Loc_Lab[2];
        ptr++;
      }
      else {
        ptr++;
      }
    }
    else {
      *ptr2++ = *ptr++;
    }
  }
  *ptr2 = '\0';
  
  ptr2 = buffer;		/*Uses putc to output % characters that might be in .asm*/
  while( *ptr2 != '\0' ) {
        if (Oflag)
  		putc( *ptr2, Lstfil );
        if (Lflag)
  		putchar( *ptr2 );
	ptr2++;
  } 

  for (; i < P_total; i++) {    /* this is for lines that produce more than 6 bytes of assembly code */
    if (i % 6 == 0)
     Lprintf ("\n    ");
    sprintf (buffer, " %02x", lobyte (P_bytes[i]));
    Lprintf (buffer);
  }
  Lprintf ("\n");
}


/*
 *      any --- does str contain c?
 */

int
any (char c, char *str)
{
  while (*str != NULL) {
    if (*str++ == c)
      return (YES);
  }
  return (NO);
}


/*
 *      mapdn --- convert A-Z to a-z
 */

char
mapdn (char c)
{
  if (c >= 'A' && c <= 'Z')
    return (c + 040);
  return (c);
}


/*
 *      lobyte --- return low byte of an int
 */

int
lobyte (int i)
{
  return (i & 0xFF);
}


/*
 *      hibyte --- return high byte of an int
 */

int
hibyte (int i)
{
  return ((i >> 8) & 0xFF);
}


/*
 *      head --- is str2 the head of str1?
 */

int
head (char *str1, char *str2)
{
  while (*str1 != NULL && *str2 != NULL) {
    if (*str1 != *str2)
      break;
    str1++;
    str2++;
  }
  if (*str1 == *str2)
    return (YES);
  if (*str2 == NULL)
    if (any (*str1, " \t\n,+-];*"))
      return (YES);
  return (NO);
}

/*
 *      alpha --- is character a legal letter
 */

int
alpha (char c)
{
  if (c <= 'z' && c >= 'a')
    return (YES);
  if (c <= 'Z' && c >= 'A')
    return (YES);
  if (c == '_')
    return (YES);
  if (c == '.')
    return (YES);
  return (NO);
}


/*
 *      alphan --- is character a legal letter or digit
 */

int
alphan (char c)
{
  if (alpha (c))
    return (YES);
  if (c <= '9' && c >= '0')
    return (YES);
  if (c == '$')
    return (YES);               /* allow imbedded $ */
  return (NO);
}


/*
 *  white --- is character whitespace?
 */

int
white (char c)
{
  if (c == TAB || c == BLANK || c == '\n')
    return (YES);
  return (NO);
}


/*
 *  alloc --- allocate memory
 */

void *
alloc (int nbytes)
{
  return (malloc (nbytes));
}


/*
 *  get_start_of_next_word
 */

char *
get_start_of_next_word (char *ptrfrm)
{
  char *ptr1;

  ptr1 = ptrfrm;
  ptr1++;
  while (alpha (*ptr1))
    ptr1++;
  ptr1 = skip_white (ptr1);
  return ptr1;
}

/* 
 * parse_filename
 * skip_white has already been done.
 * "ptr1" points to the input string.
 * on exit, "string" points to the resulting filename we parsed from the string.
 */
char *
parse_filename (char *string, char *ptr1)
{
  char *ptr2;
  int i;

  if (Debug) {
    sprintf (errbuf, "parse_filename: %s\n", ptr1);
    printf (errbuf);
  }

  if ((*ptr1 == '\'') || (*ptr1 == '\"')) {
    // we have a quoted filename to parse
    // start in position 1 - right after the quote
    ptr2 = string;
    ptr1++;
    while (*ptr1) {
      // look for the terminating quote
      if ((*ptr1 == '\'') || (*ptr1 == '\"'))
        break;
      // append a character to output filename
      *ptr2++ = *ptr1++;
    }
    *ptr2 = NULL;
  }
  else {
    // there are no quotes around filename, so parse it the old way
    word_to_string (string, ptr1, " \t;\n");
    return string;
  }
}

/*
 * word_to_string
 * skip_white has already been done.
 * "ptr1" points to the input string.
 * On exit, "string" points to the resulting word we parsed from the string,
 * and "ptr1" will be advanced to the point in the input string right after the
 * word we found.
 */

char *
word_to_string (char *string, char *ptr1, char *delimiter_list)
{
  char *ptr2;
  int i;

  ptr2 = string;
  while (!any (*ptr1, delimiter_list)) {
    if (*ptr1 == '#') {
      ptr1++;
      switch (*ptr1++) {
      case 'p':
        for (i = 0; (unsigned) i < strlen (Part_Number); i++) {
          *ptr2++ = Part_Number[i];
        }
        break;
      default:
        break;
      }
    }
    *ptr2++ = *ptr1++;
  }
  *ptr2 = 0;
  return (ptr1);
}

/*
 * FGETS
 * This is needed to compile DOS text files which uses \r\n for their newlines
 * unlike most of the rest of the PC operating systems that use just \n. If you
 * are running DOS then \r is stripped off transparently and this is redundant.
 */
char *FGETS( char *s, int n, register FILE *iop ){
	
	register int c;
	register char *cs;

	cs = s;
	while( --n > 0 && ( c = getc( iop )) != EOF ){ /* read chars if not file end */
		if( ( *cs = c ) != '\r' )
			cs++; /* incr buffer pointer if not CR */
  			      /* If CR, leave to be written over */
		if( c == '\n')
   			break;
 	}
 	*cs = '\0'; /* replace NEWLINE with NULL as in standard fgets() */
 	return(( c == EOF && cs == s ) ? NULL : s );  /* return NULL if this is EOF */
} /* end FGETS() */

/*
 *      suck_out_commas(): Sucks out commas from operands and converts 
 *      them to spaces
 */
int
suck_out_commas (void)
{
  char *peek;
  peek = Optr;
  
  while ((!delim (*peek)) && (*peek != NULL)) {
    if (*peek == ',') {
      if (!is_index_reg (peek + 1)) {
        *peek = ' ';
      }
      peek++;
    }
    else {
      peek++;
    }
  }
  return 0;                     // to satisfy BC compiler
}

