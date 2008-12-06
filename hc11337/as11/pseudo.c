/***************************************************************************
 *   The Motorola Freeware Assembler                              
 *         
 *   pseudo.c --- Pseudo op processing
 *
 ***************************************************************************/
#include <string.h>
#include <stdio.h>

#include  "vars.h"
#include  "funcs.h"

/*
 *  Variables and defines local to this module.
 */

char hexstr2[] = "0123456789";


/*
 *  pseudo_init (void)
 */

int
pseudo_init (void)
{
  strcpy (Operand, "0");
  do_pseudo (LOC);
  return (NUM_PSEUDOS);
}


/*
 *  do_pseudo --- do pseudo op processing
 */

int
do_pseudo (int op)
{
  char fccdelim;
  int fill;
  int tmp, tmp2;
  struct nlist *np, *index, *val;
  char ctemp[80], ctemp1[80];
  char *ptr;


  if (op != EQU && *Label) {
    install (Label, Pc);
  }
  P_force++;
  switch (op) {
  case RMB:                     /* reserve memory bytes */
    if (eval ()) {
      Pc += Result;
      if (strncmp ("ds.w", Op, 4) == 0)
        Pc += Result;           /* double it if talking words */
      if (strncmp ("rmw", Op, 4) == 0)
        Pc += Result;           /* double it if talking words */
      f_record ();              /* flush out bytes */
    }
    else
      error ("Undefined operand in RMB pseudo-op.");
    break;

  case ZMB:                     /* zero memory bytes */
    if (eval ())
      while (Result--)
        emit (0);
    else {
      error ("Undefined operand for ZMB pseudo-op.");
    }
    break;

  case FILL:                    /* fill memory with constant */
    eval ();
    fill = Result;
    if (*Optr++ != ',')
      error ("Comma missing in FILL pseudo-op.");
    else {
      Optr = skip_white (Optr);
      eval ();
      while (Result--)
        emit (fill);
    }
    break;

  case FCB:                     /* form constant byte(s) */
    do {
      Optr = skip_white (Optr);
      eval ();
      if (Result > 0xFF) {
        if (!Force_byte)
          warn ("Value too large and has been masked to 0xFF");
        Result = lobyte (Result);
      }
      emit (Result);
    } while (*Optr++ == ',');
    break;

  case FDB:                     /* form double byte(s) */
    do {
      Optr = skip_white (Optr);
      eval ();
      eword (Result);
    } while (*Optr++ == ',');
    break;

  case FCC:                     /* form constant characters */
    if (*Operand == NULL)
      break;
    fccdelim = *Optr++;
    while (*Optr != NULL && *Optr != fccdelim)
      emit (*Optr++);
    if (*Optr == fccdelim)
      Optr++;
    else
      error ("Missing delimiter in FCC pseudo-op.");
    break;

  case ORG:                     /* origin */
    if (eval ()) {
      Old_pc = Pc = Result;
      f_record ();              /* flush out any bytes */
    }
    else
      error ("Undefined operand for ORG pseudo-op.");
    break;

  case EQU:                     /* equate */
    if (*Label == NULL) {
      error ("Missing or bad label for EQU pseudo-op.");
      break;
    }
    if (eval ()) {
      if (Pass == 1) {
        install (Label, Result);
      }
      else {
        np = lookup (Label);
        np->def = Result;
      }
      Old_pc = Result;          /* override normal */
    }
    else
      error ("Undefined operand for EQU pseudo-op.");
    break;

  case REDEF:
    ptr = ctemp;
    tmp = 0;
    Optr = Operand;
    /*    while ((!delim(*Optr)) && (*Optr!=NULL))  {  */
    while ((!delim (*Optr)) && (*Optr != 0)) {
      if (*Optr == ',') {
        tmp++;
      }
      Optr++;
    }
    Optr = Operand;
    while (alphan (*Optr))
      *ptr++ = *Optr++;
    /*    *ptr = NULL;  */
    *ptr = 0;
    Optr++;
    np = lookup (ctemp);
    if (np == NULL) {
      error ("Cannot redefine an undefined label.");
    }
    else {
      if (tmp > 1) {
        Optr = Operand;
        while (alphan (*Optr))
          Optr++;
        Optr++;                 /* go past the comma */
        ptr = ctemp;
        while (alphan (*Optr))
          *ptr++ = *Optr++;     /* collect the next label into ctemp */

        *ptr = 0;
        if (*Optr == ',') {
          Optr++;               /* go past the next comma */
          ptr = ctemp1;
          while (alphan (*Optr))
            *ptr++ = *Optr++;   /* collect the next label into ctemp1 */
          *ptr = 0;
          index = lookup (ctemp1);      /* now I have the address of the index variable */
          if (index == NULL) {
            error ("Index variable in REDEF pseudo-op is undefined.");
            return 0;
          }
          if (*Optr == ',') {
            Optr++;             /* go past next comma */
            eval ();            /* gets the limit for the increment */
            index->def = index->def + 1;
            if (index->def > Result) {
              index->def = 0;
            }
            sprintf (ctemp1, "%s%03d", ctemp, index->def);
            val = lookup (ctemp1);
            if (val == NULL) {
              error ("Internal error in REDEF pseudo-op; val == NULL.");
              return 0;
            }
            np->def = val->def;
          }
          else {
            error ("Missing arguments in REDEF pseudo-op.");
            return 0;
          }
        }
        else {
          error ("Missing arguments in REDEF pseudo-op.");
          return 0;
        }
      }
      else {
        eval ();
        np->def = Result;
        Old_pc = Result;        /* override normal */
      }
    }
    break;

  case LOC:                     /* equate */
    Optr = Operand;
    if (alphan (*Optr)) {
      eval ();
      LocLab = Result;
    }
    P_force = 0;
    tmp = tmp2 = LocLab / 100;
    Loc_Lab[0] = hexstr2[tmp];
    tmp = (LocLab - (tmp2 * 100)) / 10;
    Loc_Lab[1] = hexstr2[tmp];
    tmp = LocLab - ((tmp2 * 100) + (tmp * 10));
    Loc_Lab[2] = hexstr2[tmp];
    Loc_Lab[3] = 0;
    strcpy (Operand, Loc_Lab);
    LocLab++;
    break;

  case OPT:                     /* assembler option */
    P_force = 0;
    do {     
    if (Pass == 2)
    {
        Optr = skip_white (Optr);
	if (head (Optr, "l")) {
		Lflag = 1;
		Optr++;
	} else if (head (Optr, "nol")) {
		Lflag = 0;
		Optr += 3;
	} else if (head (Optr, "c")) {
		Cflag = 1;
		Ctotal = 0;
		Optr++;
	} else if (head (Optr, "noc")) {
		Cflag = 0;
		Optr += 3;
	} else if (head (Optr, "contc")) {
		Cflag = 1;
		Optr += 5;
	} else {
		sprintf (errbuf,
		"Unknown option %s in OPT pseudo-op; must be L, NOL, C, NOC, CONTC.",
		Optr);
		error (errbuf);
	}
    }
    } while (*Optr++ == ',');
    break;

  case NULL_OP:         /* ignored psuedo ops */
    P_force = 0;
    break;

  default:
    fatal ("Pseudo-op error, could not find a pseudo-op to match.");
  }
  return 0;                     // added to keep the BC compiler happy
}
