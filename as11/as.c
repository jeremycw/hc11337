/***************************************************************************
 *   The Motorola Freeware Assembler                              
 *                                                     
 *   as.c ---  MCU cross assembler main program
 *
 ***************************************************************************/

#ifdef HAVE_CONFIG_H
#include <config.h>
#endif

#include <stdio.h>
#include <string.h>
#include <time.h>
#include <stdlib.h>

#define  OWNER
#include "vars.h"
#include "funcs.h"

#if defined(applec) || defined(__SC__)
#include <CursorCtl.h>
#endif

/*
 *  Variables local to this module
 */

struct tm *timeptr;
time_t timer;

int 
main (int argc, char *argv[])
{
  int i;
  int j;
  char **np;
  static char buffer2[MAXBUF];
  static char string[MAXBUF];
  char *ptr;

  if (argc < 2 || (strncmp(argv[1],"-", 1) == 0) ) {
    printf ("%s, an absolute assembler for Motorola MCU's, version %s\n", PROG_NAME, PROG_VERSION, PROG_BUILD);
    printf ("\nUsage: %s filename [options]", argv[0]);
    printf ("\nOptions:");
    printf ("\n   -o<filename>   Define object file (default extension is .s19)");
    printf ("\n   -d<symbol>     Define the symbol 'name' with a value of 1");
    printf ("\n   -l<dir>        Define a library directory with path 'lib'");
    printf ("\n   -L<filename>   Define listing file (default extension is .lst)");
    printf ("\n   -D             Turn on debugging printout");
    printf ("\n   -s<filename>   Create a symbol table file (use dflt: filename.sym)");
    printf ("\n   -p<part #>     Define MCU part number, such as 68hc12a4 (see #ifp)");
    printf ("\nListing Options:");
    printf ("\n   --list         Display list file to console");
    printf ("\n   --cycles       Display the cycle count");
    printf ("\n   --line-numbers Display line numbers in list file");
    printf ("\nOther Options:");
    printf ("\n   --no-warns     Supress warnings being displayed to console and list file");
    printf ("\n");
    exit (EXIT_SUCCESS);
  }

  /*Default Options*/
  Lflag = 0;  /* list file */
  Oflag = 0;  /* object file */
  Cflag = 0;  /* cycle flag, if 1 display cycle count in list*/
  sflag = 0;  /* symbol flag */
  WarnStopped = 0;
  LineFlag = 0;
  Debug = 0;
    
  strcpy (Lst_name, "");
  strcpy (Obj_name, change_file_ext(argv[1], ".s19")); /* default obj output */

  /* **Argv = 0; */
  Argv = argv;                  /* make names globally accessible */
  for (i = 1; i < argc;) {      /* find -d define options and then define them */
    switch (*argv[i]) {
    case '-':
      switch (*(argv[i] + 1)) {
      case 'd':
        strcpy (string, argv[i]);       /* capture -d option, cause it is going to be deleted and forgotten */
        install (&string[2], 1);        /* install new define with a value of 1, strip off -d */
        break;

      case 'l':
        strcpy (string, argv[i]);       /* capture -l option, cause it is going to be deleted and forgotten */
        strcpy (Master_Libs, &string[2]);  /* We now have at least three paths to search */
        break;

      case 'p':
        strcpy (string, argv[i]);       /* capture -p option, cause it is going to be deleted and forgotten */
        strcpy (Part_Number, &string[2]);  /* Now we have a part number */
        break;

      case 'D':
        Debug = 1;
        break;

      case 'o':
        strcpy (string, argv[i]);       /* capture -o option, it contains object file name */
        strcpy (Obj_name, &string[2]);  /* now Obj_name holds output file name */
        if (!strchr (Obj_name, '.')) {  /* if no . already in string... */
          strcat (Obj_name, ".s19");    /* append proper extension */
        }
        break;

      case 'L':
        Oflag = 1;
        if (argv[i][2] == '\0') {       /* if no name given */
          strcpy (Lst_name, change_file_ext(argv[1], ".lst")); /* create name from input filename */
        }
        else {
          strcpy (string, argv[i]);      /* capture -L option file name */
          strcpy (Lst_name, &string[2]); /* remove "-L" from front */
          if (!strchr (Lst_name, '.')) { /* if no . already in string... */
            strcat (Lst_name, ".lst");   /* append proper extension */
          }
        }
        break;

      case 's':
        sflag = 1;
        if (argv[i][2] == '\0') {       /* if no name given */
          strcpy (Sym_name, change_file_ext(argv[1], ".sym")); /* create name from input filename */
        }
        else {
          strcpy (string, argv[i]);      /* capture -s option file name */
          strcpy (Sym_name, &string[2]); /* remove "-s" from front */
          if (!strchr (Sym_name, '.')) { /* if no . already in string... */
            strcat (Sym_name, ".sym");   /* append proper extension */
          }
        }
        break;

      /*Listfile options*/
      case '-':
      	if (!(strcmp(argv[i]+2,"list")))
	  Lflag = 1;
	else if (!(strcmp(argv[i]+2,"line-numbers")))
	  LineFlag = 1;
	else if (!(strcmp(argv[i]+2,"cycles")))
	  Cflag = 1;
	else if (!(strcmp(argv[i]+2,"no-warns")))
	  WarnStopped = 1;
	else {
	   printf ("\nIllegal or unknown option %s.", argv[i]);
           printf ("\nType '%s' with no arguments to get a short commandline description.\n", argv[0]);
           exit (EXIT_SUCCESS);
	}
	break;
      default:
        printf ("\nIllegal or unknown option %s.", argv[i]);
        printf ("\nType '%s' with no arguments to get a short commandline description.\n", argv[0]);
        exit (EXIT_SUCCESS);
        break;
      }
      for (j = i; j < argc - 1; j++) {  /* move all arguments up to fill where -d was */
        argv[j] = argv[j + 1];
      }
      argc--;                   /* indicate that there is one less command line argument */
      break;
    default:
      i++;
      break;
    }
  }

  N_files = argc - 1;
  initialize ();
#if defined(applec) || defined(__SC__)
  InitCursorCtl (NULL);
#endif
  sprintf (S0_buffer, "Binary Creator: %s %s\n", *argv, Version);
  sprintf (S0_buffer, "%sCommand Line: %s\n", S0_buffer, command_line);
	/* TAA -- added \n to start of string in next line 01/06 */
  sprintf (buffer, "\n%s, an absolute assembler for Motorola MCU's, version %s\n\n", PROG_NAME, PROG_VERSION, PROG_BUILD);
  Bprintf (buffer);


  // We have 2 passes
  for (Pass = 1; Pass <= 2; Pass++) {
    re_init ();
    Cfn = 0;
    np = argv;

    while (++Cfn <= N_files) {
      This_Files_Path[0] = '\0';
      strcpy (This_File, *++np);

      for (j = strlen (This_File); j >= 0; j--) {
        if (This_File[j] == '\\' || This_File[j] == '/') {      /* TAA -- allow forward slashes */
          strcpy (&This_Files_Path[0], &This_File[0]);
          strcpy (&This_File[0], &This_Files_Path[j+1]);          
	  This_Files_Path[j+1] = '\0'; /*MWK keep last slash in path*/
          j = -1;
        }
      }
      if (strchr (This_File, '.') == NULL) {    /* TAA -- add default extension */
        /* Add extension */
        strcat (This_File, ".asm");
      }
      if (This_Files_Path[0] == '\0')   /* Build file name to open file */
        strcpy (buffer2, This_File);
      else
        sprintf (buffer2, "%s%s", This_Files_Path, This_File); /*MWK don't assume MSDOS paths */

      if ((Fd = fopen (buffer2, "r")) == NULL) {
        printf ("Can't open file %s in pass %d\n", buffer2, Pass);
        fatal ("Missing files");
      }
      else {
        strcpy (CurrentFilename, buffer2);      /* Save current filename */
        if (Pass == 2) {        /* header information stuff */
          s0_record ();
        }
        else {
          sprintf (S0_buffer, "%sFile: %s\n", S0_buffer, buffer2);
        }
        Line_num = 0;           /* reset line number */
        make_pass ();
        fclose (Fd);
      }
    }
    if (Err_count != 0) {
      Pass = 3;                 /* forces loop exit */
    }
  }
  fprintf (Objfil, "S9030000FC\n");     /* at least give a decent ending */
  fclose (Objfil);

  if (sflag)
    psymtab(Sym_name);
  
  time (&timer);
  timeptr = localtime (&timer);
  sprintf (buffer, "\nExecuted: %s", asctime (timeptr));
  Bprintf (buffer);
  if (!Err_count) {
	sprintf (buffer, "Total cycles: %d, Total bytes: %d\n",  Ccount, F_total);
	Bprintf (buffer);
  }
  sprintf (buffer, "Total errors: %d, Total warnings: %d\n", Err_count,Warn_count);
  Bprintf (buffer);

  for (i = 0; i < MAX_NESTING; i++)
    free (Filename_Array[i]);

  return Err_count;
}


