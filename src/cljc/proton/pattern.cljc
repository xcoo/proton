(ns proton.pattern)

(def email-regex "^[a-zA-Z0-9.!#$&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")
(def email-pattern (re-pattern email-regex))

(def password-regex "^[!-~]+$")
(def password-pattern (re-pattern password-regex))

(defn valid-email?
  [mail]
  (not (nil? (re-matches email-pattern mail))))

(defn valid-password?
  [password]
  (not (nil? (re-matches password-pattern password))))
