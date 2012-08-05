# jobesque

Jobesque is a simple Clojure job scheduler, built on top of [cron4j](http://www.sauronsoftware.it/projects/cron4j). The
scheduling pattern is the same than the one used in crontab entries, on Unix-like operating systems.

[![Build Status](https://secure.travis-ci.org/gmazelier/jobesque.png)](https://secure.travis-ci.org/gmazelier/jobesque.png)

## Usage

Jobesque goal is to be a dead easy job scheduler solution for Clojure.

### Artifact

Artifacts are released to [Clojars](https://clojars.org/jobesque). It's a default repository for Leiningen but you can
use it with other build tools, like Maven:

    <repository>
      <id>clojars.org</id>
      <url>http://clojars.org/repo</url>
    </repository>

With Leiningen:

    [jobesque "0.0.1"]

With Maven:

    <dependency>
      <groupId>jobesque</groupId>
      <artifactId>jobesque</artifactId>
      <version>0.0.1</version>
    </dependency>

### Quickstart

As you can see in the example below, only four functions are required to work with jobesque:

+ `initialize` allows you to create a new scheduler instance;
+ `schedule` tells the scheduler *what* to do, and *when*;
+ `start` starts the scheduler;
+ `stop` stops the scheduler.

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

### The scheduling pattern

A pattern is divided in five values, in the given order :

+ **minutes**, allowed values range from 0 to 59;
+ **hours**, allowed values range from 0 to 23;
+ **days of month**, values range from 1 to 31, with a special value `L` which means the last day ot the month;
+ **months**, values range from 1 to 12, with aliases like `jan` for January and so on;
+ **days of week**, values range from 0 (Sunday) to 7 (Sunday again), with aliases like `sun` for Sunday and so on.

#### Special characters

+ **wildcard**, to define every allowed values;
+ **comma**, to define a list a possible values;
+ **minus**, to define values interval;
+ **slash**, to define multiple values in a range with a step;
+ **pipe**, to combine multiple patterns in one.

#### Examples

+ `* * * * *`, every minute;
+ `*/5 * * * *`, every five minutes;
+ `0-15/5 * * * *`, every five minutes the first quarter hour;
+ `* 12 * * mon`, every minute of noon on monday;
+ `0 12 * * 1,2,3,4,5`, from Monday to Friday at noon;
+ `0 12 * * 1-5`, the same pattern than above;
+ `0 0 * * *|1 1 * * *|2 2 * * *`, every day at 00:00, 01:01 and 02:02.

### Jobs
To be documented.

#### List
To be documented.

#### Job informations
To be documented.

#### Schedule
To be documented.

#### Deschedule
To be documented.

#### Reschedule
To be documented.

## Roadmap

### 0.0.1
+ ~~Schedule a job~~

### 0.0.2
+ ~~List scheduled jobs~~
+ ~~Reschedule a job~~
+ ~~Deschedule a job~~

### 0.1.0
+ Extends job properties
+ Improve documentation
+ Improve tests

### 0.1.1
+ Schedule jobs from a file

### 0.1.2
+ Jobs persistence

## See also

You can also try:

+ https://github.com/michaelklishin/quartzite

## License

Copyright (C) 2012 Gaylord Mazelier

Distributed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html), the same as Clojure.