/*void initialize (void) */
void
initialize (void)
{
  char buffer[256];
  int i;
  if (Debug != 0)
    printf ("Initializing...\n");
  for (i = 0; i < MAX_NESTING; i++) {
    F_Array[i] = NULL;
    Filename_Array[i] = malloc (MAXBUF);
  }
  
  ErrWarnCnt = 0;
  ErrorsStopped = 0;
  F_total = 0;
  E_total = 0;
  E_bytes[0] = 0;
  E_pc = 0;
  P_force = 0;
  P_total = 0;
  P_bytes[0] = 0;
  Cycles = 0;			/* cycle count for individual instruction */
  Ctotal = 0;			/* cycle count as depending on options given */
  Ccount = 0;			/* total cycles in assembled code */
  Total_Pseudo = 0;
  F_ref = 0;
  Ffn = 0;
  Cfn = 0;
  Err_count = 0;
  Warn_count = 0;
  Pc = 0;
  Pass = 1;
  InIf = 0;
  
  if (Oflag) {
     if ((Lstfil = fopen (Lst_name, "w"))==NULL) {
        Oflag = 0;
        sprintf(buffer, "Can't write to list file, '%s'", Lst_name);
     	fatal(buffer);
     }
  }
  
  Line[MAXBUF - 1] = '\n';   /* guard against garbage input */
  strcpy (Loc_Lab, "1");
  Total_Pseudo = pseudo_init ();

  if ((Objfil = fopen (Obj_name, "w")) == NULL) {
    sprintf(buffer,"Can't write to S-record file, '%s'",Obj_name);
    fatal (buffer);
  }

  fwdinit ();                   /* forward ref init */
}


