;;;; Copyright © 2011 José Pablo Fernández Silva
(ns leiningen.lobos
  (:use [leiningen.help :only (help-for)]
        [leiningen.core.eval :only [eval-in-project]]))

(defn migrate [project]
  (eval-in-project project
    `(do
       (lobos.config/open-global-when-necessary (assoc lobos.config/db-spec :unsafe true)) ; Establish a connection only when none exists (unsafe true)
       (binding [lobos.migration/*reload-migrations* false]
         (lobos.core/migrate)))
    '(require 'lobos.config 'lobos.core 'lobos.connectivity 'lobos.migration)))

(defn rollback [project]
  (eval-in-project project
    `(do
       (lobos.config/open-global-when-necessary (assoc lobos.config/db-spec :unsafe true)) ; Establish a connection only when none exists (unsafe true)
       (binding [lobos.migration/*reload-migrations* false]
         (lobos.core/rollback)))
    '(require 'lobos.config 'lobos.core 'lobos.connectivity)))

(defn lobos
  "Run Lobos commands."
  {:help-arglists '([migrate rollback])
   :subtasks [#'migrate #'rollback]}
  ([] (println (help-for "lobos")))
  ([project] (println (help-for "lobos")))
  ([project subtask]
    (case subtask
      "migrate" (leiningen.lobos/migrate project)
      "rollback" (leiningen.lobos/rollback project)
      (println (help-for "lobos")))))
