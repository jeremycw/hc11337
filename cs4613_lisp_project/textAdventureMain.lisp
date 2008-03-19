
(setf *objects* '(whiskey-bottle bucket frog chain))


;;changed
(setf *map* '((living-room (you are in the living-room of a wizards house. there is a wizard snoring loudly on the couch.)
                           ((west door garden)  
                           (upstairs stairway attic))
                           ((wizard (A drunken wizard asleep on the couch))))
              (garden (you are in a beautiful garden. there is a well in front of you.)
                      ((east door living-room))
                      ((well (A deep well with water in it. You cannot see the bottom.))))
              (attic (you are in the attic of the wizards house. there is a giant welding torch in the corner.)
                     ((downstairs stairway living-room))
                     ((torch (A welding torch useful for welding metal objects together))))))

(setf *object-locations* '((whiskey-bottle living-room)
                           (bucket living-room)
                           (chain garden)
                           (frog garden)))

(setf *location* 'living-room)

(defun describe-location (location map)
  (second (assoc location map)))

(defun describe-path (path)
  `(there is a ,(second path) going ,(first path) from here.))

;;new
(defun describe-thing (object location map)
  (let ((obj (assoc object (fourth (assoc location map)))))
  (cond (obj (second obj))
        (t '(There is nothing there.)))))

;;changed
(defun describe-paths (location map)
  (apply #'append (mapcar #'describe-path (third (assoc location map)))))

(defun is-at (obj loc obj-loc)
  (eq (second (assoc obj obj-loc)) loc))

(defun describe-floor (loc objs obj-loc)
  (apply #'append (mapcar (lambda (x)
                            `(you see a ,x on the floor.))
                          (remove-if-not (lambda (x)
                                           (is-at x loc obj-loc))
                                         objs))))

(defun look ()
  (append (describe-location *location* *map*)
          (describe-paths *location* *map*)
          (describe-floor *location* *objects* *object-locations*)))

;;changed
(defun walk-direction (direction)
  (let ((next (assoc direction (third (assoc *location* *map*)))))
  (cond (next (setf *location* (third next)) (look))
        (t '(you cant go that way.)))))

(defmacro defspel (&rest rest) `(defmacro ,@rest))

(defspel walk (direction)
  `(walk-direction ',direction))

;;new
(defspel examine (object)
  `(describe-thing ',object *location* *map*))

(defun pickup-object (object)
  (cond ((is-at object *location* *object-locations*) (push (list object 'body) *object-locations*) `(you are now carrying the ,object))
        (t '(you cannot get that.))))

;;new
(defun drop-object (object)
  (let ((have-it (member object (inventory))))
  (cond (have-it (push (list object *location*) *object-locations*) `(you just dropped the ,object))
        (t '(you dont have that.)))))

(defspel pickup (object)
  `(pickup-object ',object))

;;new
(defspel drop (object)
  `(drop-object ',object))

(defun inventory ()
  (remove-if-not (lambda (x)
                   (is-at x 'body *object-locations*))
                 *objects*))

(defun have (object)
  (member object (inventory)))

(defparameter *chain-welded* nil)

(defparameter *bucket-filled* nil)

(defspel game-action (command subj obj place &rest rest)
  `(defspel ,command (subject object)
     `(cond ((and (eq *location* ',',place)
                  (eq ',subject ',',subj)
                  (eq ',object ',',obj)
                  (have ',',subj))
             ,@',rest)
            (t '(i cant ,',command like that.)))))

(game-action weld chain bucket attic
             (cond ((and (have 'bucket) (setf *chain-welded* 't)) '(the chain is now securely welded to the bucket.))
                   (t '(you do not have a bucket.))))

(game-action dunk bucket well garden
             (cond (*chain-welded* (setf *bucket-filled* 't) '(the bucket is now full of water))
                   (t '(the water level is too low to reach.))))

(game-action splash bucket wizard living-room
             (cond ((not *bucket-filled*) '(the bucket has nothing in it.))
                   ((have 'frog) '(the wizard awakens and sees that you stole his frog. he is so upset he banishes you to the netherworlds- you lose! the end.))
                   (t '(the wizard awakens from his slumber and greets you warmly. he hands you the magic low-carb donut- you win! the end.))))