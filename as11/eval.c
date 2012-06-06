/***************************************************************************
 *   The Motorola Freeware Assembler                              
 *         
 *   eval.c --- Expression Evaluator
 *
 *      an expression is constructed like this:
 *
 *      expr ::=  expr + term |
 *                expr - term ;
 *                expr * term ;
 *                expr / term ;
 *                expr | term ;
 *                expr & term ;
 *                expr % term ;
 *                expr ^ term ;
 *
 *      term ::=  symbol |
 *                * |
 *                constant ;
 *
 *      symbol ::=  string of alphanumerics with non-initial digit
 *
 *      constant ::= hex constant |
 *                   binary constant |
 *                   octal constant |
 *                   decimal constant |
 *                   ascii constant;
 *
 *      hex constant ::= '$' {hex digits};
 * 
 *      octal constant ::= '@' {octal digits};
 *
 *      binary constant ::= '%' { 1 | 0 };
 *
 *      decimal constant ::= {decimal digits};
 *
 *      ascii constant ::= ''' any printing char;
 *
 *  Notes: The eval routines are where the forward references are recorded.
 *         When an expression is evaluated and there is an unrecognized symbol 
 *         then it is assumed to be a forward reference. To mark a forward 
 *         reference, we record the line number and the file number in which
 *         the forward reference occurred. The limit, I believe, is one forward 
 *         reference per line.
 *
 *         eval() only works on the OPERAND field!
 ***************************************************************************/
#include <string.h>
#include <stdio.h>

#include  "vars.h"
#include  "funcs.h"

int
eval ()
{
  int left, right;              /* left and right terms for expression */
  char o;                       /* operator character */
  int depth;
  int larg[10];
  int rarg[10];
  char last_op[10];

  if (Debug) {
    sprintf (errbuf, "Evaluating '%s'\n", Optr);
    Cprintf (errbuf);
  }

  for (o = 0; o < 10; o++) {
    larg[o] = 0;
    rarg[o] = 0;
    last_op[o] = '_';
  }
  depth = 0;
  o = ' ';
  Force_byte = NO;
  Force_word = NO;

  if (*Optr == '<') {
    Force_byte++;
    Optr++;
  }
  else if (*Optr == '>') {
    Force_word++;
    Optr++;
  }
  if (*Optr != '(') {
    if (Debug) {
      sprintf (errbuf, "*Optr before get_term: $%02X\n", *Optr);
      printf (errbuf);
    }
    larg[depth] = get_term ();
  }
  
  /*MWK This will make it improper to put spaces anywhere in a given
    operand. It however makes it possible to more appropriately parse
    through operands, especially with special functions that
    required more then one. Instructions that require more then one
    operand have the operands seperated by commas or spaces.*/
  /*Optr = skip_white (Optr);*/

  // If we find something here that isn't an operator, then it is either
  // a comment, or a special case, or an error. We don't try to report an error
  // here because higher level routines need a chance to check for special
  // cases.
  
  while (is_op ()) {
    if (Debug) {
      sprintf (errbuf, "is_op: is an op: %c\n", *Optr);
      printf (errbuf);
    }
    if ((*Optr != '(') && (*(Optr + 1) == '('))
      o = *Optr++;
    if (*Optr == '(') {
      last_op[depth] = o;
      o = ' ';
      depth++;
      if (depth > 10) {
        error ("Parentheses nesting exceeded limit of 10.");
      }
      Optr++;
      while (*Optr == '(') {
        last_op[depth] = o;
        depth++;
        if (depth > 10) {
          error ("Parentheses nesting exceeded limit of 10.");
        }
        Optr++;
      }
      larg[depth] = get_term ();        /* pickup first part of expression */
      o = '_';
    }
    else if (*Optr == ')') {
      depth--;
      if (depth < 0) {
        depth = 0;
        error ("Too many right parentheses in expression.");
      }
      o = last_op[depth];
      rarg[depth] = larg[depth + 1];
      Optr++;
    }
    else {
      o = *Optr++;              	/* pickup connector and skip */
      rarg[depth] = get_term ();        /* pickup current rightmost side */
    }

    switch (o) {
    case '+':
      larg[depth] += rarg[depth];
      break;
    case '-':
      larg[depth] -= rarg[depth];
      break;
    case '*':
      larg[depth] *= rarg[depth];
      break;
    case '/':
      larg[depth] /= rarg[depth];
      break;
    case '|':
      larg[depth] |= rarg[depth];
      break;
    case '&':
      larg[depth] &= rarg[depth];
      break;
    case '%':
      larg[depth] %= rarg[depth];
      break;
    case '^':
      larg[depth] = larg[depth] ^ rarg[depth];
      break;
    case ' ':
      if (Debug)
        printf ("found space in eval\n");
      larg[depth] = rarg[depth];
      break;
    }
  }

  if (depth != 0) {
    error ("Unbalanced parentheses in expression.");
  }
  
  /* Result is the variable that returns the value calculated for the function that called it. */
  /* It is a global variable and passes information as such */
  Result = larg[depth];

  if (Debug) {
    // Tell the user what we did
    sprintf (errbuf, "eval Result=%x\n", Result);
    printf (errbuf);
    sprintf (errbuf, "Force_byte=%d  Force_word=%d\n", Force_byte,
             Force_word);
    printf (errbuf);
  }

  if (Debug) {
    sprintf (errbuf, "At end of eval, we found this: $%02X\n", *Optr);
    printf (errbuf);
  }

  // NOTE: We can't really report garbage here. There are some special
  // cases like this (where "not_ready" is a label):
  //    brclr   atd0stat,x $80 not_ready
  //if ((*Optr != '*') && (*Optr != ';') && (*Optr != ',') && (*Optr != NULL)) {
  //  sprintf (errbuf, "Found unexpected character at end of line: %c", *Optr);
  //  error (errbuf);
  //}	
  return (YES);
}

