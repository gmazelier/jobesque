# jobesque

Jobesque is a simple Clojure job scheduler, built on top of [cron4j](http://www.sauronsoftware.it/projects/cron4j). The scheduling pattern is the same than the one used in crontab entries, on Unix-like operating systems.

[![Build Status](https://secure.travis-ci.org/gmazelier/jobesque.png)](https://secure.travis-ci.org/gmazelier/jobesque.png)

## Usage

To be documented.

### Artifact

With Leiningen:

    [jobesque "0.0.1-SNAPSHOT"]

With Maven:

    <dependency>
      <groupId>jobesque</groupId>
      <artifactId>jobesque</artifactId>
      <version>0.0.1-SNAPSHOT</version>
    </dependency>

### Example

```clojure
(require '[jobesque.core :as jobs])

; Initialize the scheduler
(jobs/initialize)

; Schedule a job every minute
(jobs/schedule "* * * * *" (fn [] (println "A simple task.")))

; And start the scheduler
(jobs/start)

; Do something else...

; Stop the scheduler when not needed anymore
(jobs/stop)
```

## See also

You can also try:

+ https://github.com/michaelklishin/quartzite

## License

Copyright (C) 2012 Gaylord Mazelier

Distributed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html), the same as Clojure.
