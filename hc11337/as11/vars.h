/***************************************************************************
 *   The Motorola Freeware Assembler                              
 *
 *   vars.h --- include file for the CPU12 assembler
 *
 *   This file contains all global machine-independent definitions
 *   and variables.  Variables declared in this file belong to whichever
 *   file #defines OWNER; all other files will see these variables
 *   as externs.
 ***************************************************************************/
 
#define  PROG_TITLE  "The Freeware Motorola Assembler"
#define  PROG_VERSION  "1.2e"
#define  PROG_BUILD    "1"

extern char PROG_NAME[5];  /* actual name is in do11.c or do12.c */

#ifdef  OWNER
#define  EXTERN
#else
#define  EXTERN  extern
#endif

#define YES 1
#define NO  0
#define ERR (-1)
#define EOS '\0'

#undef   NULL
#ifndef  NULL
#define  NULL  '\0'
#endif

#define MAXBUF  256
#define MAXOP   10              /* longest mnemonic */
#define MAXLAB  32
#define MAX_NESTING 10

/*
 *  The E_LIMIT constant fixes the length of the outgoing S9
 *  records.  Due to restrictions in the 68hc912b32 boot loader,
 *  this size of the data field for this record cannot exceed
 *  32 bytes.  To be on the safe side, I've dropped this down.
 */

#define E_LIMIT 16              // max bytes to send per S9 record
#define P_LIMIT 64
#define MAX_VLAB_ENTRIES 65536
#define MAX_COMMENT 200


/*                  Character constants                       */

#define NEWLINE '\n'
#define TAB '\t'
#define BLANK   ' '


/*                  Opcode classes                           */

#define INH     0               /* Inherent         */
#define GEN     1               /* General Addressing       */
#define IMM     2               /* Immediate only       */
#define REL     3               /* Short Relative       */
#define P2REL   4               /* Long Relative        */
#define P1REL   5               /* Long Relative (LBRA and LBSR) */
#define NOIMM   6               /* General except for Immediate */
#define P2GEN   7               /* Page 2 General       */
#define P3GEN   8               /* Page 3 General       */
#define RTOR    9               /* Register To Register     */
#define INDEXED 10              /* Indexed only                 */
#define RLIST   11              /* Register List        */
#define P2NOIMM 12              /* Page 2 No Immediate      */
#define P2INH   13              /* Page 2 Inherent      */
#define P3INH   14              /* Page 3 Inherent      */
#define GRP2    15              /* Group 2 (Read/Modify/Write)  */
#define LONGIMM 16              /* Immediate mode takes 2 bytes */
#define BTB     17              /* Bit test and branch          */
#define SETCLR  18              /* Bit set or clear             */
#define CPD     19              /* compare d               6811 */
#define XLIMM   20              /* LONGIMM for X           6811 */
#define XNOIMM  21              /* NOIMM for X             6811 */
#define YLIMM   22              /* LONGIMM for Y           6811 */
#define YNOIMM  23              /* NOIMM for Y             6811 */
#define FAKE    24              /* convenience mnemonics   6804 */
#define APOST   25              /* A accum after opcode    6804 */
#define BPM     26              /* branch reg plus/minus   6804 */
#define CLRX    27              /* mvi x,0                 6804 */
#define CLRY    28              /* mvi y,0                 6804 */
#define LDX     29              /* mvi x,expr              6804 */
#define LDY     30              /* mvi y,expr              6804 */
#define MVI     31              /* mvi                     6804 */
#define EXT     32              /* extended                6804 */
#define BIT     33              /* bit manipulation        6301 */
#define SYS     34              /* syscalls (really swi)        */
#define PSEUDO  35              /* Pseudo ops                   */

/*
 *                  Pseudo-ops
 */
#define RMB     0               /* Reserve Memory Bytes         */
#define FCB     1               /* Form Constant Bytes          */
#define FDB     2               /* Form Double Bytes (words)    */
#define FCC     3               /* Form Constant Characters     */
#define ORG     4               /* Origin                       */
#define EQU     5               /* Equate                       */
#define ZMB     6               /* Zero memory bytes            */
#define FILL    7               /* block fill constant bytes    */
#define OPT     8               /* assembler option             */
#define NULL_OP 9               /* null pseudo op               */
#define LOC     10              /* for me                       */
#define REDEF   11              /* for me                       */

/*
 *                  NEW CLASSES ADDED FOR HC12 
 */
#define P2INON  36              /* page 2 indexed only stuff */
#define CCRC    37              /* TRANLATES CLEARS TO andcc's */
#define TLEA    38              /* tranlates abx, aby to leax leay */
#define P2EXT   39              /* for page two, extended only addressing */
#define TTFR    40              /* tranlates old tap (etc) into tfr r1 r2 */
#define MVIB    41              /* move bytes */
#define MVIW    42              /* move words */
#define P2G2    43              /* for 2nd page stuff */

#define PAGE1   0x00
#define PAGE2   0x18


EXTERN struct oper {            /* an entry in the mnemonic table */
  char *mnemonic;               /* its name */
  char clss;                    /* its class */
  int opcode;                   /* its base opcode */
  char cycles;                  /* its base # of cycles */
};

#define  NUM_PSEUDOS  27