void
re_init (void)
{
  if (Debug != 0)
    printf ("Reinitializing...\n");
  Tester_Label_Offset = 0;
  Pc = 0;
  E_total = 0;
  P_total = 0;
  //Lflag = 1; /*MWK why would we disable it here?*/
  //Cflag = 0; /*MWK why would we disable it here?*/
  LocLab = 0;
  /* Cycles are counted in both passes. Without clearing them they would be double what they really were */
  Ctotal = 0;
  Ccount = 0;
  
  InIf = 0;
  fwdreinit ();
  Total_Pseudo = pseudo_init ();
}

/* pass in a filename and the new desired extension. Include a period in the new extension. */
char *
change_file_ext(char *filename, char *ext)
{
  static char new_filename[MAXBUF];
  char *ptr;

  strcpy(new_filename, filename);
  ptr = strrchr (new_filename, '.');  /* point to last . if any */
  if (!ptr) {                     /* if no . already in string... */
    strcat (new_filename, ext);    /* append proper extension */
  }
  else {
    *ptr = '\0';                   /* found a period, so truncate the string at that point */
    strcat (new_filename, ext); /* add new extension */
  }
  return (char *)new_filename;
}

void
make_pass (void)
{
  if (Debug != 0) {
    sprintf (buffer, "Pass %d\n", Pass);
    printf (buffer);
  }

  // Get a line, parse it, and process it.
  while (getaline ()) {
    P_force = 0;                /* No force unless bytes emitted */
    if (parse_line ())
      process ();

    if (Pass == 2 && (Lflag || Oflag))
      print_line ();
    P_total = 0;                /* reset byte count */
    Cycles = 0;                 /* and per instruction cycle count */
    *Op = 0;
    *Operand = 0;
    *Label = 0;
    *Line = 0;
  }
  f_record ();
}

