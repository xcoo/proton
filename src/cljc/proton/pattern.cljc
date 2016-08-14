(ns proton.pattern)

;; email pattern

(def email-regex "^[a-zA-Z0-9.!#$&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")
(def email-pattern (re-pattern email-regex))

(defn valid-email?
  [mail]
  (and mail
       (not (nil? (re-matches email-pattern mail)))))

;; password pattern

(def password-regex "^[!-~]+$")
(def password-pattern (re-pattern password-regex))

(defn valid-password?
  [password]
  (and password
       (not (nil? (re-matches password-pattern password)))))

;; uuid pattern

(def uuid-regex "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
(def uuid-pattern (re-pattern uuid-regex))

(defn valid-uuid?
  [uuid-string]
  (and uuid-string
       (not (nil? (re-matches uuid-pattern uuid-string)))))