EXTERN struct oper pseudo[NUM_PSEUDOS]
#ifdef  OWNER
  = {
  {"bsz", PSEUDO, ZMB, 0},
  {"db", PSEUDO, FCB, 0},
  {"dc.b", PSEUDO, FCB, 0},
  {"dc.w", PSEUDO, FDB, 0},
  {"ds", PSEUDO, RMB, 0},
  {"ds.b", PSEUDO, RMB, 0},
  {"ds.w", PSEUDO, RMB, 0},
  {"dw", PSEUDO, FDB, 0},
  {"end", PSEUDO, NULL_OP, 0},
  {"equ", PSEUDO, EQU, 0},
  {"fcb", PSEUDO, FCB, 0},
  {"fcc", PSEUDO, FCC, 0},
  {"fdb", PSEUDO, FDB, 0},
  {"fill", PSEUDO, FILL, 0},
  {"loc", PSEUDO, LOC, 0},
  {"nam", PSEUDO, NULL_OP, 0},
  {"name", PSEUDO, NULL_OP, 0},
  {"opt", PSEUDO, OPT, 0},
  {"org", PSEUDO, ORG, 0},
  {"pag", PSEUDO, NULL_OP, 0},
  {"page", PSEUDO, NULL_OP, 0},
  {"redef", PSEUDO, REDEF, 0},
  {"rmb", PSEUDO, RMB, 0},
  {"rmw", PSEUDO, RMB, 0},
  {"spc", PSEUDO, NULL_OP, 0},
  {"ttl", PSEUDO, NULL_OP, 0},
  {"zmb", PSEUDO, ZMB, 0}
}
#endif
 ;

EXTERN int ErrWarnCnt;          /* Number of errors/warnings generated so far */
EXTERN int ErrorsStopped;       /* tells if we have stopped giving errors/warnings */
EXTERN int WarnStopped;
EXTERN char errbuf[80];         /* global error msg buffer */
EXTERN int Line_num;            /* current line number      */
EXTERN int Err_count;           /* total number of errors   */
EXTERN int Warn_count;          /* total number of warnings  */
EXTERN char Line[MAXBUF];       /* input line buffer            */
EXTERN char Label[MAXLAB];      /* label on current line        */
EXTERN char Op[MAXOP];          /* opcode mnemonic on current line      */
EXTERN char Operand[MAXBUF];    /* remainder of line after op           */
EXTERN char *Optr;              /* pointer into current Operand field   */
EXTERN int Result;              /* result of expression evaluation      */
EXTERN int Force_word;          /* Result should be a word when set     */
EXTERN int Force_byte;          /* Result should be a byte when set     */
EXTERN int Pc;                  /* Program Counter              */
EXTERN int Old_pc;              /* Program Counter at beginning */
EXTERN int Last_sym;            /* result of last lookup    */
EXTERN int Pass;                /* Current pass #       */
EXTERN int N_files;             /* Number of files to assemble  */
EXTERN FILE *Fd;                /* Current input file structure */
EXTERN FILE *F_Array[MAX_NESTING];        /* used for nesting includes (saves file descriptor) */
EXTERN int LN_Array[MAX_NESTING];         /* used for nesting includes (saving line number)    */
EXTERN char *Filename_Array[MAX_NESTING]; /* used for nesting includes (saving filenames) */
EXTERN char CurrentFilename[MAXBUF];    /* hold the name of the file we are in */
EXTERN int Cfn;                 /* Current file number 1...n    */
EXTERN int Ffn;                 /* forward ref file #           */
EXTERN int F_ref;               /* next line with forward ref   */
EXTERN char **Argv;             /* pointer to file names    */
EXTERN int F_total;             /* total bytes emitted in S file */
EXTERN int E_total;             /* total # bytes for one line   */
EXTERN int E_bytes[E_LIMIT];    /* Emitted held bytes           */
EXTERN int E_pc;                /* Pc at beginning of collection */
EXTERN int Lflag;               /* output to console, listing flag to 0=nolist, 1=list */
EXTERN int Oflag;		/* output to file,  listing flag to 0=nolist, 1=list */
EXTERN int sflag;		/* create symbol table file */
EXTERN int LineFlag;		/* line flag to display line numbers in list file */
EXTERN int P_force;             /* force listing line to include Old_pc */
EXTERN int P_total;             /* current number of bytes collected    */
EXTERN int P_bytes[P_LIMIT];    /* Bytes collected for listing  */
EXTERN int Cflag;               /* cycle count flag */
EXTERN int Cycles;              /* # of cycles per instruction  */
EXTERN long Ctotal;             /* # of cycles seen so far */
EXTERN int Ccount;		/* # of total cycles in assembled code */
EXTERN char Loc_Lab[20];        /* for local labeling ability */
EXTERN int LocLab;
EXTERN int Tester_Label_Offset;
EXTERN int InIf, IfTrue;        /* was char */
EXTERN int Total_Pseudo;
EXTERN int Debug;
EXTERN char This_Files_Path[MAXBUF];
EXTERN char This_File[MAXBUF];  /* new */
EXTERN char Master_Libs[MAXBUF];
EXTERN char Part_Number[MAXBUF];
EXTERN char Version[5];
EXTERN char command_line[MAXBUF];
EXTERN char buffer[MAXBUF];
EXTERN char S0_buffer[1024];    /* used by the S0 records to record useful information */

extern const int NMNE;		/* MWK How many entries in instruction table */
extern struct oper table[];	/* MWK Added to support mutiple instruction table files*/

EXTERN struct nlist {           /* basic symbol table entry */
  char *name;
  int def;
  struct nlist *next;           /* next entry in chain */
};

EXTERN FILE *Objfil;            /* object file's file descriptor */
EXTERN char Obj_name[MAXBUF];
EXTERN FILE *Lstfil;            /* listing output file descriptor */
EXTERN char Lst_name[MAXBUF];
EXTERN char Sym_name[MAXBUF];