/*
*      getaline --- collect (possibly continued) an input line
*      All this function does is to scan the incoming line and
*      determine if it is a line that we need to parse to get the
*      opcode, label, and operand. That is all, nothing more.
*/

/*int getaline(void)*/
int
getaline (void)
{
  register char *p = Line;
  int remaining = MAXBUF - 2;   /* space left in Line */
  int len;                      /* line length */
  char *r;
 
  // Continue in this loop if the current asm file has more data OR if we're in an include file.
  // If we're in an include file and we reach EOF, we'll pop back to the previous file.
  while (((r = FGETS (p, remaining, Fd)) != (char *) NULL) || (is_inside_include () == 1)) 
  {
    // if r is NULL then we know we're at the end of an include file, or else we would
    // have exited from the loop.
    if (r != NULL) 
    {
      Line_num++;
      if ((len = strlen (p) - 2) <= 0)
        return (1);             /* just an empty line */

      p += len;
      if (*p != '\\')
        return (1);             /* not a continuation */

      remaining -= len + 2;
      if (remaining < 3)
        warn ("Continuation too long");
    }
    else
    {
      if (Pass == 2)
      {
        // EOF in include file, so we'll back up to a previous file
        Lprintf("                        #endinclude\n\n");
      }

      // due to poor code design, this call will open the first file, in addition to going back
      // toearlier files:
      pop_include ();
      remaining = MAXBUF - 2;
      p = Line;
    }
  }
  return (0);
}


int
is_inside_include (void)
{
  if (F_Array[0] == NULL) {
    return (0);
  }
  else {
    return (1);
  }
}


/* Close the current file and move back to the previous file */
void
pop_include (void)
{
  int i;

  fclose (Fd);                  /* close the included file */
  Fd = F_Array[0];              /* restore the last file descriptor */
  Line_num = LN_Array[0];       /* restore the line number we were on */
  strcpy (CurrentFilename, Filename_Array[0]);  /* restore filename */

  /* move everything down one */
  for (i = 0; i < MAX_NESTING - 1; i++) {
    F_Array[i] = F_Array[i + 1];
    LN_Array[i] = LN_Array[i + 1];
    strcpy(Filename_Array[i], Filename_Array[i + 1]);
  }
}


