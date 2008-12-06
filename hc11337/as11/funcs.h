/***************************************************************************
 *   The Motorola Freeware Assembler                              
 *         
 *   funcs.h --- Contains all the function prototypes
 *
 ***************************************************************************/


/* as.c */
void initialize (void);
void Lprintf (char *str);
void Bprintf (char *str);
void Cprintf (char *str);
void process (void);
void re_init (void);
void make_pass (void);
void pop_include (void);
void push_include (char *);
int parse_line (void);
int is_inside_include (void);
int getaline (void);
char *change_file_ext(char *filename, char *ext);

/* eval.c */
int eval (void);
int get_term (void);
int is_op (void);

/* fwdref.c */
void fwdinit (void);
void fwdreinit (void);
void fwdmark (void);
void fwdnext (void);

/* pseudo.c */
int do_pseudo (int op);
int pseudo_init (void);

/* util.c */
void *alloc (int nbytes);
void warn (char *s);
void error (char *s);
void fatal (char *s);
int white (char c);
int alphan (char c);
int alpha (char c);
int hibyte (int i);
int lobyte (int i);
char mapdn (char c);
int any (char c, char *str);
void print_line (void);
void hexout (int byte);
int f_record (void);
int emit (int byte);
void eword (int wd);
char *skip_white (char *ptr);
int delim (char c);
int s0_record (void);
char *get_start_of_next_word (char *);
char *word_to_string (char *, char *, char *);
struct oper *mne_look ();
char *parse_filename (char *string, char *ptr1);
char *FGETS( char *, int , register FILE *);
int is_index_reg (char *);
int suck_out_commas (void);
int isoperator(char );

/*  symtab.c */
struct oper *mne_look (char *str);
int install (char *s, int pc);
struct nlist *lookup (char *);
void psymtab(char *filename);