/*
 *    is_op --- is character an expression operator?
*/

int
is_op (void)
{
  char c;
/* MWK removed 2/24/05, see comments in eval() */
/*  Optr = skip_white (Optr); */
  c = *Optr;
  if (any (c, "+-*/&%|^)("))
    return (YES);
  return (NO);
}

/*
 *      get_term --- evaluate a single item in an expression
*/

int
get_term (void)
{
  char hold[MAXBUF];
  char *tmp;
  int val = 0;                  /* local value being built */
  int minus;                    /* unary minus flag */
  int compl = NO;               /* unary complement flag TAA Addition */

  Optr = skip_white (Optr);

  while (*Optr == '#')
    Optr++;                     /* skip leading immediate flags */
  if (*Optr == '-') {
    Optr++;
    minus = YES;
  }
  else {
    minus = NO;
    if (*Optr == '~') {
      Optr++;
      compl = YES;
    }
  }

  /* look at rest of expression */
  if (*Optr == '%') {           /* binary constant */
    Optr++;
    while (!isoperator(*Optr) && *Optr != NULL) {
      if (any (*Optr, "01"))
      	val = (val * 2) + ((*Optr++) - '0');
      else {
      	error("Invalid operand value.");
	break;
      }
    }
  }
  else if (*Optr == '@') {      /* octal constant */
    Optr++;
    while (!isoperator(*Optr) && *Optr != NULL) {
      if (any (*Optr, "01234567"))
        val = (val * 8) + ((*Optr++) - '0');
      else {
        error("Invalid operand value.");
	break;
      }
    }
  }
  else if (*Optr == '$') {      /* hex constant */
    Optr++;
    while (!isoperator(*Optr) && *Optr != NULL) {
      if (any (*Optr, "0123456789abcdefABCDEF")) {
	if (*Optr > '9')
		val = (val * 16) + 10 + (mapdn (*Optr++) - 'a');
	else
		val = (val * 16) + ((*Optr++) - '0');
      } else {
        error("Invalid operand value.");
	break;
      }
    }
  }
  else if (*Optr == '!') { /* decimal constant */
    Optr++;
    while (!isoperator(*Optr) && *Optr != NULL) {
      if (*Optr >= '0' && *Optr <= '9')
        val = (val * 10) + ((*Optr++) - '0');
      else {
        error("Invalid operand value.");
	break;
      }
    }  
  }

  else if (any (*Optr, "0123456789")) { /* decimal constant */
    while (!isoperator(*Optr) && *Optr != NULL) {
      if (*Optr >= '0' && *Optr <= '9')
        val = (val * 10) + ((*Optr++) - '0');
      else {
        error("Invalid operand value.");
	break;
      }
    }
  }
  else if (*Optr == '*') {      /* current location counter */
    Optr++;
    val = Old_pc;
  }
  else if (*Optr == '\'') {     /* character literal */
    Optr++;
    if (*Optr == NULL) {
      if (Pass == 2)
      	warn("Missing character, value of 0 assumed");
      val = 0;
    } else
      val = *Optr++;
  }
  else if (alpha (*Optr)) {     /* a symbol */
    // tmp is a pointer that we use to copy the symbol into "hold" buffer
    tmp = hold;                 /* collect symbol name */

    // Collect the letters of this symbol - this ends when it sees a space, or
    // any non-alphanumeric value (like + or CR)
    while (alphan (*Optr))
      *tmp++ = *Optr++;
    *tmp = NULL;                // null terminate

    if (Debug) {
      sprintf (errbuf, "looking up symbol: %s\n", hold);
      printf (errbuf);
    }

    // See if we have this symbol in our symbol table
    if (lookup (hold) != 0) {
      val = Last_sym;
    }
    else {
      if (Pass == 1) {          /* forward ref here */
        fwdmark ();
        if (!Force_byte)
          Force_word++;
        val = 0;
      }
    }
    if (Pass == 2 && Line_num == F_ref && Cfn == Ffn) {
      if (Debug)
        printf ("force word\n");
      if (!Force_byte)
        Force_word++;
      fwdnext ();
    }
  }
  else {
    /* none of the above */
    if (Pass == 2)
      warn ("Missing expression, value of 0 assumed");
    val = 0;
  }

  if (compl) {
    if (Debug) {
      sprintf (errbuf, "get_term A returns %d\n", ~val);
      printf (errbuf);
    }
    return (~val);
  }
  else if (minus) {
    if (Debug) {
      sprintf (errbuf, "get_term B returns %d\n", -val);
      printf (errbuf);
    }
    return (-val);
  }
  else {
    if (Debug) {
      sprintf (errbuf, "get_term C returns %d\n", val);
      printf (errbuf);
    }
    return (val);
  }
}
