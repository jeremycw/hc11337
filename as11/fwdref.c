/***************************************************************************
 *   The Motorola Freeware Assembler                              
 *         
 *   fwdref.c --- In-memory version of forward ref handler.
 *
 ***************************************************************************/
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#if defined(applec) || defined(__SC__)
#include <time.h>
#endif
#if !defined(applec) && !defined(__SC__)
#include <malloc.h>
#endif

#include  "vars.h"
#include  "funcs.h"

/*
 *  variables and #defines local to this module
 */

#define FCHUNK  5               /* allocate forward refs this many at a time */

struct fchain {
  struct fchain *f_next;        /* next chunk of forward refs */
  struct fref *f_base;          /* pointer to chunk */
  struct fref *f_ref;           /* ptr to real forward refs */
};

struct fref {
  int f_fno;                    /* file number */
  int f_line;                   /* line number */
};

struct fchain Firstf = { NULL, NULL, NULL };
struct fchain *Fcur = &Firstf;

/*
 *      fwdinit --- initialize forward ref array
 */

void
fwdinit ()
{
}


/*
 *      fwdreinit --- reinitialize forward ref file
 */

void
fwdreinit ()
{
  Cfn = Line_num = 0;
  fwdmark ();
  Fcur = &Firstf;
  fwdnext ();
}


/*
 *      fwdmark --- mark current file/line as containing a forward ref
 *
 *      The biggest thing to remember here is that this routine does work.
 *      I don't know why the blocks are allocated 5 forward references at 
 *      a time since alloc() gets extra memory from the OS anyway. This is
 *      also less efficient in terms of memory, but, if it is not broken do
 *      not fix it! Here is the structure of this stuff anyway...
 *
 *       This is what the pointer structure for 10 forward references looks like
 *
 *      address      f_next         f_base      f_ref
 *      &Firstf      &Firstf+X1     NULL        NULL     (first record always)
 *      &Firstf+X1   &Firstf+X2     &happy      &Firstf+X1 + (0,1,2,3, or 4)
 *      &Firstf+X2   NULL           &happy+Y1   &Firstf+X2 + (0,1,2,3, or 4)
 * 
 *       This is what the actual forward reference data might look like
 *
 *      ---> increasing address values
 *      address f_no/Line_no f_no/Line_no f_no/Line_no f_no/Line_no f_no/Line_no   
 *      &happy    1/22          1/21         1/8          1/7          0/0
 *      &happy+Y1 2/22          2/21         2/8          2/7          1/25
 *
 *      i.e. the function allocates  memory in 5 * (fref) blocks and then 
 *       stores those addresses in another structure which just contains the
 *       pointers to the right blocks. Within each of those blocks the data is
 *       stored in reverse order; most recent has lower address. Also FirstF
 *       is really a null entry because it only points to the next valid block
 *       not to a block of its own.
 */

void
fwdmark ()
{
  struct fref *f;
  struct fchain *chain;

  f = Fcur->f_ref;
  if (f == Fcur->f_base) {      /* this one is full */
    chain = (struct fchain *) malloc (sizeof (struct fchain));
    if (chain == (struct fchain *) ERR)
      fatal ("Out of memory in fwdmark()");

    chain->f_base = (struct fref *) malloc (sizeof (struct fref) * FCHUNK);
    if (chain->f_base == (struct fref *) ERR)
      fatal ("Out of memory");
    chain->f_next = NULL;
    chain->f_ref = chain->f_base + FCHUNK;
    f = chain->f_ref;
    Fcur->f_next = chain;
    Fcur = chain;
  }
  f--;
  f->f_fno = Cfn;
  f->f_line = Line_num;
  Fcur->f_ref = f;
}


/*
 *      fwdnext --- get next forward ref
 */

void
fwdnext ()
{
  register struct fref *f = Fcur->f_ref;

  if (f == Fcur->f_base) {
    Fcur = Fcur->f_next;
    f = Fcur->f_base + FCHUNK;
  }
  f--;
  Ffn = f->f_fno;
  F_ref = f->f_line;
  Fcur->f_ref = f;
  if (Debug != 0) {
    sprintf (errbuf, "Next Fwd ref: %d,%d\n", Ffn, F_ref);
    printf (errbuf);
  }
}
