##----------------------------------------------------------------------------
# Created with genmake.pl v1.1 on Thu Mar 24 09:03:43 2005
# genmake.pl home: http://muquit.com/muquit/software/
# Copryright: GNU GPL (http://www.gnu.org/copyleft/gpl.html)
##----------------------------------------------------------------------------
rm=/bin/rm -f
CC=gcc
DEFS=  
INCLUDES=  -I.
LIBS=

DEFINES= $(INCLUDES) $(DEFS) -DSYS_UNIX=1
CFLAGS= -g $(DEFINES)

as11_OBJS = as.o do11.o eval.o fwdref.o pseudo.o symtab.o util.o 

as12_OBJS = as.o do12.o eval.o fwdref.o pseudo.o symtab.o util.o

.c:
	$(rm) $@
	$(CC) $(CFLAGS) -c $*.c

all: as11 as12

as11 : $(as11_OBJS)
	$(CC) $(CFLAGS) -o as11 $(as11_OBJS) $(LIBS)

as12: $(as12_OBJS)
	$(CC) $(CFLAGS) -o as12 $(as12_OBJS) $(LIBS)

clean:
	$(rm) $(as11_OBJS) $(as12_OBJS) as11 as12 core *~
