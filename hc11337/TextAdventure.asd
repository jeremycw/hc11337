;;;; 2008-03-19 12:52:23

(defpackage #:textadventure-asd
  (:use :cl :asdf))

(in-package :textadventure-asd)

(defsystem textadventure
  :name "textadventure"
  :version "0.1"
  :components ((:file "defpackage")
               (:file "main" :depends-on ("defpackage")))
  :depends-on ())
