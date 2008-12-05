/***************************************************************************
 *   The Motorola Freeware Assembler                              
 *         
 *   symtab.c ---
 *
 ***************************************************************************/

#include <string.h>
#include <stdio.h>

#include  "vars.h"
#include  "funcs.h"

/*
 *  Variables and defines local to this module.
 */

#define HASHSIZE 100            /* width of hash table */
static struct nlist *hashtab[HASHSIZE] = { 0 }; /* symbol table */

/*
 *  hash --- form hash value for string s
 *
 *  The hashing is a method of reducing the amount of
 *  things that you have to sort through by running an
 *  algorithm on the data to find out some sort of similarity
 *  between the different elements. In this case, the hashing
 *  routine adds up the values in the symbol name and keeps the 
 *  resulting number less than HASHSIZE. Then all of the items that
 *  'hash' down to that particular number will be in the 'bin' pointed
 *  to by hashtab[number]. This should yield reduced searches. 
 *  Incidentally, this was copied right out of K & R's book.
 */
int
hash (char *s)
{
  int hashval;

  for (hashval = 0; *s != NULL;)
    hashval += *s++;
  return (hashval % HASHSIZE);
}

/*
 *      install --- add a symbol to the symbol table
 */

int
install (char *str, int val)
{
  register struct nlist *np;
  int i;

  if (!alpha (*str)) {
    sprintf (errbuf, "Symbol name %s contains an illegal character.", str);
    error (errbuf);
    return (NO);
  }

  // EWE strlwr - make lower case qqq
  /* MWK removed for cross platform compatability 
   * If case sensitivity is really wanted to be avoided, it should be handled in the
   * line proccessing (procces_line()) where the same is done to instruction names.
   */
  
  //strlwr (str);

  if ((np = lookup (str)) != NULL) {
    if (Pass == 2) {
      if (np->def == val)
        return (YES);
      else {
        /*
           printf("label %s with pass#1 PC %d but pass #2 PC %d\n",str,np->def,val);
           printf("You cannot fix this. This is a bug in the program\n");
           printf("Where the PC between asm passes are now different.\n");
         */
        error ("Phasing error.");
        return (NO);
      }
    }
    sprintf (errbuf, "Label %s is defined more than once in program.", str);
    error (errbuf);
    return (NO);
  }
  /* enter new symbol */
  if (Debug != 0) {
    sprintf (errbuf, "Installing %s as %d\n", str, val);
    printf (errbuf);
  }
  /*
   *   please note that the structure and the location of the actual
   *   string containing the name are in 2 different places
   */
  np = (struct nlist *) alloc (sizeof (struct nlist));
  if (np == (struct nlist *) ERR) {
    error ("Symbol table full.");
    return (NO);
  }
  np->name = alloc (strlen (str) + 1);
  if (np->name == (char *) ERR) {
    error ("Symbol table full.");
    return (NO);
  }
  strcpy (np->name, str);
  np->def = val;

  /*
   *     step 1) hash (reduce) down the name so that it is reduced to
   *             a simple integer value (this will be shared with several
   *             other names for sure.
   *     step 2) put into the newly made structure (made in install()) the
   *             address which is stored in hashtab, which will be either
   *             zero (if nothing in 'bin') OR the starting address of the
   *             most recent symbol addition (which had this hash value). Put
   *             this value into the slot in the structure which points to the
   *             next available structure. (np->next).
   *     step 3) replace into the hastabl the address of this newly added symbol.
   *     
   *      In this way, the search is always from the most recent addition to the
   *      oldest addition. (within all elements which hash to the same value).
   */

  i = hash (np->name);
  np->next = hashtab[i];
  hashtab[i] = np;
  return (YES);
}

/*
 *  lookup --- find string in symbol table
 *
 *  Basically, hash down the name of the symbol we are searching for,
 *  then, grab the address to start searching the symbol table from 
 *  the hashtab and search until the 'next structure' entry is NULL.
 */

struct nlist *
lookup (char *name)
{
  struct nlist *np;
  char orig_name[80];
  char *peek = name;
  
  strcpy (orig_name, name);     // save this in it's original case for error reporting
  while ((*peek++ = mapdn(*peek)) != NULL);

  for (np = hashtab[hash (name)]; np != NULL; np = np->next) {
    if (strcmp (name, np->name) == 0) {
      Last_sym = np->def;
      return (np);
    }
  }
  Last_sym = 0;
  if (Pass == 2) {
    sprintf (errbuf, "Symbol %s is undefined.", orig_name);
    error (errbuf);
  }
  return (NULL);
}

/*
 *      mne_look --- mnemonic lookup
 *
 *  Return pointer to an oper structure if found.
 *  Searches both the machine mnemonic table and the pseudo table.
 *  both of these searches are simple binary searches. Notice that
 *  they are copied word for word from K & R.
 */

struct oper *
mne_look (char *str)
{
  struct oper *low, *high, *mid;
  int cond;

  /* Search machine mnemonics first */
  low = &table[0];
  high = &table[NMNE - 1];
  while (low <= high) {
    mid = low + (high - low) / 2;
    if ((cond = strcmp (str, mid->mnemonic)) < 0)
      high = mid - 1;
    else if (cond > 0)
      low = mid + 1;
    else
      return (mid);
  }

  /* Check for pseudo ops */
  low = &pseudo[0];
  high = &pseudo[Total_Pseudo - 1];
  while (low <= high) {
    mid = low + (high - low) / 2;
    if ((cond = strcmp (str, mid->mnemonic)) < 0)
      high = mid - 1;
    else if (cond > 0)
      low = mid + 1;
    else
      return (mid);
  }
  return (NULL);
}

/*
	psymtab - print symbol table to file
	symbols are sorted in alphabetical order

        author: Shu-Jen Chen
*/
void psymtab(char *filename)
{
	FILE *fp;

	struct nlist *np;
	int i;
	struct slist {
		struct nlist *np;
		struct slist *next;
	};

	struct slist *slist = NULL;
	struct slist *sp, *cp, *lp;
	int inserted;

	sp = (struct slist *) alloc (sizeof (struct slist));
	slist = sp;
	slist-> next = 0;

        if ((fp = fopen (filename, "w")) == NULL) {
        	sflag = 0;
	        sprintf(buffer, "Can't write to symbol file, '%s'", filename);
     		fatal(buffer);
                return;
	}

	/* We need to start the symbol table with a hex $0c to identify
           the file format to legacy programs */
        fputc(0x0c, fp);

	/* scrub the hash table to make a sorted symbol table */
	for(i = 0; i < HASHSIZE; i++) {  
		/* traverse the links */
		for (np = hashtab[i]; np != NULL; np = np->next) {
			sp = (struct slist *) alloc (sizeof (struct slist));
			if (sp == (struct slist *) ERR) {
				error ("Sorted symbol table full.");
				break;
			}
			if(slist->next == 0) {
				sp->np = np;
				sp->next = 0;
				slist->next = sp;
			} else {
				inserted = 0;
				/* look for proper place to insert the new symbol */
				for (lp = slist, cp = lp->next; cp != 0; cp = cp->next, lp = lp-> next) {
					if (strcmp(np->name, cp->np->name) < 0) {
						sp->np = np;
						sp->next = cp;
						lp->next = sp;
						inserted = 1;
						break;
					}
				}
				if (!inserted) {	/* append at the end of the list */
					sp->np = np;
					sp->next = 0;
					lp->next = sp;
				}
			}
		}
	}

	for(cp = slist->next; cp != 0; cp = cp->next)
		fprintf(fp, "%-10s %04x\n", cp->np->name, cp->np->def);

	fclose(fp);
}