void
push_include (char *new_fname)
{
  int i;
  FILE *F_new;
  char name[MAXBUF];

  F_new = NULL;                 /* we haven't opened the new file yet */

  /* Try to open the include file in the main file's directory */
  if (F_new == NULL) {
    name[0] = 0;
    if (strlen (This_Files_Path) > 0) {
      strcpy (name, This_Files_Path);
//      strcat (name, "\\"); MWK Don't want to assume MSDOS path
    }
    strcat (name, new_fname);
    F_new = fopen (name, "r");
  }

  /* If it failed, try the current directory */
  if (F_new == NULL) {
    F_new = fopen (new_fname, "r");
  }

  /* If it failed, try the Master_Libs directory */
  if ((F_new == NULL) && (strlen (Master_Libs) > 0)) {
    strcpy (name, Master_Libs);
//    strcat (name, "\\"); MWK Don't want to assume MSDOS path
    strcat (name, new_fname);
    F_new = fopen (name, "r");
  }

  /* If it failed, we don't have anyplace else to look, so fail */
  if (F_new == NULL) {
    sprintf (errbuf, "Can't open #include file %s\n", new_fname);
    fatal (errbuf);
  }
  else {
    /* We opened the file successfully */
    /* Try to add our last file to the include stack, and remember
       which line we were on */

    if (F_Array[MAX_NESTING - 1] != NULL) {
      fatal ("Maximum #include nesting depth exceeded!");
    }

    /* move all the old entries up one */
    for (i = MAX_NESTING - 1; i > 0; i--) {
      F_Array[i] = F_Array[i - 1];
      LN_Array[i] = LN_Array[i - 1];
      strcpy(Filename_Array[i], Filename_Array[i - 1]);
    }

    /* Enter the new data in location 0 */
    LN_Array[0] = Line_num;     /* save line number in old file */
    Line_num = 0;               /* reset the line number for the new file */
    F_Array[0] = Fd;            /* save fd of old file */
    Fd = F_new;                 /* switch to the new file */

    /* Now save the filename of the old file */
    strcpy (Filename_Array[0], CurrentFilename);

    /* Save the name of the new file */
    strcpy (CurrentFilename, name);
  }
}

/*
*      parse_line --- split input line into label, op and operand
*      All this function does is to parse a line that has at least
*      a label or an OPCODE (determined by getaline) and place the
*      data in either 'Label', 'Op', and/or 'Operand'. 
*/

int
parse_line (void)
{
  char *ptrfrm;
  char *ptrto;
  struct nlist *ptr;
  char *ptr1;
  char *ptr2;
  char string[50];

  ptrfrm = Line;
  ptrto = Label;
  if ((*ptrfrm == '&') && ((InIf && IfTrue) || !InIf)) {
    if (Pass == 2) {
      ptrfrm++;
      while (*ptrfrm != '\n') {
        if ((*ptrfrm == '`') && (*(ptrfrm + 1) == '`')) {
          ptrfrm += 2;
        }
        else {
        }
      }
    }
    return (0);
  }
  if (*ptrfrm == '#') {
    if (strncmp ("ife", ptrfrm + 1, 3) == 0) {
      ptr1 = get_start_of_next_word (ptrfrm);
      ptr1 = word_to_string (Label, ptr1, " \t;\n,");

      if (*ptr1 != ',') {
        error ("Expected comma in #ifeq statement.");
        return (0);
      }

      ptr1++;
      Optr = ptr1;
      if (Debug)
        printf ("eval1\n");
      eval ();
      IfTrue = 0;
      InIf = Pass;
      Pass = 1;
      ptr = lookup (Label);
      if (ptr != NULL) {
        if (ptr->def == Result)
          IfTrue = 1;
      }
      Pass = InIf;
      InIf = 1;
    }
    if (strncmp ("ifd", ptrfrm + 1, 3) == 0) {
      ptr1 = get_start_of_next_word (ptrfrm);
      word_to_string (Label, ptr1, " \t;\n");
      IfTrue = 0;
      InIf = Pass;
      Pass = 1;
      if (lookup (Label) != NULL)
        IfTrue = 1;
      Pass = InIf;
      InIf = 1;
    }
    if (strncmp ("ifp", ptrfrm + 1, 3) == 0) {
      ptr1 = get_start_of_next_word (ptrfrm);
      word_to_string (string, ptr1, " \t;\n");
      IfTrue = 0;
      InIf = Pass;
      Pass = 1;
      if (strcmp (Part_Number, string) == 0)
        IfTrue = 1;
      Pass = InIf;
      InIf = 1;
    }
    // include a file
    if (strncmp ("inc", ptrfrm + 1, 3) == 0) {
      if ((InIf && IfTrue) || !InIf) {
        if (Debug) {
          sprintf (errbuf, "found an include stmt: %s\n", ptrfrm);
          printf (errbuf);
        }
        ptr1 = get_start_of_next_word (ptrfrm);
        parse_filename (string, ptr1);
        push_include (string);
      }
    }
    if (strncmp ("ifn", ptrfrm + 1, 3) == 0) {
      ptr1 = get_start_of_next_word (ptrfrm);
      word_to_string (Label, ptr1, " \t;\n");
      IfTrue = 0;
      InIf = Pass;
      Pass = 1;
      if (lookup (Label) == NULL)
        IfTrue = 1;
      Pass = InIf;
      InIf = 1;
    }
    if (strncmp ("els", ptrfrm + 1, 3) == 0) {
      if (InIf)
        IfTrue = !IfTrue;
      else
        error ("#else statement with no prior #ifdef or #ifeq.");
    }
    if (strncmp ("end", ptrfrm + 1, 3) == 0) {
      if (!InIf)
        error ("#end statement with no prior #ifdef or #ifeq.");
      InIf = 0;
    }
  }

  if (any(*ptrfrm, "*\n#;") || ((InIf == 1) && (!IfTrue)))
    return (0);                 /* a comment line */

  // Is it a label?  
  while (delim (*ptrfrm) == NO) {
    if (*ptrfrm == '`') {
      strcpy (ptrto, Loc_Lab);
      ptrto = ptrto + strlen (Loc_Lab);
      ptrfrm++;
    }
    else {
      *ptrto++ = mapdn(*ptrfrm++);
    }
  }

  *ptrto = NULL;
  ptrfrm = skip_white (ptrfrm);
  
  // Is it an opcode?
  ptrto = Op;
  while (delim (*ptrfrm) == NO) {
    if (*ptrfrm == '`') {
      strcpy (ptrto, Loc_Lab);
      ptrto = ptrto + strlen (Loc_Lab);
      ptrfrm++;
    }
    else {
      *ptrto++ = mapdn (*ptrfrm++);
    }
  }

  *ptrto = NULL;
  ptrfrm = skip_white (ptrfrm);

  // Is it an Operand?
  ptrto = Operand;
  while (*ptrfrm != NEWLINE) {
    if (*ptrfrm == '`') {
      strcpy (ptrto, Loc_Lab);
      ptrto = ptrto + strlen (Loc_Lab);
      ptrfrm++;
    }
    else {
      *ptrto++ = *ptrfrm++;
    }
  }
  *ptrto = NULL;

  if (Debug != 0) {
    // tell the user what we found here
    sprintf (buffer, "Label '%s'     ", Label);
    printf (buffer);
    sprintf (buffer, "Op '%s'        ", Op);
    printf (buffer);
    sprintf (buffer, "Operand  '%s'\n", Operand);
    Cprintf(buffer);
  }
  return (1);
}


