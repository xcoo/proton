(ns proton.pattern
  "Commonly used regular expressions and those utilities")

;; email pattern

(def email-regex "Regexp string of email address "
  "^[a-zA-Z0-9.!#$&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")

(def email-pattern "Regexp object of email address."
  (re-pattern email-regex))

(defn valid-email?
  "Check if email address is valid format."
  [mail]
  (and mail
       (not (nil? (re-matches email-pattern mail)))))

;; password pattern

(def password-regex "Regexp string of valid password."
  "^[!-~]+$")

(def password-pattern "Regexp object of valid password."
  (re-pattern password-regex))

(defn valid-password?
  "Check if password is valid format where password contains lower case letters, upper case letters,
  numeric characters, and special characters.
  (i.e. from 0x21 to 0x7e in ascii code table)"
  [password]
  (and password
       (not (nil? (re-matches password-pattern password)))))

;; uuid pattern

(def uuid-regex "Regexp string of UUID."
  "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")

(def uuid-pattern "Regexp object of UUID."
  (re-pattern uuid-regex))

(defn valid-uuid?
  "Check if UUID string is valid format."
  [uuid-string]
  (and uuid-string
       (not (nil? (re-matches uuid-pattern uuid-string)))))