/*
*   process --- determine mnemonic class and act on it
*/

void
process (void)
{
  register struct oper *i;

#if defined(applec) || defined(__SC__)
  SpinCursor (1);
#endif
  Old_pc = Pc;                  /* setup `old' program counter */
  Optr = Operand;               /* point to beginning of operand field */
  if (*Op == NULL) {            /* no mnemonic */
    if (*Label != NULL) {
      P_force++;
      install (Label, Pc);
    }
  }
  else if ((i = mne_look (Op)) == NULL) {
    sprintf (errbuf, "Unknown mnemonic: %s", Op);
    error (errbuf);
  }
  else if (i->clss == PSEUDO) {
    do_pseudo (i->opcode);
  }
  else {
    if (*Label)
      install (Label, Pc);      // add the label to our symbol table
    Cycles = i->cycles;		// track cycles
    do_op (i->opcode, i->clss);
    Ctotal += Cycles;
    Ccount += Cycles;
  }
}


/*
 *  Lprintf      send string to listing file if we are using one
 */

void 
Lprintf (char *str)
{
  if (Oflag)
    fprintf (Lstfil, str);
  if (Lflag) 
    printf (str);
  return;
}

void 
Bprintf (char *str)
{
  if (Oflag) {
    fprintf (Lstfil, str);
  }
  printf (str);
  return;
}

void
Cprintf (char *str)
{
    while( *str != '\0' ) {
	putchar( *str );
	str++;
    }
    return;
}
